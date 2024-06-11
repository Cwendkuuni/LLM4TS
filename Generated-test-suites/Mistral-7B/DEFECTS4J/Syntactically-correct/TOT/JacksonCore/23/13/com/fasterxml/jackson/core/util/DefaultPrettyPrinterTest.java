package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Test class for DefaultPrettyPrinter.
 */
public class DefaultPrettyPrinterTest {

    /**
     * Test that the default constructor returns expected instance.
     */
    @Test
    public void testDefaultConstructor() throws JsonProcessingException {
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeRootValueSeparator(generator);
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        prettyPrinter.writeObjectEntrySeparator(generator);
        prettyPrinter.writeEndObject(generator, 0);
        String result = generator.getOutputFile();
        assertEquals("{\n}", result);
    }

    /**
     * Test that constructor with custom root separator sets it correctly.
     */
    @Test
    public void testCustomRootSeparator() throws JsonProcessingException {
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        prettyPrinter.writeRootValueSeparator(generator);
        String result = generator.getOutputFile();
        assertEquals("--", result);
    }

    /**
     * Test that withArrayIndenter method sets array indentation correctly.
     */
    @Test
    public void testArrayIndenter() throws JsonProcessingException {
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        // Default indentation
        prettyPrinter.indentArraysWith(FixedSpaceIndenter.instance);
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.writeArrayValueSeparator(generator);
        String result1 = generator.getOutputFile();
        assertEquals("[ \n", result1);

        // Custom indentation
        prettyPrinter.indentArraysWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("*");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.writeArrayValueSeparator(generator);
        String result2 = generator.getOutputFile();
        assertEquals("[*\n", result2);
    }

    /**
     * Test that withObjectIndenter method sets object indentation correctly.
     */
    @Test
    public void testObjectIndenter() throws JsonProcessingException {
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        // Default indentation
        prettyPrinter.indentObjectsWith(FixedSpaceIndenter.instance);
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        prettyPrinter.writeObjectEntrySeparator(generator);
        prettyPrinter.writeEndObject(generator, 0);
        String result1 = generator.getOutputFile();
        assertEquals("{\n}", result1);

        // Custom indentation
        prettyPrinter.indentObjectsWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("*");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        prettyPrinter.writeObjectEntrySeparator(generator);
        prettyPrinter.writeEndObject(generator, 0);
        String result2 = generator.getOutputFile();
        assertEquals("*{*\n}", result2);
    }

    /**
     * Test that withSpacesInObjectEntries method sets spaces in object entries correctly.
     */
    @Test
    public void testSpacesInObjectEntries() throws JsonProcessingException {
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(new StringWriter());
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        // Without spaces
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldName("name");
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        prettyPrinter.writeStringValue("John");
        prettyPrinter.writeObjectEntrySeparator(generator);
        prettyPrinter.writeObjectFieldName("age");
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        prettyPrinter.writeNumberValue(30);
        prettyPrinter.writeEndObject(generator, 2);
        String result1 = generator.getOutputFile();
        assertEquals("{\"name\":\"John\",\"age\":30}", result1);

        // With spaces
        prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSpacesInObjectEntries();
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldName("name");
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        prettyPrinter.writeStringValue("John");
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        prettyPrinter.writeStringValue(":");
        prettyPrinter.writeRaw(" ");
        prettyPrinter.writeStringValue("John");
        prettyPrinter.writeObjectEntrySeparator(generator);
        prettyPrinter.writeObjectFieldName("age");
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        prettyPrinter.writeNumberValue(30);
        prettyPrinter.writeObjectEntrySeparator(generator);
        prettyPrinter.writeEndObject(generator, 2);
        String result2 = generator.getOutputFile();
        assertEquals("{\n  \"name\": \"John\",\n  \"age\": 30\n}", result2);
    }
}