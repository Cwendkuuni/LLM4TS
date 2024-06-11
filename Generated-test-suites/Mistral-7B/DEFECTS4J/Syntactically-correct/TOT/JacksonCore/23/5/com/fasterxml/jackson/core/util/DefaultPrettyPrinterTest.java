package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonGenerator jsonGenerator;

    @Before
    public void setup() throws IOException {
        jsonGenerator = MAPPER.getFactory().createPrettyPrinter(new ByteArrayOutputStream(1024));
    }

    /**
     * Test for default constructor.
     */
    @Test
    public void testDefaultConstructor() throws JsonProcessingException {
        JsonGenerator jsonGenerator = MAPPER.createGenerator(new ByteArrayOutputStream(1024));
        jsonGenerator.writeValueAsString("[1, 2, 3]");
        String result = jsonGenerator.getOutputContent();
        Assert.assertEquals("[ 1,\n 2,\n 3]", result);
    }

    /**
     * Test constructor with root separator.
     */
    @Test
    public void testConstructorWithRootSeparator() throws JsonProcessingException {
        JsonGenerator jsonGenerator = MAPPER.createGenerator(new ByteArrayOutputStream(1024), new DefaultPrettyPrinter(new SerializedString(":")));
        jsonGenerator.writeValueAsString("{\"key\":1}");
        String result = jsonGenerator.getOutputContent();
        Assert.assertEquals(":{key:\n :1}", result);
    }

    /**
     * Test writing root value separator.
     */
    @Test
    public void testWriteRootValueSeparator() throws JsonProcessingException {
        JsonGenerator jsonGenerator = MAPPER.createGenerator(new ByteArrayOutputStream(1024), new DefaultPrettyPrinter(new SerializedString(":")));
        jsonGenerator.writeValueAsString(new Object());
        String result = jsonGenerator.getOutputContent();
        Assert.assertEquals(":\n", result);
    }

    /**
     * Test writing start object.
     */
    @Test
    public void testWriteStartObject() throws JsonProcessingException {
        JsonGenerator jsonGenerator = MAPPER.createGenerator(new ByteArrayOutputStream(1024), new DefaultPrettyPrinter(new SerializedString(":")));
        jsonGenerator.writeStartObject();
        String result = jsonGenerator.getOutputContent();
        Assert.assertEquals("{\n", result);
    }

    /**
     * Test writing end object with no entries.
     */
    @Test
    public void testWriteEndObjectWithNoEntries() throws JsonProcessingException {
        JsonGenerator jsonGenerator = MAPPER.createGenerator(new ByteArrayOutputStream(1024), new DefaultPrettyPrinter(new SerializedString(":")));
        jsonGenerator.writeEndObject();
        String result = jsonGenerator.getOutputContent();
        Assert.assertEquals("}\n", result);
    }

    /**
     * Test writing end object with entries.
     */
    @Test
    public void testWriteEndObjectWithEntries() throws JsonProcessingException {
        JsonGenerator jsonGenerator = MAPPER.createGenerator(new ByteArrayOutputStream(1024), new DefaultPrettyPrinter(new SerializedString(":")));
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("key");
        jsonGenerator.writeValue("value");
        jsonGenerator.writeEndObject();
        String result = jsonGenerator.getOutputContent();
        Assert.assertEquals("{\n  \"key\":\"value\"\n}\n", result);
    }

    /**
     * Test writing start array.
     */
    @Test
    public void testWriteStartArray() throws JsonProcessingException {
        JsonGenerator jsonGenerator = MAPPER.createGenerator(new ByteArrayOutputStream(1024), new DefaultPrettyPrinter(new SerializedString(":")));
        jsonGenerator.writeStartArray();
        String result = jsonGenerator.getOutputContent();
        Assert.assertEquals("[", result);
    }

    /**
     * Test writing end array with no values.
     */
    @Test
    public void testWriteEndArrayWithNoValues() throws JsonProcessingException {
        JsonGenerator jsonGenerator = MAPPER.createGenerator(new ByteArrayOutputStream(1024), new DefaultPrettyPrinter(new SerializedString(":")));
        jsonGenerator.writeEndArray();
        String result = jsonGenerator.getOutputContent();
        Assert.assertEquals("]\n", result);
    }

    /**
     * Test writing end array with values.
     */
    @Test
    public void testWriteEndArrayWithValues() throws JsonProcessingException {
        JsonGenerator jsonGenerator = MAPPER.createGenerator(new ByteArrayOutputStream(1024), new DefaultPrettyPrinter(new SerializedString(":")));
        jsonGenerator.writeStartArray();
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeValue("value2");
        jsonGenerator.writeEndArray();
        String result = jsonGenerator.getOutputContent();
        Assert.assertEquals("[  \"value1\"\n  \"value2\"\n]", result);
    }
}