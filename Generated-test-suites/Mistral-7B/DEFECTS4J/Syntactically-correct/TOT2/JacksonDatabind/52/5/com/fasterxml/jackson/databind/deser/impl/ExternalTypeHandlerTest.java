package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private Object testBean;

    @Before
    public void setUp() throws IOException {
        ExtTypedProperty property1 = new ExtTypedProperty(new SettableBeanProperty("property1", String.class), new BasicTypeDeserializer());
        ExtTypedProperty property2 = new ExtTypedProperty(new SettableBeanProperty("property2", Integer.class), new NumberDeserializer());
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(property1, property1.getTypeDeserializer())
                .addExternal(property2, property2.getTypeDeserializer())
                .build());
        testBean = new Object() {
            public Map<String, Object> properties = new HashMap<>();
        };
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser mockParser = createMockParser("{\"property1\":\"StringTypeId\"}");
        DeserializationContext context = new TestContext();
        assertTrue(handler.handleTypePropertyValue(mockParser, context, "property1", testBean));
        assertEquals("StringTypeId", handler.getProperties()[0].getProperty().getValue(testBean));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser mockParser = createMockParser("{\"property1\":\"StringValue\"}");
        DeserializationContext context = new TestContext();
        assertTrue(handler.handlePropertyValue(mockParser, context, "property1", testBean));
        assertEquals("StringValue", handler.getProperties()[0].getProperty().getValue(testBean));
    }

    @Test
    public void testHandleTypePropertyValueMissingProperty() throws IOException {
        JsonParser mockParser = createMockParser("{\"missingProperty\":\"StringTypeId\"}");
        DeserializationContext context = new TestContext();
        assertFalse(handler.handleTypePropertyValue(mockParser, context, "missingProperty", testBean));
    }

    @Test
    public void testHandlePropertyValueMissingValue() throws IOException {
        JsonParser mockParser = createMockParser("{\"property1\":{}}");
        DeserializationContext context = new TestContext();
        assertFalse(handler.handlePropertyValue(mockParser, context, "property1", testBean));
    }

    @Test(expected = IOException.class)
    public void testHandleTypePropertyValueMissingTokens() throws IOException {
        JsonParser mockParser = createMockParser("{\"property1\":\"StringTypeId\"}");
        DeserializationContext context = new TestContext();
        handler.handlePropertyValue(mockParser, context, "property1", testBean);
        assertNull(handler.getTokens()[0]);
        handler.handlePropertyValue(mockParser, context, "property1", testBean);
        assertThrows(IOException.class, () -> handler.handleTypePropertyValue(mockParser, context, "property1", testBean));
    }

    @Test
    public void testHandlePropertyValueMissingTypeId() throws IOException {
        JsonParser mockParser = createMockParser("{\"property1\":\"StringValue\"}");
        DeserializationContext context = new TestContext();
        handler.handlePropertyValue(mockParser, context, "property1", testBean);
        assertNull(handler.getTypeIds()[0]);
        handler.handleTypePropertyValue(mockParser, context, "property1", testBean);
        assertEquals("StringValue", handler.getProperties()[0].getProperty().getValue(testBean));
    }

    @Test
    public void testHandlePropertyValueMissingTokensAndTypeId() throws IOException {
        JsonParser mockParser = createMockParser("{\"property1\":\"StringValue\"}");
        DeserializationContext context = new TestContext();
        handler.handlePropertyValue(mockParser, context, "property1", testBean);
        assertNull(handler.getTypeIds()[0]);
        assertNull(handler.getTokens()[0]);
        handler.handlePropertyValue(mockParser, context, "property1", testBean);
        handler.handleTypePropertyValue(mockParser, context, "property1", testBean);
        assertEquals("StringValue", handler.getProperties()[0].getProperty().getValue(testBean));
    }

    @Test
    public void testCompleteMissingProperties() throws IOException {
        JsonParser mockParser = createMockParser("{ \"property2\":\"IntegerTypeId\" }");
        DeserializationContext context = new TestContext();
        handler.handleTypePropertyValue(mockParser, context, "property2", testBean);
        Object result = handler.complete(mockParser, context, testBean);
        assertNotNull(result);
        assertEquals(2, handler.getProperties().length);
        assertNull(handler.getTypeIds()[1]);
        assertNull(handler.getTokens()[1]);
    }

    @Test(expected = IOException.class)
    public void testCompleteMissingTokens() throws IOException {
        JsonParser mockParser = createMockParser("{ \"property1\":\"StringValue\" }");
        DeserializationContext context = new TestContext();
        handler.handlePropertyValue(mockParser, context, "property1", testBean);
        Object result = handler.complete(mockParser, context, testBean);
        assertNotNull(result);
        assertEquals(2, handler.getProperties().length);
        handler.handlePropertyValue(mockParser, context, "property1", testBean);
        handler.handleTypePropertyValue(mockParser, context, "property1", testBean);
        assertThrows(IOException.class, () -> handler.complete(mockParser, context, testBean));
    }

    @Test
    public void testCompleteMissingTypeIds() throws IOException {
        JsonParser mockParser = createMockParser("{ \"property1\":\"StringValue\", \"property2\":\"\" }");
        DeserializationContext context = new TestContext();
        handler.handlePropertyValue(mockParser, context, "property1", testBean);
        handler.handlePropertyValue(mockParser, context, "property2", testBean);
        Object result = handler.complete(mockParser, context, testBean);
        assertNotNull(result);
        assertEquals(3, handler.getProperties().length);
        assertNull(handler.getTypeIds()[2]);
        assertNull(handler.getTokens()[2]);
    }

    @Test(expected = IOException.class)
    public void testCompleteMissingTokensAndTypeId() throws IOException {
        JsonParser mockParser = createMockParser("{ \"property1\":\"StringValue\", \"property2\":\"\" }");
        DeserializationContext context = new TestContext();
        handler.handlePropertyValue(mockParser, context, "property1", testBean);
        handler.handlePropertyValue(mockParser, context, "property2", testBean);
        Object result = handler.complete(mockParser, context, testBean);
        assertNotNull(result);
        assertEquals(3, handler.getProperties().length);
        handler.handlePropertyValue(mockParser, context, "property2", testBean);
        handler.handleTypePropertyValue(mockParser, context, "property2", testBean);
        assertThrows(IOException.class, () -> handler.complete(mockParser, context, testBean));
    }

    @Test
    public void testCompleteWithCreatorProperties() throws IOException {
        JsonParser mockParser = createMockParser("{ \"property1\":{ \"property1Creator\":\"IntegerTypeId\" }, \"property2\":\"StringValue\" }");
        DeserializationContext context = new TestContext();
        handler.handleTypePropertyValue(mockParser, context, "property1", testBean);
        handler.handlePropertyValue(mockParser, context, "property2", testBean);
        Object result = handler.complete(mockParser, context, testBean);
        assertNotNull(result);
        assertEquals(3, handler.getProperties().length);
        assertEquals("IntegerTypeId", handler.getTypeIds()[1]);
        assertNotNull(handler.getTokens()[1]);
        assertEquals("StringValue", handler.getProperties()[2].getProperty().getValue(testBean));
    }

    private JsonParser createMockParser(String json) throws IOException {
        ObjectReader reader = new ObjectMapper().readerForUpdating(testBean.getClass());
        ObjectReader r = reader.readValue(new StringReader(json));
        return r.getParser();
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectReader _rootValueHandler() {
            return null;
        }

        @Override
        public ObjectReader _valueHandler(Class<?> type) throws JsonMappingException {
            return null;
        }

        @Override
        public ObjectReader _valueHandler(Class<?> type, JsonDeserializer<?> deserializer) throws JsonMappingException {
            return null;
        }

        @Override
        public ObjectReader _valueHandler(TypeReference type) throws JsonMappingException {
            return null;
        }
    }

    private ExternalTypeHandler.Property[] getProperties() { return handler._properties; }

    private String[] getTypeIds() { return handler._typeIds; }

    private TokenBuffer[] getTokens() { return handler._tokens; }
}