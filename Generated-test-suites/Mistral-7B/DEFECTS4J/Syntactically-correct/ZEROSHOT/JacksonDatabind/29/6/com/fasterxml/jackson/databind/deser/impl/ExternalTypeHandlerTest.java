package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Test class for {@link ExternalTypeHandler}
 */
public class ExternalTypeHandlerTest {

    private JsonParser mockParser;
    private DeserializationContext mockContext;
    private ObjectMock mockBean;
    private ExternalTypeHandler testee;

    @Before
    public void setUp() throws Exception {
        mockParser = Mockito.mock(JsonParser.class);
        mockContext = Mockito.mock(DeserializationContext.class);
        mockBean = Mockito.mock(Object.class);

        // Setup a simple POJO with two properties
        Class<?> pojoClass = Mockito.mock(Class.class);
        SettableBeanProperty prop1 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty prop2 = Mockito.mock(SettableBeanProperty.class);
        ObjectMock prop1Bean = Mockito.mock(Object.class);
        ObjectMock prop2Bean = Mockito.mock(Object.class);
        Mockito.when(pojoClass.getDeclaredField(prop1.getName())).thenReturn(Mockito.mock(Field.class));
        Mockito.when(pojoClass.getDeclaredField(prop2.getName())).thenReturn(Mockito.mock(Field.class));
        Mockito.when(prop1.getType()).thenReturn(prop1Bean.getClass());
        Mockito.when(prop2.getType()).thenReturn(prop2Bean.getClass());
        Mockito.when(mockBean.getClass()).thenReturn(pojoClass);

        // Setup a simple TypeDeserializer with a property name "typeId"
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        Mockito.when(typeDeserializer.getPropertyName()).thenReturn("typeId");

        // Setup the ExternalTypeHandler
        ExtTypedProperty prop1Type = new ExtTypedProperty(prop1, typeDeserializer);
        ExtTypedProperty prop2Type = new ExtTypedProperty(prop2, null);
        ExtTypedProperty[] properties = { prop1Type, prop2Type };
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put(prop1.getName(), 0);
        nameToPropertyIndex.put(prop2.getName(), 1);
        nameToPropertyIndex.put("typeId", 0);
        testee = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String propName = "typeId";
        String typeId = "StringType";

        // Setup the parser to return the expected property name and type id
        Mockito.when(mockParser.getCurrentName()).thenReturn(propName);
        Mockito.when(mockParser.getText()).thenReturn(typeId);

        // Test the handler
        assertTrue(testee.handleTypePropertyValue(mockParser, mockContext, mockBean, 0));
        assertEquals("StringType", testee._typeIds[0]);
        assertNull(testee._tokens[0]);
    }

    @Test
    public void testHandlePropertyValue_WithTypeId() throws IOException {
        String propName = "prop1";
        String typeId = "StringType";

        // Setup the parser to return the expected property name and type id
        Mockito.when(mockParser.getCurrentName()).thenReturn(propName);
        Mockito.when(mockParser.getText()).thenReturn(typeId);

        // Test the handler
        assertTrue(testee.handlePropertyValue(mockParser, mockContext, propName, mockBean));
        assertNull(testee._typeIds[0]);
        assertNotNull(testee._tokens[0]);
    }

    @Test
    public void testHandlePropertyValue_WithoutTypeId() throws IOException {
        String propName = "prop1";

        // Setup the parser to return the expected property name
        Mockito.when(mockParser.getCurrentName()).thenReturn(propName);

        // Test the handler
        assertTrue(testee.handlePropertyValue(mockParser, mockContext, propName, mockBean));
        assertNull(testee._typeIds[0]);
        assertNotNull(testee._tokens[0]);
    }

    @Test
    public void testComplete() throws IOException {
        // Setup the parser to return the expected type ids
        Mockito.when(mockParser.getText()).thenReturn("StringType");
        Mockito.when(mockParser.getText()).thenReturn("NumberType");

        // Test the handler
        Object result = testee.complete(mockParser, mockContext, mockBean);
        assertEquals(mockBean, result);
        assertEquals("StringType", testee._typeIds[0]);
        assertEquals("NumberType", testee._typeIds[1]);
        assertNull(testee._tokens[0]);
        assertNull(testee._tokens[1]);
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        // Setup the parser to return the expected type ids
        Mockito.when(mockParser.getText()).thenReturn("StringType");
        Mockito.when(mockParser.getText()).thenReturn("NumberType");

        // Setup a PropertyBasedCreator
        PropertyBasedCreator creator = Mockito.mock(PropertyBasedCreator.class);
        Mockito.when(creator.build(mockContext, Mockito.mock(PropertyValueBuffer.class))).thenReturn(mockBean);

        // Test the handler
        Object result = testee.complete(mockParser, mockContext, Mockito.mock(PropertyValueBuffer.class), creator);
        assertEquals(mockBean, result);
        assertEquals("StringType", testee._typeIds[0]);
        assertEquals("NumberType", testee._typeIds[1]);
        assertNull(testee._tokens[0]);
        assertNull(testee._tokens[1]);
    }
}

class ObjectMock {
    public Class<?> getClass() {
        return this.getClass();
    }
}

class ExtTypedProperty {
    // This class is already tested in the original code base, so we don't need to test it here.
}