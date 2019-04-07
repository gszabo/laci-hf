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

        describe("inheriting") {
            it("from one parent") {
                val t = ComplexSchemaType("MyEmptyAbstractType", isAbstract = true, inheritsFrom = List("Parent"))
                val g = new Generator()
                val result = g.generate(t)
                assert(result.fileName === "MyEmptyAbstractType.java")
                assert(result.contents ===
                    """public interface MyEmptyAbstractType extends Parent {
                      #}
                      #""".stripMargin('#')
                )
            }

            it("from multiple parents") {
                val t = ComplexSchemaType("MyEmptyAbstractType", isAbstract = true, inheritsFrom = List("Parent1", "Parent2"))
                val g = new Generator()
                val result = g.generate(t)
                assert(result.fileName === "MyEmptyAbstractType.java")
                assert(result.contents ===
                    """public interface MyEmptyAbstractType extends Parent1, Parent2 {
                      #}
                      #""".stripMargin('#')
                )
            }
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