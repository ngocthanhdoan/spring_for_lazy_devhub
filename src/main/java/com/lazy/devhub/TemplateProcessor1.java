package com.lazy.devhub;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.*;

import jakarta.persistence.Entity;

public class TemplateProcessor1 {

    private final Configuration configuration;

    // Constructor that sets up the template directory and encoding
    public TemplateProcessor1() throws IOException {
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

            try (Writer fileWriter = new java.io.FileWriter(outputFile)) {
                template.process(dataModel, fileWriter);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    // Method to generate content instead of writing to a file, returning a map of filenames to content
    public Map<String, String> generateFilesContent(Class<?> entityClass) {
        Map<String, String> filesContent = new HashMap<>();
        try {
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

            // Set the package name for the repository, controller, and HTML files
            dataModel.put("packageName", "com.lazy.devhub.repository");
            filesContent.put("repository/" + entityName + "Repository.java", processTemplate("repositoryTemplate.ftl", dataModel));

            // Update the package name for the controller
            dataModel.put("packageName", "com.lazy.devhub.controller");
            filesContent.put("controller/" + entityName + "Controller.java", processTemplate("controllerTemplate.ftl", dataModel));

            // No package needed for the HTML, so you can skip setting packageName here
            filesContent.put("html/" + entityName + "Form.html", processTemplate("htmlFormTemplate.ftl", dataModel));
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return filesContent;
    }

    // Process a template and return the content as a string
    private String processTemplate(String templateName, Map<String, Object> dataModel) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templateName);
        try (StringWriter stringWriter = new StringWriter()) {
            template.process(dataModel, stringWriter);
            return stringWriter.toString();
        }
    }

    // Properly configured Reflections scanner for classes annotated with @Entity
    public Set<Class<?>> scanForEntities(String packageName) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(Scanners.TypesAnnotated));  // Scanning for annotated classes

        return reflections.getTypesAnnotatedWith(Entity.class); // Get classes annotated with @Entity
    }

    // Check if a field has a relationship annotation (e.g., @ManyToOne, @OneToMany)
    private boolean isRelationship(Field field) {
        return Arrays.stream(field.getAnnotations())
                .anyMatch(annotation -> annotation.annotationType().getSimpleName().equals("ManyToOne")
                || annotation.annotationType().getSimpleName().equals("OneToMany")
                || annotation.annotationType().getSimpleName().equals("ManyToMany"));
    }

    public static void main(String[] args) throws IOException, TemplateException {
        TemplateProcessor1 processor = new TemplateProcessor1();

        // Specify the package to scan for entities
        String entityPackage = "com.lazy.devhub.entity";
        Set<Class<?>> entityClasses = processor.scanForEntities(entityPackage);

        // Process each entity and print the files content map
        for (Class<?> entityClass : entityClasses) {
            Map<String, String> filesContent = processor.generateFilesContent(entityClass);
            filesContent.forEach((fileName, content) -> {
                System.out.println("File: " + fileName);
                System.out.println(content);
                System.out.println("-----------------------------------");
            });
        }
    }
}
