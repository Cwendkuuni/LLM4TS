package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class ValueInstantiatorTest {

    private static ValueInstantiator getTestInstance() {
        return new BaseValueInstantiator();
    }

    @org.junit.Test
    public void testGetValueClass() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertEquals(Object.class, instantiator.getValueClass());
    }

    @org.junit.Test
    public void testGetValueTypeDesc() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertEquals("Object", instantiator.getValueTypeDesc());
    }

    @org.junit.Test
    public void testCanInstantiate() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canInstantiate());
    }

    @org.junit.Test
    public void testCanCreateUsingDefault() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateUsingDefault());

        Object value = instantiator.createUsingDefault(new TestContext());
        assertNull(value);
    }

    @org.junit.Test
    public void testCanCreateUsingDelegate() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateUsingDelegate());

        Object delegate = new Object();
        Object value = instantiator.createUsingDelegate(new TestContext(), delegate);
        assertNull(value);
    }

    @org.junit.Test
    public void testCanCreateFromString() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateFromString());

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "StringCreator");
        ObjectNode argNode = node.putObject("args");
        argNode.put("value", "test");

        Object value = instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
        assertNull(value);
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testCreateFromStringInvalidType() throws IOException {
        ValueInstantiator instantiator = getTestInstance();

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "InvalidTypeCreator");

        instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
    }

    @org.junit.Test
    public void testCreateFromInt() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateFromInt());

        int value = 42;
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "IntCreator");
        node.put("value", value);

        Object result = instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
        assertNotNull(result);
        assertEquals(Integer.valueOf(value), result);
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testCreateFromIntInvalidType() throws IOException {
        ValueInstantiator instantiator = getTestInstance();

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "InvalidTypeCreator");

        instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
    }

    @org.junit.Test
    public void testCreateFromLong() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateFromLong());

        long value = 42L;
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "LongCreator");
        node.put("value", value);

        Object result = instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
        assertNotNull(result);
        assertEquals(Long.valueOf(value), result);
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testCreateFromLongInvalidType() throws IOException {
        ValueInstantiator instantiator = getTestInstance();

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "InvalidTypeCreator");

        instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
    }

    @org.junit.Test
    public void testCreateFromDouble() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateFromDouble());

        double value = 42.0;
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "DoubleCreator");
        node.put("value", value);

        Object result = instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
        assertNotNull(result);
        assertEquals(Double.valueOf(value), result);
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testCreateFromDoubleInvalidType() throws IOException {
        ValueInstantiator instantiator = getTestInstance();

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "InvalidTypeCreator");

        instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
    }

    @org.junit.Test
    public void testCreateFromBoolean() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateFromBoolean());

        boolean value = true;
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "BooleanCreator");
        node.put("value", value);

        Object result = instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
        assertNotNull(result);
        assertEquals(Boolean.valueOf(value), result);
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testCreateFromBooleanInvalidType() throws IOException {
        ValueInstantiator instantiator = getTestInstance();

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "InvalidTypeCreator");

        instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
    }

    @org.junit.Test
    public void testCreateFromObjectWith() throws IOException {
        ValueInstantiator instantiator = getTestInstance();

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "StringObjectCreator");
        node.put("args", new ObjectMapper().createObjectNode().putPOJO("arg1", "test1"));

        Object result = instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
        assertNotNull(result);
        assertEquals("test1", result.toString());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testCreateFromObjectWithMissingType() throws IOException {
        ValueInstantiator instantiator = getTestInstance();

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "MissingTypeCreator");
        node.put("args", new ObjectMapper().createObjectNode().putPOJO("arg1", "test1"));

        instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
    }

    @org.junit.Test
    public void testCreateFromObjectWithMissingArg() throws IOException {
        ValueInstantiator instantiator = getTestInstance();

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("type", "MissingArgCreator");
        node.put("args", new ObjectMapper().createObjectNode().putPOJO("arg2", "test2"));

        try {
            instantiator.createFromObjectWith(new TestContext(), new Object[] { node });
            fail();
        } catch (AssertionError e) {
            assertEquals("Missing required argument 'arg1'", e.getMessage());
        }
    }

    @org.junit.Test
    public void testCreateUsingDelegate() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateUsingDelegate());

        Object delegate = new MyDelegate();
        Object result = instantiator.createUsingDelegate(new TestContext(), delegate);
        assertNull(result);
    }

    @org.junit.Test
    public void testCreateUsingDelegateWithArguments() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateUsingDelegate());

        Object delegate = new MyDelegateWithArgs();
        Object arg1 = new ObjectMapper().readValue(new ObjectMapper().createObjectNode().put("arg1", "test1"), String.class);
        Object arg2 = new ObjectMapper().readValue(new ObjectMapper().createObjectNode().put("arg2", 42), Integer.class);
        Object[] args = new Object[] { arg1, arg2 };

        Object result = instantiator.createUsingDelegate(new TestContext(), delegate, args);
        assertNotNull(result);
        assertEquals("test1", result.toString());
    }

    @org.junit.Test
    public void testCreateFromIntWithArgs() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateFromInt());

        Object arg1 = new ObjectMapper().readValue(new ObjectMapper().createObjectNode().put("arg1", "test1"), String.class);
        Object arg2 = new ObjectMapper().readValue(new ObjectMapper().createObjectNode().put("arg2", 42), Integer.class);
        Object[] args = new Object[] { arg1, arg2 };

        Object result = instantiator.createFromInt(new TestContext(), args);
        assertNotNull(result);
        assertEquals(Integer.valueOf(42), result);
    }

    @org.junit.Test
    public void testCreateFromLongWithArgs() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateFromLong());

        Object arg1 = new ObjectMapper().readValue(new ObjectMapper().createObjectNode().put("arg1", "test1"), String.class);
        Object arg2 = new ObjectMapper().readValue(new ObjectMapper().createObjectNode().put("arg2", 42L), Long.class);
        Object[] args = new Object[] { arg1, arg2 };

        Object result = instantiator.createFromLong(new TestContext(), args);
        assertNotNull(result);
        assertEquals(Long.valueOf(42L), result);
    }

    @org.junit.Test
    public void testCreateFromDoubleWithArgs() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateFromDouble());

        Object arg1 = new ObjectMapper().readValue(new ObjectMapper().createObjectNode().put("arg1", "test1"), String.class);
        Object arg2 = new ObjectMapper().readValue(new ObjectMapper().createObjectNode().put("arg2", 42.0), Double.class);
        Object[] args = new Object[] { arg1, arg2 };

        Object result = instantiator.createFromDouble(new TestContext(), args);
        assertNotNull(result);
        assertEquals(Double.valueOf(42.0), result);
    }

    @org.junit.Test
    public void testCreateFromBooleanWithArgs() throws IOException {
        ValueInstantiator instantiator = getTestInstance();
        assertFalse(instantiator.canCreateFromBoolean());

        Object arg1 = new ObjectMapper().readValue(new ObjectMapper().createObjectNode().put("arg1", "test1"), String.class);
        Object arg2 = new ObjectMapper().readValue(new ObjectMapper().createObjectNode().put("arg2", true), Boolean.class);
        Object[] args = new Object[] { arg1, arg2 };

        Object result = instantiator.createFromBoolean(new TestContext(), args);
        assertNotNull(result);
        assertEquals(Boolean.valueOf(true), result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JavaType getTypeFactory() {
            return new TypeFactory();
        }

        @Override
        public ObjectReader getRootValueFactory() {
            return new SimpleObjectReader();
        }

        @Override
        public ObjectReader getConfiguredReader(JavaType type) {
            return new SimpleObjectReader();
        }

        @Override
        public DeserializationConfig getConfig() {
            return new DeserializationConfig();
        }
    }

    private static class MyDelegate implements ValueInstantiator.ValueInstantiatorDelegate {
        public Object createInstance(DeserializationContext ctxt, Object inst) throws IOException {
            return new ObjectMapper().readValue(new ObjectMapper().createObjectNode().put("arg1", "test1"), String.class);
        }
    }

    private static class MyDelegateWithArgs implements ValueInstantiator.ValueInstantiatorDelegate {
        public Object createInstance(DeserializationContext ctxt, Object inst, Object[] args) throws IOException {
            return new ObjectMapper().readValue(args[0], String.class) + args[1].toString();
        }
    }

    private static class TypeFactory extends TypeFactory {
        @Override
        public JavaType constructType(JavaType baseType, Class<?> valueType) {
            return null;
        }

        @Override
        public JavaType constructType(JavaType baseType, JavaType valueType) {
            return null;
        }
    }

    private static class SimpleObjectReader extends BaseJsonReader {
        @Override
        public ObjectReader createContextualObjectReader(JsonFactory jf, DeserializationConfig ctxt, JavaType type) {
            return null;
        }
    }
}