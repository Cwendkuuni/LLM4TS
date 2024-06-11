package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import com.fasterxml.jackson.core.type.ClassTypes;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

/**
 * Test class for CreatorCollector
 */
public class CreatorCollectorTest {

    @Test
    public void testDefaultConstructor() throws Exception {
        BeanDescription beanDesc = new BeanDescription(ClassTypes.constructType(TestBean.class), null, null);
        MapperFactory config = new MapperFactory();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        ValueInstantiator inst = collector.constructValueInstantiator(config.getDefaultConfig());
        Object o = inst.createValueInstance(TestBean.class);
        assertNotNull(o);
    }

    @Test
    public void testStringCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(ClassTypes.constructType(TestBean.class), null, null);
        MapperFactory config = new MapperFactory();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        String creator = "TestBean(\"test\")";
        collector.addStringCreator(new AnnotatedWithParams(TestStringCreator.class, new Object[]{creator}), false);
        ValueInstantiator inst = collector.constructValueInstantiator(config.getDefaultConfig());
        Object o = inst.createValueInstance(TestBean.class, "test");
        assertEquals("test", o.toString());
    }

    @Test
    public void testIntCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(ClassTypes.constructType(TestBean.class), null, null);
        MapperFactory config = new MapperFactory();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        int creator = 5;
        collector.addIntCreator(new AnnotatedWithParams(TestIntCreator.class, new Object[]{creator}), false);
        ValueInstantiator inst = collector.constructValueInstantiator(config.getDefaultConfig());
        Object o = inst.createValueInstance(TestBean.class, 5);
        assertEquals(5, o.hashCode());
    }

    @Test
    public void testLongCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(ClassTypes.constructType(TestBean.class), null, null);
        MapperFactory config = new MapperFactory();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        long creator = 10L;
        collector.addLongCreator(new AnnotatedWithParams(TestLongCreator.class, new Object[]{creator}), false);
        ValueInstantiator inst = collector.constructValueInstantiator(config.getDefaultConfig());
        Object o = inst.createValueInstance(TestBean.class, 10L);
        assertEquals(10L, o.hashCode());
    }

    @Test
    public void testDoubleCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(ClassTypes.constructType(TestBean.class), null, null);
        MapperFactory config = new MapperFactory();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        double creator = 3.14;
        collector.addDoubleCreator(new AnnotatedWithParams(TestDoubleCreator.class, new Object[]{creator}), false);
        ValueInstantiator inst = collector.constructValueInstantiator(config.getDefaultConfig());
        Object o = inst.createValueInstance(TestBean.class, 3.14);
        assertEquals(3.14, o.hashCode(), 0.001);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(ClassTypes.constructType(TestBean.class), null, null);
        MapperFactory config = new MapperFactory();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        boolean creator = true;
        collector.addBooleanCreator(new AnnotatedWithParams(TestBooleanCreator.class, new Object[]{creator}), false);
        ValueInstantiator inst = collector.constructValueInstantiator(config.getDefaultConfig());
        Object o = inst.createValueInstance(TestBean.class, true);
        assertTrue(o instanceof TestBean);
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(ClassTypes.constructType(TestBeanArray.class), null, null);
        MapperFactory config = new MapperFactory();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        Object[] array = new Object[]{new TestBean()};
        collector.addDelegatingCreator(new AnnotatedWithParams(TestArrayDelegateCreator.class, new Object[]{TestBean[].class, array}), false, new SettableBeanProperty[0]);
        ValueInstantiator inst = collector.constructValueInstantiator(config.getDefaultConfig());
        Object o = inst.createValueInstance(TestBeanArray.class);
        assertArrayEquals(new TestBean[]{new TestBean()}, ((TestBeanArray) o).getBeans());
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(ClassTypes.constructType(TestBeanProperty.class), null, null);
        MapperFactory config = new MapperFactory();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        Map<String,String> map = new HashMap<>();
        map.put("name", "test");
        collector.addPropertyCreator(new AnnotatedWithParams(TestPropertyCreator.class, new Object[]{map}, false), false, new SettableBeanProperty[0]);
        ValueInstantiator inst = collector.constructValueInstantiator(config.getDefaultConfig());
        Object o = inst.createValueInstance(TestBeanProperty.class);
        TestBeanProperty testBean = (TestBeanProperty) o;
        assertEquals("test", testBean.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(ClassTypes.constructType(TestBean.class), null, null);
        MapperFactory config = new MapperFactory();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        String creator = "TestBean(\"test\")";
        collector.addStringCreator(new AnnotatedWithParams(TestStringCreator.class, new Object[]{creator}), false);
        collector.addStringCreator(new AnnotatedWithParams(TestStringCreator.class, new Object[]{creator}), false);
        ValueInstantiator inst = collector.constructValueInstantiator(config.getDefaultConfig());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(ClassTypes.constructType(TestBeanProperty.class), null, null);
        MapperFactory config = new MapperFactory();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        Map<String,String> map1 = new HashMap<>();
        map1.put("name1", "test1");
        SettableBeanProperty property1 = new TestPropertyCreator.TestProperty("name1", map1);

        Map<String,String> map2 = new HashMap<>();
        map2.put("name1", "test2");
        SettableBeanProperty property2 = new TestPropertyCreator.TestProperty("name1", map2);

        collector.addPropertyCreator(new AnnotatedWithParams(TestPropertyCreator.class, new Object[]{map1}, false), false, new SettableBeanProperty[]{property1});
        collector.addPropertyCreator(new AnnotatedWithParams(TestPropertyCreator.class, new Object[]{map2}, false), false, new SettableBeanProperty[]{property2});
        ValueInstantiator inst = collector.constructValueInstantiator(config.getDefaultConfig());
    }
}

class TestBean {
    private int i;

    public TestBean() {
    }

    public TestBean(String s) {
        this();
        i = s.hashCode();
    }

    public int getI() {
        return i;
    }

    public String toString() {
        return Integer.toString(i);
    }
}

class TestBeanArray {
    private TestBean[] beans;

    public TestBeanArray() {
        this(new TestBean[0]);
    }

    public TestBeanArray(TestBean[] beans) {
        this.beans = beans;
    }

    public TestBean[] getBeans() {
        return beans;
    }
}

class TestBeanProperty {
    private String name;

    public TestBeanProperty() {
    }

    public TestBeanProperty(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class TestStringCreator {
    public static TestBean create(String s) {
        return new TestBean(s);
    }
}

class TestIntCreator {
    public static TestBean create(int i) {
        return new TestBean(Integer.toString(i));
    }
}

class TestLongCreator {
    public static TestBean create(long l) {
        return new TestBean(Long.toString(l));
    }
}

class TestDoubleCreator {
    public static TestBean create(double d) {
        return new TestBean(Double.toString(d));
    }
}

class TestBooleanCreator {
    public static TestBean create(boolean b) {
        return new TestBean(Boolean.toString(b));
    }
}

class TestArrayDelegateCreator {
    public static TestBeanArray create(TestBean[] beans) {
        return new TestBeanArray(beans);
    }
}

class TestPropertyCreator {
    static class TestProperty {
        private String name;
        private Map<String,String> map;

        public TestProperty(String name, Map<String,String> map) {
            this.name = name;
            this.map = map;
        }

        public String getName() {
            return name;
        }
    }

    public static TestBeanProperty create(Map<String,String> map) {
        return new TestBeanProperty(map.getOrDefault("name", "defaultName"));
    }
}