package com.data_conversion.resume_to_excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DuplicateDetector {

    public static void main(String[] args) {
        String dataDirPath = "C:\\Users\\ELCOT\\Documents\\resume_collections"; // Replace with the actual directory path
        File dataDir = new File(dataDirPath);
        File[] files = dataDir.listFiles();

        if (files == null) {
            System.out.println("Invalid directory path or directory is empty.");
            return;
        }

        List<List<String>> duplicateFiles = new ArrayList<>();

        for (File fileX : files) {
            boolean isDuplicate = false;

            for (List<String> classList : duplicateFiles) {
                // Comparing files having the same content using custom method
                if (areFilesEqual(fileX, new File(dataDir, classList.get(0)))) {
                    classList.add(fileX.getName());
                    isDuplicate = true;
                    break;
                }
            }

            if (!isDuplicate) {
                List<String> newClass = new ArrayList<>();
                newClass.add(fileX.getName());
                duplicateFiles.add(newClass);
            }
        }

        // Print results
        for (List<String> classList : duplicateFiles) {
            System.out.println(classList);
        }
    }

    // Custom method to compare two files for equality
    private static boolean areFilesEqual(File file1, File file2) {
        if (file1.length() != file2.length()) {
            return false;
        }

        try {
            byte[] file1Bytes = java.nio.file.Files.readAllBytes(file1.toPath());
            byte[] file2Bytes = java.nio.file.Files.readAllBytes(file2.toPath());
            for (int i = 0; i < file1Bytes.length; i++) {
                if (file1Bytes[i] != file2Bytes[i]) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
