package example

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{HashingTF, StringIndexer, Tokenizer}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.Vector
object naive_bayes_test2 extends App {
  val conf = new SparkConf()
  conf.setMaster("local")
  conf.setAppName("Word Count")
  val sc = new SparkContext(conf)

  val data = sc.textFile("src/main/scala/example/sample_naive_bayes_data.txt")

  val spark = SparkSession
    .builder()
    .appName("Spark SQL basic example")
    .config(conf)
    .getOrCreate()

  val dataFile = "src/main/scala/example/softwareengineer_indeed_28-03-2019.json" // Should be some file on your system
  val df = spark.read.json(dataFile)

  val dfSmall = df.select("job_title","job_posting_desc")
  val indexer = new StringIndexer().setInputCol("job_title").setOutputCol("label").setHandleInvalid("keep")
  val tokenizer = new Tokenizer().setInputCol("job_posting_desc").setOutputCol("tokens")
  val hashingTF = new HashingTF().setInputCol("tokens").setOutputCol("features").setNumFeatures(20)


  val Array(trainingData, testData) = dfSmall.randomSplit(Array(0.7, 0.3))
  println("Testing Data")
  trainingData.show()
  println("Testing Data")
  testData.show()

  val pipeline = new Pipeline().setStages(Array(indexer, tokenizer, hashingTF, ovr))



//  val parsedData = dfSmall.map { line =>
//    val parts = line.split(',')
//    LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
//  }
//
//  // Split data into training (60%) and test (40%).
//  val splits = parsedData.randomSplit(Array(0.6, 0.4), seed = 11L)
//  val training = splits(0)
//  val test = splits(1)
//
//  val model = NaiveBayes.train(training, lambda = 1.0, modelType = "multinomial")
//
//  val predictionAndLabel = test.map(p => (model.predict(p.features), p.label))
//  val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()
//
//  // Save and load model
//  model.save(sc, "target/tmp/myNaiveBayesModel")
//  val sameModel = NaiveBayesModel.load(sc, "target/tmp/myNaiveBayesModel")
}
