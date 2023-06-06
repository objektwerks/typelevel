package objektwerks.shapeless

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import shapeless._

class ShapelessTest extends AnyFunSuite with Matchers {
  test("hlist") {
    val hlist = 3 :: "3" :: HNil

    hlist.head shouldBe 3
    hlist.tail shouldBe "3" :: HNil
  }

  test("coproduct") {
    object Green
    object Red
    object Yellow
    type Light = Green.type :+: Red.type :+: Yellow.type :+: CNil

    val green: Light = Coproduct[Light](Green)
    val red: Light = Coproduct[Light](Red)
    val yellow: Light = Coproduct[Light](Yellow)

    green.select[Green.type] shouldBe Some(Green)
    red.select[Red.type] shouldBe Some(Red)
    yellow.select[Yellow.type] shouldBe Some(Yellow)
  }

  test("conversion") {
    case class User(name: String, age: Int)

    val user = User("fred flintstone", 27)
    val userHList = Generic[User].to(user)

    userHList shouldBe user.name :: user.age :: HNil
  }

  test("polymorphic") {
    object polyLength extends Poly1 {
      implicit val listCase: Case.Aux[List[Int], Int] = at[List[Int]](i => i.length)
      implicit val stringCase: Case.Aux[String, Int] = at[String](s => s.length)
    }
    
    polyLength.apply(List(1, 2)) shouldBe 2
    polyLength.apply("123") shouldBe 3
    List("1", "23", "456").map(polyLength) shouldBe List(1, 2, 3)
  }
}