import com.prezi.homeassignment._
import com.prezi.homeassignment.schemalang._
import com.prezi.homeassignment.schemalang.SchemaLang._

import org.scalatest.FunSpec

class FieldOrderTest extends FunSpec {

    describe("Natural field order") {
        it("is empty if type does not inherit and does not have fields") {
            val t = ComplexSchemaType("typeName", inheritsFrom = Nil, fields = Nil)
            assert(FieldOrder.naturalOrder(t, Schema(List(t))) === Nil)
        }

        it("is the fields of the type if it does not inherit anything") {
            val fields = List(
                FieldDef("field1", isList = false, "Type1"),
                FieldDef("field2", isList = false, "Type2")
            )
            val t = ComplexSchemaType("typeName", inheritsFrom = Nil, fields = fields)
            assert(FieldOrder.naturalOrder(t, Schema(List(t))) === fields)
        }

        it("inherited fields come before own fields") {
            val parentFields = List(
                FieldDef("field1", isList = false, "Type1"),
                FieldDef("field2", isList = false, "Type2")
            )
            val parentType = ComplexSchemaType("parentTypeName", isAbstract = true, fields = parentFields)
            val ownFields = List(
                FieldDef("field3", isList = false, "Type3"),
                FieldDef("field4", isList = false, "Type4")
            )
            val t = ComplexSchemaType("typeName", inheritsFrom = List("parentTypeName"), fields = ownFields)
            val schema = Schema(List(t, parentType))
            assert(FieldOrder.naturalOrder(t, schema) === (parentFields ++ ownFields))
        }

        it("inherited fields from multiple parents come in the order of inheritance") {
            val parent1Fields = List(
                FieldDef("field1", isList = false, "Type1")
            )
            val parent2Fields = List(
                FieldDef("field2", isList = false, "Type2")
            )
            val parent1Type = ComplexSchemaType("parent1TypeName", isAbstract = true, fields = parent1Fields)
            val parent2Type = ComplexSchemaType("parent2TypeName", isAbstract = true, fields = parent2Fields)
            val ownFields = List(
                FieldDef("field3", isList = false, "Type3"),
                FieldDef("field4", isList = false, "Type4")
            )
            val t = ComplexSchemaType(
                "typeName",
                inheritsFrom = List("parent2TypeName", "parent1TypeName"),
                fields = ownFields
            )
            val schema = Schema(List(t, parent1Type, parent2Type))
            assert(FieldOrder.naturalOrder(t, schema) === (parent2Fields ++ parent1Fields ++ ownFields))
        }
    }

}
