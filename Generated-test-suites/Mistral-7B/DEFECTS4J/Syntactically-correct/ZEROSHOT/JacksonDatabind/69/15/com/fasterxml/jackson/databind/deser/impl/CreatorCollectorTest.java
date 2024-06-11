package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Test class for CreatorCollector
 */
@RunWith(MockitoJUnitRunner.class)
public class CreatorCollectorTest {

    private CreatorCollector collector;
    private BeanDescription beanDesc;
    private MapperConfig<ObjectMapper> config;

    @Before
    public void setUp() {
        beanDesc = Mockito.mock(BeanDescription.class);
        config = Mockito.mock(MapperConfig.class);
        collector = new CreatorCollector(beanDesc, config);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(creator);
        assertTrue(collector.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator, false);
        assertTrue(collector.hasCreatorForType(CreatorCollector.C_STRING));
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator, false);
        assertTrue(collector.hasCreatorForType(CreatorCollector.C_INT));
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addLongCreator(creator, false);
        assertTrue(collector.hasCreatorForType(CreatorCollector.C_LONG));
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(creator, false);
        assertTrue(collector.hasCreatorForType(CreatorCollector.C_DOUBLE));
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(creator, false);
        assertTrue(collector.hasCreatorForType(CreatorCollector.C_BOOLEAN));
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        collector.addDelegatingCreator(creator, false, injectables);
        assertTrue(collector.hasDelegatingCreator());
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        collector.addPropertyCreator(creator, false, properties);
        assertTrue(collector.hasPropertyBasedCreator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateStringCreator() {
        AnnotatedWithParams creator1 = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator1, false);
        AnnotatedWithParams creator2 = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateIntCreator() {
        AnnotatedWithParams creator1 = Mockito.mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator1, false);
        AnnotatedWithParams creator2 = Mockito.mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateLongCreator() {
        AnnotatedWithParams creator1 = Mockito.mock(AnnotatedWithParams.class);
        collector.addLongCreator(creator1, false);
        AnnotatedWithParams creator2 = Mockito.mock(AnnotatedWithParams.class);
        collector.addLongCreator(creator2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateDoubleCreator() {
        AnnotatedWithParams creator1 = Mockito.mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(creator1, false);
        AnnotatedWithParams creator2 = Mockito.mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(creator2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateBooleanCreator() {
        AnnotatedWithParams creator1 = Mockito.mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(creator1, false);
        AnnotatedWithParams creator2 = Mockito.mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(creator2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateDelegateCreator() {
        AnnotatedWithParams creator1 = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] injectables1 = new SettableBeanProperty[1];
        collector.addDelegatingCreator(creator1, false, injectables1);
        AnnotatedWithParams creator2 = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] injectables2 = new SettableBeanProperty[1];
        collector.addDelegatingCreator(creator2, false, injectables2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicatePropertyCreator() {
        AnnotatedWithParams creator1 = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] properties1 = new SettableBeanProperty[1];
        collector.addPropertyCreator(creator1, false, properties1);
        AnnotatedWithParams creator2 = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] properties2 = new SettableBeanProperty[1];
        collector.addPropertyCreator(creator2, false, properties2);
        String propertyName = "test";
        assertEquals(propertyName, properties1[0].getName());
        assertEquals(propertyName, properties2[0].getName());
    }

    @Test
    public void testConstructValueInstantiator() {
        // No creators set, should return null
        assertEquals(null, collector.constructValueInstantiator(Mockito.mock(DeserializationConfig.class)));

        // String creator
        AnnotatedWithParams stringCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(stringCreator, false);
        ValueInstantiator inst = collector.constructValueInstantiator(Mockito.mock(DeserializationConfig.class));
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        StdValueInstantiator stdInst = (StdValueInstantiator) inst;
        assertEquals(CreatorCollector.C_STRING, stdInst.getCreatorIndex());
        assertEquals(stringCreator, stdInst.getCreator());

        // Int creator
        AnnotatedWithParams intCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addIntCreator(intCreator, false);
        inst = collector.constructValueInstantiator(Mockito.mock(DeserializationConfig.class));
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        stdInst = (StdValueInstantiator) inst;
        assertEquals(CreatorCollector.C_INT, stdInst.getCreatorIndex());
        assertEquals(intCreator, stdInst.getCreator());

        // Long creator
        AnnotatedWithParams longCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addLongCreator(longCreator, false);
        inst = collector.constructValueInstantiator(Mockito.mock(DeserializationConfig.class));
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        stdInst = (StdValueInstantiator) inst;
        assertEquals(CreatorCollector.C_LONG, stdInst.getCreatorIndex());
        assertEquals(longCreator, stdInst.getCreator());

        // Double creator
        AnnotatedWithParams doubleCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(doubleCreator, false);
        inst = collector.constructValueInstantiator(Mockito.mock(DeserializationConfig.class));
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        stdInst = (StdValueInstantiator) inst;
        assertEquals(CreatorCollector.C_DOUBLE, stdInst.getCreatorIndex());
        assertEquals(doubleCreator, stdInst.getCreator());

        // Boolean creator
        AnnotatedWithParams booleanCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(booleanCreator, false);
        inst = collector.constructValueInstantiator(Mockito.mock(DeserializationConfig.class));
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        stdInst = (StdValueInstantiator) inst;
        assertEquals(CreatorCollector.C_BOOLEAN, stdInst.getCreatorIndex());
        assertEquals(booleanCreator, stdInst.getCreator());

        // Delegating creator
        AnnotatedWithParams delegateCreator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        collector.addDelegatingCreator(delegateCreator, false, delegateArgs);
        inst = collector.constructValueInstantiator(Mockito.mock(DeserializationConfig.class));
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        stdInst = (StdValueInstantiator) inst;
        assertEquals(CreatorCollector.C_DELEGATE, stdInst.getCreatorIndex());
        assertEquals(delegateCreator, stdInst.getCreator());
        assertEquals(delegateArgs[0], stdInst.getDelegateArgs()[0]);

        // Property-based creator
        AnnotatedWithParams propertyCreator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[1];
        collector.addPropertyCreator(propertyCreator, false, propertyBasedArgs);
        inst = collector.constructValueInstantiator(Mockito.mock(DeserializationConfig.class));
        assertNotNull(inst);
        assertTrue(inst instanceof StdValueInstantiator);
        stdInst = (StdValueInstantiator) inst;
        assertEquals(CreatorCollector.C_PROPS, stdInst.getCreatorIndex());
        assertEquals(propertyCreator, stdInst.getCreator());
        assertEquals(propertyBasedArgs[0], stdInst.getPropertyBasedArgs()[0]);
    }

    @Test
    public void testHasDelegatingCreator() {
        // No delegating creator, should return false
        assertFalse(collector.hasDelegatingCreator());

        // Set delegating creator, should return true
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        collector.addDelegatingCreator(creator, false, injectables);
        assertTrue(collector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        // No property-based creator, should return false
        assertFalse(collector.hasPropertyBasedCreator());

        // Set property-based creator, should return true
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        collector.addPropertyCreator(creator, false, properties);
        assertTrue(collector.hasPropertyBasedCreator());
    }

    @Test
    public void testGetCreatorForType() {
        // Default creator, should return null
        assertNull(collector.getCreatorForType(CreatorCollector.C_DEFAULT));

        // String creator
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator, false);
        assertEquals(creator, collector.getCreatorForType(CreatorCollector.C_STRING));

        // Int creator
        creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator, false);
        assertEquals(creator, collector.getCreatorForType(CreatorCollector.C_INT));

        // Long creator
        creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addLongCreator(creator, false);
        assertEquals(creator, collector.getCreatorForType(CreatorCollector.C_LONG));

        // Double creator
        creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(creator, false);
        assertEquals(creator, collector.getCreatorForType(CreatorCollector.C_DOUBLE));

        // Boolean creator
        creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(creator, false);
        assertEquals(creator, collector.getCreatorForType(CreatorCollector.C_BOOLEAN));

        // Delegating creator
        creator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        collector.addDelegatingCreator(creator, false, injectables);
        assertEquals(creator, collector.getCreatorForType(CreatorCollector.C_DELEGATE));

        // Property-based creator
        creator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        collector.addPropertyCreator(creator, false, properties);
        assertEquals(creator, collector.getCreatorForType(CreatorCollector.C_PROPS));

        // No creator for type, should return null
        assertNull(collector.getCreatorForType(CreatorCollector.C_DEFAULT + 1));
    }
}