package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Test;

public class ValueInstantiatorTest {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDefaultInstantiator() throws Exception {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        ObjectNode node = mapper.createObjectNode();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = instantiator.createUsingDefault(ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals("default instance", result.toString());
    }

    @Test
    public void testFromObjectWith() throws Exception {
        ValueInstantiator instantiator = new TestObjectInstantiator();

        ObjectNode node = mapper.createObjectNode();
        node.put("prop1", "value1");
        node.put("prop2", 123);

        Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), node.fields());
        Assert.assertEquals("ValueObject{prop1='value1', prop2=123}", result.toString());
    }

    @Test
    public void testFromString() throws Exception {
        ValueInstantiator instantiator = new TestStringInstantiator();

        Object result = instantiator.createFromString(mapper.getDeserializationContext(), "test");
        Assert.assertEquals("test", result);
    }

    @Test
    public void testFromInt() throws Exception {
        ValueInstantiator instantiator = new TestIntInstantiator();

        Object result = instantiator.createFromInt(mapper.getDeserializationContext(), 42);
        Assert.assertEquals(42, result.hashCode());
        Assert.assertEquals("IntValue{value=42}", result.toString());
    }

    @Test
    public void testFromLong() throws Exception {
        ValueInstantiator instantiator = new TestLongInstantiator();

        Object result = instantiator.createFromLong(mapper.getDeserializationContext(), 42L);
        Assert.assertEquals(42L, result.hashCode());
        Assert.assertEquals("LongValue{value=42}", result.toString());
    }

    @Test
    public void testFromDouble() throws Exception {
        ValueInstantiator instantiator = new TestDoubleInstantiator();

        Object result = instantiator.createFromDouble(mapper.getDeserializationContext(), 3.14);
        Assert.assertEquals(3.14, result.hashCode(), 0.001);
        Assert.assertEquals("DoubleValue{value=3.14}", result.toString());
    }

    @Test
    public void testFromBoolean() throws Exception {
        ValueInstantiator instantiator = new TestBooleanInstantiator();

        Object result = instantiator.createFromBoolean(mapper.getDeserializationContext(), true);
        Assert.assertEquals(true, result.hashCode());
        Assert.assertEquals("BooleanValue{value=true}", result.toString());
    }

    @Test
    public void testMissingInstantiator() throws Exception {
        ValueInstantiator instantiator = new TestMissingInstantiator();

        ObjectNode node = mapper.createObjectNode();
        node.put("value", new Object());

        try {
            instantiator.createFromObjectWith(mapper.getDeserializationContext(), node.fields());
            Assert.fail("Expected MissingInstantiatorException");
        } catch (JsonProcessingException e) {
            Assert.assertEquals("com.fasterxml.jackson.databind.JsonMappingException: No value instantiaitor found for value [Object] of type [class java.lang.Object]", e.getMessage());
        }
    }

    @Test
    public void testMissingCreatorAnnotations() throws Exception {
        ValueInstantiator instantiator = new TestMissingCreatorAnnotations();

        ObjectNode node = mapper.createObjectNode();
        node.put("value", new Object());

        try {
            instantiator.createFromObjectWith(mapper.getDeserializationContext(), node.fields());
            Assert.fail("Expected MissingInstantiatorException");
        } catch (JsonProcessingException e) {
            Assert.assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Missing creator annotations for ValueInstantiator", e.getMessage());
        }
    }

    @Test
    public void testMissingMethod() throws Exception {
        ValueInstantiator instantiator = new TestMissingMethod();

        try {
            instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[] {});
            Assert.fail("Expected MissingMethodException");
        } catch (IllegalAccessException e) {
            // expected
        }
    }

    public static class TestObjectInstantiator extends ValueInstantiator {
        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return new SettableBeanProperty[] { new TestProperty() };
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            return new ValueObject((String) args[0]);
        }

        static class TestProperty extends SettableBeanProperty {
            public TestProperty() {
                super("prop1", String.class, null);
            }
        }

        static class ValueObject {
            private String prop1;

            public ValueObject(String prop1) {
                this.prop1 = prop1;
            }

            @Override
            public String toString() {
                return "ValueObject{" + prop1 + '}';
            }
        }
    }

    public static class TestStringInstantiator extends ValueInstantiator {
        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return value;
        }
    }

    public static class TestIntInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromInt() {
            return true;
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return new IntValue(value);
        }

        static class IntValue {
            private int value;

            public IntValue(int value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "IntValue{value=" + value + '}';
            }
        }
    }

    public static class TestLongInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromLong() {
            return true;
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return new LongValue(value);
        }

        static class LongValue {
            private long value;

            public LongValue(long value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "LongValue{value=" + value + '}';
            }
        }
    }

    public static class TestDoubleInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromDouble() {
            return true;
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return new DoubleValue(value);
        }

        static class DoubleValue {
            private double value;

            public DoubleValue(double value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "DoubleValue{value=" + value + '}';
            }
        }
    }

    public static class TestBooleanInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromBoolean() {
            return true;
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return new BooleanValue(value);
        }

        static class BooleanValue {
            private boolean value;

            public BooleanValue(boolean value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "BooleanValue{value=" + value + '}';
            }
        }
    }

    public static class TestMissingInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateUsingDefault() {
            return false;
        }
    }

    public static class TestMissingCreatorAnnotations extends ValueInstantiator {
        @Override
        public AnnotatedWithParams getWithArgsCreator() {
            return null;
        }

        @Override
        public boolean canCreateFromObjectWith() {
            return false;
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return new SettableBeanProperty[0];
        }
    }

    public static class TestMissingMethod extends ValueInstantiator {
        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            throw new MissingMethodException();
        }
    }

    static class MissingMethodException extends RuntimeException {
        public MissingMethodException() {}
    }
}