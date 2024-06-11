package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.Snapshot;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getFactory().findBeanDescription(TestClass.class, null);

        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Default creator
        Method defaultConstructor = TestClass.class.getDeclaredConstructor();
        AnnotatedMethod annotatedDefaultConstructor = AnnotatedMethod.wrap(defaultConstructor, null);
        collector.setDefaultCreator(AnnotatedWithParams.with(annotatedDefaultConstructor));

        // String creator
        Method stringCreator = TestClass.class.getDeclaredMethod("stringCreator");
        AnnotatedMethod annotatedStringCreator = AnnotatedMethod.wrap(stringCreator, String.class);
        collector.addStringCreator(AnnotatedWithParams.with(annotatedStringCreator), true);

        // Int creator
        Method intCreator = TestClass.class.getDeclaredMethod("intCreator", int.class);
        AnnotatedMethod annotatedIntCreator = AnnotatedMethod.wrap(intCreator, int.class);
        collector.addIntCreator(AnnotatedWithParams.with(annotatedIntCreator), true);

        // Long creator
        Method longCreator = TestClass.class.getDeclaredMethod("longCreator", long.class);
        AnnotatedMethod annotatedLongCreator = AnnotatedMethod.wrap(longCreator, long.class);
        collector.addLongCreator(AnnotatedWithParams.with(annotatedLongCreator), true);

        // Double creator
        Method doubleCreator = TestClass.class.getDeclaredMethod("doubleCreator", double.class);
        AnnotatedMethod annotatedDoubleCreator = AnnotatedMethod.wrap(doubleCreator, double.class);
        collector.addDoubleCreator(AnnotatedWithParams.with(annotatedDoubleCreator), true);

        // Boolean creator
        Method booleanCreator = TestClass.class.getDeclaredMethod("booleanCreator", boolean.class);
        AnnotatedMethod annotatedBooleanCreator = AnnotatedMethod.wrap(booleanCreator, boolean.class);
        collector.addBooleanCreator(AnnotatedWithParams.with(annotatedBooleanCreator), true);

        // Delegating creator
        Method delegateCreator = TestClass.class.getDeclaredMethod("delegateCreator", TestDelegate.class);
        AnnotatedMethod annotatedDelegateCreator = AnnotatedMethod.wrap(delegateCreator, TestDelegate.class);
        CreatorProperty[] injectables = new CreatorProperty[] {
                AnnotatedParameter.std(annotatedDelegateCreator.getParameter(0), TestClass.class, null)
        };
        collector.addDelegatingCreator(AnnotatedWithParams.with(annotatedDelegateCreator), true, injectables);

        // Property-based creator
        Snapshot namingStrategy = mapper.getFactory().getAnnotationIntrospector().findPropertyValueNameStrategy(TestClass.class);
        BeanPropertyDefinition propertyDefinition = namingStrategy.findPropertyDefinition("property1", TestClass.class);
        AnnotatedMethod annotatedPropertyCreator = AnnotatedMethod.wrap(propertyDefinition.getSetterMethod(), Object.class);
        CreatorProperty[] properties = new CreatorProperty[] {
                AnnotatedParameter.std(annotatedPropertyCreator.getParameter(0), TestClass.class, null)
        };
        collector.addPropertyCreator(AnnotatedWithParams.with(annotatedPropertyCreator), true, properties);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());

        TestClass obj = instantiator.createValueInstance(mapper);
        assertEquals(0, obj.getIntValue());
        assertEquals(0L, obj.getLongValue());
        assertEquals(0.0, obj.getDoubleValue(), 0.001);
        assertEquals(false, obj.getBooleanValue());
        assertEquals(TestDelegate.class, obj.getDelegate().getClass());
        assertEquals("testString", obj.getStringValue());

        // Test string creator
        obj = (TestClass) instantiator.createValueInstance(mapper, "testString");
        assertEquals("testString", obj.getStringValue());

        // Test int creator
        obj = (TestClass) instantiator.createValueInstance(mapper, 1);
        assertEquals(1, obj.getIntValue());

        // Test long creator
        obj = (TestClass) instantiator.createValueInstance(mapper, 1L);
        assertEquals(1L, obj.getLongValue());

        // Test double creator
        obj = (TestClass) instantiator.createValueInstance(mapper, 1.0);
        assertEquals(1.0, obj.getDoubleValue(), 0.001);

        // Test boolean creator
        obj = (TestClass) instantiator.createValueInstance(mapper, true);
        assertEquals(true, obj.getBooleanValue());

        // Test delegating creator
        TestDelegate delegate = new TestDelegate();
        obj = instantiator.createValueInstance(mapper, delegate);
        assertEquals(delegate, obj.getDelegate());

        // Test property-based creator
        TestClass propertyObj = new TestClass();
        propertyObj.setProperty1("testProperty");
        obj = instantiator.createValueInstance(mapper, propertyObj);
        assertEquals("testProperty", obj.getProperty1());

        // Test default creator
        obj = instantiator.createValueInstance(mapper);
        assertNull(obj.getDelegate());
        assertNull(obj.getStringValue());
        assertEquals(0, obj.getIntValue());
        assertEquals(0L, obj.getLongValue());
        assertEquals(0.0, obj.getDoubleValue(), 0.001);
        assertEquals(false, obj.getBooleanValue());

        // Test missing creator
        try {
            instantiator.createValueInstance(mapper, "missingCreator");
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test non-matching creator
        TestClass otherClass = new OtherTestClass();
        try {
            instantiator.createValueInstance(mapper, otherClass);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test null creator
        try {
            instantiator.createValueInstance(mapper, null);
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test incomplete creator
        TestClass incompleteObj = new TestClass();
        collector.addIncompeteParameter(AnnotatedParameter.std(TestClass.class.getDeclaredMethod("incompleteMethod"), Object.class));
        instantiator = collector.constructValueInstantiator(mapper.getDeserializationConfig());
        obj = instantiator.createValueInstance(mapper, incompleteObj);
        assertNotNull(obj.getIncompleteParameter());
        assertEquals("incomplete", obj.getIncompleteParameter().getName());

    }

    static class TestClass {
        private int intValue;
        private long longValue;
        private double doubleValue;
        private boolean booleanValue;
        private TestDelegate delegate;
        private String stringValue;
        private String property1;
        private AnnotatedParameter incompleteParameter;

        @JsonCreator
        public TestClass(String stringValue) {
            this.stringValue = stringValue;
        }

        @JsonCreator
        public TestClass(@SuppressWarnings("unused") int i) {
            this.intValue = i;
        }

        @JsonCreator
        public TestClass(@SuppressWarnings("unused") long l) {
            this.longValue = l;
        }

        @JsonCreator
        public TestClass(@SuppressWarnings("unused") double d) {
            this.doubleValue = d;
        }

        @JsonCreator
        public TestClass(@SuppressWarnings("unused") boolean b) {
            this.booleanValue = b;
        }

        @JsonCreator
        public TestClass(@SuppressWarnings("unused") TestDelegate delegate) {
            this.delegate = delegate;
        }

        @JsonProperty
        public void setProperty1(String value) {
            this.property1 = value;
        }

        public int getIntValue() {
            return intValue;
        }

        public long getLongValue() {
            return longValue;
        }

        public double getDoubleValue() {
            return doubleValue;
        }

        public boolean getBooleanValue() {
            return booleanValue;
        }

        public TestDelegate getDelegate() {
            return delegate;
        }

        public String getStringValue() {
            return stringValue;
        }

        public String getProperty1() {
            return property1;
        }

        public AnnotatedParameter getIncompleteParameter() {
            return incompleteParameter;
        }

        @JsonCreator
        public static TestClass stringCreator(String value) {
            return new TestClass(value);
        }

        @JsonCreator
        public static TestClass intCreator(int value) {
            return new TestClass(value);
        }

        @JsonCreator
        public static TestClass longCreator(long value) {
            return new TestClass(value);
        }

        @JsonCreator
        public static TestClass doubleCreator(double value) {
            return new TestClass(value);
        }

        @JsonCreator
        public static TestClass booleanCreator(boolean value) {
            return new TestClass(value);
        }

        @JsonCreator
        public static TestClass delegateCreator(@SuppressWarnings("unused") TestDelegate delegate) {
            return new TestClass(delegate);
        }

        public static class TestDelegate {}

    }

    static class OtherTestClass {
        private int intValue;

        @JsonCreator
        public OtherTestClass(int i) {
            this.intValue = i;
        }
    }

    static class AnnotatedWithParams {
        private AnnotatedMethod _method;

        public AnnotatedWithParams(AnnotatedMethod method) {
            _method = method;
        }

        public AnnotatedMethod getAnnotatedMethod() {
            return _method;
        }

        @Override
        public String toString() {
            return getAnnotatedMethod().getName()+"()";
        }
    }

}