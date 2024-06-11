package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private ObjectMapper mapper;
    private CreatorCollector collector;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDefaultCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(HashMap.class);
        CreatorCollector cc = new CreatorCollector(beanDesc, true);
        JavaType type = beanDesc.getType();
        cc.setDefaultCreator(new AnnotatedMethod(HashMap.class.getDeclaredConstructor()));
        StdValueInstantiator instantiator = (StdValueInstantiator) cc.constructValueInstantiator(mapper.getConfig());
        Map<String, Object> map = instantiator.createFromEmptyValueContainer(mapper.getConfig(), null);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testStringCreator() throws IOException {
        AnnotatedClass<MyClass> myClass = mapper.getTypeFactory().constructType(MyClass.class);
        CreatorCollector cc = new CreatorCollector(myClass.getInstanceDesc(), true);
        Method constructor = MyClass.class.getDeclaredConstructor(String.class);
        cc.addStringCreator(new AnnotatedMethod(constructor), true);
        StdValueInstantiator instantiator = (StdValueInstantiator) cc.constructValueInstantiator(mapper.getConfig());
        MyClass obj = (MyClass) instantiator.createValueInstance(null, "test");
        assertEquals("test", obj.getValue());
    }

    @Test
    public void testIntCreator() throws IOException {
        AnnotatedClass<MyIntClass> myIntClass = mapper.getTypeFactory().constructType(MyIntClass.class);
        CreatorCollector cc = new CreatorCollector(myIntClass.getInstanceDesc(), true);
        Method constructor = MyIntClass.class.getDeclaredConstructor(int.class);
        cc.addIntCreator(new AnnotatedMethod(constructor), true);
        StdValueInstantiator instantiator = (StdValueInstantiator) cc.constructValueInstantiator(mapper.getConfig());
        MyIntClass obj = (MyIntClass) instantiator.createValueInstance(null, 42);
        assertEquals(42, obj.getValue());
    }

    @Test
    public void testLongCreator() throws IOException {
        AnnotatedClass<MyLongClass> myLongClass = mapper.getTypeFactory().constructType(MyLongClass.class);
        CreatorCollector cc = new CreatorCollector(myLongClass.getInstanceDesc(), true);
        Method constructor = MyLongClass.class.getDeclaredConstructor(long.class);
        cc.addLongCreator(new AnnotatedMethod(constructor), true);
        StdValueInstantiator instantiator = (StdValueInstantiator) cc.constructValueInstantiator(mapper.getConfig());
        MyLongClass obj = (MyLongClass) instantiator.createValueInstance(null, 42L);
        assertEquals(42L, obj.getValue());
    }

    @Test
    public void testDoubleCreator() throws IOException {
        AnnotatedClass<MyDoubleClass> myDoubleClass = mapper.getTypeFactory().constructType(MyDoubleClass.class);
        CreatorCollector cc = new CreatorCollector(myDoubleClass.getInstanceDesc(), true);
        Method constructor = MyDoubleClass.class.getDeclaredConstructor(double.class);
        cc.addDoubleCreator(new AnnotatedMethod(constructor), true);
        StdValueInstantiator instantiator = (StdValueInstantiator) cc.constructValueInstantiator(mapper.getConfig());
        MyDoubleClass obj = (MyDoubleClass) instantiator.createValueInstance(null, 3.14);
        assertEquals(3.14, obj.getValue(), 0.001);
    }

    @Test
    public void testBooleanCreator() throws IOException {
        AnnotatedClass<MyBooleanClass> myBooleanClass = mapper.getTypeFactory().constructType(MyBooleanClass.class);
        CreatorCollector cc = new CreatorCollector(myBooleanClass.getInstanceDesc(), true);
        Method constructor = MyBooleanClass.class.getDeclaredConstructor(boolean.class);
        cc.addBooleanCreator(new AnnotatedMethod(constructor), true);
        StdValueInstantiator instantiator = (StdValueInstantiator) cc.constructValueInstantiator(mapper.getConfig());
        MyBooleanClass obj = (MyBooleanClass) instantiator.createValueInstance(null, false);
        assertFalse(obj.getValue());
    }

    @Test
    public void testDelegatingCreator() throws IOException {
        AnnotatedClass<MyDelegatingClass> myDelegatingClass = mapper.getTypeFactory().constructType(MyDelegatingClass.class);
        CreatorCollector cc = new CreatorCollector(myDelegatingClass.getInstanceDesc(), true);
        Method constructor = MyDelegatingClass.class.getDeclaredConstructor(String.class, int.class);
        AnnotatedMethod injectorMethod = new AnnotatedMethod(MyDelegatingClass.class.getMethod("setInjectable", String.class));
        cc.addDelegatingCreator(new AnnotatedMethod(constructor), true, new AnnotatedMethod[0]);
        cc.addDelegatingCreator(injectorMethod, true, new AnnotatedMethod[] { new AnnotatedMethod(MyInjectedClass.class.getDeclaredConstructor(String.class)) });
        StdValueInstantiator instantiator = (StdValueInstantiator) cc.constructValueInstantiator(mapper.getConfig());
        MyDelegatingClass obj = (MyDelegatingClass) instantiator.createValueInstance(null, "test", 42);
        assertEquals("test", obj.getInjectedValue());
        MyInjectedClass injected = obj.getInjected();
        assertEquals("test", injected.getValue());
    }

    @Test
    public void testPropertyCreator() throws IOException {
        AnnotatedClass<MyPropertyClass> myPropertyClass = mapper.getTypeFactory().constructType(MyPropertyClass.class);
        CreatorCollector cc = new CreatorCollector(myPropertyClass.getInstanceDesc(), true);
        Method constructor = MyPropertyClass.class.getDeclaredConstructor();
        AnnotatedMember property1 = new AnnotatedField(MyPropertyClass.class.getDeclaredField("property1"));
        AnnotatedMember property2 = new AnnotatedField(MyPropertyClass.class.getDeclaredField("property2"));
        cc.addPropertyCreator(new AnnotatedMethod(constructor), true, new CreatorProperty[] { new CreatorProperty(property1), new CreatorProperty(property2) });
        StdValueInstantiator instantiator = (StdValueInstantiator) cc.constructValueInstantiator(mapper.getConfig());
        MyPropertyClass obj = (MyPropertyClass) instantiator.createValueInstance(null);
        assertEquals("value1", obj.getProperty1().getValue());
        assertEquals("value2", obj.getProperty2().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws IOException {
        AnnotatedClass<MyClass> myClass = mapper.getTypeFactory().constructType(MyClass.class);
        CreatorCollector cc = new CreatorCollector(myClass.getInstanceDesc(), true);
        Method constructor1 = MyClass.class.getDeclaredConstructor(String.class);
        Method constructor2 = MyClass.class.getDeclaredConstructor(int.class);
        cc.addStringCreator(new AnnotatedMethod(constructor1), true);
        cc.addIntCreator(new AnnotatedMethod(constructor2), true);
        try {
            cc.constructValueInstantiator(mapper.getConfig());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Conflicting String creators"));
            assertTrue(message.contains("already had explicitly marked " + constructor1.getName()));
            throw e;
        }
    }

    private static class MyClass {
        private String value;
        
        @JsonCreator
        public MyClass(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    private static class MyIntClass {
        private int value;
        
        @JsonCreator
        public MyIntClass(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    private static class MyLongClass {
        private long value;
        
        @JsonCreator
        public MyLongClass(long value) {
            this.value = value;
        }
        
        public long getValue() {
            return value;
        }
    }

    private static class MyDoubleClass {
        private double value;
        
        @JsonCreator
        public MyDoubleClass(double value) {
            this.value = value;
        }
        
        public double getValue() {
            return value;
        }
    }

    private static class MyBooleanClass {
        private boolean value;
        
        @JsonCreator
        public MyBooleanClass(boolean value) {
            this.value = value;
        }
        
        public boolean getValue() {
            return value;
        }
    }

    private static class MyDelegatingClass {
        private String injectedValue;
        private MyInjectedClass injected;
        
        public MyDelegatingClass(String injectedValue, int i) {
            this.injectedValue = injectedValue;
            this.injected = new MyInjectedClass(injectedValue);
        }
        
        public String getInjectedValue() {
            return injectedValue;
        }
        
        public MyInjectedClass getInjected() {
            return injected;
        }
        
        @JsonProperty("injected")
        public void setInjected(MyInjectedClass value) {
            this.injected = value;
        }
    }

    private static class MyInjectedClass {
        private String value;
        
        public MyInjectedClass(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    private static class CreatorProperty {
        private AnnotatedMember member;

        public CreatorProperty(AnnotatedMember member) {
            this.member = member;
        }
        
        public AnnotatedMember getMember() {
            return member;
        }
    }
}