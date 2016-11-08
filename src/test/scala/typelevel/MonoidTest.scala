package typelevel

import org.scalatest.FunSuite

class MonoidTest extends FunSuite {
  test("instances") {
    import cats.Semigroup
    import cats.Monoid
    import cats.instances.int._
    import cats.instances.option._
    import cats.syntax.option._
    
    assert(Semigroup[Int].combine(1, 2) == 3)
    assert(Monoid[Int].combine(1, 2) == 3)
    assert(Monoid[Option[Int]].combine(1.some, 2.some) == 3.some)
  }
  
  test("syntax") {
    import cats.Monoid
    import cats.syntax.semigroup._
    import cats.instances.int._
    
    val x = 1 |+| 2 |+| Monoid[Int].empty
    assert(x == 3)
  }
}