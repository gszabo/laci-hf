package com.prezi.homeassignment.schemalang;

import com.fasterxml.jackson.annotation.JsonCreator;

public class FieldName {

    private String value;

    @JsonCreator
    public FieldName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString(){
        return value;
    }
}
