package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer
 */
@SuppressWarnings("unchecked")
public class NumberSerializerTest {

    private JsonGenerator jsonGenerator;
    private JsonParser jsonParser;
    private ObjectMapper objectMapper;
    private NumberSerializer numberSerializer;

    @Before
    public void setUp() throws Exception {
        jsonGenerator = mock(JsonGenerator.class);
        jsonParser = mock(JsonParser.class);
        objectMapper = new ObjectMapper();
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal bigDecimal = new BigDecimal("123.456");
        numberSerializer.serialize(bigDecimal, jsonGenerator, objectMapper.getSerializerProvider());
        verify(jsonGenerator).writeNumber(bigDecimal);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        numberSerializer.serialize(bigInteger, jsonGenerator, objectMapper.getSerializerProvider());
        verify(jsonGenerator).writeNumber(bigInteger);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer integer = 123;
        numberSerializer.serialize(integer, jsonGenerator, objectMapper.getSerializerProvider());
        verify(jsonGenerator).writeNumber(integer.intValue());
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long longValue = 123L;
        numberSerializer.serialize(longValue, jsonGenerator, objectMapper.getSerializerProvider());
        verify(jsonGenerator).writeNumber(longValue.longValue());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double doubleValue = 123.456;
        numberSerializer.serialize(doubleValue, jsonGenerator, objectMapper.getSerializerProvider());
        verify(jsonGenerator).writeNumber(doubleValue.doubleValue());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Float floatValue = 123.456f;
        numberSerializer.serialize(floatValue, jsonGenerator, objectMapper.getSerializerProvider());
        verify(jsonGenerator).writeNumber(floatValue.floatValue());
    }

    @Test
    public void testSerializeByte() throws IOException {
        Byte byteValue = 127;
        numberSerializer.serialize(byteValue, jsonGenerator, objectMapper.getSerializerProvider());
        verify(jsonGenerator).writeNumber(byteValue.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        Short shortValue = 123;
        numberSerializer.serialize(shortValue, jsonGenerator, objectMapper.getSerializerProvider());
        verify(jsonGenerator).writeNumber(shortValue.intValue());
    }

    @Test
    public void testSerializeUnsupportedNumberType() throws IOException {
        Object unsupportedNumber = new Object() {
            // This class should not be instantiated
        };
        numberSerializer.serialize(unsupportedNumber, jsonGenerator, objectMapper.getSerializerProvider());
        verify(jsonGenerator).writeNumber(unsupportedNumber.toString());
    }
}