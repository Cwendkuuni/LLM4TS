package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Test class for {@link ExternalTypeHandler}
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testExternalTypeHandler() throws IOException {
        // given
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"foo\":{\"typeId\":\"StringType\",\"value\":\"test\"},\"bar\":{\"typeId\":\"NumberType\",\"value\":42}}";
        ExternalTypeHandler handler = new ExternalTypeHandler(createBuilder(mapper).build());

        // when parsing JSON
        JsonParser jp = mapper.getFactory().createParser(json);
        Object root = handler.complete(jp, mapper.getDeserializationContext(), null);

        // then
        Map<String, Object> map = (Map<String, Object>) root;
        assertEquals("test", map.get("foo"));
        assertEquals(42, map.get("bar"));

        // when deserializing just the "foo" property
        Object foo = map.get("foo");
        assertEquals("StringType", foo.getClass().getAnnotation(JsonTypeInfo.As.class).value());
        assertEquals("test", ((Map<?, ?>) foo).get("value"));

        // when deserializing just the "bar" property
        Object bar = map.get("bar");
        assertEquals("NumberType", bar.getClass().getAnnotation(JsonTypeInfo.As.class).value());
        assertEquals(42, bar);
    }

    @Test(expected = MappingException.class)
    public void testMissingExternalTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"foo\":{\"value\":\"test\"},\"bar\":{\"typeId\":\"StringType\",\"value\":42}}";
        ExternalTypeHandler handler = new ExternalTypeHandler(createBuilder(mapper).build());

        // when parsing JSON
        JsonParser jp = mapper.getFactory().createParser(json);
        handler.complete(jp, mapper.getDeserializationContext(), null);

        // then exception is thrown
    }

    @Test(expected = MappingException.class)
    public void testMissingExternalProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"foo\":{\"typeId\":\"StringType\",\"value\":\"test\"},\"bar\":{}}";
        ExternalTypeHandler handler = new ExternalTypeHandler(createBuilder(mapper).build());

        // when parsing JSON
        JsonParser jp = mapper.getFactory().createParser(json);
        handler.complete(jp, mapper.getDeserializationContext(), null);

        // then exception is thrown
    }

    @Test(expected = MappingException.class)
    public void testMissingTypeIdAndProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"foo\":{},\"bar\":{}}";
        ExternalTypeHandler handler = new ExternalTypeHandler(createBuilder(mapper).build());

        // when parsing JSON
        JsonParser jp = mapper.getFactory().createParser(json);
        handler.complete(jp, mapper.getDeserializationContext(), null);

        // then exception is thrown
    }

    private static ExternalTypeHandler.Builder createBuilder(ObjectMapper mapper) {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(new SettableBeanProperty("foo", String.class), StringTypeDeserializer.instance);
        builder.addExternal(new SettableBeanProperty("bar", Number.class), NumberTypeDeserializer.instance);
        return builder;
    }

    private static class StringTypeDeserializer extends StdDeserializer<String> {
        public static final StringTypeDeserializer instance = new StringTypeDeserializer();

        public StringTypeDeserializer() {
            this(null);
        }

        protected StringTypeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class NumberTypeDeserializer extends StdDeserializer<Number> {
        public static final NumberTypeDeserializer instance = new NumberTypeDeserializer();

        public NumberTypeDeserializer() {
            this(null);
        }

        protected NumberTypeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsNumber();
        }
    }
}