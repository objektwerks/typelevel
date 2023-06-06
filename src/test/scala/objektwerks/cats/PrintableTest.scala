package objektwerks.cats

import org.scalatest.funsuite.AnyFunSuite

trait Printable[A] {
  def format(value: A): String
}

object PrintableInstances {
  implicit val stringPrintable: Printable[String] = new Printable[String] {
    def format(input: String) = input
  }
  implicit val intPrintable: Printable[Int] = new Printable[Int] {
    def format(input: Int) = input.toString
  }
}

object PrintableSyntax {
  implicit class PrintableOps[A](value: A) {
    def format(implicit printable: Printable[A]): String = printable.format(value)
    def print(implicit printable: Printable[A]): Unit = println(printable.format(value))
  }
}

object Printable {
  def format[A](input: A)(implicit printable: Printable[A]): String = printable.format(input)
  def print[A](input: A)(implicit printable: Printable[A]): Unit = println(format(input))
}

case class Person(name: String, age: Int)

object Person {
  import PrintableInstances._
  implicit val personPrintable: Printable[Person] = new Printable[Person] {
    def format(person: Person) = {
      val name = Printable.format(person.name)
      val age = Printable.format(person.age)
      s"name: $name, age: $age"
    }
  }
}

class PrintableTest extends AnyFunSuite {
  test("instances") {
    val fred = Person(name = "fred", age = 33)
    assert(Printable.format(fred).nonEmpty)
    Printable.print(fred)
  }

  test("syntax") {
    import PrintableSyntax._
    val barney = Person(name = "barney", age = 32)
    assert(barney.format.nonEmpty)
    barney.print
  }
}