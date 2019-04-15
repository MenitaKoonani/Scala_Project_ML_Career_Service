package sparkModel

import org.apache.spark.sql.SparkSession
import org.codehaus.jettison.json.JSONObject

class JobMatch {
  def getJobMatches(role: String, spark: SparkSession): String = {

    val dataFile = "src/data/indeed_15-04-2019_cleaned.json"
    val df = spark.read.json(dataFile).drop("job_position_desc")
    val matchDF = df.filter("job_title = '" + role + "'")
    val result = matchDF.toJSON.map(new JSONObject(_).toString).collect()
    val result_1 = result.mkString(",")
    return result_1
  }
}
