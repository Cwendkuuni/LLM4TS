package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.AnnotatedClass;
import com.fasterxml.jackson.core.base.AnnotatedMember;
import com.fasterxml.jackson.core.type.AnnotatedConstructor;
import com.fasterxml.jackson.core.type.AnnotatedMemberInstance;
import com.fasterxml.jackson.core.type.AnnotatedParameter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

/**
 * Test for {@link CreatorCollector} class
 */
public class CreatorCollectorTest {

    private CreatorCollector collector;
    private MapperConfig<?> config;

    @Before
    public void setUp() throws Exception {
        JavaClass beanClass = new JavaClass(MyTestBean.class);
        config = new MapperFactory().configure(new MapperConfig.Builder()
                .build());
        collector = new CreatorCollector(beanClass.getAnnotation(BeanDescription.class), config);
    }

    @Test
    public void testDefaultCreator() {
        AnnotatedConstructor defaultConstructor = beanClass.getAnnotation(MyTestBean.class.getDeclaredConstructor());
        collector.setDefaultCreator(new AnnotatedWithParams(defaultConstructor));
        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        MyTestBean obj = instantiator.createFromEmptyValueContext(null);
        assertNotNull(obj);
        assertTrue(obj instanceof MyTestBean);
    }

    @Test
    public void testStringCreator() {
        AnnotatedConstructor stringConstructor = beanClass.getAnnotation(MyTestBean.class.getDeclaredConstructor(String.class));
        collector.addStringCreator(new AnnotatedWithParams(stringConstructor), false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        MyTestBean obj = (MyTestBean) instantiator.createValueInstance(new String[]{"test"});
        assertNotNull(obj);
        assertTrue(obj instanceof MyTestBean);
        assertEquals("test", obj.getTestString());
    }

    @Test
    public void testIntCreator() {
        AnnotatedConstructor intConstructor = beanClass.getAnnotation(MyTestBean.class.getDeclaredConstructor(int.class));
        collector.addIntCreator(new AnnotatedWithParams(intConstructor), false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        MyTestBean obj = (MyTestBean) instantiator.createValueInstance(new int[]{1});
        assertNotNull(obj);
        assertTrue(obj instanceof MyTestBean);
        assertEquals(1, obj.getIntValue());
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        // Creator with Collection type argument
        AnnotatedConstructor arrayDelegateConstructor = beanClass.getAnnotation(MyTestBeanArray.class.getDeclaredConstructor(MyTestBean[].class));
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        injectables[0] = new SettableBeanProperty.Std(JavaType.constructCollectionType(List.class, MyTestBean.class), "list");
        collector.addDelegatingCreator(new AnnotatedWithParams(arrayDelegateConstructor), false, injectables);

        // Creator with single argument
        AnnotatedConstructor singleDelegateConstructor = beanClass.getAnnotation(MyTestBeanDelegate.class.getDeclaredConstructor(MyTestBean.class));
        SettableBeanProperty[] injectablesSingle = new SettableBeanProperty[1];
        injectablesSingle[0] = new SettableBeanProperty.Std(JavaType.construct(MyTestBean.class), "delegate");
        collector.addDelegatingCreator(new AnnotatedWithParams(singleDelegateConstructor), false, injectablesSingle);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());

        // Test with null input
        assertNull(instantiator.createValueInstance(new Object[0]));

        // Test with valid input
        MyTestBean[] input = new MyTestBean[2];
        input[0] = new MyTestBean();
        input[1] = new MyTestBean();
        MyTestBeanDelegate obj = (MyTestBeanDelegate) instantiator.createValueInstance(input);
        assertNotNull(obj);
        assertTrue(obj instanceof MyTestBeanDelegate);
        assertEquals(input[0], obj.getDelegate());
        assertNotNull(obj.getList());
        assertEquals(2, obj.getList().size());
        assertEquals(input[0], obj.getList().get(0));
        assertEquals(input[1], obj.getList().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() {
        AnnotatedConstructor stringConstructor = beanClass.getAnnotation(MyTestBean.class.getDeclaredConstructor(String.class));
        collector.addStringCreator(new AnnotatedWithParams(stringConstructor), false);
        collector.addStringCreator(new AnnotatedWithParams(stringConstructor), false);
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        // Creator with single property
        AnnotatedConstructor propertyBasedConstructor = beanClass.getAnnotation(MyTestBeanProperty.class.getDeclaredConstructor(MyTestProperty.class));
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        properties[0] = new SettableBeanProperty.Std(JavaType.construct(MyTestProperty.class), "property");
        collector.addPropertyCreator(new AnnotatedWithParams(propertyBasedConstructor), false, properties);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());

        MyTestBeanProperty obj = instantiator.createValueInstance(new Object[0]);
        assertNotNull(obj);
        assertTrue(obj instanceof MyTestBeanProperty);
        assertEquals("test", obj.getProperty().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyCreator() {
        AnnotatedConstructor propertyBasedConstructor = beanClass.getAnnotation(MyTestBeanProperty.class.getDeclaredConstructor(MyTestProperty.class));
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        properties[0] = new SettableBeanProperty.Std(JavaType.construct(MyTestProperty.class), "property");
        collector.addPropertyCreator(new AnnotatedWithParams(propertyBasedConstructor), false, properties);
        collector.addPropertyCreator(new AnnotatedWithParams(propertyBasedConstructor), false, properties);
    }

    @Test
    public void testIncompleteParameter() {
        AnnotatedParameter incompleteParameter = JavaClass.getAnnotated(MyTestBean.class, "incomplete");
        collector.addIncompleteParameter(incompleteParameter);
        assertNull(collector.constructValueInstantiator(new DeserializationConfig()).createValueInstance(new Object[0]));
    }
}

class MyTestBean {
    String testString;
    int intValue;

    MyTestBean() {}

    MyTestBean(String testString) {
        this.testString = testString;
    }

    MyTestBean(int intValue) {
        this.intValue = intValue;
    }
}

class MyTestBeanArray {
    List<MyTestBean> list;

    MyTestBeanArray(List<MyTestBean> list) {
        this.list = list;
    }
}

class MyTestBeanDelegate {
    MyTestBean delegate;
    List<MyTestBean> list;

    MyTestBeanDelegate(MyTestBean delegate, List<MyTestBean> list) {
        this.delegate = delegate;
        this.list = list;
    }

    public MyTestBean getDelegate() {
        return delegate;
    }

    public List<MyTestBean> getList() {
        return list;
    }
}

class MyTestBeanProperty {
    MyTestProperty property;

    MyTestBeanProperty(MyTestProperty property) {
        this.property = property;
    }

    public MyTestProperty getProperty() {
        return property;
    }
}

class MyTestProperty {
    String value;

    MyTestProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}