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

        describe("having fields adds getters and setters") {
            it("One non-list field") {
                checkGeneratedSource(
                    ComplexSchemaType(
                        "MyAbstractType",
                        isAbstract = true,
                        fields = List(FieldDef("fieldName", isList = false, "TypeOfField"))
                    )
                ) {
                    """public interface MyAbstractType {
                      #
                      #    TypeOfField getFieldName();
                      #    void setFieldName(TypeOfField value);
                      #
                      #}
                      #""".stripMargin('#')
                }
            }

            it("One list field") {
                checkGeneratedSource(
                    ComplexSchemaType(
                        "MyAbstractType",
                        isAbstract = true,
                        fields = List(FieldDef("fieldName", isList = true, "TypeOfField"))
                    )
                ) {
                    """public interface MyAbstractType {
                      #
                      #    TypeOfFieldVector getFieldName();
                      #    void setFieldName(TypeOfFieldVector value);
                      #
                      #}
                      #""".stripMargin('#')
                }
            }
        }
    }

    describe("If package name is given") {
        it("includes the package name in the generated code") {
            checkGeneratedSource(
                ComplexSchemaType("MyEmptyAbstractType", isAbstract = true),
                packageName = Some("com.package.name")
            ) {
                """package com.package.name;
                  #
                  #public interface MyEmptyAbstractType {
                  #}
                  #""".stripMargin('#')
            }
        }
    }

    def checkGeneratedSource(t: ComplexSchemaType, packageName: Option[String] = None)(expectedSourceCode: String): Unit = {
        val g = new Generator(packageName)
        val result = g.generate(t)
        assert(result.fileName === s"${t.name}.java")
        assert(result.contents === expectedSourceCode)
    }

}