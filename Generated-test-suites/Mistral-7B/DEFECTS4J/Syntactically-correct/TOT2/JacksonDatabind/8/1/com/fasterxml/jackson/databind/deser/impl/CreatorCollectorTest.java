package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.TestUtils;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreatorCollectorTest {

    private CreatorCollector collector;
    private BeanDescription beanDesc;
    private DeserializationConfig config;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws IOException {
        beanDesc = new BeanDescription(Map.class);
        config = new DeserializationConfig();
        objectMapper = new ObjectMapper(new JsonFactory(config));
        collector = new CreatorCollector(beanDesc, true);
    }

    @Test
    public void testDefaultCreator() throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        collector.setDefaultCreator(ClassUtil.findConstructor(Map.class, 0));
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        Map<String, Object> map = inst.createValueInstance(config, null);
        assertEquals(Collections.emptyMap(), map);
    }

    @Test
    public void testStringCreator() throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        String creatorName = "MapFactory::newInstance";
        Member creator = AccessibleObject.getDeclaredMember(Map.class, creatorName);
        AnnotatedWithParams creatorAnnotation = new AnnotatedWithParams(creator, null);
        collector.addStringCreator(creatorAnnotation, true);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        Map<String, Object> map = inst.createValueInstance(config, null);
        Map<String, Object> expectedMap = new LinkedHashMap<>();
        assertEquals(expectedMap, map);
    }

    @Test
    public void testIntCreator() throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        int creatorArg = 10;
        Member creator = AccessibleObject.getDeclaredMember(Map.class, "newInstance", new Class[]{Integer.TYPE});
        AnnotatedWithParams creatorAnnotation = new AnnotatedWithParams(creator, new Object[]{Integer.valueOf(creatorArg)});
        collector.addIntCreator(creatorAnnotation, true);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        Map<String, Object> map = inst.createValueInstance(config, null);
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("0", Integer.valueOf(0));
        expectedMap.put("1", Integer.valueOf(creatorArg));
        assertEquals(expectedMap, map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        String creatorName = "MapFactory::newInstance";
        Member creator1 = AccessibleObject.getDeclaredMember(Map.class, creatorName);
        Member creator2 = AccessibleObject.getDeclaredMember(Map.class, creatorName);
        AnnotatedWithParams creatorAnnotation1 = new AnnotatedWithParams(creator1, null);
        AnnotatedWithParams creatorAnnotation2 = new AnnotatedWithParams(creator2, null);
        collector.addStringCreator(creatorAnnotation1, true);
        collector.addStringCreator(creatorAnnotation2, true);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
    }

    @Test
    public void testLongCreator() throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        long creatorArg = 10L;
        Member creator = AccessibleObject.getDeclaredMember(Map.class, "newInstance", new Class[]{Long.TYPE});
        AnnotatedWithParams creatorAnnotation = new AnnotatedWithParams(creator, new Object[]{Long.valueOf(creatorArg)});
        collector.addLongCreator(creatorAnnotation, true);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        Map<String, Object> map = inst.createValueInstance(config, null);
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("0", Long.valueOf(0));
        expectedMap.put("1", Long.valueOf(creatorArg));
        assertEquals(expectedMap, map);
    }

    @Test
    public void testDoubleCreator() throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        double creatorArg = 10.0;
        Member creator = AccessibleObject.getDeclaredMember(Map.class, "newInstance", new Class[]{Double.TYPE});
        AnnotatedWithParams creatorAnnotation = new AnnotatedWithParams(creator, new Object[]{Double.valueOf(creatorArg)});
        collector.addDoubleCreator(creatorAnnotation, true);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        Map<String, Object> map = inst.createValueInstance(config, null);
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("0", Double.valueOf(0.0));
        expectedMap.put("1", Double.valueOf(creatorArg));
        assertEquals(expectedMap, map);
    }

    @Test
    public void testBooleanCreator() throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        boolean creatorArg = true;
        Member creator = AccessibleObject.getDeclaredMember(Map.class, "newInstance", new Class[]{Boolean.TYPE});
        AnnotatedWithParams creatorAnnotation = new AnnotatedWithParams(creator, new Object[]{Boolean.valueOf(creatorArg)});
        collector.addBooleanCreator(creatorAnnotation, true);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        Map<String, Object> map = inst.createValueInstance(config, null);
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("0", Boolean.FALSE);
        expectedMap.put("1", Boolean.TRUE);
        assertEquals(expectedMap, map);
    }

    @Test
    public void testDelegateCreator() throws IOException {
        JavaType delegateType = objectMapper.getTypeFactory().constructType(Map.class);
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        Object delegateInstance = new HashMap<>();
        JavaType delegateMemberType = objectMapper.getTypeFactory().constructType(Object.class);
        Member delegateCreator = AccessibleObject.getDeclaredMember(Map.class, "newInstance", new Class[]{Object.class});
        AnnotatedWithParams creatorAnnotation = new AnnotatedWithParams(delegateCreator, new Object[]{delegateInstance});
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty("key", String.class, null, null, null);
        collector.addDelegatingCreator(creatorAnnotation, true, injectables);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        Map<String, Object> map = inst.createValueInstance(config, null);
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("key", "value");
        assertEquals(expectedMap, map);
    }

    @Test
    public void testPropertyBasedCreator() throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        JavaType stringType = objectMapper.getTypeFactory().constructType(String.class);
        JavaType intType = objectMapper.getTypeFactory().constructType(Integer.class);
        AnnotatedMember keyCreator = AccessibleObject.getDeclaredMember(Map.class, "keyCreator");
        AnnotatedMember valueCreator = AccessibleObject.getDeclaredMember(Map.class, "valueCreator");
        AnnotatedWithParams keyCreatorAnnotation = new AnnotatedWithParams(keyCreator, null);
        AnnotatedWithParams valueCreatorAnnotation = new AnnotatedWithParams(valueCreator, null);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("key", stringType, null, ClassUtil.findConstructor(String.class, "value", new Class[] {String.class}), null, null);
        properties[1] = new CreatorProperty("value", intType, null, ClassUtil.findConstructor(Integer.class, "value", new Class[] {Integer.class}), null, null);
        collector.addPropertyCreator(keyCreatorAnnotation, true, properties);
        collector.addPropertyCreator(valueCreatorAnnotation, true, properties);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
        Map<String, Object> map = inst.createValueInstance(config, null);
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("key", "value");
        expectedMap.put("key1", 1);
        assertEquals(expectedMap, map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyCreator() throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructType(Map.class);
        JavaType stringType = objectMapper.getTypeFactory().constructType(String.class);
        JavaType intType = objectMapper.getTypeFactory().constructType(Integer.class);
        AnnotatedMember keyCreator = AccessibleObject.getDeclaredMember(Map.class, "keyCreator");
        AnnotatedMember valueCreator = AccessibleObject.getDeclaredMember(Map.class, "valueCreator");
        AnnotatedWithParams keyCreatorAnnotation = new AnnotatedWithParams(keyCreator, null);
        AnnotatedWithParams valueCreatorAnnotation = new AnnotatedWithParams(valueCreator, null);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("key", stringType, null, ClassUtil.findConstructor(String.class, "value", new Class[] {String.class}), null, null);
        properties[1] = properties[0]; // duplicate
        collector.addPropertyCreator(keyCreatorAnnotation, true, properties);
        collector.addPropertyCreator(valueCreatorAnnotation, true, properties);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
    }
}