package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        BigInteger bigInteger = BigInteger.valueOf(12345);
        JsonSerializer<JsonNode> jsonSerializer = mapper.getSerializerProvider().findValueSerializer(JsonNode.class);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        String result = mapper.writeValueAsString(new Object() {
            @JsonSerialize(using = NumberSerializer.class)
            private BigInteger number;

            public BigInteger getNumber() {
                return number;
            }

            public void setNumber(BigInteger number) {
                this.number = number;
            }
        }, serializerProvider, null);

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode jsonNode = parser.readValueAs(JsonNode.class);
        Object deserialized = jsonSerializer.deserialize(jsonNode, JsonNode.class, serializerProvider);
        BigInteger deserializedBigInteger = (BigInteger) deserialized;

        assertEquals(bigInteger, deserializedBigInteger);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(bigInteger, generator, serializerProvider);
        generator.flush();
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        BigDecimal bigDecimal = new BigDecimal("12345.67");
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        String result = mapper.writeValueAsString(new Object() {
            @JsonSerialize(using = NumberSerializer.class)
            private BigDecimal number;

            public BigDecimal getNumber() {
                return number;
            }

            public void setNumber(BigDecimal number) {
                this.number = number;
            }
        }, serializerProvider, null);

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode jsonNode = parser.readValueAs(JsonNode.class);
        Object deserialized = serializer.deserialize(jsonNode, JsonNode.class, serializerProvider);
        BigDecimal deserializedBigDecimal = (BigDecimal) deserialized;

        assertEquals(bigDecimal, deserializedBigDecimal);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(bigDecimal, generator, serializerProvider);
        generator.flush();
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        Long longValue = 12345L;
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        String result = mapper.writeValueAsString(new Object() {
            @JsonSerialize(using = NumberSerializer.class)
            private Long number;

            public Long getNumber() {
                return number;
            }

            public void setNumber(Long number) {
                this.number = number;
            }
        }, serializerProvider, null);

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode jsonNode = parser.readValueAs(JsonNode.class);
        Object deserialized = serializer.deserialize(jsonNode, JsonNode.class, serializerProvider);
        Long deserializedLong = (Long) deserialized;

        assertEquals(longValue, deserializedLong);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(longValue, generator, serializerProvider);
        generator.flush();
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        Double doubleValue = 12345.67;
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        String result = mapper.writeValueAsString(new Object() {
            @JsonSerialize(using = NumberSerializer.class)
            private Double number;

            public Double getNumber() {
                return number;
            }

            public void setNumber(Double number) {
                this.number = number;
            }
        }, serializerProvider, null);

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode jsonNode = parser.readValueAs(JsonNode.class);
        Object deserialized = serializer.deserialize(jsonNode, JsonNode.class, serializerProvider);
        Double deserializedDouble = (Double) deserialized;

        assertEquals(doubleValue, deserializedDouble, 0.0);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(doubleValue, generator, serializerProvider);
        generator.flush();
    }

    @Test
    public void testSerializeInteger() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        Integer integerValue = 12345;
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        String result = mapper.writeValueAsString(new Object() {
            @JsonSerialize(using = NumberSerializer.class)
            private Integer number;

            public Integer getNumber() {
                return number;
            }

            public void setNumber(Integer number) {
                this.number = number;
            }
        }, serializerProvider, null);

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode jsonNode = parser.readValueAs(JsonNode.class);
        Object deserialized = serializer.deserialize(jsonNode, JsonNode.class, serializerProvider);
        Integer deserializedInteger = (Integer) deserialized;

        assertEquals(integerValue, deserializedInteger);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(integerValue, generator, serializerProvider);
        generator.flush();
    }

    @Test
    public void testSerializeByte() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        Byte byteValue = (byte) 123;
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        String result = mapper.writeValueAsString(new Object() {
            @JsonSerialize(using = NumberSerializer.class)
            private Byte number;

            public Byte getNumber() {
                return number;
            }

            public void setNumber(Byte number) {
                this.number = number;
            }
        }, serializerProvider, null);

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode jsonNode = parser.readValueAs(JsonNode.class);
        Object deserialized = serializer.deserialize(jsonNode, JsonNode.class, serializerProvider);
        Byte deserializedByte = (Byte) deserialized;

        assertEquals(byteValue, deserializedByte);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(byteValue, generator, serializerProvider);
        generator.flush();
    }

    @Test
    public void testSerializeShort() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        Short shortValue = (short) 123;
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        String result = mapper.writeValueAsString(new Object() {
            @JsonSerialize(using = NumberSerializer.class)
            private Short number;

            public Short getNumber() {
                return number;
            }

            public void setNumber(Short number) {
                this.number = number;
            }
        }, serializerProvider, null);

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode jsonNode = parser.readValueAs(JsonNode.class);
        Object deserialized = serializer.deserialize(jsonNode, JsonNode.class, serializerProvider);
        Short deserializedShort = (Short) deserialized;

        assertEquals(shortValue, deserializedShort);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(shortValue, generator, serializerProvider);
        generator.flush();
    }

    @Test
    public void testSerializeFloat() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        Float floatValue = 12345.67f;
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        String result = mapper.writeValueAsString(new Object() {
            @JsonSerialize(using = NumberSerializer.class)
            private Float number;

            public Float getNumber() {
                return number;
            }

            public void setNumber(Float number) {
                this.number = number;
            }
        }, serializerProvider, null);

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode jsonNode = parser.readValueAs(JsonNode.class);
        Object deserialized = serializer.deserialize(jsonNode, JsonNode.class, serializerProvider);
        Float deserializedFloat = (Float) deserialized;

        assertEquals(floatValue, deserializedFloat, 0.0f);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(floatValue, generator, serializerProvider);
        generator.flush();
    }

    @Test
    public void testSerializeCustomNumber() throws JsonGenerationException, JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        class CustomNumber {
            @JsonSerialize(using = NumberSerializer.class)
            private Number number;

            public Number getNumber() {
                return number;
            }

            public void setNumber(Number number) {
                this.number = number;
            }
        }

        CustomNumber customNumber = new CustomNumber();
        customNumber.setNumber(new BigDecimal("12345.67"));

        String result = mapper.writeValueAsString(customNumber, serializerProvider, null);

        JsonParser parser = mapper.getFactory().createParser(result);
        JsonNode jsonNode = parser.readValueAs(JsonNode.class);
        Object deserialized = serializer.deserialize(jsonNode, JsonNode.class, serializerProvider);
        BigDecimal deserializedCustomNumber = (BigDecimal) deserialized;

        assertEquals(customNumber.getNumber(), deserializedCustomNumber);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(customNumber.getNumber(), generator, serializerProvider);
        generator.flush();
    }
}