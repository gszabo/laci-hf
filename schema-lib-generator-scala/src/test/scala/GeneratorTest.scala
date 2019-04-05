import com.prezi.homeassignment._
import com.prezi.homeassignment.schemalang._
import com.prezi.homeassignment.schemalang.SchemaLang._

import org.scalatest.FunSpec

class GeneratorTest extends FunSpec {

    describe("Abstract types") {
        it("Empty abstract type generates empty interface") {
            val t = ComplexSchemaType("MyEmptyAbstractType", isAbstract = true)
            val g = new Generator()
            val result = g.generate(t)
            assert(result.fileName === "MyEmptyAbstractType.java")
            assert(result.contents ===
              """public interface MyEmptyAbstractType {
                #}
                #""".stripMargin('#')
            )
        }
    }

    // todo: reminder to include package name in the generator interface somehow

}