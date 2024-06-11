package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StringArrayDeserializerTest {

    @InjectMocks
    private StringArrayDeserializer stringArrayDeserializer;

    @Mock
    private JsonDeserializer<String> jsonDeserializer;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private ObjectBuffer objectBuffer;

    @Test
    public void testDeserializeDefault() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test2");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(new String[]{"test", "test2"}, result);
    }

    @Test
    public void testDeserializeCustom() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test2");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);
        when(deserializationContext.findContextualValueDeserializer(String.class, null)).thenReturn(jsonDeserializer);

        when(jsonDeserializer.getNullValue()).thenReturn(null);

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(new String[]{"null", "test", null, "test2"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalid() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);

        stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithType() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        StringArrayType stringArrayType = new StringArrayType();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn(new Object[]{1, 2, 3});

        when(deserializationContext.constructType(String[].class)).thenReturn(stringArrayType);
        when(deserializationContext.findValueDeserializer(String.class, null)).thenReturn(mock(JsonDeserializer.class));

        stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
    }

    @Test
    public void testCreateContextual() throws Exception {
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);

        StringArrayDeserializer expectedResult = new StringArrayDeserializer(jsonDeserializer);

        when(deserializationContext.findContextualValueDeserializer(String.class, null)).thenReturn(jsonDeserializer);

        StringArrayDeserializer result = stringArrayDeserializer.createContextual(deserializationContext, null);

        assert(expectedResult == result);
    }

    @Test
    public void testCreateContextualWithNull() throws Exception {
        JsonDeserializer<String> jsonDeserializer = null;

        StringArrayDeserializer expectedResult = new StringArrayDeserializer();

        StringArrayDeserializer result = stringArrayDeserializer.createContextual(deserializationContext, null);

        assert(expectedResult == result);
    }

    @Test
    public void testCreateContextualWithContentConverter() throws Exception {
        MyStringDeserializer myStringDeserializer = mock(MyStringDeserializer.class);

        StringArrayDeserializer expectedResult = new StringArrayDeserializer(myStringDeserializer);

        when(deserializationContext.findContentConverter(String[].class, null)).thenReturn(myStringDeserializer);

        StringArrayDeserializer result = stringArrayDeserializer.createContextual(deserializationContext, null);

        assert(expectedResult == result);
    }

    private static class MyStringDeserializer extends StdDeserializer<String> {
        MyStringDeserializer() {
            this(null);
        }

        MyStringDeserializer(Class<String> vc) {
            super(String.class);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getText();
        }
    }
}