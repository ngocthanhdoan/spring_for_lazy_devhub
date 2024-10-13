package com.lazy.devhub;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.Entity;

public class TemplateProcessor_bak {

    private final Configuration configuration;

    // Constructor that sets up the template directory and encoding
    public TemplateProcessor_bak() throws IOException {
        configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setDirectoryForTemplateLoading(new File("./src/main/java/com/lazy/devhub/templates/"));
        configuration.setDefaultEncoding("UTF-8");
    }

    // Method to generate a file based on a template and data model
    public void generateFile(String templateName, Map<String, Object> dataModel, String outputFilePath) {
        try {
            Template template = configuration.getTemplate(templateName);
            File outputFile = new File(outputFilePath);
            outputFile.getParentFile().mkdirs(); // Ensure parent directories exist

            try (Writer fileWriter = new FileWriter(outputFile)) {
                template.process(dataModel, fileWriter);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    // Properly configured Reflections scanner for classes annotated with @Entity
    public Set<Class<?>> scanForEntities(String packageName) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(Scanners.TypesAnnotated));  // Scanning for annotated classes

        return reflections.getTypesAnnotatedWith(Entity.class); // Get classes annotated with @Entity
    }

    // Process a single entity
    public void processEntity(Class<?> entityClass) throws IOException, TemplateException {
        String entityName = entityClass.getSimpleName();
        String entityPackage = entityClass.getPackageName();

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("entityName", entityName);
        dataModel.put("entityPackage", entityPackage);

        // Detect fields and relationships
        List<Map<String, Object>> fields = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            Map<String, Object> fieldInfo = new HashMap<>();
            fieldInfo.put("name", field.getName());
            fieldInfo.put("isRelationship", isRelationship(field)); // Detect relationships
            fields.add(fieldInfo);
        }
        dataModel.put("fields", fields);

        // Generate Repository
        dataModel.put("packageName", "com.lazy.devhub.repository");
        generateFile("repositoryTemplate.ftl", dataModel, "output/repository/" + entityName + "Repository.java");

        // Generate Controller
        dataModel.put("packageName", "com.lazy.devhub.controller");
        generateFile("controllerTemplate.ftl", dataModel, "output/controller/" + entityName + "Controller.java");

        // Generate HTML Form
        generateFile("htmlFormTemplate.ftl", dataModel, "output/html/" + entityName + "Form.html");
    }

    // Check if a field has a relationship annotation (e.g., @ManyToOne, @OneToMany)
    private boolean isRelationship(Field field) {
        return Arrays.stream(field.getAnnotations())
                .anyMatch(annotation -> annotation.annotationType().getSimpleName().equals("ManyToOne")
                || annotation.annotationType().getSimpleName().equals("OneToMany")
                || annotation.annotationType().getSimpleName().equals("ManyToMany"));
    }

    public static void main(String[] args) throws IOException, TemplateException {
        TemplateProcessor_bak processor = new TemplateProcessor_bak();

        // Specify the package to scan for entities
        String entityPackage = "com.lazy.devhub.entity";
        Set<Class<?>> entityClasses = processor.scanForEntities(entityPackage);

        // Process each entity
        for (Class<?> entityClass : entityClasses) {
            processor.processEntity(entityClass);
        }
    }
}
