package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Test class for {@link ExternalTypeHandler}
 */
@RunWith(MockitoJUnitRunner.class)
public class ExternalTypeHandlerTest {

    @InjectMocks
    private ExternalTypeHandler externalTypeHandler;

    @SuppressWarnings("unchecked")
    private JsonParser createJsonParser(String json) throws IOException {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        return mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
    }

    @SuppressWarnings("resource")
    private Object deserialize(String json, Class<?> clazz) throws IOException {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        return mapper.readValue(new ByteArrayInputStream(json.getBytes()), clazz);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        // Given
        String json = "{\"typeId\": \"StringTypeId\", \"property1\": \"value1\"}";
        JsonParser jsonParser = createJsonParser(json);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(String.class);

        // When
        externalTypeHandler.handleTypePropertyValue(jsonParser, ctxt, "property1", bean);

        // Then
        assertEquals("StringTypeId", externalTypeHandler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        // Given
        String json = "{\"property1\": \"value1\", \"typeId\": \"StringTypeId\"}";
        JsonParser jsonParser = createJsonParser(json);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(String.class);

        // When
        externalTypeHandler.handlePropertyValue(jsonParser, ctxt, "property1", bean);

        // Then
        assertEquals("StringTypeId", externalTypeHandler._typeIds[0]);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueWithMissingTypeId() throws IOException {
        // Given
        String json = "{\"property1\": \"value1\"}";
        JsonParser jsonParser = createJsonParser(json);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(String.class);

        // When
        externalTypeHandler.handlePropertyValue(jsonParser, ctxt, "property1", bean);

        // Then
        fail("Expected an IOException, but no exception was thrown");
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueWithMissingProperty() throws IOException {
        // Given
        String json = "{\"typeId\": \"StringTypeId\"}";
        JsonParser jsonParser = createJsonParser(json);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(String.class);

        // When
        externalTypeHandler.handlePropertyValue(jsonParser, ctxt, "property1", bean);

        // Then
        fail("Expected an IOException, but no exception was thrown");
    }

    @Test
    public void testComplete() throws IOException {
        // Given
        String json = "{\"property1\": \"value1\", \"typeId\": \"StringTypeId\"}";
        JsonParser jsonParser = createJsonParser(json);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(String.class);

        // When
        externalTypeHandler.complete(jsonParser, ctxt, bean);

        // Then
        assertEquals("value1", bean.toString());
        assertEquals("StringTypeId", externalTypeHandler._typeIds[0]);
    }

    @Test
    public void testCompleteWithNaturalType() throws IOException {
        // Given
        String json = "{\"property1\": 123}";
        JsonParser jsonParser = createJsonParser(json);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(Integer.class);

        externalTypeHandler.complete(jsonParser, ctxt, bean);

        // Then
        assertEquals(123, bean.hashCode());
    }

    @Test(expected = IOException.class)
    public void testCompleteWithMissingProperty() throws IOException {
        // Given
        String json = "{\"typeId\": \"StringTypeId\"}";
        JsonParser jsonParser = createJsonParser(json);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(String.class);

        // When
        externalTypeHandler.complete(jsonParser, ctxt, bean);

        // Then
        fail("Expected an IOException, but no exception was thrown");
    }

    @Test
    public void testCompleteWithMissingTypeId() throws IOException {
        // Given
        String json = "{\"property1\": \"value1\"}";
        JsonParser jsonParser = createJsonParser(json);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object bean = Mockito.mock(String.class);

        // When
        externalTypeHandler.complete(jsonParser, ctxt, bean);

        // Then
        assertEquals("value1", bean.toString());
        assertNull(externalTypeHandler._typeIds[0]);
    }
}