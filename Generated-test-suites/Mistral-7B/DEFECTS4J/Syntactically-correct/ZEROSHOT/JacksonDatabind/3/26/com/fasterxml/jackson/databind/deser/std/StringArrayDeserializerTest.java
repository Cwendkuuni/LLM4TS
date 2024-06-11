package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for {@link StringArrayDeserializer}
 */
public class StringArrayDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jsonParser = objectMapper.getFactory().createParser("[]");
        deserializationContext = objectMapper.getDeserializationContext();
        typeDeserializer = mock(TypeDeserializer.class);
    }

    @Test
    public void testEmptyArray() throws IOException {
        String[] result = new StringArrayDeserializer().deserialize(jsonParser, deserializationContext);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testNullArray() throws IOException {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        String[] result = new StringArrayDeserializer().deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }

    @Test
    public void testSingleValueArray() throws IOException {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        String[] result = new StringArrayDeserializer().deserialize(jsonParser, deserializationContext);
        assertArrayEquals(new String[] {"test"}, result);
    }

    @Test
    public void testArrayWithCustomDeserializer() throws IOException {
        StringArrayDeserializer customDeserializer = new StringArrayDeserializer(mock(JsonDeserializer.class));
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test1");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = customDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(new String[] {"test1", null}, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArray() throws IOException {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        new StringArrayDeserializer().deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        TypeReference<String[]> typeReference = new TypeReference<String[]>() {};
        String[] result = new StringArrayDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testCreateContextual() throws IOException {
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        StringArrayDeserializer contextualDeserializer = new StringArrayDeserializer();
        contextualDeserializer = contextualDeserializer.createContextual(deserializationContext, null);
        assertSame(contextualDeserializer, new StringArrayDeserializer(stringDeserializer));
    }
}