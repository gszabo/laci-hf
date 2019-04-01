package com.prezi.homeassignment.schemalang;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Schema {

    @JsonProperty("defs")
    private ComplexSchemaType[] complexTypes;

    public PrimitiveSchemaType[] primitiveTypes;

    public ComplexSchemaType[] getComplexTypes() {
        return complexTypes;
    }

    public PrimitiveSchemaType[] getPrimitiveTypes() {
        return new PrimitiveSchemaType[]{
            new PrimitiveSchemaType(new TypeName("String"))
        };

    }
}

