package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
import com.fasterxml.jackson.databind.util.StdSerializer;
import com.fasterxml.jackson.test.BaseTest;

/**
 * Test file for StringArrayDeserializer
 */
@JacksonStdImpl
public class StringArrayDeserializerTest extends BaseTest {

    private JsonParser parser;
    private DeserializationContext context;
    private ObjectBuffer objectBuffer;
    private StringArrayDeserializer deserializer;
    private JsonDeserializer<String> stringDeserializer;

    @Before
    public void setup() throws Exception {
        parser = setupParser();
        context = createContext();
        objectBuffer = context.leaseObjectBuffer();
        deserializer = StringArrayDeserializer.instance;
        stringDeserializer = mock(JsonDeserializer.class);
    }

    /**
     * Test deserialize method with empty array
     * @throws IOException
     */
    @Test
    public void testEmptyDeserialize() throws IOException {
        JsonToken startArray = parser.nextToken();
        parser.nextToken(); // skip ValueString "[]"
        assertEquals(startArray, parser.getCurrentToken());
        String[] result = deserializer.deserialize(parser, context);
        assertEquals(new String[0].length, result.length);
        context.returnObjectBuffer(objectBuffer);
    }

    /**
     * Test deserialize method with non-empty array
     * @throws IOException
     */
    @Test
    public void testDeserialize() throws IOException {
        String[] input = {"test1", "test2"};

        // Setup parser to mimic JsonParser.nextToken() behavior
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        JsonToken valueString;
        for (String value : input) {
            valueString = JsonToken.VALUE_STRING;
            when(parser.nextToken()).thenReturn(valueString);
            when(parser.getText()).thenReturn(value);
            String[] result = deserializer.deserialize(parser, context);
            assertEquals(input.length, result.length);
            for (int i = 0; i < input.length; i++) {
                assertEquals(input[i], result[i]);
            }
        }
        // End of array
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        deserializer.deserialize(parser, context);
        context.returnObjectBuffer(objectBuffer);
    }

    /**
     * Test deserialize method with null values
     * @throws IOException
     */
    @Test
    public void testDeserializeWithNullValues() throws IOException {
        JsonToken startArray = parser.nextToken();
        parser.nextToken(); // skip ValueString "[]"

        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        String[] result = deserializer.deserialize(parser, context);
        assertEquals(new String[0].length, result.length);
        context.returnObjectBuffer(objectBuffer);

        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        String[] nullArrayResult = deserializer.deserialize(parser, context);
        assertEquals(new String[1].length, nullArrayResult.length);
        context.returnObjectBuffer(objectBuffer);
    }

    /**
     * Test deserialize method with custom deserializer
     * @throws IOException
     */
    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        String inputValue = "customValue";
        when(parser.getText()).thenReturn(inputValue);
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        deserializer = new StringArrayDeserializer(customDeserializer);

        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        String[] result = deserializer.deserialize(parser, context);
        assertEquals(new String[0].length, result.length);
        context.returnObjectBuffer(objectBuffer);

        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        String[] input = {"test1", "test2"};
        for (String value : input) {
            when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
            when(parser.getText()).thenReturn(value);
            ObjectBuffer buffer = context.leaseObjectBuffer();
            String[] deserialized = customDeserializer.deserialize(parser, context, new TypeReference<String[]>() {});
            assertEquals(input.length, deserialized.length);
            assertArrayEquals(input, deserialized);
            context.returnObjectBuffer(buffer);
        }
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        deserializer.deserialize(parser, context, new TypeReference<String[]>() {});
        context.returnObjectBuffer(objectBuffer);
    }

    /**
     * Test deserializeWithType method
     * @throws IOException
     */
    @Test
    public void testDeserializeWithType() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        JsonToken valueString;
        String[] input = {"test1", "test2"};
        for (String value : input) {
            valueString = JsonToken.VALUE_STRING;
            when(parser.nextToken()).thenReturn(valueString);
            when(parser.getText()).thenReturn(value);
            String[] result = deserializer.deserializeWithType(parser, context, mock(TypeDeserializer.class));
            assertEquals(input.length, result.length);
            for (int i = 0; i < input.length; i++) {
                assertEquals(input[i], result[i]);
            }
        }
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        deserializer.deserializeWithType(parser, context, mock(TypeDeserializer.class));
        context.returnObjectBuffer(objectBuffer);
    }

    /**
     * Test handleNonArray method with single value
     * @throws IOException
     */
    @Test
    public void testHandleNonArraySingleValue() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        String inputValue = "testValue";
        when(parser.getText()).thenReturn(inputValue);
        String[] result = deserializer.handleNonArray(parser, context);
        assertEquals(new String[0].length, result.length);
        assertEquals(inputValue, result[0]);
        context.returnObjectBuffer(objectBuffer);
    }

    /**
     * Test handleNonArray method with JsonToken.VALUE_NULL
     * @throws IOException
     */
    @Test
    public void testHandleNonArrayNullValue() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        String[] result = deserializer.handleNonArray(parser, context);
        assertEquals(new String[0].length, result.length);
        assertNull(result[0]);
        context.returnObjectBuffer(objectBuffer);
    }

    /**
     * Test handleNonArray method with JsonToken.VALUE_NUMBER_INT
     * @throws IOException
     */
    @Test(expected = JsonMappingException.class)
    public void testHandleNonArrayNumberInt() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        deserializer.handleNonArray(parser, context);
    }

    /**
     * Test createContextual method
     */
    @Test
    public void testCreateContextual() {
        String[] input = {"test1", "test2"};
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        when(stringDeserializer.getClass()).thenReturn(StdSerializer.class); // for isDefaultDeserializer check

        // No custom deserializer
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        ObjectBuffer buffer = context.leaseObjectBuffer();
        deserializer.createContextual(context, null);
        context.returnObjectBuffer(buffer);

        // Custom deserializer
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("customDeserializer");
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        context.findContextualValueDeserializer(context.constructType(String.class), null);
        StringArrayDeserializer customDeserializer = new StringArrayDeserializer(stringDeserializer);
        assertEquals(customDeserializer, deserializer.createContextual(context, null));
        context.returnObjectBuffer(buffer);
    }

}