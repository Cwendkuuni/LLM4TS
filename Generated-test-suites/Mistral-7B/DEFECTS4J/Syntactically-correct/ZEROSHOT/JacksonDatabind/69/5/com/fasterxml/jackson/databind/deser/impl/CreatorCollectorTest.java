package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

public class CreatorCollectorTest {

    private BeanDescription beanDesc;
    private CreatorCollector collector;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();
        beanDesc = new BeanDescription(null, null, null, null, null);
        collector = new CreatorCollector(beanDesc, config);
    }

    @Test
    public void testConstructValueInstantiator() {
        DeserializationConfig config = new DeserializationConfig();
        ValueInstantiator inst = collector.constructValueInstantiator(config);
        assertNotNull(inst);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructValueInstantiatorWithNoCreator() {
        DeserializationConfig config = new DeserializationConfig();
        collector.setDefaultCreator(null);
        ValueInstantiator inst = collector.constructValueInstantiator(config);
    }

    @Test
    public void testSetDefaultCreator() {
        Method method = new MethodTestCase("CreatorCollector", "setDefaultCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[]{}, new Annotation[0]);
        collector.setDefaultCreator(creator);
        AnnotatedWithParams result = collector._creators[CreatorCollector.C_DEFAULT];
        assertEquals(creator, result);
    }

    @Test
    public void testAddStringCreator() {
        Method addStringCreator = new MethodTestCase("CreatorCollector", "addStringCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[]{}, new Annotation[0]);
        collector.addStringCreator(creator, false);
        AnnotatedWithParams result = collector._creators[CreatorCollector.C_STRING];
        assertEquals(creator, result);
    }

    @Test
    public void testAddIntCreator() {
        Method addIntCreator = new MethodTestCase("CreatorCollector", "addIntCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[]{}, new Annotation[0]);
        collector.addIntCreator(creator, false);
        AnnotatedWithParams result = collector._creators[CreatorCollector.C_INT];
        assertEquals(creator, result);
    }

    @Test
    public void testAddLongCreator() {
        Method addLongCreator = new MethodTestCase("CreatorCollector", "addLongCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[]{}, new Annotation[0]);
        collector.addLongCreator(creator, false);
        AnnotatedWithParams result = collector._creators[CreatorCollector.C_LONG];
        assertEquals(creator, result);
    }

    @Test
    public void testAddDoubleCreator() {
        Method addDoubleCreator = new MethodTestCase("CreatorCollector", "addDoubleCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[]{}, new Annotation[0]);
        collector.addDoubleCreator(creator, false);
        AnnotatedWithParams result = collector._creators[CreatorCollector.C_DOUBLE];
        assertEquals(creator, result);
    }

    @Test
    public void testAddBooleanCreator() {
        Method addBooleanCreator = new MethodTestCase("CreatorCollector", "addBooleanCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[]{}, new Annotation[0]);
        collector.addBooleanCreator(creator, false);
        AnnotatedWithParams result = collector._creators[CreatorCollector.C_BOOLEAN];
        assertEquals(creator, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateStringCreator() {
        Method addStringCreator = new MethodTestCase("CreatorCollector", "addStringCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[0], new Annotation[0]);
        collector.addStringCreator(creator, false);
        collector.addStringCreator(creator, false);
    }

    @Test
    public void testAddDelegatingCreator() {
        Method addDelegatingCreator = new MethodTestCase("CreatorCollector", "addDelegatingCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[]{}, new Annotation[0]);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        injectables[0] = new SettableBeanProperty(null, null, null, null, null, null, null);
        collector.addDelegatingCreator(creator, false, injectables);
        AnnotatedWithParams result = collector._creators[CreatorCollector.C_DELEGATE];
        assertEquals(creator, result);
        SettableBeanProperty[] resultInjectables = collector._delegateArgs;
        assertEquals(injectables.length, resultInjectables.length);
        for (int i = 0; i < injectables.length; i++) {
            assertEquals(injectables[i], resultInjectables[i]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateDelegatingCreator() {
        Method addDelegatingCreator = new MethodTestCase("CreatorCollector", "addDelegatingCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[]{}, new Annotation[0]);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        injectables[0] = new SettableBeanProperty(null, null, null, null, null, null, null);
        collector.addDelegatingCreator(creator, false, injectables);
        collector.addDelegatingCreator(creator, false, injectables);
    }

    @Test
    public void testAddPropertyCreator() {
        Method addPropertyCreator = new MethodTestCase("CreatorCollector", "addPropertyCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[]{}, new Annotation[0]);
        SettableBeanProperty[] properties = new SettableBeanProperty[2];
        properties[0] = new SettableBeanProperty(null, null, null, null, null, null, null, null, null);
        properties[1] = new SettableBeanProperty(null, null, null, null, null, null, null, null, null);
        collector.addPropertyCreator(creator, false, properties);
        AnnotatedWithParams result = collector._creators[CreatorCollector.C_PROPS];
        assertEquals(creator, result);
        SettableBeanProperty[] resultProperties = collector._propertyBasedArgs;
        assertEquals(properties.length, resultProperties.length);
        for (int i = 0; i < properties.length; i++) {
            assertEquals(properties[i], resultProperties[i]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicatePropertyCreator() {
        Method addPropertyCreator = new MethodTestCase("CreatorCollector", "addPropertyCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[]{}, new Annotation[0]);
        SettableBeanProperty[] properties = new SettableBeanProperty[2];
        properties[0] = new SettableBeanProperty(null, null, null, null, null, null, null, null, null);
        collector.addPropertyCreator(creator, false, properties);
        SettableBeanProperty[] newProperties = new SettableBeanProperty[1];
        newProperties[0] = properties[0];
        collector.addPropertyCreator(creator, false, newProperties);
    }

    @Test
    public void testHasDefaultCreator() {
        Method hasDefaultCreator = new MethodTestCase("CreatorCollector", "hasDefaultCreator").getMethod();
        collector.setDefaultCreator(null);
        assertFalse(hasDefaultCreator.invoke(collector));
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[0], new Annotation[0]);
        collector.setDefaultCreator(creator);
        assertTrue(hasDefaultCreator.invoke(collector));
    }

    @Test
    public void testHasDelegatingCreator() {
        Method hasDelegatingCreator = new MethodTestCase("CreatorCollector", "hasDelegatingCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[]{}, new Annotation[0]);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        injectables[0] = new SettableBeanProperty(null, null, null, null, null, null, null);
        collector.addDelegatingCreator(creator, false, injectables);
        assertTrue(hasDelegatingCreator.invoke(collector));
        collector.setDefaultCreator(null);
        assertFalse(hasDelegatingCreator.invoke(collector));
    }

    @Test
    public void testHasPropertyBasedCreator() {
        Method hasPropertyBasedCreator = new MethodTestCase("CreatorCollector", "hasPropertyBasedCreator").getMethod();
        AnnotatedWithParams creator = new AnnotatedWithParams(null, new Class[]{}, new Annotation[0]);
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        properties[0] = new SettableBeanProperty(null, null, null, null, null, null, null, null, null);
        collector.addPropertyCreator(creator, false, properties);
        assertTrue(hasPropertyBasedCreator.invoke(collector));
        collector.setDefaultCreator(null);
        assertFalse(hasPropertyBasedCreator.invoke(collector));
    }
}