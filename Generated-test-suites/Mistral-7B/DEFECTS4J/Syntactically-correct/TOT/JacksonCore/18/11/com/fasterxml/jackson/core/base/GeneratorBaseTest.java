package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled_WriteNumbersAsStrings() throws IOException {
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generator1 = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);
        GeneratorBase generator2 = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask(), codec);

        assertTrue(generator1.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator2.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testConfigure_WriteNumbersAsStrings() throws IOException {
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        generator.disable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testWriteString() throws IOException {
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        String inputString = "Test String";
        generator.writeString(inputString);
        String outputString = generator.getCurrentValueAsString();
        assertEquals(inputString, outputString);
    }

    @Test
    public void testWriteRawValue() throws IOException {
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        String inputString = "Test String";
        generator.writeRawValue(inputString);
        String outputString = generator.getCurrentValueAsString();
        assertEquals(inputString, outputString);
    }

    @Test
    public void testWriteRawValue_Null() throws IOException {
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        generator.writeRawValue(null);
        assertEquals("null", generator.getCurrentValueAsString());
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteRawValue_StringWithInvalidUTF16() throws IOException {
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        String inputString = "\uD800\uDC00"; // Invalid UTF-16 surrogate pair
        generator.writeRawValue(inputString);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteRawValue_BigDecimal() throws IOException {
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        BigDecimal bigDecimal = new BigDecimal("1234567890123456789012345678901234567890");
        generator.writeRawValue(bigDecimal);
    }

    @Test
    public void testWriteObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        GeneratorBase generator = writer.getRootValueGenerator();

        String testString = "Test String";
        generator.writeFieldName("testField");
        generator.writeObject(testString);

        JsonNode node = writer.writeValueAsTree(testString);
        assertEquals("\"testField\":\"Test String\"", node.toString());
    }

    @Test
    public void testWriteTree() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.getFactory().createGenerator(new StringWriter(new StringBuilder()));

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("testField", "Test String");

        generator.writeTree(rootNode);

        String outputString = generator.getOutputContext().getText();
        assertEquals("{\"testField\":\"Test String\"}", outputString);
    }

    @Test
    public void testWriteNumber() throws IOException {
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        generator.writeNumber(123);
        assertEquals("123", generator.getCurrentValueAsString());

        generator.writeNumber(123.45);
        assertEquals("123.45", generator.getCurrentValueAsString());

        generator.writeNumber(123L);
        assertEquals("123", generator.getCurrentValueAsString());

        generator.writeNumber(123.45D);
        assertEquals("123.45", generator.getCurrentValueAsString());

        generator.writeNumber(new BigDecimal("123.45"));
        assertEquals("123.45", generator.getCurrentValueAsString());
    }

    @Test
    public void testWriteBoolean() throws IOException {
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        generator.writeBoolean(true);
        assertEquals("true", generator.getCurrentValueAsString());

        generator.writeBoolean(false);
        assertEquals("false", generator.getCurrentValueAsString());
    }

    @Test
    public void testWriteNull() throws IOException {
        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), codec);

        generator.writeNull();
        assertEquals("null", generator.getCurrentValueAsString());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeneratorBase generator = mapper.writer().getRootValueGenerator();
        PrettyPrinter pp = generator.useDefaultPrettyPrinter();

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("testField1", "testString1");
        rootNode.put("testField2", "testString2");

        StringWriter writer = new StringWriter(new StringBuilder());
        generator.writeTree(rootNode, writer, pp);

        String outputString = writer.toString();
        String expectedOutputString = "{\"testField1\":\"testString1\",\"testField2\":\"testString2\"}";

        assertEquals(expectedOutputString, outputString);
    }
}