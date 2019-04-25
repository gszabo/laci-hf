package com.prezi.homeassignment

import com.prezi.homeassignment.schemalang.{ComplexSchemaType, FieldDef}

class Generator(val packageName: Option[String] = None) {

    def generate(typeDef: ComplexSchemaType): GeneratorResult = {
        if (typeDef.isAbstract) {
            generateInterface(typeDef)
        } else {
            generateClass(typeDef)
        }
    }

    private def generateClass(typeDef: ComplexSchemaType) = {
        val className = typeDef.name.toString()
        GeneratorResult(
            typeDef.name.toString() + ".java",
            packageDeclaration +
            s"""public class $className {
               #
               #    public $className(
               #    ) {
               #    }
               #
               #    public String toJson() {
               #        StringBuilder result = new StringBuilder();
               #
               #        result.append("{\\"def\\":\\"$className\\"");
               #
               #        result.append("}");
               #
               #        return result.toString();
               #    }
               #
               #}
               #""".stripMargin('#')
        )
    }

    private def generateInterface(typeDef: ComplexSchemaType) = {
        GeneratorResult(
            typeDef.name.toString() + ".java",
            packageDeclaration +
            s"""public interface ${typeDef.name.toString()}${extendsDeclaration(typeDef)} {
               #${fields(typeDef)}}
               #""".stripMargin('#')
        )
    }

    def generateVector(typeName: String): GeneratorResult = {
        val vectorVariableName = typeName.charAt(0).toLower + typeName.substring(1) + "List"
        val className = typeName + "Vector"
        GeneratorResult(
            className + ".java",
            packageDeclaration +
            s"""public class $className extends ArrayListVector<$typeName> {
               #
               #    public $className($typeName... $vectorVariableName) {
               #        super($vectorVariableName);
               #    }
               #
               #}
               #""".stripMargin('#')
        )
    }

    def packageDeclaration: String = packageName.fold("") (name => s"package $name;\n\n")

    def extendsDeclaration(typeDef: ComplexSchemaType): String = {
        if (typeDef.inheritsFrom.nonEmpty) {
            s" extends ${typeDef.inheritsFrom.mkString(", ")}"
        } else {
            ""
        }
    }

    def fields(typeDef: ComplexSchemaType): String = {
        if (typeDef.fields.nonEmpty) {
            "\n" + typeDef.fields.map(oneFieldSection).mkString("\n") + "\n"
        } else {
            ""
        }
    }

    def oneFieldSection(f: FieldDef): String = {
        val indentation = " " * 4
        List(getter(f), setter(f)).map(indentation + _).mkString("\n") + "\n"
    }

    def getter(f: FieldDef): String = s"${typeNameOfField(f)} get${f.name.toString().capitalize}();"

    def setter(f: FieldDef): String = s"void set${f.name.toString().capitalize}(${typeNameOfField(f)} value);"

    def typeNameOfField(f: FieldDef): String = {
        if (f.isList) {
            s"${f.typeName.toString()}Vector"
        } else {
            f.typeName.toString()
        }
    }

}

case class GeneratorResult(fileName: String, contents: String)
