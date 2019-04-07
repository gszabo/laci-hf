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
