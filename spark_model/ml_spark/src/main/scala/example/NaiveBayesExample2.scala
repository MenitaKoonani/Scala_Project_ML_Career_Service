package example

import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{LogisticRegression, NaiveBayes, OneVsRest}
import org.apache.spark.ml.feature.{HashingTF, StringIndexer, Tokenizer}
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object NaiveBayesExample2 extends App {
  val conf = new SparkConf()
  conf.setMaster("local")
  conf.setAppName("Word Count")
  val spark = SparkSession
    .builder()
    .appName("Spark SQL basic example")
    .config(conf)
    .getOrCreate()
  val dataFile = "src/main/scala/example/softwareengineer_indeed_28-03-2019.json" // Should be some file on your system
  val df = spark.read.json(dataFile)
  val dfSmall = df.select("job_title","job_posting_desc")
  val indexer = new StringIndexer().setInputCol("job_title").setOutputCol("label").setHandleInvalid("skip")
  val tokenizer = new Tokenizer().setInputCol("job_posting_desc").setOutputCol("tokens")
  val hashingTF = new HashingTF().setInputCol("tokens").setOutputCol("features").setNumFeatures(20)
  val lr = new LogisticRegression().setMaxIter(100).setRegParam(0.001)
  val ovr = new OneVsRest().setClassifier(lr)
  dfSmall.show()

  val pipeline = new Pipeline().setStages(Array(indexer, tokenizer, hashingTF, ovr))

  val Array(trainingData, testData) = dfSmall.randomSplit(Array(0.7, 0.3))
  trainingData.show()
  testData.show()
//  val model = pipeline.fit(trainingData)
//  val prediction = model.transform(testData).select("job_posting_desc","job_title","prediction")
//  prediction.show()

  val model2 = new NaiveBayes().setFeaturesCol("job_posting_desc").setLabelCol("job_title").fit(trainingData)

  val predictions = model2.transform(testData)
  predictions.show()


}
