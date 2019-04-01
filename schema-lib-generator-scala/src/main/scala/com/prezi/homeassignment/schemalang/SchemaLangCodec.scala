package com.prezi.homeassignment.schemalang

import argonaut._, Argonaut._

object SchemaLangCodec {

    implicit def TypeNameCodecJson: CodecJson[TypeName] =
        CodecJson(
            (t: TypeName) => jString(t.value),
            c => for (name <- c.as[String]) yield new TypeName(name)
        )

    implicit def FieldNameCodecJson: CodecJson[FieldName] =
        CodecJson(
            (t: FieldName) => jString(t.value),
            c => for (name <- c.as[String]) yield new FieldName(name)
        )

    implicit def PersonCodecJson: CodecJson[ComplexSchemaType] =
        casecodec4(ComplexSchemaType.apply, ComplexSchemaType.unapply)("name", "abstract", "inherits", "fields")

    implicit def DefCodecJson: CodecJson[Schema] =
        casecodec1(Schema.apply, Schema.unapply)("defs")

    implicit def FiedDefCodecJson: CodecJson[FieldDef] =
        casecodec3(FieldDef.apply, FieldDef.unapply)("name", "list", "type")
}
