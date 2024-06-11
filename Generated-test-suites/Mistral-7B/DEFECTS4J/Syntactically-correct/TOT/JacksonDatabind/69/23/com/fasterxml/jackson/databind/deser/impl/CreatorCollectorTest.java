package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithMembers;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.testng.Assert.*;

public class CreatorCollectorTest {

    private ObjectMapper objectMapper;
    private CreatorCollector creatorCollector;

    @BeforeMethod
    public void setUp() throws Exception {
        MapperFactory factory = new MapperFactory();
        factory.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper = factory.getObjectMapper();

        BeanDescription beanDescription = objectMapper.getTypeFactory().constructType(Pojo.class);
        creatorCollector = new CreatorCollector(beanDescription, factory.getConfig());
    }

    @Test
    public void testDefaultCreator() throws Exception {
        AnnotatedWithParams defaultCreator = creatorCollector.getAnnotatedConstructor(Pojo.class);
        creatorCollector.setDefaultCreator(defaultCreator);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Pojo pojo = instantiator.createValueInstance(null);
        assertNotNull(pojo);
        assertEquals(pojo.getValue(), "");
    }

    @Test
    public void testStringCreator() throws Exception {
        AnnotatedMethod stringCreator = new AnnotatedMethod(Pojo.class, "valueOf", new Class[]{}, String.class);
        creatorCollector.addStringCreator(stringCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Pojo pojo = instantiator.createValueInstance(new String[]{"Hello World"});
        assertNotNull(pojo);
        assertEquals(pojo.getValue(), "Hello World");
    }

    @Test
    public void testIntCreator() throws Exception {
        AnnotatedMethod intCreator = new AnnotatedMethod(Pojo.class, "<init>", new Class[]{}, int.class);
        creatorCollector.addIntCreator(intCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Pojo pojo = instantiator.createValueInstance(new Integer(42));
        assertNotNull(pojo);
        assertEquals(pojo.getValue(), 42);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        AnnotatedField booleanField = new AnnotatedField(Pojo.class, "booleanValue", boolean.class);
        creatorCollector.addBooleanCreator(booleanField, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Pojo pojo = instantiator.createValueInstance(new Boolean(true));
        assertNotNull(pojo);
        assertEquals(pojo.getValue(), true);
    }

    @Test
    public void testCustomCreator() throws Exception {
        PojoCustomCreator pojoCustomCreator = new PojoCustomCreator();
        AnnotatedWithParams customCreator = new AnnotatedWithParams(pojoCustomCreator, new Class[]{}, Object[].class);
        creatorCollector.addPropertyCreator(customCreator, false, new SettableBeanProperty[0]);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Pojo pojo = instantiator.createValueInstance(new Object[]{42, "Hello World"});
        assertNotNull(pojo);
        assertEquals(pojo.getValue(), 42);
        assertEquals(pojo.getStringValue(), "Hello World");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        AnnotatedMethod stringCreator = new AnnotatedMethod(Pojo.class, "valueOf", new Class[]{}, String.class);
        creatorCollector.addStringCreator(stringCreator, false);
        creatorCollector.addStringCreator(stringCreator, false);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
    }

    private static class Pojo {
        private String value;
        private int integerValue;
        private boolean booleanValue;
        private String stringValue;

        public Pojo() {}

        public Pojo(String value) { this.value = value; }

        public Pojo(int value) { this.integerValue = value; }

        public Pojo(boolean value) { this.booleanValue = value; }

        public static Pojo valueOf(String value) { return new Pojo(value); }

        public static Pojo valueOf(int value) { return new Pojo(value); }

        public static Pojo valueOf(boolean value) { return new Pojo(value); }
    }

    private static class PojoCustomCreator {
        public Pojo create(int value, String stringValue) {
            Pojo pojo = new Pojo();
            pojo.setValue(Integer.toString(value));
            pojo.setStringValue(stringValue);
            return pojo;
        }
    }
}