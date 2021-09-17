package lectures.part4implicits

// TYPE CLASS
trait MyTypeClassTemplate[T] {
  def action(varue: T): String
}

object MyTypeClassTemplate {
  def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
}