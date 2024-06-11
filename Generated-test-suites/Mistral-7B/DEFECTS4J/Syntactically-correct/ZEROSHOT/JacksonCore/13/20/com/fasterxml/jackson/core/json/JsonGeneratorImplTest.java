package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonGeneratorImplTest {

    private JsonGenerator jsonGenerator;
    private IOContext ioContext;
    private CharacterEscapes characterEscapes;

    @Before
    public void setup() throws Exception {
        ioContext = Mockito.mock(IOContext.class);
        characterEscapes = Mockito.mock(CharacterEscapes.class);
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null) {
            @Override
            protected CharacterEscapes _characterEscapes() {
                return characterEscapes;
            }
        };
    }

    @Test
    public void testDefaultConstruction() throws IOException, JsonProcessingException {
        JsonGenerator jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        assertEquals(JsonGenerator.Feature.UNQUOTED_FIELD_NAMES.enabledIn(jsonGenerator.getFeatures()), false);
        assertEquals(JsonGenerator.Feature.QUOTE_FIELD_NAMES.enabledIn(jsonGenerator.getFeatures()), true);
        assertEquals(JsonGenerator.Feature.ESCAPE_NON_ASCII.enabledIn(jsonGenerator.getFeatures()), false);
        assertEquals(Arrays.toString(JsonGeneratorImpl.sOutputEscapes), Arrays.toString(jsonGenerator._outputEscapes));
    }

    @Test
    public void testFeatureQuoteFieldNames() throws IOException, JsonProcessingException {
        JsonGenerator jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.QUOTE_FIELD_NAMES.asInt(), null);
        assertEquals(JsonGenerator.Feature.UNQUOTED_FIELD_NAMES.enabledIn(jsonGenerator.getFeatures()), false);
        assertEquals(JsonGenerator.Feature.QUOTE_FIELD_NAMES.enabledIn(jsonGenerator.getFeatures()), true);
        jsonGenerator.enable(Feature.UNQUOTED_FIELD_NAMES);
        assertEquals(JsonGenerator.Feature.UNQUOTED_FIELD_NAMES.enabledIn(jsonGenerator.getFeatures()), true);
        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        assertEquals(JsonGenerator.Feature.QUOTE_FIELD_NAMES.enabledIn(jsonGenerator.getFeatures()), false);
    }

    @Test
    public void testFeatureEscapeNonAscii() throws IOException, JsonProcessingException {
        JsonGenerator jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.ESCAPE_NON_ASCII.asInt(), null);
        assertEquals(JsonGenerator.Feature.ESCAPE_NON_ASCII.enabledIn(jsonGenerator.getFeatures()), true);
        jsonGenerator.setHighestNonEscapedChar(123);
        assertEquals(JsonGenerator.Feature.ESCAPE_NON_ASCII.enabledIn(jsonGenerator.getFeatures()), true);
        assertEquals(123, jsonGenerator.getHighestEscapedChar());

        jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.ESCAPE_NON_ASCII.asInt() | Feature.UNQUOTED_FIELD_NAMES.asInt(), null);
        assertEquals(JsonGenerator.Feature.UNQUOTED_FIELD_NAMES.enabledIn(jsonGenerator.getFeatures()), true);
        assertEquals(JsonGenerator.Feature.ESCAPE_NON_ASCII.enabledIn(jsonGenerator.getFeatures()), true);
        jsonGenerator.setHighestNonEscapedChar(123);
        assertEquals(123, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testCharacterEscapes() throws IOException, JsonProcessingException {
        Mockito.when(characterEscapes.getEscapeCodeFor(CharTypes.CONTROL_CHAR_EOL)).thenReturn(255);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        jsonGenerator.writeRawByte(13);
        jsonGenerator.close();
        Mockito.verify(characterEscapes).writeEscapedAscii(jsonGenerator._out, 255);
    }

    @Test
    public void testRootValueSeparator() throws IOException, JsonProcessingException {
        CharSequence rootValueSeparator = "TEST";
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeValue("testValue");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("{\"TESTtest\":{\"test\":\"testValue\"}}", result);
    }

    @Test
    public void testUnquotedFieldNames() throws IOException, JsonProcessingException {
        JsonGenerator jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.UNQUOTED_FIELD_NAMES.asInt(), null);
        jsonGenerator.enable(Feature.UNQUOTED_FIELD_NAMES);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeValue("testValue");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("{\"test\":\"testValue\"}", result);
    }

    @Test
    public void testQuotedFieldNames() throws IOException, JsonProcessingException {
        JsonGenerator jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeValue("testValue");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("{\"test\":\"testValue\"}", result);

        jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.QUOTE_FIELD_NAMES.asInt(), null);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeValue("testValue");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("{\"test\":\"testValue\"}", result);

        jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.QUOTE_FIELD_NAMES.asInt() | Feature.UNQUOTED_FIELD_NAMES.asInt(), null);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeValue("testValue");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("{\"test\":\"testValue\"}", result);

        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeValue("testValue");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("{\"test\":testValue}", result);
    }

    @Test
    public void testDisabledQuotedFieldNames() throws IOException, JsonProcessingException {
        JsonGenerator jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.UNQUOTED_FIELD_NAMES.asInt(), null);
        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeValue("testValue");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("{\"test\":testValue}", result);
    }

    @Test
    public void testQuoteFieldNamesFlag() throws IOException, JsonProcessingException {
        JsonGenerator jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        assertTrue(jsonGenerator._cfgUnqNames);
        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);

        jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.QUOTE_FIELD_NAMES.asInt(), null);
        assertFalse(jsonGenerator._cfgUnqNames);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);
    }
}