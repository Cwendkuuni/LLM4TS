package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @InjectMocks
    private StdDelegatingSerializer testee;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private TypeSerializer typeSerializer;

    @Mock
    private JsonSerializer<?> jsonSerializerMock;

    @Mock
    private Converter<Object, ?> converterMock;

    @Mock
    private JavaType javaTypeMock;

    private ArgumentCaptor<JsonSerializer<?>> jsonSerializerArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jsonSerializerArgumentCaptor = ArgumentCaptor.forClass(JsonSerializer.class);
    }

    @Test
    public void testSerializationWithDelegateType() throws IOException, JsonGenerationException {
        // Given
        Object inputValue = new Object();
        Object delegateValue = new Object();
        given(converterMock.convert(inputValue)).willReturn(delegateValue);
        given(jsonSerializerMock.serialize(delegateValue, jsonGenerator, serializerProvider)).willReturn(null);
        given(serializerProvider.findValueSerializer(javaTypeMock)).willReturn(jsonSerializerMock);

        // When
        testee.serialize(inputValue, jsonGenerator, serializerProvider);

        // Then
        verify(jsonSerializerMock).serialize(delegateValue, jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerializationWithContextualSerialization() throws IOException, JsonGenerationException {
        // Given
        Object inputValue = new Object();
        Object delegateValue = new Object();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        when(jsonSerializerMock.getContextualSerializer(serializerProvider, any(BeanProperty.class))).thenReturn(delegateSerializer);

        testee = testee.withDelegate(converterMock, javaTypeMock, jsonSerializerMock);

        // When
        testee.serialize(inputValue, jsonGenerator, serializerProvider);

        // Then
        verify(delegateSerializer).serialize(delegateValue, jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerializationWithDynamicDelegateType() throws IOException, JsonGenerationException {
        // Given
        Object inputValue = new Object();
        Object delegateValue = new Object();
        JavaType delegateType = mock(JavaType.class);
        given(serializerProvider.getTypeFactory().constructType(Object.class)).thenReturn(delegateType);
        given(jsonSerializerMock.serializeWithType(delegateValue, jsonGenerator, serializerProvider, typeSerializer)).willReturn(null);

        // When
        testee.serialize(inputValue, jsonGenerator, serializerProvider);

        // Then
        verify(jsonSerializerMock).serializeWithType(delegateValue, jsonGenerator, serializerProvider, typeSerializer);
    }

    @Test
    public void testGetDelegateSerializer() {
        // When
        JsonSerializer<?> result = testee.getDelegatee();

        // Then
        assertEquals(jsonSerializerMock, result);
    }
}