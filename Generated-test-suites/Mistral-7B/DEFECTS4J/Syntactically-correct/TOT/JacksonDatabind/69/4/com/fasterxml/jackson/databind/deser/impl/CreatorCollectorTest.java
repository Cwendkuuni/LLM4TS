package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.AnnotatedClass;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class CreatorCollectorTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDefaultCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBean.class, new AnnotatedClass(TestBean.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.setDefaultCreator(new StandardMethodCreator(TestBean.class));
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        Assert.assertNotNull(inst.createValueInstance(null));
    }

    @Test
    public void testStringCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanString.class, new AnnotatedClass(TestBeanString.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        String value = "test";
        collector.addStringCreator(new AnnotatedWithParams(TestBeanString.class, "StringCreator", new String[0]), false);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        TestBeanString result = (TestBeanString) inst.createValueInstance(value);
        Assert.assertEquals(result.getValue(), value);
    }

    @Test
    public void testIntCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanInt.class, new AnnotatedClass(TestBeanInt.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        int value = 42;
        collector.addIntCreator(new AnnotatedWithParams(TestBeanInt.class, "IntCreator", new int[0]), false);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        TestBeanInt result = (TestBeanInt) inst.createValueInstance(value);
        Assert.assertEquals(result.getValue(), value);
    }

    @Test
    public void testLongCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanLong.class, new AnnotatedClass(TestBeanLong.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        long value = 42L;
        collector.addLongCreator(new AnnotatedWithParams(TestBeanLong.class, "LongCreator", new long[0]), false);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        TestBeanLong result = (TestBeanLong) inst.createValueInstance(value);
        Assert.assertEquals(result.getValue(), value);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanDouble.class, new AnnotatedClass(TestBeanDouble.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        double value = 42.0;
        collector.addDoubleCreator(new AnnotatedWithParams(TestBeanDouble.class, "DoubleCreator", new double[0]), false);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        TestBeanDouble result = (TestBeanDouble) inst.createValueInstance(value);
        Assert.assertEquals(result.getValue(), value, 0.0);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanBoolean.class, new AnnotatedClass(TestBeanBoolean.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        boolean value = true;
        collector.addBooleanCreator(new AnnotatedWithParams(TestBeanBoolean.class, "BooleanCreator", new boolean[0]), false);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        TestBeanBoolean result = (TestBeanBoolean) inst.createValueInstance(value);
        Assert.assertEquals(result.getValue(), value);
    }

    @Test
    public void testDelegateCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanDelegate.class, new AnnotatedClass(TestBeanDelegate.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        TestDelegate delegate = new TestDelegate();
        collector.addDelegatingCreator(new AnnotatedWithParams(TestBeanDelegate.class, "DelegateCreator", new Object[] { delegate }), false, new SettableBeanProperty[0]);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        TestBeanDelegate result = (TestBeanDelegate) inst.createValueInstance(null);
        Assert.assertEquals(result.getValue(), delegate);
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanProperties.class, new AnnotatedClass(TestBeanProperties.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        TestBeanProperties.Builder builder = TestBeanProperties.builder();
        builder.setValue1("test1");
        builder.setValue2(42);
        TestBeanProperties value = builder.build();

        collector.addPropertyCreator(new AnnotatedWithParams(TestBeanProperties.class, "PropertyBasedCreator", new Object[] { null }), false, new SettableBeanProperty[] {
                BeanProperty.Standard.forFieldAnnotation(TestBeanProperties.class, "value1"),
                BeanProperty.Standard.forFieldAnnotation(TestBeanProperties.class, "value2")
        });
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        TestBeanProperties result = (TestBeanProperties) inst.createValueInstance(null);
        Assert.assertEquals(result.getValue1(), value.getValue1());
        Assert.assertEquals(result.getValue2(), value.getValue2());
    }

    @Test
    public void testIncompleteParameter() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanWithIncompleteParameter.class, new AnnotatedClass(TestBeanWithIncompleteParameter.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.addIncompleteParameter(new AnnotatedParameter(TestBeanWithIncompleteParameter.class.getDeclaredField("field")));
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        TestBeanWithIncompleteParameter result = (TestBeanWithIncompleteParameter) inst.createValueInstance(null);
        Assert.assertNotNull(result.getField());
    }

    @Test
    public void testMultipleCreators() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanMultipleCreators.class, new AnnotatedClass(TestBeanMultipleCreators.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.setDefaultCreator(new StandardMethodCreator(TestBeanMultipleCreators.class));
        collector.addStringCreator(new AnnotatedWithParams(TestBeanMultipleCreators.class, "StringCreator", new String[0]), true);
        collector.addIntCreator(new AnnotatedWithParams(TestBeanMultipleCreators.class, "IntCreator", new int[0]), true);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        Object value1 = inst.createValueInstance("test");
        TestBeanMultipleCreators result1 = (TestBeanMultipleCreators) value1;
        Assert.assertEquals(result1.getValue1(), "test");
        Assert.assertEquals(result1.getValue2(), TestBeanMultipleCreators.DEFAULT_VALUE);

        Object value2 = inst.createValueInstance(42);
        TestBeanMultipleCreators result2 = (TestBeanMultipleCreators) value2;
        Assert.assertEquals(result2.getValue1(), TestBeanMultipleCreators.DEFAULT_VALUE);
        Assert.assertEquals(result2.getValue2(), 42);
    }

    @Test
    public void testExplicitCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanExplicitCreator.class, new AnnotatedClass(TestBeanExplicitCreator.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.setDefaultCreator(new StandardMethodCreator(TestBeanExplicitCreator.class));
        collector.addIntCreator(new AnnotatedWithParams(TestBeanExplicitCreator.class, "IntCreator", new int[0]), true);

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        Assert.assertNull(inst.createValueInstance(null));
        Assert.assertNull(inst.createValueInstance(42));

        collector.addIntCreator(new AnnotatedWithParams(TestBeanExplicitCreator.class, "IntCreator", new int[0]), false);

        TestBeanExplicitCreator result = (TestBeanExplicitCreator) inst.createValueInstance(42);
        Assert.assertEquals(result.getValue(), 42);
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanArrayDelegate.class, new AnnotatedClass(TestBeanArrayDelegate.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        Object[] values = new Object[] {"test1", "test2"};
        collector.addDelegatingCreator(new AnnotatedWithParams(TestBeanArrayDelegate.class, "ArrayDelegateCreator", new Object[0]), false, new SettableBeanProperty[0]);
        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        TestBeanArrayDelegate result = (TestBeanArrayDelegate) inst.createValueInstance(values);
        Assert.assertEquals(result.getValue(), values);
    }

    @Test
    public void testNullCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanNullCreator.class, new AnnotatedClass(TestBeanNullCreator.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.setDefaultCreator(new StandardMethodCreator(TestBeanNullCreator.class));
        collector.addIntCreator(new AnnotatedWithParams(TestBeanNullCreator.class, "IntCreator", new int[0]), true);

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        Assert.assertNull(inst.createValueInstance(null));
        Assert.assertNull(inst.createValueInstance(42));

        collector.addIntCreator(new AnnotatedWithParams(TestBeanNullCreator.class, "IntCreator", new int[0]), false);

        TestBeanNullCreator result = (TestBeanNullCreator) inst.createValueInstance(null);
        Assert.assertNull(result.getValue());
    }

    @Test
    public void testImproperCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(TestBeanImproperCreator.class, new AnnotatedClass(TestBeanImproperCreator.class));
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        collector.setDefaultCreator(new StandardMethodCreator(TestBeanImproperCreator.class));
        collector.addStringCreator(new AnnotatedWithParams(TestBeanImproperCreator.class, "ImproperStringCreator", new String[0]), true);

        ValueInstantiator inst = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        MapperFactory config = mapper.getFactory();
        DeserializationConfig deserializationConfig = config.deserializationConfig();

        Assert.assertThrows(IllegalArgumentException.class, () -> inst.createValueInstance("test"));

        deserializationConfig.addHandler(new BeanProperty.StdHandler(TestBeanImproperCreator.class, "value"));
        collector.addStringCreator(new AnnotatedWithParams(TestBeanImproperCreator.class, "ImproperStringCreator", new String[0]), false);
        inst = collector.constructValueInstantiator(deserializationConfig);

        TestBeanImproperCreator result = (TestBeanImproperCreator) inst.createValueInstance("test");
        Assert.assertEquals(result.getValue(), "test");
    }
}