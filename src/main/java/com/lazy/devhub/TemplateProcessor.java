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

public class TemplateProcessor {

    private static final Configuration configuration;

    // Static initializer block to set up the configuration
    static {
        try {
            configuration = new Configuration(Configuration.VERSION_2_3_30);
            configuration.setDirectoryForTemplateLoading(new File("./src/main/java/com/lazy/devhub/templates/"));
            configuration.setDefaultEncoding("UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Failed to set up template configuration", e);
        }
    }

    // Static method to generate a file based on a template and data model
    public static void generateFile(String templateName, Map<String, Object> dataModel, String outputFilePath) {
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
    public static Set<Class<?>> scanForEntities(String packageName) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(Scanners.TypesAnnotated));  // Scanning for annotated classes

        return reflections.getTypesAnnotatedWith(Entity.class); // Get classes annotated with @Entity
    }

    // Process a single entity
    public static void processEntity(Class<?> entityClass, String repositoryPackage, String controllerPackage, String htmlOutputPath) throws IOException, TemplateException {
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
        if (repositoryPackage != null || !repositoryPackage.equals("")) {
            // Generate Repository
            dataModel.put("packageName", repositoryPackage);
            generateFile("repositoryTemplate.ftl", dataModel, htmlOutputPath + "/repository/" + entityName + "Repository.java");
        }
        if (controllerPackage != null || !controllerPackage.equals("")) {
            // Generate Controller
            dataModel.put("packageName", controllerPackage);
            generateFile("controllerTemplate.ftl", dataModel, htmlOutputPath + "/controller/" + entityName + "Controller.java");
        }

        // Generate HTML Form
        generateFile("htmlFormTemplate.ftl", dataModel, htmlOutputPath + "/html/" + entityName + "Form.html");
    }

    // Check if a field has a relationship annotation (e.g., @ManyToOne, @OneToMany)
    private static boolean isRelationship(Field field) {
        return Arrays.stream(field.getAnnotations())
                .anyMatch(annotation -> annotation.annotationType().getSimpleName().equals("ManyToOne")
                || annotation.annotationType().getSimpleName().equals("OneToMany")
                || annotation.annotationType().getSimpleName().equals("ManyToMany"));
    }

    // Main method to run the processing with dynamic package configurations
    public static void generateFilesForEntities(String entityPackage, String repositoryPackage, String controllerPackage, String outputPath) throws IOException, TemplateException {
        Set<Class<?>> entityClasses = scanForEntities(entityPackage);

        // Process each entity
        for (Class<?> entityClass : entityClasses) {
            processEntity(entityClass, repositoryPackage, controllerPackage, outputPath);
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        try {
            // Specify the package to scan for entities and output directories
            String entityPackage = "com.lazy.devhub.entity";
            String repositoryPackage = "com.lazy.devhub";
            String controllerPackage = "com.lazy.devhub";
            String outputPath = "output"; // Set the output path dynamically

            generateFilesForEntities(entityPackage, repositoryPackage, controllerPackage, outputPath);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
