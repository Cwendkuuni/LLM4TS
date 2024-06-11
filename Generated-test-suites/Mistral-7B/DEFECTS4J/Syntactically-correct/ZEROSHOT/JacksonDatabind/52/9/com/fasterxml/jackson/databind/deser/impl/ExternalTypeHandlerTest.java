package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private ObjectMapper mapper;
    private DeserializationContext ctxt;
    private JsonParser parser;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        ctxt = mapper.getDeserializationContext();
        parser = Mockito.mock(JsonParser.class);
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(Mockito.mock(SettableBeanProperty.class), Mockito.mock(TypeDeserializer.class))
                .addExternal(Mockito.mock(SettableBeanProperty.class), Mockito.mock(TypeDeserializer.class))
                .build());
    }

    @Test
    public void testStart() {
        handler = handler.start();
        assertNotNull(handler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String propName = "prop1";
        handler.handleTypePropertyValue(parser, ctxt, propName, null);
        Map<String, Integer> nameToPropertyIndex = handler._nameToPropertyIndex;
        assertEquals(1, nameToPropertyIndex.size());
        assertEquals(0, nameToPropertyIndex.get(propName).intValue());
        String typeId = handler._typeIds[0];
        assertNotNull(typeId);

        handler.handleTypePropertyValue(parser, ctxt, propName, Mockito.mock(Object.class));
        assertNull(handler._tokens[0]);
        assertEquals(null, handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String propName = "prop1";
        handler.handlePropertyValue(parser, ctxt, propName, null);
        Map<String, Integer> nameToPropertyIndex = handler._nameToPropertyIndex;
        assertEquals(1, nameToPropertyIndex.size());
        assertEquals(0, nameToPropertyIndex.get(propName).intValue());
        assertNull(handler._typeIds[0]);

        handler.handlePropertyValue(parser, ctxt, propName, Mockito.mock(Object.class));
        TokenBuffer tokens = handler._tokens[0];
        assertNotNull(tokens);
        assertEquals(JsonParser.ValueType.START_ARRAY, tokens.currentToken().getValueType());

        handler.handlePropertyValue(parser, ctxt, propName, Mockito.mock(Object.class));
        String typeId = handler._typeIds[0];
        assertNotNull(typeId);

        handler.handlePropertyValue(parser, ctxt, propName, Mockito.mock(Object.class));
        assertNull(handler._tokens[0]);
        assertEquals(null, handler._typeIds[0]);
    }

    @Test
    public void testComplete() throws IOException {
        String propName1 = "prop1";
        String propName2 = "prop2";
        handler.handlePropertyValue(parser, ctxt, propName1, Mockito.mock(Object.class));
        handler.handlePropertyValue(parser, ctxt, propName2, Mockito.mock(Object.class));
        handler.handleTypePropertyValue(parser, ctxt, propName1, null);

        Object result = handler.complete(parser, ctxt, Mockito.mock(Object.class));
        assertNotNull(result);

        handler.handleTypePropertyValue(parser, ctxt, propName2, null);

        result = handler.complete(parser, ctxt, Mockito.mock(Object.class));
        assertNotNull(result);
    }

    @Test
    public void testCompleteWithPropertyValueBufferAndPropertyBasedCreator() throws IOException {
        String propName1 = "prop1";
        String propName2 = "prop2";

        PropertyBasedCreator creator = Mockito.mock(PropertyBasedCreator.class);
        PropertyValueBuffer buffer = Mockito.mock(PropertyValueBuffer.class);

        handler.handlePropertyValue(parser, ctxt, propName1, Mockito.mock(Object.class));
        handler.handlePropertyValue(parser, ctxt, propName2, Mockito.mock(Object.class));
        handler.handleTypePropertyValue(parser, ctxt, propName1, null);

        handler.complete(parser, ctxt, buffer, creator);

        Object result = creator.build(ctxt, buffer);
        assertNotNull(result);

        handler.handleTypePropertyValue(parser, ctxt, propName2, null);

        result = handler.complete(parser, ctxt, buffer, creator);
        assertNotNull(result);
    }
}