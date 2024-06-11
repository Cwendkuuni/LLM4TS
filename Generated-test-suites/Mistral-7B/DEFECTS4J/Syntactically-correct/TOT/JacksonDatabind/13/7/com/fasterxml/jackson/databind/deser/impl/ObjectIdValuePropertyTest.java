package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer.ContextualDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.Object;

public class ObjectIdValuePropertyTest {

    @SuppressWarnings("unchecked")
    private static class TestPojo {
        private Object id;

        @JsonDeserialize(using = ContextualDeserializer.None.class)
        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }
    }

    @SuppressWarnings("unchecked")
    private static class TestIdReader implements ObjectIdReader {
        @Override
        public PropertyName propertyName() {
            return new PropertyName("id");
        }

        @Override
        public Class<?> idType() {
            return Object.class;
        }

        @Override
        public ObjectReader idPropertyReader() {
            return null;
        }

        @Override
        public JsonDeserializer<?> idDeserializer() {
            return null;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return null;
        }

        @Override
        public ObjectIdResolver idResolver() {
            return null;
        }
    }

    @Test
    public void testBasicDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(new TestIdReader(), null);
        mapper.registerPropertyValueCondition(property);

        String json = "{\"id\":1}";
        TestPojo pojo = mapper.readValue(json, TestPojo.class);
        assertEquals(1, pojo.getId());
    }

    @Test
    public void testNullId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(new TestIdReader(), null);
        mapper.registerPropertyValueCondition(property);

        String json = "{}";
        TestPojo pojo = mapper.readValue(json, TestPojo.class);
        assertNull(pojo.getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSettingIdDirectly() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(new TestIdReader(), null);
        mapper.registerPropertyValueCondition(property);

        TestPojo pojo = new TestPojo();
        pojo.setId(1);
        mapper.writeValueAsString(pojo);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSettingIdProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectIdValueProperty property = new ObjectIdValueProperty(new TestIdReader(), null);
        mapper.registerPropertyValueCondition(property);

        TestPojo pojo = new TestPojo();
        TestIdReader idReader = new TestIdReader();
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(idReader, "id");
        pojo.setIdProperty(idProperty);
        pojo.setId(1);
        mapper.writeValueAsString(pojo);
    }
}