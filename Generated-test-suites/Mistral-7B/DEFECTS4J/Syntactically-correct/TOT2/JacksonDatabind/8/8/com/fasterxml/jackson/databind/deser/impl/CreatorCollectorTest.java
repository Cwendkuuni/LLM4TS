package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector collector;
    private BeanDescription beanDesc;
    private DeserializationConfig config;

    @Before
    public void setUp() throws Exception {
        beanDesc = new BeanDescription(Object.class);
        config = new DeserializationConfig();
        collector = new CreatorCollector(beanDesc, true);
    }

    @Test
    public void testDefaultCreator() throws Exception {
        collector.setDefaultCreator(createTestCreator("default", Object.class));
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertEquals(Object.class, inst.getType());
        assertNotNull(inst.createUsingDefault(config));
    }

    @Test
    public void testStringCreator() throws Exception {
        collector.addStringCreator(createTestCreator("StringCreator", String.class), false);
        collector.setDefaultCreator(null);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertEquals(String.class, inst.getType());
        assertNotNull(inst.createUsingDefault(config));
    }

    @Test
    public void testIntCreator() throws Exception {
        collector.addIntCreator(createTestCreator("IntCreator", int.class), false);
        collector.setDefaultCreator(null);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertEquals(int.class, inst.getType());
        assertNotNull(inst.createUsingDefault(config));
    }

    @Test
    public void testLongCreator() throws Exception {
        collector.addLongCreator(createTestCreator("LongCreator", long.class), false);
        collector.setDefaultCreator(null);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertEquals(long.class, inst.getType());
        assertNotNull(inst.createUsingDefault(config));
    }

    @Test
    public void testDoubleCreator() throws Exception {
        collector.addDoubleCreator(createTestCreator("DoubleCreator", double.class), false);
        collector.setDefaultCreator(null);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertEquals(double.class, inst.getType());
        assertNotNull(inst.createUsingDefault(config));
    }

    @Test
    public void testBooleanCreator() throws Exception {
        collector.addBooleanCreator(createTestCreator("BooleanCreator", boolean.class), false);
        collector.setDefaultCreator(null);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertEquals(boolean.class, inst.getType());
        assertNotNull(inst.createUsingDefault(config));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateStringCreator() throws Exception {
        collector.addStringCreator(createTestCreator("StringCreator", String.class), false);
        collector.addStringCreator(createTestCreator("StringCreator", String.class), false);
        collector.setDefaultCreator(null);
        collector.constructValueInstantiator(config);
    }

    @Test
    public void testDelegateCreator() throws Exception {
        Class<?> delegateType = String[].class;
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty("strArr", String[].class);
        collector.addDelegatingCreator(createTestCreator("DelegateCreator", delegateType), false, injectables);
        collector.setDefaultCreator(null);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertEquals(delegateType, inst.getType());
        assertNotNull(inst.createUsingDefault(config));
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("prop1", String.class);
        properties[1] = new CreatorProperty("prop2", Integer.class);
        collector.addPropertyCreator(createTestCreator("PropertyCreator", null), false, properties);
        collector.setDefaultCreator(null);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertEquals(Map.class, inst.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyCreator() throws Exception {
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("prop1", String.class);
        properties[1] = new CreatorProperty("prop1", String.class);
        collector.addPropertyCreator(createTestCreator("PropertyCreator", null), false, properties);
        collector.setDefaultCreator(null);
        collector.constructValueInstantiator(config);
    }

    @Test
    public void testIncompleteParameter() throws Exception {
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        collector.addIncompleteParameter(param);
        collector.setDefaultCreator(null);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertEquals(Object.class, inst.getType());
        assertNotNull(inst.configureIncompleteParameter(param));
    }

    private AnnotatedWithParams createTestCreator(String name, Class<?> type) {
        return new AnnotatedWithParams(createTestMethod("create" + name, type));
    }

    private Method createTestMethod(String name, Class<?>... types) {
        return mockMethod(name, types);
    }

    private AnnotatedMember createTestMember(String name, Class<?> type) {
        return mock(AnnotatedMember.class);
    }

    private Member mockMember(Class<?> clazz, String name, Class<?>... types) {
        return mock(Member.class);
    }

    private Method mockMethod(String name, Class<?>... types) {
        return mock(Method.class);
    }
}