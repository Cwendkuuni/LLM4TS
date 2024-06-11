package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ReflectionUtils;

/**
 * Unit tests for {@link CreatorCollector} class.
 */
public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        Method defaultConstructor = TestBean.class.getDeclaredConstructor();
        AnnotatedWithParams defaultConstructorAnnotated = ReflectionUtils.findMethodAnnotated(defaultConstructor, JsonCreator.class);

        collector.setDefaultCreator(defaultConstructorAnnotated);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
        assertTrue(instantiator instanceof StdValueInstantiator);
    }

    @Test
    public void testStringCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        String stringValue = "hello";
        Method stringConstructor = TestBean.class.getDeclaredConstructor(String.class);
        AnnotatedWithParams stringConstructorAnnotated = ReflectionUtils.findMethodAnnotated(stringConstructor, JsonCreator.class);

        collector.addStringCreator(stringConstructorAnnotated, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
        TestBean bean = instantiator.createValueInstance(mapper.getDeserializationConfig(), stringValue);
        assertEquals(stringValue, bean.getStringValue());
    }

    @Test
    public void testIntCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        int intValue = 42;
        Method intConstructor = TestBean.class.getDeclaredConstructor(int.class);
        AnnotatedWithParams intConstructorAnnotated = ReflectionUtils.findMethodAnnotated(intConstructor, JsonCreator.class);

        collector.addIntCreator(intConstructorAnnotated, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
        TestBean bean = instantiator.createValueInstance(mapper.getDeserializationConfig(), intValue);
        assertEquals(intValue, beam.getIntValue());
    }

    @Test
    public void testLongCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        long longValue = 42L;
        Method longConstructor = TestBean.class.getDeclaredConstructor(long.class);
        AnnotatedWithParams longConstructorAnnotated = ReflectionUtils.findMethodAnnotated(longConstructor, JsonCreator.class);

        collector.addLongCreator(longConstructorAnnotated, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
        TestBean bean = instantiator.createValueInstance(mapper.getDeserializationConfig(), longValue);
        assertEquals(longValue, bean.getLongValue());
    }

    @Test
    public void testDoubleCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        double doubleValue = 42.0;
        Method doubleConstructor = TestBean.class.getDeclaredConstructor(double.class);
        AnnotatedWithParams doubleConstructorAnnotated = ReflectionUtils.findMethodAnnotated(doubleConstructor, JsonCreator.class);

        collector.addDoubleCreator(doubleConstructorAnnotated, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
        TestBean bean = instantiator.createValueInstance(mapper.getDeserializationConfig(), doubleValue);
        assertEquals(doubleValue, bean.getDoubleValue(), 0.0);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        boolean booleanValue = true;
        Method booleanConstructor = TestBean.class.getDeclaredConstructor(boolean.class);
        AnnotatedWithParams booleanConstructorAnnotated = ReflectionUtils.findMethodAnnotated(booleanConstructor, JsonCreator.class);

        collector.addBooleanCreator(booleanConstructorAnnotated, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
        TestBean bean = instantiator.createValueInstance(mapper.getDeserializationConfig(), booleanValue);
        assertEquals(booleanValue, bean.getBooleanValue());
    }

    @Test
    public void testDelegateCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestDelegateBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        Method delegateConstructor = TestDelegateBean.class.getDeclaredConstructor(TestDelegate.class);
        AnnotatedWithParams delegateConstructorAnnotated = ReflectionUtils.findMethodAnnotated(delegateConstructor, JsonCreator.class);

        SettableBeanProperty delegateArg = new AnnotatedBeanPropertyDefinition("delegate", TestDelegate.class, null, null, null, null, null, null);
        collector.addDelegatingCreator(delegateConstructorAnnotated, true, new SettableBeanProperty[] { delegateArg });

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
        TestDelegateBean bean = instantiator.createValueInstance(mapper.getDeserializationConfig(), new TestDelegate());
        assertEquals(bean.getDelegate(), new TestDelegate());
    }

    @Test
    public void testArrayDelegateCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestArrayDelegateBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        Method arrayDelegateConstructor = TestArrayDelegateBean.class.getDeclaredConstructor(TestDelegate[].class);
        AnnotatedWithParams arrayDelegateConstructorAnnotated = ReflectionUtils.findMethodAnnotated(arrayDelegateConstructor, JsonCreator.class);

        SettableBeanProperty[] arrayDelegateArgs = new SettableBeanProperty[] { new AnnotatedBeanPropertyDefinition("args", TestDelegate[].class, null, null, null, null, null, null) };
        collector.addDelegatingCreator(arrayDelegateConstructorAnnotated, true, arrayDelegateArgs);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
        TestArrayDelegateBean bean = instantiator.createValueInstance(mapper.getDeserializationConfig(), new TestDelegate[0]);
        TestDelegate[] delegates = bean.getDelegates();
        assertEquals(delegates.length, 0);
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestPropertyBasedBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        Method propertyBasedConstructor = TestPropertyBasedBean.class.getDeclaredConstructor();
        AnnotatedWithParams propertyBasedConstructorAnnotated = ReflectionUtils.findMethodAnnotated(propertyBasedConstructor, JsonCreator.class);

        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[] {
            new AnnotatedBeanPropertyDefinition("stringValue", String.class, null, null, null, null, null, null),
            new AnnotatedBeanPropertyDefinition("intValue", int.class, null, null, null, null, null, null),
            new AnnotatedBeanPropertyDefinition("booleanValue", boolean.class, null, null, null, null, null, null)
        };
        collector.addPropertyCreator(propertyBasedConstructorAnnotated, true, propertyBasedArgs);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
        TestPropertyBasedBean bean = instantiator.createValueInstance(mapper.getDeserializationConfig(), null);
        assertEquals("", bean.getStringValue());
        assertEquals(0, bean.getIntValue());
        assertFalse(bean.getBooleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreatorProperties() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestPropertyBasedBeanWithDuplicateProperties.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        Method propertyBasedConstructor = TestPropertyBasedBeanWithDuplicateProperties.class.getDeclaredConstructor();
        AnnotatedWithParams propertyBasedConstructorAnnotated = ReflectionUtils.findMethodAnnotated(propertyBasedConstructor, JsonCreator.class);

        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[] {
            new AnnotatedBeanPropertyDefinition("stringValue", String.class, null, null, null, null, null, null),
            new AnnotatedBeanPropertyDefinition("stringValue", String.class, null, null, null, null, null, null)
        };
        collector.addPropertyCreator(propertyBasedConstructorAnnotated, true, propertyBasedArgs);

        // should throw IllegalArgumentException
        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
    }

    @Test
    public void testExplicitCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestExplicitBean.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, mapper.getConfig());

        Method explicitConstructor = TestExplicitBean.class.getDeclaredConstructor(String.class);
        AnnotatedWithParams explicitConstructorAnnotated = ReflectionUtils.findMethodAnnotated(explicitConstructor, JsonCreator.class);
        explicitConstructorAnnotated.getParameterAnnotations()[0].put(JsonProperty.PropertyAnnotation.class, new JsonProperty.Std("stringValue"));

        collector.addStringCreator(explicitConstructorAnnotated, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
        TestExplicitBean bean = instantiator.createValueInstance(mapper.getDeserializationConfig(), "explicit");
        assertEquals("explicit", bean.getStringValue());
    }

    static class TestBean {
        @JsonCreator
        public TestBean() {}
        
        @JsonCreator
        public TestBean(String stringValue) {
            this.stringValue = stringValue;
        }

        @JsonCreator
        public TestBean(int intValue) {
            this.intValue = intValue;
        }

        @JsonCreator
        public TestBean(long longValue) {
            this.longValue = longValue;
        }

        @JsonCreator
        public TestBean(double doubleValue) {
            this.doubleValue = doubleValue;
        }

        @JsonCreator
        public TestBean(boolean booleanValue) {
            this.booleanValue = booleanValue;
        }

        @JsonProperty
        public String stringValue;
        @JsonProperty
        public int intValue;
        @JsonProperty
        public long longValue;
        @JsonProperty
        public double doubleValue;
        @JsonProperty
        public boolean booleanValue;
    }

    static class TestDelegateBean {
        @JsonCreator
        public TestDelegateBean(TestDelegate delegate) {
            this.delegate = delegate;
        }

        public TestDelegate delegate;
    }

    static class TestArrayDelegateBean {
        @JsonCreator
        public TestArrayDelegateBean(TestDelegate[] args) {
            this.delegates = delegates;
        }

        public TestDelegate[] delegates;
    }

    static class TestPropertyBasedBean {
        @JsonCreator
        public TestPropertyBasedBean() {}

        @JsonCreator
        public TestPropertyBasedBean(@JsonProperty("stringValue") String stringValue,
                                     @JsonProperty("intValue") int intValue,
                                     @JsonProperty("booleanValue") boolean booleanValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
            this.booleanValue = booleanValue;
        }

        public String stringValue;
        public int intValue;
        public boolean booleanValue;
    }

    static class TestPropertyBasedBeanWithDuplicateProperties {
        @JsonCreator
        public TestPropertyBasedBeanWithDuplicateProperties() {}

        @JsonCreator
        public TestPropertyBasedBeanWithDuplicateProperties(@JsonProperty("stringValue") String stringValue) {
            this.stringValue = stringValue;
        }

        public String stringValue;
    }

    static class TestExplicitBean {
        @JsonCreator
        public TestExplicitBean(String stringValue) {
            this.stringValue = stringValue;
        }

        @JsonProperty
        public String stringValue;
    }

    static class TestDelegate {

    }
}