package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JsonGeneratorImplTest {

    private JsonFactory jsonFactory;
    private JsonGenerator jsonGenerator;

    @Before
    public void setup() throws Exception {
        jsonFactory = new JsonFactory();
        jsonGenerator = jsonFactory.createGenerator(System.out);
    }

    @Test
    public void testJsonGeneratorImplConstructor() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(jsonGeneratorImpl);
    }

    @Test
    public void testSetHighestNonEscapedChar() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.setHighestNonEscapedChar(128);
        jsonGenerator.writeString("test");
        assertEquals("test", jsonGenerator.getOutput(). toString());

        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.setHighestNonEscapedChar(65535);
        jsonGenerator.writeString("test");
        assertEquals("test", jsonGenerator.getOutput().toString());

        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.setHighestNonEscapedChar(0);
        jsonGenerator.writeString("test");
        String output = jsonGenerator.getOutput().toString();
        assertTrue(output.contains("\\u0074"));
        assertTrue(output.contains("\\u0065"));
        assertTrue(output.contains("\\u0073"));
        assertTrue(output.contains("\\u006e"));
        assertTrue(output.contains("\\u0061"));
        assertTrue(output.contains("\\u0073"));
    }

    @Test
    public void testSetCharacterEscapes() throws Exception {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public String getEscapeSequence(int charId) {
                return Integer.toString(charId);
            }
        };
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        jsonGenerator.writeString("test");
        String output = jsonGenerator.getOutput().toString();
        assertTrue(output.contains("\\u0074"));
        assertTrue(output.contains("\\u0065"));
        assertTrue(output.contains("\\u0073"));
        assertTrue(output.contains("\\u006e"));
        assertTrue(output.contains("\\u0061"));
        assertTrue(output.contains("\\u0073"));

        jsonGenerator = jsonFactory.createGenerator(System.out);
        CharacterEscapes standardCharacterEscapes = jsonGenerator.getCharacterEscapes();
        jsonGenerator.setCharacterEscapes(null);
        jsonGenerator.writeString("test");
        output = jsonGenerator.getOutput().toString();
        assertNotNull(standardCharacterEscapes);
        assertTrue(output.contains("\\\""));
        assertTrue(output.contains("\\/"));
        assertTrue(output.contains("\\b"));
        assertTrue(output.contains("\\f"));
        assertTrue(output.contains("\\n"));
        assertTrue(output.contains("\\r"));
        assertTrue(output.contains("\\t"));
        assertNull(jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.setRootValueSeparator(new SerializableString("|"));
        jsonGenerator.writeString("test");
        String output = jsonGenerator.getOutput().toString();
        assertTrue(output.startsWith("|test"));
    }

    @Test
    public void testConfigureFeatures() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.writeObject(new TestObject("test"));
        String output = jsonGenerator.getOutput().toString();
        assertTrue(output.contains("\"test\""));

        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.writeObject(new TestObject("test"));
        output = jsonGenerator.getOutput().toString();
        assertTrue(output.contains("test"));
    }

    @Test
    public void testCheckStdFeatureChanges() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.writeObject(new TestObject("test"));
        String output = jsonGenerator.getOutput().toString();
        assertTrue(output.contains("\"test\""));

        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.writeObject(new TestObject("test"));
        output = jsonGenerator.getOutput().toString();
        assertTrue(output.contains("test"));
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteNull() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeNull();
        jsonGenerator.close();
    }

    @Test
    public void testClose() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.close();
        assertNull(jsonGenerator.getOutput());
    }

    @Test
    public void testWriteStartObject() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeStartObject();
        assertEquals(JsonWriteFeature.WRITE_NULL_SERIALIZERS, jsonGenerator.getFeatures().getMaskForEnabled(JsonWriteFeature.ClassId.WRITE_NULL_SERIALIZERS));
        jsonGenerator.close();
    }

    @Test
    public void testWriteEndObject() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("test");
        jsonGenerator.writeEndObject();
        assertEquals("{\"name\":\"test\"}", jsonGenerator.getOutput().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteFieldName() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("test");
        jsonGenerator.writeEndObject();
        assertEquals("{\"name\":\"test\"}", jsonGenerator.getOutput().toString());
        jsonGenerator.close();

        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeNull();
        jsonGenerator.writeEndObject();
        assertEquals("{\"name\":null}", jsonGenerator.getOutput().toString());
        jsonGenerator.close();

        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeNumber(123);
        jsonGenerator.writeEndObject();
        assertEquals("{\"name\":123}", jsonGenerator.getOutput().toString());
        jsonGenerator.close();

        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeBoolean(true);
        jsonGenerator.writeEndObject();
        assertEquals("{\"name\":true}", jsonGenerator.getOutput().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteString() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeString("test");
        assertEquals("test", jsonGenerator.getOutput().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteNumber() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeNumber(123);
        assertEquals("123", jsonGenerator.getOutput().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteNumberAsString() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeNumber(123.45);
        assertEquals("123.45", jsonGenerator.getOutput().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteBoolean() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeBoolean(true);
        assertEquals("true", jsonGenerator.getOutput().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteNull() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeNull();
        assertEquals("null", jsonGenerator.getOutput().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteValue() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeValue("test");
        assertEquals("test", jsonGenerator.getOutput().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteValueAsString() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeValue("test");
        assertEquals("test", jsonGenerator.getOutput().toString());
        jsonGenerator.close();

        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeValue(123);
        assertEquals("123", jsonGenerator.getOutput().toString());
        jsonGenerator.close();

        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeValue(123.45);
        assertEquals("123.45", jsonGenerator.getOutput().toString());
        jsonGenerator.close();

        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeValue(true);
        assertEquals("true", jsonGenerator.getOutput().toString());
        jsonGenerator.close();

        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeValue(null);
        assertEquals("null", jsonGenerator.getOutput().toString());
        jsonGenerator.close();

        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeValue(new TestObject("test"));
        assertEquals("{\"test\":null}", jsonGenerator.getOutput().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteValueWithNullObjectMapper() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.setCodec(null);
        jsonGenerator.writeValue(new TestObject("test"));
        assertEquals("null", jsonGenerator.getOutput().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteValueWithNullObject() throws Exception {
        jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.writeValue(null);
        assertEquals("null", jsonGenerator.getOutput().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteValueWithNullWriter() throws Exception {
        Writer writer = null;
        jsonGenerator = jsonFactory.createGenerator(writer);
        jsonGenerator.writeValue(new TestObject("test"));
        jsonGenerator.flush();
        jsonGenerator.close();
    }

    private static class TestObject {
        private final String name;

        public TestObject(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "{\"test\":\"" + name + "\"}";
        }
    }
}