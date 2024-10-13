package com.lazy.devhub;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplatePOMProcessor {

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
    // Method to generate the pom.xml file

    public void generatePomFile(Map<String, Object> dataModel, String outputFilePath) {
        try {
            Template template = configuration.getTemplate("pomTemplate.ftl");
            File outputFile = new File(outputFilePath);
            outputFile.getParentFile().mkdirs(); // Ensure parent directories exist

            try (Writer fileWriter = new FileWriter(outputFile)) {
                template.process(dataModel, fileWriter);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        TemplatePOMProcessor processor = new TemplatePOMProcessor();

        // Data model for the pom.xml
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("springBootVersion", "3.3.2");
        dataModel.put("groupId", "io.ptit.hcm");
        dataModel.put("artifactId", "exams");
        dataModel.put("version", "0.0.1-SNAPSHOT");
        dataModel.put("projectName", "exams");
        dataModel.put("javaVersion", "17");

        // Define dynamic dependencies
        List<Map<String, String>> dependencies = new ArrayList<>();

        // Dependency 1
        Map<String, String> dependency1 = new HashMap<>();
        dependency1.put("groupId", "org.springframework.boot");
        dependency1.put("artifactId", "spring-boot-starter-web");
        dependencies.add(dependency1);

        // Dependency 2
        Map<String, String> dependency2 = new HashMap<>();
        dependency2.put("groupId", "com.mysql");
        dependency2.put("artifactId", "mysql-connector-j");
        dependency2.put("scope", "runtime");
        dependencies.add(dependency2);

        // Dependency 3
        Map<String, String> dependency3 = new HashMap<>();
        dependency3.put("groupId", "org.springframework.boot");
        dependency3.put("artifactId", "spring-boot-starter-thymeleaf");
        dependencies.add(dependency3);

        // Add dependencies list to the data model
        dataModel.put("dependencies", dependencies);

        // Generate the pom.xml file
        processor.generatePomFile(dataModel, "output/pom.xml");
    }
}
