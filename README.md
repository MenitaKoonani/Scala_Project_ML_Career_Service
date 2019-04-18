# Scala Project ML Career Service
CSYE7200 Scala Project : ML Career Service

## Introduction
-   MLBC is Web based system is for candidates who are seeking a job and recruiter who are looking for the right talent.
    
-   The platform scrapes and analyzes publicly available job posting data from sites like Glassdoor, LinkedIn, etc.
    
-   The system will recommend the candidate which job title will fit them based on their resume. The system will also give job links to the suitable for them.
    
-   The platform will also help us to gain statistical information about the latest job trend.

## Team Details

| Name| NUID| Email Address|
| ------------- |:-------------:| -----:|
| Menita Koonani    | 001883043 |[koonani.m@husky.neu.edu](mailto:koonani.m@husky.neu.edu) |
| Raghavi Kirouchenaradjou     | 001826638|   [kirouchenaradjou.r@husky.neu.edu](mailto:kirouchenaradjou.r@husky.neu.edu) |
| Sreerag Mandakathil Sreenath | 001838559      |    [mandakathil.s@husky.neu.edu](mailto:mandakathil.s@husky.neu.edu) |

## Use case

-   Actor :
    

	-  A user (In this case a graduate IS student) will initiate the interaction with the web api
    

-   Action :
    

	 - The user will upload their resume in pdf/text format to the web api
    

-   Reaction :
    

	- The system will recommend the user the best job titles based on their skills mentioned in the resume. The system will also send in URLs to apply for the suggested job titles. Predict salary expectation in different profiles and which is best suitable for that resume

## Project Stack

| Sno.| Task                          | Library						 |
| --- |:-----------------------------:|:------------------------------------------------:|
<<<<<<< HEAD
| 1.  | Web Scraping                  |Python : Scrapy, BeautifulSoup and LXML 	 |
=======
| 1.  | Web Scraping                  |Python : Scrappy, Beautiful Soup and LXML 	 |
>>>>>>> 94e1902a67f9b438797740cec16c997dad4cad62
| 2.  | Data Cleaning                 |Scala/Spark : RegexTokenizer and StopWordsRemover |
| 3.  | Classification and ML modeling|Scala/Spark : NLTK and Classification     	 |
| 4.  | Web Server and Rest API       |Scala : AKKA HTML                         	 |

## Web Scraping
Indeed.com is Web Scrapped for 3 Job Titles - Software Engineer, Data Scientist, Technical Writer to get information for about 100 jobs per job title using Python.

Attributes that are scrapped for every job title:
 - Title
 - Url
 - Description
 - Company
 - Salary
 - Location

These attributes are scrapped using libraries like Scrapy and Beautifufl Soup and are stored as a json file.

## Data Cleaning
The scrapped file is cleaned in Scala to remove punctuation and stopwords from the job_posting_desc column in order to make it fit for training the model.

## Model Training Result
The model is trained using Naive bayes classifier for both raw and cleaned data which were scraped from indeed.com on 04-11-2019

The data was split into 3:1 ratio for testing and training 

The percentage of accuracy as follows:

| Sno. | Type of data              | Accuray |
|:----:|:-------------------------:|:-------:|
| 1.   | indeed 04-11-19 (raw)     |  87.75% |
| 2.   | indeed 04-11-19 (cleaned) |  90.48% |

## Web Service
- Developing WEB API's using AKKA HTTP

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


## Final Project Execution

To execute the project navigate to the final_project folder and open it on Intellij

```
\Scala_Project_ML_Career_Service\final_project\
```
##### Run it via the Postman or Advanced Rest Client

_Method_ : POST <br>
_Endpoint_ : http://localhost:9000/pdf_file <br>
_Body_ (form-encoded): Upload a PDF File by providing the key as "filePdfUpload" and value as the path to the PDF file <br>
_Response_ : A json listing the Predicted Job Title and the Available Job Links as follows <br>
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
To know more about 
- AKKA HTTP : https://doc.akka.io/docs/akka-http/current/
- PdfTextStripper : https://pdfbox.apache.org/docs/2.0.7/javadocs/org/apache/pdfbox/text/PDFTextStripper.html

Data Cleaning:

Scala:
- StopWordsRemover: https://spark.apache.org/docs/latest/ml-features.html#stopwordsremover
- RegexTokenizer: https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.ml.feature.RegexTokenizer

Python:
- scrapy: https://docs.scrapy.org/en/latest/intro/overview.html
- BeatifulSoup: https://www.crummy.com/software/BeautifulSoup/bs4/doc/
