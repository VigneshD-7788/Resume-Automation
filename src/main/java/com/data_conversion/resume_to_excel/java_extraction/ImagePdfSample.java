package com.data_conversion.resume_to_excel.java_extraction;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.*;

public class ImagePdfSample {
    public static void main(String[] args) throws TesseractException {
        samplePdfExtract();
    }

    private static void samplePdfExtract() {
        Tesseract tesseract = new Tesseract();
        try{
            tesseract.setDatapath("C:\\Users\\ELCOT\\Downloads\\Tess4J\\tessdata");
            String text = tesseract.doOCR(new File("C:\\Users\\ELCOT\\Documents\\Durairagupathy__Singapore__yrs.pdf"));
            System.out.println(text);
        } catch (TesseractException e){
            e.printStackTrace();
        }
    }

}
