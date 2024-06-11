package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;

        Integer input = 123;
        String json = mapper.writeValueAsString(input, integerSerializer);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(input, parser.readValueAs(Integer.class));

        // Edge case: minimum value of int
        input = Integer.MIN_VALUE;
        json = mapper.writeValueAsString(input, integerSerializer);
        parser = mapper.getFactory().createParser(json);
        assertEquals(input, parser.readValueAs(Integer.class));

        // Error scenario: null input
        try {
            parser = mapper.getFactory().createParser(JsonFactory.nullValue());
            parser.readValueAs(Integer.class);
            fail("Expected JsonMappingException but got none");
        } catch (JsonMappingException e) {
            // expected
        }

        // Error scenario: non-numeric input
        try {
            parser = mapper.getFactory().createParser("\"abc\"");
            parser.readValueAs(Integer.class);
            fail("Expected JsonMappingException but got none");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;

        Short input = Short.MIN_VALUE;
        String json = mapper.writeValueAsString(input, shortSerializer);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(input, parser.readValueAs(Short.class));

        // Edge case: minimum value of short
        input = Short.MIN_VALUE;
        json = mapper.writeValueAsString(input, shortSerializer);
        parser = mapper.getFactory().createParser(json);
        assertEquals(input, parser.readValueAs(Short.class));

        // Error scenario: null input
        try {
            parser = mapper.getFactory().createParser(JsonFactory.nullValue());
            parser.readValueAs(Short.class);
            fail("Expected JsonMappingException but got none");
        } catch (JsonMappingException e) {
            // expected
        }

        // Error scenario: non-numeric input
        try {
            parser = mapper.getFactory().createParser("\"abc\"");
            parser.readValueAs(Short.class);
            fail("Expected JsonMappingException but got none");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;

        Long input = Long.MAX_VALUE;
        String json = mapper.writeValueAsString(input, longSerializer);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(input, parser.readValueAs(Long.class));

        // Edge case: maximum value of long
        input = Long.MAX_VALUE;
        json = mapper.writeValueAsString(input, longSerializer);
        parser = mapper.getFactory().createParser(json);
        assertEquals(input, parser.readValueAs(Long.class));

        // Error scenario: null input
        try {
            parser = mapper.getFactory().createParser(JsonFactory.nullValue());
            parser.readValueAs(Long.class);
            fail("Expected JsonMappingException but got none");
        } catch (JsonMappingException e) {
            // expected
        }

        // Error scenario: non-numeric input
        try {
            parser = mapper.getFactory().createParser("\"abc\"");
            parser.readValueAs(Long.class);
            fail("Expected JsonMappingException but got none");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;

        Float input = Float.POSITIVE_INFINITY;
        String json = mapper.writeValueAsString(input, floatSerializer);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(input, parser.readValueAs(Float.class));

        // Edge case: positive infinity
        input = Float.POSITIVE_INFINITY;
        json = mapper.writeValueAsString(input, floatSerializer);
        parser = mapper.getFactory().createParser(json);
        assertEquals(input, parser.readValueAs(Float.class));

        // Error scenario: null input
        try {
            parser = mapper.getFactory().createParser(JsonFactory.nullValue());
            parser.readValueAs(Float.class);
            fail("Expected JsonMappingException but got none");
        } catch (JsonMappingException e) {
            // expected
        }

        // Error scenario: non-numeric input
        try {
            parser = mapper.getFactory().createParser("\"abc\"");
            parser.readValueAs(Float.class);
            fail("Expected JsonMappingException but got none");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;

        Double input = Double.POSITIVE_INFINITY;
        String json = mapper.writeValueAsString(input, doubleSerializer);
        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(input, parser.readValueAs(Double.class));

        // Edge case: positive infinity
        input = Double.POSITIVE_INFINITY;
        json = mapper.writeValueAsString(input, doubleSerializer);
        parser = mapper.getFactory().createParser(json);
        assertEquals(input, parser.readValueAs(Double.class));

        // Error scenario: null input
        try {
            parser = mapper.getFactory().createParser(JsonFactory.nullValue());
            parser.readValueAs(Double.class);
            fail("Expected JsonMappingException but got none");
        } catch (JsonMappingException e) {
            // expected
        }

        // Error scenario: non-numeric input
        try {
            parser = mapper.getFactory().createParser("\"abc\"");
            parser.readValueAs(Double.class);
            fail("Expected JsonMappingException but got none");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}