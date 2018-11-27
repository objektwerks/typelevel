package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class ReaderMonadTest extends FunSuite with Matchers {
  test("reader monad") {
    import cats.data.Reader

    case class Name(value: String)
    val nameReader: Reader[Name, String] = Reader(name => name.value)
    nameReader.run(Name("Fred")).toString shouldEqual "Fred"
    val greetingReader: Reader[Name, String] = nameReader.map(value => s"Hello, $value!")
    greetingReader.run(Name("Barney")).toString shouldEqual "Hello, Barney!"
    val nameGreetingReader: Reader[Name, String] = for {
      name <- nameReader
      greeting <- greetingReader
    } yield s"$name - $greeting"
    nameGreetingReader.run(Name("Wilma")).toString shouldEqual "Wilma - Hello, Wilma!"
  }
}