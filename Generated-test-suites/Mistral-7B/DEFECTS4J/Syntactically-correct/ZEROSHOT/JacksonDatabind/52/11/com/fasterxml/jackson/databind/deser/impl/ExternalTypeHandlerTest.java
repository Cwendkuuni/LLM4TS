package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Unit tests for {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonFactory JSON_FACTORY = MAPPER.getFactory();

    // A simple POJO with two external properties
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.With(value = ExternalTypeA.class, name = "A"),
        @JsonSubTypes.With(value = ExternalTypeB.class, name = "B")
    })
    static class ExternalTestClass {
        private String type;
        private String propA;
        private ExternalTypeB propB;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPropA() {
            return propA;
        }

        public void setPropA(String propA) {
            this.propA = propA;
        }

        public ExternalTypeB getPropB() {
            return propB;
        }

        public void setPropB(ExternalTypeB propB) {
            this.propB = propB;
        }
    }

    // External Type A
    @JsonTypeName("A")
    static class ExternalTypeA {
        private String propA;

        public String getPropA() {
            return propA;
        }

        public void setPropA(String propA) {
            this.propA = propA;
        }
    }

    // External Type B
    static class ExternalTypeB {
        private String propB;

        public String getPropB() {
            return propB;
        }

        public void setPropB(String propB) {
            this.propB = propB;
        }
    }

    // Test object for ExternalTypeHandler
    private ExternalTypeHandler handler;
    private ExternalTestClass testObj;

    @Before
    public void setUp() throws Exception {
        JsonParser p = JSON_FACTORY.createParser("{\"type\":\"A\", \"propA\":\"valueA\", \"propB\":{\"type\":\"B\", \"propB\":\"valueB\"}}");
        TypeDeserializer typeDeserializerA = Mockito.mock(TypeDeserializer.class);
        TypeDeserializer typeDeserializerB = Mockito.mock(TypeDeserializer.class);
        SettableBeanProperty propA = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty propB = Mockito.mock(SettableBeanProperty.class);
        handler = new ExternalTypeHandler.Builder()
            .addExternal(propA, typeDeserializerA)
            .addExternal(propB, typeDeserializerB)
            .build();
        testObj = new ExternalTestClass();
        handler.start();
        handler.handlePropertyValue(p, Mockito.mock(DeserializationContext.class), "type", testObj);
        handler.handlePropertyValue(p, Mockito.mock(DeserializationContext.class), "propA", testObj);
        handler.handlePropertyValue(p, Mockito.mock(DeserializationContext.class), "propB.type", testObj);
        handler.handlePropertyValue(p, Mockito.mock(DeserializationContext.class), "propB.propB", testObj);
        handler.complete(p, Mockito.mock(DeserializationContext.class), testObj);
        p.close();
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        JsonParser p = JSON_FACTORY.createParser("{\"type\":\"A\", \"typeA\":\"valueA\"}");
        handler.handleTypePropertyValue(p, Mockito.mock(DeserializationContext.class), "type", testObj);
        assertEquals("valueA", testObj.getType());
    }

    @Test(expected = IllegalStateException.class)
    public void testHandleTypePropertyValueWithMissingProperty() throws Exception {
        JsonParser p = JSON_FACTORY.createParser("{\"type\":\"A\"}");
        handler.handleTypePropertyValue(p, Mockito.mock(DeserializationContext.class), "type", testObj);
    }

    @Test(expected = IllegalStateException.class)
    public void testHandleTypePropertyValueWithMissingTypeId() throws Exception {
        JsonParser p = JSON_FACTORY.createParser("{\"type\":\"A\", \"propA\":\"valueA\"}");
        handler.handleTypePropertyValue(p, Mockito.mock(DeserializationContext.class), "typeA", testObj);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        JsonParser p = JSON_FACTORY.createParser("{\"type\":\"A\", \"propA\":\"valueA\", \"propB\":{\"type\":\"B\", \"propB\":\"valueB\"}}");
        handler.handlePropertyValue(p, Mockito.mock(DeserializationContext.class), "propA", testObj);
        assertEquals("valueA", testObj.getPropA());

        handler.handlePropertyValue(p, Mockito.mock(DeserializationContext.class), "propB.type", testObj);
        assertEquals("B", testObj.getType());

        handler.handlePropertyValue(p, Mockito.mock(DeserializationContext.class), "propB.propB", testObj);
        assertEquals("valueB", testObj.getPropB().getPropB());
    }

    @Test(expected = IllegalStateException.class)
    public void testHandlePropertyValueWithMissingProperty() throws Exception {
        JsonParser p = JSON_FACTORY.createParser("{\"type\":\"A\", \"propB.propB\":\"valueB\"}");
        handler.handlePropertyValue(p, Mockito.mock(DeserializationContext.class), "propB.propB", testObj);
    }

    @Test(expected = IllegalStateException.class)
    public void testHandlePropertyValueWithMissingTypeId() throws Exception {
        JsonParser p = JSON_FACTORY.createParser("{\"type\":\"A\", \"propA\":\"valueA\", \"propB\":{\"propB\":\"valueB\"}}");
        handler.handlePropertyValue(p, Mockito.mock(DeserializationContext.class), "propB.propB", testObj);
    }

    @Test(expected = IllegalStateException.class)
    public void testHandlePropertyValueWithMissingBoth() throws Exception {
        JsonParser p = JSON_FACTORY.createParser("{\"propB.propB\":\"valueB\"}");
        handler.handlePropertyValue(p, Mockito.mock(DeserializationContext.class), "propB.propB", testObj);
    }

    @Test
    public void testComplete() throws Exception {
        JsonParser p = JSON_FACTORY.createParser("{\"type\":\"A\", \"propA\":\"valueA\", \"propB\":{\"type\":\"B\", \"propB\":\"valueB\"}}");
        Object result = handler.complete(p, Mockito.mock(DeserializationContext.class), testObj);
        assertNotNull(result);
        assertEquals(ExternalTestClass.class, result.getClass());
        assertEquals("A", testObj.getType());
        assertEquals("valueA", testObj.getPropA());
        assertEquals("B", testObj.getPropB().getType());
        assertEquals("valueB", testObj.getPropB().getPropB());
    }

    @Test
    public void testCompleteWithPropertyBasedConstructor() throws Exception {
        JsonParser p = JSON_FACTORY.createParser("{\"type\":\"A\", \"propA\":\"valueA\", \"propB\":{\"type\":\"B\", \"propB\":\"valueB\"}}");
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = Mockito.mock(PropertyBasedCreator.class);
        Object result = handler.complete(p, Mockito.mock(DeserializationContext.class), buffer, creator);
        assertNotNull(result);
        assertEquals(ExternalTestClass.class, result.getClass());
        assertEquals("A", testObj.getType());
        assertEquals("valueA", testObj.getPropA());
        assertEquals("B", testObj.getPropB().getType());
        assertEquals("valueB", testObj.getPropB().getPropB());

        // Check that properties have been set correctly using PropertyValueBuffer
        SettableBeanProperty propA = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty propB = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty typePropA = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty typePropB = Mockito.mock(SettableBeanProperty.class);
        Mockito.when(testObj.getClass()).thenReturn(ExternalTestClass.class);
        Mockito.when(testObj.getProperty("propA")).thenReturn(propA);
        Mockito.when(testObj.getProperty("propB")).thenReturn(propB);
        Mockito.when(testObj.getTypeProperty()).thenReturn(typePropA);
        Mockito.when(testObj.getPropB().getTypeProperty()).thenReturn(typePropB);
        Mockito.when(typePropA.getName()).thenReturn("typeA");
        Mockito.when(typePropB.getName()).thenReturn("typeB");
        Mockito.when(propA.getName()).thenReturn("propA");
        Mockito.when(propB.getName()).thenReturn("propB");

        for (Map.Entry<String, Object> entry : buffer.getParameters().entrySet()) {
            String propertyName = entry.getKey();
            Object propertyValue = entry.getValue();
            if (propertyName.equals("type")) {
                assertEquals("A", propertyValue);
            } else if (propertyName.equals("propA")) {
                assertEquals("valueA", propertyValue);
                Mockito.verify(propA).setValue(testObj, Mockito.any());
            } else if (propertyName.equals("propB.type")) {
                assertEquals("B", propertyValue);
                Mockito.verify(typePropB).setValue(testObj.getPropB(), Mockito.any());
            } else if (propertyName.equals("propB.propB")) {
                assertEquals("valueB", propertyValue);
                Mockito.verify(propB).setValue(testObj.getPropB(), Mockito.any());
            }
        }
    }
}