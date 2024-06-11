package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for CreatorCollector.
 */
public class CreatorCollectorTest {

    /**
     * Test constructor with null bean description and canFixAccess = false
     */
    @Test
    public void testConstructorNullBeanDescFalse() {
        try {
            new CreatorCollector(null, false);
            fail("should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Null BeanDescription not allowed", e.getMessage());
        }

        try {
            new CreatorCollector(null, true);
            fail("should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Null BeanDescription not allowed", e.getMessage());
        }
    }

    /**
     * Test constructor with null bean description and canFixAccess = true
     */
    @Test
    public void testConstructorNullBeanDescTrue() {
        try {
            CreatorCollector collector = new CreatorCollector(null, true);
            fail("should not reach here");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test constructor with non-null bean description and canFixAccess = false
     */
    @Test
    public void testConstructorNonNullBeanDescFalse() throws Exception {
        BeanDescription beanDescription = new BeanDescription(AnnotatedClass.construct(CreatorCollector.class), null, null);
        CreatorCollector collector = new CreatorCollector(beanDescription, false);

        // test default creator
        Object defaultInstance = collector.constructValueInstantiator(new DeserializationConfig()).createValue(new Object(), null);
        assertTrue(defaultInstance instanceof CreatorCollector);

        // test string creator
        String stringValue = "hello";
        Method stringCreatorMethod = CreatorCollector.class.getMethod("setStringCreator", AnnotatedWithParams.class);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringCreatorMethod, new Object[]{}, new Class[0]);
        collector.setStringCreator(stringCreator);
        Object stringInstance = collector.constructValueInstantiator(new DeserializationConfig()).createValue(new Object(), null);
        assertEquals(stringValue, stringInstance);

        // test int creator
        int intValue = 10;
        Method intCreatorMethod = CreatorCollector.class.getMethod("addIntCreator", AnnotatedWithParams.class);
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intCreatorMethod, new Object[]{new Object()}, new Class[]{int[].class});
        collector.addIntCreator(intCreator, true);
        Object intInstance = collector.constructValueInstantiator(new DeserializationConfig()).createValue(new Object(), null);
        assertEquals(intValue, intInstance.hashCode());
    }

    /**
     * Test constructor with non-null bean description and canFixAccess = true
     */
    @Test
    public void testConstructorNonNullBeanDescTrue() throws Exception {
        BeanDescription beanDescription = new BeanDescription(AnnotatedClass.construct(CreatorCollector.class), null, null);
        CreatorCollector collector = new CreatorCollector(beanDescription, true);

        // test default creator
        Object defaultInstance = collector.constructValueInstantiator(new DeserializationConfig()).createValue(new Object(), null);
        assertTrue(defaultInstance instanceof CreatorCollector);

        // test string creator
        String stringValue = "hello";
        Method stringCreatorMethod = CreatorCollector.class.getMethod("setStringCreator", AnnotatedWithParams.class);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringCreatorMethod, new Object[]{}, new Class[0]);
        collector.setStringCreator(stringCreator);
        Object stringInstance = collector.constructValueInstantiator(new DeserializationConfig()).createValue(new Object(), null);
        assertEquals(stringValue, stringInstance.toString());

        // test int creator
        int intValue = 10;
        Method intCreatorMethod = CreatorCollector.class.getMethod("addIntCreator", AnnotatedWithParams.class);
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intCreatorMethod, new Object[]{new Object()}, new Class[]{int[].class});
        collector.addIntCreator(intCreator, true);
        Object intInstance = collector.constructValueInstantiator(new DeserializationConfig()).createValue(new Object(), null);
        assertEquals(intValue, intInstance.hashCode());

        // test long creator
        long longValue = 20L;
        Method longCreatorMethod = CreatorCollector.class.getMethod("addLongCreator", AnnotatedWithParams.class);
        AnnotatedWithParams longCreator = new AnnotatedWithParams(longCreatorMethod, new Object[]{new Object()}, new Class[]{long[].class});
        collector.addLongCreator(longCreator, true);
        Object longInstance = collector.constructValueInstantiator(new DeserializationConfig()).createValue(new Object(), null);
        assertEquals(longValue, longInstance.hashCode());

        // test double creator
        double doubleValue = 3.14;
        Method doubleCreatorMethod = CreatorCollector.class.getMethod("addDoubleCreator", AnnotatedWithParams.class);
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(doubleCreatorMethod, new Object[]{new Object()}, new Class[]{double[].class});
        collector.addDoubleCreator(doubleCreator, true);
        Object doubleInstance = collector.constructValueInstantiator(new DeserializationConfig()).createValue(new Object(), null);
        assertEquals(doubleValue, doubleInstance.hashCode(), 0.001);

        // test boolean creator
        boolean booleanValue = true;
        Method booleanCreatorMethod = CreatorCollector.class.getMethod("addBooleanCreator", AnnotatedWithParams.class);
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(booleanCreatorMethod, new Object[]{new Object()}, new Class[]{boolean[].class});
        collector.addBooleanCreator(booleanCreator, true);
        Object booleanInstance = collector.constructValueInstantiator(new DeserializationConfig()).createValue(new Object(), null);
        assertEquals(booleanValue, booleanInstance.hashCode());

        // test delegate creator
        Object delegateInstance = new Object();
        Object[] delegateArgs = new Object[]{new Object(), new Object()};
        Method delegateCreatorMethod = CreatorCollector.class.getMethod("addDelegatingCreator", AnnotatedWithParams.class, CreatorProperty[].class);
        CreatorProperty[] delegateArgsProperties = new CreatorProperty[]{new CreatorProperty(String.class, "delegateProperty"), new CreatorProperty(String.class, "delegateProperty2")};
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(delegateCreatorMethod, new Object[]{new Object(), new Object(), new Object()}, new Class[]{CreatorProperty[].class});
        collector.addDelegatingCreator(delegateCreator, true, delegateArgsProperties);
        Object delegatedInstance = collector.constructValueInstantiator(new DeserializationConfig()).createValue(new Object[]{delegateInstance}, null);
        assertEquals(delegateInstance, delegatedInstance);

        // test property creator
        Object propertyInstance = new Object();
        PropertyName propertyName = PropertyName.construct("propertyName");
        Method propertyCreatorMethod = CreatorCollector.class.getMethod("addPropertyCreator", AnnotatedWithParams.class, CreatorProperty[].class);
        CreatorProperty[] propertyArgs = new CreatorProperty[]{new CreatorProperty(String.class, "propertyName"), new CreatorProperty(String.class, "propertyValue")};
        AnnotatedWithParams propertyCreator = new AnnotatedWithParams(propertyCreatorMethod, new Object[]{propertyInstance}, new Class[0]);
        collector.addPropertyCreator(propertyCreator, true, propertyArgs);
        Object propertyValue = "propertyValue";
        Object propertyCreatedInstance = collector.constructValueInstantiator(new DeserializationConfig()).createValue(new Object[]{propertyInstance}, new Object[]{propertyValue});
        Node propertyValueNode = new ObjectNode(new ObjectMapper().getFactory());
        propertyValueNode.put("propertyName", propertyValue);
        assertEquals(propertyValueNode, propertyCreatedInstance);
    }
}