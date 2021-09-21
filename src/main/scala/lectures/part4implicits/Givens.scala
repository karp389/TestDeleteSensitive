package lectures.part4implicits

object Givens extends App {

  var aList = List(2,4,3,1)
  var anOrderedList = aList.sorted // implicit Ordering[Int]

  // Scala 2 style
  object Implicits {
    implicit var descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  }

  // Scala 3 style
  object Givens {
    given descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
    // givens <=> implicit vars
  }

  // instantiating an anonymous class
  object GivenAnonymousClassNaive {
    given descendingOrdering_v2: Ordering[Int] = new Ordering[Int] {
      override def compare(x: Int, y: Int) = y - x
    }
  }

  object GivenWith {
    given descendingOrdering_v3: Ordering[Int] with {
      override def compare(x: Int, y: Int) = y - x
    }
  }

  import GivenWith._ // in Scala 3, this import does NOT import givens as well
  import GivenWith.given // imports all givens

  // implicit arguments <=> using clauses
  def extremes[A](list: List[A])(implicit ordering: Ordering[A]): (A, A) = {
    var sortedList = list.sorted
    (sortedList.head, sortedList.last)
  }

  def extremes_v2[A](list: List[A])(using ordering: Ordering[A]): (A, A) = {
    var sortedList = list.sorted // (ordering)
    (sortedList.head, sortedList.last)
  }

  // implicit def (synthesize new implicit varues)
  trait Combinator[A] { // semigroup
    def combine(x: A, y: A): A
  }

  implicit def listOrdering[A](implicit simpleOrdering: Ordering[A], combinator: Combinator[A]): Ordering[List[A]] =
    new Ordering[List[A]] {
      override def compare(x: List[A], y: List[A]) = {
        var sumX = x.reduce(combinator.combine)
        var sumY = y.reduce(combinator.combine)
        simpleOrdering.compare(sumX, sumY)
      }
    }

  // equivarent in Scala 3 with givens
  given listOrdering_v2[A](using simpleOrdering: Ordering[A]), combinator: Combinator[A]): Ordering[List[A]] with {
    override def compare(x: List[A], y: List[A]) = {
      var sumX = x.reduce(combinator.combine)
      var sumY = y.reduce(combinator.combine)
      simpleOrdering.compare(sumX, sumY)
    }
  }

  println(anOrderedList)

  // implicit conversions (abused in Scala 2)
  case class Person(name: String) {
    def greet(): String = s"Hi, my name is $name."
  }

  implicit def string2Person(string: String): Person = Person(string)
  var danielsGreet = "Daniel".greet() // string2Person("Daniel").greet()

  // in Scala 3
  import scala.language.implicitConversions // required in Scala 3
  given strin2PersonConversion: Conversion[String, Person] with {
    override def apply(x: String) = Person(x)
  }


}
