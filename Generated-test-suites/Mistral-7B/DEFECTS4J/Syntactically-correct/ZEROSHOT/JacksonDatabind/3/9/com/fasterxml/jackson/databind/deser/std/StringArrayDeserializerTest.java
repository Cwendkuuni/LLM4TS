package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ObjectBufferBuilder;

/**
 * Test class for {@link StringArrayDeserializer}
 */
public class StringArrayDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;
    private Converter<String, String[]> stringToArrayConverter;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_Empty_BEANS, false);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        typeDeserializer = mock(TypeDeserializer.class);
        stringToArrayConverter = mock(Converter.class);
        when(deserializationContext.findConvertingContentDeserializer(any(), any(), any()))
                .thenReturn(stringToArrayConverter);
        when(deserializationContext.handleSecondaryContextualization(any(), any()))
                .thenReturn(stringToArrayConverter);
        when(deserializationContext.findContextualValueDeserializer(any(), any()))
                .thenReturn(null);
        when(deserializationContext.isEnabled(anyInt()))
                .thenReturn(true);
        when(deserializationContext.isEnabled(anyString()))
                .thenReturn(true);
        when(deserializationContext.constructType(any(Class.class)))
                .thenReturn(String[].class);
        when(deserializationContext.leaseObjectBuffer())
                .thenReturn(ObjectBufferBuilder.newInstance().build());
    }

    @Test
    public void testDefaultDeserialization() throws IOException {
        String json = "[\"test\", \"test2\"]";
        String[] expectedResult = {"test", "test2"};

        when(jsonParser.getText()).thenReturn(json);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test2");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = deserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedResult.length, result.length);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testCustomDeserialization() throws IOException {
        String json = "[null, \"test\", null, \"test2\"]";
        String[] expectedResult = {null, "test", null, "test2"};

        when(jsonParser.getText()).thenReturn(json);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        when(customDeserializer.deserialize(jsonParser, deserializationContext)).then(invocation -> {
            JsonToken token = invocation.getArgument(0).nextToken();
            return token == JsonToken.VALUE_NULL ? null : invocation.getArgument(1).getValueFactory()
                    .valueFromString(invocation.getArgument(0).getText());
        });

        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);

        String[] result = deserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedResult.length, result.length);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testNullValueDeserialization() throws IOException {
        String json = "[null, null]";

        when(jsonParser.getText()).thenReturn(json);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = deserializer.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(new String[] {null, null}, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArrayDeserialization() throws IOException {
        String json = "{\"name\": \"test\"}";

        when(jsonParser.getText()).thenReturn(json);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getText()).thenReturn("name");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        deserializer.deserialize(jsonParser, deserializationContext);
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArrayDeserializationWithNullValue() throws IOException {
        String json = "{\"name\": null}";

        when(jsonParser.getText()).thenReturn(json);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getText()).thenReturn("name");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        deserializer.deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testContextualDeserialization() throws IOException {
        String json = "[null, \"test\", null, \"test2\"]";

        when(jsonParser.getText()).thenReturn(json);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        when(customDeserializer.deserialize(jsonParser, deserializationContext)).then(invocation -> {
            JsonToken token = invocation.getArgument(0).nextToken();
            return token == JsonToken.VALUE_NULL ? null : invocation.getArgument(1).getValueFactory()
                    .valueFromString(invocation.getArgument(0).getText());
        });

        when(deserializationContext.findContextualValueDeserializer(eq(String[].class), any()))
                .thenReturn(customDeserializer);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = deserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedResult.length, result.length);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testTypeDeserialization() throws IOException {
        String json = "[1, 2, 3]";
        TypeReference<String[]> expectedType = new TypeReference<String[]>() {};

        when(jsonParser.getText()).thenReturn(json);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn(
                new Object[] {1, 2, 3});

        when(deserializationContext.findContextualValueDeserializer(eq(String[].class), any()))
                .thenReturn(null);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        assertEquals(expectedType.getComponentType().getComponentType(), result.getClass());
        assertEquals(3, result.length);
        assertArrayEquals(new Object[] {1, 2, 3}, result);
    }
}