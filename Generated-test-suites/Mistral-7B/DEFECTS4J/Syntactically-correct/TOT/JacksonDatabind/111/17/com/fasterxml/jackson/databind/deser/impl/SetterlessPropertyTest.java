package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    private ObjectNode objectNode;
    private JsonParser jsonParser;
    private ObjectMapDeserializer objectMapDeserializer;
    private SettableBeanProperty<Object, String> property;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectNode = mapper.createObjectNode();
        jsonParser = mapper.getFactory().createParser(objectNode);

        Class<Object> clazz = Object.class;
        BeanPropertyDefinition propDef = BeanPropertyDefinition.standardPropertyDefinition(
                "testProperty", clazz, String.class, SetterlessProperty.class);
        JavaType javaType = JavaTypeFactory.instance.constructType(List.class, String.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        property = new SetterlessProperty(propDef, javaType, typeDeser, contextAnnotations, method);
        objectMapDeserializer = Mockito.spy(new ObjectMapDeserializer(mapper.getConfig(), null, MapperFeature.AUTO_DETECT_GETTERS));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String value = "testValue";
        Object testInstance = new Object();

        ArrayNode listNode = objectNode.putArray("testArray");
        listNode.add(value);

        jsonParser.nextToken();

        property.deserializeAndSet(jsonParser, objectMapDeserializer, testInstance);
        assertEquals(Arrays.asList(value), testInstance.getClass().getMethod("getTestArray").invoke(testInstance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        String value = "testValue";
        Object testInstance = new Object();

        ArrayNode listNode = objectNode.putArray("testArray");
        listNode.add(value);

        jsonParser.nextToken();

        Object result = property.deserializeSetAndReturn(jsonParser, objectMapDeserializer, testInstance);
        assertEquals(testInstance, result);
        assertEquals(Arrays.asList(value), testInstance.getClass().getMethod("getTestArray").invoke(testInstance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object testInstance = new Object();
        String value = "testValue";

        property.set(testInstance, value);
        fail("Should not reach this point");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        Object testInstance = new Object();
        String value = "testValue";

        property.setAndReturn(testInstance, value);
        fail("Should not reach this point");
    }

    @Test
    public void testGetAnnotation() {
        assertEquals(MyAnnotation.class, property.getAnnotation(MyAnnotation.class));
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationNull() {
        property.getAnnotation(null);
    }

    @Test
    public void testFixAccess() throws IOException {
        property.fixAccess(objectMapDeserializer.getConfig());
    }

    @Test(expected = IllegalAccessException.class)
    public void testInvokeGetMethodWithNullInstance() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object testInstance = null;
        property.getGetter().invoke(testInstance);
    }

    @Test
    public void testInvokeGetMethod() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object testInstance = new Object();
        assertEquals(Collections.emptyList(), testInstance.getClass().getMethod("getTestProperty").invoke(testInstance));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvokeGetMethodWithWrongTypeInstance() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object testInstance = new List<>("testValue");
        try {
            property.getGetter().invoke(testInstance);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testInvokeGetMethodWithExpectedTypeInstance() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object testInstance = new Object();
        Object expectedValue = "testValue";
        Object testValue = Mockito.mock(String.class);
        when(testValue.toString()).thenReturn(expectedValue);
        when(property.getGetter().invoke(testInstance)).thenReturn(testValue);

        assertEquals(expectedValue, property.getGetter().invoke(testInstance).toString());
    }

    public static class MyAnnotation {}
}