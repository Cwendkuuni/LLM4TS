package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);

        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithAlwaysAsId() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, true);

        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(true, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsIdMethod() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, false);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());

        assertEquals("testProperty", newObjectIdInfo.getPropertyName().getName());
        assertEquals(String.class, newObjectIdInfo.getScope());
        assertEquals(CustomObjectIdGenerator.class, newObjectIdInfo.getGeneratorType());
        assertEquals(true, newObjectIdInfo.getAlwaysAsId());
    }

    static class CustomObjectIdGenerator implements ObjectIdGenerator<Object> {
        // Custom implementation for testing purposes
    }
}