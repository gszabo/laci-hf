package com.prezi.homeassignment
import com.prezi.homeassignment.schemalang.Schema
import schemalang._, SchemaLang._

class LibraryGenerator {

    // TODO: write your generator here

    def generate(schema: Schema, outputDir: String): Unit = {

        validateSchema(schema)

        // if there are no schema errors, generate files in the `outputDir`
        // Create outputDir, and put your result there

        ???
    }

    private def validateSchema(schema: Schema) = {
        // in case of errors, throw SchemaValidationException with a human readable message
    }
}
