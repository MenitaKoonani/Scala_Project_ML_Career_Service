package example

import example.NaiveBayesExample.counts
import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession

object NaiveBayesExample2 extends App {
  val conf = new SparkConf()
  conf.setMaster("local")
  conf.setAppName("Word Count")
  val sc = new SparkContext(conf)
  // $example on$
  // Load and parse the data file.
  val data = MLUtils.loadLibSVMFile(sc, "src/main/scala/example/sample_libsvm_data.txt")

  // Split data into training (60%) and test (40%).
  val Array(training, test) = data.randomSplit(Array(0.6, 0.4))

  val model = NaiveBayes.train(training, lambda = 1.0, modelType = "multinomial")

  val predictionAndLabel = test.map(p => (model.predict(p.features), p.label))
  val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()
  System.out.println("Accuracy: " + accuracy);

  // Save and load model
  model.save(sc, "src/main/scala/example/myNaiveBayesModel")
  val sameModel = NaiveBayesModel.load(sc, "src/main/scala/example/myNaiveBayesModel")
  // $example off$

  sc.stop()

}
