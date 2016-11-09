package typelevel

import org.scalatest.FunSuite

class FunctorTest extends FunSuite {
  test("instances") {
    import cats.Functor
    import cats.instances.list._
    
    val list = List(1, 2)
    val result = Functor[List].map(list)(_ * 1)
    assert(list === result)
  }
  
  test("lift") {
    import cats.Functor
    import cats.instances.option._
    import cats.syntax.option._
    
    val double = (x: Int) => x * x
    val lifted = Functor[Option].lift(double)
    assert(lifted(3.some) == 9.some)
  }
  
  test("syntax") {
    import cats.instances.function._
    import cats.syntax.functor._
    
    val double = (x: Int) => x * x
    val triple = (x: Int) => x * x * x
    val combo = double.map(triple)
    
    assert(combo(3) == 729)
  }
}