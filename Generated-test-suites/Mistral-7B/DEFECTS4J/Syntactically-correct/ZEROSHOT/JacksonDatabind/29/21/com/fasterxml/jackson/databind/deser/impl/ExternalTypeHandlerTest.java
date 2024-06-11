package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TokenBuffer.Token;

/**
 * Test class for ExternalTypeHandler.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExternalTypeHandlerTest {

    @InjectMocks
    private ExternalTypeHandler handler;

    private JsonParser mockParser;
    private DeserializationContext mockContext;
    private Object mockBean;
    private List<ExtTypedProperty> mockProperties;
    private Map<String, Integer> mockNameToPropertyIndex;
    private String[] mockTypeIds;
    private TokenBuffer[] mockTokens;

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        // Prepare test data
        String propName = "testProp";
        String typeId = "TestType";
        SettableBeanProperty mockProperty = Mockito.mock(SettableBeanProperty.class);
        ExtTypedProperty mockExtProperty = new ExtTypedProperty(mockProperty, Mockito.mock(TypeDeserializer.class));
        Integer index = 0;

        Mockito.when(mockContext.getCurrentName()).thenReturn(propName);
        mockParser = Mockito.mock(JsonParser.class);
        Mockito.when(mockParser.getText()).thenReturn(typeId);
        mockBean = Mockito.mock(Object.class);
        mockProperties = new ArrayList<>();
        mockProperties.add(mockExtProperty);
        mockNameToPropertyIndex = new HashMap<>();
        mockNameToPropertyIndex.put(propName, index);
        handler = new ExternalTypeHandler(mockProperties.toArray(new ExtTypedProperty[0]),
                mockNameToPropertyIndex, mockTypeIds, mockTokens);

        // Test behavior
        assertTrue(handler.handleTypePropertyValue(mockParser, mockContext, propName, mockBean));

        // Verify side effects
        Mockito.verify(mockContext, Mockito.times(1)).handleExternalTypeId(typeId, mockBean, index);
        Mockito.verify(mockTokens, Mockito.times(1)).setCurrentPositionAfterCurrent();

        // Verify state
        assertEquals(typeId, handler._typeIds[index]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        // Prepare test data
        String propName = "testProp";
        SettableBeanProperty mockProperty = Mockito.mock(SettableBeanProperty.class);
        Object mockValue = Mockito.mock(Object.class);
        ExtTypedProperty mockExtProperty = new ExtTypedProperty(mockProperty, Mockito.mock(TypeDeserializer.class));
        Integer index = 0;

        Mockito.when(mockContext.getCurrentName()).thenReturn(propName);
        mockParser = Mockito.mock(JsonParser.class);
        JsonToken token = Mockito.mock(JsonToken.class);
        Mockito.when(mockParser.getCurrentToken()).thenReturn(token);
        Mockito.when(token.getValueAsString()).thenReturn("TestValue");
        mockBean = Mockito.mock(Object.class);
        mockProperties = new ArrayList<>();
        mockProperties.add(mockExtProperty);
        mockNameToPropertyIndex = new HashMap<>();
        mockNameToPropertyIndex.put(propName, index);
        handler = new ExternalTypeHandler(mockProperties.toArray(new ExtTypedProperty[0]),
                mockNameToPropertyIndex, mockTypeIds, mockTokens);

        // Test behavior
        assertTrue(handler.handlePropertyValue(mockParser, mockContext, propName, mockBean));

        // Verify side effects
        Mockito.verify(mockContext, Mockito.times(1)).handleValueSequence(mockParser, mockBean, index);
        Mockito.verify(mockTokens, Mockito.times(1)).copyCurrentStructure(mockParser);

        // Verify state
        assertEquals("TestValue", handler._tokens[index].firstToken().getValueAsString());
    }

    @Test
    public void testHandleMissingTypeProperty() throws IOException {
        // Prepare test data
        String propName = "testProp";
        SettableBeanProperty mockProperty = Mockito.mock(SettableBeanProperty.class);
        ExtTypedProperty mockExtProperty = new ExtTypedProperty(mockProperty, Mockito.mock(TypeDeserializer.class));
        Integer index = 0;

        Mockito.when(mockContext.getCurrentName()).thenReturn(propName);
        mockParser = Mockito.mock(JsonParser.class);
        mockBean = Mockito.mock(Object.class);
        mockProperties = new ArrayList<>();
        mockProperties.add(mockExtProperty);
        mockNameToPropertyIndex = new HashMap<>();
        mockNameToPropertyIndex.put(propName, index);
        handler = new ExternalTypeHandler(mockProperties.toArray(new ExtTypedProperty[0]),
                mockNameToPropertyIndex, mockTypeIds, mockTokens);

        // Test behavior
        assertFalse(handler.handleTypePropertyValue(mockParser, mockContext, propName, mockBean));
        assertNull(handler._typeIds[index]);
    }

    @Test
    public void testHandleMissingProperty() throws IOException {
        // Prepare test data
        String propName = "testProp";
        SettableBeanProperty mockProperty = Mockito.mock(SettableBeanProperty.class);
        ExtTypedProperty mockExtProperty = new ExtTypedProperty(mockProperty, Mockito.mock(TypeDeserializer.class));
        Integer index = 0;

        Mockito.when(mockContext.getCurrentName()).thenReturn(propName);
        mockParser = Mockito.mock(JsonParser.class);
        JsonToken token = Mockito.mock(JsonToken.class);
        Mockito.when(mockParser.getCurrentToken()).thenReturn(token);
        mockBean = Mockito.mock(Object.class);
        mockProperties = new ArrayList<>();
        mockProperties.add(mockExtProperty);
        mockNameToPropertyIndex = new HashMap<>();
        mockNameToPropertyIndex.put(propName, index);
        handler = new ExternalTypeHandler(mockProperties.toArray(new ExtTypedProperty[0]),
                mockNameToPropertyIndex, mockTypeIds, mockTokens);

        // Test behavior
        assertFalse(handler.handlePropertyValue(mockParser, mockContext, propName, mockBean));
        assertNull(handler._typeIds[index]);
        assertNull(handler._tokens[index]);
    }

    @Test
    public void testComplete() throws IOException {
        // Prepare test data
        JsonParser mockParser = Mockito.mock(JsonParser.class);
        DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
        Object mockBean = Mockito.mock(Object.class);
        List<ExtTypedProperty> mockProperties = new ArrayList<>();
        ExtTypedProperty mockExtProperty = new ExtTypedProperty(null, null);
        mockProperties.add(mockExtProperty);
        SettableBeanProperty mockProperty = Mockito.mock(SettableBeanProperty.class);
        mockExtProperty.setProperty(mockProperty);
        String typeId = "TestType";
        String propName = "testProp";
        Integer index = 0;

        // Test behavior
        handler = new ExternalTypeHandler(mockProperties.toArray(new ExtTypedProperty[0]),
                null, null, null);
        handler.start();
        handler.handleTypePropertyValue(mockParser, mockContext, propName, mockBean);
        handler.handlePropertyValue(mockParser, mockContext, propName, mockBean);
        Object result = handler.complete(mockParser, mockContext, mockBean);

        // Verify side effects
        Mockito.verify(mockBean, Mockito.times(1)).set(propName, Mockito.any());
        Mockito.verify(mockParser, Mockito.times(1)).nextToken();
        Mockito.verify(mockParser, Mockito.times(1)).nextToken();

        // Verify state
        assertEquals(typeId, mockProperties.get(index).getProperty().getType().getName());

        // Verify result
        assertEquals(mockBean, result);
    }

    @Test
    public void testCompleteWithNullTypeId() throws IOException {
        // Prepare test data
        JsonParser mockParser = Mockito.mock(JsonParser.class);
        DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
        Object mockBean = Mockito.mock(Object.class);
        List<ExtTypedProperty> mockProperties = new ArrayList<>();
        ExtTypedProperty mockExtProperty = new ExtTypedProperty(null, null);
        mockProperties.add(mockExtProperty);
        SettableBeanProperty mockProperty = Mockito.mock(SettableBeanProperty.class);
        mockExtProperty.setProperty(mockProperty);
        String propName = "testProp";
        Integer index = 0;

        // Test behavior
        handler = new ExternalTypeHandler(mockProperties.toArray(new ExtTypedProperty[0]),
                null, null, null);
        handler.start();
        handler.handleTypePropertyValue(mockParser, mockContext, propName, mockBean);
        handler.handlePropertyValue(mockParser, mockContext, propName, mockBean);
        handler.complete(mockParser, mockContext, mockBean);

        // Verify side effects
        Mockito.verify(mockParser, Mockito.times(2)).nextToken();

        // Verify state
        assertNull(mockProperties.get(index).getProperty().getType());
    }

    @Test(expected = JsonProcessingException.class)
    public void testCompleteWithMissingProperty() throws IOException {
        // Prepare test data
        JsonParser mockParser = Mockito.mock(JsonParser.class);
        DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
        Object mockBean = Mockito.mock(Object.class);
        List<ExtTypedProperty> mockProperties = new ArrayList<>();
        ExtTypedProperty mockExtProperty = new ExtTypedProperty(null, null);
        mockProperties.add(mockExtProperty);
        SettableBeanProperty mockProperty = Mockito.mock(SettableBeanProperty.class);
        mockExtProperty.setProperty(mockProperty);
        String propName = "testProp";
        Integer index = 0;

        // Test behavior
        handler = new ExternalTypeHandler(mockProperties.toArray(new ExtTypedProperty[0]),
                null, null, null);
        handler.start();
        handler.handleTypePropertyValue(mockParser, mockContext, propName, mockBean);

        // Verify side effects
        Mockito.verify(mockParser, Mockito.times(1)).nextToken();

        // Verify state
        assertNull(mockProperties.get(index).getProperty().getType());

        // Throws exception
        handler.complete(mockParser, mockContext, mockBean);
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        // Prepare test data
        JsonParser mockParser = Mockito.mock(JsonParser.class);
        DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
        Object mockBean = Mockito.mock(Object.class);
        List<ExtTypedProperty> mockProperties = new ArrayList<>();
        ExtTypedProperty mockExtProperty = new ExtTypedProperty(null, null);
        mockProperties.add(mockExtProperty);
        SettableBeanProperty mockProperty = Mockito.mock(SettableBeanProperty.class);
        mockExtProperty.setProperty(mockProperty);
        Object[] mockValues = new Object[1];
        mockValues[0] = "TestValue";
        PropertyValueBuffer mockBuffer = Mockito.mock(PropertyValueBuffer.class);
        PropertyBasedCreator mockCreator = Mockito.mock(PropertyBasedCreator.class);
        Mockito.when(mockCreator.build(mockContext, mockBuffer)).thenReturn(mockBean);
        mockProperties.get(0).setProperty(mockCreator);

        // Test behavior
        handler = new ExternalTypeHandler(mockProperties.toArray(new ExtTypedProperty[0]), null, null, null);
        handler.start();
        handler.handleTypePropertyValue(mockParser, mockContext, "testProp1", mockBean);
        handler.handlePropertyValue(mockParser, mockContext, "testProp2", mockBean);
        handler.complete(mockParser, mockContext, mockBuffer, mockCreator);

        // Verify side effects
        Mockito.verify(mockBean, Mockito.times(2)).set(mockProperties.get(0).getProperty().getName(), Mockito.any());

        // Verify state
        Mockito.verify(mockParser, Mockito.times(1)).nextToken();
        Mockito.verify(mockParser, Mockito.times(1)).nextToken();

        // Verify result
        assertEquals(mockBean, handler.complete(mockParser, mockContext, mockBean));
    }
}

class ExtTypedProperty {
    private SettableBeanProperty _property;
    private TypeDeserializer _typeDeserializer;
    private String _typePropertyName;

    public ExtTypedProperty(SettableBeanProperty property, TypeDeserializer typeDeserializer) {
        this._property = property;
        this._typeDeserializer = typeDeserializer;
        this._typePropertyName = typeDeserializer.getPropertyName();
    }

    public boolean hasTypePropertyName(String n) {
        return n.equals(_typePropertyName);
    }

    public boolean hasDefaultType() {
        return _typeDeserializer.getDefaultImpl() != null;
    }

    public String getDefaultTypeId() {
        Class<?> defaultType = _typeDeserializer.getDefaultImpl();
        if (defaultType == null) {
            return null;
        }
        return _typeDeserializer.getTypeIdResolver().idFromValueAndType(null, defaultType);
    }

    public String getTypePropertyName() {
        return _typePropertyName;
    }

    public SettableBeanProperty getProperty() {
        return _property;
    }
}