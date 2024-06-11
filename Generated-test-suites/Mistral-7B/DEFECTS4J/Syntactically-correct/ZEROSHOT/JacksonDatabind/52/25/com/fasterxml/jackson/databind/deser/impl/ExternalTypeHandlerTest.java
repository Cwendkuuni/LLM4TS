package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Annotations;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link ExternalTypeHandler}
 */
public class ExternalTypeHandlerTest {

    @Mock
    private JsonParser mockJsonParser;

    @Mock
    private DeserializationContext mockDeserializationContext;

    @Mock
    private JsonToken mockJsonToken;

    @Mock
    private Object mockBean;

    @Mock
    private SettableBeanProperty mockSettableBeanProperty;

    @Mock
    private TypeDeserializer mockTypeDeserializer;

    private ExternalTypeHandler externalTypeHandler;
    private ExternalTypeHandler.Builder builder;

    @Before
    public void setUp() {
        Annotations.initMocks(this);
        builder = new ExternalTypeHandler.Builder();
    }

    @After
    public void tearDown() {
        externalTypeHandler = null;
    }

    /**
     * Test for {@link ExternalTypeHandler#ExternalTypeHandler(ExtTypedProperty[], HashMap, String[], TokenBuffer[])}
     */
    @Test
    public void testConstructor() throws JsonProcessingException {
        List<ExtTypedProperty> extTypedProperties = new ArrayList<>();
        extTypedProperties.add(new ExtTypedProperty(mockSettableBeanProperty, mockTypeDeserializer));
        String[] typeIds = new String[]{"typeId1"};
        TokenBuffer[] tokens = new TokenBuffer[1];

        externalTypeHandler = new ExternalTypeHandler(extTypedProperties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), typeIds, tokens);

        assertEquals(1, externalTypeHandler._properties.length);
        assertEquals(mockSettableBeanProperty, externalTypeHandler._properties[0].getProperty());
        assertEquals(mockTypeDeserializer, externalTypeHandler._properties[0].getTypeDeserializer());
        assertEquals("typeId1", externalTypeHandler._typeIds[0]);
        assertNull(externalTypeHandler._tokens[0]);
    }

    /**
     * Test for {@link ExternalTypeHandler#handleTypePropertyValue(JsonParser, DeserializationContext, String, Object)}
     */
    @Test
    public void testHandleTypePropertyValue() throws IOException, JsonProcessingException {
        when(mockJsonParser.getText()).thenReturn("typeId1");
        when(mockDeserializationContext.findBeanProperty(Mockito.anyString())).thenReturn(mockSettableBeanProperty);

        builder.addExternal(mockSettableBeanProperty, mockTypeDeserializer);
        externalTypeHandler = builder.build();

        externalTypeHandler.handleTypePropertyValue(mockJsonParser, mockDeserializationContext, "propertyName", mockBean);

        verify(mockJsonParser).getText();
        verify(mockSettableBeanProperty).getName();
        verify(mockDeserializationContext).findBeanProperty("typeId1");

        assertEquals("typeId1", externalTypeHandler._typeIds[0]);
        assertNull(externalTypeHandler._tokens[0]);
    }

    /**
     * Test for {@link ExternalTypeHandler#handlePropertyValue(JsonParser, DeserializationContext, String, Object)}
     */
    @Test
    public void testHandlePropertyValue() throws IOException, JsonProcessingException {
        when(mockJsonParser.getText()).thenReturn("propertyValue");
        when(mockDeserializationContext.findBeanProperty(Mockito.anyString())).thenReturn(mockSettableBeanProperty);

        builder.addExternal(mockSettableBeanProperty, mockTypeDeserializer);
        externalTypeHandler = builder.build();

        externalTypeHandler.handlePropertyValue(mockJsonParser, mockDeserializationContext, "propertyName", mockBean);

        verify(mockJsonParser).getText();
        verify(mockJsonParser).skipChildren();

        assertNull(externalTypeHandler._typeIds[0]);
        assertNull(externalTypeHandler._tokens[0]);
    }

    /**
     * Test for {@link ExternalTypeHandler#start()}
     */
    @Test
    public void testStart() {
        externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler[0], new HashMap<>(), null, null);
        externalTypeHandler = externalTypeHandler.start();

        assertEquals(externalTypeHandler.getClass(), ExternalTypeHandler.class);
    }

    /**
     * Test for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)}
     */
    @Test
    public void testComplete() throws IOException, JsonProcessingException {
        when(mockJsonParser.nextToken()).thenReturn(mockJsonToken);

        List<ExtTypedProperty> extTypedProperties = new ArrayList<>();
        extTypedProperties.add(new ExtTypedProperty(mockSettableBeanProperty, mockTypeDeserializer));
        externalTypeHandler = new ExternalTypeHandler(extTypedProperties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), null, null);
        externalTypeHandler._typeIds = new String[1];
        externalTypeHandler._tokens = new TokenBuffer[1];

        externalTypeHandler.handlePropertyValue(mockJsonParser, mockDeserializationContext, "propertyName", mockBean);

        externalTypeHandler.complete(mockJsonParser, mockDeserializationContext, mockBean);

        verify(mockJsonParser).nextToken();
        verifyNoMoreInteractions(mockJsonParser);
        verify(mockSettableBeanProperty).deserialize(mockJsonParser, mockDeserializationContext);
    }

    /**
     * Test for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, PropertyValueBuffer, PropertyBasedCreator)}
     */
    @Test
    public void testCompleteWithCreator() throws IOException, JsonProcessingException {
        when(mockJsonParser.nextToken()).thenReturn(mockJsonToken);

        List<ExtTypedProperty> extTypedProperties = new ArrayList<>();
        extTypedProperties.add(new ExtTypedProperty(mockSettableBeanProperty, mockTypeDeserializer));
        externalTypeHandler = new ExternalTypeHandler(extTypedProperties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), null, null);
        externalTypeHandler._typeIds = new String[1];
        externalTypeHandler._tokens = new TokenBuffer[1];

        PropertyBasedCreator mockPropertyBasedCreator = Mockito.mock(PropertyBasedCreator.class);
        PropertyValueBuffer mockPropertyValueBuffer = Mockito.mock(PropertyValueBuffer.class);

        externalTypeHandler.handlePropertyValue(mockJsonParser, mockDeserializationContext, "propertyName", mockBean);

        externalTypeHandler.complete(mockJsonParser, mockDeserializationContext, mockPropertyValueBuffer, mockPropertyBasedCreator);

        // Not possible to verify interactions with mocks created inside the method
    }
}