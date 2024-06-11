package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test cases for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    private static ObjectMapper mapper;

    static {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Number.class, NumberSerializer.instance);
        mapper = new JsonMapper(new ObjectMapper());
        mapper.registerModule(module);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Test serialization of {@link Number} instances.
     *
     * @throws IOException if an error occurs during serialization.
     */
    @org.junit.Test
    public void testSerialization() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Test serialization of various number types
        Number number = new BigDecimal("3.14159265359");
        JsonNode jsonNode = mapper.writeValueAsTree(number, SerializerProvider.StdioSerializerProvider.instance());
        assertEquals("3.14159265359", jsonNode.getTextValue());

        number = new BigInteger("123456789012345678901234567890");
        jsonNode = mapper.writeValueAsTree(number, SerializerProvider.StdioSerializerProvider.instance());
        assertEquals("123456789012345678901234567890", jsonNode.getTextValue());

        number = 123;
        jsonNode = mapper.writeValueAsTree(number, SerializerProvider.StdioSerializerProvider.instance());
        assertEquals("123", jsonNode.getTextValue());

        number = 123L;
        jsonNode = mapper.writeValueAsTree(number, SerializerProvider.StdioSerializerProvider.instance());
        assertEquals("123", jsonNode.getTextValue());

        number = 123.0;
        jsonNode = mapper.writeValueAsTree(number, SerializerProvider.StdioSerializerProvider.instance());
        assertEquals("123.0", jsonNode.getTextValue());

        number = 123.0F;
        jsonNode = mapper.writeValueAsTree(number, SerializerProvider.StdioSerializerProvider.instance());
        assertEquals("123.0", jsonNode.getTextValue());

        number = 123.0d;
        jsonNode = mapper.writeValueAsTree(number, SerializerProvider.StdioSerializerProvider.instance());
        assertEquals("123.0", jsonNode.getTextValue());

        number = 123.0D;
        jsonNode = mapper.writeValueAsTree(number, SerializerProvider.StdioSerializerProvider.instance());
        assertEquals("123.0", jsonNode.getTextValue());

        number = Byte.MIN_VALUE;
        jsonNode = mapper.writeValueAsTree(number, SerializerProvider.StdioSerializerProvider.instance());
        assertEquals("-128", jsonNode.getTextValue());

        number = Short.MIN_VALUE;
        jsonNode = mapper.writeValueAsTree(number, SerializerProvider.StdioSerializerProvider.instance());
        assertEquals("-32768", jsonNode.getTextValue());

        number = Integer.MIN_VALUE;
        jsonNode = mapper.writeValueAsTree(number, SerializerProvider.StdioSerializerProvider.instance());
        assertEquals("-2147483648", jsonNode.getTextValue());

        // Test serialization with custom JsonFormat annotation
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.000")
        Number customNumber = new BigDecimal("3.14159265359");
        jsonNode = mapper.writeValueAsTree(customNumber, SerializerProvider.StdioSerializerProvider.instance());
        assertEquals("3.141", jsonNode.getTextValue());

        // Test serialization with default JsonFormat (number)
        jsonNode = mapper.writeValueAsTree(customNumber, new SerializerProvider.BaseSerializerProvider());
        assertEquals("3.14159265359", jsonNode.getTextValue());

        // Test serialization with null value
        JsonNode nullNode = mapper.writeValueAsTree(null, SerializerProvider.StdioSerializerProvider.instance());
        assertNull(nullNode.getTextValue());
    }

    /**
     * Test deserialization of JSON strings to {@link Number} instances.
     *
     * @throws IOException if an error occurs during deserialization.
     */
    @org.junit.Test
    public void testDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test deserialization of various number types
        String jsonString = "3.14159265359";
        Number number = mapper.readValue(jsonString, Number.class);
        assertEquals(new BigDecimal(jsonString), number);

        jsonString = "123456789012345678901234567890";
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(new BigInteger(jsonString), number);

        jsonString = "123";
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(123, number.intValue());

        jsonString = "123L";
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(123, number.longValue());

        jsonString = "123.0";
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(123.0, number.doubleValue());

        jsonString = "123.0F";
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(123.0f, number.floatValue());

        jsonString = "123.0d";
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(123.0d, number.doubleValue());

        jsonString = "123.0D";
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(123.0d, number.doubleValue());

        jsonString = "-128";
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(Byte.MIN_VALUE, number);

        jsonString = "-32768";
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(Short.MIN_VALUE, number);

        jsonString = "-2147483648";
        number = mapper.readValue(jsonString, Number.class);
        assertEquals(Integer.MIN_VALUE, number);

        // Test deserialization with custom JsonFormat annotation
        String customJsonString = "3.14159265359";
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.000")
        Number customNumber = mapper.readValue(customJsonString, Number.class);
        assertEquals(new BigDecimal("3.141"), customNumber);

        // Test deserialization with default JsonFormat (number)
        String defaultJsonString = "3.14159265359";
        number = mapper.readValue(defaultJsonString, Number.class);
        assertEquals(new BigDecimal(defaultJsonString), number);

        // Test deserialization with null value
        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader("null"));
        number = mapper.readValue(jsonParser, Number.class);
        assertNull(number);
    }

    /**
     * Test serialization and deserialization round trip with JSON strings.
     *
     * @throws IOException if an error occurs during serialization or deserialization.
     */
    @org.junit.Test
    public void testRoundTrip() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test round trip with various number types
        Number number = new BigDecimal("3.14159265359");
        String jsonString = mapper.writeValueAsString(number);
        Number deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(number, deserializedNumber);

        number = new BigInteger("123456789012345678901234567890");
        jsonString = mapper.writeValueAsString(number);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(number, deserializedNumber);

        number = 123;
        jsonString = mapper.writeValueAsString(number);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(number, deserializedNumber);

        number = 123L;
        jsonString = mapper.writeValueAsString(number);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(number, deserializedNumber);

        number = 123.0;
        jsonString = mapper.writeValueAsString(number);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(number, deserializedNumber);

        number = 123.0F;
        jsonString = mapper.writeValueAsString(number);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(number, deserializedNumber);

        number = 123.0d;
        jsonString = mapper.writeValueAsString(number);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(number, deserializedNumber);

        number = 123.0D;
        jsonString = mapper.writeValueAsString(number);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(number, deserializedNumber);

        number = Byte.MIN_VALUE;
        jsonString = mapper.writeValueAsString(number);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(number, deserializedNumber);

        number = Short.MIN_VALUE;
        jsonString = mapper.writeValueAsString(number);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(number, deserializedNumber);

        number = Integer.MIN_VALUE;
        jsonString = mapper.writeValueAsString(number);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(number, deserializedNumber);

        // Test round trip with custom JsonFormat annotation
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.000")
        Number customNumber = new BigDecimal("3.14159265359");
        jsonString = mapper.writeValueAsString(customNumber);
        customNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(customNumber, new BigDecimal("3.141"));

        // Test round trip with default JsonFormat (number)
        jsonString = mapper.writeValueAsString(customNumber);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertEquals(customNumber, deserializedNumber);

        // Test round trip with null value
        jsonString = mapper.writeValueAsString(null);
        deserializedNumber = mapper.readValue(jsonString, Number.class);
        assertNull(deserializedNumber);
    }
}