package com.prezi.homeassignment

import com.prezi.homeassignment.schemalang.ComplexSchemaType

class Generator {

    def generate(typeDef: ComplexSchemaType): GeneratorResult = {
        GeneratorResult(
            typeDef.name.toString() + ".java",
            s"""public interface ${typeDef.name.toString()} {
              #}
              #""".stripMargin('#')
        )
    }

}

case class GeneratorResult(fileName: String, contents: String)