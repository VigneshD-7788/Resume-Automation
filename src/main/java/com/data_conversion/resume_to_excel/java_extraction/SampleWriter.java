package com.data_conversion.resume_to_excel.java_extraction;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SampleWriter {
//    public static void main(String[] args) {
//        String[] skills = {"C", "Python", "Java"};
//
//        // Create a new Workbook and Sheet
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Skills");
//
//            // Create a header row
//            Row headerRow = sheet.createRow(0);
//            Cell headerCell = headerRow.createCell(0);
//            headerCell.setCellValue("Skills");
//
//            // Write skills to the sheet
//            for (int i = 0; i < skills.length; i++) {
//                Row row = sheet.createRow(i + 1);
//                Cell cell = row.createCell(0);
//                cell.setCellValue(skills[i]);
//            }
//
//            // Save the data to an Excel file
//            try (FileOutputStream outputStream = new FileOutputStream("C:\\Users\\ELCOT\\Downloads\\output.xlsx")) {
//                workbook.write(outputStream);
//            }
//
//            System.out.println("Data has been written to the Excel file successfully.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
//        String educationResult = "Bachelor of Science,Computer Science,";
//        String skillResult = "Java Programming,Python Programming,C++ Programming";
//
//        // Split educationResult by last comma
//        int lastCommaIndexEducation = educationResult.lastIndexOf(",");
//        String educationDegree = educationResult.substring(0, lastCommaIndexEducation).trim();
//        String educationField = educationResult.substring(lastCommaIndexEducation + 1).trim();
//
//        // Split skillResult by last comma
//        int lastCommaIndexSkill = skillResult.lastIndexOf(",");
//        String[] skills = skillResult.substring(0, lastCommaIndexSkill).split(",");
//        String lastSkill = skillResult.substring(lastCommaIndexSkill + 1).trim();
//
//        // Output the results
//        System.out.println("Education Degree: " + educationDegree);
//        System.out.println("Education Field: " + educationField);
//
//        System.out.println("Skills:");
//        for (String skill : skills) {
//            System.out.println(skill.trim());
//        }
//        System.out.println("Last Skill: " + lastSkill);
        extractCompanyNames();
    }
    public static String[] extractCompanyNames() {
        String text = "A bh il a s h a He g d e\n" +
                "Bengaluru, India\n" +
                "+91 7892679962\n" +
                "abhilashah48@gmail.com\n" +
                "LinkedIn Profile\n" +
                "PROFESSIONAL SUMMARY\n" +
                "Energetic software developer with 4.8 years of experience in developing the robust code, involved in complete\n" +
                "SDLC including analysis, design, development, testing, implementation of products using technology like C++ in\n" +
                "Linux platform. An enthusiastic team player and a creative thinker.\n" +
                "EXPERIENCE\n" +
                "Infinera India Pvt Ltd, Bengaluru — Software Developer II\n" +
                "JULY 2017 – FEB 2022\n" +
                "\uF0FC Develop software modules in each software release cycle to provide different functionalities for Optical transport\n" +
                "Network.\n" +
                "\uF0FC Implement new feature set, do maintenance, and bug fixes across software release cycles.\n" +
                "\uF0FC Contribute to testing framework that automates process behavior testing.\n" +
                "\uF0FC Mentor junior engineers to design and implement new feature set.\n" +
                "\uF0D8 Released features L0 restoration, Sndp protocol, Connectivity Matrix and L-Band support as a part of FlexILS.\n" +
                "\uF0D8 Developed many features of Optical services in each Software release cycle.\n" +
                "EDUCATION\n" +
                "University Visvesvaraya College of Engineering (UVCE), Bengaluru — B.E.\n" +
                "Aug 2013 - June 2017\n" +
                "Specialization : Information Science and Engineering\n" +
                "Percentage : 80.98%\n" +
                "KEY SKILLS\n" +
                "\uF0FC C++, Java (Core)\n" +
                "\uF0FC Object Oriented Programming, Data structures and Algorithms, LINUX\n" +
                "\uF0FC Jira, Git (Bitbucket), Perforce, Visual Studio\n" +
                "PROJECTS\n" +
                "SNDP\n" +
                "A generic layer independent neighbor discovery protocol that uses a simple set of message exchange\n" +
                "to stich the neighbor relationships over a point-to-point link.\n" +
                "\uF0FC Designed and implemented the protocol from scratch in C++.\n" +
                "\uF0FC Provided Unit test framework along with developing the protocol.\n" +
                "\uF0FC Tested and maintained the software module.\n" +
                "FlexILS\n" +
                "The core backend system of Infinera Line System that does routing and signaling of optical services.\n" +
                "\uF0FC Designed and implemented Optical service creation with L-Band frequencies and advertising L-Band frequency\n" +
                "information across the network using C++ in Linux platform.\n" +
                "\uF0FC Bug fixing in Optical service creation and C+L band features.\n" +
                "\uF0FC Addition of new optical features and bug fixes in L0 restoration.\n" +
                "OPTICAL RESTORATION\n" +
                "Optical Restoration or L0-Restoration detects the fault in the network and automatically re-routes the bandwidths\n" +
                "around the failed links.\n" +
                "\uF0FC Designed and implemented In-Service Optical service migration for L0-restoration using C++ in Linux platform.\n" +
                "\uF0FC Handling the different requirements of Optical service creation in the route query which actually does the verification of the path\n" +
                "and bandwidth allocation.\n" +
                "\uF0FC Maintaining and bug fixing of Auto-Retuning of digital line modules feature.\n" +
                "\uF0FC Addition of different attributes for Optical light path for service creation and restoration.\n" +
                "\uF0FC Addition of new optical attributes and bug fixes in L0-restoration.\n" +
                "FLEX CONNECTIVITY MATRIX\n" +
                "Internal connectivity of individual FlexTeInterface with all possible outgoing physical ports. It has the\n" +
                "available bandwidth and connectivity information of the ports across the network.\n" +
                "\uF0FC Designing and implementing the new format for the Flex connectivity matrix which has the information per\n" +
                "equipment basis using C++ in Linux platform.\n" +
                "\uF0FC Developed the feature using OSPF TE Opaque LSA (Type-10).\n" +
                "\uF0FC Adding support for advertising the L-Band frequencies across the network.\n" +
                "\uF0FC Maintaining and fixing bugs across the software release cycles.\n" +
                "OTHER PROJECTS\n" +
                "\uF0FC In-Memory Logger\n" +
                "Designed and developed a light-weight In-memory logger using C++ which primarily logged events during a\n" +
                "debugging session in multi-threaded environment. In-memory logger is used to store logs according to the object\n" +
                "Id and level of importance.\n" +
                "CERTIFICATIONS AND WORKSHOPS\n" +
                "\uF0FC Completed Core java course in Uttara InfoTech solutions in 2017.\n" +
                "\uF0FC Completed C workshop conducted by Subhash K.U in 2017.";
        Pattern companyPattern = Pattern.compile("\\b[A-Z][A-Za-z0-9&\\(\\)\\-,. ]+\\b");
        Matcher matcher = companyPattern.matcher(text);

        // Create a list to store the matched company names
        List<String> companyNames = new ArrayList<>();
        while (matcher.find()) {
            String companyName = matcher.group();
            // Add more filters or checks if needed to eliminate false positives
            companyNames.add(companyName);
        }

        // Convert the list to an array
        return companyNames.toArray(new String[0]);
    }
    }
