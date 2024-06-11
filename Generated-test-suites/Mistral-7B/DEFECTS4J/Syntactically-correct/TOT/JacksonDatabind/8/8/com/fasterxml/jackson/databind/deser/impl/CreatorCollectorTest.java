package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CreatorCollectorTest {

    private ObjectMapper mapper;
    private DeserializationConfig config;
    private CreatorCollector collector;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        config = mapper.getDeserializationConfig();
        collector = new CreatorCollector(BeanDescription.standardModel(_Dummy.class), true);
    }

    @Test
    public void testDefaultConstructor() throws Exception {
        Method defaultConstructor = _Dummy.class.getDeclaredConstructor();
        AnnotatedWithParams constructor = new AnnotatedWithParams(defaultConstructor, null, null);
        collector.setDefaultCreator(constructor);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);

        _Dummy result = instantiator.createValue(_Dummy.class, null, null);
        assertNotNull(result);
    }

    @Test
    public void testStringConstructor() throws Exception {
        String arg = "test";
        Method stringConstructor = _Dummy.class.getDeclaredConstructor(String.class);
        AnnotatedWithParams constructor = new AnnotatedWithParams(stringConstructor, null, new TypeReference<String>() {});
        collector.addStringCreator(constructor, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);

        _Dummy result = instantiator.createValue(_Dummy.class, arg, null);
        assertEquals(arg, result.value);
    }

    @Test
    public void testIntConstructor() throws Exception {
        int arg = 5;
        Method intConstructor = _Dummy.class.getDeclaredConstructor(int.class);
        AnnotatedWithParams constructor = new AnnotatedWithParams(intConstructor, null, new TypeReference<Integer>() {});
        collector.addIntCreator(constructor, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);

        _Dummy result = instantiator.createValue(_Dummy.class, arg, null);
        assertEquals(arg, result.value);
    }

    @Test
    public void testLongConstructor() throws Exception {
        long arg = 10L;
        Method longConstructor = _Dummy.class.getDeclaredConstructor(long.class);
        AnnotatedWithParams constructor = new AnnotatedWithParams(longConstructor, null, new TypeReference<Long>() {});
        collector.addLongCreator(constructor, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);

        _Dummy result = instantiator.createValue(_Dummy.class, arg, null);
        assertEquals(arg, result.value);
    }

    @Test
    public void testDoubleConstructor() throws Exception {
        double arg = 3.14;
        Method doubleConstructor = _Dummy.class.getDeclaredConstructor(double.class);
        AnnotatedWithParams constructor = new AnnotatedWithParams(doubleConstructor, null, new TypeReference<Double>() {});
        collector.addDoubleCreator(constructor, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);

        _Dummy result = instantiator.createValue(_Dummy.class, arg, null);
        assertEquals(arg, result.value, 0.0);
    }

    @Test
    public void testBooleanConstructor() throws Exception {
        boolean arg = true;
        Method booleanConstructor = _Dummy.class.getDeclaredConstructor(boolean.class);
        AnnotatedWithParams constructor = new AnnotatedWithParams(booleanConstructor, null, new TypeReference<Boolean>() {});
        collector.addBooleanCreator(constructor, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);

        _Dummy result = instantiator.createValue(_Dummy.class, arg, null);
        assertEquals(arg, result.value);
    }

    @Test
    public void testDelegateConstructor() throws Exception {
        Class<_Delegate> delegateClass = _Delegate.class;
        Method delegateConstructor = _Dummy.class.getDeclaredConstructor(_Delegate.class);
        AnnotatedWithParams constructor = new AnnotatedWithParams(delegateConstructor, null, new TypeReference<_Delegate>() {});
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty(delegateClass.getDeclaredField("delegate"), null, null, null, null);
        collector.addDelegatingCreator(constructor, true, injectables);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);

        _Delegate delegate = new _Delegate();
        _Dummy result = instantiator.createValue(_Dummy.class, delegate, null);
        assertNotNull(result);
        assertTrue(result instanceof _DummyWithDelegate);
    }

    @Test
    public void testPropertyBasedConstructor() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        Map<String, Object> args = new LinkedHashMap<String, Object>();
        args.put("key1", "value1");
        args.put("key2", 2);

        Method method = _DummyWithProperties.class.getDeclaredConstructor(Map.class);
        AnnotatedWithParams constructor = new AnnotatedWithParams(method, null, javaType);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = new CreatorProperty("key1", null, null, new Object(), null);
        properties[1] = new CreatorProperty("key2", null, null, new Integer(2), null);
        collector.addPropertyCreator(constructor, true, properties);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        assertNotNull(instantiator);

        _DummyWithProperties result = instantiator.createValue(_DummyWithProperties.class, args, null);
        assertNotNull(result);
        assertEquals("value1", result.property1);
        assertEquals(2, result.property2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        Method stringConstructor = _Dummy.class.getDeclaredConstructor(String.class);
        AnnotatedWithParams constructor = new AnnotatedWithParams(stringConstructor, null, new TypeReference<String>() {});
        collector.addStringCreator(constructor, true);
        collector.addStringCreator(constructor, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        instantiator = null; // intentionally left uninitialized
    }

    private static class _Dummy {
        int value;

        @Deprecated
        public _Dummy() {
            this(0);
        }

        public _Dummy(int value) {
            this.value = value;
        }

        public static class _DummyWithDelegate {
            _Delegate delegate;
        }

        @Deprecated
        public static class _DummyWithProperties {
            Map<String, Object> properties;

            public _DummyWithProperties(Map<String, Object> properties) {
                this.properties = properties;
            }

            public String property1;
            public int property2;
        }

        @Deprecated
        public static class _Delegate {
            @Deprecated
            Object delegate;
        }
    }
}