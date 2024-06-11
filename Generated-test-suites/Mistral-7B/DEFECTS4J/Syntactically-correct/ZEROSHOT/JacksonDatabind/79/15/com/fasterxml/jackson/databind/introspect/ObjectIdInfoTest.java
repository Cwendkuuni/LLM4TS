package com.fasterxml.jackson.databind.introspect.test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link ObjectIdInfo} class
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
    }

    @Test
    public void testObjectIdInfoWithPropertyNameAndScopeConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null, false, null);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithPropertyNameStringScopeAndGeneratorConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithPropertyNameStringScopeGeneratorAndResolverConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        Class<CustomIdResolver> resolver = CustomIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsIdFlag() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomIdGenerator> generator = CustomIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, null);
        ObjectIdInfo withAlwaysAsIdFalse = new ObjectIdInfo(objectIdInfo.getPropertyName(),
                objectIdInfo.getScope(), objectIdInfo.getGeneratorType(), false, objectIdInfo.getResolverType());

        assertNotEquals(objectIdInfo, withAlwaysAsIdFalse);
        assertTrue(objectIdInfo.getAlwaysAsId());
        assertFalse(withAlwaysAsIdFalse.getAlwaysAsId());
    }

    /**
     * Custom IdGenerator and IdResolver classes for testing
     */
    public static class CustomIdGenerator implements ObjectIdGenerator<String> {
    }

    public static class CustomIdResolver implements ObjectIdResolver {
        @Override
        public ObjectId resolveId(String id) {
            return null;
        }
    }
}