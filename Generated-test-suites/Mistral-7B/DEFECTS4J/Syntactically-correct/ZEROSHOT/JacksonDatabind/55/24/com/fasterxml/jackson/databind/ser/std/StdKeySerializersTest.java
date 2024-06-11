package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer_Default() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNull(serializer);
    }

    @Test
    public void testGetStdKeySerializer_String() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_Date() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_Class() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CLASS, Class.class), serializer);
    }

    @Test
    public void testGetStdKeySerializer_Number() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Integer.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_Enum() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, MyEnum.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_ENUM, MyEnum.class), serializer);
    }

    @Test
    public void testGetStdKeySerializer_CustomType() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, CustomType.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CLASS, CustomType.class), serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_String() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_Date() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Date.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_CustomType() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, CustomType.class);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CLASS, CustomType.class), serializer);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFallbackKeySerializer_Null() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
    }

    @Test
    public void testDynamicKeySerializer() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        Object testObj = new TestDynamicKeySerializerObj();

        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        String jsonWithDefault = mapper.writeValueAsString(new Object[] { testObj, "test" });
        String jsonWithDynamic = mapper.writeValueAsString(new Object[] { testObj, "dynamic" });

        JsonParser parser = mapper.getFactory().createParser(jsonWithDefault);
        assertEquals("test", parser.readValueAs(String.class));

        JsonParser parserDynamic = mapper.getFactory().createParser(jsonWithDynamic);
        assertEquals("dynamic", parserDynamic.readValueAs(String.class));
    }

    static class TestDynamicKeySerializerObj {
    }

    enum MyEnum {
        ONE, TWO, THREE
    }

    static class CustomType {
    }
}

class DefaultSerializerTest extends StdKeySerializers {

    @Test
    public void testSerialize_Date() throws IOException {
        JsonGenerator g = createMyTestGenerator();
        Date date = new Date();
        serialize(date, g, new SerializerProvider());
        assertEquals("2022-03-25T14:27:36.225Z", g.getOutputText());
    }

    @Test
    public void testSerialize_Calendar() throws IOException {
        JsonGenerator g = createMyTestGenerator();
        Calendar calendar = Calendar.getInstance();
        serialize(calendar, g, new SerializerProvider());
        assertEquals("2022-03-25T14:27:36.225Z", g.getOutputText());
    }

    @Test
    public void testSerialize_Class() throws IOException {
        JsonGenerator g = createMyTestGenerator();
        Class<?> clazz = String.class;
        serialize(clazz, g, new SerializerProvider());
        assertEquals("java.lang.String", g.getOutputText());
    }

    @Test
    public void testSerialize_Enum() throws IOException {
        JsonGenerator g = createMyTestGenerator();
        MyEnum enumValue = MyEnum.ONE;
        serialize(enumValue, g, new SerializerProvider());
        assertEquals("ONE", g.getOutputText());
    }

    @Test
    public void testSerialize_ToString() throws IOException {
        JsonGenerator g = createMyTestGenerator();
        String stringValue = "test";
        serialize(stringValue, g, new SerializerProvider());
        assertEquals("test", g.getOutputText());
    }

    @Test
    public void testSerialize_Dynamic() throws IOException {
        JsonGenerator g = createMyTestGenerator();
        Object testObj = new TestDynamicKeySerializerObj();
        serialize(testObj, g, new SerializerProvider());
        PropertySerializerMap serializers = ((StdSerializer.SerializerProvider) g.getProvider()).getSerializerProvider()
                .getSerializers();
        JsonSerializer<Object> serializer = serializers.getSerializerProvider().findValueSerializer(TestDynamicKeySerializerObj.class);
        assertEquals("testDynamicKeySerializerObj", serializer.getClass().getSimpleName());
        assertEquals("testDynamicKeySerializerObj", g.getOutputText());
    }

    @Test
    public void testSerialize_Fallback() throws IOException {
        JsonGenerator g = createMyTestGenerator();
        Object testObj = new TestFallbackKeySerializerObj();
        serialize(testObj, g, new SerializerProvider());
        assertEquals("testFallbackKeySerializerObj", g.getOutputText());
    }

    static class TestDynamicKeySerializerObj {
    }

    static class TestFallbackKeySerializerObj {
    }
}