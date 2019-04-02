package example
import com.tsukaby.bayes.classifier.BayesClassifier

object naiveBayes_test extends App {

  // Create instance
  val bayes = new BayesClassifier[String, String]()

  // Learning
  bayes.learn("technology", "github" :: "git" :: "tech" :: "technology" :: Nil)
  bayes.learn("weather", "sun" :: "rain" :: "cloud" :: "weather" :: "snow" :: Nil)
  bayes.learn("government", "ballot" :: "winner" :: "party" :: "money" :: "candidate" :: Nil)

  val unknownText1 = "I use git weather rain github tech".split(" ")
  val unknownText2 = "Today's weather is snow".split(" ")
  val unknownText3 = "I will vote for that party".split(" ")

  // Classify
  println(bayes.classifyDetailed(unknownText1)) // government

}
