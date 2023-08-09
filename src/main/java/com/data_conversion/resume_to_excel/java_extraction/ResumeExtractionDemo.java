package com.data_conversion.resume_to_excel.java_extraction;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javax.ws.rs.core.Link;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeExtractionDemo {

    public static void main(String[] args) {

        String folderPath = "C:\\Users\\ELCOT\\Documents\\very_latest_bulk_resumes";

        LinkedHashSet<String> names = new LinkedHashSet<>();
        List<String> emails = new ArrayList<>();
//        List<String> phoneNumbers = new ArrayList<>();
        List<List<String>> duplicateFiles = new ArrayList<>();

        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        Workbook workbook = new XSSFWorkbook();

        // Create a sheet in the workbook
        Sheet sheet = workbook.createSheet("Resume Data");

        // Add headers to the sheet
//        String[] headers = {"Name", "Email", "Phone"};
        String[] headers = {"Name", "Email"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        if (files != null) {
            for (File file : files) {
                boolean isDuplicate = false;
                for (List<String> classList : duplicateFiles) {
                    // Comparing files having the same content using custom method
                    if (areFilesEqual(file, new File(folderPath, classList.get(0)))) {
                        classList.add(file.getName());
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    List<String> newClass = new ArrayList<>();
                    newClass.add(file.getName());
                    duplicateFiles.add(newClass);
                    if (file.getName().endsWith(".pdf")) {
                        long fileSizeInBytes = file.length();
                        long fileSizeInKB = fileSizeInBytes / 1024; // Convert bytes to kilobytes
                        long fileSizeInMB = fileSizeInKB / 1024;    // Convert kilobytes to megabytes

                        if (fileSizeInMB <= 1) {
                            if (containsImages(file)) {
                                extractFromImagePDF(file, names, emails);
                            } else {
                                extractFromPDF(file, names, emails);
                            }
                        } else {
                            System.out.println("PDF file size exceeds 1 MB limit.");
                        }
                    }
                    if (file.getName().endsWith(".docx")) {
                        long fileSizeInBytes = file.length();
                        long fileSizeInKB = fileSizeInBytes / 1024; // Convert bytes to kilobytes
                        long fileSizeInMB = fileSizeInKB / 1024;    // Convert kilobytes to megabytes

                        if (fileSizeInMB <= 1.5) {
                            extractFromDocx(file, names, emails);
                        } else {
                            System.out.println("Document file size exceeds 1.5 MB limit.");
                        }

                    }
                    if (file.getName().endsWith(".doc")) {
                        long fileSizeInBytes = file.length();
                        long fileSizeInKB = fileSizeInBytes / 1024; // Convert bytes to kilobytes
                        long fileSizeInMB = fileSizeInKB / 1024;    // Convert kilobytes to megabytes

                        if (fileSizeInMB <= 1.5) {
                            extractFromDoc(file, names, emails);
                        } else {
                            System.out.println("Document file size exceeds 1.5 MB limit.");
                        }
                    }
                }
            }
        }

        System.out.println("Names:");
        for (String name : names) {
            System.out.println(name);
        }

        System.out.println("Emails:");
        for (String email : emails) {
            System.out.println(email);
        }

//        System.out.println("Phone Numbers:");
//        for (String phoneNumber : phoneNumbers) {
//            System.out.println(phoneNumber);
//        }

        List<Integer> indexesToDelete = new ArrayList<>();

        for (int i = 0; i < emails.size(); i++) {
            String currentEmail = emails.get(i);

            // Check if the email is "email not found"
            if ("Email Not Found".equals(currentEmail)) {
                indexesToDelete.add(i);
            }
        }

        List<String> namesList = new ArrayList<>(names);

        // Remove elements at the corresponding indexes from all three lists
        for (int i = indexesToDelete.size() - 1; i >= 0; i--) {
            int indexToDelete = indexesToDelete.get(i);
            if (indexToDelete >= 0 && indexToDelete < emails.size()) {
                emails.remove(indexToDelete);
                namesList.remove(indexToDelete);
                // phoneNumbers.remove(indexToDelete);
            } else {
                System.out.println("Index " + indexToDelete + " is out of bounds.");
            }
        }

        names = new LinkedHashSet<>(namesList);
        String[] sNames = names.toArray(new String[0]);
        String[] sEmails = emails.toArray(new String[0]);
//        String[] sPhoneNumbers = phoneNumbers.toArray(new String[0]);

        int lastRowNum = sheet.getLastRowNum();

        int dataSize = names.size();

        for(int i=0; i<dataSize;i++){
            Row newRow = sheet.createRow(lastRowNum+1+i);

            Cell nameCell = newRow.createCell(0);
            nameCell.setCellValue(sNames[i]);

            Cell emailCell = newRow.createCell(1);
            emailCell.setCellValue(sEmails[i]);

//            Cell phoneCell = newRow.createCell(2);
//            phoneCell.setCellValue(sPhoneNumbers[i]);

        }

        try {
            String excelFilePath = "C:\\Users\\ELCOT\\Downloads\\resume_extraction_latest.xlsx";
            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            System.out.println("Data written to Excel successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void extractFromPDF(File file, LinkedHashSet<String> names, List<String> emails) {

        boolean phoneNumberFound = false;
        boolean emailFound = false;

        HashSet<String> uniquePhoneNumbersSet = new HashSet<>();
        HashSet<String> uniqueEmailsSet = new HashSet<>();


        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            Pattern namePattern = Pattern.compile("(?i)\\b([A-Z][a-z]+(?: [A-Z][a-z]+)?)\\b");


            Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
            Matcher emailMatcher = emailPattern.matcher(text);


//            Pattern phonePattern = Pattern.compile("(?:(?:\\+91)|(?:0))?[789]\\d{9}");
            Pattern phonePattern = Pattern.compile("(?:(?:\\+91)|(?:0)|(?:\\+65))?[789]\\d{9}");
            Matcher phoneMatcher = phonePattern.matcher(text);


            String regex =  "foundit Profile_ ([^.]+)";
            String regex2 = "([A-Za-z]+[A-Za-z]+)";
            String regex3 =  "Naukri_(\\w+)";
            Pattern pattern = Pattern.compile(regex);
            Pattern pattern2 = Pattern.compile(regex2);
            Pattern pattern3 = Pattern.compile(regex3);
            System.out.println("FILE++++++++++++++++"+file.getName());
            Matcher fileNameMatcher = pattern.matcher(file.getName());
            Matcher fileNameMatcher2 = pattern2.matcher(file.getName());
            Matcher fileNameMatcher3 = pattern3.matcher(file.getName());



            if (fileNameMatcher.find()) {
                String fullName = fileNameMatcher.group(1);
                Matcher nameMatcher = namePattern.matcher(text);
                if (nameMatcher.find()) {
                    String name = nameMatcher.group(1).trim();
                    List<String> matcherGroupResult = List.of(name);
                    if (!matcherGroupResult.contains(fullName)) {
                        String convertedFullName = convertToFullName(fullName);
                        names.add(convertedFullName);
                    }
                }
            }

            if (!fileNameMatcher.find() && fileNameMatcher2.find()) {
                String fullName = fileNameMatcher2.group(1);
                if(!fullName.contains("Naukri")) {
                    Matcher nameMatcher = namePattern.matcher(text);
                    if (nameMatcher.find()) {
                        String name = nameMatcher.group(1).trim();
                        List<String> matcherGroupResult = List.of(name);
                        if (!matcherGroupResult.contains(fullName) && !fullName.contains("foundit")) {
                            String convertedFullName = convertToFullName(fullName);
                            names.add(convertedFullName);
                        }
                    }
                }
            }

            if ((!fileNameMatcher.find() && fileNameMatcher3.find())) {
                String fullName = fileNameMatcher3.group(1);
                Matcher nameMatcher = namePattern.matcher(text);
                if (nameMatcher.find()) {
                    String name = nameMatcher.group(1).trim();
                    List<String> matcherGroupResult = List.of(name);
                    if (!matcherGroupResult.contains(fullName) && (!fullName.contains("foundit"))) {
                        String convertedFullName = convertToFullName(fullName);
                        names.add(convertedFullName);
                    }
                }
            }

                while (emailMatcher.find()) {
                    String email = emailMatcher.group();
                    if (!uniqueEmailsSet.contains(email) && uniqueEmailsSet.size() == 0) {
                        uniqueEmailsSet.add(email);
                        emails.add(email);
                        emailFound = true;
                    }
                }

//                while (phoneMatcher.find()) {
//                    String phoneNumber = phoneMatcher.group();
//                    if (!uniquePhoneNumbersSet.contains(phoneNumber) && uniquePhoneNumbersSet.size() == 0) {
//                        uniquePhoneNumbersSet.add(phoneNumber);
//                        phoneNumbers.add(phoneNumber);
//                        phoneNumberFound = true;
//                    }
//                }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!emailFound) {
            String email = "Email Not Found";
            emails.add(email);
        }
//        if (!phoneNumberFound) {
//            String phoneNumber = "Phone Number Not Found";
//            phoneNumbers.add(phoneNumber);
//        }

    }

    private static void extractFromImagePDF(File file, LinkedHashSet<String> names, List<String> emails) {

        boolean phoneNumberFound = false;
        boolean emailFound = false;

        HashSet<String> uniquePhoneNumbersSet = new HashSet<>();
        HashSet<String> uniqueEmailsSet = new HashSet<>();

        Tesseract tesseract = new Tesseract();

        try {
            tesseract.setDatapath("C:\\Users\\ELCOT\\Downloads\\Tess4J\\tessdata");
            String text = tesseract.doOCR(file);
            System.out.println(text);

            Pattern namePattern = Pattern.compile("(?i)\\b([A-Z][a-z]+(?: [A-Z][a-z]+)?)\\b");


            Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
            Matcher emailMatcher = emailPattern.matcher(text);


//            Pattern phonePattern = Pattern.compile("(?:(?:\\+91)|(?:0))?[789]\\d{9}");
            Pattern phonePattern = Pattern.compile("(?:(?:\\+91)|(?:0)|(?:\\+65))?[789]\\d{9}");
            Matcher phoneMatcher = phonePattern.matcher(text);


            String regex =  "foundit Profile_ ([^.]+)";
            String regex2 = "([A-Za-z]+[A-Za-z]+)";
            String regex3 =  "Naukri_(\\w+)";
            Pattern pattern = Pattern.compile(regex);
            Pattern pattern2 = Pattern.compile(regex2);
            Pattern pattern3 = Pattern.compile(regex3);
            System.out.println("FILE++++++++++++++++"+file.getName());
            Matcher fileNameMatcher = pattern.matcher(file.getName());
            Matcher fileNameMatcher2 = pattern2.matcher(file.getName());
            Matcher fileNameMatcher3 = pattern3.matcher(file.getName());



            if (fileNameMatcher.find()) {
                String fullName = fileNameMatcher.group(1);
                Matcher nameMatcher = namePattern.matcher(text);
                if (nameMatcher.find()) {
                    String name = nameMatcher.group(1).trim();
                    List<String> matcherGroupResult = List.of(name);
                    if (!matcherGroupResult.contains(fullName)) {
                        String convertedFullName = convertToFullName(fullName);
                        names.add(convertedFullName);
                    }
                }
            }

            if (!fileNameMatcher.find() && fileNameMatcher2.find()) {
                    String fullName = fileNameMatcher2.group(1);
                    if(!fullName.contains("Naukri")) {
                    Matcher nameMatcher = namePattern.matcher(text);
                    if (nameMatcher.find()) {
                        String name = nameMatcher.group(1).trim();
                        List<String> matcherGroupResult = List.of(name);
                        if (!matcherGroupResult.contains(fullName) && !fullName.contains("foundit")) {
                            String convertedFullName = convertToFullName(fullName);
                            names.add(convertedFullName);
                        }
                    }
                }
            }

            if ((!fileNameMatcher.find() && fileNameMatcher3.find())) {
                String fullName = fileNameMatcher3.group(1);
                Matcher nameMatcher = namePattern.matcher(text);
                if (nameMatcher.find()) {
                    String name = nameMatcher.group(1).trim();
                    List<String> matcherGroupResult = List.of(name);
                    if (!matcherGroupResult.contains(fullName) && (!fullName.contains("foundit"))) {
                        String convertedFullName = convertToFullName(fullName);
                        names.add(convertedFullName);
                    }
                }
            }

            while (emailMatcher.find()) {
                String email = emailMatcher.group();
                if (!uniqueEmailsSet.contains(email) && uniqueEmailsSet.size() == 0) {
                    uniqueEmailsSet.add(email);
                    emails.add(email);
                    emailFound = true;
                }
            }

//                while (phoneMatcher.find()) {
//                    String phoneNumber = phoneMatcher.group();
//                    if (!uniquePhoneNumbersSet.contains(phoneNumber) && uniquePhoneNumbersSet.size() == 0) {
//                        uniquePhoneNumbersSet.add(phoneNumber);
//                        phoneNumbers.add(phoneNumber);
//                        phoneNumberFound = true;
//                    }
//                }

        } catch (TesseractException e) {
            e.printStackTrace();
        }
        if (!emailFound) {
            String email = "Email Not Found";
            emails.add(email);
        }
//        if (!phoneNumberFound) {
//            String phoneNumber = "Phone Number Not Found";
//            phoneNumbers.add(phoneNumber);
//        }

    }

    private static void extractFromDocx(File file, LinkedHashSet<String> names, List<String> emails) {

        boolean phoneNumberFound = false;
        boolean emailFound = false;

        HashSet<String> uniquePhoneNumbersSet = new HashSet<>();
        HashSet<String> uniqueEmailsSet = new HashSet<>();


        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();

                Pattern namePattern = Pattern.compile("(?i)\\b([A-Z][a-z]+(?: [A-Z][a-z]+)?)\\b");


                Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
                Matcher emailMatcher = emailPattern.matcher(text);

//                Pattern phonePattern = Pattern.compile("(?:(?:\\+91)|(?:0))?[789]\\d{9}");
                Pattern phonePattern = Pattern.compile("(?:(?:\\+91)|(?:0)|(?:\\+65))?[789]\\d{9}");
                Matcher phoneMatcher = phonePattern.matcher(text);


                String regex =  "foundit Profile_ ([^.]+)";
                String regex2 = "([A-Za-z]+[A-Za-z]+)";
                String regex3 =  "Naukri_(\\w+)";
                Pattern pattern = Pattern.compile(regex);
                Pattern pattern2 = Pattern.compile(regex2);
                Pattern pattern3 = Pattern.compile(regex3);
                System.out.println("FILE++++++++++++++++"+file.getName());
                Matcher fileNameMatcher = pattern.matcher(file.getName());
                Matcher fileNameMatcher2 = pattern2.matcher(file.getName());
                Matcher fileNameMatcher3 = pattern3.matcher(file.getName());



                if (fileNameMatcher.find()) {
                    String fullName = fileNameMatcher.group(1);
                    Matcher nameMatcher = namePattern.matcher(text);
                    if (nameMatcher.find()) {
                        String name = nameMatcher.group(1).trim();
                        List<String> matcherGroupResult = List.of(name);
                        if (!matcherGroupResult.contains(fullName)) {
                            String convertedFullName = convertToFullName(fullName);
                            names.add(convertedFullName);
                        }
                    }
                }

                if (!fileNameMatcher.find() && fileNameMatcher2.find()) {
                    String fullName = fileNameMatcher2.group(1);
                    if(!fullName.contains("Naukri")) {
                        Matcher nameMatcher = namePattern.matcher(text);
                        if (nameMatcher.find()) {
                            String name = nameMatcher.group(1).trim();
                            List<String> matcherGroupResult = List.of(name);
                            if (!matcherGroupResult.contains(fullName) && !fullName.contains("foundit")) {
                                String convertedFullName = convertToFullName(fullName);
                                names.add(convertedFullName);
                            }
                        }
                    }
                }

                if ((!fileNameMatcher.find() && fileNameMatcher3.find())) {
                    String fullName = fileNameMatcher3.group(1);
                    Matcher nameMatcher = namePattern.matcher(text);
                    if (nameMatcher.find()) {
                        String name = nameMatcher.group(1).trim();
                        List<String> matcherGroupResult = List.of(name);
                        if (!matcherGroupResult.contains(fullName) && (!fullName.contains("foundit"))) {
                            String convertedFullName = convertToFullName(fullName);
                            names.add(convertedFullName);
                        }
                    }
                }

                    while (emailMatcher.find()) {
                        String email = emailMatcher.group();
                        if (!uniqueEmailsSet.contains(email) && uniqueEmailsSet.size() == 0) {
                            uniqueEmailsSet.add(email);
                            emails.add(email);
                            emailFound = true;
                        }
                    }

//                    while (phoneMatcher.find()) {
//                        String phoneNumber = phoneMatcher.group();
//                        if (!uniquePhoneNumbersSet.contains(phoneNumber) && uniquePhoneNumbersSet.size() == 0) {
//                            uniquePhoneNumbersSet.add(phoneNumber);
//                            phoneNumbers.add(phoneNumber);
//                            phoneNumberFound = true;
//                        }
//                    }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!emailFound) {
            String email = "Email Not Found";
            emails.add(email);
        }

//        if (!phoneNumberFound) {
//            String phoneNumber = "Phone Number Not Found";
//            phoneNumbers.add(phoneNumber);
//        }

    }

    private static void extractFromDoc(File file, LinkedHashSet<String> names, List<String> emails) {

        boolean phoneNumberFound = false;
        boolean emailFound = false;

        HashSet<String> uniquePhoneNumbersSet = new HashSet<>();
        HashSet<String> uniqueEmailsSet = new HashSet<>();

        try (FileInputStream fis = new FileInputStream(file);
             HWPFDocument document = new HWPFDocument(fis)) {

            Range range = document.getRange();
            for (int i = 0; i < range.numParagraphs(); i++) {
                Paragraph paragraph = range.getParagraph(i);
                String text = paragraph.text();

                Pattern namePattern = Pattern.compile("(?i)\\b([A-Z][a-z]+(?: [A-Z][a-z]+)?)\\b");
                Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
//                Pattern phonePattern = Pattern.compile("(?:(?:\\+91)|(?:0))?[789]\\d{9}");
                Pattern phonePattern = Pattern.compile("(?:(?:\\+91)|(?:0)|(?:\\+65))?[789]\\d{9}");

//                Matcher nameMatcher = namePattern.matcher(text);
                Matcher emailMatcher = emailPattern.matcher(text);
                Matcher phoneMatcher = phonePattern.matcher(text);


                String regex =  "foundit Profile_ ([^.]+)";
                String regex2 = "([A-Za-z]+[A-Za-z]+)";
                String regex3 =  "Naukri_(\\w+)";
                Pattern pattern = Pattern.compile(regex);
                Pattern pattern2 = Pattern.compile(regex2);
                Pattern pattern3 = Pattern.compile(regex3);
                System.out.println("FILE++++++++++++++++"+file.getName());
                Matcher fileNameMatcher = pattern.matcher(file.getName());
                Matcher fileNameMatcher2 = pattern2.matcher(file.getName());
                Matcher fileNameMatcher3 = pattern3.matcher(file.getName());


                if (fileNameMatcher.find()) {
                    String fullName = fileNameMatcher.group(1);
                    Matcher nameMatcher = namePattern.matcher(text);
                    if (nameMatcher.find()) {
                        String name = nameMatcher.group(1).trim();
                        List<String> matcherGroupResult = List.of(name);
                        if (!matcherGroupResult.contains(fullName)) {
                            String convertedFullName = convertToFullName(fullName);
                            names.add(convertedFullName);
                        }
                    }
                }

                if (!fileNameMatcher.find() && fileNameMatcher2.find()) {
                    String fullName = fileNameMatcher2.group(1);
                    if(!fullName.contains("Naukri")) {
                        Matcher nameMatcher = namePattern.matcher(text);
                        if (nameMatcher.find()) {
                            String name = nameMatcher.group(1).trim();
                            List<String> matcherGroupResult = List.of(name);
                            if (!matcherGroupResult.contains(fullName) && !fullName.contains("foundit")) {
                                String convertedFullName = convertToFullName(fullName);
                                names.add(convertedFullName);
                            }
                        }
                    }
                }

                if ((!fileNameMatcher.find() && fileNameMatcher3.find())) {
                    String fullName = fileNameMatcher3.group(1);
                    Matcher nameMatcher = namePattern.matcher(text);
                    if (nameMatcher.find()) {
                        String name = nameMatcher.group(1).trim();
                        List<String> matcherGroupResult = List.of(name);
                        if (!matcherGroupResult.contains(fullName) && (!fullName.contains("foundit"))) {
                            String convertedFullName = convertToFullName(fullName);
                            names.add(convertedFullName);
                        }
                    }
                }

                    while (emailMatcher.find()) {
                        String email = emailMatcher.group();
                        if (!uniqueEmailsSet.contains(email) && uniqueEmailsSet.size() == 0) {
                            uniqueEmailsSet.add(email);
                            emails.add(email);
                            emailFound = true;
                        }
                    }

//                    while (phoneMatcher.find()) {
//                        String phoneNumber = phoneMatcher.group();
//                        if (!uniquePhoneNumbersSet.contains(phoneNumber) && uniquePhoneNumbersSet.size() == 0) {
//                            uniquePhoneNumbersSet.add(phoneNumber);
//                            phoneNumbers.add(phoneNumber);
//                            phoneNumberFound = true;
//                        }
//                    }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!emailFound) {
            String email = "Email Not Found";
            emails.add(email);
        }
//        if (!phoneNumberFound) {
//            String phoneNumber = "Phone Number Not Found";
//            phoneNumbers.add(phoneNumber);
//        }
    }

    public static String convertToFullName(String name) {
        StringBuilder fullName = new StringBuilder();

        // Add the first character to the fullName
        fullName.append(name.charAt(0));

        // Iterate through the remaining characters in the name
        for (int i = 1; i < name.length(); i++) {
            char currentChar = name.charAt(i);

            // Check if the character is uppercase
            if (Character.isUpperCase(currentChar)) {
                // Insert a space before the uppercase letter
                fullName.append(' ');
            }

            // Append the current character to the fullName
            fullName.append(currentChar);
        }

        return fullName.toString();
    }


    public static boolean containsImages(File file) {
        try (PDDocument document = PDDocument.load(file)) {
            for (PDPage page : document.getPages()) {
                for (COSName name : page.getResources().getXObjectNames()) {
                    if (name instanceof COSName) {
                        // Check if the XObject is an instance of PDImageXObject
                        if (page.getResources().getXObject(name) instanceof PDImageXObject) {
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // If no images found, return false
        return false;
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
