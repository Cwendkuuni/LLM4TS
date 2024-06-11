package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ObjectMap<String, Integer> nameToPropertyIndex;
    private String[] typeIds;
    private TokenBuffer[] tokens;
    private ExtTypedProperty[] properties;

    @Before
    public void setup() {
        // Prepare test data
        nameToPropertyIndex = new HashMap<>();
        typeIds = new String[3];
        tokens = new TokenBuffer[3];
        properties = new ExtTypedProperty[3];

        SettableBeanProperty prop1 = new SimpleBeanProperty("prop1", String.class);
        SettableBeanProperty prop2 = new SimpleBeanProperty("prop2", Integer.class);
        SettableBeanProperty prop3 = new SimpleBeanProperty("prop3", Float.class);

        TypeDeserializer typeDeser1 = new StdScalarTypeDeserializer(String.class);
        TypeDeserializer typeDeser2 = new StdScalarTypeDeserializer(Integer.class);
        TypeDeserializer typeDeser3 = new StdScalarTypeDeserializer(Float.class);

        properties[0] = new ExtTypedProperty(prop1, typeDeser1);
        properties[1] = new ExtTypedProperty(prop2, typeDeser2);
        properties[2] = new ExtTypedProperty(prop3, typeDeser3);

        nameToPropertyIndex.put("prop1", 0);
        nameToPropertyIndex.put("prop2", 1);
        nameToPropertyIndex.put("prop3", 2);
        nameToPropertyIndex.put("typeId1", 0);
        nameToPropertyIndex.put("typeId2", 1);
        nameToPropertyIndex.put("typeId3", 2);
    }

    @Test
    public void testStart() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null).start();
        assertNotNull(handler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleTypePropertyValueWithNullProperties() throws IOException {
        new ExternalTypeHandler(null, nameToPropertyIndex, null, null).handleTypePropertyValue(mockParser(), mockContext(), "prop1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleTypePropertyValueWithNullNameToPropertyIndex() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, null, null, null);
        handler.handleTypePropertyValue(mockParser(), mockContext(), "prop1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleTypePropertyValueWithInvalidPropertyIndex() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        handler.handleTypePropertyValue(mockParser(), mockContext(), "invalidProperty", null);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        handler.handleTypePropertyValue(mockParser(), mockContext(), "prop1", new Object());
        assertEquals("typeId1", handler._typeIds[0]);
        assertEquals(null, handler._tokens[0]);
    }

    @Test
    public void testHandlePropertyValueWithNullProperties() throws IOException {
        new ExternalTypeHandler(null, nameToPropertyIndex, null, null).handlePropertyValue(mockParser(), mockContext(), "prop1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandlePropertyValueWithNullNameToPropertyIndex() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, null, null, null);
        handler.handlePropertyValue(mockParser(), mockContext(), "prop1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandlePropertyValueWithInvalidPropertyIndex() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        handler.handlePropertyValue(mockParser(), mockContext(), "invalidProperty", null);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        handler.handlePropertyValue(mockParser(), mockContext(), "prop1", new Object());
        assertNull(handler._typeIds[0]);
        assertEquals(mockParser(), handler._tokens[0]);
    }

    @Test
    public void testCompleteWithNullProperties() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, nameToPropertyIndex, null, null);
        handler.complete(mockParser(), mockContext(), null);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        handler.handlePropertyValue(mockParser(), mockContext(), "prop1", new Object());
        handler.handlePropertyValue(mockParser(), mockContext(), "prop2", new Object());
        handler.handlePropertyValue(mockParser(), mockContext(), "prop3", new Object());

        Object result = handler.complete(mockParser(), mockContext(), null);
        assertNotNull(result);
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
        when(creator.build(mock(DeserializationContext.class), mock(PropertyValueBuffer.class))).thenReturn(new Object());

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        handler.handlePropertyValue(mockParser(), mockContext(), "prop1", new Object());
        handler.handlePropertyValue(mockParser(), mockContext(), "prop2", new Object());
        handler.handlePropertyValue(mockParser(), mockContext(), "prop3", new Object());

        Object result = handler.complete(mockParser(), mockContext(), mock(PropertyValueBuffer.class), creator);
        assertNotNull(result);

        verify(creator).build(mock(DeserializationContext.class), mock(PropertyValueBuffer.class));
    }

    @Test(expected = NullPointerException.class)
    public void testCompleteWithNullParser() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        handler.handlePropertyValue(mockParser(), mockContext(), "prop1", new Object());
        handler.handlePropertyValue(mockParser(), mockContext(), "prop2", new Object());
        handler.handlePropertyValue(mockParser(), mockContext(), "prop3", new Object());

        handler.complete(null, mockContext(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompleteWithNullContext() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        handler.handlePropertyValue(mockParser(), mockContext(), "prop1", new Object());
        handler.handlePropertyValue(mockParser(), mockContext(), "prop2", new Object());
        handler.handlePropertyValue(mockParser(), mockContext(), "prop3", new Object());

        handler.complete(mockParser(), null);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        handler.handlePropertyValue(mockParser(), mockContext(), "prop1", new Object());
        handler.handlePropertyValue(mockParser(), mockContext(), "prop2", new Object());
        handler.handlePropertyValue(mockParser(), mockContext(), "prop3", new Object());

        Object result = handler.complete(mockParser(), mockContext(), null);
        assertNotNull(result);
    }

    private JsonParser mockParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext mockContext() {
        return mock(DeserializationContext.class);
    }
}

class SimpleBeanProperty {
    private final String name;
    private final Class<?> clazz;

    public SimpleBeanProperty(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return clazz;
    }
}