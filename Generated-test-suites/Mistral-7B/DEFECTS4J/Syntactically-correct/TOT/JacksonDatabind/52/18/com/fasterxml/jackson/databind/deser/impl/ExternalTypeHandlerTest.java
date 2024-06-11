package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private static class DummyTypeDeserializer extends TypeDeserializer {
        private static final long serialVersionUID = 1L;

        DummyTypeDeserializer() {
            super(String.class, new TypeReference<String>() {});
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
                throws IOException {
            String typeId = p.getText();
            p.nextToken();
            return typeId;
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt) throws IOException {
            String typeId = p.getText();
            p.nextToken();
            return typeId;
        }

        @Override
        public String getPropertyName() {
            return "dummyTypeId";
        }

        @Override
        public Class<?> getValueInstantiator(DeserializationConfig config) throws JsonMappingException {
            return String.class;
        }
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
                new ExtTypedProperty(new SettableBeanProperty(DummyClass1.class, "prop1"), new DummyTypeDeserializer())},
                new HashMap<>(), new String[] { "dummyTypeId1" }, new TokenBuffer[0]);

        JsonParser p = mapper.getFactory().createParser("[{\"prop1\": \"type1\"}]");
        handler.start();
        assertTrue(handler.handleTypePropertyValue(p, mapper.getDeserializationContext(), "prop1", null));
        assertEquals("type1", handler.getProperties()[0].getProperty().getValue(handler.getBean()));
        p.close();

        handler.complete(p, mapper.getDeserializationContext(), null);
        assertEquals(DummyClass1.class, handler.getBean().getClass());
        p.close();
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testHandlePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
                new ExtTypedProperty(new SettableBeanProperty(DummyClass1.class, "prop1"), null)},
                new HashMap<>(), null, null);

        JsonParser p = mapper.getFactory().createParser("[{\"prop1\": \"value1\"}]");
        handler.start();
        assertTrue(handler.handlePropertyValue(p, mapper.getDeserializationContext(), "prop1", null));
        assertEquals("value1", handler.getProperties()[0].getProperty().getValue(handler.getBean()));
        p.close();

        handler.complete(p, mapper.getDeserializationContext(), null);
        assertEquals(DummyClass1.class, handler.getBean().getClass());
        p.close();
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testMissingTypeIdProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
                new ExtTypedProperty(new SettableBeanProperty(DummyClass1.class, "prop1"), new DummyTypeDeserializer())},
                new HashMap<>(), new String[] { "dummyTypeId1" }, new TokenBuffer[0]);

        JsonParser p = mapper.getFactory().createParser("[{\"prop1\": null}]");
        handler.start();
        handler.handlePropertyValue(p, mapper.getDeserializationContext(), "prop1", null);
        // should not have completed the object
        assertNull(handler.getBean());
        p.close();
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testMissingPropertyForTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
                new ExtTypedProperty(new SettableBeanProperty(DummyClass1.class, "prop1"), new DummyTypeDeserializer())},
                new HashMap<>(), new String[] { "dummyTypeId1" }, new TokenBuffer[0]);

        JsonParser p = mapper.getFactory().createParser("[{\"dummyTypeId1\": null}]");
        handler.start();
        handler.handleTypePropertyValue(p, mapper.getDeserializationContext(), "dummyTypeId1", null);
        // should not have completed the object
        assertNull(handler.getBean());
        p.close();
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testMissingPropertyAndTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
                new ExtTypedProperty(new SettableBeanProperty(DummyClass1.class, "prop1"), new DummyTypeDeserializer())},
                new HashMap<>(), new String[] { "dummyTypeId1" }, new TokenBuffer[0]);

        JsonParser p = mapper.getFactory().createParser("[{}]");
        handler.start();
        handler.handlePropertyValue(p, mapper.getDeserializationContext(), "prop1", null);
        handler.handlePropertyValue(p, mapper.getDeserializationContext(), "dummyTypeId1", null);
        // should not have completed the object
        assertNull(handler.getBean());
        p.close();
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testHandleExtraProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
                new ExtTypedProperty(new SettableBeanProperty(DummyClass1.class, "prop1"), new DummyTypeDeserializer()),
                new ExtTypedProperty(new SettableBeanProperty(DummyClass1.class, "prop2"), null)},
                new HashMap<>(), new String[] { "dummyTypeId1", "dummyTypeId2" }, new TokenBuffer[0]);

        JsonParser p = mapper.getFactory().createParser("[{\"prop1\": \"value1\", \"prop2\": \"value2\"}]");
        handler.start();
        assertTrue(handler.handlePropertyValue(p, mapper.getDeserializationContext(), "prop1", null));
        assertTrue(handler.handlePropertyValue(p, mapper.getDeserializationContext(), "prop2", null));
        assertEquals("value1", handler.getProperties()[0].getProperty().getValue(handler.getBean()));
        assertEquals("value2", handler.getProperties()[1].getProperty().getValue(handler.getBean()));
        p.close();

        handler.complete(p, mapper.getDeserializationContext(), null);
        assertEquals(DummyClass1.class, handler.getBean().getClass());
        p.close();
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testHandleTypeIdAsCreatorProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
                new ExtTypedProperty(new SettableBeanProperty(DummyClass1.class, "prop1"), new DummyTypeDeserializer()),
                new ExtTypedProperty(new SettableBeanProperty(DummyClass2.class, "dummyTypeId"), null)},
                new HashMap<>(), null, null);

        JsonParser p = mapper.getFactory().createParser("[{\"prop1\": \"value1\", \"dummyTypeId\": \"DummyClass2\"}]");
        handler.start();
        assertTrue(handler.handlePropertyValue(p, mapper.getDeserializationContext(), "prop1", null));
        assertTrue(handler.handleTypePropertyValue(p, mapper.getDeserializationContext(), "dummyTypeId", null));
        assertEquals("value1", handler.getProperties()[0].getProperty().getValue(handler.getBean()));
        assertEquals(DummyClass2.class, handler.getBean().getClass());
        p.close();

        handler.complete(p, mapper.getDeserializationContext(), null);
        assertEquals(DummyClass2.class, handler.getBean().getClass());
        p.close();
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testHandleTypeIdAsCreatorProperty2() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[] {
                new ExtTypedProperty(new SettableBeanProperty(DummyClass1.class, "prop1"), new DummyTypeDeserializer()),
                new ExtTypedProperty(new SettableBeanProperty(DummyClass2.class, "dummyTypeId"), null)},
                new HashMap<>(), null, null);

        JsonParser p = mapper.getFactory().createParser("[{\"prop1\": \"value1\", \"dummyTypeId\": {}}]");
        handler.start();
        assertTrue(handler.handlePropertyValue(p, mapper.getDeserializationContext(), "prop1", null));
        assertTrue(handler.handleTypePropertyValue(p, mapper.getDeserializationContext(), "dummyTypeId", null));
        assertEquals("value1", handler.getProperties()[0].getProperty().getValue(handler.getBean()));
        assertEquals(DummyClass2.class, handler.getBean().getClass());
        p.close();

        handler.complete(p, mapper.getDeserializationContext(), null);
        assertEquals(DummyClass2.class, handler.getBean().getClass());
        p.close();
    }

    static class DummyClass1 {
        private String prop1;

        public String getProp1() {
            return prop1;
        }

        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }
    }

    static class DummyClass2 implements Creator<DummyClass1> {
        @Override
        public DummyClass1 createUsingDefaults() {
            return new DummyClass1();
        }

        @Override
        public DummyClass1 createUsingGuid() {
            return new DummyClass1();
        }
    }
}