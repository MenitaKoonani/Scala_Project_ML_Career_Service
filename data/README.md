# Scala Project ML Career Service
CSYE7200 Scala Project : ML Career Service

# Web Scraping

## Introduction
In this section of the project we will be scrapping data from Indeed.com using Python

## Technology Stack

Libraries used:
 - scrapy
	* CrawlerProcess
	* HtmlXPathSelector
	* LinkExtractor
	* CrawlSpider
	* Rule
- BeautifulSoup
- time
- logging

## Processes done
A web scrapper is defined to scrap indeed.com with the specified url search parameters and is store into a json file.
Methods are defined in the IndeedScrapper class to parse the main job page as well as each of the individual job pages until a depth level specified which is 1.

JSON generated: 
```json
[
	{
		"job_title": "", 
		"job_posting_desc": "", 
		"job_posting_url": "", 
		"scrap_date": "", 
		"scrap_website": "", 
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
		"scrap_website": "", 
		"job_posting_salary": "", 
		"job_posting_title": "", 
		"location": "", 
		"company": ""
	}
]
```

## Intructions for execution
Run Python notebook -> Kernel Menu -> Restart and run all


## Notes
To know more about 
- scrapy: https://docs.scrapy.org/en/latest/intro/overview.html
- BeatifulSoup: https://www.crummy.com/software/BeautifulSoup/bs4/doc/

