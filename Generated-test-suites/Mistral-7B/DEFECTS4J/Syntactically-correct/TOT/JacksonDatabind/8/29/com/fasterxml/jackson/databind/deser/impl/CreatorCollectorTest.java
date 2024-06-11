package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.PojoBootstrapper;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CreatorCollectorTest {

    private CreatorCollector collector;
    private DeserializationConfig config;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        config = new DeserializationConfig();
        mapper = new ObjectMapper(config);
        BeanDescription beanDescription = new PojoBootstrapper(NopAnnotationIntrospector.instance).buildDescription(Dummy.class);
        collector = new CreatorCollector(beanDescription, true);
    }

    @Test
    public void testDefaultCreator() throws Exception {
        Dummy dummy = mapper.readValue("{}", Dummy.class);
        assertNotNull(dummy);
        verifyDefaultCreator(dummy, Dummy.class);
    }

    @Test
    public void testStringCreator() throws Exception {
        Dummy dummy = mapper.readValue("\"name\":\"John\"", Dummy.class);
        assertEquals("John", dummy.name);
        verifyStringCreator(dummy, "name", "John");
    }

    @Test
    public void testIntCreator() throws Exception {
        Dummy dummy = mapper.readValue("{\"age\":5}", Dummy.class);
        assertEquals(5, dummy.age);
        verifyIntCreator(dummy, "age", 5);
    }

    @Test
    public void testLongCreator() throws Exception {
        Dummy dummy = mapper.readValue("{\"longField\":10L}", Dummy.class);
        assertEquals(10L, dummy.longField);
        verifyLongCreator(dummy, "longField", 10L);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        Dummy dummy = mapper.readValue("{\"doubleField\":3.14}", Dummy.class);
        assertEquals(3.14, dummy.doubleField, 0.001);
        verifyDoubleCreator(dummy, "doubleField", 3.14);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        Dummy dummy = mapper.readValue("{\"booleanField\":true}", Dummy.class);
        assertTrue(dummy.booleanField);
        verifyBooleanCreator(dummy, "booleanField", true);
    }

    @Test
    public void testDelegateCreator() throws Exception {
        DummyDelegate delegate = new DummyDelegate();
        Dummy dummy = mapper.readValue("{\"delegate\":{\"name\":\"John\"}}", Dummy.class, delegate);
        assertEquals("John", dummy.name);
        verifyDelegateCreator(dummy, "delegate", delegate, "name", "John");
    }

    @Test
    public void testPropertyCreator() throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put("name", "John");
        DummyWithProperties dummy = mapper.readValue(mapper.writerWithView(PropertyName::getSimpleName()).writeValueAsString(properties), DummyWithProperties.class);
        assertEquals("John", dummy.name);
        verifyPropertyCreator(dummy, "name", "John");
    }

    @Test
    public void testNoCreators() throws Exception {
        Dummy dummy = mapper.readValue("{}", Dummy.class);
        assertNull(dummy.name);
        assertNull(dummy.age);
        assertNull(dummy.longField);
        assertNull(dummy.doubleField);
        assertFalse(dummy.booleanField);
        verifyNoCreators(dummy);
    }

    private void verifyDefaultCreator(Dummy dummy, Class<?> type) {
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertEquals(instantiator.getClass().getName(), instantiator.getClass().getName());
        assertEquals(type, instantiator.getType(null));
    }

    private void verifyStringCreator(Dummy dummy, String propertyName, String expectedValue) {
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        AnnotatedMethod creatorMethod = findCreatorMethod("name", dummy.getClass(), instantiator.getClass());
        assertNotNull(creatorMethod);
        assertEquals(String.class, creatorMethod.getParameterTypes()[0].getType());
        Object value = instantiator.createUsingBeanProperty(dummy, creatorMethod, new String[]{"name"}, new Object[]{expectedValue});
        assertEquals(expectedValue, value);
    }

    private void verifyIntCreator(Dummy dummy, String propertyName, int expectedValue) {
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        AnnotatedMethod creatorMethod = findCreatorMethod("age", dummy.getClass(), instantiator.getClass());
        assertNotNull(creatorMethod);
        assertEquals(Integer.TYPE, creatorMethod.getParameterTypes()[0].getType());
        Object value = instantiator.createUsingBeanProperty(dummy, creatorMethod, new String[]{"age"}, new Object[]{expectedValue});
        assertEquals(expectedValue, value);
    }

    private void verifyLongCreator(Dummy dummy, String propertyName, long expectedValue) {
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        AnnotatedMethod creatorMethod = findCreatorMethod("longField", dummy.getClass(), instantiator.getClass());
        assertNotNull(creatorMethod);
        assertEquals(Long.TYPE, creatorMethod.getParameterTypes()[0].getType());
        Object value = instantiator.createUsingBeanProperty(dummy, creatorMethod, new String[]{"longField"}, new Object[]{expectedValue});
        assertEquals(expectedValue, value);
    }

    private void verifyDoubleCreator(Dummy dummy, String propertyName, double expectedValue) {
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        AnnotatedMethod creatorMethod = findCreatorMethod("doubleField", dummy.getClass(), instantiator.getClass());
        assertNotNull(creatorMethod);
        assertEquals(Double.TYPE, creatorMethod.getParameterTypes()[0].getType());
        Object value = instantiator.createUsingBeanProperty(dummy, creatorMethod, new String[]{"doubleField"}, new Object[]{expectedValue});
        assertEquals(expectedValue, value, 0.001);
    }

    private void verifyBooleanCreator(Dummy dummy, String propertyName, boolean expectedValue) {
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        AnnotatedMethod creatorMethod = findCreatorMethod("booleanField", dummy.getClass(), instantiator.getClass());
        assertNotNull(creatorMethod);
        assertEquals(Boolean.TYPE, creatorMethod.getParameterTypes()[0].getType());
        Object value = instantiator.createUsingBeanProperty(dummy, creatorMethod, new String[]{"booleanField"}, new Object[]{expectedValue});
        assertEquals(expectedValue, value);
    }

    private void verifyDelegateCreator(Dummy dummy, String propertyName, DummyDelegate delegate, String delegatePropertyName, String expectedName) {
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        AnnotatedMethod creatorMethod = findCreatorMethod("delegate", dummy.getClass(), instantiator.getClass());
        assertNotNull(creatorMethod);
        assertEquals(new Class[]{DummyDelegate.class}, creatorMethod.getParameterTypes());
        Object[] arguments = {delegate};
        Object value = instantiator.createUsingConstructor(dummy.getClass(), creatorMethod, arguments);
        Dummy createdDummy = (Dummy) value;
        assertEquals(expectedName, createdDummy.name);
    }

    private void verifyPropertyCreator(DummyWithProperties dummy, String propertyName, String expectedValue) {
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        AnnotatedParameter creatorParameter = findCreatorParameter("name", dummy.getClass(), instantiator.getClass());
        assertNotNull(creatorParameter);
        assertEquals(String.class, creatorParameter.getType());
        Object value = instantiator.createUsingBeanProperty(dummy, creatorParameter, new String[]{"name"}, new Object[]{expectedValue});
        assertEquals(expectedValue, value);
    }

    private void verifyNoCreators(Dummy dummy) {
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getStringCreator());
        assertNull(instantiator.getIntCreator());
        assertNull(instantiator.getLongCreator());
        assertNull(instantiator.getDoubleCreator());
        assertNull(instantiator.getBooleanCreator());
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getPropertyBasedCreator());
        assertNull(instantiator.getIncompleteParameter());
    }

    private AnnotatedWithParams findCreatorMethod(String propertyName, Class<?> type, Class<? extends ValueInstantiator> instantiatorClass)
            throws NoSuchMethodException {
        List<Method> methods = Arrays.asList(instantiatorClass.getDeclaredMethods());
        for (Method method : methods) {
            if (method.getName().startsWith("createUsingBeanProperty") && method.getParameterCount() == 3) {
                String[] parameterNames = method.getParameterNames();
                if (propertyName.equals(parameterNames[0])) {
                    Object[] args = new Object[]{type, collector, new Object[]{propertyName}};
                    return new AnnotatedWithParams(method, args);
                }
            }
        }
        return null;
    }

    private AnnotatedParameter findCreatorParameter(String propertyName, Class<?> type, Class<? extends ValueInstantiator> instantiatorClass)
            throws NoSuchMethodException {
        List<Method> methods = Arrays.asList(instantiatorClass.getDeclaredMethods());
        for (Method method : methods) {
            if (method.getName().equals("configureFromPropertyCreator")) {
                Object[] args = method.getParameterValues();
                AnnotatedParameter parameter = (AnnotatedParameter) args[1];
                if (propertyName.equals(parameter.getName())) {
                    return parameter;
                }
            }
        }
        return null;
    }

    static class Dummy {
        String name;
        int age;
        long longField;
        double doubleField;
        boolean booleanField;

        @JsonCreator
        public Dummy(@JsonProperty("name") String name) {
            this.name = name;
        }

        @JsonCreator
        public Dummy(@JsonProperty("age") int age) {
            this.age = age;
        }

        @JsonCreator
        public Dummy(@JsonProperty("longField") long longField) {
            this.longField = longField;
        }

        @JsonCreator
        public Dummy(@JsonProperty("doubleField") double doubleField) {
            this.doubleField = doubleField;
        }

        @JsonCreator
        public Dummy(@JsonProperty("booleanField") boolean booleanField) {
            this.booleanField = booleanField;
        }
    }

    static class DummyWithProperties {
        String name;

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String value) {
            this.name = value;
        }
    }

    static class DummyDelegate {
        @JsonProperty("name")
        String name;

        @JsonCreator
        public DummyDelegate() {
        }

        @JsonCreator
        public DummyDelegate(@JsonProperty("name") String name) {
            this.name = name;
        }
    }
}

class AnnotatedWithParams {
    private final Method _method;
    private final Object[] _arguments;

    public AnnotatedWithParams(Method method, Object[] arguments) {
        _method = method;
        _arguments = arguments;
    }

    public Method getMethod() {
        return _method;
    }

    public Object[] getArguments() {
        return _arguments;
    }

    public AnnotatedMember getAnnotated() {
        return _method;
    }
}