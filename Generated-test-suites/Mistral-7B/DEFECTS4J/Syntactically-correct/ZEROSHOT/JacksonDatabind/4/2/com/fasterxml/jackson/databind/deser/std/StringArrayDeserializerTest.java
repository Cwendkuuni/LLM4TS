package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertArrayEquals;

public class StringArrayDeserializerTest {

    @Test
    public void testEmptyArrayDeserialization() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);

        when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(mockParser, mockContext);

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testStringArrayDeserializationWithDefaultDeserializer() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);

        when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("testString1");
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("testString2");
        when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(mockParser, mockContext);

        assertArrayEquals(new String[]{"testString1", "testString2"}, result);
    }

    @Test
    public void testCustomElementDeserializer() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(mock(JsonDeserializer.class));
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);

        when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("customString1");
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("customString2");
        when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        JsonDeserializer<String> mockElementDeserializer = mock(JsonDeserializer.class);
        when(mockElementDeserializer.deserialize(mockParser, mockContext)).thenReturn("customDeserializedString");

        String[] result = deserializer.deserialize(mockParser, mockContext);

        assertArrayEquals(new String[]{"customDeserializedString", "customDeserializedString"}, result);
    }

    @Test(expected = IOException.class)
    public void testNonArrayDeserialization() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);

        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER);

        deserializer.deserialize(mockParser, mockContext);

        fail("Expected IOException: Cannot deserialize value of type [Ljava.lang.String; from JSON String");
    }

    @Test(expected = IOException.class)
    public void testNullDeserialization() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);

        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        deserializer.deserialize(mockParser, mockContext);

        fail("Expected IOException: Cannot deserialize value of type [Ljava.lang.String; from JSON Null");
    }
}