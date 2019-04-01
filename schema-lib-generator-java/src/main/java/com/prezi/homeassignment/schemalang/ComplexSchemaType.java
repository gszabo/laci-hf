package com.prezi.homeassignment.schemalang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ComplexSchemaType extends SchemaType {

    private boolean isAbstract;

    private TypeName[] inheritsFrom;

    private FieldDef[] fields;

    @JsonCreator
    public ComplexSchemaType(
        @JsonProperty("name") TypeName name,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("inherits") TypeName[] inheritsFrom,
        @JsonProperty("fields") FieldDef[] fields
    ) {
        super(name);
        this.isAbstract = isAbstract;
        this.inheritsFrom = inheritsFrom;
        this.fields = fields;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public TypeName[] getInheritsFrom() {
        return inheritsFrom;
    }

    public FieldDef[] getFields() {
        return fields;
    }
}
