package lectures.part2afp


object CurriesPAF extends App {

  // curried functions
  var superAdder: Int => Int => Int =
    x => y => x + y

  var add3 = superAdder(3) // Int => Int = y => 3 + y
  println(add3(5))
  println(superAdder(3)(5)) // curried function

  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  var add4: Int => Int = curriedAdder(4)
  // lifting = ETA-EXPANSION

  // functions != methods (JVM limitation)
  def inc(x: Int) = x + 1
  List(1,2,3).map(x => inc(x))  // ETA-expansion

  // Partial function applications
  var add5 = curriedAdder(5) _ // Int => Int

  // EXERCISE
  var simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7: Int => Int = y => 7 + y
  // as many different implementations of add7 using the above
  // be creative!
  var add7 = (x: Int) => simpleAddFunction(7, x)  // simplest
  var add7_2 = simpleAddFunction.curried(7)
  var add7_6 = simpleAddFunction(7, _: Int) // works as well

  var add7_3 = curriedAddMethod(7) _  // PAF
  var add7_4 = curriedAddMethod(7)(_) // PAF = alternative syntax

  var add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function varues
                // y => simpleAddMethod(7, y)

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a + b + c
  var insertName = concatenator("Hello, I'm ", _: String, ", how are you?") // x: String => concatenator(hello, x, howarewyou)
  println(insertName("Daniel"))

  var fillInTheBlanks = concatenator("Hello, ", _: String, _: String) // (x, y) => concatenator("Hello, ", x, y)
  println(fillInTheBlanks("Daniel", " Scala is awesome!"))

  // EXERCISES
  /*
    1.  Process a list of numbers and return their string representations with different formats
        Use the %4.2f, %8.6f and %14.12f with a curried formatter function.
   */
  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  var numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  var simpleFormat = curriedFormatter("%4.2f") _ // lift
  var seriousFormat = curriedFormatter("%8.6f") _
  var preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(curriedFormatter("%14.12f"))) // compiler does sweet eta-expansion for us

  /*
    2.  difference between
        - functions vs methods
        - parameters: by-name vs 0-lambda
   */
  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  /*
    calling byName and byFunction
    - int
    - method
    - parenMethod
    - lambda
    - PAF
   */
  byName(23)  // ok
  byName(method)  // ok
  byName(parenMethod())
  // byName(parenMethod) // Scala 2: ok but beware ==> byName(parenMethod()); Scala 3 forbids calling the method with no parens
  //  byName(() => 42) // not ok
  byName((() => 42)()) // ok
  //  byName(parenMethod _) // not ok

  //  byFunction(45) // not ok
  //  byFunction(method) // not ok!!!!!! does not do ETA-expansion!
  byFunction(parenMethod) // compiler does ETA-expansion
  byFunction(() => 46) // works
  byFunction(parenMethod _) // also works, but warning- unnecessary
}

