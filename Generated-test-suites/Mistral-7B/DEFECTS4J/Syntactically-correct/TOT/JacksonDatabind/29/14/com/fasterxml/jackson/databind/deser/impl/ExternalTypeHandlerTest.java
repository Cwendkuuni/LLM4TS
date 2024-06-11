package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest {

    private ObjectMapper mapper;
    private DeserializationContext ctxt;
    private TypeDeserializer typeDeserializerMock;
    private SettableBeanProperty settableBeanPropertyMock;
    private ExternalTypeHandler handler;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        ctxt = mock(DeserializationContext.class);
        typeDeserializerMock = mock(TypeDeserializer.class);
        settableBeanPropertyMock = mock(SettableBeanProperty.class);

        handler = new ExternalTypeHandler(
                new ExternalTypeHandler.Builder()
                        .addExternal(settableBeanPropertyMock, typeDeserializerMock)
                        .build()
        );
    }

    @Test
    public void testHandleTypePropertyValue_handlesExternalTypeId_returnsTrue() throws Exception {
        String propertyName = "externalProperty";
        String typeId = "IntType";

        when(settableBeanPropertyMock.getName()).thenReturn(new PropertyName(propertyName));
        when(settableBeanPropertyMock.getType()).thenReturn(Integer.class);
        when(typeDeserializerMock.getPropertyName()).thenReturn(new PropertyName("typeId"));
        when(typeDeserializerMock.getTypeIdResolver()).thenReturn(mock(ObjectIdReader.class));

        handler.handleTypePropertyValue(mapper.getFactory().createParser("{\"externalProperty\": \"IntType\"}"), ctxt, propertyName, null);

        verify(ctxt).handleUnequivocalTypeId(eq(Integer.class), eq(typeId));
    }

    @Test
    public void testHandleTypePropertyValue_doesNotHandleProperty_returnsFalse() throws Exception {
        String propertyName = "nonExternalProperty";

        when(settableBeanPropertyMock.getName()).thenReturn(new PropertyName(propertyName));
        when(settableBeanPropertyMock.getType()).thenReturn(Integer.class);

        handler.handleTypePropertyValue(mapper.getFactory().createParser("{\"nonExternalProperty\": 42}"), ctxt, propertyName, null);

        verifyZeroInteractions(ctxt);
    }

    @Test
    public void testHandlePropertyValue_handlesValue_returnsTrue() throws Exception {
        String propertyName = "internalProperty";
        String jsonValue = "42";

        when(settableBeanPropertyMock.getName()).thenReturn(new PropertyName(propertyName));
        when(settableBeanPropertyMock.getType()).thenReturn(Integer.class);

        handler.handlePropertyValue(mapper.getFactory().createParser("{\"internalProperty\": " + jsonValue + "}"), ctxt, propertyName, null);

        verifyZeroInteractions(ctxt);
    }

    @Test
    public void testHandlePropertyValue_handlesExternalTypeId_returnsTrue() throws Exception {
        String propertyName = "externalProperty";
        String typeId = "IntType";

        when(settableBeanPropertyMock.getName()).thenReturn(new PropertyName(propertyName));
        when(settableBeanPropertyMock.getType()).thenReturn(Integer.class);
        when(typeDeserializerMock.getPropertyName()).thenReturn(new PropertyName("typeId"));
        when(typeDeserializerMock.getTypeIdResolver()).thenReturn(mock(ObjectIdReader.class));

        handler.handlePropertyValue(mapper.getFactory().createParser("{\"externalProperty\": " + jsonValue + ", \"typeId\": \"" + typeId + "\"}"), ctxt, propertyName, null);

        verify(ctxt).handleUnequivocalTypeId(eq(Integer.class), eq(typeId));
    }

    @Test
    public void testHandlePropertyValue_doesNotHandleProperty_returnsFalse() throws Exception {
        String propertyName = "nonExternalProperty";

        when(settableBeanPropertyMock.getName()).thenReturn(new PropertyName(propertyName));
        when(settableBeanPropertyMock.getType()).thenReturn(Integer.class);

        handler.handlePropertyValue(mapper.getFactory().createParser("{\"nonExternalProperty\": 42, \"externalTypeId\": \"IntType\"}"), ctxt, propertyName, null);

        verifyZeroInteractions(ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandlePropertyValue_missingProperty_throwsJsonMappingException() throws Exception {
        String propertyName = "missingProperty";

        when(settableBeanPropertyMock.getName()).thenReturn(new PropertyName(propertyName));
        when(settableBeanPropertyMock.getType()).thenReturn(Integer.class);

        handler.handlePropertyValue(mapper.getFactory().createParser("{\"externalTypeId\": \"IntType\"}"), ctxt, propertyName, null);

        fail("Expected JsonMappingException but was not thrown");
    }

    @Test(expected = JsonMappingException.class)
    public void testHandlePropertyValue_missingTypeIdProperty_throwsJsonMappingException() throws Exception {
        String propertyName = "externalProperty";
        String jsonValue = "{\"externalProperty\": 42}";

        when(settableBeanPropertyMock.getName()).thenReturn(new PropertyName(propertyName));
        when(settableBeanPropertyMock.getType()).thenReturn(Integer.class);
        when(typeDeserializerMock.getTypeIdResolver()).thenThrow(new JsonMappingException("Missing required type id property 'typeId'"));

        handler.handlePropertyValue(mapper.getFactory().createParser(jsonValue), ctxt, propertyName, null);

        fail("Expected JsonMappingException but was not thrown");
    }

    @Test(expected = JsonMappingException.class)
    public void testHandlePropertyValue_missingTypeId_throwsJsonMappingException() throws Exception {
        String propertyName = "externalProperty";
        String jsonValue = "{\"externalProperty\": \"IntType\"}";

        when(settableBeanPropertyMock.getName()).thenReturn(new PropertyName(propertyName));
        when(settableBeanPropertyMock.getType()).thenReturn(Integer.class);
        when(typeDeserializerMock.getTypeIdResolver()).thenReturn(mock(ObjectIdReader.class));
        when(typeDeserializerMock.deserializeAndSet(any(JsonParser.class), eq(ctxt), any(Object.class), anyInt(), anyString()))
                .thenThrow(new JsonMappingException("Missing type id"));

        handler.handlePropertyValue(mapper.getFactory().createParser(jsonValue), ctxt, propertyName, null);

        fail("Expected JsonMappingException but was not thrown");
    }

    @Test
    public void testComplete_noMissingValues_returnsDeserializedBean() throws Exception {
        Object bean = new Object();
        String typeId = "IntType";

        when(typeDeserializerMock.getTypeIdResolver()).thenReturn(mock(ObjectIdReader.class));
        when(typeDeserializerMock.deserializeAndSet(any(JsonParser.class), eq(ctxt), any(Object.class), anyInt(), anyString()))
                .thenReturn(42);

        handler.complete(mapper.getFactory().createParser("{\"externalProperty\": \"IntType\"}"), ctxt, bean);

        assertEquals(42, bean.getClass().getField("externalProperty").get(bean));
    }

    @Test
    public void testComplete_missingTypeId_throwsJsonMappingException() throws Exception {
        Object bean = new Object();

        when(typeDeserializerMock.getTypeIdResolver()).thenReturn(mock(ObjectIdReader.class));
        when(typeDeserializerMock.deserializeAndSet(any(JsonParser.class), eq(ctxt), any(Object.class), anyInt(), anyString()))
                .thenThrow(new JsonMappingException("Missing type id"));

        try {
            handler.complete(mapper.getFactory().createParser("{\"externalProperty\": \"IntType\"}"), ctxt, bean);
            fail("Expected JsonMappingException but was not thrown");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testComplete_missingProperty_throwsJsonMappingException() throws Exception {
        Object bean = new Object();

        try {
            handler.complete(mapper.getFactory().createParser("{\"typeId\": \"IntType\"}"), ctxt, bean);
            fail("Expected JsonMappingException but was not thrown");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testComplete_completesDeserialization() throws Exception {
        Object bean = new Object();
        String typeId = "IntType";

        when(typeDeserializerMock.getTypeIdResolver()).thenReturn(mock(ObjectIdReader.class));
        when(typeDeserializerMock.deserializeAndSet(any(JsonParser.class), eq(ctxt), any(Object.class), anyInt(), anyString()))
                .thenReturn(42);

        handler.complete(mapper.getFactory().createParser("{\"externalProperty\": \"IntType\"}"), ctxt, bean);

        assertEquals(42, bean.getClass().getField("externalProperty").get(bean));
    }

    @Test
    public void testComplete_propertyExistsInBean_doesNotOverwrite() throws Exception {
        Object bean = new Object() {{
            externalProperty = 42;
        }};

        String typeId = "IntType";

        when(typeDeserializerMock.getTypeIdResolver()).thenReturn(mock(ObjectIdReader.class));
        when(typeDeserializerMock.deserializeAndSet(any(JsonParser.class), eq(ctxt), any(Object.class), anyInt(), anyString()))
                .thenReturn(43);

        handler.complete(mapper.getFactory().createParser("{\"externalProperty\": \"IntType\"}"), ctxt, bean);

        assertEquals(42, bean.getClass().getField("externalProperty").get(bean));
    }

    @Test
    public void testComplete_propertyDoesNotExistInBean_setsValue() throws Exception {
        Object bean = new Object();

        when(typeDeserializerMock.getTypeIdResolver()).thenReturn(mock(ObjectIdReader.class));
        when(typeDeserializerMock.deserializeAndSet(any(JsonParser.class), eq(ctxt), any(Object.class), anyInt(), anyString()))
                .thenReturn(42);

        handler.complete(mapper.getFactory().createParser("{\"externalProperty\": \"IntType\"}"), ctxt, bean);

        assertEquals(42, bean.getClass().getField("externalProperty").get(bean));
    }
}