package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerationSchema;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GeneratorBaseTest {

    private JsonGenerator generator;
    private ObjectMapper objectMapper;
    private ObjectCodec objectCodec;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectCodec = objectMapper.getCodec();
        generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), null);
        generator.setCodec(objectCodec);
    }

    @Test
    public void testIsEnabled() throws IOException {
        JsonGenerator gen = new GeneratorBase(JsonGenerationSchema.Feature.WRITE_BOOLEAN.getMask() | JsonGenerationSchema.Feature.WRITE_NULL.getMask(), objectCodec) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };
        assertTrue(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_BOOLEAN));
        assertTrue(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_NULL));
        assertFalse(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisableFeatures() throws IOException {
        JsonGenerator gen = new GeneratorBase(JsonGenerationSchema.Feature.ALL.getMask(), objectCodec) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };

        gen.disable(JsonGenerationSchema.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.disable(JsonGenerationSchema.Feature.WRITE_BOOLEAN);
        assertFalse(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_BOOLEAN));

        gen.disable(JsonGenerationSchema.Feature.WRITE_NULL);
        assertFalse(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_NULL));

        gen.disable(JsonGenerationSchema.Feature.WRITE_STRING);
        assertFalse(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_STRING));

        gen.disable(JsonGenerationSchema.Feature.WRITE_RAW);
        assertFalse(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_RAW));

        gen.disable(JsonGenerationSchema.Feature.WRITE_NUMBER);
        assertFalse(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_NUMBER));

        gen.disable(JsonGenerationSchema.Feature.WRITE_BINARY);
        assertFalse(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_BINARY));
    }

    @Test
    public void testSetFeatureMask() throws IOException {
        JsonGenerator gen = new GeneratorBase(JsonGenerationSchema.Feature.ALL.getMask(), objectCodec) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };

        gen.setFeatureMask(JsonGenerationSchema.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerationSchema.Feature.WRITE_BOOLEAN.getMask());
        assertTrue(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_BOOLEAN));
        assertFalse(gen.isEnabled(JsonGenerationSchema.Feature.WRITE_NULL));
    }

    @Test
    public void testWriteString() throws IOException {
        generator.writeStartObject();
        generator.writeString("test");
        generator.writeEndObject();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutput();
        assertEquals("{\"test\":}", byteArrayOutputStream.toString());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream);
        generator = new GeneratorBase(JsonGenerationSchema.Feature.ALL.getMask(), objectCodec, writer) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };
        generator.writeRawValue("test");
        generator.close();
        assertEquals("test", byteArrayOutputStream.toString());
    }

    @Test(expected = IOException.class)
    public void testWriteNull() throws IOException {
        generator.writeNull();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutput();
        byteArrayOutputStream.close();
    }

    @Test
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);
        generator.writeBoolean(false);
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutput();
        byteArrayOutputStream.close();
        String result = new String(byteArrayOutputStream.toByteArray());
        assertEquals("true,false", result);
    }

    @Test
    public void testWriteNumber() throws IOException {
        generator.writeNumber(123);
        generator.writeNumber(Long.MAX_VALUE);
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutput();
        byteArrayOutputStream.close();
        String result = new String(byteArrayOutputStream.toByteArray());
        assertEquals("123,9223372036854775807", result);
    }

    @Test
    public void testWriteBigDecimal() throws IOException {
        BigDecimal bdecimal = new BigDecimal("1234567890123456789");
        generator.writeNumber(bdecimal);
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutput();
        byteArrayOutputStream.close();
        String result = new String(byteArrayOutputStream.toByteArray());
        assertEquals("1234567890123456789", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testWriteInvalidJson() throws IOException {
        generator.writeStartObject();
        generator.writeNumber(123);
        generator.writeEndObject();
        generator.writeStartObject();
        generator.writeNumber(123);
        generator.writeString("test");
        generator.writeEndObject();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutput();
        byteArrayOutputStream.close();
    }

    @Test
    public void testWriteArray() throws IOException {
        generator.writeStartArray();
        generator.writeNumber(123);
        generator.writeNumber(456);
        generator.writeEndArray();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutput();
        byteArrayOutputStream.close();
        String result = new String(byteArrayOutputStream.toByteArray());
        assertEquals("[123,456]", result);
    }

    @Test
    public void testWriteObject() throws IOException {
        generator.writeStartObject();
        Object obj = new Object();
        generator.writeObject(obj);
        generator.writeEndObject();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutput();
        byteArrayOutputStream.close();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = new String(byteArrayOutputStream.toByteArray());
        assertEquals(objectMapper.writeValueAsString(obj), result);
    }

    @Test
    public void testWriteTree() throws IOException {
        generator.writeTree(new TreeNode(123));
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutput();
        byteArrayOutputStream.close();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = new String(byteArrayOutputStream.toByteArray());
        assertEquals(objectMapper.writeValueAsString(new TreeNode(123)), result);
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream);
        generator = new GeneratorBase(JsonGenerationSchema.Feature.ALL.getMask(), objectCodec, writer) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };
        generator.useDefaultPrettyPrinter();
        generator.writeStartObject();
        generator.writeFieldName("test");
        generator.writeNumber(123);
        generator.writeEndObject();
        generator.close();
        String result = new String(byteArrayOutputStream.toByteArray());
        assertEquals(new DefaultPrettyPrinter().printValueAsString("test"), result);
    }
}