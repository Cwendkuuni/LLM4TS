package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link JsonGeneratorImpl} class.
 */
public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() {
        IOContext ioContext = new IOContext();
        int features = 0; // default features
        ObjectCodec codec = null; // not used in this test
        jsonGenerator = new JsonGeneratorImpl(ioContext, features, codec);
    }

    @Test
    public void testDefaultConstruction() {
        assertNotNull(jsonGenerator);
        assertEquals(JsonGenerator.Feature.QUOTE_FIELD_NAMES.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        assertEquals(JsonGenerator.Feature.ESCAPE_NON_ASCII.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        assertEquals(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT));
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
        assertEquals(null, jsonGenerator.getCharacterEscapes());
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, jsonGenerator.getRootValueSeparator());
    }

    @Test
    public void testSetHighestNonEscapedChar() throws IOException {
        jsonGenerator.setHighestNonEscapedChar(123);
        assertEquals(123, jsonGenerator.getHighestEscapedChar());
        jsonGenerator.setHighestNonEscapedChar(0);
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() throws IOException {
        CharacterEscapes characterEscapes = new CharacterEscapes() {};
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes.getEscapeCodesForAscii(), jsonGenerator._outputEscapes);
        jsonGenerator.setCharacterEscapes(null);
        assertEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() throws IOException {
        SerializableString rootValueSeparator = new SerializableString("_");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator.getRootValueSeparator());
    }

    @Test
    public void testQuoteFieldNames() throws IOException {
        jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        String fieldName = "field_name";
        jsonGenerator.writeFieldName(fieldName);
        String actual = jsonGenerator.toString();
        String expected = String.format("\"%s\":", fieldName);
        assertEquals(expected, actual);

        jsonGenerator.disable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.writeFieldName(fieldName);
        actual = jsonGenerator.toString();
        assertEquals(fieldName, actual);
    }
}