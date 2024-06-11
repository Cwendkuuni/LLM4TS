package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector collector;
    private MapperFactory mapperFactory;

    @Before
    public void setup() throws Exception {
        mapperFactory = new MapperFactory();
        AnnotatedClass ac = mapperFactory.introspect(TestBean.class);
        collector = new CreatorCollector(ac, mapperFactory);
    }

    @Test
    public void testDefaultCreator() throws Exception {
        // Default creator is null in this case
        DeserializationConfig config = mapperFactory.getConfig();
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        TestBean bean = instantiator.createValueInstance(null);
        assertNull(bean);
    }

    @Test
    public void testStringCreator() throws Exception {
        String creatorName = "TestStringCreator";
        AnnotatedWithParams creator = new AnnotatedWithParams(
                ValueIntrospector.findValueMethod(TestBean.class, creatorName), collector._beanDesc);
        collector.addStringCreator(creator, true);

        DeserializationConfig config = mapperFactory.getConfig();
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        TestBean bean = instantiator.createValueInstance("Test value");
        assertEquals("Test value", bean.getStringValue());
    }

    @Test
    public void testIntCreator() throws Exception {
        String creatorName = "TestIntCreator";
        AnnotatedWithParams creator = new AnnotatedWithParams(
                ValueIntrospector.findValueMethod(TestBean.class, creatorName), collector._beanDesc);
        collector.addIntCreator(creator, true);

        DeserializationConfig config = mapperFactory.getConfig();
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        TestBean bean = instantiator.createValueInstance(42);
        assertEquals(42, bean.getIntValue());
    }

    @Test
    public void testLongCreator() throws Exception {
        String creatorName = "TestLongCreator";
        AnnotatedWithParams creator = new AnnotatedWithParams(
                ValueIntrospector.findValueMethod(TestBean.class, creatorName), collector._beanDesc);
        collector.addLongCreator(creator, true);

        DeserializationConfig config = mapperFactory.getConfig();
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        TestBean bean = instantiator.createValueInstance(42L);
        assertEquals(42L, bean.getLongValue());
    }

    @Test
    public void testDoubleCreator() throws Exception {
        String creatorName = "TestDoubleCreator";
        AnnotatedWithParams creator = new AnnotatedWithParams(
                ValueIntrospector.findValueMethod(TestBean.class, creatorName), collector._beanDesc);
        collector.addDoubleCreator(creator, true);

        DeserializationConfig config = mapperFactory.getConfig();
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        TestBean bean = instantiator.createValueInstance(42.0);
        assertEquals(42.0, bean.getDoubleValue(), 0.0);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        String creatorName = "TestBooleanCreator";
        AnnotatedWithParams creator = new AnnotatedWithParams(
                ValueIntrospector.findValueMethod(TestBean.class, creatorName), collector._beanDesc);
        collector.addBooleanCreator(creator, true);

        DeserializationConfig config = mapperFactory.getConfig();
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        TestBean bean = instantiator.createValueInstance(true);
        assertEquals(true, bean.isBooleanValue());
    }

    @Test
    public void testDelegateCreator() throws Exception {
        String creatorName = "TestDelegateCreator";
        AnnotatedWithParams creator = new AnnotatedWithParams(
                ValueIntrospector.findConstructorMethod(TestDelegate.class), collector._beanDesc);
        AnnotatedParameter[] params = creator.getParameterAnnotations();
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        delegateArgs[0] = new SettableBeanProperty(params[0].getParameter(), creatorName, "delegate");

        collector.addDelegatingCreator(creator, true, delegateArgs);

        DeserializationConfig config = mapperFactory.getConfig();
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        TestDelegate delegate = new TestDelegate();
        TestBean bean = instantiator.createValueInstance(delegate);
        assertEquals(delegate, bean.getDelegate());
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        String creatorName = "TestArrayDelegateCreator";
        AnnotatedWithParams creator = new AnnotatedWithParams(
                ValueIntrospector.findConstructorMethod(TestArrayDelegate.class), collector._beanDesc);
        AnnotatedParameter[] params = creator.getParameterAnnotations();
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        delegateArgs[0] = new SettableBeanProperty(params[0].getParameter(), creatorName, "delegate");

        collector.addDelegatingCreator(creator, true, delegateArgs);

        DeserializationConfig config = mapperFactory.getConfig();
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        TestArrayDelegate arrayDelegate = new TestArrayDelegate(new Integer[]{1, 2, 3});
        TestBean bean = instantiator.createValueInstance(arrayDelegate);
        assertArrayEquals(new Integer[]{1, 2, 3}, bean.getArrayDelegate());
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        String creatorName = "TestPropertyBasedCreator";
        AnnotatedWithParams creator = new AnnotatedWithParams(
                ValueIntrospector.findValueMethod(TestBean.class, creatorName), collector._beanDesc);
        SettableBeanProperty[] properties = new SettableBeanProperty[]{
                new SettableBeanProperty(ValueIntrospector.findProperty(TestBean.class, "stringProperty"), "stringProperty", "stringValue"),
                new SettableBeanProperty(ValueIntrospector.findProperty(TestBean.class, "intProperty"), "intProperty", "intValue"),
                new SettableBeanProperty(ValueIntrospector.findProperty(TestBean.class, "longProperty"), "longProperty", "longValue"),
                new SettableBeanProperty(ValueIntrospector.findProperty(TestBean.class, "doubleProperty"), "doubleProperty", "doubleValue"),
                new SettableBeanProperty(ValueIntrospector.findProperty(TestBean.class, "booleanProperty"), "booleanProperty", "booleanValue")
        };

        collector.addPropertyCreator(creator, true, properties);

        DeserializationConfig config = mapperFactory.getConfig();
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        TestBean bean = instantiator.createValueInstance(new JSONPObject("{\"stringProperty\":\"stringValue\",\"intProperty\":42,\"longProperty\":42L,\"doubleProperty\":42.0,\"booleanProperty\":true}"));
        assertEquals("stringValue", bean.getStringValue());
        assertEquals(42, bean.getIntValue());
        assertEquals(42L, bean.getLongValue());
        assertEquals(42.0, bean.getDoubleValue(), 0.0);
        assertEquals(true, bean.isBooleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreators() throws Exception {
        String creatorName = "TestStringCreator";
        AnnotatedWithParams creator = new AnnotatedWithParams(
                ValueIntrospector.findValueMethod(TestBean.class, creatorName), collector._beanDesc);
        collector.addStringCreator(creator, true);
        collector.addStringCreator(creator, true);
    }

    @Test
    public void testMissingCreator() throws Exception {
        DeserializationConfig config = mapperFactory.getConfig();
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        TestBean bean = instantiator.createValueInstance("Test value");
        assertNull(bean);
    }

    private static void assertArrayEquals(Object[] expected, Object[] actual) {
        if (expected == null) {
            assertNull(actual);
        } else if (actual == null) {
            fail("Expected non-null array but got null");
        } else if (expected.length != actual.length) {
            fail("Expected array of length " + expected.length + " but got array of length " + actual.length);
        } else {
            for (int i = 0; i < expected.length; ++i) {
                assertEquals(expected[i], actual[i]);
            }
        }
    }

    static class TestBean {
        private String stringValue;
        private int intValue;
        private long longValue;
        private double doubleValue;
        private boolean booleanValue;

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public long getLongValue() {
            return longValue;
        }

        public void setLongValue(long longValue) {
            this.longValue = longValue;
        }

        public double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
        }

        public boolean isBooleanValue() {
            return booleanValue;
        }

        public void setBooleanValue(boolean booleanValue) {
            this.booleanValue = booleanValue;
        }
    }

    static class TestDelegate {
        private Object delegate;

        public TestDelegate() {}

        public TestDelegate(Object delegate) {
            this.delegate = delegate;
        }

        public Object getDelegate() {
            return delegate;
        }

        public void setDelegate(Object delegate) {
            this.delegate = delegate;
        }
    }

    static class TestArrayDelegate {
        private Integer[] array;

        public TestArrayDelegate() {}

        public TestArrayDelegate(Integer[] array) {
            this.array = array;
        }

        public Integer[] getArrayDelegate() {
            return array;
        }
    }
}