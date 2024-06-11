package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Test cases for {@link ValueInstantiator} abstract class
 */
public class ValueInstantiatorTest {

    private static class MyValue {
        private int value;

        @JsonCreator
        public MyValue(@JsonProperty("v") int value) {
            this.value = value;
        }
    }

    private static class MyValueDeserializer extends StdDeserializer<MyValue> {
        public MyValueDeserializer() {
            this(null);
        }

        public MyValueDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public MyValue deserialize(DeserializationContext ctxt, ObjectNode node) throws IOException {
            ObjectMapper mapper = ctxt.getContext().getDefaultMapper();
            JavaType valueType = mapper.getTypeFactory().constructType(MyValue.class);
            ObjectNode creatorNode = node.get("creator");

            if (creatorNode != null) {
                Object creator = mapper.readValue(creatorNode, Object.class);
                Constructor<MyValue> constructor = findCreator(MyValue.class, creator);
                return constructor.newInstance(new Object[] { node.get("arg").asInt() });
            }

            // if no creator found, use default constructor
            return mapper.treeToValue(node, MyValue.class);
        }

        private Constructor<MyValue> findCreator(Class<?> cls, Object creator) {
            Constructor<MyValue> constructor = null;

            if (creator instanceof Method) {
                Method method = (Method) creator;
                if (method.getParameterTypes().length == 1 && method.getReturnType().isAssignableFrom(cls)) {
                    constructor = (Constructor<MyValue>) cls.getConstructor(method.getParameterTypes()[0].getType());
                }
            }

            if (constructor == null && creator instanceof Constructor<?>) {
                Constructor<?> constructorC = (Constructor<?>) creator;
                if (constructorC.getParameterTypes().length == 1 && constructorC.getReturnType().isAssignableFrom(cls)) {
                    constructor = (Constructor<MyValue>) cls.getConstructor(constructorC.getParameterTypes());
                }
            }

            return constructor;
        }
    }

    @Test
    public void testDefaultInstantiation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MyValue value = mapper.readValue(Object.class.getResource("/valueinstantiator-test.json"), MyValue.class);
        assertEquals(0, value.value);
    }

    @Test
    public void testInstantiationWithConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(Object.class.getResource("/valueinstantiator-test-constructor.json"), ObjectNode.class);
        MyValue value = mapper.findValueInserter(MyValue.class).findValue(mapper, node);
        assertEquals(42, value.value);
    }

    @Test
    public void testInstantiationWithFactoryMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(Object.class.getResource("/valueinstantiator-test-factory.json"), ObjectNode.class);
        MyValue value = mapper.readValue(node, MyValue.class);
        assertEquals(3, value.value);
    }

    @Test(expected = IOException.class)
    public void testMissingCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(Object.class.getResource("/valueinstantiator-test-missing.json"), ObjectNode.class);
        mapper.readValue(node, MyValue.class);
        fail("Expected IOException");
    }

    @Test
    public void testJsonDeserializeWithCreatorAnnotation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(Object.class.getResource("/valueinstantiator-test-deserializer.json"), ObjectNode.class);
        MyValue value = mapper.readValue(node, MyValue.class);
        assertEquals(5, value.value);
    }

    @Test
    public void testJsonDeserializeWithCreatorProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(Object.class.getResource("/valueinstantiator-test-deserializer-property.json"), ObjectNode.class);
        MyValue value = mapper.readValue(node, MyValue.class);
        assertEquals(6, value.value);
    }

    @Test
    public void testJsonDeserializeWithCreatorArgument() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(Object.class.getResource("/valueinstantiator-test-deserializer-argument.json"), ObjectNode.class);
        MyValue value = mapper.readValue(node, MyValue.class);
        assertEquals(7, value.value);
    }

    @Test(expected = IOException.class)
    public void testJsonDeserializeWithCreatorArgumentMissing() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(Object.class.getResource("/valueinstantiator-test-deserializer-argument-missing.json"), ObjectNode.class);
        mapper.readValue(node, MyValue.class);
        fail("Expected IOException");
    }

    @Test
    public void testJsonDeserializeWithCreatorArgumentInvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(Object.class.getResource("/valueinstantiator-test-deserializer-argument-invalidtype.json"), ObjectNode.class);
        mapper.readValue(node, MyValue.class);
    }

    @Test
    public void testJsonDeserializeWithCreatorArgumentMultipleValues() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(Object.class.getResource("/valueinstantiator-test-deserializer-argument-multiple.json"), ObjectNode.class);
        MyValue value = mapper.readValue(node, MyValue.class);
        assertEquals(8, value.value);
    }

    @Test
    public void testJsonDeserializeWithCreatorArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(Object.class.getResource("/valueinstantiator-test-deserializer-array.json"), ObjectNode.class);
        MyValue[] values = mapper.readValue(node, MyValue[].class);
        assertEquals(2, values.length);
        assertEquals(3, values[0].value);
        assertEquals(4, values[1].value);
    }

    @JsonDeserialize(using = MyValueDeserializer.class)
    static class NestedMyValue {
        private int value;

        public NestedMyValue(@JsonProperty("v") int value) {
            this.value = value;
        }
    }

    @Test
    public void testJsonDeserializeWithCreatorNested() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(Object.class.getResource("/valueinstantiator-test-deserializer-nested.json"), ObjectNode.class);
        NestedMyValue value = mapper.readValue(node, NestedMyValue.class);
        assertEquals(5, value.value);
    }

    @Test
    public void testJsonDeserializeWithCreatorNestedArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.readValue(Object.class.getResource("/valueinstantiator-test-deserializer-nested-array.json"), ObjectNode.class);
        NestedMyValue[] values = mapper.readValue(node, NestedMyValue[].class);
        assertEquals(2, values.length);
        assertEquals(3, values[0].value);
        assertEquals(4, values[1].value);
    }
}