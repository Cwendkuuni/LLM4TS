package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class InnerClassPropertyTest {

    private ObjectMapper objectMapper;
    private InnerClassProperty innerClassProperty;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();

        Class<?> innerClass = new Class<InnerClass>() {
        };

        Constructor<InnerClass> innerClassConstructor = innerClass.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        AnnotatedConstructor<InnerClass> annotatedConstructor = new AnnotatedConstructor<>(objectMapper.getTypeFactory().constructType(innerClass), innerClassConstructor);

        innerClassProperty = new InnerClassProperty(new StdSerializer<Object>(Object.class) {}, annotatedConstructor);
    }

    @Test
    public void testNormalDeserialization() throws IOException {
        ObjectNode input = objectMapper.createObjectNode();
        input.put("class", innerClass.getName());
        input.put("value", "normal value");

        InnerClass expectedValue = new InnerClass();
        expectedValue.setValue("normal value");

        InnerClass actualValue = objectMapper.readValue(input.traverse(), InnerClass.class);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testNullDeserialization() throws IOException {
        ObjectNode input = objectMapper.createObjectNode();
        input.put("class", innerClass.getName());

        InnerClass actualValue = objectMapper.readValue(input.traverse(), InnerClass.class);

        assertNull(actualValue);
    }

    @Test(expected = JsonProcessingException.class)
    public void testMissingClassException() throws IOException {
        ObjectNode input = objectMapper.createObjectNode();
        input.put("class", "com.nonexistent.package.InnerClass");
        input.put("value", "value");

        objectMapper.readValue(input.traverse(), InnerClass.class);
    }

    @Test
    public void testErrorDuringInstantiation() throws IOException {
        Constructor<InnerClass> constructorWithError = new Class<InnerClass>() {
            @Override
            protected Constructor<?>[] getDeclaredConstructors() {
                return new Constructor[]{
                        new Constructor<InnerClass>() {
                            @Override
                            public InnerClass newInstance(Object... args) throws InstantiationException, IllegalAccessException {
                                throw new RuntimeException("Error during instantiation");
                            }
                        }
                };
            }
        }.getDeclaredConstructors()[0];

        constructorWithError.setAccessible(true);

        AnnotatedConstructor<InnerClass> annotatedConstructorWithError = new AnnotatedConstructor<>(objectMapper.getTypeFactory().constructType(InnerClass.class), constructorWithError);

        InnerClassProperty innerClassPropertyWithError = new InnerClassProperty(new StdSerializer<Object>(Object.class) {}, annotatedConstructorWithError);

        ObjectNode input = objectMapper.createObjectNode();
        input.put("class", innerClass.getName());
        input.put("value", "value");

        try {
            objectMapper.readValue(input.traverse(), InnerClass.class);
        } catch (JsonProcessingException e) {
            assertEquals("Failed to instantiate class com.fasterxml.jackson.databind.deser.impl.InnerClass$InnerClassImpl, problem: Error during instantiation", e.getMessage());
        }
    }

    @Test
    public void testSetter() throws IOException {
        ObjectNode input = objectMapper.createObjectNode();
        input.put("class", innerClass.getName());
        input.put("value", "value");

        InnerClass innerClassInstance = objectMapper.readValue(input.traverse(), InnerClass.class);
        Object newValue = new Object();

        innerClassProperty.set(innerClassInstance, newValue);

        Object actualValue = innerClassProperty.get(innerClassInstance);

        assertEquals(newValue, actualValue);
    }

    @Test
    public void testSerialization() throws IOException {
        InnerClass innerClassInstance = new InnerClass();
        innerClassInstance.setValue("value");

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        String json = objectWriter.writeValueAsString(innerClassInstance);

        InnerClass deserializedInstance = objectMapper.readValue(objectMapper.readTree(json), InnerClass.class);

        assertEquals(innerClassInstance, deserializedInstance);
    }

    @Test
    public void testSerializationWithNullInnerClass() throws IOException {
        InnerClass innerClassInstance = new InnerClass();

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        String json = objectWriter.writeValueAsString(innerClassInstance);

        InnerClass deserializedInstance = objectMapper.readValue(objectMapper.readTree(json), InnerClass.class);

        assertNull(deserializedInstance.getValue());
    }
}