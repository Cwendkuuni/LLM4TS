package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdDelegatingSerializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testSerializingStringWithStringConverter() throws JsonProcessingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(
                new StringToObjectConverter(),
                Object.class.getType(),
                MAPPER.findValueSerializer(Object.class)
        );

        ObjectNode result = MAPPER.writeValueAsTree(serializer, "test");
        Object delegateValue = serializer.getDelegatee().deserialize(MAPper, result);
        assertEquals("test", delegateValue);
    }

    @Test
    public void testSerializingNullWithStringConverter() throws JsonProcessingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(
                new StringToObjectConverter(),
                Object.class.getType(),
                MAPPER.findValueSerializer(Object.class)
        );

        ObjectNode result = MAPPER.writeValueAsTree(serializer, null);
        Object delegateValue = serializer.getDelegatee().deserialize(MAPper, result);
        assertNull(delegateValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUsingSameConverter() throws JsonProcessingException {
        new StdDelegatingSerializer(
                new StringToObjectConverter(),
                String.class,
                MAPPER.findValueSerializer(String.class)
        );
    }

    @Test
    public void testSerializingIntegerWithIntegerConverter() throws JsonProcessingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(
                new IntegerToObjectConverter(),
                int[].class.getType(),
                MAPPER.findValueSerializer(int[].class)
        );

        ObjectNode result = MAPPER.writeValueAsTree(serializer, 1);
        Object delegateValue = serializer.getDelegatee().deserialize(MAPper, result);
        assertEquals(1, delegateValue);
    }

    @Test
    public void testSerializingNullWithIntegerConverter() throws JsonProcessingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(
                new IntegerToObjectConverter(),
                int[].class.getType(),
                MAPPER.findValueSerializer(int[].class)
        );

        ObjectNode result = MAPPER.writeValueAsTree(serializer, null);
        Object delegateValue = serializer.getDelegatee().deserialize(MAPper, result);
        assertNull(delegateValue);
    }

    private static class StringToObjectConverter implements Converter<String, Object> {
        @Override
        public Object convert(String text) {
            return text;
        }
    }

    private static class IntegerToObjectConverter implements Converter<Integer, Object> {
        @Override
        public Object convert(Integer value) {
            return value;
        }
    }
}