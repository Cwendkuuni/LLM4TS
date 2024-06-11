package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Properties;

@SuppressWarnings("unchecked")
public class StdKeySerializersTest {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static ObjectMapper mapper = new ObjectMapper();

    @org.junit.BeforeClass
    public static void setup() {
        // Set up Mapper with default SerializationConfig
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @org.junit.Test
    public void testGetStdKeySerializerWithString() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());
    }

    @org.junit.Test
    public void testGetStdKeySerializerWithObject() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Object.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
    }

    @org.junit.Test
    public void testGetStdKeySerializerWithDate() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer.getClass().getField("_typeId").getInt(null));
    }

    @org.junit.Test
    public void testGetStdKeySerializerWithCalendar() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer.getClass().getField("_typeId").getInt(null));
    }

    @org.junit.Test
    public void testGetStdKeySerializerWithEnum() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Enum.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer.getClass().getField("_typeId").getInt(null));
    }

    @org.junit.Test
    public void testGetStdKeySerializerWithClass() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Class.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, serializer.getClass().getField("_typeId").getInt(null));
    }

    @org.junit.Test
    public void testGetStdKeySerializerWithPrimitiveType() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
    }

    @org.junit.Test
    public void testGetStdKeySerializerWithNumber() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Number.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
    }

    @org.junit.Test
    public void testGetStdKeySerializerWithUuid() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), java.util.UUID.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer.getClass().getField("_typeId").getInt(null));
    }

    @org.junit.Test
    public void testGetFallbackKeySerializerWithString() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());
    }

    @org.junit.Test
    public void testGetFallbackKeySerializerWithObject() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Object.class);
        assertEquals(null, serializer);
    }

    @org.junit.Test
    public void testGetFallbackKeySerializerWithDate() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Date.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
    }

    @org.junit.Test
    public void testGetFallbackKeySerializerWithCalendar() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Calendar.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
    }

    @org.junit.Test
    public void testGetFallbackKeySerializerWithEnum() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Enum.class);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer.getClass().getField("_typeId").getInt(null));
    }

    @org.junit.Test
    public void testGetFallbackKeySerializerWithClass() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Class.class);
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, serializer.getClass().getField("_typeId").getInt(null));
    }

    @org.junit.Test
    public void testGetFallbackKeySerializerWithEnumSet() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), EnumSet.class);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer.getClass().getField("_typeId").getInt(null));
    }

    @org.junit.Test
    public void testGetFallbackKeySerializerWithCustomClass() throws IOException {
        class CustomClass {}
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), CustomClass.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());
    }

    @org.junit.Test
    public void testDynamicKeySerializer() throws IOException {
        Object obj = new Object() {
            private static final long serialVersionUID = 1L;
            public Class<?> getKeyClass() {
                return String.class;
            }
        };

        JsonSerializer<Object> serializer = mapper.getSerializationConfig().getDefaultSerializerProvider().getSerializerProvider().getSerializerForClass(obj.getClass());
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());

        JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter());
        serializer.serialize(obj, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());

        String json = generator.getOutputContent();
        assertEquals("{\"key\":\"com.fasterxml.jackson.databind.ser.std.StdKeySerializersTest$1\"}", json);
    }
}