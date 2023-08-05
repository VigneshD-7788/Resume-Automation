package com.data_conversion.resume_to_excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ExcelWriter {
    public static void main(String[] args) {
        // Sample data for names, emails, and phone numbers
        String[] names = {"John", "Jane", "Michael", "Emily", "William"};
        String[] emails = {"john@example.com", "jane@example.com", "michael@example.com", "emily@example.com", "william@example.com"};
        String[] phoneNumbers = {"1234567890", "9876543210", "5555555555", "9999999999", "7777777777"};

        String filePath = "C:\\Users\\ELCOT\\Downloads\\sample_data.xlsx"; // Replace with your desired file path

        System.out.println("Names"+ Arrays.toString(names));

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            // Write data to the Excel file
            for (int i = 0; i < names.length; i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(names[i]);
                row.createCell(1).setCellValue(emails[i]);
                row.createCell(2).setCellValue(phoneNumbers[i]);
            }

            // Save the data to the file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }

            System.out.println("Data has been written to the Excel file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
