package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

/**
 * Unit test for DefaultPrettyPrinter class
 */
public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws JsonProcessingException, IOException {

        // Test default constructor
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeObjectFieldName("field1");
        prettyPrinter.writeObjectFieldValue("value1");
        prettyPrinter.writeObjectFieldName("field2");
        prettyPrinter.writeObjectFieldValue("value2");
        prettyPrinter.writeEndObject(jsonGenerator);
        jsonGenerator.close();

        String actual = stringWriter.toString();
        String expected = "{\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": \"value2\"\n" +
                "}";
        assertEquals(expected, actual);

        // Test constructor with root separator
        String rootSeparator = "----";
        prettyPrinter = new DefaultPrettyPrinter(new SerializableString(rootSeparator));
        stringWriter = new StringWriter();
        jsonGenerator = new JsonFactory().createGenerator(stringWriter);
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeObjectFieldName("field1");
        prettyPrinter.writeObjectFieldValue("value1");
        prettyPrinter.writeObjectFieldName("field2");
        prettyPrinter.writeObjectFieldValue("value2");
        prettyPrinter.writeEndObject(jsonGenerator);
        jsonGenerator.close();

        actual = stringWriter.toString();
        expected = rootSeparator.getValue() + "{\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": \"value2\"\n" +
                "}";
        assertEquals(expected, actual);

        // Test writeRootValueSeparator
        stringWriter = new StringWriter();
        JsonGenerator jsonGeneratorWithRootSeparator = new JsonFactory().createGenerator(stringWriter);
        prettyPrinter.writeRootValueSeparator(jsonGeneratorWithRootSeparator);
        String actualRootValueSeparator = stringWriter.toString();
        String expectedRootValueSeparator = " ";
        assertEquals(expectedRootValueSeparator, actualRootValueSeparator);

        // Test writeStartObject
        stringWriter = new StringWriter();
        jsonGeneratorWithRootSeparator = new JsonFactory().createGenerator(stringWriter);
        prettyPrinter.writeStartObject(jsonGeneratorWithRootSeparator);
        String actualWriteStartObject = stringWriter.toString();
        String expectedWriteStartObject = "{\n";
        assertEquals(expectedWriteStartObject, actualWriteStartObject);

        // Test beforeObjectEntries
        stringWriter = new StringWriter();
        jsonGeneratorWithRootSeparator = new JsonFactory().createGenerator(stringWriter);
        prettyPrinter.beforeObjectEntries(jsonGeneratorWithRootSeparator);
        String actualBeforeObjectEntries = stringWriter.toString();
        String expectedBeforeObjectEntries = "";
        assertEquals(expectedBeforeObjectEntries, actualBeforeObjectEntries);

        // Test writeObjectFieldValueSeparator
        stringWriter = new StringWriter();
        jsonGeneratorWithRootSeparator = new JsonFactory().createGenerator(stringWriter);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGeneratorWithRootSeparator);
        String actualWriteObjectFieldValueSeparator = stringWriter.toString();
        String expectedWriteObjectFieldValueSeparator = "  ";
        assertEquals(expectedWriteObjectFieldValueSeparator, actualWriteObjectFieldValueSeparator);

        // Test writeObjectEntrySeparator
        stringWriter = new StringWriter();
        jsonGeneratorWithRootSeparator = new JsonFactory().createGenerator(stringWriter);
        prettyPrinter.writeObjectEntrySeparator(jsonGeneratorWithRootSeparator);
        String actualWriteObjectEntrySeparator = stringWriter.toString();
        String expectedWriteObjectEntrySeparator = "\n  ";
        assertEquals(expectedWriteObjectEntrySeparator, actualWriteObjectEntrySeparator);

        // Test writeEndObject
        stringWriter = new StringWriter();
        jsonGeneratorWithRootSeparator = new JsonFactory().createGenerator(stringWriter);
        prettyPrinter.writeEndObject(jsonGeneratorWithRootSeparator, 0);
        String actualWriteEndObject = stringWriter.toString();
        String expectedWriteEndObject = "}";
        assertEquals(expectedWriteEndObject, actualWriteEndObject);

        // Test writeStartArray
        stringWriter = new StringWriter();
        jsonGeneratorWithRootSeparator = new JsonFactory().createGenerator(stringWriter);
        prettyPrinter.writeStartArray(jsonGeneratorWithRootSeparator);
        String actualWriteStartArray = stringWriter.toString();
        String expectedWriteStartArray = "[";
        assertEquals(expectedWriteStartArray, actualWriteStartArray);

        // Test beforeArrayValues
        stringWriter = new StringWriter();
        jsonGeneratorWithRootSeparator = new JsonFactory().createGenerator(stringWriter);
        prettyPrinter.beforeArrayValues(jsonGeneratorWithRootSeparator);
        String actualBeforeArrayValues = stringWriter.toString();
        String expectedBeforeArrayValues = "";
        assertEquals(expectedBeforeArrayValues, actualBeforeArrayValues);

        // Test writeArrayValueSeparator
        stringWriter = new StringWriter();
        jsonGeneratorWithRootSeparator = new JsonFactory().createGenerator(stringWriter);
        prettyPrinter.writeArrayValueSeparator(jsonGeneratorWithRootSeparator);
        String actualWriteArrayValueSeparator = stringWriter.toString();
        String expectedWriteArrayValueSeparator = "\n  ";
        assertEquals(expectedWriteArrayValueSeparator, actualWriteArrayValueSeparator);

        // Test writeEndArray
        stringWriter = new StringWriter();
        jsonGeneratorWithRootSeparator = new JsonFactory().createGenerator(stringWriter);
        prettyPrinter.writeEndArray(jsonGeneratorWithRootSeparator, 0);
        String actualWriteEndArray = stringWriter.toString();
        String expectedWriteEndArray = "]";
        assertEquals(expectedWriteEndArray, actualWriteEndArray);
    }
}