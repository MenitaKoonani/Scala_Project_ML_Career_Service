/**
  * The WordFilter program implements an class to perform cleaning operations on the scrapped data
  *
  * @author Menita Koonani
  * @version 1.0
  * @since 2019-04-17
  */

package sparkModel

import org.apache.spark.ml.feature.{RegexTokenizer, StopWordsRemover}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

class WordFilter {
  /**
    * Function to get the dataframe where the job role matches the job_posting_title column of the dataframe
    *
    * @param resumeText A String variable that takes resume content as input
    * @param spark      Spark session
    * @return String which is the cleaned String
    */

  def stringOperations(resumeText: String, spark: SparkSession): String = {
    import spark.implicits._
    var df = Seq(
      (resumeText)
    ).toDF("job_posting_desc")

    //  Converting job_posting_Desc column to lowercase
    df = df.withColumn("job_posting_desc", lower(col("job_posting_desc")))

    //  Removing punctuation form the job_posting_desc column
    df = df.withColumn("job_posting_desc", regexp_replace(col("job_posting_desc"), """[\p{Punct}&&[^.]]""", " "))

    //   Tokenizing description column to words
    val regexTokenizer = new RegexTokenizer()
      .setInputCol("job_posting_desc")
      .setOutputCol("words")

    val countTokens = udf { (words: Array[String]) => words.length }
    val regexTokenized = regexTokenizer.transform(df).toDF()

    //  Removing stopwords from the description column
    val remover = new StopWordsRemover()
      .setInputCol("words")
      .setOutputCol("filtered")

    val tokenizedDesc = remover.transform(regexTokenized)
    var cleanedDF = tokenizedDesc.withColumn("job_posting_desc", concat_ws(" ", col("filtered")))
    cleanedDF = cleanedDF.drop("words").drop("filtered")
    cleanedDF.show()
    
    // Converting the cleanedDF into string
    val cleanedText = cleanedDF.head().getString(0)
    return cleanedText
  }
}
