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

    describe("If package name is given") {
        it("includes the package name in the generated code") {
            val t = ComplexSchemaType("MyEmptyAbstractType", isAbstract = true)
            val g = new Generator(Some("com.package.name"))
            val result = g.generate(t)
            assert(result.fileName === "MyEmptyAbstractType.java")
            assert(result.contents ===
                """package com.package.name;
                  #
                  #public interface MyEmptyAbstractType {
                  #}
                  #""".stripMargin('#')
            )
        }
    }

}