package com.prezi.homeassignment.schemalang

import scala.language.implicitConversions

case class Schema(complexTypes: List[ComplexSchemaType]) {
    def primitiveTypes: List[PrimitiveSchemaType] = List(
        PrimitiveSchemaType(new TypeName("String"))
    )
}

sealed trait SchemaType {
    def name: TypeName
}

case class PrimitiveSchemaType(name: TypeName) extends SchemaType


case class ComplexSchemaType(
                                name: TypeName,
                                isAbstract: Boolean = false,
                                inheritsFrom: List[TypeName] = Nil,
                                fields: List[FieldDef] = Nil) extends SchemaType


case class FieldDef(name: FieldName, isList: Boolean, typeName: TypeName)

object SchemaLang {
    implicit def typeNameWrapper(x: String): TypeName = new TypeName(x)

    implicit def fieldNameWrapper(x: String): FieldName = new FieldName(x)
}

class TypeName(val value: String) extends AnyVal {
    override def toString: String = value
}

class FieldName(val value: String) extends AnyVal {
    override def toString: String = value
}
