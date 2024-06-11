package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Unit tests for {@link StringArrayDeserializer} class.
 */
@JacksonStdImpl
public class StringArrayDeserializerTest extends TestBase {

    private StringArrayDeserializer deserializer;
    private ObjectBuffer objectBuffer;

    @Before
    public void setup() {
        deserializer = StringArrayDeserializer.instance;
        objectBuffer = new ObjectBuffer();
    }

    @Test
    public void testDefaultDeserialization() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("test1");
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("test2");
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{"test1", "test2"}, result);
    }

    @Test
    public void testCustomDeserialization() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);

        when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("test3");
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);

        when(ctxt.findContextualValueDeserializer(String[].class, null)).thenReturn(deserializer);
        when(ctxt.handleSecondaryContextualization(eq(deserializer), any())).thenReturn(new StringArrayDeserializer(elementDeserializer));

        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{"test3"}, result);
    }

    @Test
    public void testEmptyArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[0], result);
    }

    @Test(expected = IOException.class)
    public void testNonArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("test");

        deserializer.deserialize(jp, ctxt);
    }

    @Test(expected = IOException.class)
    public void testNullElementDeserializer() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("test1");
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("test2");
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);

        deserializer._elementDeserializer = null;
        deserializer.deserialize(jp, ctxt);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("test");

        String[] result = deserializer.handleNonArray(jp, ctxt);
        assertArrayEquals(new String[]{"test"}, result);
    }
}