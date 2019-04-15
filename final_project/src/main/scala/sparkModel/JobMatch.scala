package sparkModel

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.codehaus.jettison.json.JSONObject

class JobMatch {
  def getJobMatches(role: String, spark: SparkSession): String = {
    import spark.implicits._
    val dataFile = "src/main/scala/data/indeed_15-04-2019_cleaned.json"
    val df = spark.read.json(dataFile).drop("job_position_desc")
    val matchDF = df.select("job_posting_title","company","location","job_posting_url","job_posting_salary").filter("job_title = '" + role + "'")
    val result = matchDF.toJSON.map(new JSONObject(_).toString).collect()
    val result_1 = result.mkString(",")
    return result_1
  }
}
