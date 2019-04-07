package com.prezi.homeassignment

import com.prezi.homeassignment.schemalang.ComplexSchemaType

class Generator(val packageName: Option[String] = None) {

    def generate(typeDef: ComplexSchemaType): GeneratorResult = {
        GeneratorResult(
            typeDef.name.toString() + ".java",
            packageDeclaration +
            s"""public interface ${typeDef.name.toString()}${extendsDeclaration(typeDef)} {
               #${fields(typeDef)}}
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
            val f = typeDef.fields.head
            s"""
               |    ${f.typeName.toString()} get${f.name.toString().capitalize}();
               |    void set${f.name.toString().capitalize}(${f.typeName.toString()} value);
               |
               |""".stripMargin
        } else {
            ""
        }
    }

}

case class GeneratorResult(fileName: String, contents: String)
