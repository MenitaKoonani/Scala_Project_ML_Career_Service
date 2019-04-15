package sparkModel

import org.apache.spark.ml.feature.{RegexTokenizer, StopWordsRemover}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

class WordFilter {
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

    val cleanedText = cleanedDF.head().getString(0)
    return cleanedText
  }
}
