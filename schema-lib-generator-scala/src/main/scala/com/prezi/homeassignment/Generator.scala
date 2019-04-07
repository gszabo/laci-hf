package com.prezi.homeassignment

import com.prezi.homeassignment.schemalang.{ComplexSchemaType, FieldDef}

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
               |    ${typeNameOfField(f)} get${f.name.toString().capitalize}();
               |    void set${f.name.toString().capitalize}(${typeNameOfField(f)} value);
               |
               |""".stripMargin
        } else {
            ""
        }
    }

    def typeNameOfField(f: FieldDef): String = {
        if (f.isList) {
            s"${f.typeName.toString()}Vector"
        } else {
            f.typeName.toString()
        }
    }

}

case class GeneratorResult(fileName: String, contents: String)
