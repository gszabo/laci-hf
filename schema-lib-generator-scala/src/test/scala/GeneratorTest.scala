import com.prezi.homeassignment._
import com.prezi.homeassignment.schemalang._
import com.prezi.homeassignment.schemalang.SchemaLang._

import org.scalatest.FunSpec

class GeneratorTest extends FunSpec {

    describe("Abstract types") {
        it("Empty abstract type generates empty interface") {
            checkGeneratedSource(
                ComplexSchemaType("MyEmptyAbstractType", isAbstract = true)
            ) {
                """public interface MyEmptyAbstractType {
                  #}
                  #""".stripMargin('#')
            }
        }

        describe("inheriting adds an extends clause to the interface declaration") {
            it("from one parent") {
                checkGeneratedSource(
                    ComplexSchemaType("MyEmptyAbstractType", isAbstract = true, inheritsFrom = List("Parent"))
                ) {
                    """public interface MyEmptyAbstractType extends Parent {
                      #}
                      #""".stripMargin('#')
                }
            }

            it("from multiple parents") {
                checkGeneratedSource(
                    ComplexSchemaType(
                        "MyEmptyAbstractType",
                        isAbstract = true,
                        inheritsFrom = List("Parent1", "Parent2")
                    )
                ) {
                    """public interface MyEmptyAbstractType extends Parent1, Parent2 {
                      #}
                      #""".stripMargin('#')
                }
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

    def checkGeneratedSource(t: ComplexSchemaType)(expectedSourceCode: String): Unit = {
        val g = new Generator()
        val result = g.generate(t)
        assert(result.fileName === s"${t.name}.java")
        assert(result.contents === expectedSourceCode)
    }

}