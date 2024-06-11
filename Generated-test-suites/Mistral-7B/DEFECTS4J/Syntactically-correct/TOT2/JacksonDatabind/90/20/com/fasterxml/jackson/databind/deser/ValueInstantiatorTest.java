package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.base.ParserUtils;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ValueInstantiatorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    private static <T> T createValueInstance(Class<T> valueType, ObjectNode node) throws IOException {
        ObjectReader reader = MAPPER.readerFor(valueType);
        return reader.readValue(node);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void testMethod(String testName, ValueInstantiator valueInstantiator, Class<?> valueType, ObjectNode node) throws Exception {
        try {
            Object object = createValueInstance(valueType, node);
            System.out.println(testName + ": succeeded in creating an instance of " + valueType + " using the given JSON.");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create an instance of " + valueType + " using the given JSON.", e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create an instance of " + valueType + " using the given instantiator.", e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void testCanInstantiate(ValueInstantiator valueInstantiator, Class<?> valueType, ObjectNode node) throws Exception {
        if (!valueInstantiator.canInstantiate()) {
            ObjectNode nullNode = ParserUtils.createEmptyObject();
            System.out.println("Test skipped: instantiator does not support creating instances of " + valueType + ".");
            return;
        }

        testMethod("Test createFromObjectWith: ", valueInstantiator, valueType, node);
        testMethod("Test createFromString: ", valueInstantiator, String.class, node.put("test", "test"));
        testMethod("Test createFromInt: ", valueInstantiator, Integer.class, node.put("test", 1));
        testMethod("Test createFromLong: ", valueInstantiator, Long.class, node.put("test", 1L));
        testMethod("Test createFromDouble: ", valueInstantiator, Double.class, node.put("test", 1.0));
        testMethod("Test createFromBoolean: ", valueInstantiator, Boolean.class, node.put("test", true));

        if (valueInstantiator.canCreateUsingDelegate()) {
            testMethod("Test createUsingDelegate: ", valueInstantiator, valueType, node);
        }

        if (valueInstantiator.canCreateUsingArrayDelegate()) {
            AnnotatedClass ac = MAPPER.getIntrospect().findCreatorAnnotation(valueType);
            ObjectNode arrayNode = ParserUtils.createObjectNode();
            arrayNode.put("test", new ObjectNode(MapperFactory.getEmptyTreeAsTree(ObjectNode.class)) {
                {
                    put("delegate", new ObjectNode(MapperFactory.getEmptyTreeAsTree(Object.class)) {
                        {
                            put("type", ac.getAnnotation(ValueInstantiator.ValueInstantiatorFactory.class).value());
                        }
                    });
                }
            });
            testMethod("Test createUsingArrayDelegate: ", valueInstantiator, valueType, arrayNode);
        }
    }

    @org.junit.Test
    public void testCanInstantiateValueInstantiator() throws Exception {
        ValueInstantiator valueInstantiator = new Base(ValueInstantiator.class);
        ObjectNode node = ParserUtils.createEmptyObject();
        testCanInstantiate(valueInstantiator, ValueInstantiator.class, node);
    }

    @org.junit.Test
    public void testBaseValueInstantiator() throws Exception {
        Class<StdSerializer.StdSerializerWithType<String>> stringSerializer = StdSerializer.StdSerializerWithType.class;
        ValueInstantiator valueInstantiator = new Base(stringSerializer);
        ObjectNode node = ParserUtils.createEmptyObject();
        testCanInstantiate(valueInstantiator, stringSerializer, node);
    }

    @org.junit.Test
    public void testNullValueInstantiator() throws Exception {
        ValueInstantiator valueInstantiator = null;
        ObjectNode node = ParserUtils.createEmptyObject();
        try {
            testCanInstantiate(valueInstantiator, Object.class, node);
            throw new AssertionError("Expected an exception.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testCanInstantiateWithMissingCreator() throws Exception {
        class MyValueInstantiator extends Base {
            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[0];
            }

            @Override
            public boolean canCreateUsingObjectWith() {
                return false;
            }
        }

        MyValueInstantiator valueInstantiator = new MyValueInstantiator(Object.class);
        ObjectNode node = ParserUtils.createEmptyObject();
        try {
            testCanInstantiate(valueInstantiator, Object.class, node);
            throw new AssertionError("Expected an exception.");
        } catch (JsonMappingException e) {
            // Expected
            Assert.assertEquals("No single-argument constructor/factory method", e.getMessage().substring(0, 32));
        }
    }

    @org.junit.Test
    public void testCanInstantiateWithMissingCreatorAndDelegate() throws Exception {
        class MyValueInstantiator extends Base {
            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[0];
            }

            @Override
            public boolean canCreateUsingObjectWith() {
                return false;
            }

            @Override
            public boolean canCreateUsingDelegate() {
                return false;
            }

            @Override
            public AnnotatedWithParams getDefaultCreator() {
                return null;
            }
        }

        MyValueInstantiator valueInstantiator = new MyValueInstantiator(Object.class);
        ObjectNode node = ParserUtils.createEmptyObject();
        try {
            testCanInstantiate(valueInstantiator, Object.class, node);
            throw new AssertionError("Expected an exception.");
        } catch (JsonMappingException e) {
            // Expected
            Assert.assertEquals("No creator methods found for value [class java.lang.Object]", e.getMessage());
        }
    }

    @org.junit.Test
    public void testCanInstantiateWithSingleCreator() throws Exception {
        class MyValueInstantiator extends Base {
            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[0];
            }

            @Override
            public boolean canCreateUsingObjectWith() {
                return false;
            }

            @Override
            public boolean canCreateUsingDelegate() {
                return false;
            }

            @Override
            public boolean canCreateFromInt() {
                return true;
            }

            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return Integer.valueOf(value);
            }
        }

        MyValueInstantiator valueInstantiator = new MyValueInstantiator(Object.class);
        ObjectNode node = ParserUtils.createObjectNode();
        node.put("test", 1);
        testCanInstantiate(valueInstantiator, Object.class, node);
    }

    @org.junit.Test
    public void testCanInstantiateWithSingleCreatorAndDelegate() throws Exception {
        class MyValueInstantiator extends Base {
            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[0];
            }

            @Override
            public boolean canCreateUsingObjectWith() {
                return false;
            }

            @Override
            public boolean canCreateUsingDelegate() {
                return true;
            }

            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return new MyDelegateCreator();
            }

            private static class MyDelegateCreator extends AnnotatedWithParams {
                @Override
                public AnnotatedMethod getMethod() {
                    return null;
                }

                @Override
                public String getName() {
                    return "delegateCreator";
                }
            }
        }

        MyValueInstantiator valueInstantiator = new MyValueInstantiator(Object.class);
        ObjectNode node = ParserUtils.createObjectNode();
        node.put("delegate", new ObjectNode(MapperFactory.getEmptyTreeAsTree(Object.class)) {
            {
                put("type", "java.lang.String");
                put("value", "test");
            }
        });
        testCanInstantiate(valueInstantiator, Object.class, node);
    }

    @org.junit.Test
    public void testCanInstantiateWithMultipleCreators() throws Exception {
        class MyValueInstantiator extends Base {
            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[0];
            }

            @Override
            public boolean canCreateUsingObjectWith() {
                return false;
            }

            @Override
            public boolean canCreateUsingDelegate() {
                return false;
            }

            @Override
            public boolean canCreateFromInt() {
                return true;
            }

            @Override
            public boolean canCreateFromLong() {
                return true;
            }

            @Override
            public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
                return Integer.valueOf(value);
            }

            @Override
            public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
                return Long.valueOf(value);
            }
        }

        MyValueInstantiator valueInstantiator = new MyValueInstantiator(Object.class);
        ObjectNode node = ParserUtils.createObjectNode();
        node.put("test", 1);
        testCanInstantiate(valueInstantiator, Object.class, node);

        node.put("test", 1L);
        testCanInstantiate(valueInstantiator, Object.class, node);
    }

    @org.junit.Test
    public void testCanInstantiateWithMissingCreatorAndDelegateButDefault() throws Exception {
        class MyValueInstantiator extends Base {
            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[0];
            }

            @Override
            public boolean canCreateUsingObjectWith() {
                return false;
            }

            @Override
            public boolean canCreateUsingDelegate() {
                return false;
            }

            @Override
            public AnnotatedWithParams getDefaultCreator() {
                return null;
            }
        }

        MyValueInstantiator valueInstantiator = new MyValueInstantiator(Object.class);
        ObjectNode node = ParserUtils.createEmptyObject();
        testCanInstantiate(valueInstantiator, Object.class, node);
    }

    @org.junit.Test
    public void testCanInstantiateWithMissingCreatorAndDelegateButArrayDelegate() throws Exception {
        class MyValueInstantiator extends Base {
            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[0];
            }

            @Override
            public boolean canCreateUsingObjectWith() {
                return false;
            }

            @Override
            public boolean canCreateUsingDelegate() {
                return false;
            }

            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return null;
            }

            @Override
            public AnnotatedWithParams getArrayDelegateCreator() {
                return new AnnotatedWithParams() {
                    @Override
                    public AnnotatedMethod getMethod() {
                        return null;
                    }

                    @Override
                    public String getName() {
                        return "arrayDelegateCreator";
                    }
                };
            }
        }

        MyValueInstantiator valueInstantiator = new MyValueInstantiator(Object.class);
        ObjectNode node = ParserUtils.createObjectNode();
        node.put("delegate", new ObjectNode(MapperFactory.getEmptyTreeAsTree(Object[].class)) {
            {
                put("type", "java.lang.String[]");
                put("value", new ObjectNode(MapperFactory.getEmptyTreeAsTree(ObjectNode.class)) {
                    {
                        put("test", "test");
                    }
                });
            }
        });
        testCanInstantiate(valueInstantiator, Object.class, node);
    }

    @org.junit.Test
    public void testCanInstantiateWithMissingCreatorAndDelegateButArrayDelegateWithEmptyArray() throws Exception {
        class MyValueInstantiator extends Base {
            @Override
            public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
                return new SettableBeanProperty[0];
            }

            @Override
            public boolean canCreateUsingObjectWith() {
                return false;
            }

            @Override
            public boolean canCreateUsingDelegate() {
                return false;
            }

            @Override
            public AnnotatedWithParams getDelegateCreator() {
                return null;
            }

            @Override
            public AnnotatedWithParams getArrayDelegateCreator() {
                return new AnnotatedWithParams() {
                    @Override
                    public AnnotatedMethod getMethod() {
                        return null;
                    }

                    @Override
                    public String getName() {
                        return "arrayDelegateCreator";
                    }
                };
            }
        }

        MyValueInstantiator valueInstantiator = new MyValueInstantiator(Object.class);
        ObjectNode node = ParserUtils.createObjectNode();
        node.put("delegate", new ObjectNode(MapperFactory.getEmptyTreeAsTree(Object[].class)));
        try {
            testCanInstantiate(valueInstantiator, Object.class, node);
            throw new AssertionError("Expected an exception.");
        } catch (JsonMappingException e) {
            // Expected
            Assert.assertEquals("No single-argument constructor/factory method for delegate type [class [Ljava.lang.Object;]", e.getMessage());
        }
    }
}