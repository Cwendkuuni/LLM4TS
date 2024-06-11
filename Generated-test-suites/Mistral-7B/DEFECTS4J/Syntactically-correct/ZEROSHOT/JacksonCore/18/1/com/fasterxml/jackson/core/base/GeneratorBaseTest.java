package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.VERSION;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {

    @Test
    public void testGeneratorBaseFeatures() throws IOException {
        ObjectCodec objectCodec = new ObjectMapper().getFactory().getCodec();
        GeneratorBase gb = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), objectCodec);

        // Test features
        assertTrue(gb.isEnabled(Feature.AUTO_CLOSE_SOURCE));
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test setFeature
        gb.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        gb.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        gb.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.overrideStdFeatures(Feature.WRITE_NULL_AS_NULL.getMask(), Feature.WRITE_NULL_AS_NULL.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NULL_AS_NULL));

        // Test isEnabled with deprecated setFeatureMask
        gb.setFeatureMask(Feature.WRITE_NULL_AS_NULL.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NULL_AS_NULL));
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.setFeatureMask(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gb.isEnabled(Feature.AUTO_CLOSE_SOURCE));
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test getFeatureMask
        assertEquals(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), gb.getFeatureMask());

        // Test writeValue
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        gb.writeRawValue("test");
        assertEquals("test", gb.getCurrentValue());
        gb.flush();
        assertEquals("test", baos.toString());

        // Test writeObject
        String json = "{\"name\":\"John\",\"age\":30,\"isStudent\":false,\"address\":{\"street\":\"1 Main St\",\"city\":\"Anytown\",\"zip\":\"12345\"}}";
        TreeNode rootNode = new ObjectMapper().readTree(json);
        gb.writeObject(rootNode);
        assertEquals(json, gb.getCurrentValue().asText());
        gb.flush();

        // Test writeNumber
        gb.writeNumber(123);
        assertEquals(123, gb.getCurrentValue().asInt());
        gb.writeNumber(123L);
        assertEquals(123L, gb.getCurrentValue().asLong());
        gb.writeNumber(123.0);
        assertEquals(123.0, gb.getCurrentValue().asDouble());

        // Test writeBoolean
        gb.writeBoolean(true);
        assertEquals(true, gb.getCurrentValue().asBoolean());
        gb.writeBoolean(false);
        assertEquals(false, gb.getCurrentValue().asBoolean());

        // Test writeNull
        gb.writeNull();
        assertEquals(null, gb.getCurrentValue());

        // Test writeFieldName
        gb.writeStartObject();
        gb.writeFieldName("name");
        gb.writeString("John");
        gb.writeFieldName("age");
        gb.writeNumber(30);
        gb.writeFieldName("isStudent");
        gb.writeBoolean(false);
        gb.writeFieldName("address");
        gb.writeStartObject();
        gb.writeFieldName("street");
        gb.writeString("1 Main St");
        gb.writeFieldName("city");
        gb.writeString("Anytown");
        gb.writeFieldName("zip");
        gb.writeNumber(12345);
        gb.writeEndObject();
        gb.writeEndObject();
        assertEquals(json, gb.getCurrentValue().asText());
        gb.flush();

        // Test writeRaw
        gb.writeRawValue("test raw value");
        assertEquals("test raw value", gb.getCurrentValue().asText());
        gb.writeRawValue("test raw value".getBytes());
        assertEquals("test raw value", gb.getCurrentValue().asText());

        // Test writeBinary
        gb.writeBinary(null, null, 0);
        _reportError("writeBinary should not be called");

        // Test writeStartArray and writeEndArray
        gb.writeStartArray();
        gb.writeNumber(1);
        gb.writeNumber(2);
        gb.writeNumber(3);
        gb.writeEndArray();
        assertEquals("[1,2,3]", gb.getCurrentValue().asText());
        gb.flush();

        // Test writeStartObject and writeEndObject
        gb.writeStartObject();
        gb.writeFieldName("name");
        gb.writeString("John");
        gb.writeFieldName("age");
        gb.writeNumber(30);
        gb.writeFieldName("isStudent");
        gb.writeBoolean(false);
        gb.writeFieldName("address");
        gb.writeStartObject();
        gb.writeFieldName("street");
        gb.writeString("1 Main St");
        gb.writeFieldName("city");
        gb.writeString("Anytown");
        gb.writeFieldName("zip");
        gb.writeNumber(12345);
        gb.writeEndObject();
        gb.writeEndObject();
        assertEquals(json, gb.getCurrentValue().asText());
        gb.flush();

        // Test useDefaultPrettyPrinter
        GeneratorBase gb2 = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), objectCodec);
        gb2.useDefaultPrettyPrinter();
        assertEquals(new DefaultPrettyPrinter().getDefaultPrettyPrinter().getClass(), gb2.getPrettyPrinter().getClass());
        gb2.flush();

        // Test setCodec
        ObjectCodec objectCodec2 = new ObjectMapper().getFactory().getCodec();
        gb.setCodec(objectCodec2);
        assertEquals(objectCodec2, gb.getCodec());

        // Test overrideStdFeatures
        gb.overrideStdFeatures(Feature.WRITE_NULL_AS_NULL.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NULL_AS_NULL.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NULL_AS_NULL));
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        gb.overrideStdFeatures(Feature.AUTO_CLOSE_SOURCE.getMask(), Feature.AUTO_CLOSE_SOURCE.getMask());
        assertTrue(gb.isEnabled(Feature.AUTO_CLOSE_SOURCE));

        // Test writeNumber with BigDecimal
        gb.writeNumber(new BigDecimal("123.456789"));
        assertEquals("123.456789", gb.getCurrentValue().asText());
        gb.flush();

        // Test _verifyValueWrite
        try {
            gb.writeRawValue("test");
            gb.writeNumber(123);
            gb._verifyValueWrite("write value");
            gb.writeRawValue("test2");
        } catch (JsonGenerationException e) {
            assertEquals("write value: not in state for writing raw value", e.getMessage());
        }

        // Test _releaseBuffers
        String json2 = "{\"name\":\"John\",\"age\":30,\"isStudent\":false,\"address\":{\"street\":\"1 Main St\",\"city\":\"Anytown\",\"zip\":\"12345\"}}";
        TreeNode rootNode2 = new ObjectMapper().readTree(json2);
        gb.writeObject(rootNode2);
        gb._releaseBuffers();
        assertEquals(null, gb.getCurrentValue());

        // Test _verifyValueWrite with null value
        gb = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), objectCodec);
        gb.writeNull();
        gb.flush();
        gb._releaseBuffers();
        assertEquals(null, gb.getCurrentValue());

        // Test _asString with BigDecimal
        assertEquals("123.456789", gb._asString(new BigDecimal("123.456789")));

        // Test _decodeSurrogate
        int surr1 = 0xDB40;
        int surr2 = 0xDC07;
        assertEquals('\uD835\uDC07', gb._decodeSurrogate(surr1, surr2));

        // Test version
        assertEquals(VERSION.VERSION_2_12, gb.version().version());

        // Test close
        gb.close();
        assertFalse(gb.isClosed());
        gb = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), objectCodec);
        gb.writeRawValue("test");
        gb.close();
        assertTrue(gb.isClosed());

    }

    private void _reportError(String message) throws IOException {
        throw new IOException(message);
    }

}