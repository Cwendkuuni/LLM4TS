package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.BaseApiMap;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class MethodPropertyTest {

    private Object testObject;
    private JsonParser testParser;
    private ObjectMapper mapper;
    private MethodProperty property;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        testObject = new BaseApiMap();
        Class<?> testClass = testObject.getClass();
        Method testMethod = testClass.getMethod("setTest", String.class);
        property = new MethodProperty(
                new BeanPropertyDefinition(new PropertyName("test"), String.class),
                Object.class, null, null, testMethod);

        testParser = mapper.getFactory().createParser(new Object[]{"test", "value"});
        mapper = new ObjectMapper();
    }

    @Test
    public void testSetterMethodWithValue() throws IOException, JsonProcessingException {
        property.setValueDeserializer(mapper.getValueFactory().stringValueDeserializer());

        property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testObject);
        assertEquals("value", testObject.get("test"));
    }

    @Test
    public void testSetterMethodWithNullValue() throws IOException, JsonProcessingException {
        property.setValueDeserializer(mapper.getValueFactory().stringValueDeserializer());
        NullValueProvider nvp = mapper.getValueFactory().nullValueProvider();
        property = new MethodProperty(property, null, nvp);

        testParser.nextToken(); // consume {
        testParser.nextToken(); // consume null
        property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testObject);
        assertNull(testObject.get("test"));
    }

    @Test
    public void testSetterMethodWithTypeDeserializer() throws IOException, JsonProcessingException {
        ObjectNode testNode = mapper.createObjectNode();
        testNode.put("test", mapper.valueToTree(123));

        property.setValueDeserializer(mapper.getTypeFactory().constructValueType(Integer.TYPE, NumberInput.class));

        property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testObject);
        assertEquals(123, testObject.get("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetterMethodWithInvalidTypeDeserializer() throws IOException, JsonProcessingException {
        ObjectNode testNode = mapper.createObjectNode();
        testNode.put("test", mapper.valueToTree(true));

        property.setValueDeserializer(mapper.getTypeFactory().constructValueType(Integer.TYPE, BooleanInput.class));

        property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testObject);
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testSetterMethodWithAnnotation() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, JsonProcessingException {
        Method testAnnotatedMethod = testClass.getMethod("setTestAnnotation", TestAnnotation.class);
        TestAnnotation testAnnotation = new TestAnnotation();
        testAnnotation.setTest("testAnnotationValue");

        property = new MethodProperty(
                new BeanPropertyDefinition(new PropertyName("testAnnotation"), TestAnnotation.class),
                Object.class, null, null, testAnnotatedMethod);

        testParser.nextToken(); // consume {
        testParser.nextToken(); // consume START_OBJECT
        testParser.nextToken(); // consume "testAnnotation"
        testParser.nextToken(); // consume VALUE_STRING
        testParser.nextValue("testAnnotationValue");
        testParser.nextToken(); // consume END_OBJECT

        property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testObject);
        assertEquals("testAnnotationValue", testObject.get("testAnnotation").getClass().getField("test").get(testObject.get("testAnnotation")));
    }

    @Test
    public void testSetterMethodWithException() throws IOException, JsonProcessingException {
        Method testMethod = testClass.getMethod("setTestException", NullableTestException.class);
        property = new MethodProperty(
                new BeanPropertyDefinition(new PropertyName("testException"), NullableTestException.class),
                Object.class, null, null, testMethod);

        ObjectNode testNode = mapper.createObjectNode();
        testNode.put("testException", mapper.valueToTree(new NullableTestException("TestExceptionMessage")));

        try {
            property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testObject);
            fail("Should throw TestException");
        } catch (IOException e) {
            assertNotNull(e.getCause());
            assertEquals("TestExceptionMessage", e.getCause().getMessage());
        }
    }

    @Test
    public void testSetterMethodWithAccessModifier() throws IOException, JsonProcessingException {
        Method testPrivateSetterMethod = testClass.getDeclaredMethod("setTestPrivate", String.class);
        testPrivateSetterMethod.setAccessible(true);

        property = new MethodProperty(
                new BeanPropertyDefinition(new PropertyName("testPrivate"), String.class),
                Object.class, null, null, testPrivateSetterMethod);

        ObjectNode testNode = mapper.createObjectNode();
        testNode.put("testPrivate", mapper.valueToTree("testPrivateValue"));

        property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testObject);
        assertEquals("testPrivateValue", testObject.get("testPrivate"));
    }

    @Test(expected = IllegalAccessException.class)
    public void testSetterMethodWithPrivateSetterAndNoAccess() throws IOException, JsonProcessingException {
        Method testPrivateSetterMethod = testClass.getDeclaredMethod("setTestPrivate", String.class);

        property = new MethodProperty(
                new BeanPropertyDefinition(new PropertyName("testPrivate"), String.class),
                Object.class, null, null, testPrivateSetterMethod);

        ObjectNode testNode = mapper.createObjectNode();
        testNode.put("testPrivate", mapper.valueToTree("testPrivateValue"));

        property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testObject);
        fail("Should throw IllegalAccessException");
    }

    @Test
    public void testSetterMethodWithPrivateSetterAndAccess() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, JsonProcessingException {
        Method testPrivateSetterMethod = testClass.getDeclaredMethod("setTestPrivate", String.class);
        testPrivateSetterMethod.setAccessible(true);

        property = new MethodProperty(
                new BeanPropertyDefinition(new PropertyName("testPrivate"), String.class),
                Object.class, null, null, testPrivateSetterMethod);

        ObjectNode testNode = mapper.createObjectNode();
        testNode.put("testPrivate", mapper.valueToTree("testPrivateValue"));

        Object testInstance = ClassUtil.newInstance(testClass);
        property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testInstance);
        assertEquals("testPrivateValue", testInstance.getClass().getField("testPrivate").get(testInstance));
    }

    @Test
    public void testSetterMethodWithPublicSetter() throws IOException, JsonProcessingException {
        Method testPublicSetterMethod = testClass.getMethod("setTestPublic", String.class);

        property = new MethodProperty(
                new BeanPropertyDefinition(new PropertyName("testPublic"), String.class),
                Object.class, null, null, testPublicSetterMethod);

        ObjectNode testNode = mapper.createObjectNode();
        testNode.put("testPublic", mapper.valueToTree("testPublicValue"));

        property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testObject);
        assertEquals("testPublicValue", testObject.get("testPublic"));
    }

    @Test
    public void testSetterMethodWithNoSetter() throws IOException, JsonProcessingException {
        property = new MethodProperty(
                new BeanPropertyDefinition(new PropertyName("testNoSetter"), String.class),
                Object.class, null, null, null);

        ObjectNode testNode = mapper.createObjectNode();
        testNode.put("testNoSetter", mapper.valueToTree("testNoSetterValue"));

        property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testObject);
        assertEquals("testNoSetterValue", testObject.get("testNoSetter"));
    }

    @Test
    public void testSetterMethodWithNullSetter() throws IOException, JsonProcessingException {
        property = new MethodProperty(
                new BeanPropertyDefinition(new PropertyName("testNullSetter"), String.class),
                Object.class, null, null, null);
        property._setter = null;

        ObjectNode testNode = mapper.createObjectNode();
        testNode.put("testNullSetter", mapper.valueToTree("testNullSetterValue"));

        try {
            property.deserializeAndSet(testParser, mapper.getDeserializationContext(), testObject);
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("No setter method found for property"));
        }
    }
}