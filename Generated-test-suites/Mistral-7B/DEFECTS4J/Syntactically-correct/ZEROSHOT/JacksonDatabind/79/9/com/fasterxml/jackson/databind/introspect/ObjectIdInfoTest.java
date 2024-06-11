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
 * Unit tests for ObjectIdInfo class
 */
public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo actual = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        ObjectIdInfo expected = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        assertEquals(expected, actual);
    }

    @Test
    public void testObjectIdInfoConstructionWithDefaultResolver() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;

        ObjectIdInfo actual = new ObjectIdInfo(propertyName, scope, generator, false);

        ObjectIdInfo expected = new ObjectIdInfo(propertyName, scope, generator, false, SimpleObjectIdResolver.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo actual = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo withAlwaysAsId = actual.withAlwaysAsId(true);

        ObjectIdInfo expected = new ObjectIdInfo(propertyName, scope, generator, true, resolver);

        assertEquals(expected, withAlwaysAsId);
    }

    @Test
    public void testObjectIdInfoToString() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        String actual = objectIdInfo.toString();
        String expected = "ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class CustomObjectIdGenerator, alwaysAsId=false";

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullPropertyName() {
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, scope, generator, false, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullScope() {
        PropertyName propertyName = new PropertyName("id");
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, null, generator, false, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullGenerator() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scope = String.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null, false, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullResolver() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
    }
}

class CustomObjectIdGenerator {}
class CustomObjectIdResolver {}