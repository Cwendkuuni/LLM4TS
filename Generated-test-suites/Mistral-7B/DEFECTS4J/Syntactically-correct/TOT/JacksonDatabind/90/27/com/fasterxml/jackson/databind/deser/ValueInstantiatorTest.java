package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableValue;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Set;

public class ValueInstantiatorTest {

    @SuppressWarnings("unchecked")
    private static class TestValueInstantiator extends ValueInstantiator {
        public TestValueInstantiator() {
            super();
        }

        @Override
        public boolean canCreateUsingDefault() {
            return true;
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new Object();
        }

        public static class TestClass {
            private int value;

            @JsonCreator
            public TestClass(@JsonProperty("value") int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        @Override
        public Object instantiate(DeserializationContext ctxt, Object bean, BeanDescription beanDesc) throws IOException {
            return new TestClass(ctxt.numberParser().parseInt(ctxt.getTextToken().getText()));
        }
    }

    private static ValueInstantiator instantiator;

    @BeforeClass
    public static void setup() throws Exception {
        instantiator = new TestValueInstantiator();
    }

    @Test
    public void testDefaultInstantiator() throws Exception {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("value", 1);

        Object result = instantiator.createFromObjectWith(ObjectMapper.getDefaultInstance(), null, node);

        TestClass testClass = (TestClass) result;
        assertEquals(1, testClass.getValue());
    }

    @Test(expected = IOException.class)
    public void testMissingInstantiator() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        ObjectNode node = mapper.createObjectNode();
        node.put("value", new Object());

        instantiator.createFromObjectWith(ObjectMapper.getDefaultInstance(), null, node);
    }

    @Test
    public void testCreateFromDefaultConstructor() throws Exception {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();

        Object result = instantiator.createUsingDefault(ObjectMapper.getDefaultInstance());

        assertTrue(result instanceof Object);
    }

    @Test
    public void testCreateFromAnnotation() throws Exception {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("value", 42);

        Constructor<TestValueInstantiator.TestClass> constructor = TestValueInstantiator.TestClass.class.getDeclaredConstructor(Integer.TYPE);
        constructor.setAccessible(true);

        Object result = instantiator.createFromObjectWith(ObjectMapper.getDefaultInstance(), null, node, constructor);

        TestValueInstantiator.TestClass testClass = (TestValueInstantiator.TestClass) result;
        assertEquals(42, testClass.getValue());
    }

    @Test
    public void testCreateFromFactoryMethod() throws Exception {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("value", 42);

        AnnotatedClass ac = ObjectMapper.getDefaultInstance().findCreator(TestValueInstantiator.TestClass.class);
        AnnotatedMethod am = ac.findMethod("createFromInt");

        Object result = instantiator.createFromObjectWith(ObjectMapper.getDefaultInstance(), null, node, am);

        TestValueInstantiator.TestClass testClass = (TestValueInstantiator.TestClass) result;
        assertEquals(42, testClass.getValue());
    }

    @Test
    public void testCreateFromString() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        ObjectMapper mapperWithToString = ObjectMapper.getDefaultInstance().copy();
        mapperWithToString.setSerializerProvider(mapperWithToString.getSerializerProvider().withValueSerializer(new ToStringSerializer()));

        Object result = instantiator.createFromString(mapperWithToString, "new TestClass(1)");

        TestClass testClass = (TestClass) result;
        assertEquals(1, testClass.getValue());
    }

    @Test(expected = IOException.class)
    public void testCreateFromInvalidString() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();

        instantiator.createFromString(mapper, "new InvalidTestClass(1)");
    }
}