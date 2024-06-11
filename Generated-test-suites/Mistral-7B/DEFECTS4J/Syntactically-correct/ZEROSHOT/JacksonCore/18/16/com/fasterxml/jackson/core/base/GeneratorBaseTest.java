package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {

    private GeneratorBase generator;
    private ByteArrayOutputStream outputStream;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        ObjectCodec objectCodec = new ObjectMapper().getFactory().getCodec();
        generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, objectCodec);
        outputStream = new ByteArrayOutputStream();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testIsEnabled() throws IOException {
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(GeneratorBase.Feature.WRITE_BOOLEAN_VALUE_AS_TRUE_FALSE));

        generator.disable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testWriteNull() throws IOException {
        generator.writeNull();
        assertEquals("null", outputStream.toString().trim());
    }

    @Test
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);
        assertEquals("true", outputStream.toString().trim());

        generator.writeBoolean(false);
        assertEquals("false", outputStream.toString().trim());
    }

    @Test
    public void testWriteNumber() throws IOException {
        generator.writeNumber(123);
        assertEquals("123", outputStream.toString().trim());

        generator.writeNumber(123.45);
        assertEquals("123.45", outputStream.toString().trim());

        generator.writeNumber(123L);
        assertEquals("123", outputStream.toString().trim());

        generator.writeNumber(123.45d);
        assertEquals("123.45", outputStream.toString().trim());

        generator.disable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        generator.writeNumber(123);
        assertEquals("123", outputStream.toString().trim());
    }

    @Test
    public void testWriteString() throws IOException {
        generator.writeString("test");
        assertEquals("test", outputStream.toString().trim());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        generator.writeRawValue("test");
        assertEquals("test", outputStream.toString().trim());
    }

    @Test
    public void testWriteRawValueWithOffset() throws IOException {
        String test = "test";
        generator.writeRawValue(test.substring(1), 0, test.length() - 1);
        assertEquals("est", outputStream.toString().trim());
    }

    @Test
    public void testWriteRawValueWithLength() throws IOException {
        String test = "test";
        generator.writeRawValue(test.substring(1), 1, test.length() - 2);
        assertEquals("est", outputStream.toString().trim());
    }

    @Test
    public void testWriteRawValueWithSerializableString() throws IOException {
        generator.writeRawValue(new com.fasterxml.jackson.core.base.SerializableString("test"));
        assertEquals("test", outputStream.toString().trim());
    }

    @Test
    public void testWriteRawValueWithByteArray() throws IOException {
        byte[] test = new byte[] { 't', 'e', 's', 't' };
        generator.writeRawValue(test);
        assertEquals(new String(test), outputStream.toString().trim());
    }

    @Test
    public void testWriteRawValueWithByteArrayAndOffset() throws IOException {
        byte[] test = new byte[] { 't', 'e', 's', 't' };
        generator.writeRawValue(test, 1, test.length - 2);
        assertEquals(new String(test, 1, test.length - 2), outputStream.toString().trim());
    }

    @Test
    public void testWriteRawValueWithByteArrayAndLength() throws IOException {
        byte[] test = new byte[] { 't', 'e', 's', 't' };
        generator.writeRawValue(test, 1, 2);
        assertEquals(new String(test, 1, 2), outputStream.toString().trim());
    }

    @Test
    public void testWriteBinary() throws IOException {
        byte[] test = new byte[] { 0x01, 0x02, 0x03 };
        generator.writeBinary(GeneratorBase.Base64Variant.STANDARD, new ByteArrayInputStream(test), test.length);
        assertEquals("AQIDBA==", outputStream.toString().trim());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteBinaryUnsupported() throws IOException {
        generator.writeBinary(GeneratorBase.Base64Variant.STANDARD, new ByteArrayInputStream("test"), 5);
    }

    @Test
    public void testWriteObject() throws IOException {
        String json = "{\"name\":\"test\",\"age\":30}";
        generator.writeObject(new ObjectMapper().readTree(json));
        assertEquals(json, outputStream.toString().trim());
    }

    @Test
    public void testWriteTree() throws IOException {
        String json = "{\"name\":\"test\",\"age\":30}";
        generator.writeTree(new ObjectMapper().readTree(json));
        assertEquals(json, outputStream.toString().trim());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        generator.useDefaultPrettyPrinter();
        generator.writeValue("test");
        assertEquals(getDefaultPrettyPrint().writeValueAsString("test"), outputStream.toString().trim());
    }

    @Test
    public void testSetCodec() throws IOException {
        ObjectCodec newCodec = new ObjectMapper().getFactory().getCodec();
        generator.setCodec(newCodec);
        PrettyPrinter newPrinter = new DefaultPrettyPrinter();
        generator.useDefaultPrettyPrinter();
        generator.writeValue("test");
        assertEquals(newPrinter.writeValueAsString("test"), outputStream.toString().trim());
    }

    @Test
    public void testSetPrettyPrinter() throws IOException {
        PrettyPrinter newPrinter = new DefaultPrettyPrinter();
        generator.setPrettyPrinter(newPrinter);
        generator.writeValue("test");
        assertEquals(newPrinter.writeValueAsString("test"), outputStream.toString().trim());
    }

    @Test
    public void testGetOutputContext() {
        JsonStreamContext context = generator.getOutputContext();
        assertNotNull(context.getCurrentValue());
    }

    @Test
    public void testGetCodec() {
        ObjectCodec codec = generator.getCodec();
        assertNotNull(codec);
    }

    @Test
    public void testGetPrettyPrinter() {
        PrettyPrinter prettyPrinter = generator.getPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testFlush() throws IOException {
        generator.writeString("test");
        generator.flush();
        assertEquals("test", outputStream.toString().trim());
    }

    @Test
    public void testClose() throws IOException {
        generator.close();
        generator.writeString("test");
        assertFalse(generator.isOpen());
    }

    @Test
    public void testIsClosed() throws IOException {
        generator.close();
        assertTrue(generator.isClosed());
    }

    private PrettyPrinter getDefaultPrettyPrinter() {
        return new DefaultPrettyPrinter();
    }

    @After
    public void tearDown() throws Exception {
        generator.close();
        outputStream.reset();
    }
}