package com.lazy.devhub;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplatePropertiesProcessor {

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

    // Method to generate the application.properties file
    public void generatePropertiesFile(Map<String, Object> dataModel, String outputFilePath) {
        try {
            Template template = configuration.getTemplate("applicationTemplate.ftl");
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
        TemplatePropertiesProcessor processor = new TemplatePropertiesProcessor();

        // Data model for the application.properties
        Map<String, Object> dataModel = new HashMap<>();

        // Define dynamic properties (key-value pairs)
        Map<String, String> properties = new HashMap<>();
        properties.put("server.port", "8080");
        properties.put("spring.datasource.url", "jdbc:mysql://localhost:3306/mydb");
        properties.put("spring.datasource.username", "root");
        properties.put("spring.datasource.password", "password");
        properties.put("spring.jpa.hibernate.ddl-auto", "update");

        // Add properties to data model
        dataModel.put("properties", properties);

        // Generate the application.properties file
        processor.generatePropertiesFile(dataModel, "output/application.properties");
    }
}
