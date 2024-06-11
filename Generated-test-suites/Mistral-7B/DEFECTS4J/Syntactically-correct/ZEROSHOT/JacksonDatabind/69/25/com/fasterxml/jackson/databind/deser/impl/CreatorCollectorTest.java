package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

public class CreatorCollectorTest {

    private CreatorCollector collector;
    private MapperConfig<?> config;
    private BeanDescription beanDesc;

    @Before
    public void setup() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getConfig();
        beanDesc = Mockito.mock(BeanDescription.class);
        collector = new CreatorCollector(beanDesc, config);
    }

    @Test
    public void testDefaultCreator() {
        collector.setDefaultCreator(Mockito.mock(AnnotatedWithParams.class));
        assertTrue(collector.hasDefaultCreator());
    }

    @Test
    public void testStringCreator() {
        AnnotatedWithParams stringCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(stringCreator, true);
        assertTrue(collector.hasStringCreator());
    }

    @Test
    public void testIntCreator() {
        AnnotatedWithParams intCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addIntCreator(intCreator, true);
        assertTrue(collector.hasIntCreator());
    }

    @Test
    public void testLongCreator() {
        AnnotatedWithParams longCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addLongCreator(longCreator, true);
        assertTrue(collector.hasLongCreator());
    }

    @Test
    public void testDoubleCreator() {
        AnnotatedWithParams doubleCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(doubleCreator, true);
        assertTrue(collector.hasDoubleCreator());
    }

    @Test
    public void testBooleanCreator() {
        AnnotatedWithParams booleanCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(booleanCreator, true);
        assertTrue(collector.hasBooleanCreator());
    }

    @Test
    public void testDelegatingCreator() {
        AnnotatedWithParams delegatingCreator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        collector.addDelegatingCreator(delegatingCreator, true, delegateArgs);
        assertTrue(collector.hasDelegatingCreator());
    }

    @Test
    public void testPropertyBasedCreator() {
        AnnotatedWithParams propertyCreator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[2];
        collector.addPropertyCreator(propertyCreator, true, propertyBasedArgs);
        assertTrue(collector.hasPropertyBasedCreator());
        assertEquals(2, propertyBasedArgs.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() {
        AnnotatedWithParams stringCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(stringCreator, true);
        collector.addStringCreator(stringCreator, true);
    }

    @Test
    public void testConstructValueInstantiator() throws IOException {
        JavaType delegateType = ClassType.constructUnresolved(List.class, Object.class);
        JavaType arrayDelegateType = ClassType.constructUnresolved(ArrayList.class, Object.class);
        JavaType type = ClassType.constructUnresolved(MyClass.class);

        AnnotatedWithParams defaultCreator = Mockito.mock(AnnotatedWithParams.class);
        AnnotatedWithParams stringCreator = Mockito.mock(AnnotatedWithParams.class);
        AnnotatedWithParams intCreator = Mockito.mock(AnnotatedWithParams.class);
        AnnotatedWithParams longCreator = Mockito.mock(AnnotatedWithParams.class);
        AnnotatedWithParams doubleCreator = Mockito.mock(AnnotatedWithParams.class);
        AnnotatedWithParams booleanCreator = Mockito.mock(AnnotatedWithParams.class);
        AnnotatedWithParams delegatingCreator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[1];

        collector.setDefaultCreator(defaultCreator);
        collector.addStringCreator(stringCreator, true);
        collector.addIntCreator(intCreator, true);
        collector.addLongCreator(longCreator, true);
        collector.addDoubleCreator(doubleCreator, true);
        collector.addBooleanCreator(booleanCreator, true);
        collector.addDelegatingCreator(delegatingCreator, true, injectables);

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        assertEquals(type, inst.getTargetType(null));
        assertEquals(defaultCreator, inst.getDefaultCreator());
        assertEquals(stringCreator, inst.getStringCreator());
        assertEquals(intCreator, inst.getIntCreator());
        assertEquals(longCreator, inst.getLongCreator());
        assertEquals(doubleCreator, inst.getDoubleCreator());
        assertEquals(booleanCreator, inst.getBooleanCreator());
        assertEquals(delegateType, inst.getDelegateType());
        assertEquals(arrayDelegateType, inst.getArrayDelegateType());
        assertNull(inst.getPropertyBasedCreator());
    }
}

class MyClass {
    public MyClass() {}
    @JsonCreator
    public MyClass(@JsonProperty("list") List<Object> list) {
        this();
    }
}