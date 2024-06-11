package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Test;

/**
 * Test class for {@link NumberSerializer}
 */
public class NumberSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Test method for {@link NumberSerializer#NumberSerializer(Class)}
     */
    @Test
    public void testNumberSerializerConstruction() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        assertEquals(Number.class, numberSerializer.getHandledType());
    }

    /**
     * Test method for {@link NumberSerializer#createContextual(SerializerProvider, BeanProperty)}
     */
    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        JsonFormat jsonFormat = new JsonFormat(format);

        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonSerializer<?> serializer = numberSerializer.createContextual(provider, property);
        assertEquals(ToStringSerializer.instance, serializer);
    }

    /**
     * Test method for {@link NumberSerializer#serialize(Number, JsonGenerator, SerializerProvider)}
     */
    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BigDecimal bigDecimal = new BigDecimal("123.456");

        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(bigDecimal);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BigInteger bigInteger = BigInteger.valueOf(123);

        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.serialize(bigInteger, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(bigInteger);
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        long longValue = 123;

        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.serialize(longValue, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(longValue);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        double doubleValue = 123.456;

        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.serialize(doubleValue, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(doubleValue);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        float floatValue = 123.456f;

        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.serialize(floatValue, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(floatValue);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        int intValue = 123;

        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.serialize(intValue, jsonGenerator, serializerProvider);
        verify(jsonGenerator, never()).writeNumber(intValue.toString());
        verify(jsonGenerator).writeNumber(intValue);
    }

    @Test
    public void testSerializeByte() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        byte byteValue = 123;

        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.serialize(byteValue, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(byteValue);
    }

    @Test
    public void testSerializeShort() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        short shortValue = 123;

        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.serialize(shortValue, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(shortValue);
    }

    @Test
    public void testSerializeUnsupportedType() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        String unsupportedType = "UnsupportedType";

        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.serialize(unsupportedType, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(unsupportedType.toString());
    }

    /**
     * Test method for {@link NumberSerializer#getSchema(SerializerProvider, Type)}
     */
    @Test
    public void testGetSchema() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonNode jsonNode = mapper.readValue("{\"type\": \"number\", \"allowedNumberTypes\": [\"integer\"]}", JsonNode.class);

        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonNode schema = numberSerializer.getSchema(serializerProvider, JsonType.NUMBER);
        assertEquals(jsonNode, schema);
    }

    /**
     * Test method for {@link NumberSerializer#acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType)}
     */
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = null;

        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);
    }
}