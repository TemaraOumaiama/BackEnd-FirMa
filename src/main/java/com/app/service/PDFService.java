package com.app.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PDFService {

    // ...
/*
    public void generateSearchablePDF(MultipartFile inputFile) throws IOException {
        String outputFilePath = "C:/myapp_temp_dir/output.pdf";
        try (PDDocument document = new PDDocument()) {
            try (InputStream inputStream = inputFile.getInputStream()) {
                PDDocument inputDocument = PDDocument.load(inputStream);
                PDFRenderer pdfRenderer = new PDFRenderer(inputDocument);
                int totalPages = inputDocument.getNumberOfPages();

                for (int pageNumber = 0; pageNumber < totalPages; pageNumber++) {
                    PDPage page = new PDPage();
                    document.addPage(page);

                    BufferedImage bufferedImage = pdfRenderer.renderImage(pageNumber);
                    String extractedText = performOCR(bufferedImage);
                    addTextToPDF(document, page, extractedText);
                }
            } catch (TesseractException e) {
                e.printStackTrace();
            }

            document.save(outputFilePath);
        }
    }
    */

// ...




/*
    private String performOCR(BufferedImage image) throws TesseractException {
        try {
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
            return tesseract.doOCR(image);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TesseractException(e.getMessage(), e);
        }
    }


    private void addTextToPDF(PDDocument document, PDPage page, String text) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            text = text.replace("\n", "").replace("\r", "");
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(10, 10);
            contentStream.showText(text);
            contentStream.endText();
        }
    }*/





    public void generateSearchablePDF(MultipartFile inputFile) throws IOException {
        String outputFilePath = "C:/myapp_temp_dir/ham.pdf";

        try (PDDocument inputDocument = PDDocument.load(inputFile.getInputStream());
             PDDocument outputDocument = new PDDocument()) {
            PDFRenderer pdfRenderer = new PDFRenderer(inputDocument);
            int totalPages = inputDocument.getNumberOfPages();

            for (int pageNumber = 0; pageNumber < totalPages; pageNumber++) {
                PDPage inputPage = inputDocument.getPage(pageNumber);
                PDPage outputPage = new PDPage(inputPage.getMediaBox());
                outputDocument.addPage(outputPage);

                BufferedImage bufferedImage = pdfRenderer.renderImage(pageNumber);
                String extractedText = performOCR(bufferedImage);
                addTextToPage(outputDocument, outputPage, extractedText);
            }

            // Set OCR processed flag to make the document searchable
            outputDocument.getDocumentCatalog().getCOSObject().setInt("OCRD", 1);

            outputDocument.save(outputFilePath);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }

    private String performOCR(BufferedImage image) throws TesseractException {
        try {
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
            return tesseract.doOCR(image);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TesseractException(e.getMessage(), e);
        }
    }




    private void addTextToPage(PDDocument document, PDPage page, String text) throws IOException {
        PDRectangle mediaBox = page.getMediaBox();
        PDResources resources = page.getResources();

        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);

        // Load the font file from the classpath
        try (InputStream fontStream = getClass().getResourceAsStream("C:/myapp_temp_dir/Arial.ttf")) {
            // Check if the fontStream is null
            if (fontStream == null) {
                throw new IOException("Font file not  wa33333333333333333333 found or could not be loaded.");
            }

            contentStream.beginText();
            contentStream.setFont(PDType0Font.load(document, fontStream), 12);
            contentStream.newLineAtOffset(10, mediaBox.getHeight() - 10);
            contentStream.showText(text);
            contentStream.endText();

            // Embed font resources
            PDStream embeddedFontStream = new PDStream(document, fontStream);
            resources.getCOSObject().setItem(COSName.getPDFName("FontFile"), embeddedFontStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (contentStream != null) {
                contentStream.close();
            }
        }
    }




}
