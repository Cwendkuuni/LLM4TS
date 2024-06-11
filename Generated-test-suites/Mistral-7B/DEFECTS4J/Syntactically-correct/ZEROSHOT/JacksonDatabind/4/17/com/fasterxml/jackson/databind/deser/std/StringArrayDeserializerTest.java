package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TypeUtils;

/**
 * Test class for {@link StringArrayDeserializer}
 */
@JacksonStdImpl
public class StringArrayDeserializerTest extends TestBase {

    private ObjectBuffer buffer;
    private StringArrayDeserializer serializer;
    private DeserializationContext context;
    private JsonParser parser;

    @Before
    public void setUp() throws Exception {
        buffer = new ObjectBuffer(16);
        context = mock(DeserializationContext.class);
        serializer = new StringArrayDeserializer();
        parser = createParser("[]");
    }

    @Test
    public void testEmptyArray() throws IOException {
        String[] result = serializer.deserialize(parser, context);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testNullArray() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        String[] result = serializer.deserialize(parser, context);
        assertNull(result);
    }

    @Test
    public void testSingleElement() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("element");
        String[] result = serializer.deserialize(parser, context);
        assertArrayEquals(new String[] {"element"}, result);
    }

    @Test
    public void testMultipleElements() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        String[] expected = {"element1", "element2", null, "element3"};
        String[] elements = new String[4];
        buffer.resetAndStart();
        Object[] chunk = buffer.getNextChunk();

        for (int i = 0; i < expected.length; i++) {
            when(parser.nextToken()).thenReturn((i == expected.length - 1) ? JsonToken.END_ARRAY : JsonToken.VALUE_STRING);
            when(parser.getText()).thenReturn(expected[i]);

            String value = serializer.deserialize(parser, context);
            if (i < elements.length) {
                elements[i] = value;
            }
            if (i >= chunk.length) {
                chunk = buffer.appendCompletedChunk(chunk);
            }
        }

        String[] result = serializer.deserialize(parser, context);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testInvalidInput() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        serializer.deserialize(parser, context);
    }

    @Test
    public void testCustomDeserializer() throws IOException {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        when(deserializer.getClass()).thenReturn(StringDeserializer.class);
        when(deserializer.getNullValue()).thenReturn(null);

        serializer = new StringArrayDeserializer(deserializer);

        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        String[] result = serializer.deserialize(parser, context);
        assertArrayEquals(new String[] {deserializer.getNullValue()}, result);
    }

    @Test
    public void testContextualDeserializer() throws IOException {
        String[] expected = {"element1", "element2"};
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        when(deserializer.getClass()).thenReturn(StringDeserializer.class);

        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForArray(String[].class);

        JsonParser parser = createParser("[" + mapper.writerWithRootValue(expected).writeValueAsString() + "]");

        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn(expected[0]);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn(expected[1]);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = serializer.createContextual(context, null).deserialize(parser, context, typeDeserializer);
        assertArrayEquals(expected, result);
    }
}

class StringDeserializer {}