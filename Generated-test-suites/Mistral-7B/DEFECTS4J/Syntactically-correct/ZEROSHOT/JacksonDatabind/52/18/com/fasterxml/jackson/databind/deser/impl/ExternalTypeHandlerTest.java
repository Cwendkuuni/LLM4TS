package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Test class for ExternalTypeHandler
 */
public class ExternalTypeHandlerTest {

    private ObjectMapper mapper;
    private ExternalTypeHandler handler;
    private JsonParser parser;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        parser = Mockito.spy(mapper.getFactory().createParser("{"
                + "\"name\":\"John\","
                + "\"age\":42,"
                + "\"typeId1\":\"Type1\""
                + "}"));
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(Mockito.mock(SettableBeanProperty.class), Mockito.mock(TypeDeserializer.class))
                .addExternal(Mockito.mock(SettableBeanProperty.class), Mockito.mock(TypeDeserializer.class))
                .build());
    }

    @Test
    public void testStart() throws IOException {
        handler = handler.start();
        assertTrue(handler instanceof ExternalTypeHandler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        handler.handleTypePropertyValue(parser, Mockito.mock(DeserializationContext.class), "typeId1", Mockito.mock(Object.class));
        assertEquals("Type1", handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue_nullValue() throws IOException {
        handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "name", Mockito.mock(Object.class));
        assertNull(handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue_nonNullValue() throws IOException {
        handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "age", Mockito.mock(Object.class));
        assertNotNull(handler._tokens[0]);
    }

    @Test
    public void testHandlePropertyValue_noPropertyFound() throws IOException {
        handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "invalidPropertyName", Mockito.mock(Object.class));
        assertFalse(handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "name", Mockito.mock(Object.class)));
    }

    @Test
    public void testHandlePropertyValue_typePropertyFound() throws IOException {
        handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "typeId1", Mockito.mock(Object.class));
        assertTrue(handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "name", Mockito.mock(Object.class)));
    }

    @Test
    public void testComplete() throws IOException {
        handler.complete(parser, Mockito.mock(DeserializationContext.class), Mockito.mock(Object.class));
        assertEquals(2, handler._properties.length);
        assertEquals("John", handler._properties[0].getProperty().getName());
        assertEquals("Type1", handler._typeIds[0]);
        assertEquals(42, handler._properties[1].getProperty().get(handler._properties[1].getProperty().get(Mockito.mock(Object.class))));
    }

    @Test
    public void testComplete_PropertyBasedCreator() throws IOException {
        Object creator = Mockito.mock(PropertyBasedCreator.class);
        PropertyValueBuffer buffer = Mockito.mock(PropertyValueBuffer.class);
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(Mockito.mock(SettableBeanProperty.class), Mockito.mock(TypeDeserializer.class))
                .addExternal(Mockito.mock(SettableBeanProperty.class), Mockito.mock(TypeDeserializer.class))
                .build());
        handler = handler.start();

        handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "name", Mockito.mock(Object.class));
        handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "typeId1", Mockito.mock(Object.class));
        handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "age", Mockito.mock(Object.class));

        handler.complete(parser, Mockito.mock(DeserializationContext.class), buffer, creator);

        Mockito.verify(creator).build(Mockito.any(DeserializationContext.class), Mockito.eq(buffer));
    }

    @Test(expected = IOException.class)
    public void testComplete_MissingProperty() throws IOException {
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(Mockito.mock(SettableBeanProperty.class), Mockito.mock(TypeDeserializer.class))
                .addExternal(Mockito.mock(SettableBeanProperty.class), Mockito.mock(TypeDeserializer.class))
                .build());
        handler = handler.start();

        handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "name", Mockito.mock(Object.class));
        handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "typeId1", Mockito.mock(Object.class));

        handler.complete(parser, Mockito.mock(DeserializationContext.class), Mockito.mock(Object.class));

        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testComplete_MissingTypeId() throws IOException {
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(Mockito.mock(SettableBeanProperty.class), Mockito.mock(TypeDeserializer.class))
                .build());
        handler = handler.start();

        handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "name", Mockito.mock(Object.class));
        handler.handlePropertyValue(parser, Mockito.mock(DeserializationContext.class), "name", Mockito.mock(Object.class));

        handler.complete(parser, Mockito.mock(DeserializationContext.class), Mockito.mock(Object.class));

        fail("Expected IOException");
    }
}