package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for CreatorCollector
 */
public class CreatorCollectorTest {

    @Test
    public void testCreateDefault() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(ArrayList.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        JavaType type = beanDesc.getType();
        Object obj = inst.createValueInstance(type);
        assertEquals(new ArrayList<Object>(), obj);
    }

    @Test
    public void testCreateWithNoArgsConstructor() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(ArrayList.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        collector.setDefaultCreator(new AnnotatedWithParams(beanDesc.getBeanClass().getDeclaredConstructor()));
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object obj = inst.createValueInstance(beanDesc.getType());
        assertEquals(new ArrayList<Object>(), obj);
    }

    @Test
    public void testCreateWithStringConstructor() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(LinkedHashMap.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        String constructorArg = "test";
        AnnotatedWithParams creator = new AnnotatedWithParams(beanDesc.getBeanClass().getDeclaredConstructor(String.class), constructorArg);
        collector.addStringCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object obj = inst.createValueInstance(beanDesc.getType(), new String[] { constructorArg });
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("test", null);
        assertEquals(map, obj);
    }

    @Test
    public void testCreateWithIntConstructor() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(HashMap.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        int constructorArg = 42;
        AnnotatedWithParams creator = new AnnotatedWithParams(beanDesc.getBeanClass().getDeclaredConstructor(int.class), constructorArg);
        collector.addIntCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object obj = inst.createValueInstance(beanDesc.getType(), new Integer[] { constructorArg });
        HashMap<Object, Object> map = new HashMap<>();
        map.put(constructorArg, null);
        assertEquals(map, obj);
    }

    @Test
    public void testCreateWithLongConstructor() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(ArrayList.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        long constructorArg = 42L;
        AnnotatedWithParams creator = new AnnotatedWithParams(beanDesc.getBeanClass().getDeclaredConstructor(long.class), constructorArg);
        collector.addLongCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object obj = inst.createValueInstance(beanDesc.getType(), new Long[] { constructorArg });
        assertEquals(new ArrayList<Object>(), obj);
    }

    @Test
    public void testCreateWithDoubleConstructor() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(HashMap.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        double constructorArg = 42.0;
        AnnotatedWithParams creator = new AnnotatedWithParams(beanDesc.getBeanClass().getDeclaredConstructor(double.class), constructorArg);
        collector.addDoubleCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object obj = inst.createValueInstance(beanDesc.getType(), new Double[] { constructorArg });
        HashMap<Object, Object> map = new HashMap<>();
        map.put("42.0", null);
        assertEquals(map, obj);
    }

    @Test
    public void testCreateWithBooleanConstructor() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(ArrayList.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        boolean constructorArg = true;
        AnnotatedWithParams creator = new AnnotatedWithParams(beanDesc.getBeanClass().getDeclaredConstructor(boolean.class), constructorArg);
        collector.addBooleanCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object obj = inst.createValueInstance(beanDesc.getType(), new Boolean[] { constructorArg });
        assertEquals(new ArrayList<Object>(), obj);
    }

    @Test
    public void testCreateWithDelegate() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(LinkedHashMap.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        String constructorArg = "test";
        AnnotatedWithParams creator = new AnnotatedWithParams(LinkedHashMap.class.getDeclaredConstructor(String.class), constructorArg);
        SettableBeanProperty argProp = new SettableBeanProperty(LinkedHashMap.class, "arg");
        SettableBeanProperty[] args = { argProp };
        collector.addDelegatingCreator(creator, true, args);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object obj = inst.createValueInstance(beanDesc.getType(), new Object[] { constructorArg });
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("arg", new Object());
        map.put("test", null);
        assertEquals(map, obj);
    }

    @Test
    public void testCreateWithPropertyBased() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(MyClass.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        String propertyName = "testProperty";
        int propertyValue = 42;
        AnnotatedWithParams creator = new AnnotatedWithParams(MyClass.class.getDeclaredConstructor(String.class), propertyName);
        SettableBeanProperty argProp = new SettableBeanProperty(MyClass.class, "testProperty");
        SettableBeanProperty[] args = { argProp };
        collector.addPropertyCreator(creator, true, args);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object obj = inst.createValueInstance(beanDesc.getType(), new Object[] { propertyName });
        MyClass myClass = new MyClass(propertyName, propertyValue);
        assertEquals(myClass, obj);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithDuplicateCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(LinkedHashMap.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        String constructorArg = "test";
        AnnotatedWithParams creator = new AnnotatedWithParams(LinkedHashMap.class.getDeclaredConstructor(String.class), constructorArg);
        collector.addStringCreator(creator, true);
        collector.addStringCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        inst.createValueInstance(beanDesc.getType(), new String[] { constructorArg });
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithDuplicateExplicitCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(LinkedHashMap.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        String constructorArg = "test";
        AnnotatedWithParams creator = new AnnotatedWithParams(LinkedHashMap.class.getDeclaredConstructor(String.class), constructorArg);
        collector.addStringCreator(creator, false);
        collector.addStringCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        ValueInstantiator inst2 = collector.constructValueInstantiator(new DeserializationConfig());
        inst.createValueInstance(beanDesc.getType(), new String[] { constructorArg });
        inst2.createValueInstance(beanDesc.getType(), new String[] { constructorArg });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithDuplicateProperties() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(MyClass.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        String propertyName = "testProperty";
        int propertyValue = 42;
        AnnotatedWithParams creator = new AnnotatedWithParams(MyClass.class.getDeclaredConstructor(String.class), propertyName);
        SettableBeanProperty argProp = new SettableBeanProperty(MyClass.class, "testProperty");
        SettableBeanProperty[] args = { argProp };
        collector.addPropertyCreator(creator, true, args);
        collector.addPropertyCreator(creator, true, args);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        inst.createValueInstance(beanDesc.getType(), new Object[] { propertyName });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithDuplicateExplicitProperties() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(MyClass.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        String propertyName = "testProperty";
        int propertyValue = 42;
        AnnotatedWithParams creator = new AnnotatedWithParams(MyClass.class.getDeclaredConstructor(String.class), propertyName);
        SettableBeanProperty argProp = new SettableBeanProperty(MyClass.class, "testProperty");
        SettableBeanProperty[] args = { argProp };
        collector.addPropertyCreator(creator, false, args);
        collector.addPropertyCreator(creator, true, args);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        inst.createValueInstance(beanDesc.getType(), new Object[] { propertyName });
    }

    @Test
    public void testCreateWithMissingProperty() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(MyClass.class));
        MapperConfig<?> config = new MapperFactory(new AnnotationIntrospector()).getConfig();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        String propertyName = "missingProperty";
        AnnotatedWithParams creator = new AnnotatedWithParams(MyClass.class.getDeclaredConstructor(String.class), propertyName);
        SettableBeanProperty argProp = new SettableBeanProperty(MyClass.class, "missingProperty");
        SettableBeanProperty[] args = { argProp };
        collector.addPropertyCreator(creator, true, args);
        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig());
        Object obj = inst.createValueInstance(beanDesc.getType(), new Object[] { propertyName });
        assertEquals(null, obj);
    }

    private static class MyClass {
        @JsonProperty("testProperty")
        private String testProperty;

        public MyClass(String testProperty) {
            this.testProperty = testProperty;
        }

        public String getTestProperty() {
            return testProperty;
        }
    }
}