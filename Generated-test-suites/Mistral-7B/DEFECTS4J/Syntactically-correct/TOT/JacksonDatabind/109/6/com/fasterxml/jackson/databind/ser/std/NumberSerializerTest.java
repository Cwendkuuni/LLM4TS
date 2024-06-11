package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

public class NumberSerializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeNumber() throws JsonProcessingException, JsonGenerationException {
        // Test serialization of various Number types
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        // Test integer types
        int intValue = 123;
        serializer.serialize(intValue, gen, provider);
        gen.flush();
        String intJson = gen.getOutputContent();
        Assert.assertEquals("123", intJson);

        byte byteValue = 45;
        serializer.serialize(byteValue, gen, provider);
        gen.flush();
        String byteJson = gen.getOutputContent();
        Assert.assertEquals("45", byteJson);

        short shortValue = 32767;
        serializer.serialize(shortValue, gen, provider);
        gen.flush();
        String shortJson = gen.getOutputContent();
        Assert.assertEquals("32767", shortJson);

        // Test long types
        long longValue = 12345678901234L;
        serializer.serialize(longValue, gen, provider);
        gen.flush();
        String longJson = gen.getOutputContent();
        Assert.assertEquals("12345678901234", longJson);

        // Test float types
        float floatValue = 123.45f;
        serializer.serialize(floatValue, gen, provider);
        gen.flush();
        String floatJson = gen.getOutputContent();
        Assert.assertEquals("123.45", floatJson);

        // Test double types
        double doubleValue = 123.456789;
        serializer.serialize(doubleValue, gen, provider);
        gen.flush();
        String doubleJson = gen.getOutputContent();
        Assert.assertEquals("123.456789", doubleJson);

        // Test BigInteger and BigDecimal types
        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        serializer.serialize(bigInteger, gen, provider);
        gen.flush();
        String bigIntegerJson = gen.getOutputContent();
        Assert.assertEquals("123456789012345678901234567890", bigIntegerJson);

        BigDecimal bigDecimal = new BigDecimal("123.456789");
        serializer.serialize(bigDecimal, gen, provider);
        gen.flush();
        String bigDecimalJson = gen.getOutputContent();
        Assert.assertEquals("123.456789", bigDecimalJson);

        // Test other Number types
        BigDecimal bd = new BigDecimal("1.2E9");
        serializer.serialize(bd, gen, provider);
        gen.flush();
        String bdJson = gen.getOutputContent();
        Assert.assertEquals("1200000000000.0", bdJson);

        Double d = new Double("NAN");
        serializer.serialize(d, gen, provider);
        gen.flush();
        String nanJson = gen.getOutputContent();
        Assert.assertEquals("NaN", nanJson);

        // Test null value
        serializer.serialize(null, gen, provider);
        gen.flush();
        String nullJson = gen.getOutputContent();
        Assert.assertEquals("null", nullJson);
    }

    @Test
    public void testDeserializeNumber() throws JsonProcessingException {
        // Test deserialization of various Number types
        ObjectMapper mapper = new ObjectMapper();

        // Test integer types
        JsonParser parser = mapper.getFactory().createParser("123");
        Number number = mapper.readValue(parser, Number.class);
        Assert.assertEquals(123, number.intValue());

        // Test long types
        parser = mapper.getFactory().createParser("12345678901234");
        number = mapper.readValue(parser, Number.class);
        Assert.assertEquals(12345678901234L, number.longValue());

        // Test float types
        parser = mapper.getFactory().createParser("123.45");
        number = mapper.readValue(parser, Number.class);
        Assert.assertEquals(123.45f, number, 0.001f);

        // Test double types
        parser = mapper.getFactory().createParser("123.456789");
        number = mapper.readValue(parser, Number.class);
        Assert.assertEquals(123.456789, number, 0.00001);

        // Test BigInteger and BigDecimal types
        parser = mapper.getFactory().createParser("123456789012345678901234567890");
        number = mapper.readValue(parser, Number.class);
        Assert.assertTrue(number instanceof BigInteger);

        parser = mapper.getFactory().createParser("123.456789");
        number = mapper.readValue(parser, Number.class);
        Assert.assertTrue(number instanceof BigDecimal);

        // Test other Number types
        parser = mapper.getFactory().createParser("1.2E9");
        number = mapper.readValue(parser, Number.class);
        Assert.assertTrue(number instanceof BigDecimal);

        // Test null value
        parser = mapper.getFactory().createParser("null");
        number = mapper.readValue(parser, Number.class);
        Assert.assertEquals(0, number.intValue());

        // Test custom format
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "currency")
        class CustomNumber {
            private BigDecimal value;
            private Currency currency;

            public CustomNumber(BigDecimal value, Currency currency) {
                this.value = value;
                this.currency = currency;
            }

            public BigDecimal getValue() {
                return value;
            }

            public Currency getCurrency() {
                return currency;
            }
        }

        JsonNode rootNode = JsonNodeFactory.instance.objectNode();
        rootNode.set("number", JsonNodeFactory.instance.numberNode(123456789012345678901234567890L));
        rootNode.set("currency", JsonNodeFactory.instance.valueNode("USD"));

        CustomNumber customNumber = mapper.readValue(rootNode, CustomNumber.class);
        Assert.assertEquals(123456789012345678901234567890L, customNumber.getValue().longValue());
        Assert.assertEquals(Currency.getInstance("USD"), customNumber.getCurrency());
    }
}