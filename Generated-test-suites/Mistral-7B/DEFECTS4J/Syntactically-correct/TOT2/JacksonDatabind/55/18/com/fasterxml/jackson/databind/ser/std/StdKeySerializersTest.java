package com.example.test;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);
    }

    @Test
    public void testGetStdKeySerializer_NullConfig() throws Exception {
        SerializationConfig config = null;
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNull(serializer);
    }

    @Test
    public void testGetStdKeySerializer_NullRawKeyType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = null;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNull(serializer);
    }

    @Test
    public void testGetStdKeySerializer_PrimitiveTypeOrNumber() throws Exception {
        SerializationConfig config = new SerializationConfig();
        int intValue = 1;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, intValue.getClass(), false);
        assertNotNull(serializer);
    }

    @Test
    public void testGetStdKeySerializer_StringType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        String rawKeyType = "String";
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Class.forName(rawKeyType), false);
        assertNotNull(serializer);
    }

    @Test
    public void testGetStdKeySerializer_ClassType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Class.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);
    }

    @Test
    public void testGetStdKeySerializer_DateType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        Date date = new Date();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, date.getClass(), false);
        ObjectMapper mapper = new ObjectMapper(config);
        String json = mapper.writeValueAsString(date);
        assertTrue(json.contains("\"date\":"));
    }

    @Test
    public void testGetStdKeySerializer_CalendarType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        Calendar calendar = Calendar.getInstance();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, calendar.getClass(), false);
        ObjectMapper mapper = new ObjectMapper(config);
        String json = mapper.writeValueAsString(calendar.getTimeInMillis());
        assertTrue(json.contains("\"long\":"));
    }

    @Test
    public void testGetStdKeySerializer_UuidType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        java.util.UUID uuid = java.util.UUID.randomUUID();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, uuid.getClass(), false);
        ObjectMapper mapper = new ObjectMapper(config);
        String json = mapper.writeValueAsString(uuid.toString());
        assertTrue(json.contains("\"" + uuid.toString() + "\""));
    }

    @Test
    public void testGetStdKeySerializer_UseDefaultTrue() throws Exception {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertNotNull(serializer);
    }

    @Test
    public void testGetStdKeySerializer_UseDefaultFalse() throws Exception {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_EnumType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        Enum<?> e = Enum.valueOf(MyEnumClass.class, "VAL1");
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, e.getClass());
        assertNotNull(serializer);
    }

    @Test
    public void testDefault_DateType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        Date date = new Date();
        JsonSerializer<Object> serializer = new Default(Default.TYPE_DATE, date.getClass()).getInstance(config);
        ObjectMapper mapper = new ObjectMapper(config);
        String json = mapper.writeValueAsString(date);
        assertTrue(json.contains("\"date\":"));
    }

    @Test
    public void testDynamic_StringType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        String rawKeyType = "String";
        JsonSerializer<Object> serializer = new Dynamic().getInstance(config);
        ObjectMapper mapper = new ObjectMapper(config);
        String json = mapper.writeValueAsString("Test");
        assertTrue(json.contains("\"String\":"));
    }

    @Test
    public void testStringKeySerializer_StringType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        String rawKeyType = "String";
        JsonSerializer<Object> serializer = new StringKeySerializer();
        ObjectMapper mapper = new ObjectMapper(config);
        String json = mapper.writeValueAsString("Test");
        assertTrue(json.contains("\"Test\":"));
    }
}