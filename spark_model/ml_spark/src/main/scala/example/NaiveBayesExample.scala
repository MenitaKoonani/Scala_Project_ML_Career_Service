package example


import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.util.MLUtils


object NaiveBayesExample extends App {

  //Create a SparkContext to initialize Spark
  val conf = new SparkConf()
  conf.setMaster("local")
  conf.setAppName("Word Count")
  val sc = new SparkContext(conf)

  // Load the text into a Spark RDD, which is a distributed representation of each line of text
  val textFile = sc.textFile("src/main/scala/example/shakespeare.txt")

  //word count
  val counts = textFile.flatMap(line => line.split(" "))
    .map(word => (word, 1))
    .reduceByKey(_ + _)

  counts.foreach(println)
  System.out.println("Total words: " + counts.count());
  counts.saveAsTextFile("/tmp/shakespeareWordCount")

}