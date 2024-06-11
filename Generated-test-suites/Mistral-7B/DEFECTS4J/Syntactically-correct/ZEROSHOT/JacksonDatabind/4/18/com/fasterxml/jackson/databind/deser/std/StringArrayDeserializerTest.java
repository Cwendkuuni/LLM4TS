package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContentConverterDeserializer;
import com.fasterxml.jackson.databind.deser.JsonDeserializer.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private DeserializationContext context;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonParser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
    }

    @Test
    public void testDeserializeStandard() throws IOException {
        // Given
        String json = "[\"hello\", \"world\"]";

        when(jsonParser.getValueAsString()).thenReturn(json);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        String[] result = new StringArrayDeserializer().deserialize(jsonParser, context);

        // Then
        assertArrayEquals(new String[] {"hello", "world"}, result);
    }

    @Test
    public void testDeserializeStandardNull() throws IOException {
        // Given
        when(jsonParser.getValueAsString()).thenReturn(null);

        String[] result = new StringArrayDeserializer().deserialize(jsonParser, context);

        // Then
        assertNull(result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        // Given
        String json = "[null, null]";
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);

        when(jsonParser.getValueAsString()).thenReturn(json);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(context.findContextualValueDeserializer(String.class, null)).thenReturn(jsonDeserializer);

        StringArrayDeserializer serializer = new StringArrayDeserializer(jsonDeserializer);
        String[] result = serializer.deserialize(jsonParser, context);

        // Then
        assertArrayEquals(new String[] {null, null}, result);
    }

    @Test
    public void testDeserializeType() throws IOException {
        // Given
        String json = "[1, 2]";
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(jsonParser.getValueAsString()).thenReturn(json);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        String[] result = new StringArrayDeserializer().deserializeWithType(jsonParser, context, typeDeserializer);

        // Then
        assertArrayEquals(new String[] {"1", "2"}, result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        // Given
        String json = "hello";

        when(jsonParser.getValueAsString()).thenReturn(json);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        String[] result = new StringArrayDeserializer().handleNonArray(jsonParser, context);

        // Then
        assertArrayEquals(new String[] {json}, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArrayNonString() throws IOException {
        // Given
        String json = "1";

        when(jsonParser.getValueAsString()).thenReturn(json);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);

        new StringArrayDeserializer().handleNonArray(jsonParser, context);
    }

    @Test
    public void testCreateContextualNoDeserializer() throws JsonMappingException {
        // Given
        String propertyName = "propertyName";

        when(context.constructType(String[].class)).thenReturn(ClassUtil.constructType(String[].class));

        StringArrayDeserializer result = new StringArrayDeserializer().createContextual(context, null);

        // Then
        assertSame(StringArrayDeserializer.class, result.getClass());
    }

    @Test
    public void testCreateContextualWithDeserializer() throws JsonMappingException {
        // Given
        String propertyName = "propertyName";
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);

        when(context.constructType(String[].class)).thenReturn(ClassUtil.constructType(String[].class));
        when(context.findContextualValueDeserializer(String.class, propertyName)).thenReturn(jsonDeserializer);

        StringArrayDeserializer result = new StringArrayDeserializer().createContextual(context, null);

        // Then
        assertSame(StringArrayDeserializer.class, result.getClass());
        assertSame(jsonDeserializer, result._elementDeserializer);
    }

    @Test
    public void testCreateContextualWithContentConverterDeserializer() throws JsonMappingException {
        // Given
        String propertyName = "propertyName";
        ContentConverterDeserializer contentConverterDeserializer = mock(ContentConverterDeserializer.class);

        when(context.constructType(String[].class)).thenReturn(ClassUtil.constructType(String[].class));
        when(context.findContextualValueDeserializer(String.class, propertyName)).thenReturn(contentConverterDeserializer);

        StringArrayDeserializer result = new StringArrayDeserializer().createContextual(context, null);

        // Then
        assertSame(StringArrayDeserializer.class, result.getClass());
        assertNull(result._elementDeserializer);
    }
}