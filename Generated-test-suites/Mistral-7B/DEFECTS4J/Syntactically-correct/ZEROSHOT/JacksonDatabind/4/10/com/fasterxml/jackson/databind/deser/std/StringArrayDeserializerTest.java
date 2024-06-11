package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StandardDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link StringArrayDeserializer}
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

    private ObjectBuffer buffer;
    private DeserializationContext ctxt;
    private JsonDeserializer<String> elementDeserializer;
    private StringArrayDeserializer stringArrayDeserializer;
    private JsonParser jsonParser;
    private TypeDeserializer typeDeserializer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        buffer = ObjectBuffer.newInstance();
        ctxt = mock(DeserializationContext.class);
        jsonParser = mock(JsonParser.class);
        typeDeserializer = mock(TypeDeserializer.class);
        stringArrayDeserializer = new StringArrayDeserializer();
        elementDeserializer = mock(JsonDeserializer.class);
    }

    @Test
    public void testDefaultDeserialization() throws IOException {
        // Given
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        // When
        String[] result = stringArrayDeserializer.deserialize(jsonParser, ctxt);

        // Then
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializationWithNullElementDeserializer() throws IOException {
        // Given
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        // When
        String[] result = stringArrayDeserializer.deserialize(jsonParser, ctxt);

        // Then
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializationWithElementDeserializer() throws IOException {
        // Given
        String element = "element";
        when(elementDeserializer.deserialize(jsonParser, ctxt)).thenReturn(element);
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("element1");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("element2");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        // When
        StringArrayDeserializer deserializerWithElementDeserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = deserializerWithElementDeserializer.deserialize(jsonParser, ctxt);

        // Then
        assertArrayEquals(new String[]{"element1", "element2"}, result);
    }

    @Test
    public void testDeserializationWithCustomTypeDeserializer() throws IOException {
        // Given
        String[] expected = {"expectedElement1", "expectedElement2"};
        Type elementType = ClassUtil.typeFromValueAndContentType(String.class, String[].class);
        TypeDeserializer typeDeserializerMock = mock(TypeDeserializer.class);
        when(typeDeserializerMock.deserializeTypedFromArray(jsonParser, ctxt)).thenReturn(expected);
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        // When
        String[] result = stringArrayDeserializer.deserializeWithType(jsonParser, ctxt, typeDeserializerMock);

        // Then
        assertArrayEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidToken() throws IOException {
        // Given
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("invalidToken");

        // When & Then
        stringArrayDeserializer.deserialize(jsonParser, ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArrayWithInvalidToken() throws IOException {
        // Given
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("invalidToken");

        // When & Then
        stringArrayDeserializer.handleNonArray(jsonParser, ctxt);
    }

    @Test
    public void testCreateContextual() throws IOException {
        // Given
        DeserializationContext contextualCtxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        when(contextualCtxt.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);
        when(contextualCtxt.findContextualValueDeserializer(String[].class, property)).thenReturn(elementDeserializer);

        // When
        JsonDeserializer<String[]> result = stringArrayDeserializer.createContextual(contextualCtxt, property);

        // Then
        assertEquals(StringArrayDeserializer.class, result.getClass());
        StringArrayDeserializer stringArrayDeserializerResult = (StringArrayDeserializer) result;
        assertEquals(elementDeserializer, stringArrayDeserializerResult._elementDeserializer);
    }

    @Test
    public void testCreateContextualWithNullElementDeserializer() throws IOException {
        // Given
        DeserializationContext contextualCtxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);

        // When
        JsonDeserializer<String[]> result = stringArrayDeserializer.createContextual(contextualCtxt, property);

        // Then
        assertNull(result);
    }

    @Test
    public void testCreateContextualWithEmptyDeserializer() throws IOException {
        // Given
        DeserializationContext contextualCtxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        when(contextualCtxt.findContextualValueDeserializer(String.class, property)).thenReturn(null);

        // When
        JsonDeserializer<String[]> result = stringArrayDeserializer.createContextual(contextualCtxt, property);

        // Then
        assertNull(result);
    }
}