package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for {@link JsonGeneratorImpl}.
 */
public class JsonGeneratorImplTest {

    private ObjectMapper objectMapper;
    private ByteArrayOutputStream outputStream;
    private JsonGenerator jsonGenerator;

    @Before
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        outputStream = new ByteArrayOutputStream();
        jsonGenerator = objectMapper.getFactory()
                .createGenerator(outputStream, null);
    }

    @After
    public void tearDown() throws IOException {
        if (jsonGenerator != null) {
            jsonGenerator.close();
        }
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(jsonGenerator);
    }

    /**
     * Test {@link JsonGeneratorImpl#writeFieldName(String)} method.
     */
    @Test
    public void testWriteFieldName() throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("fieldName");
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        String json = new String(outputStream.toByteArray());
        assertEquals("{\"fieldName\":null}", json);
    }

    /**
     * Test {@link JsonGeneratorImpl#writeString(String)} method.
     */
    @Test
    public void testWriteString() throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("fieldName");
        jsonGenerator.writeString("value");
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        String json = new String(outputStream.toByteArray());
        assertEquals("{\"fieldName\":\"value\"}", json);
    }

    /**
     * Test {@link JsonGeneratorImpl#writeStringField(String, String)} method.
     */
    @Test
    public void testWriteStringField() throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("fieldName", "value");
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        String json = new String(outputStream.toByteArray());
        assertEquals("{\"fieldName\":\"value\"}", json);
    }

    /**
     * Test {@link JsonGeneratorImpl#enable(Feature)} method.
     */
    @Test
    public void testEnableFeature() throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("fieldName", "value");
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        String json = new String(outputStream.toByteArray());
        assertEquals("{{\"fieldName\":\"value\"}}", json);
    }

    /**
     * Test {@link JsonGeneratorImpl#setHighestNonEscapedChar(int)} and {@link JsonGeneratorImpl#getHighestEscapedChar()} methods.
     */
    @Test
    public void testSetHighestNonEscapedChar() throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.setHighestNonEscapedChar(128);
        jsonGenerator.writeStringField("fieldName", "value");
        jsonGenerator.setHighestNonEscapedChar(0);
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        String json = new String(outputStream.toByteArray());
        assertEquals("{\"fieldName\":\"value\"}", json);

        jsonGenerator.reset();
        jsonGenerator.setHighestNonEscapedChar(128);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("fieldName", "value");
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        String jsonWithEscapes = new String(outputStream.toByteArray());
        assertEquals("{\"fieldName\":\"value\"}", json);
        assertEquals("{\"fieldName\":\"value\"}", jsonWithEscapes.substring(0, jsonWithEscapes.length() - 1));
        assertTrue(jsonWithEscapes.charAt(jsonWithEscapes.length() - 1) == ',');
    }

    /**
     * Test {@link JsonGeneratorImpl#setCharacterEscapes(CharacterEscapes)} and {@link JsonGeneratorImpl#getCharacterEscapes()} methods.
     */
    @Test
    public void testSetCharacterEscapes() throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public String getEscapeSequence(int ch) {
                return super.getEscapeSequence('\\') + String.valueOf(ch);
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);
        jsonGenerator.writeStringField("fieldName", "value");
        jsonGenerator.setCharacterEscapes(null);
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        String json = new String(outputStream.toByteArray());
        assertEquals("{\"fieldName\":\"value\\u005Cvalue\"}", json);
    }

    /**
     * Test {@link JsonGeneratorImpl#setRootValueSeparator(SerializableString)} method.
     */
    @Test
    public void testSetRootValueSeparator() throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.setRootValueSeparator(new SerializableString("rootValueSeparator"));
        jsonGenerator.writeStringField("fieldName", "value");
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        String json = new String(outputStream.toByteArray());
        assertEquals("{\"rootValueSeparator\":{\"fieldName\":\"value\"}}", json);
    }
}