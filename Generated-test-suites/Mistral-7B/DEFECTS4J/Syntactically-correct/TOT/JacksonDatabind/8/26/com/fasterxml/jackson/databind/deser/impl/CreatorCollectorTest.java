package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatorCollectorTest extends TestBase {

    private ObjectMapper mapper;

    @BeforeClass
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDefaultCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(MyClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.setDefaultCreator(new AnnotatedWithParams(MyClass.class.getDeclaredConstructor(), null));

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig(mapper.getConfig()));
        MyClass obj = inst.createValueInstance(null, null);
        Assert.assertNotNull(obj);
    }

    @Test
    public void testStringCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(MyClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.addStringCreator(new AnnotatedWithParams(MyClass.class.getDeclaredConstructor("String"), null), false);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig(mapper.getConfig()));
        MyClass obj = inst.createValueInstance(null, Arrays.asList("hello"));
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj.getStringValue(), "hello");
    }

    @Test
    public void testIntCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(MyClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.addIntCreator(new AnnotatedWithParams(MyClass.class.getDeclaredConstructor(int.class), null), false);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig(mapper.getConfig()));
        MyClass obj = inst.createValueInstance(null, 42);
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj.getIntValue(), 42);
    }

    @Test
    public void testLongCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(MyClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.addLongCreator(new AnnotatedWithParams(MyClass.class.getDeclaredConstructor(long.class), null), false);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig(mapper.getConfig()));
        MyClass obj = inst.createValueInstance(null, 42L);
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj.getLongValue(), 42L);
    }

    @Test
    public void testDoubleCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(MyClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.addDoubleCreator(new AnnotatedWithParams(MyClass.class.getDeclaredConstructor(double.class), null), false);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig(mapper.getConfig()));
        MyClass obj = inst.createValueInstance(null, 42.0);
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj.getDoubleValue(), 42.0);
    }

    @Test
    public void testBooleanCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(MyClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        collector.addBooleanCreator(new AnnotatedWithParams(MyClass.class.getDeclaredConstructor(boolean.class), null), false);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig(mapper.getConfig()));
        MyClass obj = inst.createValueInstance(null, true);
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj.getBooleanValue(), true);
    }

    @Test
    public void testDelegateCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(MyClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        Class<?> delegateType = MyDelegate.class;
        Method delegateConstructor = delegateType.getDeclaredMethod("delegate");
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(delegateConstructor, null);

        CreatorProperty[] delegateArgs = new CreatorProperty[1];
        delegateArgs[0] = new CreatorProperty("arg1", String.class);

        collector.addDelegatingCreator(new AnnotatedWithParams(MyClass.class.getDeclaredConstructor(delegateType), null), false, delegateArgs);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig(mapper.getConfig()));
        MyClass obj = inst.createValueInstance(null, new MyDelegate("hello"));
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj.getDelegate().getStringValue(), "hello");
    }

    @Test
    public void testPropertyBasedCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(MyClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        JavaType delegateType = mapper.getTypeFactory().constructType(MyDelegate.class);
        Class<?> stringType = String.class;

        CreatorProperty[] propertyArgs = new CreatorProperty[2];
        propertyArgs[0] = new CreatorProperty("arg1", stringType);
        propertyArgs[1] = new CreatorProperty("arg2", delegateType);

        AnnotatedWithParams propertyCreator = new AnnotatedWithParams(MyClass.class.getDeclaredConstructor(String.class, MyDelegate.class), null);
        collector.addPropertyCreator(propertyCreator, false, propertyArgs);

        ValueInstantiator inst = collector.constructValueInstantiator(new DeserializationConfig(mapper.getConfig()));
        MyClass obj = inst.createValueInstance(null, "hello", new MyDelegate("world"));
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj.getStringValue(), "hello");
        Assert.assertEquals(obj.getDelegate().getStringValue(), "world");
    }

    @Test
    public void testMissingCreator() throws IOException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(MyClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        try {
            collector.constructValueInstantiator(new DeserializationConfig(mapper.getConfig()));
            Assert.fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    private static class MyClass {
        private int intValue;
        private long longValue;
        private double doubleValue;
        private boolean booleanValue;
        private String stringValue;
        private MyDelegate delegate;

        MyClass() {}

        @JsonCreator
        public MyClass(@JsonDeserialize(using=MyDelegateDeserializer.class) MyDelegate delegate) {
            this.delegate = delegate;
        }

        public MyClass(String stringValue) {
            this.stringValue = stringValue;
        }

        public MyClass(int intValue) {
            this.intValue = intValue;
        }

        public MyClass(long longValue) {
            this.longValue = longValue;
        }

        public MyClass(double doubleValue) {
            this.doubleValue = doubleValue;
        }

        public MyClass(boolean booleanValue) {
            this.booleanValue = booleanValue;
        }

        public int getIntValue() { return intValue; }
        public long getLongValue() { return longValue; }
        public double getDoubleValue() { return doubleValue; }
        public boolean getBooleanValue() { return booleanValue; }
        public String getStringValue() { return stringValue; }

        public MyDelegate getDelegate() { return delegate; }
    }

    private static class MyDelegate {
        private String value;

        MyDelegate(String value) {
            this.value = value;
        }

        public String getStringValue() { return value; }
    }

    private static class MyDelegateDeserializer extends StdDeserializer<MyDelegate> {
        public MyDelegateDeserializer() { this(null); }
        public MyDelegateDeserializer(Class<?> vc) { super(vc); }

        @Override
        public MyDelegate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new MyDelegate(p.getValueAsString());
        }
    }
}