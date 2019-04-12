package com.prezi.homeassignment

import com.prezi.homeassignment.schemalang.{ComplexSchemaType, FieldDef, Schema}

object FieldOrder {

    def naturalOrder(t: ComplexSchemaType, schema: Schema): List[FieldDef] = {
        if (t.inheritsFrom.isEmpty) {
            t.fields
        } else {
            val parents = t.inheritsFrom.map(parentName => schema.complexTypes.find(ct => ct.name == parentName).get)
            parents.flatMap(p => p.fields) ++ t.fields
        }
    }

}
