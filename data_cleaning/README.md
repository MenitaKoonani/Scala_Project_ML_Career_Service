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
		"company": "",
		"job_title": "", 
		"location": "", 
		"job_posting_title": "",
		"job_posting_desc": "", 
		"job_posting_url": "", 
		"job_posting_salary": "", 
		"salary_period": "",
		"salary_lower": "",
		"salary_upper": "",
		"salary_average": "",
		"scrap_date": "", 
		"scrap_website": ""
	},
	{
		"company": "",
		"job_title": "", 
		"location": "", 
		"job_posting_title": "",
		"job_posting_desc": "", 
		"job_posting_url": "", 
		"job_posting_salary": "", 
		"salary_period": "",
		"salary_lower": "",
		"salary_upper": "",
		"salary_average": "",
		"scrap_date": "", 
		"scrap_website": ""
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
Navigate to the below file and run the application file
<br>```scala/DataCleaning/CleaningScrapedData.scala```

## Notes
To know more about 
- StopWordsRemover: https://spark.apache.org/docs/latest/ml-features.html#stopwordsremover
- RegexTokenizer: https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.ml.feature.RegexTokenizer
