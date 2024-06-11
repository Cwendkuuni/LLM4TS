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
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private JsonDeserializer<String> stringDeserializer;
    private StringArrayDeserializer stringArrayDeserializer;
    private TypeDeserializer typeDeserializer;
    private DeserializationContext context;
    private ObjectBuffer buffer;

    @Before
    public void setUp() {
        stringDeserializer = mock(JsonDeserializer.class);
        stringArrayDeserializer = new StringArrayDeserializer();
        typeDeserializer = mock(TypeDeserializer.class);
        context = mock(DeserializationContext.class);
        buffer = mock(ObjectBuffer.class);
    }

    @Test
    public void testDeserializeStandard() throws IOException {
        when(context.findValueDeserializer(String[].class)).thenReturn(stringArrayDeserializer);
        when(context.leaseObjectBuffer()).thenReturn(buffer);

        String[] result = stringArrayDeserializer.deserialize(mapper.getFactory().createParser("[\"test1\", \"test2\"]"), context);
        assertArrayEquals(new String[]{"test1", "test2"}, result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        when(context.findValueDeserializer(String.class)).thenReturn(stringDeserializer);
        when(stringDeserializer.deserialize(mapper.getFactory().createParser("\"test1\""), context)).thenReturn("test1");
        when(context.findContextualValueDeserializer(String[].class, null)).thenReturn(null);

        stringArrayDeserializer._elementDeserializer = stringDeserializer;
        JsonParser jsonParser = mapper.getFactory().createParser("[\".test1\", null, \"test2\"]");

        String[] result = stringArrayDeserializer.deserialize(jsonParser, context);
        assertArrayEquals(new String[]{"test1", null, "test2"}, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        when(typeDeserializer.deserializeTypedFromArray(mapper.getFactory().createParser("[\"test1\", \"test2\"]"), context))
            .thenReturn(new int[] { 1, 2 });
        when(context.findValueDeserializer(String.class)).thenReturn(stringDeserializer);

        String[] result = stringArrayDeserializer.deserializeWithType(mapper.getFactory().createParser("[\".test1\", \"test2\"]"), context, typeDeserializer);
        assertArrayEquals(new int[] { 1, 2 }, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNonArray() throws IOException {
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        when(context.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(false);

        StringArrayDeserializer serializer = new StringArrayDeserializer();
        serializer.deserialize(mapper.getFactory().createParser("\"test\""), context);
    }

    @Test
    public void testCreateContextual() throws IOException {
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(String.class, null)).thenReturn(customDeserializer);

        StringArrayDeserializer result = (StringArrayDeserializer) stringArrayDeserializer.createContextual(context, null);
        assertEquals(customDeserializer, result._elementDeserializer);
    }
}