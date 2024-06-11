package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader DUMMY_OBJECT_ID_READER = new ObjectIdReader() {
        @Override
        public PropertyName propertyName() {
            return PropertyName.construct("id");
        }

        @Override
        public Class<?> idType() {
            return Long.class;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return new StdObjIdGenerator();
        }

        @Override
        public JsonDeserializer<?> idDeserializer() {
            return new JsonLongDeserializer();
        }

        @Override
        public PropertyMetadata metadata() {
            return new PropertyMetadata();
        }

        @Override
        public SettableBeanProperty idProperty() {
            return null;
        }

        @Override
        public NullValueProvider nullProvider() {
            return null;
        }
    };

    private static final ObjectIdValueProperty DUMMY_OBJECT_ID_VALUE_PROPERTY = new ObjectIdValueProperty(
            DUMMY_OBJECT_ID_READER, new PropertyMetadata());

    @org.junit.Test
    public void testDeserializeAndSetWithValidId() throws IOException {
        ObjectParser parser = new ObjectMapper().treeAsTokens(
                "{\"id\":1}");
        JsonParser jsonParser = parser.getParser();
        Object object = new Object();

        ObjectIdValueProperty.deserializeAndSet(jsonParser, new DeserializationContext(), object);

        assertEquals(1, object.hashCode());
    }

    @org.junit.Test
    public void testDeserializeAndSetWithNullId() throws IOException {
        ObjectParser parser = new ObjectMapper().treeAsTokens(
                "{\"id\":null}");
        JsonParser jsonParser = parser.getParser();

        Object object = new Object();

        ObjectIdValueProperty.deserializeAndSet(jsonParser, new DeserializationContext(), object);

        assertNull(object);
    }

    @org.junit.Test
    public void testDeserializeAndSetWithEmptyId() throws IOException {
        ObjectParser parser = new ObjectMapper().treeAsTokens(
                "{\"id\":\"\"}");
        JsonParser jsonParser = parser.getParser();

        Object object = new Object();

        ObjectIdValueProperty.deserializeAndSet(jsonParser, new DeserializationContext(), object);

        assertNull(object);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetWithNullValue() throws IOException {
        Object object = new Object();

        ObjectIdValueProperty.setAndReturn(object, null);
    }

    @org.junit.Test
    public void testSetWithValidValue() throws IOException {
        Object object = new Object();
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(DUMMY_OBJECT_ID_READER, new PropertyMetadata());

        Object value = new Long(1);

        Object result = idProperty.setAndReturn(object, value);

        assertEquals(value.hashCode(), object.hashCode());
        assertEquals(value, result);
    }

    @org.junit.Test
    public void testSetWithEmptyValue() throws IOException {
        Object object = new Object();
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(DUMMY_OBJECT_ID_READER, new PropertyMetadata());

        Object value = JsonNodeFactory.nullValue();

        Object result = idProperty.setAndReturn(object, value);

        assertNull(object);
        assertNull(result);
    }

    @org.junit.Test
    public void testSetWithCustomValueDeserializer() throws IOException {
        ObjectParser parser = new ObjectMapper().treeAsTokens(
                "{\"id\":\"test\"}");
        JsonParser jsonParser = parser.getParser();
        JsonDeserializer<String> customValueDeserializer = new JsonStringDeserializer();

        Object object = new Object();
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(
                DUMMY_OBJECT_ID_READER, new PropertyMetadata(), new JsonDeserializer.NullValueProvider());

        Object result = idProperty.withValueDeserializer(customValueDeserializer)
                .deserializeAndSet(jsonParser, new DeserializationContext(), object);

        assertEquals("test", result);
    }

    @org.junit.Test
    public void testSetWithCustomNullValueProvider() throws IOException {
        ObjectParser parser = new ObjectMapper().treeAsTokens(
                "{\"id\":null}");
        JsonParser jsonParser = parser.getParser();

        Object object = new Object();
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(
                DUMMY_OBJECT_ID_READER, new PropertyMetadata(),
                new JsonDeserializer.NullValueProvider() {
                    @Override
                    public JsonNode getNullNode() throws IOException {
                        return JsonNodeFactory.nullValue();
                    }
                });

        Object result = idProperty.deserializeAndSet(jsonParser, new DeserializationContext(), object);

        assertNull(result);
    }

    @org.junit.Test
    public void testSetWithCustomName() throws IOException {
        ObjectParser parser = new ObjectMapper().treeAsTokens(
                "{\"name\":\"test\"}");
        JsonParser jsonParser = parser.getParser();
        PropertyName newName = PropertyName.construct("name");

        Object object = new Object();
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(
                DUMMY_OBJECT_ID_READER, new PropertyMetadata(), new JsonDeserializer.NullValueProvider(), new PropertyName("id"));

        Object result = idProperty.withName(newName).deserializeAndSet(jsonParser, new DeserializationContext(), object);

        assertEquals("test", result);
    }

    @org.junit.Test
    public void testSerializedObjectHasCorrectId() throws IOException {
        ObjectParser parser = new ObjectMapper().treeAsTokens(
                "{\"id\":1}");
        JsonParser jsonParser = parser.getParser();
        Object object = new Object();

        ObjectIdValueProperty.deserializeAndSet(jsonParser, new DeserializationContext(), object);

        ObjectNode jsonNode = new ObjectMapper().readValue(
                ObjectIdValueProperty.serializeValue(object), ObjectNode.class);

        assertEquals(1, jsonNode.get("id").asLongValue());
    }

    @org.junit.Test
    public void testSerializedObjectHasNullIdIfNull() throws IOException {
        ObjectParser parser = new ObjectMapper().treeAsTokens(
                "{\"id\":null}");
        JsonParser jsonParser = parser.getParser();
        Object object = new Object();

        ObjectIdValueProperty.deserializeAndSet(jsonParser, new DeserializationContext(), object);

        ObjectNode jsonNode = new ObjectMapper().readValue(
                ObjectIdValueProperty.serializeValue(object), ObjectNode.class);

        assertNull(jsonNode.get("id"));
    }

    @org.junit.Test
    public void testSerializedObjectHasCorrectIdProperty() throws IOException {
        ObjectParser parser = new ObjectMapper().treeAsTokens(
                "{\"id\":1,\"name\":\"test\"}");
        JsonParser jsonParser = parser.getParser();
        Object object = new Object();
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(
                DUMMY_OBJECT_ID_READER, new PropertyMetadata(), new JsonDeserializer.NullValueProvider(), new PropertyName("id"));
        ObjectIdValueProperty nameProperty = new ObjectIdValueProperty(
                DUMMY_OBJECT_ID_READER, new PropertyMetadata(), new JsonDeserializer.NullValueProvider(), new PropertyName("name"));
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode jsonNode = mapper.readValue(jsonParser, ObjectNode.class);

        ObjectIdPropertyValue idPropertyValue = mapper.readerForUpdating(Object.class)
                .readValue(jsonNode);

        assertEquals(idProperty, idPropertyValue);
        assertEquals(nameProperty, jsonNode.get("name").isContainerNode() ? mapper.readerForUpdating(Object.class)
                .readValue(jsonNode.get("name")) : null);
    }
}