package com.fasterxml.jackson.databind.ser.std; // imported for testing purposes

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StdKeySerializersTest {

    private JsonGenerator mockJsonGenerator;
    private SerializerProvider mockSerializerProvider;

    @Before
    public void setup() throws Exception {
        mockJsonGenerator = mock(JsonGenerator.class);
        mockSerializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testDefaultKeySerializer() throws IOException, JsonMappingException {
        Object value = new Object();
        StdKeySerializers.DEFAULT_KEY_SERIALIZER.serialize(value, mockJsonGenerator, mockSerializerProvider);
        Assert.assertEquals("defaultKeySerializer", mockJsonGenerator.getOutputValue());
    }

    @Test
    public void testGetStringSerializer() throws IOException, JsonMappingException {
        String key = "testString";
        Object value = key;
        StdKeySerializers.STRING_KEY_SERIALIZER.serialize(value, mockJsonGenerator, mockSerializerProvider);
        Assert.assertEquals(key, mockJsonGenerator.getOutputValue());
    }

    @Test
    public void testGetFallbackKeySerializerForObject() throws IOException, JsonMappingException {
        Object value = new Object();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, value.getClass());
        Assert.assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializerForEnum() throws IOException, JsonMappingException {
        Enum testEnum = Enum.valueOf(String.class, "TestEnumValue");
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, testEnum.getClass());
        Assert.assertNotNull(serializer);
        String key = serializer.serializeValueAsString(testEnum, mockJsonGenerator, mockSerializerProvider);
        Assert.assertEquals(testEnum.name(), key);
    }
}