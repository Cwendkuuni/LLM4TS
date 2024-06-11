package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("Inner");
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        Object bean = new Object();
        SettableBeanProperty property = new InnerClassProperty(null, innerClassConstructor);

        assertEquals(InnerClassProperty.class, property.getClass());
        assertEquals(null, property._delegate);
        assertEquals(innerClassConstructor, property._creator);
        assertNull(property._annotated);
    }

    @Test
    public void testTransientConstructor() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("Inner");
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        AnnotatedConstructor annotatedConstructor = new AnnotatedConstructor(null, innerClassConstructor, null, null);
        InnerClassProperty property = new InnerClassProperty(null, annotatedConstructor);

        assertEquals(InnerClassProperty.class, property.getClass());
        assertEquals(null, property._delegate);
        assertNull(property._creator);
        assertEquals(annotatedConstructor, property._annotated);
    }

    @Test
    public void testSerialization() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("Inner");
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        Object bean = new Object();
        InnerClassProperty property = new InnerClassProperty(null, innerClassConstructor);

        // Serialize
        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(property);

        // Deserialize
        ObjectMapper objectMapper = new ObjectMapper();
        Object readValue = objectMapper.readValue(json, InnerClassProperty.class);

        // Check equality
        assertEquals(property, readValue);
        assertEquals(property._delegate, ((InnerClassProperty) readValue)._delegate);
        assertEquals(property._creator, ((InnerClassProperty) readValue)._creator);
        assertEquals(property._annotated, ((InnerClassProperty) readValue)._annotated);
    }

    @Test
    public void testDeserialization() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("Inner");
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        Object bean = new Object();
        InnerClassProperty property = new InnerClassProperty(null, innerClassConstructor);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(
                "{\"name\":\"test\",\"value\":{\"class\":\""+innerClassType.getName()+"\",\"constructor\":[\""+innerClassConstructor.getName()+"\"]}}");

        property.deserializeAndSet(jsonParser, new TestContext(), bean);

        // Check equality
        assertEquals(innerClassType.newInstance(bean), bean.getClass().getField("inner").get(bean));
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("Inner");
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        Object bean = new Object();
        InnerClassProperty property = new InnerClassProperty(null, innerClassConstructor);

        JsonDeserializer<?> valueDeserializer = new TestJsonDeserializer();
        property = new InnerClassProperty(property, valueDeserializer);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(
                "{\"name\":\"test\",\"value\":\"testValue\"}");

        property.deserializeAndSet(jsonParser, new TestContext(), bean);

        assertEquals("testValue", bean.getClass().getField("inner").get(bean));
    }

    @Test
    public void testWithName() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("Inner");
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        Object bean = new Object();
        PropertyName newName = PropertyName.construct("innerName");
        InnerClassProperty property = new InnerClassProperty(null, innerClassConstructor);
        InnerClassProperty newProperty = new InnerClassProperty(property, newName);

        assertEquals(newName, newProperty.getName());
        assertEquals(property._delegate, newProperty._delegate);
        assertEquals(property._creator, newProperty._creator);
        assertNull(newProperty._annotated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingConstructor() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("MissingInner");
        InnerClassProperty property = new InnerClassProperty(null, new AnnotatedConstructor(null, null, null, null));
    }

    @Test(expected = IllegalAccessException.class)
    public void testInstantiateWithAccessDenied() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("Inner");
        innerClassType.setAccessible(false);
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();

        Object bean = new Object();
        try {
            InnerClassProperty property = new InnerClassProperty(null, innerClassConstructor);
            property.deserializeAndSet(new TestParser(), new TestContext(), bean);
            fail("Expected IllegalAccessException");
        } catch (IllegalAccessException e) {
            // Expected
        }
    }

    @Test(expected = InstantiationException.class)
    public void testFailedInstantiation() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("FailedInner");
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        Object bean = new Object();
        try {
            InnerClassProperty property = new InnerClassProperty(null, innerClassConstructor);
            property.deserializeAndSet(new TestParser(), new TestContext(), bean);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullValue() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("Inner");
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        Object bean = new Object();
        try {
            InnerClassProperty property = new InnerClassProperty(null, innerClassConstructor);
            property.deserializeAndSet(new TestParser(), new TestContext(), bean);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithTypeMismatch() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("Inner");
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        Object bean = new Object();
        try {
            InnerClassProperty property = new InnerClassProperty(null, innerClassConstructor);
            JsonParser jsonParser = new TestParser();
            jsonParser.setValueAsString("notInnerType");
            property.deserializeAndSet(jsonParser, new TestContext(), bean);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testSet() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("Inner");
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        Object bean = new Object();
        InnerClassProperty property = new InnerClassProperty(null, innerClassConstructor);

        Object inner = new Object();
        property.set(bean, inner);

        assertEquals(inner, bean.getClass().getField("inner").get(bean));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("Inner");
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        Object bean = new Object();
        InnerClassProperty property = new InnerClassProperty(null, innerClassConstructor);

        Object inner = new Object();
        Object returned = property.setAndReturn(bean, inner);

        assertEquals(inner, bean.getClass().getField("inner").get(bean));
        assertEquals(inner, returned);
    }

    @Test
    public void testWriteReplace() {
        Class<?> innerClassType = Object.class.getDeclaredClass().getNestedClass("Inner");
        Constructor<?> innerClassConstructor = innerClassType.getDeclaredConstructor();
        innerClassConstructor.setAccessible(true);

        Object bean = new Object();
        InnerClassProperty property = new InnerClassProperty(null, innerClassConstructor);

        Object outerWriteReplace = property.writeReplace();
        Object innerWriteReplace = ((InnerClassProperty) outerWriteReplace)._annotated.getAnnotated();

        assertEquals(InnerClassProperty.class, outerWriteReplace.getClass());
        assertEquals(innerClassType.getName(), innerWriteReplace.getClass().getName());
        assertEquals(innerClassConstructor.getName(), innerWriteReplace.getClass().getDeclaredConstructors()[0].getName());
    }

    static class TestContext extends TestBaseContext {
    }

    static class TestJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.readValueAs(String.class);
        }
    }

    static class TestParser extends JsonParser {
        @Override
        public void nextToken() throws IOException {
            // Do nothing
        }

        @Override
        public Object readValueAs(Class<?> valueType) throws IOException {
            return "testValue";
        }
    }
}