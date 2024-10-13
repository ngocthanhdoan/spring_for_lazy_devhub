package com.lazy.devhub;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TemplateFileProcessor {

    // Method to get list of template file names from a directory
    public List<String> getTemplateFileNames(String templateDirectory) {
        File directory = new File(templateDirectory);

        List<String> templateFileNames = new ArrayList<>();

        // Check if the directory exists and is a directory
        if (directory.exists() && directory.isDirectory()) {
            // Get all files in the directory
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".ftl"));

            if (files != null) {
                // Add each file's name to the list
                for (File file : files) {
                    templateFileNames.add(file.getName());
                }
            }
        }

        return templateFileNames;
    }

    public static void main(String[] args) {
        TemplateFileProcessor processor = new TemplateFileProcessor();

        // Define the template directory path
        String templateDirectory = "./src/main/java/com/lazy/devhub/templates/";

        // Get the list of template file names
        List<String> templateFileNames = processor.getTemplateFileNames(templateDirectory);

        // Print the template file names
        for (String fileName : templateFileNames) {
            System.out.println("Template File: " + fileName);
        }
    }
}
