package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = null; // Initialize JSON generator later

        // Test root value separator
        String rootValue = "rootValue";
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeString(jsonGenerator, rootValue);

        // Test writeString method
        String value = "value";
        prettyPrinter.writeString(jsonGenerator, value);
        String expected = "rootValue " + value;
        String actual = jsonGenerator.getOutputString();
        assertEquals(expected, actual);

        // Test writeStartObject method
        prettyPrinter.writeStartObject(jsonGenerator);
        String fieldName = "fieldName";
        String fieldValue = "fieldValue";

        // Test writeObjectFieldValueSeparator method
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeString(jsonGenerator, fieldName);
        prettyPrinter.writeString(jsonGenerator, fieldValue);

        // Test writeObjectEntrySeparator method
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);

        // Test writeEndObject method
        prettyPrinter.writeEndObject(jsonGenerator, 1);

        expected = "{\n" +
                  "  \"" + fieldName + "\":\n" +
                  "  \"" + fieldValue + "\"\n" +
                  "}";
        actual = jsonGenerator.getOutputString();
        assertEquals(expected, actual);

        // Test writeStartArray method
        prettyPrinter.writeStartArray(jsonGenerator);
        String arrayElement = "arrayElement";

        // Test writeArrayValueSeparator method
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeString(jsonGenerator, arrayElement);

        // Test writeEndArray method
        prettyPrinter.writeEndArray(jsonGenerator, 1);

        expected = "[";
        actual = jsonGenerator.getOutputString();
        assertStartsWith(expected, actual);
        jsonGenerator.flush();

        // Test flushing
        String flushedOutput = jsonGenerator.getOutputString();
        assertEquals(expected + arrayElement + "\n]", flushedOutput);
    }
}