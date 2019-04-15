package classifier

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession



object NB2 extends App {

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  val conf = new SparkConf()
  conf.setMaster("local")
  conf.setAppName("NaiveBayes Text classifier")

  val spark = SparkSession
    .builder()
    .appName("NaiveBayes Text classifier")
    .config(conf)
    .getOrCreate()

  var model = new NaiveBayesClass()

  var prediction = model.predict("Technical writer","src/main/scala/classifier/spark-model", spark)
  println(prediction)

  spark.stop()


}

