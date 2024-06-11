package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.ExtTypedProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Builder builder = new Builder();
        builder.addExternal(new PropertyName("color"), new ColorTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        String json = "{\"color\":\"red\",\"value\":123}";
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(p, ctxt, "color", bean);
        assertEquals("red", bean.getClass().getField("color").get(bean));
    }

    @Test
    public void testHandlePropertyValue() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Builder builder = new Builder();
        builder.addExternal(new PropertyName("name"), new NameTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        String json = "{\"name\":\"John\",\"value\":123}";
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(p, ctxt, "name", bean);
        assertEquals("John", bean.getClass().getField("name").get(bean));
    }

    @Test
    public void testHandlePropertyValue_MissingTypeId() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Builder builder = new Builder();
        builder.addExternal(new PropertyName("name"), new NameTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        String json = "{\"value\":123}";
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(p, ctxt, "name", bean);
        assertNull(bean.getClass().getField("name").get(bean));
    }

    @Test
    public void testHandlePropertyValue_MissingProperty() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Builder builder = new Builder();
        builder.addExternal(new PropertyName("color"), new ColorTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        String json = "{\"value\":123,\"typeId\":\"MyColorType\"}";
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(p, ctxt, "nonexistent", bean);
        assertNull(bean.getClass().getField("color").get(bean));
    }

    @Test
    public void testComplete() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Builder builder = new Builder();
        builder.addExternal(new PropertyName("color"), new ColorTypeDeserializer());
        builder.addExternal(new PropertyName("value"), new ValueTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        String json = "{\"color\":\"red\",\"value\":123}";
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.complete(p, ctxt, bean);
        assertEquals("red", bean.getClass().getField("color").get(bean));
        assertEquals(123, bean.getClass().getField("value").get(bean));
    }

    @Test
    public void testComplete_MissingTypeId()
        throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Builder builder = new Builder();
        builder.addExternal(new PropertyName("color"), new ColorTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        String json = "{\"value\":123}";
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object[2];

        try {
            handler.complete(p, ctxt, bean);
            fail("Expected MappingException");
        } catch (MappingException e) {
            assertEquals(
                "Missing property 'color' for external type id 'null'", e.getMessage());
        }
    }

    @Test
    public void testComplete_MissingProperty()
        throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Builder builder = new Builder();
        builder.addExternal(new PropertyName("color"), new ColorTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        String json = "{\"typeId\":\"MyColorType\",\"value\":123}";
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        try {
            handler.complete(p, ctxt, bean);
            fail("Expected MappingException");
        } catch (MappingException e) {
            assertEquals(
                "Missing property 'color' for external type id 'MyColorType'", e.getMessage());
        }
    }

    @Test
    public void testComplete_NullValue()
        throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Builder builder = new Builder();
        builder.addExternal(new PropertyName("color"), new ColorTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        String json = "{\"typeId\":null,\"value\":null}";
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.complete(p, ctxt, bean);
        assertNull(bean.getClass().getField("color").get(bean));
    }

    @Test
    public void testComplete_MultipleProperties()
        throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Builder builder = new Builder();
        builder.addExternal(new PropertyName("color"), new ColorTypeDeserializer());
        builder.addExternal(new PropertyName("name"), new NameTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        String json = "{\"color\":\"red\",\"value\":123,\"name\":\"John\"}";
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object[3];

        handler.complete(p, ctxt, bean);
        assertEquals("red", bean[0].getClass().getField("color").get(bean[0]));
        assertEquals(123, bean[1].getClass().getField("value").get(bean[1]));
        assertEquals("John", bean[2].getClass().getField("name").get(bean[2]));
    }

    @Test
    public void testComplete_MultipleProperties_NullValues()
        throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Builder builder = new Builder();
        builder.addExternal(new PropertyName("color"), new ColorTypeDeserializer());
        builder.addExternal(new PropertyName("name"), new NameTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        String json = "{\"color\":null,\"value\":null,\"name\":null}";
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object[3];

        handler.complete(p, ctxt, bean);
        assertNull(bean[0].getClass().getField("color").get(bean[0]));
        assertNull(bean[1].getClass().getField("value").get(bean[1]));
        assertNull(bean[2].getClass().getField("name").get(bean[2]));
    }

    @Test
    public void testComplete_NestedObjects()
        throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Builder builder = new Builder();
        builder.addExternal(new PropertyName("color"), new ColorTypeDeserializer());
        builder.addExternal(new PropertyName("name"), new NameTypeDeserializer());
        ExternalTypeHandler handler = builder.build();

        String json = "{\"color\":\"red\",\"value\":{\"name\":\"John\",\"age\":30}}}";
        JsonParser p = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object[3];

        handler.complete(p, ctxt, bean);
        assertEquals("red", bean[0].getClass().getField("color").get(bean[0]));
        assertEquals("John", bean[1].getClass().getField("name").get(bean[1]));
        assertEquals(30, bean[2].getClass().getField("age").get(bean[2]));
    }

    private static class ColorTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
                throws IOException {
            String color = jp.getText();
            return color;
        }

        @Override
        public String getPropertyName() {
            return "color";
        }
    }

    private static class NameTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
                throws IOException {
            String name = jp.getText();
            return name;
        }

        @Override
        public String getPropertyName() {
            return "name";
        }
    }

    private static class ValueTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
                throws IOException {
            int value = jp.getIntValue();
            return value;
        }

        @Override
        public String getPropertyName() {
            return "value";
        }
    }
}