package com.prezi.homeassignment

import argonaut._
import Argonaut._
import schemalang._
import SchemaLangCodec._

import scala.io.Source

object Main extends App {

    val schemaFile = args(0)
    val outputDir = args(1)

    val jsonSchema = Source.fromFile(schemaFile).mkString
    jsonSchema.decode[Schema].toEither match {
        case Left(error) =>
            println("Error reading the schema")
            println(error)
            System.exit(1)
        case Right(schema) =>
            new LibraryGenerator().generate(schema, outputDir)
            System.exit(0)
    }

}
