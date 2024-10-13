package com.lazy.devhub.chatbox;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private final Path directoryPath;

    // Constructor nhận đường dẫn tới thư mục
    public FileManager(String directoryPath) {
        this.directoryPath = Paths.get(directoryPath);
    }

    // Hàm kiểm tra nếu thư mục hợp lệ
    public boolean isValidDirectory() {
        return Files.isDirectory(directoryPath);
    }

    // Lấy danh sách file theo định dạng hình ảnh (jpg, png, gif, bmp)
    public List<File> getImageFiles() throws IOException {
        List<File> imageFiles = new ArrayList<>();
        
        // Duyệt qua các file trong thư mục
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath, "*.{jpg,jpeg,png,gif,bmp}")) {
            for (Path entry : stream) {
                imageFiles.add(entry.toFile());
            }
        } catch (DirectoryIteratorException e) {
            throw e.getCause();
        }

        return imageFiles;
    }

    // Lấy một file theo tên
    public File getFileByName(String fileName) {
        Path filePath = directoryPath.resolve(fileName);
        File file = filePath.toFile();

        if (file.exists() && !file.isDirectory()) {
            return file;
        }
        return null;
    }

    // Lấy một file ngẫu nhiên (Random)
    public File getRandomFile() throws IOException {
        List<File> files = getImageFiles();
        
        if (files.isEmpty()) {
            return null; // Không có file nào trong thư mục
        }
        
        // Lấy ngẫu nhiên một file từ danh sách
        int randomIndex = (int) (Math.random() * files.size());
        return files.get(randomIndex);
    }

    // Lấy nhiều file ngẫu nhiên (Random)
    public List<File> getRandomFiles(int numberOfFiles) throws IOException {
        List<File> files = getImageFiles();
        
        if (files.size() < numberOfFiles) {
            return files; // Trả về toàn bộ file nếu số lượng yêu cầu lớn hơn
        }

        List<File> randomFiles = new ArrayList<>();
        while (randomFiles.size() < numberOfFiles) {
            File randomFile = getRandomFile();
            if (!randomFiles.contains(randomFile)) {
                randomFiles.add(randomFile);
            }
        }

        return randomFiles;
    }

    // Lấy tất cả file trong thư mục
    public List<File> getAllFiles() throws IOException {
        List<File> allFiles = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {
            for (Path entry : stream) {
                File file = entry.toFile();
                if (!file.isDirectory()) {
                    allFiles.add(file);
                }
            }
        }

        return allFiles;
    }
}
