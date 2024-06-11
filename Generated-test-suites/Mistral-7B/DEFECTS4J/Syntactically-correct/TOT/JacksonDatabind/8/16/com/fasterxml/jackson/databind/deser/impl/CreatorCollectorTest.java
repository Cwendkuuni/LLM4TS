package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.*;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = new CreatorCollector(mapper.getTypeFactory().constructType(Collection.class), true);
        BeanDescription beanDesc = mapper.findBeanDescription(Collection.class);

        collector.setDefaultCreator(mapper.findValueDeserializer(beanDesc, Collection.class, null));
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        Collection<Object> collection = inst.createValueInstance(mapper, ObjectNode.class, null);
        Assert.assertNotNull(collection);
        Assert.assertEquals(collection.size(), 0);
    }

    @Test
    public void testStringCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = new CreatorCollector(mapper.getTypeFactory().constructType(String.class), true);
        BeanDescription beanDesc = mapper.findBeanDescription(String.class);

        String value = "test";
        AnnotatedWithParams creator = mapper.findValueDeserializerAnnotation(beanDesc, String.class, "value");
        collector.addStringCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        String string = inst.createValueInstance(mapper, String.class, null);
        Assert.assertEquals(string, value);
    }

    @Test
    public void testIntCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = new CreatorCollector(mapper.getTypeFactory().constructType(int[].class), true);
        BeanDescription beanDesc = mapper.findBeanDescription(int[].class);

        int value = 5;
        AnnotatedWithParams creator = mapper.findValueDeserializerAnnotation(beanDesc, int[].class, "value");
        collector.addIntCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        int[] ints = inst.createValueInstance(mapper, int[].class, null);
        Assert.assertNotNull(ints);
        Assert.assertEquals(ints.length, 0);
        Assert.assertEquals(ints[0], value);
    }

    @Test
    public void testLongCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = new CreatorCollector(mapper.getTypeFactory().constructType(long[].class), true);
        BeanDescription beanDesc = mapper.findBeanDescription(long[].class);

        long value = 10L;
        AnnotatedWithParams creator = mapper.findValueDeserializerAnnotation(beanDesc, long[].class, "value");
        collector.addLongCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        long[] longs = inst.createValueInstance(mapper, long[].class, null);
        Assert.assertNotNull(longs);
        Assert.assertEquals(longs.length, 0);
        Assert.assertEquals(longs[0], value);
    }

    @Test
    public void testDoubleCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = new CreatorCollector(mapper.getTypeFactory().constructType(double[].class), true);
        BeanDescription beanDesc = mapper.findBeanDescription(double[].class);

        double value = 3.14;
        AnnotatedWithParams creator = mapper.findValueDeserializerAnnotation(beanDesc, double[].class, "value");
        collector.addDoubleCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        double[] doubles = inst.createValueInstance(mapper, double[].class, null);
        Assert.assertNotNull(doubles);
        Assert.assertEquals(doubles.length, 0);
        Assert.assertEquals(doubles[0], value);
    }

    @Test
    public void testBooleanCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = new CreatorCollector(mapper.getTypeFactory().constructType(boolean[].class), true);
        BeanDescription beanDesc = mapper.findBeanDescription(boolean[].class);

        boolean value = true;
        AnnotatedWithParams creator = mapper.findValueDeserializerAnnotation(beanDesc, boolean[].class, "value");
        collector.addBooleanCreator(creator, true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        boolean[] booleans = inst.createValueInstance(mapper, boolean[].class, null);
        Assert.assertNotNull(booleans);
        Assert.assertEquals(booleans.length, 0);
        Assert.assertEquals(booleans[0], value);
    }

    @Test
    public void testCustomCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = new CreatorCollector(mapper.getTypeFactory().constructType(MyClass.class), true);
        BeanDescription beanDesc = mapper.findBeanDescription(MyClass.class);

        MyClass instance = new MyClass();
        AnnotatedWithParams creator = mapper.findValueDeserializerAnnotation(beanDesc, MyClass.class, "custom");
        collector.addDelegatingCreator(creator, null, new CreatorProperty[0]);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        MyClass result = inst.createValueInstance(mapper, MyClass.class, null);
        Assert.assertEquals(result, instance);
    }

    @Test
    public void testPropertyBasedCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = new CreatorCollector(mapper.getTypeFactory().constructType(MyClass.class), true);
        BeanDescription beanDesc = mapper.findBeanDescription(MyClass.class);

        MyClass instance = new MyClass();
        CreatorProperty property1 = mapper.findInjectableValue("property1");
        CreatorProperty property2 = mapper.findInjectableValue("property2");
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = property1;
        properties[1] = property2;
        AnnotatedWithParams creator = mapper.findValueDeserializerAnnotation(beanDesc, MyClass.class, "property-based");
        collector.addPropertyCreator(creator, true, properties);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        MyClass result = inst.createValueInstance(mapper, MyClass.class, null);
        Assert.assertEquals(result, instance);
    }

    @Test
    public void testErrorScenario() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = new CreatorCollector(mapper.getTypeFactory().constructType(MyClass.class), true);
        BeanDescription beanDesc = mapper.findBeanDescription(MyClass.class);

        MyClass instance = new MyClass();
        AnnotatedWithParams creator = mapper.findValueDeserializerAnnotation(beanDesc, MyClass.class, "invalid");
        collector.addStringCreator(creator, false); // should fail due to incompatible types
        try {
            collector.constructValueInstantiator(mapper.getConfig());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIncompleteParameter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorCollector collector = new CreatorCollector(mapper.getTypeFactory().constructType(MyClass.class), true);
        BeanDescription beanDesc = mapper.findBeanDescription(MyClass.class);

        AnnotatedParameter parameter = mapper.findInjectableValue("incomplete");
        collector.addIncompleteParameter(parameter);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
        try {
            inst.createValueInstance(mapper, MyClass.class, null);
            Assert.fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    public static class MyClass {
        @JsonCreator
        public MyClass(@JsonProperty("property1") String property1, @JsonProperty("property2") int property2) {}
    }
}