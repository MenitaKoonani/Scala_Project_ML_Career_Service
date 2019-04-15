package DataCleaning

import org.apache.spark.ml.feature.{RegexTokenizer, StopWordsRemover}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.{SparkConf, SparkContext}

object CleaningScrapedData extends App {

  val conf = new SparkConf()
  conf.setMaster("local")
  conf.setAppName("Data Cleaning")
  val sc = new SparkContext(conf)

  val spark = SparkSession
    .builder()
    .appName("Data Cleaning")
    .config(conf)
    .getOrCreate()

  val dataFile = "src/data/indeed_10-04-2019.json"
  val df = spark.read.json(dataFile).drop("job_position_desc")

  //  Removing first row - it contains null
  val junk = df.first()
  var filteredDF = df.filter(x => x != junk)
  filteredDF.show()

  //  Converting job_posting_Desc column to lowercase
  filteredDF = filteredDF.withColumn("job_posting_desc", lower(col("job_posting_desc")))

  //  Removing punctuation form the job_posting_desc column
  filteredDF = filteredDF.withColumn("job_posting_desc", regexp_replace(col("job_posting_desc"), """[\p{Punct}&&[^.]]""", " "))
  filteredDF.show()

  //   Tokenizing description column to words
  val regexTokenizer = new RegexTokenizer()
    .setInputCol("job_posting_desc")
    .setOutputCol("words")

  val countTokens = udf { (words: Array[String]) => words.length }
  val regexTokenized = regexTokenizer.transform(filteredDF).toDF()

  //  Removing stopwords from the description column
  val remover = new StopWordsRemover()
    .setInputCol("words")
    .setOutputCol("filtered")

  val tokenizedDesc = remover.transform(regexTokenized)

  println("Cleaned Dataframe")
  var cleanedDF = tokenizedDesc.withColumn("filtered", concat_ws(" ", col("filtered")))
  cleanedDF = cleanedDF.drop("words").drop("filtered")
  cleanedDF.show()

  spark.stop()

}
