package com.fasterxml.jackson.core.json.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link JsonGeneratorImpl} class.
 */
public class JsonGeneratorImplTest {

    private JsonGenerator _generator;
    private IOContext _ioContext;

    @Before
    public void setUp() throws Exception {
        _ioContext = new IOContext();
        JsonFactory factory = new JsonFactory(_ioContext);
        _generator = factory.createGenerator(System.out, new DefaultPrettyPrinter());
        _generator.enable(Feature.QUOTE_FIELD_NAMES);
    }

    /**
     * Test writing simple string values using {@link JsonGenerator#writeString(String)} method.
     */
    @Test
    public void testWriteSimpleStringValues() throws JsonProcessingException {
        _generator.writeStartObject();
        _generator.writeStringField("name", "John");
        _generator.writeEndObject();
        _generator.flush();

        String json = _ioContext.getText();
        assertEquals("{\"name\":\"John\"}", json);
    }

    /**
     * Test writing null values using {@link JsonGenerator#writeNull()} method.
     */
    @Test
    public void testWriteNullValues() throws JsonProcessingException {
        _generator.writeStartObject();
        _generator.writeStringField("age", null);
        _generator.writeEndObject();
        _generator.flush();

        String json = _ioContext.getText();
        assertNull(json.trim());
    }

    /**
     * Test writing array values using {@link JsonGenerator#writeValueAsArray(Object)} method.
     */
    @Test
    public void testWriteArrayValues() throws JsonProcessingException {
        _generator.writeStartObject();
        _generator.writeFieldName("colors");
        _generator.writeValueAsArray(new String[]{"red", "green", "blue"});
        _generator.writeEndObject();
        _generator.flush();

        String json = _ioContext.getText();
        assertEquals("{\"colors\":[\"red\",\"green\",\"blue\"]}", json);
    }

    /**
     * Test custom character escapes using {@link JsonGenerator#setCharacterEscapes(CharacterEscapes)} method.
     */
    @Test
    public void testCustomCharacterEscapes() throws JsonProcessingException {
        CharacterEscapes customEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{'\\', '\"', '/'};
            }
        };

        _generator.setCharacterEscapes(customEscapes);
        _generator.writeStartObject();
        _generator.writeStringField("name", "John\\Doe");
        _generator.writeEndObject();
        _generator.flush();

        String json = _ioContext.getText();
        assertEquals("{\"name\":\"John\\\\Doe\"}", json);
    }
}