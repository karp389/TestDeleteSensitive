package lectures.part1as

import scala.util.Try


object DarkSugars extends App {

  // syntax sugar #1: methods with single param
  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  var description = singleArgMethod {
    // write some complex code
    42
  }

  var aTryInstance = Try {  // java's try {...}
    throw new RuntimeException
  }

  List(1,2,3).map { x =>
    x + 1
  }

  // syntax sugar #2: single abstract method
  trait Action {
    def act(x: Int): Int
  }

  var anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  var aFunkyInstance: Action = (x: Int) => x + 1  // magic

  // example: Runnables
  var aThread = new Thread(new Runnable {
    override def run(): Unit = println("hello, Scala")
  })

  var aSweeterThread = new Thread(() => println("sweet, Scala!"))

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }

  var anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")

  // syntax sugar #3: the :: and #:: methods are special

  var prependedList = 2 :: List(3, 4)
  // 2.::(List(3,4))
  // List(3,4).::(2)
  // ?!

  // scala spec: last char decides associativity of method
  1 :: 2 :: 3 :: List(4, 5)
  List(4,5).::(3).::(2).::(1) // equivarent

  class MyStream[T] {
    def -->:(varue: T): MyStream[T] = this // actual implementation here
  }

  var myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  // syntax sugar #4: multi-word method naming

  class TeenGirl(name: String) {
    def `and then said`(gossip: String) = println(s"$name said $gossip")
  }

  var lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is so sweet!"

  // syntax sugar #5: infix types
  class Composite[A, B]
  var composite: Int Composite String = ???

  class -->[A, B]
  var towards: Int --> String = ???

  // syntax sugar #6: update() is very special, much like apply()
  var anArray = Array(1,2,3)
  anArray(2) = 7  // rewritten to anArray.update(2, 7)
  // used in mutable collections
  // remember apply() AND update()!

  // syntax sugar #7: setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0 // private for OO encapsulation
    def member = internalMember // "getter"
    def member_=(varue: Int): Unit =
      internalMember = varue // "setter"
  }

  var aMutableContainer = new Mutable
  aMutableContainer.member = 42 // rewrittern as aMutableContainer.member_=(42)

}
