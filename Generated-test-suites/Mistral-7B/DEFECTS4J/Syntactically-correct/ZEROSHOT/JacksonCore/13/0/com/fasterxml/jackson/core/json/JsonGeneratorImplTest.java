package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;
    private IOContext ioContext;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        ioContext = new IOContext();
        JsonFactory jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        jsonGenerator = jsonFactory.createGenerator(System.out, ioContext, null);
    }

    @After
    public void tearDown() throws Exception {
        jsonGenerator.close();
    }

    @Test
    public void testEmptyConstruction() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        assertNull(jsonGenerator.getCharacterEscapes());
        assertEquals(JsonGenerator.Feature.QUOTE_FIELD_NAMES, jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        assertEquals(JsonGenerator.Feature.ESCAPE_NON_ASCII, jsonGenerator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testFeatureEnabledConstruction() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0 | JsonGenerator.Feature.QUOTE_FIELD_NAMES.mask(), null);
        assertFalse(jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void testSettingCharacterEscapes() throws JsonProcessingException {
        CharacterEscapes characterEscapes = new CharacterEscapes();
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void testQuoteFieldNames() throws IOException {
        jsonGenerator.setEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("name", 1);
        jsonGenerator.writeEndObject();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonGenerator.getOutputStream());
        assertEquals(null, jsonParser.readValueAs(String.class));
        assertTrue(jsonParser.nextToken() == JsonToken.START_OBJECT);
        assertEquals("name", jsonParser.nextFieldName());
        assertEquals(1, jsonParser.nextIntValue());
        assertEquals(JsonToken.END_OBJECT, jsonParser.nextToken());

        jsonGenerator.reset();
        jsonGenerator.setEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("name", 1);
        jsonGenerator.writeEndObject();
        jsonParser = objectMapper.getFactory().createParser(jsonGenerator.getOutputStream());
        assertEquals("\"name\"", jsonParser.getValueAsString());
        assertTrue(jsonParser.nextToken() == JsonToken.START_OBJECT);
        assertEquals("name", jsonParser.nextFieldName());
        assertEquals(1, jsonParser.nextIntValue());
        assertEquals(JsonToken.END_OBJECT, jsonParser.nextToken());
    }

    @Test
    public void testHighestNonEscapedChar() throws IOException {
        jsonGenerator.setHighestNonEscapedChar(127);
        jsonGenerator.writeString("test");
        String output = jsonGenerator.getOutputBuffer().toString();
        assertEquals("test", output.substring(0, output.length() - 1));
        assertEquals("\\u007f", output.charAt(output.length() - 1) == '\u007f' ? "" : "wrong character");

        jsonGenerator.reset();
        jsonGenerator.setHighestNonEscapedChar(0);
        jsonGenerator.writeString("test");
        output = jsonGenerator.getOutputBuffer().toString();
        assertEquals("test", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeHighestNonEscapedChar() throws IOException {
        jsonGenerator.setHighestNonEscapedChar(-1);
    }

    @Test
    public void testRootValueSeparator() throws IOException {
        jsonGenerator.setRootValueSeparator(new SerializableString(","));
        jsonGenerator.writeValue("test");
        String output = jsonGenerator.getOutputBuffer().toString();
        assertEquals(",test", output);
    }

    @Test
    public void testVersion() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        assertEquals(VersionUtil.versionFor(JsonGeneratorImpl.class), jsonGenerator.version());
    }
}