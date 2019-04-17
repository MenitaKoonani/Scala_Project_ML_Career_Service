# Scala Project ML Career Service
CSYE7200 Scala Project : ML Career Service

# Data Cleaning

## Introduction
In this section of the project we will be trying to clean the scrapped data by removing punctuation and stopwords from the mainly the job_posting_desc column

## Technology Stack

| Library              | Version | 
| ---------------------|:-------:|
| scala version        | 2.11.8  |
| spark-core           | 2.3.0   |
| spark-sql            | 2.3.0   |
| spark-mllib          | 2.3.0   |
| spark-streaming      | 2.3.0   |
| spark-hive		   | 2.3.0   |
| mysql-connector-java | 5.1.6   |


## Required files
The scala application is expecting to find the json scrapped data in the data directory
JSON expected: 
```json
[
	{
		"job_title": "", 
		"job_posting_desc": "", 
		"job_posting_url": "", 
		"scrap_date": "", 
		"job_posting_salary": "", 
		"job_posting_title": "", 
		"location": "", 
		"company": ""
	},
	{
		"job_title": "", 
		"job_posting_desc": "", 
		"job_posting_url": "", 
		"scrap_date": "", 
		"job_posting_salary": "", 
		"job_posting_title": "", 
		"location": "", 
		"company": ""
	}
]
```

## Processes done
The job_posting_description and job_posting_salary columns are cleaned by the following steps:
 - job_posting_desc column
	 * Conversion of  into lowercase
	 * Removing punctuation from the text
	 * Removing stop words form the text
	 * Converting them back into String
- job_posting_salary column
	 * Generating lower bound of salary
	 * Generating upper bound of salary
	 * Calculating average bound of salary

## Intructions for execution
Run application CleaningScrapedData.scala file in scala/DataCleaning directory

## Notes