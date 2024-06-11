package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StandardDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.util.TestBase;

import org.junit.Test;

/**
 * Unit tests for {@link StringArrayDeserializer} class.
 */
public class StringArrayDeserializerTest extends TestBase {

    @Test
    public void testBasicDeserialization() throws Exception {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().addDeserializer(String[].class, StringArrayDeserializer.instance));
        StringJsonParser jp = mapper.getFactory().createParser("[\"foo\", \"bar\", \"baz\"]");

        String[] result = StringArrayDeserializer.instance.deserialize(jp, new DeserializationContext.Instance());
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testCustomDeserialization() throws Exception {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .addDeserializer(String[].class, StringArrayDeserializer.instance)
                .addDeserializer(String.class, new TestCustomStringDeserializer()));

        StringJsonParser jp = mapper.getFactory().createParser("[\"foo\", \"bar\", \"baz\"]");
        JsonDeserializer<String> customDeserializer = mapper.getFactory().valueDeserializerOf(String.class);
        StringArrayDeserializer deserializer = (StringArrayDeserializer) StringArrayDeserializer.instance.createContextual(
                new DeserializationContext.Instance(mapper.getConfiguration(), null, null), null);
        deserializer._elementDeserializer = customDeserializer;

        String[] result = deserializer.deserialize(jp, new DeserializationContext.Instance());
        assertArrayEquals(new String[]{"testCustomStringDeserializer:foo", "testCustomStringDeserializer:bar", "testCustomStringDeserializer:baz"}, result);
    }

    @Test
    public void testEmptyArrayDeserialization() throws Exception {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().addDeserializer(String[].class, StringArrayDeserializer.instance));

        StringJsonParser jp = mapper.getFactory().createParser("[]");
        String[] result = StringArrayDeserializer.instance.deserialize(jp, new DeserializationContext.Instance());
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testNullDeserialization() throws Exception {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().addDeserializer(String[].class, StringArrayDeserializer.instance));

        StringJsonParser jp = mapper.getFactory().createParser("null");
        String[] result = StringArrayDeserializer.instance.deserialize(jp, new DeserializationContext.Instance());
        assertNull(result);
    }

    @Test
    public void testNonArrayDeserialization() throws Exception {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().addDeserializer(String[].class, StringArrayDeserializer.instance));

        StringJsonParser jp = mapper.getFactory().createParser("\"foo\"");
        try {
            StringArrayDeserializer.instance.deserialize(jp, new DeserializationContext.Instance());
            fail("Expected MappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unrecognized token 'STRING': was expecting 'START_ARRAY'", e.getMessage());
        }
    }

    @Test
    public void testTypeDeserialization() throws Exception {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .addDeserializer(String[].class, StringArrayDeserializer.instance)
                .addDeserializer(String[].class, "myArrayType", new MyArrayTypeDeserializer()));

        StringJsonParser jp = mapper.getFactory().createParser("[\"foo\", \"bar\", \"baz\"]");
        Object result = StringArrayDeserializer.instance.deserializeWithType(jp, new DeserializationContext.Instance(), mapper.getTypeFactory().constructType("java.lang.String[]"));
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);

        result = StringArrayDeserializer.instance.deserializeWithType(jp, new DeserializationContext.Instance(), mapper.getTypeFactory().constructType("com.example.MyArrayType"));
        assertInstanceOf(MyArrayType.class, result);
    }

    private static class MyArrayTypeDeserializer extends StdDeserializer<Object> {
        public MyArrayTypeDeserializer() {
            super(Object.class);
        }

        @Override
        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
        }

        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new MyArrayType();
        }
    }

    static class TestCustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getText() + ":testCustomStringDeserializer";
        }
    }

}