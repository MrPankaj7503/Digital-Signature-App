package com.pankaj.simpleapp;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/docs")
public class DocumentController {

    @PostMapping("/sign")
    public String signDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("signaturesData") String signaturesJson,
            @RequestParam(value = "signatureImages", required = false) MultipartFile[] signatureImages
    ) {

        String uploadDir = "C:/uploads/";

        try {

            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            File originalFile = new File(uploadDir + file.getOriginalFilename());
            file.transferTo(originalFile);

            PDDocument document = PDDocument.load(originalFile);
            PDPage page = document.getPage(0);

            float pageHeight = page.getMediaBox().getHeight();
            float pageWidth = page.getMediaBox().getWidth();

            // Must match iframe size
            float viewerHeight = 500f;
            float viewerWidth = 800f;

            float scaleX = pageWidth / viewerWidth;
            float scaleY = pageHeight / viewerHeight;

            PDPageContentStream contentStream =
                    new PDPageContentStream(document, page,
                            PDPageContentStream.AppendMode.APPEND, true, true);

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> signatures =
                    mapper.readValue(signaturesJson, List.class);

            for (int i = 0; i < signatures.size(); i++) {

                Map<String, Object> sig = signatures.get(i);

                float x = Float.parseFloat(sig.get("x").toString());
                float y = Float.parseFloat(sig.get("y").toString());
                String type = sig.get("type").toString();
                String signer = sig.get("signer").toString();

                float pdfX = x * scaleX;
                float pdfY = pageHeight - (y * scaleY) - 70;

                if ("image".equals(type) && signatureImages != null) {

                    int imageIndex = Integer.parseInt(sig.get("imageIndex").toString());

                    File temp = new File(uploadDir + "temp_" + imageIndex + ".png");
                    signatureImages[imageIndex].transferTo(temp);

                    PDImageXObject pdImage =
                            PDImageXObject.createFromFile(temp.getAbsolutePath(), document);

                    contentStream.drawImage(pdImage, pdfX, pdfY, 150, 70);
                }

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(pdfX, pdfY - 20);
                contentStream.showText("Signed by: " + signer);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Date: " + LocalDate.now());
                contentStream.endText();
            }

            contentStream.close();

            File signedFile = new File(uploadDir + "signed_multi_drag.pdf");
            document.save(signedFile);
            document.close();

            return "SUCCESS: " + signedFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
}