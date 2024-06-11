package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JsonGenerator jsonGenerator = serializerProvider.getJsonGenerator(new Object());
        numberSerializer.serialize(BigDecimal.ONE, jsonGenerator, serializerProvider);
        numberSerializer.serialize(BigInteger.ONE, jsonGenerator, serializerProvider);
        numberSerializer.serialize(1, jsonGenerator, serializerProvider);
        numberSerializer.serialize(1L, jsonGenerator, serializerProvider);
        numberSerializer.serialize(1.0, jsonGenerator, serializerProvider);
        numberSerializer.serialize(1.0f, jsonGenerator, serializerProvider);
        numberSerializer.serialize(new Byte((byte) 1), jsonGenerator, serializerProvider);
        numberSerializer.serialize(new Short((short) 1), jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expectedOutput = "1,\"1.0\",1.0,1.0,\"1\",1,1,1";
        String actualOutput = jsonGenerator.getOutputString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDeserializeNumber() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper numberMapper = objectMapper.copy();
        JsonParser jsonParser = objectMapper.getFactory().createParser("1,\"1.0\",1.0,1.0,\"1\",1,1,1");

        Number deserializedNumber;
        deserializedNumber = numberMapper.readValue(jsonParser, Number.class);
        assertEquals(BigDecimal.ONE, deserializedNumber);

        numberMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        deserializedNumber = numberMapper.readValue(jsonParser, BigDecimal.class);
        assertEquals(BigDecimal.ONE, deserializedNumber);

        deserializedNumber = numberMapper.readValue(jsonParser, BigInteger.class);
        assertEquals(BigInteger.ONE, deserializedNumber);

        deserializedNumber = numberMapper.readValue(jsonParser, Integer.class);
        assertEquals(1, deserializedNumber.intValue());

        deserializedNumber = numberMapper.readValue(jsonParser, Long.class);
        assertEquals(1L, deserializedNumber.longValue());

        deserializedNumber = numberMapper.readValue(jsonParser, Double.class);
        assertEquals(1.0, deserializedNumber.doubleValue(), 0.0);

        deserializedNumber = numberMapper.readValue(jsonParser, Float.class);
        assertEquals(1.0f, deserializedNumber, 0.0f);

        deserializedNumber = numberMapper.readValue(jsonParser, Byte.class);
        assertEquals((byte) 1, deserializedNumber);

        deserializedNumber = numberMapper.readValue(jsonParser, Short.class);
        assertEquals((short) 1, deserializedNumber);

        jsonParser.close();
    }

    @Test
    public void testDeserializeComplexNumber() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper numberMapper = objectMapper.copy();
        ObjectMapper complexMapper = objectMapper.copy();

        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"number\": 1,\"bigDecimal\": 1.5,\"bigInteger\": -123456789012345678901234567890,\"integer\": 1,\"long\": 123456789012345678901234567890,\"double\": 123.456789,\"float\": 123.456789f,\"byte\": 1,\"short\": 1}");

        Number deserializedNumber;
        deserializedNumber = numberMapper.readValue(jsonParser, Number.class);
        assertEquals(BigDecimal.valueOf(1.5), deserializedNumber);

        deserializedNumber = complexMapper.readValue(jsonParser, BigDecimal.class);
        assertEquals(BigDecimal.valueOf(1.5), deserializedNumber);

        deserializedNumber = complexMapper.readValue(jsonParser, BigInteger.class);
        assertEquals(BigInteger.valueOf(-123456789012345678901234567890L), deserializedNumber);

        deserializedNumber = numberMapper.readValue(jsonParser, Integer.class);
        assertEquals(1, deserializedNumber.intValue());

        deserializedNumber = numberMapper.readValue(jsonParser, Long.class);
        assertEquals(123456789012345678901234567890L, deserializedNumber.longValue());

        deserializedNumber = complexMapper.readValue(jsonParser, Double.class);
        assertEquals(123.456789, deserializedNumber, 0.0);

        deserializedNumber = complexMapper.readValue(jsonParser, Float.class);
        assertEquals(123.456789f, deserializedNumber, 0.0f);

        deserializedNumber = numberMapper.readValue(jsonParser, Byte.class);
        assertEquals((byte) 1, deserializedNumber);

        deserializedNumber = numberMapper.readValue(jsonParser, Short.class);
        assertEquals((short) 1, deserializedNumber);

        jsonParser.close();
    }

    @Test
    public void testJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.getSerializerProvider().getJsonFactory()
                .getJsonFormatVisitor(null, NumberSerializer.class, null);

        if (NumberSerializer.instance._isInt) {
            jsonFormatVisitorWrapper.visitIntFormat(null, null, JsonParser.NumberType.INT);
        } else {
            jsonFormatVisitorWrapper.expectNumberFormat(null);
        }
    }

    @JsonSerialize(using = NumberSerializer.class)
    @JsonDeserialize(using = NumberSerializer.class)
    static class TestNumber {
        private Number number;

        public Number getNumber() {
            return number;
        }

        public void setNumber(Number number) {
            this.number = number;
        }

        public TypeSerializer getNumberTypeSerializer() {
            return null;
        }
    }
}