package com.data_conversion.resume_to_excel.java_extraction;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkillExtractor {
public static void main(String[] args) {
    // Sample text extracted from the resume (replace this with actual text from the PDF/DOC)
    String resumeText = "C++, Java (Core)\n" +
            "\uF0FC Object Oriented Programming, Data structures and Algorithms, LINUX\n" +
            "\uF0FC Jira, Git (Bitbucket), Perforce, Visual Studio";

    // Define skill keywords
    List<String> skillKeywords = new ArrayList<>();
    skillKeywords.add("Java");
    skillKeywords.add("Python");
    skillKeywords.add("C");
    skillKeywords.add("C++");
    skillKeywords.add("C\\+\\+");
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
    skillKeywords.add("J2EE");
    skillKeywords.add("Oracle");
    skillKeywords.add("Apache Tomcat");
    skillKeywords.add("JDBC");
    skillKeywords.add("core-java");
    skillKeywords.add("Visual Studio Code");

    // Add more skills as needed...

    // Create the regex pattern to match the skill keywords
    String regexPattern = createSkillRegexPattern(skillKeywords);

    // Extract skills using regex
    List<String> skillsFound = extractSkills(resumeText, regexPattern);

    // Print the extracted skills
    for (String skill : skillsFound) {
        System.out.println(skill);
    }
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
}
