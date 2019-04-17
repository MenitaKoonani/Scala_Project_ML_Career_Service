# Scala Project ML Career Service
CSYE7200 Scala Project : ML Career Service

# Web Service

## Introduction
In this section of the project we will be trying to develop web api using AKKA HTTP

## Technology Stack

- **_AKKA HTTP_**: Akka HTTP modules implement a full server and client-side HTTP stack on top of akka-actor and akka-stream.
- **_PDFTextStripper_**: This class will take a pdf document and strip out all of the text and ignore the formatting and such.

| Library              | Version | 
| ---------------------|:-------:|
| akka-actor           | 2.5.13  |
| akka-http            | 10.1.3  |
| akka-stream          | 2.5.13  |
| pdfbox               | 2.0.1   |
| fontbox              | 2.0.1   |


## Instructions for execution
 Following are the endpoints available :

- _Method_ : GET <br>
_URL_ : http://localhost:9000/<br>
_Response_ : ```json{
               "message": "Hello World"
               }```
- _Method_ : POST<br>
_URL_ : http://localhost:9000/resume_txt<br>
_Body_ (raw text): Some data <br>
_Response_ : ```json{
               "body": "Some data"
               }```
- _Method_ : POST<br>
_URL_ : http://localhost:9000/resume_txt_file<br>
_Body_ (form-data): Upload a .txt file with the key value "fileUpload" <br>
_Response_ : Displays the content inside the .txt file              

- _Method_ : POST<br>
_URL_ : http://localhost:9000/pdf_file<br>
_Body_ (form-data): Upload a .pdf file with the key value "filePdfUpload" <br>
_Response_ : Displays the content inside the .pdf file        

## Notes
To know more about 
- AKKA HTTP : https://doc.akka.io/docs/akka-http/current/
- PdfTextStripper : https://pdfbox.apache.org/docs/2.0.7/javadocs/org/apache/pdfbox/text/PDFTextStripper.html