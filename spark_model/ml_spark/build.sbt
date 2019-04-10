name := "ml_spark"

version := "0.1"

scalaVersion := "2.11.8"

//libraryDependencies ++= {
//  val sparkVersion = "2.2.1"
//  Seq("org.apache.spark" % "spark-core_2.11" % sparkVersion)
//}
//libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "2.1.0"

val sparkVersion = "2.3.0"


resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots/"
)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-hive" % sparkVersion,
  "mysql" % "mysql-connector-java" % "5.1.6"
)

libraryDependencies ++= Seq(
  "com.tsukaby" %% "naive-bayes-classifier-scala" % "0.2.0"
)