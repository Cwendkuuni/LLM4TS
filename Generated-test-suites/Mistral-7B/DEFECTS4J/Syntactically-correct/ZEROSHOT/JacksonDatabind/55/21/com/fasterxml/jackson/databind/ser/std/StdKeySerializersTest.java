package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.util.UUID;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

public class StdKeySerializersTest {

    private ObjectMapper objectMapper;

    @BeforeClass
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultKeySerializer() throws JsonProcessingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(objectMapper.getSerializationConfig(), null, false);
        assertNotNull(serializer);
        Object value = new Integer(1);
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        generator.flush();
    }

    @Test
    public void testGetStringSerializer() throws JsonProcessingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(objectMapper.getSerializationConfig(), String.class, false);
        assertNotNull(serializer);
        Object value = "test";
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        generator.flush();
    }

    @Test
    public void testPrimitiveAndNumberSerializer() throws JsonProcessingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(objectMapper.getSerializationConfig(), int.class, false);
        assertNotNull(serializer);
        Object value = new Integer(1);
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        generator.flush();

        serializer = StdKeySerializers.getDefaultKeySerializer(objectMapper.getSerializationConfig(), double.class, false);
        assertNotNull(serializer);
        Object valueDouble = 3.14;
        JsonGenerator generatorDouble = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(valueDouble, generatorDouble, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        generatorDouble.flush();
    }

    @Test
    public void testClassSerializer() throws JsonProcessingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(objectMapper.getSerializationConfig(), Class.class, false);
        assertNotNull(serializer);
        Object value = String.class;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        generator.flush();
    }

    @Test
    public void testDateSerializer() throws JsonProcessingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(objectMapper.getSerializationConfig(), Date.class, false);
        assertNotNull(serializer);
        Object value = new Date();
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        generator.flush();
    }

    @Test
    public void testCalendarSerializer() throws JsonProcessingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(objectMapper.getSerializationConfig(), Calendar.class, false);
        assertNotNull(serializer);
        Calendar calendar = Calendar.getInstance();
        Object value = calendar;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        generator.flush();
    }

    @Test
    public void testUuidSerializer() throws JsonProcessingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(objectMapper.getSerializationConfig(), UUID.class, false);
        assertNotNull(serializer);
        Object value = UUID.randomUUID();
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        generator.flush();
    }

    @Test
    public void testEnumSerializer() throws JsonProcessingException, IOException {
        enum TestEnum {
            ONE, TWO
        }
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(objectMapper.getSerializationConfig(), TestEnum.class, false);
        assertNotNull(serializer);
        TestEnum value = TestEnum.ONE;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        generator.flush();
    }

    @Test
    public void testGetFallbackKeySerializer() throws JsonProcessingException, IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), null);
        assertNotNull(serializer);
        Object value = new Integer(1);
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        generator.flush();
    }

    @Test
    public void testGetFallbackKeySerializerWithEnum() throws JsonProcessingException, IOException {
        enum TestEnum {
            ONE, TWO
        }
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), TestEnum.class);
        assertNotNull(serializer);
        TestEnum value = TestEnum.ONE;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, generator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        generator.flush();
    }
}