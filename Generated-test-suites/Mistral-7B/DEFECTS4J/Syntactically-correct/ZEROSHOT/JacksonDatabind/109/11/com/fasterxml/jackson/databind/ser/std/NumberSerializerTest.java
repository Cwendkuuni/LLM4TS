package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for {@link NumberSerializer}
 */
public class NumberSerializerTest {

    @InjectMocks
    private NumberSerializer numberSerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private BeanProperty property;

    @Mock
    private JsonFormat jsonFormat;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal bigDecimal = new BigDecimal("123.45");
        when(serializerProvider.getActiveContext()).thenReturn(objectMapper.getConfiguration());
        when(objectMapper.getConfiguration().findFormatOverrides(serializerProvider, property, Number.class))
                .thenReturn(jsonFormat);
        when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);

        numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);

        assertEquals("123.45", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        when(serializerProvider.getActiveContext()).thenReturn(objectMapper.getConfiguration());
        when(objectMapper.getConfiguration().findFormatOverrides(serializerProvider, property, Number.class))
                .thenReturn(null);

        numberSerializer.serialize(bigInteger, jsonGenerator, serializerProvider);

        assertEquals("12345678901234567890", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long longValue = 123L;
        when(serializerProvider.getActiveContext()).thenReturn(objectMapper.getConfiguration());
        when(objectMapper.getConfiguration().findFormatOverrides(serializerProvider, property, Long.class))
                .thenReturn(null);

        numberSerializer.serialize(longValue, jsonGenerator, serializerProvider);

        assertEquals("123", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double doubleValue = 123.45;
        when(serializerProvider.getActiveContext()).thenReturn(objectMapper.getConfiguration());
        when(objectMapper.getConfiguration().findFormatOverrides(serializerProvider, property, Double.class))
                .thenReturn(null);

        numberSerializer.serialize(doubleValue, jsonGenerator, serializerProvider);

        assertEquals("123.45", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeInt() throws IOException {
        Integer intValue = 123;
        when(serializerProvider.getActiveContext()).thenReturn(objectMapper.getConfiguration());
        when(objectMapper.getConfiguration().findFormatOverrides(serializerProvider, property, Integer.class))
                .thenReturn(null);

        numberSerializer.serialize(intValue, jsonGenerator, serializerProvider);

        assertEquals("123", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeOtherNumberTypes() throws IOException {
        Byte byteValue = 123;
        Short shortValue = 123;
        Float floatValue = 123.45f;

        when(serializerProvider.getActiveContext()).thenReturn(objectMapper.getConfiguration());
        when(objectMapper.getConfiguration().findFormatOverrides(serializerProvider, property, Number.class))
                .thenReturn(null);

        numberSerializer.serialize(byteValue, jsonGenerator, serializerProvider);
        numberSerializer.serialize(shortValue, jsonGenerator, serializerProvider);
        numberSerializer.serialize(floatValue, jsonGenerator, serializerProvider);

        assertEquals("123", jsonGenerator.getOutputBuffer().toString());
        assertEquals("123", jsonGenerator.getOutputBuffer().toString());
        assertEquals("123.45", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeCustomNumberTypeWithStringFormat() throws IOException {
        JsonSerializer<CustomNumber> customNumberSerializer = mock(JsonSerializer.class);
        when(objectMapper.getSerializationConfig().findValueSerializer(CustomNumber.class)).thenReturn(customNumberSerializer);

        when(serializerProvider.getActiveContext()).thenReturn(objectMapper.getConfiguration());
        when(objectMapper.getConfiguration().findFormatOverrides(serializerProvider, property, CustomNumber.class))
                .thenReturn(jsonFormat);
        when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);

        CustomNumber customNumber = new CustomNumber(123);
        numberSerializer.serialize(customNumber, jsonGenerator, serializerProvider);

        assertEquals("", jsonGenerator.getOutputBuffer().toString());
        verify(customNumberSerializer).serialize(customNumber, jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerializeCustomNumberTypeWithoutStringFormat() throws IOException {
        JsonSerializer<CustomNumber> customNumberSerializer = mock(JsonSerializer.class);
        when(objectMapper.getSerializationConfig().findValueSerializer(CustomNumber.class)).thenReturn(customNumberSerializer);

        when(serializerProvider.getActiveContext()).thenReturn(objectMapper.getConfiguration());
        when(objectMapper.getConfiguration().findFormatOverrides(serializerProvider, property, Number.class))
                .thenReturn(null);

        CustomNumber customNumber = new CustomNumber(123);
        numberSerializer.serialize(customNumber, jsonGenerator, serializerProvider);

        assertEquals("123", jsonGenerator.getOutputBuffer().toString());
        verify(customNumberSerializer, never()).serialize(customNumber, jsonGenerator, serializerProvider);
    }

    @JsonSerialize(using = ToStringSerializer.class)
    private static class CustomNumber extends Number {
        private int value;

        public CustomNumber(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value;
        }

        @Override
        public long longValue() {
            return value;
        }

        @Override
        public float floatValue() {
            return value;
        }

        @Override
        public double doubleValue() {
            return value;
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public Type getType(SerializerProvider serializers) {
            return null;
        }

        @Override
        public BigDecimal toBigDecimal(Locale locale) {
            throw new UnsupportedOperationException();
        }

        @Override
        public BigInteger toBigInteger() {
            throw new UnsupportedOperationException();
        }
    }
}