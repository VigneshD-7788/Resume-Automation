package com.data_conversion.resume_to_excel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeNameExtraction {
    public static void main(String[] args) {
        String resumeText = "John Doe\nSoftware Engineer\nContact: john.doe@email.com\nPhone: (123) 456-7890\n...";
        String extractedName = extractNameFromResume(resumeText);
        System.out.println("Extracted Name: " + extractedName);
    }

    public static String extractNameFromResume(String resumeText) {
        String name = null;
        Pattern namePattern = Pattern.compile("\\b[A-Z][a-z]+ [A-Z][a-z]+\\b");

        Matcher matcher = namePattern.matcher(resumeText);
        if (matcher.find()) {
            name = matcher.group();
        }

        return name;
    }
}
