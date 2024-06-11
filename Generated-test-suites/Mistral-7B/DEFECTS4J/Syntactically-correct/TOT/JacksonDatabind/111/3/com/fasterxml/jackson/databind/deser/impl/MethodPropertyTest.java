package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;

public class MethodPropertyTest {

    private ObjectMapper objectMapper;
    private MethodProperty methodProperty;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {
        objectMapper = new ObjectMapper();
        Class<?> testClass = TestClass.class;
        PropertyName propertyName = PropertyName.construct("testProperty");
        Method method = testClass.getMethod("setTestProperty", Object.class);
        Annotation[] annotations = new Annotation[] { new JsonProperty("testProperty") };
        MethodProperty methodProperty = new MethodProperty(new BeanPropertyDefinition(propertyName, testClass.getDeclaredField("testProperty"), null, null),
                objectMapper.getTypeFactory().constructType(testClass), null, new AnnotatedMethod(method, annotations), null, null, null, false);
        this.methodProperty = new MethodProperty(methodProperty, mock(JsonDeserializer.class), new NullValueProvider(Type.NULL) {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return null;
            }
        });
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object testInstance = new TestClass();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": 1}");
        methodProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance);
        assertEquals(1, testInstance.getTestProperty());
    }

    @Test
    public void testDeserializeAndSetNull() throws IOException {
        Object testInstance = new TestClass();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": null}");
        methodProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance);
        assertNull(testInstance.getTestProperty());
    }

    @Test
    public void testDeserializeAndSetNullSkip() throws IOException {
        Object testInstance = new TestClass();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": null}");
        methodProperty = new MethodProperty(methodProperty, mock(JsonDeserializer.class), new NullValueProvider(Type.NULL) {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return null;
            }
        }, null, null, null, true);
        methodProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance);
        assertNull(testInstance.getTestProperty());
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws IOException {
        Object testInstance = new TestClass();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": null}");
        assertEquals(testInstance, methodProperty.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationContext(), testInstance));
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithException() throws IOException {
        Object testInstance = new TestClass();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": \"abc\"}");
        methodProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance);
    }

    @Test
    public void testSet() throws InstantiationException, IllegalAccessException {
        Object testInstance = new TestClass();
        methodProperty.set(testInstance, 1);
        assertEquals(1, testInstance.getTestProperty());
    }

    @Test
    public void testSetNull() throws InstantiationException, IllegalAccessException {
        Object testInstance = new TestClass();
        methodProperty.set(testInstance, null);
        assertNull(testInstance.getTestProperty());
    }

    @Test
    public void testSetNullSkip() throws InstantiationException, IllegalAccessException {
        Object testInstance = new TestClass();
        methodProperty = new MethodProperty(methodProperty, null, null, null, null, null, new EmptyTypeDeserializer(), true);
        methodProperty.set(testInstance, null);
        assertNull(testInstance.getTestProperty());
    }

    @Test
    public void testSetWithAnnotation() throws InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {
        Object testInstance = new TestClass();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": 1}");
        when(jsonParser.getValueAsInt()).thenReturn(1);
        AnnotationIntrospector introspector = new AnnotationIntrospector();
        AnnotatedMember member = introspector.findAnnotatedMember(TestClass.class, "testProperty");
        methodProperty = new MethodProperty(new BeanPropertyDefinition(member),
                objectMapper.getTypeFactory().constructType(TestClass.class), null, null, null,
                new AnnotatedMethod(method, new Annotation[] { new JsonProperty("testProperty") }), null, null, false);
        methodProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance);
        assertEquals(1, testInstance.getTestProperty());
    }

    @Test
    public void testSetWithAnnotationAndNullValue() throws InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {
        Object testInstance = new TestClass();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": null}");
        when(jsonParser.getValueAsInt()).thenReturn(null);
        AnnotationIntrospector introspector = new AnnotationIntrospector();
        AnnotatedMember member = introspector.findAnnotatedMember(TestClass.class, "testProperty");
        methodProperty = new MethodProperty(new BeanPropertyDefinition(member),
                objectMapper.getTypeFactory().constructType(TestClass.class), null, null, null,
                new AnnotatedMethod(method, new Annotation[] { new JsonProperty("testProperty") }), null, null, false);
        methodProperty = new MethodProperty(methodProperty, null, null, null, null, null, new EmptyTypeDeserializer(), true);
        methodProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance);
        assertNull(testInstance.getTestProperty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullMethod() throws InstantiationException, IllegalAccessException {
        Object testInstance = new TestClass();
        methodProperty = new MethodProperty(methodProperty, mock(Method.class), null, null, null, null, null, false);
        methodProperty.set(testInstance, 1);
    }

    @Test
    public void testSetWithPrivateMethod() throws InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {
        Object testInstance = new TestClass();
        methodProperty = new MethodProperty(methodProperty, new Method(TestClass.class.getDeclaredMethod("setTestPropertyPrivate"), Object.class), null, null, null, null, null, false);
        methodProperty.set(testInstance, 1);
        assertEquals(1, testInstance.getTestProperty());
    }

    @Test(expected = IllegalAccessException.class)
    public void testSetWithPrivateMethodAndNull() throws InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {
        Object testInstance = new TestClass();
        methodProperty = new MethodProperty(methodProperty, new Method(TestClass.class.getDeclaredMethod("setTestPropertyPrivate"), Object.class), null, null, null, null, null, false);
        methodProperty.set(testInstance, null);
    }

    @Test
    public void testSetWithPrivateMethodAndNullSkip() throws InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {
        Object testInstance = new TestClass();
        methodProperty = new MethodProperty(methodProperty, new Method(TestClass.class.getDeclaredMethod("setTestPropertyPrivate"), Object.class), null, null, null, null, null, true);
        methodProperty.set(testInstance, null);
        assertNull(testInstance.getTestProperty());
    }

    @Test
    public void testReadResolve() throws InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {
        Object testInstance = new TestClass();
        methodProperty = new MethodProperty(methodProperty, _annotated.getAnnotated(), null, null, null, null, null, false);
        methodProperty = (MethodProperty) testInstance.getClass().getField("methodProperty").get(testInstance);
        Object resolved = methodProperty.readResolve();
        assertEquals(methodProperty, resolved);
    }

    static class TestClass {
        private int testProperty;

        @JsonProperty("testProperty")
        public int getTestProperty() {
            return testProperty;
        }

        @JsonProperty("testProperty")
        public void setTestProperty(int value) {
            testProperty = value;
        }

        @JsonProperty("testPropertyPrivate")
        private void setTestPropertyPrivate(int value) {
            testProperty = value;
        }
    }
}