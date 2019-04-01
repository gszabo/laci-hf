package com.prezi.homeassignment.schemalang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SchemaType {
    private TypeName name;

    public TypeName getName() {
        return name;
    }

    @JsonCreator
    protected SchemaType(
        @JsonProperty("name") TypeName name)
    {
        this.name = name;
    }
}
