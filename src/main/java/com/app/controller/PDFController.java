package com.app.controller;

import com.app.service.testPDFService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/pdf")
public class PDFController {

    private final testPDFService pdfService;

    @Autowired
    public PDFController(testPDFService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/make-searchable")
    public String makeSearchablePDF(
            @RequestParam("sourceFilePath") String sourceFilePath,
            @RequestParam("destinationFilePath") String destinationFilePath) {
        try {
            pdfService.makeSearchablePDF(sourceFilePath, destinationFilePath, "1-2", "", "fra");
            return "PDF conversion completed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred during PDF conversion.";
        }
    }
}
