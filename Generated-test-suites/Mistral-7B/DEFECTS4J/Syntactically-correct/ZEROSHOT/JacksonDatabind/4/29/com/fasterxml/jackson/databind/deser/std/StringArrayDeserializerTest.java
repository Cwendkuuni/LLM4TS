package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.TypeDeserializer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for StringArrayDeserializer.
 */
public class StringArrayDeserializerTest {

    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private ObjectBuffer objectBuffer;

    @Before
    public void setUp() {
        jsonParser = Mockito.mock(JsonParser.class);
        deserializationContext = Mockito.mock(DeserializationContext.class);
        objectBuffer = Mockito.mock(ObjectBuffer.class);
    }

    @Test
    public void testDeserializeStandard() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test1");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test2");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = new StringArrayDeserializer().deserialize(jsonParser, deserializationContext);

        Assert.assertArrayEquals(new String[]{"test1", "test2"}, result);
    }

    @Test
    public void testDeserializeCustom() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("testCustom1");

        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        when(deserializationContext.findContextualValueDeserializer(String.class, null)).thenReturn(stringDeserializer);

        StringArrayDeserializer serializer = new StringArrayDeserializer(stringDeserializer);
        String[] result = serializer.deserialize(jsonParser, deserializationContext);

        Assert.assertArrayEquals(new String[]{"testCustom1"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalid() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER);

        new StringArrayDeserializer().deserialize(jsonParser, deserializationContext);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNull() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        new StringArrayDeserializer().deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test1");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test2");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn(new String[]{"test1", "test2"});

        String[] result = new StringArrayDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        Assert.assertArrayEquals(new String[]{"test1", "test2"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithTypeInvalid() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER);

        new StringArrayDeserializer().deserializeWithType(jsonParser, deserializationContext, mock(TypeDeserializer.class));
    }

    @Test
    public void testCreateContextual() {
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        when(deserializationContext.findContextualValueDeserializer(String.class, null)).thenReturn(stringDeserializer);

        JsonDeserializer<StringArrayDeserializer> result = new StringArrayDeserializer().createContextual(deserializationContext, null);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof StringArrayDeserializer);
        Assert.assertSame(stringDeserializer, result.getContentDeserializer());
    }
}