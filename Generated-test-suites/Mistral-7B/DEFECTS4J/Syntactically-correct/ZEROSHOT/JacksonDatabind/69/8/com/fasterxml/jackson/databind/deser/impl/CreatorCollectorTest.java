package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;

/**
 * Unit tests for {@link CreatorCollector}.
 */
public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new SimpleBean(), null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardMapperConfig());

        Method defaultMethod = SimpleBean.class.getDeclaredMethod("defaultConstructor");
        AnnotatedWithParams creator = JavaDeserializerProvider.defaultInstance().findValueDeserializer(defaultMethod, null);
        collector.setDefaultCreator(creator);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object created = instantiator.createValue(null, SimpleBean.class, null);
        assertEquals(new SimpleBean(), created);
    }

    @Test
    public void testStringCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new StringBean(), null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardMapperConfig());

        Method stringMethod = StringBean.class.getDeclaredMethod("stringConstructor");
        AnnotatedWithParams creator = JavaDeserializerProvider.defaultInstance().findValueDeserializer(stringMethod, null);
        collector.addStringCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object created = instantiator.createValue(null, StringBean.class, null, null);
        assertEquals("created", created);

        String input = "input";
        Object deserialized = new ObjectMapper().readValue(new StringReader(input), StringBean.class);
        assertEquals("input", deserialized);
    }

    @Test
    public void testIntCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new IntBean(), null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardMapperConfig());

        Method intMethod = IntBean.class.getDeclaredMethod("intConstructor");
        AnnotatedWithParams creator = JavaDeserializerProvider.defaultInstance().findValueDeserializer(intMethod, null);
        collector.addIntCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object created = instantiator.createValue(null, IntBean.class, null, null);
        assertEquals(42, created);

        int input = 42;
        Object deserialized = new ObjectMapper().readValue(new ByteBuffer(ByteBuffer.allocateDirect(4).putInt(input)), IntBean.class);
        assertEquals(input, deserialized);
    }

    @Test
    public void testLongCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new LongBean(), null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardMapperConfig());

        Method longMethod = LongBean.class.getDeclaredMethod("longConstructor");
        AnnotatedWithParams creator = JavaDeserializerProvider.defaultInstance().findValueDeserializer(longMethod, null);
        collector.addLongCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object created = instantiator.createValue(null, LongBean.class, null, null);
        assertEquals(1L, created);

        long input = 1L;
        Object deserialized = new ObjectMapper().readValue(new ByteBuffer(ByteBuffer.allocateDirect(8).putLong(input)), LongBean.class);
        assertEquals(input, deserialized);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new DoubleBean(), null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardMapperConfig());

        Method doubleMethod = DoubleBean.class.getDeclaredMethod("doubleConstructor");
        AnnotatedWithParams creator = JavaDeserializerProvider.defaultInstance().findValueDeserializer(doubleMethod, null);
        collector.addDoubleCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object created = instantiator.createValue(null, DoubleBean.class, null, null);
        assertEquals(3.14, created);

        double input = 3.14;
        Object deserialized = new ObjectMapper().readValue(new ByteBuffer(ByteBuffer.allocateDirect(8).putDouble(input)), DoubleBean.class);
        assertEquals(input, deserialized);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new BooleanBean(), null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardMapperConfig());

        Method booleanMethod = BooleanBean.class.getDeclaredMethod("booleanConstructor");
        AnnotatedWithParams creator = JavaDeserializerProvider.defaultInstance().findValueDeserializer(booleanMethod, null);
        collector.addBooleanCreator(creator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object created = instantiator.createValue(null, BooleanBean.class, null, null);
        assertTrue((Boolean) created);

        boolean input = true;
        Object deserialized = new ObjectMapper().readValue(new ByteBuffer(ByteBuffer.allocateDirect(1).putBoolean(input)), BooleanBean.class);
        assertEquals(input, deserialized);
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new StringDelegateBean(), null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardMapperConfig());

        Method delegateMethod = StringDelegateBean.class.getDeclaredMethod("delegateConstructor");
        AnnotatedWithParams creator = JavaDeserializerProvider.defaultInstance().findValueDeserializer(delegateMethod, null);
        SettableBeanProperty[] injectables = new SettableBeanProperty[] { new AnnotatedStringProperty("name", new SimpleStringProperty()) };
        collector.addDelegatingCreator(creator, true, injectables);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object created = instantiator.createValue(null, StringDelegateBean.class, null, null);
        assertEquals("created", created);

        String input = "input";
        Object deserialized = new ObjectMapper().readValue(new StringReader(input), StringDelegateBean.class);
        assertEquals("input", deserialized);
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new IntArrayDelegateBean(), null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardMapperConfig());

        Method delegateMethod = IntArrayDelegateBean.class.getDeclaredMethod("arrayDelegateConstructor");
        AnnotatedWithParams creator = JavaDeserializerProvider.defaultInstance().findValueDeserializer(delegateMethod, null);
        SettableBeanProperty[] injectables = new SettableBeanProperty[] { new AnnotatedIntProperty("value", new SimpleIntProperty()) };
        collector.addDelegatingCreator(creator, true, injectables);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object created = instantiator.createValue(null, IntArrayDelegateBean.class, new ValueInstantiator.ValueInstanciatorContext(), null);
        assertArrayEquals(new int[] { 42 }, (int[]) created);

        int[] input = new int[] { 42 };
        Object deserialized = new ObjectMapper().readValue(new ByteBuffer(ByteBuffer.allocateDirect(input.length * 4).asIntBuffer()), IntArrayDelegateBean.class);
        assertArrayEquals(input, (int[]) deserialized);
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new PropertyBasedBean(), null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardMapperConfig());

        Method propertyMethod = PropertyBasedBean.class.getDeclaredMethod("propertyConstructor");
        AnnotatedWithParams creator = JavaDeserializerProvider.defaultInstance().findValueDeserializer(propertyMethod, null);
        SettableBeanProperty[] properties = new SettableBeanProperty[] {
            new AnnotatedStringProperty("name", new SimpleStringProperty()),
            new AnnotatedIntProperty("age", new SimpleIntProperty())
        };
        collector.addPropertyCreator(creator, true, properties);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object created = instantiator.createValue(null, PropertyBasedBean.class, null, null);
        PropertyBasedBean expected = new PropertyBasedBean("name", 42);
        assertEquals(expected.getName(), created.getName());
        assertEquals(expected.getAge(), created.getAge());

        Map<String, Object> input = new HashMap<>();
        input.put("name", "input");
        input.put("age", 42);
        Object deserialized = new ObjectMapper().readValue(new ObjectWriter().writeValueAsString(input), PropertyBasedBean.class);
        assertEquals(expected.getName(), deserialized.getName());
        assertEquals(expected.getAge(), deserialized.getAge());
    }

    @Test
    public void testIncompleteParameter() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new IncompleteBean(), null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardMapperConfig());

        Method incompleteMethod = IncompleteBean.class.getDeclaredMethod("incompleteConstructor");
        AnnotatedParameter parameter = Introspector.getParameterInfo(incompleteMethod, 0);
        collector.addIncompleteParameter(parameter);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object created = instantiator.createValue(null, IncompleteBean.class, null, null);
        IncompleteBean expected = new IncompleteBean();
        assertEquals(expected, created);

        ObjectReader reader = new ObjectMapper().readerForUpdating(IncompleteBean.class);
        Object deserialized = reader.readValue(new StringReader("{\"value\": 42}"));
        assertEquals(expected, deserialized);
    }

    @Test
    public void testExplicitAndImplicitCreators() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new ExplicitAndImplicitBean(), null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, new MapperConfig.StandardMapperConfig());

        Method implicitMethod = ExplicitAndImplicitBean.class.getDeclaredMethod("implicitConstructor");
        AnnotatedWithParams implicitCreator = JavaDeserializerProvider.defaultInstance().findValueDeserializer(implicitMethod, null);
        collector.setDefaultCreator(implicitCreator);

        Method explicitMethod = ExplicitAndImplicitBean.class.getDeclaredMethod("explicitConstructor");
        AnnotatedWithParams explicitCreator = JavaDeserializerProvider.defaultInstance().findValueDeserializer(explicitMethod, null);
        collector.addIntCreator(explicitCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());

        Object created = instantiator.createValue(null, ExplicitAndImplicitBean.class, null, null);
        ExplicitAndImplicitBean expected = new ExplicitAndImplicitBean(42);
        assertEquals(expected, created);

        int input = 42;
        Object deserialized = new ObjectMapper().readValue(new ByteBuffer(ByteBuffer.allocateDirect(4).putInt(input)), ExplicitAndImplicitBean.class);
        assertEquals(expected, deserialized);

        ObjectReader reader = new ObjectMapper().readerForUpdating(ExplicitAndImplicitBean.class);
        Object deserialized2 = reader.readValue(new StringReader("{}"));
        assertEquals(expected, deserialized2);
    }
}