package classifier
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{HashingTF, IndexToString, StringIndexer, Tokenizer}
import org.apache.spark.sql.{SparkSession}


class NaiveBayesClass{

  def predict(resumeText : String, modelPath: String,spark : SparkSession): String ={

    val modelPipline = PipelineModel.load(modelPath)
    import spark.implicits._

    val df = Seq(
      (resumeText)
    ).toDF( "job_posting_desc")

    val predictions = modelPipline.transform(df)

    val result = predictions.head().getString(0)

    return result

  }

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

    spark.stop()


  }


}
