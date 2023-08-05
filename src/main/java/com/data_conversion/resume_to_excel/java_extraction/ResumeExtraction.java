package com.data_conversion.resume_to_excel.java_extraction;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ResumeExtraction {

    public static String extractTextFromResume(File file) throws IOException, SAXException, TikaException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputStream = new FileInputStream(file);
        ParseContext pContext = new ParseContext();

        AutoDetectParser parser = new AutoDetectParser();
        parser.parse(inputStream, handler, metadata, pContext);
        inputStream.close();

        return handler.toString().trim();
    }

    public static void main(String[] args) {
        String folderPath = "C:\\Users\\ELCOT\\Documents\\resume_data";
        String excelFilePath = "C:\\Users\\ELCOT\\Downloads\\resume_extraction.xlsx";

        try{
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Resumes");

            Set<String> uniqueResumes = new HashSet<>();

            File folder = new File(folderPath);
            File[] files = folder.listFiles();

            int rowNumber = 0;
            for(File file : files){
                String text = extractTextFromResume(file);
                String[] lines = text.split("\\r?\\n");

                String name = null;
                String phone = null;
                String email = null;


                for(String line : lines){
                    if(line.toLowerCase().contains("name:") && name == null) {
                        name = line.substring(line.toLowerCase().indexOf("name:") + 5).trim();
                    } else if (line.matches("[+]?[0-9]{10,13}") && phone == null) {
                        phone = line.trim();
                    } else if (line.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$") && email == null){
                        email = line.trim();
                    }
                }

                if(name != null) {
                    String resumeData = name +phone + email;
                    if(!uniqueResumes.contains(resumeData)){
                        uniqueResumes.add(resumeData);

                        Row row = sheet.createRow(rowNumber++);
                        row.createCell(0).setCellValue(name);
                        row.createCell(1).setCellValue(phone);
                        row.createCell(2).setCellValue(email);
                    }
                }
            }

            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

            System.out.println("Data written to Excel successfully.");
        } catch (IOException | TikaException | SAXException e) {
            e.printStackTrace();
        }
    }
}
