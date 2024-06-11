package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Created by mike on 11/1/2022.
 */
public class CreatorCollectorTest {

    private ObjectMapper objectMapper;
    private CreatorCollector creatorCollector;

    @Before
    public void setup() throws Exception {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        objectMapper = builder.build();
        BeanDescription beanDescription = objectMapper.getFactory().findConcreteDeserializer(String.class);
        creatorCollector = new CreatorCollector(beanDescription, objectMapper.getDeserializationConfig());
    }

    @Test
    public void testDefaultCreator() throws Exception {
        Class<?> type = String.class;
        Method defaultCreator = String.class.getDeclaredConstructor();
        AnnotatedWithParams annotatedParams = new AnnotatedWithParams(defaultCreator, new AnnotatedClass(type), null);
        creatorCollector.setDefaultCreator(annotatedParams);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        assertNotNull(valueInstantiator);
    }

    @Test
    public void testStringCreator() throws Exception {
        String value = "Test";
        Class<?> stringClass = String.class;
        Method stringCreator = String.class.getDeclaredConstructor(String.class);
        AnnotatedWithParams annotatedParams = new AnnotatedWithParams(stringCreator, new AnnotatedClass(stringClass), new AnnotationIntrospector().findAnnotationValues(value, Object.class));
        creatorCollector.addStringCreator(annotatedParams, true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        assertEquals(value, valueInstantiator.createValueInstance(null, null));
    }

    @Test
    public void testIntCreator() throws Exception {
        int value = 1;
        Class<?> intClass = Integer.class;
        Method intCreator = Integer.class.getDeclaredConstructor(int.class);
        AnnotatedWithParams annotatedParams = new AnnotatedWithParams(intCreator, new AnnotatedClass(intClass), new AnnotationIntrospector().findAnnotationValues(value, Object.class));
        creatorCollector.addIntCreator(annotatedParams, true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        assertEquals(value, valueInstantiator.createValueInstance(null, null));
    }

    @Test
    public void testLongCreator() throws Exception {
        long value = 1L;
        Class<?> longClass = Long.class;
        Method longCreator = Long.class.getDeclaredConstructor(long.class);
        AnnotatedWithParams annotatedParams = new AnnotatedWithParams(longCreator, new AnnotatedClass(longClass), new AnnotationIntrospector().findAnnotationValues(value, Object.class));
        creatorCollector.addLongCreator(annotatedParams, true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        assertEquals(value, valueInstantiator.createValueInstance(null, null));
    }

    @Test
    public void testDoubleCreator() throws Exception {
        double value = 1.0D;
        Class<?> doubleClass = Double.class;
        Method doubleCreator = Double.class.getDeclaredConstructor(double.class);
        AnnotatedWithParams annotatedParams = new AnnotatedWithParams(doubleCreator, new AnnotatedClass(doubleClass), new AnnotationIntrospector().findAnnotationValues(value, Object.class));
        creatorCollector.addDoubleCreator(annotatedParams, true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        assertEquals(value, valueInstantiator.createValueInstance(null, null));
    }

    @Test
    public void testBooleanCreator() throws Exception {
        boolean value = true;
        Class<?> booleanClass = Boolean.class;
        Method booleanCreator = Boolean.class.getDeclaredConstructor(boolean.class);
        AnnotatedWithParams annotatedParams = new AnnotatedWithParams(booleanCreator, new AnnotatedClass(booleanClass), new AnnotationIntrospector().findAnnotationValues(value, Object.class));
        creatorCollector.addBooleanCreator(annotatedParams, true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        assertEquals(value, valueInstantiator.createValueInstance(null, null));
    }

    @Test
    public void testDelegateCreator() throws Exception {
        Class<?> delegateType = String.class;
        Method delegateCreator = String.class.getDeclaredConstructor(String.class);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        injectables[0] = new SettableBeanProperty("delegate", String.class, VisibilityChecker.getDefault(), new NestedIntrospector(), null, new AnnotatedClass(delegateType), new AnnotationIntrospector());
        AnnotatedWithParams annotatedParams = new AnnotatedWithParams(delegateCreator, new AnnotatedClass(String.class), null);
        creatorCollector.addDelegatingCreator(annotatedParams, true, injectables);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        String delegate = "Test";
        Object result = valueInstantiator.createValueInstance(null, new Object[] { delegate });
        assertNotNull(result);
        assertEquals(delegate, ((String) result).getDelegate());
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        Class<?> stringClass = String.class;
        String propertyName = "propertyName";
        Class<?> propertyType = Integer.class;
        Method propertyCreator = String.class.getDeclaredConstructor(Integer.class);
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition(propertyName, propertyType, VisibilityChecker.getDefault(), new NestedIntrospector(), new AnnotatedClass(stringClass), new AnnotationIntrospector());
        properties[0] = new SettableBeanProperty(propertyName, propertyType, beanPropertyDefinition, null, new AnnotatedClass(stringClass), new AnnotationIntrospector());
        AnnotatedWithParams annotatedParams = new AnnotatedWithParams(propertyCreator, new AnnotatedClass(String.class), null);
        creatorCollector.addPropertyCreator(annotatedParams, true, properties);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig());
        Integer propertyValue = 1;
        Object result = valueInstantiator.createValueInstance(null, new Object[] { propertyValue });
        assertNotNull(result);
        assertEquals(propertyValue, ((String) result).get(propertyName));
    }

    @Test
    public void testHasDefaultCreator() throws Exception {
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() throws Exception {
        creatorCollector.addDelegatingCreator(null, false, null);
        assertFalse(creatorCollector.hasDelegatingCreator());

        creatorCollector.addDelegatingCreator(null, true, null);
        assertFalse(creatorCollector.hasDelegatingCreator());

        creatorCollector.addDelegatingCreator(null, true, new SettableBeanProperty[0]);
        assertFalse(creatorCollector.hasDelegatingCreator());

        creatorCollector.addDelegatingCreator(mock(Method.class), false, null);
        assertFalse(creatorCollector.hasDelegatingCreator());

        creatorCollector.addDelegatingCreator(mock(Method.class), true, null);
        assertFalse(creatorCollector.hasDelegatingCreator());

        creatorCollector.addDelegatingCreator(mock(Method.class), true, new SettableBeanProperty[0]);
        assertFalse(creatorCollector.hasDelegatingCreator());

        creatorCollector.addDelegatingCreator(mock(Method.class), true, new SettableBeanProperty[1]);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() throws Exception {
        creatorCollector.addPropertyCreator(null, false, null);
        assertFalse(creatorCollector.hasPropertyBasedCreator());

        creatorCollector.addPropertyCreator(null, true, null);
        assertFalse(creatorCollector.hasPropertyBasedCreator());

        creatorCollector.addPropertyCreator(null, true, new SettableBeanProperty[0]);
        assertFalse(creatorCollector.hasPropertyBasedCreator());

        creatorCollector.addPropertyCreator(mock(Method.class), false, null);
        assertFalse(creatorCollector.hasPropertyBasedCreator());

        creatorCollector.addPropertyCreator(mock(Method.class), true, null);
        assertFalse(creatorCollector.hasPropertyBasedCreator());

        creatorCollector.addPropertyCreator(mock(Method.class), true, new SettableBeanProperty[1]);
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }

    private static class AnnotatedWithParams extends AnnotatedMethod {
        private final AnnotatedClass _annotationType;
        private final AnnotationValues _annotationValues;

        public AnnotatedWithParams(Method method, AnnotatedClass annotatedClass, AnnotationValues annotationValues) {
            super(method, VisibilityChecker.getDefault(), new NestedIntrospector(), annotatedClass, new AnnotationIntrospector());
            _annotationType = annotatedClass;
            _annotationValues = annotationValues;
        }

        public AnnotatedClass getAnnotatedClass() { return _annotationType; }
        public AnnotationValues getAnnotationValues() { return _annotationValues; }
    }
}