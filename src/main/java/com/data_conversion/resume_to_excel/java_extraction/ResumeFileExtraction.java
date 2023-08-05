package com.data_conversion.resume_to_excel.java_extraction;

import org.apache.pdfbox.pdmodel.PDDocument;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeFileExtraction {

    public static void main(String[] args) {

        String folderPath = "C:\\Users\\ELCOT\\Documents\\resume_collections";

        LinkedHashSet<String> names = new LinkedHashSet<>();
        List<String> emails = new ArrayList<>();
        List<String> phoneNumbers = new ArrayList<>();
        List<String> educations = new ArrayList<>();
        List<String> skills = new ArrayList<>();
        List<List<String>> duplicateFiles = new ArrayList<>();

        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        Workbook workbook = new XSSFWorkbook();

        // Create a sheet in the workbook
        Sheet sheet = workbook.createSheet("Resume Data");

        // Add headers to the sheet
        String[] headers = {"Name", "Email", "Phone", "Education", "Skills"};
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
                            extractFromPDF(file, names, emails, phoneNumbers, educations, skills);
                        }
                        if (file.getName().endsWith(".docx")) {
                            extractFromDocx(file, names, emails, phoneNumbers, educations, skills);

                        }
                        if (file.getName().endsWith(".doc")) {
                            extractFromDoc(file, names, emails, phoneNumbers, educations, skills);
                        }
                    }
            }
        }

        String[] sNames = names.toArray(new String[0]);
        String[] sEmails = emails.toArray(new String[0]);
        String[] sPhoneNumbers = phoneNumbers.toArray(new String[0]);
        String educationResult = String.join(" ", educations);
        String skillResult = String.join(" ", skills);

        String[] educationArray = educationResult.split(" ");
        String[] skillsArray = skillResult.split(" ");
        System.out.println("Names:");
        for (String name : names) {
            System.out.println(name);
        }

        System.out.println("Emails:");
        for (String email : emails) {
            System.out.println(email);
        }

        System.out.println("Phone Numbers:");
        for (String phoneNumber : phoneNumbers) {
            System.out.println(phoneNumber);
        }

        System.out.println("Educations:");
        for (String education : educations) {
            System.out.println(education);
        }

        System.out.println("Skills:");
        for (String skill : skills) {
            System.out.println(skill);
        }

        int lastRowNum = sheet.getLastRowNum();

        int dataSize = names.size();

        for(int i=0; i<dataSize;i++){
            Row newRow = sheet.createRow(lastRowNum+1+i);

            Cell nameCell = newRow.createCell(0);
            nameCell.setCellValue(sNames[i]);

            Cell emailCell = newRow.createCell(1);
            emailCell.setCellValue(sEmails[i]);

            Cell phoneCell = newRow.createCell(2);
            phoneCell.setCellValue(sPhoneNumbers[i]);

            Cell educationCell = newRow.createCell(3);
            educationCell.setCellValue(educationArray[i]);

            Cell skillCell = newRow.createCell(4);
            skillCell.setCellValue(skillsArray[i]);
        }

        try {
            String excelFilePath = "C:\\Users\\ELCOT\\Downloads\\resume_extraction.xlsx";
            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            System.out.println("Data written to Excel successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void extractFromPDF(File file, LinkedHashSet<String> names, List<String> emails, List<String> phoneNumbers, List<String> educations, List<String> skills) {

        boolean phoneNumberFound = false;
        boolean emailFound = false;
        boolean educationFound = false;
        boolean skillFound = false;
        HashSet<String> uniquePhoneNumbersSet = new HashSet<>();
        HashSet<String> uniqueEmailsSet = new HashSet<>();
        HashSet<String> uniqueEducationSet = new HashSet<>();
        HashSet<String> uniqueSkillsSet = new HashSet<>();

        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            Pattern namePattern = Pattern.compile("(?i)\\b([A-Z][a-z]+(?: [A-Z][a-z]+)?)\\b");


            Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
            Matcher emailMatcher = emailPattern.matcher(text);


            Pattern phonePattern = Pattern.compile("(?:(?:\\+91)|(?:0))?[789]\\d{9}");
            Matcher phoneMatcher = phonePattern.matcher(text);


            List<String> educationKeywords = educationKeyWords();

            // Create the regex pattern to match the education keywords
            String educationRegexPattern = createEducationRegexPattern(educationKeywords);

            // Extract education using regex
            List<String> educationsFound = extractEducation(text, educationRegexPattern);
            System.out.println("Education FOUND============="+educationsFound);
//            educations.addAll(educationsFound);

            if(!educationsFound.isEmpty() && educationsFound.contains("B.E.")) {
                System.out.println("Education FOUND============="+educationsFound);
                educationFound = true;

                StringBuilder educationBuilder = new StringBuilder();
                for (String educationItem : educationsFound) {
                    educationBuilder.append(educationItem).append(",");
                }
                String educationData = educationBuilder.deleteCharAt(educationBuilder.length() - 1).toString(); // Remove the trailing comma
//            List<String> educationList = new ArrayList<>(List.of(educationData));

                if(!uniqueEducationSet.contains(educationData) && uniqueEducationSet.size() == 0) {
                    uniqueEducationSet.add(educationData);
                    educations.add(educationData);


                    // Print the extracted education
                    for (String education : educationsFound) {
                        System.out.println(education);
                    }
                }
            }

            List<String> skillKeywords = skillsKeywords();

            // Create the regex pattern to match the skill keywords
            String skillsRegexPattern = createSkillRegexPattern(skillKeywords);

            // Extract skills using regex
            List<String> skillsFound = extractSkills(text, skillsRegexPattern);
            System.out.println("SKILLS FOUND============="+skillsFound);
            if (!skillsFound.isEmpty()) {
                System.out.println("skills found.");
                skillFound = true;
                List<String> updatedSkillList = removeSpaces(skillsFound);


                StringBuilder skillBuilder = new StringBuilder();
                for (String skillItem : updatedSkillList) {
                    skillBuilder.append(skillItem).append(",");
                }
                String skillData = skillBuilder.deleteCharAt(skillBuilder.length() - 1).toString(); // Remove the trailing comma
//            List<String> skillList = new ArrayList<>(List.of(skillData));

                if(!uniqueSkillsSet.contains(skillData) && uniqueSkillsSet.size() == 0) {
                    uniqueSkillsSet.add(skillData);
                    skills.add(skillData);

                    // Print the extracted skills
                    for (String skill : skillsFound) {
                        System.out.println(skill);
                    }
                }
            }

                String regex = "([A-Za-z]+[A-Za-z]+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher fileNameMatcher = pattern.matcher(file.getName());

            if (fileNameMatcher.find()) {
                String fullName = fileNameMatcher.group(1);
                Matcher nameMatcher = namePattern.matcher(text);
                if (nameMatcher.find()) {
                    String name =nameMatcher.group(1);
                    List<String> matcherGroupResult = List.of(name);
                    if(!matcherGroupResult.contains(fullName)) {
                        String convertedFullName = convertToFullName(fullName);
                        names.add(convertedFullName);
                    }
                }
            }

            while (emailMatcher.find()) {
                String email = emailMatcher.group();
                if(!uniqueEmailsSet.contains(email)&& uniqueEmailsSet.size() == 0) {
                    uniqueEmailsSet.add(email);
                    emails.add(email);
                    emailFound = true;
                }
            }

            while (phoneMatcher.find()) {
                String phoneNumber = phoneMatcher.group();
                if(!uniquePhoneNumbersSet.contains(phoneNumber)&& uniquePhoneNumbersSet.size() == 0) {
                    uniquePhoneNumbersSet.add(phoneNumber);
                    phoneNumbers.add(phoneNumber);
                    phoneNumberFound = true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!emailFound) {
            String email = "Email Not Found";
            emails.add(email);
        }
        if (!phoneNumberFound) {
            String phoneNumber = "Phone Number Not Found";
            phoneNumbers.add(phoneNumber);
        }
        if(!skillFound) {
            String skill = "Skill Not Found";
            skills.add(skill);
        }

        if(!educationFound) {
            String education = "NoData";
            educations.add(education);
        }
    }

    private static void extractFromDocx(File file, LinkedHashSet<String> names, List<String> emails, List<String> phoneNumbers, List<String> educations, List<String> skills) {

        boolean phoneNumberFound = false;
        boolean emailFound = false;
        boolean educationFound = false;
        boolean skillFound = false;
        HashSet<String> uniquePhoneNumbersSet = new HashSet<>();
        HashSet<String> uniqueEmailsSet = new HashSet<>();
        HashSet<String> uniqueEducationSet = new HashSet<>();
        HashSet<String> uniqueSkillsSet = new HashSet<>();

        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();

                Pattern namePattern = Pattern.compile("(?i)\\b([A-Z][a-z]+(?: [A-Z][a-z]+)?)\\b");


                Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
                Matcher emailMatcher = emailPattern.matcher(text);

                Pattern phonePattern = Pattern.compile("(?:(?:\\+91)|(?:0))?[789]\\d{9}");
                Matcher phoneMatcher = phonePattern.matcher(text);


                List<String> educationKeywords = educationKeyWords();

                // Create the regex pattern to match the education keywords
                String educationRegexPattern = createEducationRegexPattern(educationKeywords);

                // Extract education using regex
                List<String> educationsFound = extractEducation(text, educationRegexPattern);
                System.out.println("Education FOUND============="+educationsFound);

                if(!educationsFound.isEmpty() && educationsFound.contains("B.E.")) {
                    System.out.println("Education FOUND============="+educationsFound);
                    educationFound = true;

                    StringBuilder educationBuilder = new StringBuilder();
                    for (String educationItem : educationsFound) {
                        educationBuilder.append(educationItem).append(",");
                    }
                    String educationData = educationBuilder.deleteCharAt(educationBuilder.length() - 1).toString(); // Remove the trailing comma
//                List<String> educationList = new ArrayList<>(List.of(educationData));
                    if(!uniqueEducationSet.contains(educationData) && uniqueEducationSet.size() == 0) {
                        uniqueEducationSet.add(educationData);

                        educations.add(educationData);

                        // Print the extracted education
                        for (String education : educationsFound) {
                            System.out.println(education);
                        }
                    }
                }

                List<String> skillKeywords = skillsKeywords();

                // Create the regex pattern to match the skill keywords
                String skillsRegexPattern = createSkillRegexPattern(skillKeywords);

                // Extract skills using regex
                List<String> skillsFound = extractSkills(text, skillsRegexPattern);
                System.out.println("SKILLS FOUND============="+skillsFound);

                if (!skillsFound.isEmpty()) {
                    System.out.println("skills found.");
                    skillFound = true;
                    List<String> updatedSkillList = removeSpaces(skillsFound);

                    StringBuilder skillBuilder = new StringBuilder();
                    for (String skillItem : updatedSkillList) {
                        skillBuilder.append(skillItem).append(",");
                    }
                    String skillData = skillBuilder.deleteCharAt(skillBuilder.length() - 1).toString(); // Remove the trailing comma
//                List<String> skillList = new ArrayList<>(List.of(skillData));
                    if(!uniqueSkillsSet.contains(skillData) && uniqueSkillsSet.size() == 0) {
                        uniqueSkillsSet.add(skillData);

                        skills.add(skillData);

                        // Print the extracted skills
                        for (String skill : skillsFound) {
                            System.out.println(skill);
                        }
                    }
                }


                String regex = "([A-Za-z]+[A-Za-z]+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher fileNameMatcher = pattern.matcher(file.getName());

                if (fileNameMatcher.find()) {
                    String fullName = fileNameMatcher.group(1);
                    Matcher nameMatcher = namePattern.matcher(text);
                    if (nameMatcher.find()) {
                        String name =nameMatcher.group(1);
                        List<String> matcherGroupResult = List.of(name);
                        if(!matcherGroupResult.contains(fullName)) {
                            String convertedFullName = convertToFullName(fullName);
                            names.add(convertedFullName);
                        }
                    }
                }

                while (emailMatcher.find()) {
                    String email = emailMatcher.group();
                    if(!uniqueEmailsSet.contains(email)&& uniqueEmailsSet.size() == 0) {
                        uniqueEmailsSet.add(email);
                        emails.add(email);
                        emailFound = true;
                    }
                }

                while (phoneMatcher.find()) {
                    String phoneNumber = phoneMatcher.group();
                    if(!uniquePhoneNumbersSet.contains(phoneNumber)&& uniquePhoneNumbersSet.size() == 0) {
                        uniquePhoneNumbersSet.add(phoneNumber);
                        phoneNumbers.add(phoneNumber);
                        phoneNumberFound = true;
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!emailFound) {
            String email = "Email Not Found";
            emails.add(email);
        }

        if (!phoneNumberFound) {
            String phoneNumber = "Phone Number Not Found";
            phoneNumbers.add(phoneNumber);
        }

        if(!skillFound) {
            String skill = "Skill Not Found";
            skills.add(skill);
        }

        if(!educationFound) {
            String education = "NoData";
            educations.add(education);
        }
    }

    private static void extractFromDoc(File file, LinkedHashSet<String> names, List<String> emails, List<String> phoneNumbers, List<String> educations, List<String> skills) {

        boolean phoneNumberFound = false;
        boolean emailFound = false;
        boolean educationFound = false;
        boolean skillFound = false;
        HashSet<String> uniquePhoneNumbersSet = new HashSet<>();
        HashSet<String> uniqueEmailsSet = new HashSet<>();
        HashSet<String> uniqueEducationSet = new HashSet<>();
        HashSet<String> uniqueSkillsSet = new HashSet<>();

        try (FileInputStream fis = new FileInputStream(file);
             HWPFDocument document = new HWPFDocument(fis)) {

            Range range = document.getRange();
            for (int i = 0; i < range.numParagraphs(); i++) {
                Paragraph paragraph = range.getParagraph(i);
                String text = paragraph.text();

                Pattern namePattern = Pattern.compile("(?i)\\b([A-Z][a-z]+(?: [A-Z][a-z]+)?)\\b");
                Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
                Pattern phonePattern = Pattern.compile("(?:(?:\\+91)|(?:0))?[789]\\d{9}");

                Matcher nameMatcher = namePattern.matcher(text);
                Matcher emailMatcher = emailPattern.matcher(text);
                Matcher phoneMatcher = phonePattern.matcher(text);


                List<String> educationKeywords = educationKeyWords();

                // Create the regex pattern to match the education keywords
                String educationRegexPattern = createEducationRegexPattern(educationKeywords);

                // Extract education using regex
                List<String> educationsFound = extractEducation(text, educationRegexPattern);
                System.out.println("Education FOUND============="+educationsFound);

                if(!educationsFound.isEmpty() && educationsFound.contains("B.E.")) {
                    System.out.println("Education FOUND============="+educationsFound);
                    educationFound = true;

                    StringBuilder educationBuilder = new StringBuilder();
                    for (String educationItem : educationsFound) {
                        educationBuilder.append(educationItem).append(",");
                    }
                    String educationData = educationBuilder.deleteCharAt(educationBuilder.length() - 1).toString(); // Remove the trailing comma
//                List<String> educationList = new ArrayList<>(List.of(educationData));
                    if(!uniqueEducationSet.contains(educationData)&& uniqueEducationSet.size()==0) {
                        uniqueEducationSet.add(educationData);

                        educations.add(educationData);

                        // Print the extracted education
                        for (String education : educationsFound) {
                            System.out.println(education);
                        }
                    }
                }

                List<String> skillKeywords = skillsKeywords();

                // Create the regex pattern to match the skill keywords
                String skillsRegexPattern = createSkillRegexPattern(skillKeywords);

                // Extract skills using regex
                List<String> skillsFound = extractSkills(text, skillsRegexPattern);
                System.out.println("SKILLS FOUND============="+skillsFound);

                if (!skillsFound.isEmpty()) {
                    System.out.println("skills found.");
                    skillFound = true;
                    List<String> updatedSkillList = removeSpaces(skillsFound);

                    StringBuilder skillBuilder = new StringBuilder();
                    for (String skillItem : updatedSkillList) {
                        skillBuilder.append(skillItem).append(",");
                    }
                    String skillData = skillBuilder.deleteCharAt(skillBuilder.length() - 1).toString(); // Remove the trailing comma
//                List<String> skillList = new ArrayList<>(List.of(skillData));
                    if (!uniqueSkillsSet.contains(skillData) && uniqueSkillsSet.size()== 0){
                        uniqueSkillsSet.add(skillData);

                    skills.add(skillData);

                    // Print the extracted skills
                    for (String skill : skillsFound) {
                        System.out.println(skill);
                    }
                }
                }


                String regex = "([A-Za-z]+[A-Za-z]+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher fileNameMatcher = pattern.matcher(file.getName());

                if (fileNameMatcher.find()) {
                    String fullName = fileNameMatcher.group(1);
                    if (nameMatcher.find()) {
                        String name =nameMatcher.group(1);
                        List<String> matcherGroupResult = List.of(name);
                        if(!matcherGroupResult.contains(fullName)) {
                            String convertedFullName = convertToFullName(fullName);
                            names.add(convertedFullName);
                        }
                    }
                }

                while (emailMatcher.find()) {
                    String email = emailMatcher.group();
                    if(!uniqueEmailsSet.contains(email)&& uniqueEmailsSet.size() == 0) {
                        uniqueEmailsSet.add(email);
                        emails.add(email);
                        emailFound = true;
                    }
                }

                while (phoneMatcher.find()) {
                    String phoneNumber = phoneMatcher.group();
                    if(!uniquePhoneNumbersSet.contains(phoneNumber)&& uniquePhoneNumbersSet.size() == 0) {
                        uniquePhoneNumbersSet.add(phoneNumber);
                        phoneNumbers.add(phoneNumber);
                        phoneNumberFound = true;
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!emailFound) {
            String email = "Email Not Found";
            emails.add(email);
        }
        if (!phoneNumberFound) {
            String phoneNumber = "Phone Number Not Found";
            phoneNumbers.add(phoneNumber);
        }

        if(!skillFound) {
            String skill = "Skill Not Found";
            skills.add(skill);
        }

        if(!educationFound) {
            String education = "NoData";
            educations.add(education);
        }
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

    public static List<String> educationKeyWords(){

        List<String> educationKeywords = new ArrayList<>();

        educationKeywords.add("B.E.");
        educationKeywords.add("B.E");
        educationKeywords.add("M.E");
        educationKeywords.add("M.E.");
        educationKeywords.add("M.B.A");
        educationKeywords.add("MBA");
        educationKeywords.add("M.S");
        educationKeywords.add("B.Com");
        educationKeywords.add("B.TECH");
        educationKeywords.add("M.TECH");
        educationKeywords.add("M.Tech");
        educationKeywords.add("SSLC");
        educationKeywords.add("SSC");
        educationKeywords.add("12th");
        educationKeywords.add("10th");
        educationKeywords.add("HSC");
        educationKeywords.add("XII");


        return educationKeywords;
    }

    private static String createEducationRegexPattern(List<String> educationKeywords) {
        // Join the education keywords into a regex pattern with OR(|) for matching
        return "(" + String.join("|", educationKeywords) + ")";
    }

    private static List<String> extractEducation(String text, String regexPattern) {
        List<String> education = new ArrayList<>();
        Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            education.add(matcher.group());
        }

        return education;
    }

    public static List<String> skillsKeywords(){

        List<String> skillKeywords = new ArrayList<>();
        skillKeywords.add("Java");
        skillKeywords.add("Python");
        skillKeywords.add("Ruby");
        skillKeywords.add("Swift");
        skillKeywords.add("Data structures and Algorithms,");
        skillKeywords.add("Object Oriented Programming");
        skillKeywords.add("LINUX");
        skillKeywords.add("Linux");
        skillKeywords.add("devops");
        skillKeywords.add("Mysql");
        skillKeywords.add("Postgresql");
        skillKeywords.add("Angular");
        skillKeywords.add("Html");
        skillKeywords.add("CSS");
        skillKeywords.add("Bootstrap");
        skillKeywords.add("Team work");
        skillKeywords.add("Communication");
        skillKeywords.add("Jira");
        skillKeywords.add("Git (Bitbucket),");
        skillKeywords.add("Perforce");
        skillKeywords.add("Visual Studio");
        skillKeywords.add("Git");
        skillKeywords.add("KAP Tool");
        skillKeywords.add("Data Structures");
        skillKeywords.add("SDLC");
        skillKeywords.add("Windows");
        skillKeywords.add("Adobe Photoshop");
        skillKeywords.add("Microsoft PowerPoint");
        skillKeywords.add("JIRA");
        skillKeywords.add("Google Analytics");
        skillKeywords.add("Canva");
        skillKeywords.add("CATIA");
        skillKeywords.add("GitHub");
        skillKeywords.add("Microsoft Project");
        skillKeywords.add("HTTP");
        skillKeywords.add("Figma");
        skillKeywords.add("Google Docs");
        skillKeywords.add("Artificial Intelligence");
        skillKeywords.add("Social media sites");
        skillKeywords.add("Firebase");
        skillKeywords.add("Good Problem-Solving Skills");
        skillKeywords.add("Node.JS,");
        skillKeywords.add("TypeScript");
        skillKeywords.add("Golang");
        skillKeywords.add("React.JS");
        skillKeywords.add("NestJS");
        skillKeywords.add("MySQL");
        skillKeywords.add("MongoDB");
        skillKeywords.add("Docker");
        skillKeywords.add("AWS");
        skillKeywords.add("Matlab");
        skillKeywords.add("java");
        skillKeywords.add("Shell Script");
        skillKeywords.add("Mysql");
        skillKeywords.add("Data structure");
        skillKeywords.add("Image Processing");
        skillKeywords.add("Version Control - GIT");
        skillKeywords.add("Networking");
        skillKeywords.add("Algorithms");
        skillKeywords.add("MorphX");
        skillKeywords.add("C#");
        skillKeywords.add("MS SQL Server ");
        skillKeywords.add("STL");
        skillKeywords.add("data structures and algorithmns");
        skillKeywords.add("OOPs concepts");
        skillKeywords.add("Multithreading");
        skillKeywords.add("GIT");
        skillKeywords.add("QNX RTOS");
        skillKeywords.add("python");
        skillKeywords.add("JIRA");
        skillKeywords.add("Data structures & Algorithms");
        skillKeywords.add("Design Patterns");
        skillKeywords.add("Core Java");
        skillKeywords.add("Eclipse");
        skillKeywords.add("Mac OS");
        skillKeywords.add("SVN");
        skillKeywords.add("Agile");
        skillKeywords.add("Spring boot");
        skillKeywords.add("REST API");
        skillKeywords.add("CURL");
        skillKeywords.add("SQLite");
        skillKeywords.add("Shell Scripting");
        skillKeywords.add("PostGreSQL");
        skillKeywords.add("HTML5");
        skillKeywords.add("CSS3");
        skillKeywords.add("dotNet,");
        skillKeywords.add(".Net,");
        skillKeywords.add("NoSQL");
        skillKeywords.add("Spring");
        skillKeywords.add("SpringBoot");
        skillKeywords.add("SpringFramework");
        skillKeywords.add("SQL");
        skillKeywords.add("Jmeter");
        skillKeywords.add("Hibernate,");
        skillKeywords.add("MVC");
        skillKeywords.add("Rest Api");
        skillKeywords.add("Collection Framework,");
        skillKeywords.add("Multithreading");
        skillKeywords.add("Postman");
        skillKeywords.add("Rest API");
        skillKeywords.add("C/C\\+\\+");
        skillKeywords.add("Bitbucket");
        skillKeywords.add("XML");
        skillKeywords.add("ASP");
        skillKeywords.add("Asp.NET ");
        skillKeywords.add("Magento");
        skillKeywords.add("Wordpress");
        skillKeywords.add("PHP");
        skillKeywords.add("AJAX");
        skillKeywords.add("SOAP");
        skillKeywords.add("REST");
        skillKeywords.add("Web Services");
        skillKeywords.add("HTML");
        skillKeywords.add("Java Script");
        skillKeywords.add("JavaScript");
        skillKeywords.add("J2EE");
        skillKeywords.add("Oracle");
        skillKeywords.add("Apache Tomcat");
        skillKeywords.add("JDBC");
        skillKeywords.add("core-java");
        skillKeywords.add("Visual Studio Code");


        return skillKeywords;

    }

    private static String createSkillRegexPattern(List<String> skillKeywords) {
        // Join the skill keywords into a regex pattern with OR(|) for matching
        return "(" + String.join("|", skillKeywords) + ")";
    }

    private static List<String> extractSkills(String text, String regexPattern) {
        List<String> skills = new ArrayList<>();
        Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            skills.add(matcher.group());
        }

        return skills;
    }

    private static List<String> removeSpaces(List<String> list) {
        List<String> updatedList = new ArrayList<>();
        for (String item : list) {
            if (item.contains(" ")) {
                // If the element contains spaces, remove the spaces and add it to the updated list
                String updatedItem = item.replace(" ", "");
                updatedList.add(updatedItem);
            } else {
                // If the element doesn't contain spaces, add it as it is to the updated list
                updatedList.add(item);
            }
        }
        return updatedList;
    }


}
