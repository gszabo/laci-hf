package com.prezi.homeassignment;
import com.prezi.homeassignment.schemalang.*;

import java.io.File;

class LibraryGenerator {
    // TODO: write your generator here

    void generate(Schema schema, File outputDir) {

        validateSchema(schema);

        // if there are no schema errors, generate files in the `outputDir`
        // Create outputDir, and put your result there

        throw new UnsupportedOperationException("Generator logic is missing");
    }

    private void validateSchema(Schema schema) {
        // in case of errors, throw SchemaValidationException with a human readable message

        throw new SchemaValidationException("coki");

    }
}

