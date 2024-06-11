package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValueInstantiatorTest {

    private static class TestValueInstantiator extends ValueInstantiator.Base {
        public TestValueInstantiator() {
            super(String.class);
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return _createFromStringFallbacks(ctxt, value);
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new String("");
        }
    }

    @Test
    public void testEmptyStringAsNull() throws JsonProcessingException {
        ValueInstantiator instantiator = new TestValueInstantiator();
        ObjectMapper mapper = new ObjectMapper();

        String json = "\"\""; // empty JSON string
        Object result = mapper.readValue(json, instantiator.getType());

        assertNull(result);
    }

    @Test
    public void testStringInstantiation() throws JsonProcessingException {
        ValueInstantiator instantiator = new TestValueInstantiator();
        ObjectMapper mapper = new ObjectMapper();

        String json = "\"hello\"";
        Object result = mapper.readValue(json, instantiator.getType());

        assertEquals("hello", result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testNonStringArgumentConstructor() throws JsonProcessingException {
        ValueInstantiator instantiator = new TestValueInstantiator();
        ObjectMapper mapper = new ObjectMapper();

        String json = "42";
        Object result = mapper.readValue(json, instantiator.getType());

        // this should throw an exception because there's no int constructor
    }

    @Test
    public void testDelegateCreator() {
        // Since the given Java class doesn't specify any default or delegate creators,
        // this test case is left empty.
    }

    @Test
    public void testCreateFromObjectWith() {
        // Since the given Java class doesn't specify a method to create from Object with,
        // this test case is left empty.
    }

    @Test
    public void testCreateUsingArrayDelegate() {
        // Since the given Java class doesn't specify any array delegate creator,
        // this test case is left empty.
    }
}