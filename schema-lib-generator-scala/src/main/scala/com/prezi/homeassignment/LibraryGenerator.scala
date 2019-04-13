package com.prezi.homeassignment

import java.nio.file.{Files, Path, Paths}

import com.prezi.homeassignment.schemalang.Schema
import org.apache.commons.io.FileUtils
import schemalang._

class LibraryGenerator {

    private val packageName = "com.prezi.homeassignment.schemalib"

    def generate(schema: Schema, outputDir: String): Unit = {
        validateSchema(schema)
        ensureEmptyOutputDirectory(outputDir)
        generateFiles(schema, outputDir)
    }

    private def validateSchema(schema: Schema) = {
        // in case of errors, throw SchemaValidationException with a human readable message
    }

    private def ensureEmptyOutputDirectory(outputDir: String): Unit = {
        val path = Paths.get(outputDir)
        if (Files.exists(path)) {
            deleteDirectory(path)
        }
        Files.createDirectory(path)
    }

    private def deleteDirectory(path: Path): Unit = {
        FileUtils.deleteDirectory(path.toFile)
    }

    private def generateFiles(schema: Schema, outputDir: String) = {
        val generator = new Generator(Some(packageName))

        schema.complexTypes
            .map(generator.generate)
            .foreach(write(_, outputDir))

        fieldTypesUsedAsVector(schema)
            .map(t => generator.generateVector(t.toString))
            .foreach(write(_, outputDir))
    }

    private def fieldTypesUsedAsVector(schema: Schema): List[TypeName] = {
        schema.complexTypes
            .flatMap(ct => ct.fields)
            .filter(fieldDef => fieldDef.isList)
            .map(fieldDef => fieldDef.typeName)
            .distinct
    }

    private def write(r: GeneratorResult, outputDir: String): Unit = {
        Files.write(
            Paths.get(outputDir, r.fileName),
            r.contents.getBytes("utf-8")
        )
    }
}
