package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperForTest;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Unit tests for {@link NumberSerializer}.
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeInteger() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        mapper.writeValue(gen, 1);

        Number deserialized = mapper.readValue(gen.asStream(), Number.class);
        assertEquals(1, deserialized.intValue());
    }

    @Test
    public void testSerializeLong() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        mapper.writeValue(gen, 1L);

        Number deserialized = mapper.readValue(gen.asStream(), Number.class);
        assertEquals(1L, deserialized.longValue());
    }

    @Test
    public void testSerializeBigInteger() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.BIG_DECIMAL_FOR_INTEGERS, true);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        BigInteger bigInteger = BigInteger.valueOf(123456789012345L);
        mapper.writeValue(gen, bigInteger);

        Number deserialized = mapper.readValue(gen.asStream(), Number.class);
        assertEquals(bigInteger, deserialized);
    }

    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.BIG_DECIMAL_FOR_INTEGERS, true);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        BigDecimal bigDecimal = new BigDecimal("123456789012345.123456");
        mapper.writeValue(gen, bigDecimal);

        Number deserialized = mapper.readValue(gen.asStream(), Number.class);
        assertEquals(bigDecimal, deserialized);
    }

    @Test
    public void testSerializeFloat() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        float value = 1.23f;
        mapper.writeValue(gen, value);

        Number deserialized = mapper.readValue(gen.asStream(), Number.class);
        assertEquals(value, deserialized.floatValue(), 0.001f);
    }

    @Test
    public void testSerializeDouble() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        double value = 1.23;
        mapper.writeValue(gen, value);

        Number deserialized = mapper.readValue(gen.asStream(), Number.class);
        assertEquals(value, deserialized.doubleValue(), 0.001);
    }

    @Test
    public void testSerializeByte() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        byte value = 1;
        mapper.writeValue(gen, value);

        Number deserialized = mapper.readValue(gen.asStream(), Number.class);
        assertEquals((int) value, deserialized.intValue());
    }

    @Test
    public void testSerializeShort() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        short value = 1;
        mapper.writeValue(gen, value);

        Number deserialized = mapper.readValue(gen.asStream(), Number.class);
        assertEquals((int) value, deserialized.intValue());
    }

    @Test
    public void testSerializeUnsupportedNumberType() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new JsonMapperForTest();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Object obj = new Object() {
            @Override
            public Number getNumber() {
                return new Object() {};
            }
        };
        mapper.writeValue(gen, obj.getNumber());

        try {
            Number deserialized = mapper.readValue(gen.asStream(), Number.class);
            fail("Expected JsonMappingException");
        } catch (IOException e) {
            // expected
        }
    }
}