package classifier

import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.classification.{NaiveBayes}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{HashingTF, StringIndexer, Tokenizer}
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.{SparkConf}

import org.apache.log4j.Logger
import org.apache.log4j.Level


object NaiveBayesTextClassifier extends App {

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  // Defining the configuration for Spark
  val conf = new SparkConf()
  conf.setMaster("local")
  conf.setAppName("NaiveBayes Text classifier")

  val spark = SparkSession
    .builder()
    .appName("NaiveBayes Text classifier")
    .config(conf)
    .getOrCreate()


  // Specifying the location of the data file
  val dataFile = "src/main/scala/example/indeed_11-04-2019_cleaned.json"

  // Loading the data file into a data frame
  val df = spark.read.json(dataFile)

  val dfSmall = df.select("job_title", "job_posting_desc")
  // Defining processes for the pipeline
  val indexer = new StringIndexer().setInputCol("job_title").setOutputCol("label")
  val tokenizer = new Tokenizer().setInputCol("job_posting_desc").setOutputCol("tokens")
  val hashingTF = new HashingTF().setInputCol("tokens").setOutputCol("features")
  val nb = new NaiveBayes()

  // Creating a pipeline for the modelling
  val pipeline = new Pipeline().setStages(Array(indexer, tokenizer, hashingTF, nb))

  // Splitting into training and testing data from the main data frame
  val Array(trainingData, testData) = dfSmall.randomSplit(Array(0.7, 0.3), seed = 11L)

  println("Testing Data")
  trainingData.show()
  println("Testing Data")
  testData.show()
  val model = pipeline.fit(trainingData)

  val prediction = model.transform(testData)
  prediction.show()
  var labels = prediction.select("label","job_title").distinct()
  labels = labels.withColumnRenamed("label","label2").withColumnRenamed("job_title","predicted_job_title")
  labels.show()

  val join2 = prediction.join(labels,prediction.col("prediction") === labels.col("label2"))
  join2.show()

  join2.select("label","job_title", "predicted_job_title")
    .write.mode(SaveMode.Overwrite).format("json").save("src/main/scala/example/spark-prediction")

  prediction.select("label","job_title","probability")
    .write.mode(SaveMode.Overwrite).format("json").save("src/main/scala/example/spark-prediction-full")

  val predictions = model.transform(testData)
  val evaluatorRF = new MulticlassClassificationEvaluator().setLabelCol("label").setPredictionCol("prediction").setMetricName("accuracy")
  val accuracy = evaluatorRF.evaluate(predictions)



  model.write.overwrite().save("src/main/scala/example/spark-model")

  val sameModel = PipelineModel.load("src/main/scala/example/spark-model")

  println(f"Accuracy = $accuracy%.4f")
  spark.stop()
}
