package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GeneratorBaseTest {

    private JsonFactory jsonFactory;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        jsonFactory = new JsonFactory();
        mapper = new ObjectMapper(jsonFactory);
    }

    @After
    public void tearDown() throws Exception {
        if (jsonFactory != null) {
            jsonFactory.close();
        }
    }

    @Test
    public void testDefaultConfiguration() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        assertNotNull(generator);
        assertFalse(generator.isClosed());

        generator.writeNumber(123);
        generator.writeString("abc");
        generator.writeObject(new Object());
        generator.close();
        assertTrue(generator.isClosed());
    }

    @Test
    public void testWriteNull() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.writeNull();
        String result = generator.getOutputBuffer().toString();
        assertEquals("null", result);
        generator.close();
    }

    @Test
    public void testWriteBoolean() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.writeBoolean(true);
        String result = generator.getOutputBuffer().toString();
        assertEquals("true", result);
        generator.close();
    }

    @Test
    public void testWriteNumber() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.writeNumber(123);
        String result = generator.getOutputBuffer().toString();
        assertEquals("123", result);
        generator.close();

        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.writeNumber(123.45);
        result = generator.getOutputBuffer().toString();
        assertEquals("123.45", result);
        generator.close();

        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        generator.writeNumber(123);
        result = generator.getOutputBuffer().toString();
        assertEquals("123", result);
        generator.close();
    }

    @Test
    public void testWriteString() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.writeString("abc");
        String result = generator.getOutputBuffer().toString();
        assertEquals("\"abc\"", result);
        generator.close();

        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.disable(Feature.ESCAPE_NON_ASCII);
        generator.writeString("abc\u00E9de");
        result = generator.getOutputBuffer().toString();
        assertEquals("\"abc\u00E9de\"", result);
        generator.close();

        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.enable(Feature.ESCAPE_NON_ASCII);
        generator.writeString("abc\u00E9de");
        result = generator.getOutputBuffer().toString();
        assertEquals("\"abc\\u00E9de\"", result);
        generator.close();
    }

    @Test
    public void testWriteObject() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.writeObject(new Object());
        String result = generator.getOutputBuffer().toString();
        assertTrue(result.startsWith("{"));
        generator.close();

        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.disable(Feature.WRITE_NULL_KEYS);
        generator.writeObject(new Object());
        result = generator.getOutputBuffer().toString();
        assertFalse(result.startsWith("{"));
        generator.close();

        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.setCodec(null);
        generator.writeObject(new Object());
        assertThrows(IllegalArgumentException.class, () -> generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test
    public void testWriteRawValue() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.writeRawValue("abc");
        String result = generator.getOutputBuffer().toString();
        assertEquals("\"abc\"", result);
        generator.close();

        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.writeRawValue("abc\u00E9de");
        result = generator.getOutputBuffer().toString();
        assertEquals("\"abc\\u00E9de\"", result);
        generator.close();

        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.writeRawValue(new byte[]{(byte) 0xED, (byte) 0xFA, (byte) 0xAB});
        result = generator.getOutputBuffer().toString();
        assertEquals("\\u00ED\\u00FA\\u00AB", result);
        generator.close();
    }

    @Test
    public void testWriteBinary() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        // unsupported, as per documentation
        assertThrows(UnsupportedOperationException.class, () -> generator.writeBinary(null, null, 0));
        generator.close();
    }

    @Test
    public void testSetFeatureMask() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask());
        generator.writeNumber(123);
        String result = generator.getOutputBuffer().toString();
        assertEquals("123", result);
        generator.close();

        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.setFeatureMask(Feature.ESCAPE_NON_ASCII.getMask());
        generator.writeString("abc\u00E9de");
        result = generator.getOutputBuffer().toString();
        assertEquals("\"abc\\u00E9de\"", result);
        generator.close();

        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        generator.setFeatureMask(0);
        generator.writeString("abc\u00E9de");
        result = generator.getOutputBuffer().toString();
        assertEquals("\"abc\u00E9de\"", result);
        generator.close();
    }
}