package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import org.junit.Test;

/**
 * Test class for ObjectIdInfo
 */
public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoDefaultConstructor() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null, null);
        assertNull(objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertEquals("ObjectIdInfo: propName=null, scope=null, generatorType=null, alwaysAsId=false",
                objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoMinimalConstructor() {
        PropertyName propertyName = new PropertyName("testProperty");
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, null, null, false, null);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertEquals("ObjectIdInfo: propName=testProperty, scope=null, generatorType=null, alwaysAsId=false",
                objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoFullConstructor() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = new IdGenerator();
        Class<? extends ObjectIdResolver> resolver = new IdResolver();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.IdGenerator, alwaysAsId=true",
                objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = new IdGenerator();
        Class<? extends ObjectIdResolver> resolver = new IdResolver();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertNotSame(objectIdInfo, objectIdInfoWithAlwaysAsId);
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.IdGenerator, alwaysAsId=true",
                objectIdInfoWithAlwaysAsId.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoNullPropertyName() {
        new ObjectIdInfo(null, null, null, false, null);
    }

    private static class IdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public ObjectId generateId(SerializerProvider serializerProvider, Object bean) {
            return null;
        }
    }

    private static class IdResolver implements ObjectIdResolver {
        @Override
        public ObjectId idFromValue(SerializerProvider serializerProvider, Object value) {
            return null;
        }
    }
}