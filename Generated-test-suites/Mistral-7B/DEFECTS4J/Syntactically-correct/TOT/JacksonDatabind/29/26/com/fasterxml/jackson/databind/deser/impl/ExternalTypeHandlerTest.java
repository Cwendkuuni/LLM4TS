package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.StandardDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser jp = factory.createParser("{\"typeProperty\":\"String\"}");

        Builder builder = new Builder();
        builder.addExternal(new SettableBeanProperty("typeProperty", String.class), new StringTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        DeserializationContext context = new StandardDeserializationContext();
        Object bean = handler.start().handleTypePropertyValue(jp, context, "typeProperty", null);
        assertEquals("String", bean.getClass().getName());

        jp.close();
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser jp = factory.createParser("{\"property\":\"value\"}");

        Builder builder = new Builder();
        builder.addExternal(new SettableBeanProperty("property", String.class), new StringTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        DeserializationContext context = new StandardDeserializationContext();
        Object bean = handler.start().handlePropertyValue(jp, context, "property", null);
        assertEquals("value", bean);

        jp.close();
    }

    @Test
    public void testComplete() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser jp = factory.createParser("[\"String\",{\"typeProperty\":\"Number\"}]");

        Builder builder = new Builder();
        builder.addExternal(new SettableBeanProperty("typeProperty", Number.class), new NumberTypeDeserializer());
        ExternalTypeHandler handler = builder.build();
        handler = handler.start();

        DeserializationContext context = new StandardDeserializationContext();
        Object bean = handler.complete(jp, context, null);
        assertEquals(Arrays.asList("String", new Integer(1)), bean);

        jp.close();
    }

    @Test(expected = JsonProcessingException.class)
    public void testMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser jp = factory.createParser("[{\"typeProperty\":\"String\"}]");

        Builder builder = new Builder();
        builder.addExternal(new SettableBeanProperty("typeProperty", String.class), new StringTypeDeserializer());
        ExternalTypeHandler handler = builder.build();
        handler = handler.start();

        DeserializationContext context = new StandardDeserializationContext();
        handler.complete(jp, context, null);

        jp.close();
    }

    @Test(expected = JsonProcessingException.class)
    public void testMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser jp = factory.createParser("[{\"property\":[1,2,3]}]");

        Builder builder = new Builder();
        builder.addExternal(new SettableBeanProperty("property", List.class), new ListTypeDeserializer());
        ExternalTypeHandler handler = builder.build();
        handler = handler.start();

        DeserializationContext context = new StandardDeserializationContext();
        handler.complete(jp, context, null);

        jp.close();
    }

    @Test
    public void testNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser jp = factory.createParser("[null]");

        Builder builder = new Builder();
        builder.addExternal(new SettableBeanProperty("property", String.class), new StringTypeDeserializer());
        ExternalTypeHandler handler = builder.build();
        handler = handler.start();

        DeserializationContext context = new StandardDeserializationContext();
        Object bean = handler.complete(jp, context, null);
        assertNull(bean);

        jp.close();
    }

    static class StringTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getText();
        }
    }

    static class NumberTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsNumber();
        }
    }

    static class ListTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jp, List.class);
        }
    }
}