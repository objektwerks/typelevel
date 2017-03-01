package objektwerks.cats

import org.scalatest.{FunSuite, Matchers}

class FunctorTest extends FunSuite with Matchers {
  test("instances") {
    import cats.Functor
    import cats.instances.list._
    
    val list = List(1, 2)
    val result = Functor[List].map(list)(_ * 1)
    list shouldEqual result
  }
  
  test("lift") {
    import cats.Functor
    import cats.instances.option._
    import cats.syntax.option._
    
    val double = (x: Int) => x * x
    val lifted = Functor[Option].lift(double)
    lifted(3.some) shouldEqual 9.some
  }
  
  test("syntax") {
    import cats.instances.function._
    import cats.syntax.functor._
    
    val double = (x: Int) => x * x
    val triple = (x: Int) => x * x * x
    val combo = double.map(triple)
    
    combo(3) shouldEqual 729
  }
}