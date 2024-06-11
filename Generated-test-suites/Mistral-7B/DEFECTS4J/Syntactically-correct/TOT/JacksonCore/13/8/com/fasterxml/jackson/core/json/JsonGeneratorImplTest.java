package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.JsonTest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link JsonGeneratorImpl} class.
 *
 * @author John Doe
 */
public class JsonGeneratorImplTest extends JsonTest {

    /**
     * Test method for {@link JsonGeneratorImpl#JsonGeneratorImpl(IOContext, int, ObjectCodec)} constructor.
     */
    @Test
    public void testJsonGeneratorImplConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(this.getOutputStream(), null);
        assertTrue(generator instanceof JsonGeneratorImpl);
    }

    /**
     * Test method for {@link JsonGeneratorImpl#enable(Feature)} method.
     */
    @Test
    public void testEnableFeature() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(this.getOutputStream(), null);

        generator.enable(Feature.QUOTE_FIELD_NAMES);
        generator.writeStartObject();
        generator.writeFieldName("field1");
        generator.writeValue("value1");
        generator.writeEndObject();
        generator.close();

        String result = this.readValueAsString(this.getInputStream());
        assertEquals("{\"field1\":\"value1\"}", result);

        generator = mapper.getFactory().createGenerator(this.getOutputStream(), null);
        generator.disable(Feature.QUOTE_FIELD_NAMES);
        generator.writeStartObject();
        generator.writeFieldName("field1");
        generator.writeValue("value1");
        generator.writeEndObject();
        generator.close();

        result = this.readValueAsString(this.getInputStream());
        assertEquals("{field1=value1}", result);
    }

    /**
     * Test method for {@link JsonGeneratorImpl#setHighestNonEscapedChar(int)} method.
     */
    @Test
    public void testSetHighestNonEscapedChar() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(this.getOutputStream(), null);

        generator.setHighestNonEscapedChar(127);
        generator.writeStartObject();
        generator.writeFieldName("field1");
        generator.writeValue("char127");
        generator.writeEndObject();
        generator.close();

        String result = this.readValueAsString(this.getInputStream());
        assertEquals("{\"field1\":\"char127\"}", result);

        generator = mapper.getFactory().createGenerator(this.getOutputStream(), null);
        generator.setHighestNonEscapedChar(65535);
        generator.writeStartObject();
        generator.writeFieldName("field1");
        generator.writeValue("char65535");
        generator.writeEndObject();
        generator.close();

        result = this.readValueAsString(this.getInputStream());
        assertEquals("{\"field1\":\"char65535\"}", result);

        generator = mapper.getFactory().createGenerator(this.getOutputStream(), null);
        generator.setHighestNonEscapedChar(0);
        generator.writeStartObject();
        generator.writeFieldName("field1");
        generator.writeValue("nonAsciiChar"); // e.g. "é"
        generator.writeEndObject();
        generator.close();

        result = this.readValueAsString(this.getInputStream());
        assertEquals("{\"field1\":\"nonAsciiChar\"}", result);

        generator = mapper.getFactory().createGenerator(this.getOutputStream(), null);
        generator.setHighestNonEscapedChar(-1);
        generator.writeStartObject();
        generator.writeFieldName("field1");
        generator.writeValue("nonAsciiChar"); // e.g. "é"
        generator.writeEndObject();
        generator.close();

        result = this.readValueAsString(this.getInputStream());
        assertTrue(result.contains("\\u00E9"));
    }

    /**
     * Test method for {@link JsonGeneratorImpl#setCharacterEscapes(CharacterEscapes)} method.
     */
    @Test
    public void testSetCharacterEscapes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(this.getOutputStream(), null);

        CharacterEscapes customEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 1, 2, 3 };
            }
        };
        generator.setCharacterEscapes(customEscapes);
        generator.writeStartObject();
        generator.writeFieldName("field1");
        generator.writeValue("char1");
        generator.writeFieldName("field2");
        generator.writeValue("char2");
        generator.writeEndObject();
        generator.close();

        String result = this.readValueAsString(this.getInputStream());
        assertEquals("{\"field1\":\"char1\\u0001\",\"field2\":\"char2\\u0002\"}", result);
    }

    /**
     * Test method for {@link JsonGeneratorImpl#setRootValueSeparator(SerializableString)} method.
     */
    @Test
    public void testSetRootValueSeparator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(this.getOutputStream(), null);
        SerializableString customSeparator = new SerializableString(":");
        generator.setRootValueSeparator(customSeparator);
        generator.writeStartObject();
        generator.writeValue("value1");
        generator.writeEndObject();
        generator.close();

        String result = this.readValueAsString(this.getInputStream());
        assertEquals("{\":value1\"}", result);
    }
}