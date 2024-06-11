package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser mockParser = mockParser();
        DeserializationContext mockCtx = mockCtx();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(new Object(), "test"), new SimpleTypeDeserializer(String.class))}, new HashMap<>(), null, null);

        String propName = "test";
        Object bean = new Object();
        handler.handleTypePropertyValue(mockParser, mockCtx, propName, bean);
        assertEquals("test", _getTypeId(mockParser));
        assertNull(handler._tokens[0]);
        assertNull(handler._typeIds[0]);

        // Test when bean is null
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(null, "test"), new SimpleTypeDeserializer(String.class))}, new HashMap<>(), null, null);
        handler.handleTypePropertyValue(mockParser, mockCtx, propName, bean);
        assertNull(_getTypeId(mockParser));
        assertNull(handler._tokens[0]);
        assertNull(handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser mockParser = mockParser();
        DeserializationContext mockCtx = mockCtx();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(new Object(), "test"), new SimpleTypeDeserializer(String.class))}, new HashMap<>(), null, null);

        String propName = "test";
        Object bean = new Object();
        handler.handlePropertyValue(mockParser, mockCtx, propName, bean);
        assertEquals("test", _getTypeId(mockParser));
        assertEquals(new StringToken("test").asText(), handler._tokens[0].getText());
        assertNull(handler._typeIds[0]);

        // Test when bean is null
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(null, "test"), new SimpleTypeDeserializer(String.class))}, new HashMap<>(), null, null);
        handler.handlePropertyValue(mockParser, mockCtx, propName, bean);
        assertNull(_getTypeId(mockParser));
        assertNotNull(handler._tokens[0]);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser mockParser = mockParser();
        DeserializationContext mockCtx = mockCtx();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(bean, "test"), new SimpleTypeDeserializer(String.class))}, new HashMap<>(), null, null);
        handler.start();

        String typeId = "testType";
        String propName = "test";
        handler.handleTypePropertyValue(mockParser, mockCtx, propName, bean);
        handler.handlePropertyValue(mockParser, mockCtx, propName, bean);
        handler.complete(mockParser, mockCtx, bean);

        assertEquals(typeId, bean.getClass().getField("test").get(bean));
        assertNull(handler._tokens[0]);
        assertNull(handler._typeIds[0]);

        // Test with missing external type id
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(bean, "test"), new SimpleTypeDeserializer(String.class))}, new HashMap<>(), null, null);
        handler.start();
        handler.handlePropertyValue(mockParser, mockCtx, propName, bean);
        handler.complete(mockParser, mockCtx, bean);
        assertEquals(null, bean.getClass().getField("test").get(bean));
        assertTrue(handler._tokens[0] != null);

        // Test with missing property for external type id
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(bean, "missingProp"), new SimpleTypeDeserializer(String.class))}, new HashMap<>(), null, null);
        handler.start();
        handler.handleTypePropertyValue(mockParser, mockCtx, "missingProp", bean);
        handler.handlePropertyValue(mockParser, mockCtx, propName, bean);
        try {
            handler.complete(mockParser, mockCtx, bean);
            fail("Expected MappingException");
        } catch (MappingException e) {
            assertEquals("Missing property 'missingProp' for external type id 'missingProp'", e.getMessage());
        }
        assertNotNull(handler._tokens[0]);
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        JsonParser mockParser = mockParser();
        DeserializationContext mockCtx = mockCtx();
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(bean, "test"), new SimpleTypeDeserializer(String.class))}, new HashMap<>(), null, null);
        handler.start();

        String typeId = "testType";
        String propName = "test";
        handler.handleTypePropertyValue(mockParser, mockCtx, propName, bean);
        handler.handlePropertyValue(mockParser, mockCtx, propName, bean);
        handler.complete(mockParser, mockCtx, null, creator);

        assertEquals(typeId, creator.build(mockCtx, null).getClass().getField("test").get(creator.build(mockCtx, null)));
        assertNull(handler._tokens[0]);
        assertNull(handler._typeIds[0]);

        // Test with missing external type id
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(bean, "test"), new SimpleTypeDeserializer(String.class))}, new HashMap<>(), null, null);
        handler.start();
        handler.handlePropertyValue(mockParser, mockCtx, propName, bean);
        handler.complete(mockParser, mockCtx, null, creator);
        assertNull(creator.build(mockCtx, null).getClass().getField("test").get(creator.build(mockCtx, null)));
        assertTrue(handler._tokens[0] != null);

        // Test with missing property for external type id
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SettableBeanProperty(bean, "missingProp"), new SimpleTypeDeserializer(String.class))}, new HashMap<>(), null, null);
        handler.start();
        handler.handleTypePropertyValue(mockParser, mockCtx, "missingProp", bean);
        handler.handlePropertyValue(mockParser, mockCtx, propName, bean);
        try {
            handler.complete(mockParser, mockCtx, null, creator);
            fail("Expected MappingException");
        } catch (MappingException e) {
            assertEquals("Missing property 'missingProp' for external type id 'missingProp'", e.getMessage());
        }
        assertNotNull(handler._tokens[0]);
    }

    private JsonParser mockParser() {
        ObjectCodec mockObjectCodec = mock(ObjectCodec.class);
        JsonFactory mockJsonFactory = mock(JsonFactory.class);
        when(mockJsonFactory.createParser(any(CharSequence.class))).thenReturn(new JsonParser(mockObjectCodec, mockJsonFactory));
        return mockParser(mockJsonFactory);
    }

    private JsonParser mockParser(JsonFactory jsonFactory) {
        return new JsonParser(new ObjectMapper(), jsonFactory);
    }

    private DeserializationContext mockCtx() {
        ObjectCodec mockObjectCodec = mock(ObjectCodec.class);
        return new DeserializationContext(mockObjectCodec);
    }

    private String _getTypeId(JsonParser parser) {
        ArrayNode node = parser.readValueAsTree().path("typeId");
        return node.getText();
    }
}