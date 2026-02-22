# Digital-Signature-App
Multi-Signature PDF Signing Web Application is a Spring Boot-based system that allows users to upload PDF documents and add multiple draggable signatures (text or image-based). The application converts browser coordinates to PDF coordinates to ensure accurate placement and generates a newly signed PDF using Apache PDFBox.

# Multi-Signature PDF Signing Web Application

## 🔹 Project Description

This project is a web-based PDF signing system built using Spring Boot and Apache PDFBox.

The application allows users to upload a PDF document and place multiple signatures (text or image-based) on the document using drag-and-drop functionality.

It demonstrates full-stack development concepts including:

- REST API development
- File handling
- Coordinate system transformation
- PDF manipulation
- Frontend drag-and-drop logic
- Multipart form-data processing

---

## 🔹 Key Features

- Upload PDF documents
- Add unlimited signatures
- Drag-and-drop signature positioning
- Support for:
  - Text-only signatures
  - Image + text signatures
- Automatic date stamping
- Coordinate scaling from browser to PDF system
- Generate final signed PDF output

---

## 🔹 Tech Stack

Backend:
- Java
- Spring Boot
- Apache PDFBox
- Maven

Frontend:
- HTML
- CSS
- JavaScript (Drag & Drop API)

---

## 🔹 System Architecture

Frontend:
- Collects PDF file
- Allows draggable signature elements
- Sends signature metadata (JSON) + image files via multipart request

Backend:
- Parses JSON signature data
- Converts browser coordinates to PDF coordinates
- Uses PDFBox to:
  - Draw images
  - Add text
- Generates new signed PDF file

---

## 🔹 Coordinate Conversion Logic

Browser uses top-left origin (0,0)
PDF uses bottom-left origin (0,0)

To resolve this mismatch:

scaleX = pageWidth / viewerWidth  
scaleY = pageHeight / viewerHeight  

pdfX = x * scaleX  
pdfY = pageHeight - (y * scaleY) - imageHeight  

This ensures correct placement of signatures inside the PDF.

---

## 🔹 Setup Instructions

1. Install Java 17+
2. Install Maven
3. Create folder:
   C:/uploads/
4. Run:
   mvn clean spring-boot:run
5. Open:
   http://localhost:8080

Signed PDFs will be saved in:
C:/uploads/

---

## 🔹 Limitations

- Currently supports only first page signing
- No database storage
- No authentication system
- No cryptographic digital certificate signing

---

## 🔹 Future Enhancements

- Multi-page PDF support
- Real digital certificate-based signing
- Signature resizing UI
- User login and role management
- Cloud deployment
- Audit trail logging

---

## 🔹 Author
Pankaj Kumar
