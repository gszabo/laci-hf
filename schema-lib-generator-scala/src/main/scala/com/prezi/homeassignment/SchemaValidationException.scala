package com.prezi.homeassignment

case class SchemaValidationException(message: String = "")
    extends Exception(message)