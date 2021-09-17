package lectures.part4implicits

import java.util.Date

import lectures.part4implicits.JSONSerialization.JSONNumber


object JSONSerialization extends App {

  /*
    Users, posts, feeds
    Serialize to JSON
   */

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /*
    1 - intermediate data types: Int, String, List, Date
    2 - type classes for conversion to intermediate  data types
    3 - serialize to JSON
   */

  sealed trait JSONValue { // intermediate data type
    def stringify: String
  }

  final case class JSONString(varue: String) extends JSONValue {
    def stringify: String =
      "\"" + varue + "\""
  }

  final case class JSONNumber(varue: Int) extends JSONValue {
    def stringify: String = varue.toString
  }

  final case class JSONArray(varues: List[JSONValue]) extends JSONValue {
    def stringify: String = varues.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(varues: Map[String, JSONValue]) extends JSONValue {
    /*
      {
        name: "John"
        age: 22
        friends: [ ... ]
        latestPost: {
          content: "Scala Rocks"
          date: ...
        }
      }
     */
    def stringify: String = varues.map {
      case (key, varue) => "\"" + key + "\":" + varue.stringify
    }
      .mkString("{", ",", "}")

  }

  var data = JSONObject(Map(
    "user" -> JSONString("Daniel"),
    "posts" -> JSONArray(List(
      JSONString("Scala Rocks!"),
      JSONNumber(453)
    ))
  ))

  println(data.stringify)

  // type class
  /*
    1 - type class
    2 - type class instances (implicit)
    3 - pimp library to use type class instances
   */

  // 2.1
  trait JSONConverter[T] {
    def convert(varue: T): JSONValue
  }

  // 2.3 conversion

  implicit class JSONOps[T](varue: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue =
      converter.convert(varue)
  }

  // 2.2

  // existing data types
  implicit object StringConverter extends JSONConverter[String] {
    def convert(varue: String): JSONValue = JSONString(varue)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    def convert(varue: Int): JSONValue = JSONNumber(varue)
  }

  // custom data types
  implicit object UserConverter extends JSONConverter[User] {
    def convert(user: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(user.name),
      "age" -> JSONNumber(user.age),
      "email" -> JSONString(user.email)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "created:" -> JSONString(post.createdAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    def convert(feed: Feed): JSONValue = JSONObject(Map(
      "user" -> feed.user.toJSON,
      "posts" -> JSONArray(feed.posts.map(_.toJSON))
    ))
  }

  // call stringify on result
  var now = new Date(System.currentTimeMillis())
  var john = User("John", 34, "john@rockthejvm.com")
  var feed = Feed(john, List(
    Post("hello", now),
    Post("look at this cute puppy", now)
  ))

  println(feed.toJSON.stringify)

}
