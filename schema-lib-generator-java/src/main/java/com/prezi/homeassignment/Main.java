package com.prezi.homeassignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.prezi.homeassignment.schemalang.Schema;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        File schemaFile = new File(args[0]);
        File outputDir = new File(args[1]);

        String content = Files.toString(schemaFile, Charsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();
        Schema schema = objectMapper.readValue(content, Schema.class);

        new LibraryGenerator().generate(schema, outputDir);
    }

}






