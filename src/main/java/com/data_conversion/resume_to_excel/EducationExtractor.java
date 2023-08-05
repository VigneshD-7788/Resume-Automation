package com.data_conversion.resume_to_excel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EducationExtractor {

    public static void main(String[] args) {
        // Sample text extracted from the resume (replace this with actual text from the PDF/DOC)
        String resumeText = "University Visvesvaraya College of Engineering (UVCE), Bengaluru — B.E.\n" +
                "Aug 2013 - June 2017\n" +
                "Specialization : Information Science and Engineering\n" +
                "Percentage : 80.98%";

        // Define skill keywords
        List<String> educationKeywords = new ArrayList<>();

//        educationKeywords.add("BE");
        educationKeywords.add("B.E.");
        educationKeywords.add("B.E");
//        educationKeywords.add("BS");
        educationKeywords.add("B.S");
        educationKeywords.add("ME");
        educationKeywords.add("M.E");
//        educationKeywords.add("M.E.");
        educationKeywords.add("M.B.A");
        educationKeywords.add("MBA");
        educationKeywords.add("MS");
        educationKeywords.add("M.S");
        educationKeywords.add("BTECH");
        educationKeywords.add("B.TECH");
        educationKeywords.add("M.TECH");
        educationKeywords.add("M.Tech");
        educationKeywords.add("MTECH");
        educationKeywords.add("SSLC");
        educationKeywords.add("SSC");
        educationKeywords.add("Master of Computer Applications");
        educationKeywords.add("Bachelor of Computer Application");
        educationKeywords.add("Bachelor Of Technology");
        educationKeywords.add("Bachelor of Engineering");
        educationKeywords.add("Post-Graduate");
        educationKeywords.add("MCA");
        educationKeywords.add("BCA");
        educationKeywords.add("12th");
        educationKeywords.add("10th");
        educationKeywords.add("MPC");
        educationKeywords.add("PUC");
        educationKeywords.add("HSC");
        educationKeywords.add("CBSE");
        educationKeywords.add("ICSE");
        educationKeywords.add("X");
        educationKeywords.add("XII");

        System.out.println(educationKeywords);
        // Add more skills as needed...

        // Create the regex pattern to match the skill keywords
        String regexPattern = createEducationRegexPattern(educationKeywords);

        // Extract skills using regex
        List<String> educationFound = extractEducation(resumeText, regexPattern);

        // Print the extracted skills
        for (String education : educationFound) {
            System.out.println(education);
        }

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
    public static List<String> educationKeyWords(List<String> educationExtracts){

        List<String> educationKeywords = new ArrayList<>();

        educationKeywords.add("B.E.");
        educationKeywords.add("B.E");
        educationKeywords.add("BS");
        educationKeywords.add("B.S");
        educationKeywords.add("ME");
        educationKeywords.add("M.E");
        educationKeywords.add("M.E.");
        educationKeywords.add("M.B.A");
        educationKeywords.add("MBA");
        educationKeywords.add("MS");
        educationKeywords.add("M.S");
        educationKeywords.add("BTECH");
        educationKeywords.add("B.TECH");
        educationKeywords.add("M.TECH");
        educationKeywords.add("MTECH");
        educationKeywords.add("SSLC");
        educationKeywords.add("SSC");
        educationKeywords.add("HSC");
        educationKeywords.add("CBSE");
        educationKeywords.add("ICSE");
        educationKeywords.add("X");
        educationKeywords.add("XII");

        educationExtracts.addAll(educationKeywords);


        return educationExtracts;
    }
}
