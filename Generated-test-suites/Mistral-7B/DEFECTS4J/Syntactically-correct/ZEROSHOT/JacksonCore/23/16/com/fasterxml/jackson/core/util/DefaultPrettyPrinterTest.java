package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.*;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;
    private JsonGenerator jsonGeneratorMock;

    @Before
    public void setup() {
        jsonGeneratorMock = mock(JsonGenerator.class);
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        PrettyPrinter underTest = prettyPrinter;
        JsonGenerator jsonGenerator = jsonGeneratorMock;
        String rootSeparator = "testRootSeparator";

        underTest.writeRootValueSeparator(jsonGenerator);

        verify(jsonGenerator).writeRaw(rootSeparator);
    }

    @Test
    public void testWriteStartObject() throws IOException {
        PrettyPrinter underTest = prettyPrinter;
        JsonGenerator jsonGenerator = jsonGeneratorMock;

        underTest.writeStartObject(jsonGenerator);

        verify(jsonGenerator).writeRaw("{");
    }

    @Test
    public void testWriteEndObject() throws IOException {
        PrettyPrinter underTest = prettyPrinter;
        JsonGenerator jsonGenerator = jsonGeneratorMock;

        underTest.writeEndObject(jsonGenerator, 1);

        verify(jsonGenerator).writeRaw("}");
    }

    @Test
    public void testWriteStartArray() throws IOException {
        PrettyPrinter underTest = prettyPrinter;
        JsonGenerator jsonGenerator = jsonGeneratorMock;

        underTest.writeStartArray(jsonGenerator);

        verify(jsonGenerator).writeRaw("[");
    }

    @Test
    public void testWriteEndArray() throws IOException {
        PrettyPrinter underTest = prettyPrinter;
        JsonGenerator jsonGenerator = jsonGeneratorMock;

        underTest.writeEndArray(jsonGenerator, 1);

        verify(jsonGenerator).writeRaw("]");
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        PrettyPrinter underTest = prettyPrinter;
        JsonGenerator jsonGenerator = jsonGeneratorMock;

        underTest.writeObjectFieldValueSeparator(jsonGenerator);

        verify(jsonGenerator).writeRaw(": ");
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        PrettyPrinter underTest = prettyPrinter;
        JsonGenerator jsonGenerator = jsonGeneratorMock;

        underTest.writeArrayValueSeparator(jsonGenerator);

        verify(jsonGenerator).writeRaw(", ");
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        PrettyPrinter underTest = prettyPrinter;
        JsonGenerator jsonGenerator = jsonGeneratorMock;

        underTest.writeObjectEntrySeparator(jsonGenerator);

        verify(jsonGenerator).writeRaw(",\n");
    }

    @Test
    public void testWriteIndentationObjectsWith() throws IOException {
        PrettyPrinter underTest = prettyPrinter.withObjectIndenter(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("TestIndent");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });

        underTest.writeStartObject(jsonGeneratorMock);
        underTest.writeObjectFieldValueSeparator(jsonGeneratorMock);
        underTest.writeObjectEntrySeparator(jsonGeneratorMock);
        underTest.writeString("testField", jsonGeneratorMock);
        underTest.writeObjectFieldValueSeparator(jsonGeneratorMock);
        underTest.writeString("testValue", jsonGeneratorMock);
        underTest.writeObjectEntrySeparator(jsonGeneratorMock);
        underTest.writeEndObject(jsonGeneratorMock, 1);

        String expectedOutput = "{\n TestIndent\n : testField\n TestIndent\n : testValue\n}";

        String actualOutput = getJsonStringFromJsonGenerator(jsonGeneratorMock);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteIndentationArraysWith() throws IOException {
        PrettyPrinter underTest = prettyPrinter.withArrayIndenter(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("TestIndent");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });

        underTest.writeStartArray(jsonGeneratorMock);
        underTest.writeArrayValueSeparator(jsonGeneratorMock);
        underTest.writeNumber(jsonGeneratorMock, 1);
        underTest.writeArrayValueSeparator(jsonGeneratorMock);
        underTest.writeNumber(jsonGeneratorMock, 2);
        underTest.writeArrayValueSeparator(jsonGeneratorMock);
        underTest.writeEndArray(jsonGeneratorMock, 2);

        String expectedOutput = "[ TestIndent\n  1\n TestIndent\n  2\n]";

        String actualOutput = getJsonStringFromJsonGenerator(jsonGeneratorMock);

        assertEquals(expectedOutput, actualOutput);
    }

    private String getJsonStringFromJsonGenerator(JsonGenerator jsonGenerator) throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        jsonGenerator = jsonFactory.createGenerator(stringWriter);

        underTest.writeStartObject(jsonGenerator);
        underTest.writeObjectFieldValueSeparator(jsonGenerator);
        underTest.writeObjectEntrySeparator(jsonGenerator);
        underTest.writeString("testField", jsonGenerator);
        underTest.writeObjectFieldValueSeparator(jsonGenerator);
        underTest.writeString("testValue", jsonGenerator);
        underTest.writeObjectEntrySeparator(jsonGenerator);
        underTest.writeEndObject(jsonGenerator, 1);

        jsonGenerator.close();

        return stringWriter.toString();
    }
}

interface Indenter {
    void writeIndentation(JsonGenerator g, int level) throws IOException;

    boolean isInline();
}