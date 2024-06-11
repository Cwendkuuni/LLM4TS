package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ObjectIdInfoTest {

    @org.junit.Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @org.junit.Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);

        ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(false);
        assertNotSame(objectIdInfo, withAlwaysAsId);
        assertFalse(withAlwaysAsId.getAlwaysAsId());

        ObjectIdInfo withAlwaysAsIdBack = withAlwaysAsId.withAlwaysAsId(true);
        assertNotSame(withAlwaysAsId, withAlwaysAsIdBack);
        assertTrue(withAlwaysAsIdBack.getAlwaysAsId());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoNullResolver() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;

        // null resolver should throw an IllegalArgumentException
        new ObjectIdInfo(propertyName, scope, generator, false, null);
    }

    private static class MyCustomObjectIdGenerator implements ObjectIdGenerator<Object> {
    }
}