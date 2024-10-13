package com.lazy.devhub.chatbox;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DirectoryTreePretty {

    // Hàm duyệt cây thư mục và in ra theo định dạng đẹp mắt
    public void printDirectoryTree(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);

        if (Files.isDirectory(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                private int indent = 0;

                // In ra cây thư mục
                private void printIndented(String name, boolean isDirectory) {
                    String prefix = isDirectory ? "|-- " : "|   ";
                    System.out.println(" ".repeat(indent) + prefix + name);
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (indent == 0) {
                        System.out.println(dir.toAbsolutePath().toString()); // In thư mục gốc
                    } else {
                        printIndented(dir.getFileName().toString(), true); // In thư mục con
                    }
                    indent += 4; // Tăng độ lùi vào để hiển thị cấp độ sâu hơn
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    indent -= 4; // Giảm độ lùi khi thoát khỏi thư mục con
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    printIndented(file.getFileName().toString(), false); // In file
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            System.out.println(directoryPath + " is not a valid directory.");
        }
    }

    public static void main(String[] args) {
        DirectoryTreePretty directoryTreePretty = new DirectoryTreePretty();

        try {
            // Duyệt cây thư mục và in ra cây thư mục đẹp mắt
            directoryTreePretty.printDirectoryTree("./");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

