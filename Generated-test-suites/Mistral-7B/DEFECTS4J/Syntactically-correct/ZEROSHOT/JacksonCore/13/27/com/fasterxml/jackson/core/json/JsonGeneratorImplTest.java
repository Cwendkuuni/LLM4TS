package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class JsonGeneratorImplTest {

    private IOContext ioContext;
    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() {
        ioContext = mock(IOContext.class);
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null) {
            @Override
            public void close() throws IOException {
                // no-op
            }
        };
    }

    @Test
    public void testDefaultConstructor() {
        // Nothing to test as the constructor is abstract
    }

    @Test
    public void testDefaultOutputEscapes() throws IOException, JsonProcessingException {
        JsonParser parser = new JsonParser(new Utf8JsonFactory());
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();

        String result = parser.getValueAsString();
        Assert.assertEquals("{\"name\":\"John Doe\"}", result);
    }

    @Test
    public void testSetOutputEscapes() throws IOException, JsonProcessingException {
        CharacterEscapes characterEscapes = mock(CharacterEscapes.class);
        int[] outputEscapes = new int[256];

        jsonGenerator.setCharacterEscapes(characterEscapes);
        jsonGenerator.setOutputEscapes(outputEscapes);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();

        String result = new String(jsonGenerator.getOutputBuffer().getData(), 0, jsonGenerator.getOutputBuffer().size());
        Assert.assertEquals("{\"name\":\"John Doe\"}", result);

        verify(characterEscapes).getEscapeCodesForAscii();
    }

    @Test
    public void testSetMaximumNonEscapedChar() throws IOException, JsonProcessingException {
        jsonGenerator.setHighestNonEscapedChar(255);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doé");
        jsonGenerator.writeEndObject();

        String result = new String(jsonGenerator.getOutputBuffer().getData(), 0, jsonGenerator.getOutputBuffer().size());
        Assert.assertEquals("{\"name\":\"John Doé\"}", result);

        byte[] bytes = jsonGenerator.getOutputBuffer().getData();
        Assert.assertEquals((byte) 'Ã', bytes[22]); // 'ö' UTF-8 byte sequence first byte
        Assert.assertEquals((byte) 'o', bytes[23]); // 'ö' UTF-8 byte sequence second byte
    }

    @Test(expected = JsonGenerationException.class)
    public void testSetMaximumNonEscapedCharInvalidChar() throws IOException, JsonProcessingException {
        jsonGenerator.setHighestNonEscapedChar(Character.MAX_VALUE);
    }

    @Test
    public void testSetRootValueSeparator() throws IOException, JsonProcessingException {
        jsonGenerator.setRootValueSeparator(new SerializableString(","));

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();

        String result = new String(jsonGenerator.getOutputBuffer().getData(), 0, jsonGenerator.getOutputBuffer().size());
        Assert.assertEquals("{,\"name\":\"John Doe\"}", result);
    }

    @Test
    public void testEnableQuoteFieldNames() throws IOException, JsonProcessingException {
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();

        String result = new String(jsonGenerator.getOutputBuffer().getData(), 0, jsonGenerator.getOutputBuffer().size());
        Assert.assertEquals("{\"name\":\"John Doe\"}", result);

        result = new String(jsonGenerator.getOutputBuffer().getData(), jsonGenerator.getOutputBuffer().getPosition(), jsonGenerator.getOutputBuffer().size() - jsonGenerator.getOutputBuffer().getPosition());
        Assert.assertFalse(result.contains("name"));

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeNull();
        jsonGenerator.writeEndObject();

        result = new String(jsonGenerator.getOutputBuffer().getData(), jsonGenerator.getOutputBuffer().getPosition(), jsonGenerator.getOutputBuffer().size() - jsonGenerator.getOutputBuffer().getPosition());
        Assert.assertEquals("{\"name\":null}", result);
    }

    @Test
    public void testDisableQuoteFieldNames() throws IOException, JsonProcessingException {
        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();

        String result = new String(jsonGenerator.getOutputBuffer().getData(), 0, jsonGenerator.getOutputBuffer().size());
        Assert.assertEquals("{name:\"John Doe\"}", result);
    }

    @Test
    public void testEnableEscapeNonAscii() throws IOException, JsonProcessingException {
        jsonGenerator.enable(Feature.ESCAPE_NON_ASCII);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();

        String result = new String(jsonGenerator.getOutputBuffer().getData(), 0, jsonGenerator.getOutputBuffer().size());
        Assert.assertEquals("{\"name\":\"John Doe\"}", result);

        result = new String(jsonGenerator.getOutputBuffer().getData(), jsonGenerator.getOutputBuffer().getPosition(), jsonGenerator.getOutputBuffer().size() - jsonGenerator.getOutputBuffer().getPosition());
        Assert.assertEquals("name\":\"John Doe\"", result);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doé");
        jsonGenerator.writeEndObject();

        result = new String(jsonGenerator.getOutputBuffer().getData(), jsonGenerator.getOutputBuffer().getPosition(), jsonGenerator.getOutputBuffer().size() - jsonGenerator.getOutputBuffer().getPosition());
        Assert.assertEquals("name\":\"John Doé\"", result);
    }

    @Test
    public void testDisableEscapeNonAscii() throws IOException, JsonProcessingException {
        jsonGenerator.disable(Feature.ESCAPE_NON_ASCII);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doé");
        jsonGenerator.writeEndObject();

        String result = new String(jsonGenerator.getOutputBuffer().getData(), 0, jsonGenerator.getOutputBuffer().size());
        Assert.assertEquals("{\"name\":\"John Doé\"}", result);
    }

    @Test
    public void testVersion() {
        Assert.assertNotNull(jsonGenerator.version());
    }
}