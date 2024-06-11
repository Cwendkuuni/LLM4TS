package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.util.StandardDeserializationContext;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValueInstantiatorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonFactory JSON_FACTORY = MAPper.getFactory();

    @Test
    public void testDefaultCreator() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        Object result = instantiator.createUsingDefault(new StandardDeserializationContext(JSON_FACTORY, null));
        assertNull(result);
    }

    @Test
    public void testStringCreator() throws Exception {
        class MyStringValue {
            @JsonCreator
            public MyStringValue(@JsonProperty("value") String value) {
                this.value = value;
            }

            private String value;
        }

        ValueInstantiator instantiator = new ValueInstantiator.Base(MyStringValue.class);
        Object result = instantiator.createFromString(new StandardDeserializationContext(JSON_FACTORY, null), "test");
        assertEquals("test", result);
    }

    @Test(expected = IOException.class)
    public void testMissingCreator() throws Exception {
        class MyMissingValue {
            // no creator defined
        }

        ValueInstantiator instantiator = new ValueInstantiator.Base(MyMissingValue.class);
        instantiator.createFromString(new StandardDeserializationContext(JSON_FACTORY, null), "test");
    }

    @Test
    public void testIntCreator() throws Exception {
        class MyIntValue {
            @JsonCreator
            public MyIntValue(@JsonProperty("value") int value) {
                this.value = value;
            }

            private int value;
        }

        ValueInstantiator instantiator = new ValueInstantiator.Base(MyIntValue.class);
        Object result = instantiator.createFromInt(new StandardDeserializationContext(JSON_FACTORY, null), 42);
        assertEquals(42, result);
    }

    @Test(expected = IOException.class)
    public void testMissingIntCreator() throws Exception {
        class MyMissingIntValue {
            // no int creator defined
        }

        ValueInstantiator instantiator = new ValueInstantiator.Base(MyMissingIntValue.class);
        instantiator.createFromInt(new StandardDeserializationContext(JSON_FACTORY, null), 42);
    }

    // add similar tests for Long, Double, Boolean and other methods

    @Test
    public void testCreateFromObjectWith() throws Exception {
        class MyObjectValue {
            private String property;

            @JsonCreator
            public MyObjectValue(@JsonProperty("property") String property) {
                this.property = property;
            }
        }

        ValueInstantiator instantiator = new ValueInstantiator.Base(MyObjectValue.class);
        Object[] args = new Object[]{"test"};
        Object result = instantiator.createFromObjectWith(new StandardDeserializationContext(JSON_FACTORY, null), args);
        assertEquals("test", result);
    }

    @Test(expected = IOException.class)
    public void testMissingCreateFromObjectWith() throws Exception {
        class MyMissingObjectValue {
            // no createFromObjectWith defined
        }

        ValueInstantiator instantiator = new ValueInstantiator.Base(MyMissingObjectValue.class);
        Object[] args = new Object[]{"test"};
        instantiator.createFromObjectWith(new StandardDeserializationContext(JSON_FACTORY, null), args);
    }

    // add similar tests for createUsingDelegate, createUsingArrayDelegate, and other methods
}