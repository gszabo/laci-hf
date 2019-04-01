package com.prezi.homeassignment.schemalang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldDef {

    private FieldName name;

    private Boolean isList;

    private TypeName typeName;

    private FieldDef(){}

    @JsonCreator
    protected FieldDef(
        @JsonProperty("name") FieldName name,
        @JsonProperty("list") Boolean isList,
        @JsonProperty("type") TypeName typeName) {

        this.name = name;
        this.isList = isList;
        this.typeName = typeName;
    }

    public FieldName getName() {
        return name;
    }

    public Boolean isList() {
        return isList;
    }

    public TypeName getTypeName() {
        return typeName;
    }
}
