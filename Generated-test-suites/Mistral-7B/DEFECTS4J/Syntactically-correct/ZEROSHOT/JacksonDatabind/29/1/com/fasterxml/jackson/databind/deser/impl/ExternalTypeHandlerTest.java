package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * This class tests the functionality of ExternalTypeHandler class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExternalTypeHandlerTest {

    @Mock
    private JsonParser mockJsonParser;

    @Mock
    private DeserializationContext mockContext;

    @Mock
    private SettableBeanProperty mockSettableBeanProperty;

    @Mock
    private TypeDeserializer mockTypeDeserializer;

    private ExternalTypeHandler externalTypeHandler;
    private List<ExtTypedProperty> extTypedProperties;

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        when(mockJsonParser.getText()).thenReturn("typeId1");
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.isExpectedStartObjectToken()).thenReturn(false);

        SettableBeanProperty testProperty = mockSettableBeanProperty;
        TypeDeserializer typeDeserializer = mockTypeDeserializer;
        when(typeDeserializer.getPropertyName()).thenReturn("typePropertyName");

        ExtTypedProperty extTypedProperty = new ExtTypedProperty(testProperty, typeDeserializer);

        externalTypeHandler = new ExternalTypeHandler(
                new ExtTypedProperty[] { extTypedProperty },
                new HashMap<>(),
                new String[] {},
                new TokenBuffer[0]);

        // Calling handleTypePropertyValue method
        boolean result = externalTypeHandler.handleTypePropertyValue(mockJsonParser, mockContext,
                "propertyName", mockSettableBeanProperty);

        // Verify the result
        assertTrue(result);

        // Verify that the correct type id is assigned to the property
        verify(mockContext).handleUnknownProperty("propertyName");
        verify(mockContext, times(1)).handleNamedTypeId("typePropertyName", "typeId1");

        // Verify that the property is set with the correct value
        verify(testProperty).set(mockSettableBeanProperty, "typeId1");

        // Verify that the tokens are cleared
        verify(mockJsonParser, times(1)).skipChildren();
    }

    @Test
    public void testHandleTypePropertyValue_NullBean() throws IOException {
        when(mockJsonParser.getText()).thenReturn("typeId1");
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.isExpectedStartObjectToken()).thenReturn(false);

        SettableBeanProperty testProperty = mockSettableBeanProperty;
        TypeDeserializer typeDeserializer = mockTypeDeserializer;
        when(typeDeserializer.getPropertyName()).thenReturn("typePropertyName");

        ExtTypedProperty extTypedProperty = new ExtTypedProperty(testProperty, typeDeserializer);

        externalTypeHandler = new ExternalTypeHandler(
                new ExtTypedProperty[] { extTypedProperty },
                new HashMap<>(),
                new String[] {},
                new TokenBuffer[0]);

        // Calling handleTypePropertyValue method with null bean
        boolean result = externalTypeHandler.handleTypePropertyValue(mockJsonParser, mockContext,
                "propertyName", null);

        // Verify the result
        assertTrue(result);

        // No verification needed as no bean is set and no property is set
    }

    @Test
    public void testHandleTypePropertyValue_NullTypeId() throws IOException {
        when(mockJsonParser.getText()).thenReturn(null);
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.isExpectedStartObjectToken()).thenReturn(false);

        SettableBeanProperty testProperty = mockSettableBeanProperty;
        TypeDeserializer typeDeserializer = mockTypeDeserializer;
        when(typeDeserializer.getPropertyName()).thenReturn("typePropertyName");

        ExtTypedProperty extTypedProperty = new ExtTypedProperty(testProperty, typeDeserializer);

        externalTypeHandler = new ExternalTypeHandler(
                new ExtTypedProperty[] { extTypedProperty },
                new HashMap<>(),
                new String[] {},
                new TokenBuffer[0]);

        // Calling handleTypePropertyValue method with null type id
        boolean result = externalTypeHandler.handleTypePropertyValue(mockJsonParser, mockContext,
                "propertyName", mockSettableBeanProperty);

        // Verify the result
        assertTrue(result);

        // No verification needed as no type id is assigned and no property is set
    }

    @Test
    public void testHandleTypePropertyValue_NullTypeDeserializer() throws IOException {
        when(mockJsonParser.getText()).thenReturn("typeId1");
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.isExpectedStartObjectToken()).thenReturn(false);

        SettableBeanProperty testProperty = mockSettableBeanProperty;
        TypeDeserializer typeDeserializer = null;

        ExtTypedProperty extTypedProperty = new ExtTypedProperty(testProperty, typeDeserializer);

        externalTypeHandler = new ExternalTypeHandler(
                new ExtTypedProperty[] { extTypedProperty },
                new HashMap<>(),
                new String[] {},
                new TokenBuffer[0]);

        // Calling handleTypePropertyValue method with null TypeDeserializer
        boolean result = externalTypeHandler.handleTypePropertyValue(mockJsonParser, mockContext,
                "propertyName", mockSettableBeanProperty);

        // Verify the result
        assertTrue(result);

        // No verification needed as no type id is assigned and no property is set
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        when(mockJsonParser.getText()).thenReturn("value");
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.isExpectedStartObjectToken()).thenReturn(false);

        SettableBeanProperty testProperty = mockSettableBeanProperty;

        externalTypeHandler = new ExternalTypeHandler(
                new ExtTypedProperty[] {},
                new HashMap<>(),
                new String[0],
                new TokenBuffer[0]);

        // Calling handlePropertyValue method
        boolean result = externalTypeHandler.handlePropertyValue(mockJsonParser, mockContext,
                "propertyName", mockSettableBeanProperty);

        // Verify the result
        assertTrue(result);

        // Verify that the value is assigned to the property
        verify(testProperty).set(mockSettableBeanProperty, "value");
    }

    @Test
    public void testHandlePropertyValue_NullBean() throws IOException {
        when(mockJsonParser.getText()).thenReturn("value");
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.isExpectedStartObjectToken()).thenReturn(false);

        SettableBeanProperty testProperty = mockSettableBeanProperty;

        externalTypeHandler = new ExternalTypeHandler(
                new ExtTypedProperty[] {},
                new HashMap<>(),
                new String[0],
                new TokenBuffer[0]);

        // Calling handlePropertyValue method with null bean
        boolean result = externalTypeHandler.handlePropertyValue(mockJsonParser, mockContext,
                "propertyName", null);

        // Verify the result
        assertTrue(result);

        // No verification needed as no bean is set and no property is set
    }

    @Test
    public void testHandlePropertyValue_NullValue() throws IOException {
        when(mockJsonParser.getText()).thenReturn(null);
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.isExpectedStartObjectToken()).thenReturn(false);

        SettableBeanProperty testProperty = mockSettableBeanProperty;

        externalTypeHandler = new ExternalTypeHandler(
                new ExtTypedProperty[] {},
                new HashMap<>(),
                new String[0],
                new TokenBuffer[0]);

        // Calling handlePropertyValue method with null value
        boolean result = externalTypeHandler.handlePropertyValue(mockJsonParser, mockContext,
                "propertyName", mockSettableBeanProperty);

        // Verify the result
        assertTrue(result);

        // No verification needed as no value is assigned and no property is set
    }

    @Test
    public void testComplete() throws IOException {
        when(mockJsonParser.getText()).thenReturn("typeId1");
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.isExpectedStartObjectToken()).thenReturn(false);

        SettableBeanProperty testProperty = mockSettableBeanProperty;
        Object testBean = new Object();

        ExtTypedProperty extTypedProperty = new ExtTypedProperty(testProperty, mockTypeDeserializer);

        externalTypeHandler = new ExternalTypeHandler(
                new ExtTypedProperty[] { extTypedProperty },
                new HashMap<>(),
                new String[] { "typeId1" },
                new TokenBuffer[0]);

        // Calling complete method
        Object result = externalTypeHandler.complete(mockJsonParser, mockContext, testBean);

        // Verify the result
        assertEquals(testBean, result);

        // Verify that the type id is assigned to the property
        verify(testProperty).set(testBean, "typeId1");
    }

    @Test
    public void testComplete_NullBean() throws IOException {
        when(mockJsonParser.getText()).thenReturn("typeId1");
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.isExpectedStartObjectToken()).thenReturn(false);

        SettableBeanProperty testProperty = mockSettableBeanProperty;

        externalTypeHandler = new ExternalTypeHandler(
                new ExtTypedProperty[] {},
                new HashMap<>(),
                new String[] { "typeId1" },
                new TokenBuffer[0]);

        // Calling complete method with null bean
        Object result = externalTypeHandler.complete(mockJsonParser, mockContext, null);

        // Verify the result
        assertNull(result);

        // No verification needed as no bean is set and no property is set
    }

    @Test
    public void testComplete_MissingTypeId() throws IOException {
        when(mockJsonParser.getText()).thenReturn("typeId1");
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.isExpectedStartObjectToken()).thenReturn(false);

        SettableBeanProperty testProperty = mockSettableBeanProperty;
        Object testBean = new Object();

        extTypedProperties = new ArrayList<>();
        extTypedProperties.add(new ExtTypedProperty(testProperty, mockTypeDeserializer));

        externalTypeHandler = new ExternalTypeHandler(
                extTypedProperties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(),
                new String[] {},
                new TokenBuffer[0]);

        externalTypeHandler.setTokens(new TokenBuffer[1][]);

        doThrow(IOException.class)
                .when(mockContext)
                .mappingException(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object.class));

        // Calling complete method with missing type id
        try {
            externalTypeHandler.complete(mockJsonParser, mockContext, testBean);
        } catch (IOException e) {
            // Expected exception
        }

        // Verify that no exception is thrown for missing property
        verifyNoMoreInteractions(mockContext);

        // Verify that the property is not set
        verifyNoInteractions(testProperty);
    }

    @Test
    public void testComplete_MissingProperty() throws IOException {
        when(mockJsonParser.getText()).thenReturn("typeId1");
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.isExpectedStartObjectToken()).thenReturn(false);

        SettableBeanProperty testProperty = mockSettableBeanProperty;
        Object testBean = new Object();

        extTypedProperties = new ArrayList<>();
        extTypedProperties.add(new ExtTypedProperty(testProperty, mockTypeDeserializer));

        externalTypeHandler = new ExternalTypeHandler(
                extTypedProperties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(),
                new String[] {},
                new TokenBuffer[0]);

        externalTypeHandler.setTokens(new TokenBuffer[1][]);

        doThrow(IOException.class)
                .when(mockContext)
                .mappingException(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object.class));

        // Calling complete method with missing property
        try {
            externalTypeHandler.complete(mockJsonParser, mockContext, testBean);
        } catch (IOException e) {
            // Expected exception
        }

        // Verify that an exception is thrown for missing property
        verify(mockContext, times(1)).mappingException("Missing property 'typePropertyName' for external type id 'typeId1'",
                eq(testBean));

        // Verify that the property is not set
        verifyNoInteractions(testProperty);
    }

    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder testBuilder = new ExternalTypeHandler.Builder();

        SettableBeanProperty testProperty = mockSettableBeanProperty;
        TypeDeserializer typeDeserializer = mockTypeDeserializer;

        testBuilder.addExternal(testProperty, typeDeserializer);

        ExternalTypeHandler externalTypeHandler = testBuilder.build();

        assertNotNull(externalTypeHandler);
        assertEquals(1, externalTypeHandler._properties.length);
        assertEquals(testProperty, externalTypeHandler._properties[0].getProperty());
        assertEquals(typeDeserializer, externalTypeHandler._properties[0].getTypeDeserializer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilder_NullTypeDeserializer() {
        ExternalTypeHandler.Builder testBuilder = new ExternalTypeHandler.Builder();

        SettableBeanProperty testProperty = mockSettableBeanProperty;
        TypeDeserializer typeDeserializer = null;

        testBuilder.addExternal(testProperty, typeDeserializer);

        testBuilder.build();
    }

    private void setTokens(ExternalTypeHandler externalTypeHandler, TokenBuffer[] tokens) {
        for (int i = 0; i < externalTypeHandler._properties.length; i++) {
            externalTypeHandler._tokens[i] = tokens[i];
        }
    }
}