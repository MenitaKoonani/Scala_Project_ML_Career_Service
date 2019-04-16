# Scala Project ML Career Service
CSYE7200 Scala Project : ML Career Service

## Introduction
-   MLBC is Web based system is for candidates who are seeking a job and recruiter who are looking for the right talent.
    
-   The platform scrapes and analyzes publicly available job posting data from sites like Glassdoor, LinkedIn, etc.
    
-   The system will recommend the candidate which job title will fit them based on their resume. The system will also give job links to the suitable for them.
    
-   The platform will also help us to gain statistical information about the latest job trend.

## Technology Stack

| Library              | Version | 
| ---------------------|:-------:|
| scala version        | 2.11.8  |
| spark-core           | 2.3.0   |
| spark-sql            | 2.3.0   |
| spark-mllib          | 2.3.0   |
| spark-streaming      | 2.3.0   |
| spark-hive		   | 2.3.0   |
| mysql-connector-java | 2.3.0   |
| akka-actor           | 2.5.13  |
| akka-http            | 10.1.3  |
| akka-stream          | 2.5.13  |
| pdfbox               | 2.0.1   |
| fontbox              | 2.0.1   |


## Required files
The scala application is expecting to find a PDF/text uploaded by the user

## Instructions for execution
Run it via the Postman or Advanced Rest Client

Endpoint : http://localhost:9000/pdf_file
Body (form-encoded): Upload a PDF File by providing the key as "filePdfUpload" and value as the path to the PDF file
Response : A json listing the Predicted Job Title and the Available Job Links as follows
```json
{
    "Predicted Role": "software engineer",
    "Available Jobs": [
        {
            "job_posting_title": "Software Quality Engineer",
            "company": "DELL",
            "location": "Boston, MA",
            "job_posting_url": "https://www.indeed.com/pagead/clk?mo=r&ad=-6NYlbfkN0DhVAxkc_TxySVbUOs6bxWYWOfhmDTNcVTjFFBAY1FXZ_f-lnuRL7vGmhrcjkjTSE3fin6ve_ms4_9mScuaceMLDlH5RM-fUHmHxZE5PndrOse_GkPZwuCVyi6uzk699vmQcNe663vhzNZYMDTKXCuX_SXq9blbeu-m_sPFggUQmSJ3v2d4J7fnz01fJbN01w-4bzs4qhEzv8nkSkWNqEwrSGidIZ4UO13FjMoRpSx5MCV6FY8nm0BD3WFo6ZkQjgkPxQ4F4_N8f-MKYhPbXYZZmB0dwoVPXx02RGboZ7Ar7vzZtO8Lza-DXlpN05wt-_4ghGoPuQe6TKMc4NBW8q4mIDORxZ-dUoVWA76HFzHSSMDBlBWlT5fVyILzmdm7FEoPlA_waWP3GgMYUwRaqm204uEfvZJ3Osdb8u9BnQp4cw==&vjs=3&p=9&fvj=1",
            "job_posting_salary": "None"
        },
         {
            "job_posting_title": "User Interface Software Engineer",
            "company": "JW Fishers Mfg., Inc.",
            "location": "Boston, MA",
            "job_posting_url": "https://www.indeed.com/pagead/clk?mo=r&ad=-6NYlbfkN0DhVAxkc_TxySVbUOs6bxWYWOfhmDTNcVTjFFBAY1FXZ_f-lnuRL7vGmhrcjkjTSE3fin6ve_ms4_9mScuaceMLDlH5RM-fUHmHxZE5PndrOse_GkPZwuCVyi6uzk699vmQcNe663vhzNZYMDTKXCuX_SXq9blbeu-m_sPFggUQmSJ3v2d4J7fnz01fJbN01w-4bzs4qhEzv8nkSkWNqEwrSGidIZ4UO13FjMoRpSx5MCV6FY8nm0BD3WFo6ZkQjgkPxQ4F4_N8f-MKYhPbXYZZmB0dwoVPXx02RGboZ7Ar7vzZtO8Lza-DXlpN05wt-_4ghGoPuQe6TKMc4NBW8q4mIDORxZ-dUoVWA76HFzHSSMDBlBWlT5fVyILzmdm7FEoPlA_waWP3GgMYUwRaqm204uEfvZJ3Osdb8u9BnQp4cw==&vjs=3&p=9&fvj=1",
            "job_posting_salary": "80000 - 120000"
         }
     ]
}
```
If the uploaded file is not a PDF you would get the following :
```json
{
    "errorMessage": "Error in file uploading:only PDF allowed"
}
```
## Notes
