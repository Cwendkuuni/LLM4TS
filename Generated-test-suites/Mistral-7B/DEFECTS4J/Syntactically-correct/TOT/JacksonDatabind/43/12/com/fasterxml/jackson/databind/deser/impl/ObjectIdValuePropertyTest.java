package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReaderBase;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        objectIdReader = new ObjectIdReaderBase() {
            @Override
            public String propertyName() {
                return "id";
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> valueDeserializer() {
                return new FromStringDeserializer(Long.class);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
    }

    @Test
    public void testDeserializeAndSetNullId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectNode testObject = mapper.createObjectNode();
        Object id = null;
        Object instance = new Object();

        property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance);
        assertEquals(instance, property.getBean());
        assertEquals(null, property.getValue());
    }

    @Test
    public void testDeserializeAndSetValidId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectNode testObject = mapper.createObjectNode();
        Object id = 1L;
        Object instance = new Object();

        when(mapper.parserForTesting().getValueAsLong()).thenReturn(id);
        property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance);
        assertEquals(instance, property.getBean());
        assertEquals(id, property.getValue());
        ObjectIdReader idReader = mock(ObjectIdReader.class);
        PropertyName idPropertyName = PropertyName.construct("idProperty");
        when(idReader.getIdProperty()).thenReturn(idPropertyName);
        property = new ObjectIdValueProperty(objectIdReader, idReader);
        when(mapper.parserForTesting().getValueAsLong()).thenReturn(id);
        when(mapper.deserializationContext().findObjectId(id, idReader, null)).thenReturn(mock(ReadableObjectId.class));
        property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance);
        assertEquals(instance, property.getBean());
        assertEquals(id, property.getValue());
        assertEquals(id, mapper.readerForUpdating(Object.class).findValue("idProperty", instance)); // check if idProperty is set correctly
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNotNullValueWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);
    }

    @Test(expected = NullPointerException.class)
    public void testDeserializeNullObject() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = null;
        property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), instance);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectNode testObject = mapper.createObjectNode();
        testObject.put("invalidJson", "InvalidJson");
        property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationContext(), new Object());
    }

    @Test
    public void testSetValue() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        Object value = new Object();
        property.setValue(instance, value);
        assertEquals(value, mapper.readerForUpdating(Object.class).findValue("id", instance));
    }
}