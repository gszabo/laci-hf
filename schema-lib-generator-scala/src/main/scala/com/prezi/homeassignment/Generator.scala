package com.prezi.homeassignment

import com.prezi.homeassignment.schemalang.ComplexSchemaType

class Generator(val packageName: Option[String] = None) {

    def generate(typeDef: ComplexSchemaType): GeneratorResult = {
        GeneratorResult(
            typeDef.name.toString() + ".java",
            packageDeclaration +
            s"""public interface ${typeDef.name.toString()} {
               #}
               #""".stripMargin('#')
        )
    }

    def packageDeclaration: String = packageName.fold("") (name => s"package $name;\n\n")

}

case class GeneratorResult(fileName: String, contents: String)