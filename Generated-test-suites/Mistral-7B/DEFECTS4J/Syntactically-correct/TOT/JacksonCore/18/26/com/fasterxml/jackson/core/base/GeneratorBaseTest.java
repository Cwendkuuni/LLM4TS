package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeneratorBaseTest {

    private GeneratorBase jsonGenerator;

    @Before
    public void setUp() {
        JsonGenerator.Feature featureSet = JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS
                | JsonGenerator.Feature.ESCAPE_NON_ASCII
                | JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION;
        ObjectCodec objectCodec = new ObjectMapper.DefaultObjectMapper().getSerializerProvider().getObjectCodec();
        jsonGenerator = new GeneratorBase(featureSet, objectCodec);
    }

    @Test
    public void testWriteStructural() throws JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("field1");
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeFieldName("field2");
        jsonGenerator.writeValue(42);
        jsonGenerator.writeEndObject();
        JsonParser parser = jsonGenerator.getCodec().treeParser();
        JsonNode rootNode = parser.readValueAsTree(jsonGenerator.getOutputBuffer().toString());
        Assert.assertEquals(2, rootNode.size());
        Assert.assertEquals("field1", rootNode.fieldNames().next().getName());
        Assert.assertEquals("value1", rootNode.get(0).asText());
        Assert.assertEquals("field2", rootNode.fieldNames().next().getName());
        Assert.assertEquals(42, rootNode.get(1).asInt());
    }

    @Test
    public void testWriteTextual() throws JsonProcessingException {
        jsonGenerator.writeString("test string");
        JsonParser parser = jsonGenerator.getCodec().treeParser();
        JsonNode rootNode = parser.readValueAsTree(jsonGenerator.getOutputBuffer().toString());
        Assert.assertEquals("test string", rootNode.asText());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteInvalidRaw() throws JsonProcessingException {
        jsonGenerator.writeRawValue("<script>alert(\"XSS attack!\");</script>");
    }

    @Test
    public void testWritePrimitive() throws JsonProcessingException {
        jsonGenerator.writeNumber(42);
        JsonParser parser = jsonGenerator.getCodec().treeParser();
        JsonNode rootNode = parser.readValueAsTree(jsonGenerator.getOutputBuffer().toString());
        Assert.assertEquals(42, rootNode.asInt());
    }

    @Test
    public void testWriteNull() throws JsonProcessingException {
        jsonGenerator.writeNull();
        JsonParser parser = jsonGenerator.getCodec().treeParser();
        JsonNode rootNode = parser.readValueAsTree(jsonGenerator.getOutputBuffer().toString());
        Assert.assertNull(rootNode);
    }

    @Test
    public void testWriteBoolean() throws JsonProcessingException {
        jsonGenerator.writeBoolean(true);
        JsonParser parser = jsonGenerator.getCodec().treeParser();
        JsonNode rootNode = parser.readValueAsTree(jsonGenerator.getOutputBuffer().toString());
        Assert.assertTrue(rootNode.asBoolean());
    }

    @Test
    public void testWriteBigDecimal() throws JsonProcessingException {
        BigDecimal number = new BigDecimal("123456789012345678901234567890");
        jsonGenerator.writeNumber(number);
        JsonParser parser = jsonGenerator.getCodec().treeParser();
        JsonNode rootNode = parser.readValueAsTree(jsonGenerator.getOutputBuffer().toString());
        Assert.assertEquals("123456789012345678901234567890", rootNode.asText());
    }

    @Test
    public void testWriteArray() throws JsonProcessingException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeValue(42);
        jsonGenerator.writeValue("test string");
        jsonGenerator.writeEndArray();
        JsonParser parser = jsonGenerator.getCodec().treeParser();
        JsonNode rootNode = parser.readValueAsTree(jsonGenerator.getOutputBuffer().toString());
        Assert.assertEquals(2, rootNode.size());
        Assert.assertArrayEquals(new Object[]{"42", "test string"}, rootNode.elements());
    }

    @Test
    public void testVersion() throws JsonProcessingException {
        Version version = jsonGenerator.version();
        Assert.assertEquals("2.11.3", version.toString());
    }

    @Test
    public void testPrettyPrinting() throws JsonProcessingException {
        PrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        jsonGenerator = new GeneratorBase(GeneratorBase.Feature.AUTO_CLOSE_TARGET, null, prettyPrinter);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("field1");
        jsonGenerator.writeValue("value1");
        jsonGenerator.writeFieldName("field2");
        jsonGenerator.writeValue(42);
        jsonGenerator.writeEndObject();
        String json = jsonGenerator.getOutputBuffer().toString();
        Assert.assertEquals("{\n" +
                "  \"field1\": \"value1\",\n" +
                "  \"field2\": 42\n" +
                "}", json);
    }
}