package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithMembers;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Unit tests for {@link CreatorCollector} class
 */
public class CreatorCollectorTest {

    private Object testBean;
    private BeanDescription testBeanDesc;
    private MapperFactory mapperConfig;

    @Before
    public void setUp() throws Exception {
        testBean = new TestBean();
        AnnotatedClass testBeanClass = new AnnotatedClass(TestBean.class, null, null);
        testBeanDesc = new BeanDescription(testBeanClass, Object.class, null, new JacksonAnnotationIntrospector());
        mapperConfig = new MapperFactory();
    }

    @Test
    public void testDefaultCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(testBeanDesc, mapperConfig);
        AnnotatedMethod defaultCreator = testBeanClass.findConstructor();
        collector.setDefaultCreator(new AnnotatedWithParams(defaultCreator, new Object[0]));

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapperConfig.getConfig());
        assertEquals(TestBean.class, instantiator.getType());
        assertNotNull(instantiator.createUsingDefault(null));
    }

    @Test
    public void testStringCreator() throws Exception {
        TestBean testBeanInstance = new TestBean();
        String stringValue = "test";

        TestBean constructorWithStringArg = new TestBean(stringValue);
        AnnotatedMethod stringConstructor = testBeanClass.getMethod("TestBean", String.class);
        SettableBeanProperty[] stringConstructorArgs = new SettableBeanProperty[] { new SettableBeanProperty("str", String.class) };

        CreatorCollector collector = new CreatorCollector(testBeanDesc, mapperConfig);
        collector.addStringCreator(new AnnotatedWithParams(stringConstructor, false), false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapperConfig.getConfig());
        assertEquals(TestBean.class, instantiator.getType());
        TestBean instantiatedBean = (TestBean) instantiator.createValueInstance(null, stringValue);
        assertEquals(stringValue, instantiatedBean.getStr());

        TestBean testBeanInstance2 = new TestBean();
        instantiator.createValueInstance(testBeanInstance2, stringValue);
        assertEquals(stringValue, testBeanInstance2.getStr());
    }

    @Test
    public void testIntCreator() throws Exception {
        TestBean testBeanInstance = new TestBean();
        int intValue = 1;

        TestBean constructorWithIntArg = new TestBean(intValue);
        AnnotatedMethod intConstructor = testBeanClass.getMethod("TestBean", int.class);
        SettableBeanProperty[] intConstructorArgs = new SettableBeanProperty[] { new SettableBeanProperty("intField", Integer.TYPE) };

        CreatorCollector collector = new CreatorCollector(testBeanDesc, mapperConfig);
        collector.addIntCreator(new AnnotatedWithParams(intConstructor, false), false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapperConfig.getConfig());
        assertEquals(TestBean.class, instantiator.getType());
        TestBean instantiatedBean = (TestBean) instantiator.createValueInstance(null, intValue);
        assertEquals(intValue, instantiatedBean.getIntField());

        TestBean testBeanInstance2 = new TestBean();
        instantiator.createValueInstance(testBeanInstance2, intValue);
        assertEquals(intValue, testBeanInstance2.getIntField());
    }

    @Test
    public void testLongCreator() throws Exception {
        TestBean testBeanInstance = new TestBean();
        long longValue = 1L;

        TestBean constructorWithLongArg = new TestBean(longValue);
        AnnotatedMethod longConstructor = testBeanClass.getMethod("TestBean", long.class);
        SettableBeanProperty[] longConstructorArgs = new SettableBeanProperty[] { new SettableBeanProperty("longField", Long.TYPE) };

        CreatorCollector collector = new CreatorCollector(testBeanDesc, mapperConfig);
        collector.addLongCreator(new AnnotatedWithParams(longConstructor, false), false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapperConfig.getConfig());
        assertEquals(TestBean.class, instantiator.getType());
        TestBean instantiatedBean = (TestBean) instantiator.createValueInstance(null, longValue);
        assertEquals(longValue, instantiatedBean.getLongField());

        TestBean testBeanInstance2 = new TestBean();
        instantiator.createValueInstance(testBeanInstance2, longValue);
        assertEquals(longValue, testBeanInstance2.getLongField());
    }

    @Test
    public void testDoubleCreator() throws Exception {
        TestBean testBeanInstance = new TestBean();
        double doubleValue = 1.0;

        TestBean constructorWithDoubleArg = new TestBean(doubleValue);
        AnnotatedMethod doubleConstructor = testBeanClass.getMethod("TestBean", double.class);
        SettableBeanProperty[] doubleConstructorArgs = new SettableBeanProperty[] { new SettableBeanProperty("doubleField", Double.TYPE) };

        CreatorCollector collector = new CreatorCollector(testBeanDesc, mapperConfig);
        collector.addDoubleCreator(new AnnotatedWithParams(doubleConstructor, false), false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapperConfig.getConfig());
        assertEquals(TestBean.class, instantiator.getType());
        TestBean instantiatedBean = (TestBean) instantiator.createValueInstance(null, doubleValue);
        assertEquals(doubleValue, instantiatedBean.getDoubleField(), 0.0);

        TestBean testBeanInstance2 = new TestBean();
        instantiator.createValueInstance(testBeanInstance2, doubleValue);
        assertEquals(doubleValue, testBeanInstance2.getDoubleField(), 0.0);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        TestBean testBeanInstance = new TestBean();
        boolean booleanValue = true;

        TestBean constructorWithBooleanArg = new TestBean(booleanValue);
        AnnotatedMethod booleanConstructor = testBeanClass.getMethod("TestBean", boolean.class);
        SettableBeanProperty[] booleanConstructorArgs = new SettableBeanProperty[] { new SettableBeanProperty("booleanField", Boolean.TYPE) };

        CreatorCollector collector = new CreatorCollector(testBeanDesc, mapperConfig);
        collector.addBooleanCreator(new AnnotatedWithParams(booleanConstructor, false), false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapperConfig.getConfig());
        assertEquals(TestBean.class, instantiator.getType());
        TestBean instantiatedBean = (TestBean) instantiator.createValueInstance(null, booleanValue);
        assertEquals(booleanValue, instantiatedBean.isBooleanField());

        TestBean testBeanInstance2 = new TestBean();
        instantiator.createValueInstance(testBeanInstance2, booleanValue);
        assertEquals(booleanValue, testBeanInstance2.isBooleanField());
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        TestDelegate testDelegateInstance = new TestDelegate();
        TestBean testBeanInstance = new TestBean();

        TestBean constructorWithDelegateArg = new TestBean(testDelegateInstance);
        AnnotatedMethod delegateConstructor = testBeanClass.getMethod("TestBean", TestDelegate.class);
        SettableBeanProperty[] delegateConstructorArgs = new SettableBeanProperty[] { new SettableBeanProperty("delegate", TestDelegate.class) };

        CreatorCollector collector = new CreatorCollector(testBeanDesc, mapperConfig);
        collector.addDelegatingCreator(new AnnotatedWithParams(delegateConstructor, false, new SettableBeanProperty[] { new SettableBeanProperty("delegate", TestDelegate.class) }), false, null);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapperConfig.getConfig());
        assertEquals(TestBean.class, instantiator.getType());
        TestBean instantiatedBean = (TestBean) instantiator.createValueInstance(null, testDelegateInstance);
        assertEquals(testDelegateInstance, instantiatedBean.getDelegate());

        TestBean testBeanInstance2 = new TestBean();
        instantiator.createValueInstance(testBeanInstance2, testDelegateInstance);
        assertEquals(testDelegateInstance, testBeanInstance2.getDelegate());
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        TestBean testBeanInstance = new TestBean();
        String stringValue = "test";
        int intValue = 1;

        TestBean constructorWithProperties = new TestBean(stringValue, intValue);
        BeanPropertyDefinition[] properties = testBeanClass.getProperties();

        Map<String, BeanPropertyDefinition> propertiesMap = new HashMap<>();
        for (BeanPropertyDefinition property : properties) {
            propertiesMap.put(property.getName(), property);
        }

        CreatorProperty[] propertyConstructors = new CreatorProperty[] {
                new CreatorProperty("str", String.class, propertiesMap.get("str")),
                new CreatorProperty("intField", Integer.TYPE, propertiesMap.get("intField"))
        };

        CreatorCollector collector = new CreatorCollector(testBeanDesc, mapperConfig);
        collector.addPropertyCreator(new AnnotatedWithParams(testBeanClass.getConstructor(String.class, int.class), false), false, propertyConstructors);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapperConfig.getConfig());
        assertEquals(TestBean.class, instantiator.getType());
        TestBean instantiatedBean = (TestBean) instantiator.createValueInstance(null, stringValue, intValue);
        assertEquals(stringValue, instantiatedBean.getStr());
        assertEquals(intValue, instantiatedBean.getIntField());

        TestBean testBeanInstance2 = new TestBean();
        instantiator.createValueInstance(testBeanInstance2, stringValue, intValue);
        assertEquals(stringValue, testBeanInstance2.getStr());
        assertEquals(intValue, testBeanInstance2.getIntField());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        TestBean testBeanInstance = new TestBean();

        TestBean constructorWithStringArg = new TestBean("stringValue");
        AnnotatedMethod stringConstructor = testBeanClass.getMethod("TestBean", String.class);
        SettableBeanProperty[] stringConstructorArgs = new SettableBeanProperty[] { new SettableBeanProperty("str", String.class) };

        TestBean constructorWithIntArg = new TestBean(1);
        AnnotatedMethod intConstructor = testBeanClass.getMethod("TestBean", int.class);
        SettableBeanProperty[] intConstructorArgs = new SettableBeanProperty[] { new SettableBeanProperty("intField", Integer.TYPE) };

        CreatorCollector collector = new CreatorCollector(testBeanDesc, mapperConfig);
        collector.addStringCreator(new AnnotatedWithParams(stringConstructor, false), false);
        collector.addIntCreator(new AnnotatedWithParams(intConstructor, false), false);

        Method methodThatCreatesWithBothArgs = testBeanClass.getMethod("TestBean", String.class, int.class);
        AnnotatedWithParams creatorWithBothArgs = new AnnotatedWithParams(methodThatCreatesWithBothArgs, new Object[0]);

        collector.addStringCreator(creatorWithBothArgs, false);
    }
}

class TestBean {
    private String str;
    private int intField;
    private long longField;
    private double doubleField;
    private boolean booleanField;
    private TestDelegate delegate;

    public TestBean() {
    }

    public TestBean(String str) {
        this.str = str;
    }

    public TestBean(int intField) {
        this.intField = intField;
    }

    public TestBean(long longField) {
        this.longField = longField;
    }

    public TestBean(double doubleField) {
        this.doubleField = doubleField;
    }

    public TestBean(boolean booleanField) {
        this.booleanField = booleanField;
    }

    public TestBean(String str, int intField) {
        this.str = str;
        this.intField = intField;
    }

    public String getStr() {
        return str;
    }

    public int getIntField() {
        return intField;
    }

    public long getLongField() {
        return longField;
    }

    public double getDoubleField() {
        return doubleField;
    }

    public boolean isBooleanField() {
        return booleanField;
    }

    public TestDelegate getDelegate() {
        return delegate;
    }

    public static class TestDelegate {
    }
}