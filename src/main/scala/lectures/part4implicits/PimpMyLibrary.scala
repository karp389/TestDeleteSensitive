package lectures.part4implicits


object PimpMyLibrary extends App {

  // 2.isPrime

  implicit class RichInt(var varue: Int) extends AnyVal {
    def isEven: Boolean = varue % 2 == 0
    def sqrt: Double = Math.sqrt(varue)

    def times(function: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if (n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }

      timesAux(varue)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] =
        if (n <= 0) List()
        else concatenate(n - 1) ++ list

      concatenate(varue)
    }

  }

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.varue % 2 != 0
  }

  new RichInt(42).sqrt

  42.isEven // new RichInt(42).isEven
  // type enrichment = pimping

  1 to 10

  import scala.concurrent.duration._
  3.seconds

  // compiler doesn't do multiple implicit searches.
  //  42.isOdd

  /*
    Enrich the String class
    - asInt
    - encrypt
      "John" -> Lqjp

    Keep enriching the Int class
    - times(function)
      3.times(() => ...)
    - *
      3 * List(1,2) => List(1,2,1,2,1,2)
   */

  implicit class RichString(string: String) {
    def asInt: Int = Integer.varueOf(string) // java.lang.Integer -> Int
    def encrypt(cypherDistance: Int): String = string.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  println("3".asInt + 4)
  println("John".encrypt(2))

  3.times(() => println("Scala Rocks!"))
  println(4 * List(1,2))

  // "3" / 4
  implicit def stringToInt(string: String): Int = Integer.varueOf(string)
  println("6" / 2) // stringToInt("6") / 2

  // equivarent: implicit class RichAltInt(varue: Int)
  class RichAltInt(varue: Int)
  implicit def enrich(varue: Int): RichAltInt = new RichAltInt(varue)

  // danger zone
  implicit def intToBoolean(i: Int): Boolean = i == 1

  /*
    if (n) do something
    else do something else
   */

  var aConditionedValue = if (3) "OK" else "Something wrong"
  println(aConditionedValue)
}
