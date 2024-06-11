package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ValueInstantiatorTest {

    private static class TestValueInstantiator extends ValueInstantiator {
        @JsonCreator
        public static TestValue createFromObject(@JsonProperty("foo") int i, @JsonProperty("bar") String s) {
            return new TestValue(i, s);
        }

        @Override
        public boolean canCreateFromObjectWith() {
            return true;
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return new SettableBeanProperty[]{
                    new SettableBeanProperty.Std("foo", Integer.TYPE),
                    new SettableBeanProperty.Std("bar", String.class)
            };
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            return createFromObject((Integer) args[0], (String) args[1]);
        }
    }

    private static class TestValue {
        private final int foo;
        private final String bar;

        public TestValue(int foo, String bar) {
            this.foo = foo;
            this.bar = bar;
        }

        public int getFoo() {
            return foo;
        }

        public String getBar() {
            return bar;
        }

        @Override
        public String toString() {
            return "TestValue{" +
                    "foo=" + foo +
                    ", bar='" + bar + '\'' +
                    '}';
        }
    }

    @Test
    public void testValueInstantiator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        ValueInstantiator instantiator = new TestValueInstantiator(Integer.TYPE);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateFromInt());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateUsingArrayDelegate());

        TestValue result = mapper.readValue("{\"foo\":1,\"bar\":\"abc\"}", instantiator.getType());
        assertEquals(new TestValue(1, "abc"), result);

        instantiator = new TestValueInstantiator(String.class);
        assertTrue(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateFromInt());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateUsingArrayDelegate());

        result = mapper.readValue("\"abc\"", instantiator.getType());
        assertEquals("abc", result);

        instantiator = new TestValueInstantiator(Integer[].class);
        assertFalse(instantiator.canInstantiate());
        assertFalse(instantiator.canCreateFromInt());
        assertFalse(instantiator.canCreateFromString());
        assertFalse(instantiator.canCreateUsingDefault());
        assertFalse(instantiator.canCreateUsingDelegate());
        assertFalse(instantiator.canCreateUsingArrayDelegate());

        instantiator = new TestValueInstantiator(TestValue.class);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromObjectWith());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getArrayDelegateCreator());
        assertNull(instantiator.getWithArgsCreator());
        assertNull(instantiator.getIncompleteParameter());

        DeserializationConfig config = mapper.getConfig();
        SettableBeanProperty[] properties = instantiator.getFromObjectArguments(config);
        assertEquals(2, properties.length);

        result = mapper.readValue("{\"foo\":1,\"bar\":\"abc\"}", instantiator.getType());
        TestValue expected = new TestValue(1, "abc");
        assertEquals(expected.getFoo(), result.getFoo());
        assertEquals(expected.getBar(), result.getBar());

        instantiator = new Base(TestValue.class);
        assertTrue(instantiator.canInstantiate());
        assertTrue(instantiator.canCreateFromObjectWith());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getArrayDelegateCreator());
        assertNull(instantiator.getWithArgsCreator());
        assertNull(instantiator.getIncompleteParameter());

        result = mapper.readValue("{\"foo\":1,\"bar\":\"abc\"}", instantiator.getType());
        assertEquals(expected.toString(), result.toString());
    }

}