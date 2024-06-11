package com.example.junit;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NumberSerializerTest {

    @Test
    public void testSerialize_ValidNumber() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Number number = 123;
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        Verifier.verify(jsonGenerator).writeNumber(eq(123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_NullInput() {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Number number = null;
        assertThrows(IllegalArgumentException.class, () -> numberSerializer.serialize(number, mock(JsonGenerator.class), serializerProvider));
    }

    @Test(expected = ClassCastException.class)
    public void testSerialize_NonNumberInput() {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Object nonNumberInput = new Object();
        assertThrows(ClassCastException.class, () -> numberSerializer.serialize(nonNumberInput, jsonGenerator, serializerProvider));
    }

    @Test public void testSerialize_BigDecimalInput() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BigDecimal bigDecimal = new BigDecimal("123.45");
        numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
        Verifier.verify(jsonGenerator).writeNumber(eq(bigDecimal.toPlainString()));
    }

    @Test public void testSerialize_BigIntegerInput() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        numberSerializer.serialize(bigInteger, jsonGenerator, serializerProvider);
        Verifier.verify(jsonGenerator).writeNumber(eq(bigInteger.toString()));
    }

    @Test public void testSerialize_IntegerInput() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Number number = 123;
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        Verifier.verify(jsonGenerator).writeNumber(eq(number.intValue()));
    }

    @Test public void testSerialize_LongInput() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Number number = 123L;
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        Verifier.verify(jsonGenerator).writeNumber(eq(number.longValue()));
    }

    @Test public void testSerialize_DoubleInput() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Number number = 123.45;
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        Verifier.verify(jsonGenerator).writeNumber(eq(number.doubleValue()));
    }

    @Test public void testSerialize_FloatInput() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Float.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Number number = 123.45f;
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        Verifier.verify(jsonGenerator).writeNumber(eq(number.floatValue()));
    }

    @Test public void testSerialize_ByteInput() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Byte.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Number number = (byte) 123;
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        Verifier.verify(jsonGenerator).writeNumber(eq(number.intValue()));
    }

    @Test public void testSerialize_ShortInput() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Short.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Number number = (short) 123;
        numberSerializer.serialize(number, jsonGenerator, serializerProvider);
        Verifier.verify(jsonGenerator).writeNumber(eq(number.intValue()));
    }

    @Test(expected = IOException.class)
    public void testSerialize_IOException() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.writeNumber(anyInt())).thenThrow(new IOException());
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Number number = 123;
        assertThrows(IOException.class, () -> numberSerializer.serialize(number, jsonGenerator, serializerProvider));
    }
}