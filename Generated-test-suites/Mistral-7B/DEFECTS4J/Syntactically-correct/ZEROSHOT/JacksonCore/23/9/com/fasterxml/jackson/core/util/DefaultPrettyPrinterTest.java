package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.core.json.JsonFactory;
import com.fasterxml.jackson.core.json.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(sw);
        prettyPrinter.writeStartObject(jsonGenerator);

        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);

        jsonGenerator.writeFieldName("age");
        jsonGenerator.writeValue(30);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);

        jsonGenerator.writeFieldName("city");
        jsonGenerator.writeValue("New York");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);

        jsonGenerator.writeEndObject();

        String result = sw.toString();
        String expected = "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30,\n" +
                "  \"city\": \"New York\"\n" +
                "}";

        assertEquals(expected, result);
    }

    @Test
    public void testRootSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(": ");

        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(sw);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);

        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);

        jsonGenerator.writeFieldName("age");
        jsonGenerator.writeValue(30);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);

        jsonGenerator.writeFieldName("city");
        jsonGenerator.writeValue("New York");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);

        jsonGenerator.writeEndObject();

        String result = sw.toString();
        String expected = ": {\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30,\n" +
                "  \"city\": \"New York\"\n" +
                "}";

        assertEquals(expected, result);
    }

    @Test
    public void testIndentObjectsWith() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });

        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(sw);
        prettyPrinter.writeStartObject(jsonGenerator);

        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeValue("John Doe");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);

        jsonGenerator.writeFieldName("age");
        jsonGenerator.writeValue(30);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);

        jsonGenerator.writeFieldName("city");
        jsonGenerator.writeValue("New York");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);

        jsonGenerator.writeEndObject();

        String result = sw.toString();
        String expected = "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30,\n" +
                "  \"city\": \"New York\"\n" +
                "}";

        assertEquals(expected, result);
    }

    @Test
    public void testIndentArraysWith() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });

        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(sw);
        prettyPrinter.writeStartArray(jsonGenerator);

        jsonGenerator.writeValue("Apples");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);

        jsonGenerator.writeValue("Oranges");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);

        jsonGenerator.writeValue("Bananas");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);

        jsonGenerator.writeEndArray();

        String result = sw.toString();
        String expected = "[\"Apples\", \"Oranges\", \"Bananas\"]";

        assertEquals(expected, result);
    }
}

interface Indenter {
    void writeIndentation(JsonGenerator g, int level) throws IOException;

    boolean isInline();
}

class DefaultIndenter implements Indenter {
    public static final DefaultIndenter SYSTEM_LINEFEED_INSTANCE = new DefaultIndenter();

    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException {
        g.writeRaw(System.lineSeparator());
    }

    @Override
    public boolean isInline() {
        return true;
    }
}

class FixedSpaceIndenter implements Indenter {
    public static final FixedSpaceIndenter instance = new FixedSpaceIndenter();

    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException {
        g.writeRaw(" ");
    }

    @Override
    public boolean isInline() {
        return true;
    }
}

class NopIndenter implements Indenter, java.io.Serializable {
    public static final NopIndenter instance = new NopIndenter();

    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException { }

    @Override
    public boolean isInline() { return true; }
}