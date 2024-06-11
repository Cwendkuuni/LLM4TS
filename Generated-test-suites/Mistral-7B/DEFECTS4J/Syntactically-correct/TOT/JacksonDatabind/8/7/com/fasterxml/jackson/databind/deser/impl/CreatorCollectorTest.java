package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class CreatorCollectorTest extends BaseMapTest {

    @Test
    public void testDefaultCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // create using default constructor
        ValueInstantiator valueInst = collector.constructValueInstantiator(null);
        Map result = valueInst.instantiateValue(null, HashMap.class);
        assertTrue("Default constructor test passed", result instanceof HashMap);
    }

    @Test
    public void testStringCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        String testClassName = "testMapClass";
        Class testClass = Class.forName(testClassName);
        Method testMethod = testClass.getMethod("createMap");
        JavaType javaType = mapper.typeFactory().constructType(testClass);

        collector.addStringCreator(testMethod, true);
        ValueInstantiator valueInst = collector.constructValueInstantiator(null);
        Map result = valueInst.instantiateValue(null, javaType);
        assertTrue("String constructor test passed", result instanceof Map);
    }

    @Test
    public void testIntCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Class testClass = Integer.TYPE;

        collector.addIntCreator(new AnnotatedWithParams(testClass, new int[0]), true);
        ValueInstantiator valueInst = collector.constructValueInstantiator(null);
        Map result = valueInst.instantiateValue(null, HashMap.class);
        assertTrue("Int constructor test passed", result instanceof HashMap);
    }

    @Test
    public void testLongCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Class testClass = Long.TYPE;

        collector.addLongCreator(new AnnotatedWithParams(testClass, new long[0]), true);
        ValueInstantiator valueInst = collector.constructValueInstantiator(null);
        Map result = valueInst.instantiateValue(null, HashMap.class);
        assertTrue("Long constructor test passed", result instanceof HashMap);
    }

    @Test
    public void testBooleanCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Class testClass = Boolean.TYPE;

        collector.addBooleanCreator(new AnnotatedWithParams(testClass, new boolean[0]), true);
        ValueInstantiator valueInst = collector.constructValueInstantiator(null);
        Map result = valueInst.instantiateValue(null, HashMap.class);
        assertTrue("Boolean constructor test passed", result instanceof HashMap);
    }

    @Test
    public void testDelegateCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Class testClass = HashMap.class;

        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty("key", String.class, null, null, null, null);

        collector.addDelegatingCreator(new AnnotatedWithParams(testClass, new Object[1], null), true, injectables);
        ValueInstantiator valueInst = collector.constructValueInstantiator(null);
        Map result = valueInst.instantiateValue(null, HashMap.class);
        assertTrue("Delegate constructor test passed", result instanceof HashMap);

        assertEquals("Key was not set correctly", "test", result.get("test"));
    }

    @Test
    public void testPropertyBasedCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        HashMap testMap = new HashMap();
        testMap.put("key", "value");
        JavaType javaType = mapper.typeFactory().constructType(testMap.getClass());

        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("key", String.class, null, null, null, null);
        properties[1] = new CreatorProperty("value", String.class, null, null, null, null);

        collector.addPropertyCreator(new AnnotatedWithParams(new HashMap().getClass(), new Object[0]), true, properties);
        ValueInstantiator valueInst = collector.constructValueInstantiator(null);
        Map result = valueInst.instantiateValue(null, javaType);
        assertTrue("Property based constructor test passed", result instanceof Map);

        assertEquals("Key was not set correctly", "value", result.get("value"));
    }

    @Test
    public void testEmptyParameterBasedCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        CreatorProperty[] properties = new CreatorProperty[0];

        collector.addPropertyCreator(new AnnotatedWithParams(new HashMap().getClass(), new Object[0]), true, properties);
        ValueInstantiator valueInst = collector.constructValueInstantiator(null);
        Map result = valueInst.instantiateValue(null, HashMap.class);
        assertTrue("Empty parameter based constructor test passed", result instanceof HashMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Class testClass = HashMap.class;

        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty("key", String.class, null, null, null, null);

        collector.addDelegatingCreator(new AnnotatedWithParams(testClass, new Object[1], null), true, injectables);
        collector.addDelegatingCreator(new AnnotatedWithParams(testClass, new Object[1], null), false, injectables);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateStringCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Class testClass = String.class;

        collector.addStringCreator(new AnnotatedWithParams(testClass, new Object[0]), true);
        collector.addStringCreator(new AnnotatedWithParams(testClass, new Object[0]), true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateIntCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Class testClass = Integer.TYPE;

        collector.addIntCreator(new AnnotatedWithParams(testClass, new int[0]), true);
        collector.addIntCreator(new AnnotatedWithParams(testClass, new int[0]), true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateLongCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Class testClass = Long.TYPE;

        collector.addLongCreator(new AnnotatedWithParams(testClass, new long[0]), true);
        collector.addLongCreator(new AnnotatedWithParams(testClass, new long[0]), true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateBooleanCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BeanDescription beanDesc = mapper.findBeanDescription(HashMap.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Class testClass = Boolean.TYPE;

        collector.addBooleanCreator(new AnnotatedWithParams(testClass, new boolean[0]), true);
        collector.addBooleanCreator(new AnnotatedWithParams(testClass, new boolean[0]), true);
    }
}