/**
  * The Naive Bayes Classifier class
  * To make simple functions to run training and prediction
  *
  * @author  Sreerag Mandakathil Sreenath
  * @version 1.0
  * @since   2019-04-17
  */

package sparkModel
// Importing libraries
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{HashingTF, IndexToString, StringIndexer, Tokenizer}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.SparkSession


class NaiveBayesClass{
  /**
    * Function to validate the dataframe headers
    *
    * @param resumeText String that contains the content of the resume
    * @param modelPath String defining the path to the model
    * @param spark Spark session variable
    * @return Predicted role for the resume
    */
  def predict(resumeText : String, modelPath: String, spark : SparkSession): String ={

    val modelPipline = PipelineModel.load(modelPath)
    import spark.implicits._

    val df = Seq(
      (resumeText)
    ).toDF( "job_posting_desc")

    val predictions = modelPipline.transform(df)

    predictions.show()

    val result = predictions.select("predicted_job_title").head().getString(0)


    return result

  }

  /**
    * Function to validate the dataframe headers
    *
    * @param dataFilePath String that contains the path to the training data
    * @param modelSavePath String defining the path to save the model
    * @param spark Spark session variable
    * @return Predicted role for the resume
    */

  def trainModel(dataFilePath : String, modelSavePath: String, spark : SparkSession): Unit ={

    val df = spark.read.json(dataFilePath)

    val dfSmall = df.select("job_title", "job_posting_desc")
    // Defining processes for the pipeline
    val indexer = new StringIndexer().setInputCol("job_title").setOutputCol("label")
    val tokenizer = new Tokenizer().setInputCol("job_posting_desc").setOutputCol("tokens")
    val hashingTF = new HashingTF().setInputCol("tokens").setOutputCol("features")
    val nb = new NaiveBayes()

    var reverseIndexer = indexer.fit(dfSmall).labels
    val labelReverse = new IndexToString().setInputCol("prediction").setLabels(reverseIndexer).setOutputCol("predicted_job_title")


    // Creating a pipeline for the modelling
    val pipeline = new Pipeline().setStages(Array(indexer,tokenizer,hashingTF,nb,labelReverse))

    // Splitting into training and testing data from the main data frame
    val Array(trainingData, testData) = dfSmall.randomSplit(Array(0.7, 0.3), seed = 11L)

    println("Testing Data")
    trainingData.show()
    println("Testing Data")
    testData.show()
    val model = pipeline.fit(trainingData)

    val prediction = model.transform(testData)
    prediction.show()

    val predictions = model.transform(testData)
    val evaluatorRF = new MulticlassClassificationEvaluator().setLabelCol("label").setPredictionCol("prediction").setMetricName("accuracy")
    val accuracy = evaluatorRF.evaluate(predictions)
    println(f"Accuracy = $accuracy%.4f")



    model.write.overwrite().save(modelSavePath)

  }


}
