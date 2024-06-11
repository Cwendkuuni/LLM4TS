package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ValueInstantiatorTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultInstantiator() throws JsonProcessingException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Object.class);

        assertEquals("default instantiator test", instantiator.createUsingDefault(mapper.getDeserializationContext()));
    }

    @Test
    public void testCreateFromString() throws JsonProcessingException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class) {
            @Override
            public boolean canCreateFromString() { return true; }

            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                return value.toUpperCase();
            }
        };

        assertEquals("TEST", instantiator.createFromString(mapper.getDeserializationContext(), "test"));
    }

    @Test
    public void testCreateFromInt() throws JsonProcessingException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Integer.class) {
            @Override
            public boolean canCreateFromInt() { return true; }

            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return value + 1;
            }
        };

        assertEquals(2, instantiator.createFromInt(mapper.getDeserializationContext(), 1));
    }

    @Test
    public void testCreateFromDelegate() throws JsonProcessingException {
        class Delegate {
            @JsonCreator
            public Delegate(@JsonProperty("str") String str) {
                this.str = str;
            }

            String str;
        }

        ValueInstantiator instantiator = new ValueInstantiator.Base(Delegate.class) {
            @Override
            public boolean canCreateUsingDelegate() { return true; }

            @Override
            public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
                Delegate d = (Delegate) delegate;
                return d.str.toUpperCase();
            }
        };

        assertEquals("TEST", instantiator.createUsingDelegate(mapper.getDeserializationContext(), new Delegate("test")));
    }

    @Test(expected = RuntimeException.class)
    public void testMissingCreator() throws JsonProcessingException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Integer[].class);

        instantiator.createUsingDefault(mapper.getDeserializationContext());
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidCreator() throws JsonProcessingException {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class) {
            @Override
            public boolean canCreateUsingDefault() { return false; }

            @Override
            public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
                throw new RuntimeException("dummy error");
            }

            // this is wrong, but will be detected at runtime
            @Override
            public boolean canCreateFromInt() { return true; }
        };

        instantiator.createFromInt(mapper.getDeserializationContext(), 1);
    }
}