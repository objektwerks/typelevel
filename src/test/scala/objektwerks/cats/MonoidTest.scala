package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

case class Amount(value: Double)

object Amount {
  import cats.Monoid
  implicit val amountMonoid: Monoid[Amount] = new Monoid[Amount] {
    def empty: Amount = Amount(0)
    def combine(x: Amount, y: Amount): Amount = Amount(x.value + y.value)  
  }
}

class MonoidTest extends FunSuite with Matchers {
  test("instances") {
    import cats.instances.int._
    import cats.instances.option._
    import cats.syntax.option._
    import cats.{Monoid, Semigroup}
    
    Semigroup[Int].combine(1, 2) shouldEqual 3
    Monoid[Int].combine(1, 2) shouldEqual 3
    Monoid[Option[Int]].combine(1.some, 2.some) shouldEqual 3.some
  }
  
  test("syntax") {
    import cats.Monoid
    import cats.instances.int._
    import cats.syntax.monoid._
    
    val x = 1 |+| 2 |+| Monoid[Int].empty
    x shouldEqual 3
  }
  
  test("custom") {
    import cats.syntax.monoid._
    
    val amount = Amount(1) |+| Amount(2)
    amount shouldEqual Amount(3)
  }
}