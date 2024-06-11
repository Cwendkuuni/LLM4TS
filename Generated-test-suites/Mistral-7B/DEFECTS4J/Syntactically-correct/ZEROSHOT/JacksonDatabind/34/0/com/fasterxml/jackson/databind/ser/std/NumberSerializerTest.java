package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * Test class for {@link NumberSerializer}.
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeInt() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
         int intValue = 123;
         SerializerProvider provider = mapper.getSerializerProvider();
         JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
         serializer.serialize(intValue, generator, provider);
         generator.flush();
         String expected = "123";
         assertEquals(expected, generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        long longValue = 9876543210L;
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(longValue, generator, provider);
        generator.flush();
        String expected = "9876543210";
        assertEquals(expected, generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeFloat() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        float floatValue = 123.45f;
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(floatValue, generator, provider);
        generator.flush();
        String expected = "123.45";
        assertEquals(expected, generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        double doubleValue = 123.456;
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(doubleValue, generator, provider);
        generator.flush();
        String expected = "123.456";
        assertEquals(expected, generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        BigInteger bigIntegerValue = new BigInteger("12345678901234567890");
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(bigIntegerValue, generator, provider);
        generator.flush();
        String expected = "12345678901234567890";
        assertEquals(expected, generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        BigDecimal bigDecimalValue = new BigDecimal("123.456");
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(bigDecimalValue, generator, provider);
        generator.flush();
        String expected = "123.456";
        assertEquals(expected, generator.getOutputContentAsString());
    }

    @Test
    public void testSerializationWithTypeInformation() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        JsonSerializer<Number> serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        Object value = new BigInteger("12345678901234567890");
        serializer.serialize(value, generator, serializerProvider, TypeSerializer.instance.findValueSerializer(BigInteger.class, serializerProvider));
        generator.flush();
        String expected = "12345678901234567890";
        assertEquals(expected, generator.getOutputContentAsString());
    }

    @Test
    public void testDeserializeInt() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("123");
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(123, deserialized.intValue());
    }

    @Test
    public void testDeserializeLong() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("9876543210");
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(9876543210L, deserialized.longValue());
    }

    @Test
    public void testDeserializeFloat() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("123.45");
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(123.45f, deserialized, 0.001f);
    }

    @Test
    public void testDeserializeDouble() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("123.456");
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(123.456, deserialized, 0.00001);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("12345678901234567890");
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(new BigInteger("12345678901234567890"), deserialized);
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("123.456");
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(new BigDecimal("123.456"), deserialized);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalid() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("abc");
        mapper.readValue(parser, Number.class);
    }

    /**
     * Test class for deserializing Number with type information.
     */
    @JsonSerialize(using = NumberSerializer.class)
    @JsonDeserialize(using = NumberDeserializer.class)
    static class TestNumber {
        private Number number;

        public Number getNumber() {
            return number;
        }

        public void setNumber(Number number) {
            this.number = number;
        }
    }

    @Test
    public void testDeserializeWithTypeInformation() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"number\":\"12345678901234567890\"}");
        TestNumber testNumber = mapper.readValue(parser, TestNumber.class);
        assertEquals(new BigInteger("12345678901234567890"), testNumber.getNumber());
    }
}

class NumberDeserializer extends StdScalarDeserializer<Number> {

    public NumberDeserializer() {
        this(null);
    }

    protected NumberDeserializer(Class<? extends Number> vc) {
        super(Number.class, vc);
    }

    @Override
    public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Number number = null;
        JsonToken token = p.getCurrentToken();
        if (token == JsonToken.VALUE_STRING) {
            String stringValue = p.getValueAsString();
            if (stringValue.matches("-?[\\d]+(\\.\\d+)?")) {
                number = new BigDecimal(stringValue);
            } else if (stringValue.matches("-?[\\d]+")) {
                number = new BigInteger(stringValue);
            }
        } else if (token.isNumeric()) {
            number = (Number) p.numberValue();
        }
        if (number == null) {
            throw ctxt.mappingException("Could not deserialize Number");
        }
        return number;
    }
}