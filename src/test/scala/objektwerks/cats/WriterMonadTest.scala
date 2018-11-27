package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class WriterMonadTest extends FunSuite with Matchers {
  test("writer monad") {
    import cats.data.Writer

    val writer = Writer(Vector("A joke is a very serious thing.", "Winston Churchill"), 1939)
    val (quotes, year) = writer.run
    quotes.size shouldEqual 2
    year shouldEqual 1939
  }
}