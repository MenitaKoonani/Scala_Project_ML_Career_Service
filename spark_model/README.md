# Scala Project ML Career Service
CSYE7200 Scala Project : ML Career Service

# ML Algorithm development

## Introduction
In this section of the project we will be trying to develop the machine learning model which will be used for predicting the kind of profile 

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


## Required files
The scala application is expecting to find the training data in the script directory

The training data is going to be a Json array of the following format:
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

Example : 

```json
{"job_title": "technical writer", "job_posting_desc": "Technical Writer (E20-19-0594-F):\n\nBowhead seeks a Technical writer with experience proof reading, editing and formatting of documentation ensuring it is accurate, complete. The Technical Writer should have the ability to communicate effectively orally and through written reports in specified formats. Effectively manage scheduling of personnel/calendars to ensure coverage of multiple buildings/facilities. Comfortable with the use of computer/web-based systems with desired experience in clearance verification systems, badge access and alarm systems. Ability to take responsibility for personnel and facilities to include the escorting, within classified spaces, of un-cleared personnel (those without US security clearances), manage use of resources as well as maintain inventory of assorted equipment and supplies.\n\nRequirements High School diploma, Bachelor's degree preferred\n\nIntermediate to advanced level skills in Microsoft Office software suite - Word, Excel, Outlook, PowerPoint\n\n\nA working knowledge of tech editing tools resident within the MS Office Suite.\n\nAbility to communicate effectively with all levels of employees and outside contacts. To receive and respond to instructions/assignments, must be able to read, write and speak English.\n\n\nThe ability to communicate effectively with engineers and other technical personnel.\n\n\nThe ability to work independently to deliver products on time.\n\nSECURITY CLEARANCE REQUIRED: Must be able to obtain a security clearance at the Secret level. US Citizenship is a requirement for Secret clearance at this location.\n\n\nApplicants may be subject to a pre-employment drug & alcohol screening and/or random drug screen, and must follow UIC\u2019s Non-DOT Drug & Alcohol Testing Program requirements. If the position requires, an applicant must pass a pre-employment criminal background history check. All post-secondary education listed on the applicant\u2019s resume/application may be subject to verification.\n\nWhere driving may be required or where a rental car must be obtained for business travel purposes, applicants must have a valid driver license for this position and will be subject to verification. In addition, the applicant must pass an in-house, online, driving course to be authorized to drive for company purposes.", "job_posting_url": "https://www.indeed.com/pagead/clk?mo=r&ad=-6NYlbfkN0BPhkBAIDmFMPMN_Wbqu40VnORwh18Yi8IRBq-wdVg1C4FRosJ448JJ5pdSzOPOT_viSmiTrQHHcy5dGSqXH94pIMieokfjho6VMqSXAZZf1Ig3v_K3Rkr1gFXiP8-szt3VXjQSq85YpKZJ13wPNAVcT7bAEUELsHrtT9mja2lnwzJRKmu5PDXKIz_3SuH5Qp7ldMh-Dnkq5n7VLXHgFGqkXr34QIT8RkZnQH0EoJLuu4WeW7BwpCXp7YfoUdpXNFMsR3mclnAsJGCTzGbGSEjFOYI9SGLYjFosws1vkJ4acTtshL8-AhTR-X56g6vMY37RCm2d5VV_WaqLrEtfXpDmswxt81eajKOksJTpIE556R6edj7p--aHvZsB8TCGd_VT9OlEqXtXdtLLgUaUy3sUxU4OW3ApJT1vHj4_imvkUA==&vjs=3&p=9&fvj=1", "scrap_date": "11-04-2019", "job_posting_salary": null, "job_posting_title": "Technical Writer I", "location": "San Francisco, CA", "company": null}
```

## Intructions for execution
Run the following scala app in intellij after compling libraries from SBT:
```
Scala_Project_ML_Career_Service\final_project\src\main\scala\NaiveBayesTextClassifier.scala
```

## Model Training Result
The model is trained using Naive bayes classifier for both raw and cleaned data which were scraped from indeed.com on 04-11-2019

The data was split into 3:1 ratio for testing and training 

The percentage of accuracy as follows:

| Sno. | Type of data              | Accuray |
|:----:|:-------------------------:|:-------:|
| 1.   | indeed 04-11-19 (raw)     |  87.75% |
| 2.   | indeed 04-11-19 (cleaned) |  90.48% |