package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
// Test cases for ObjectIdInfo constructor, edge cases, and exception handling
// ...

    @Test
    public void testObjectIdInfoConstructionWithStringName() {
        String propertyName = "propertyName";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);
        assertEquals(new PropertyName(propertyName), objectIdInfo._propertyName);
    }

    @Test
    public void testObjectIdInfoConstructionWithDeprecatedConstructor() {
        String propertyName = "propertyName";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertEquals(new PropertyName(propertyName), objectIdInfo._propertyName);
        assertEquals(scope, objectIdInfo._scope);
        assertEquals(generator, objectIdInfo._generator);
        assertEquals(false, objectIdInfo._alwaysAsId);
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertEquals(objectIdInfo._propertyName, objectIdInfoWithAlwaysAsId._propertyName);
        assertEquals(objectIdInfo._scope, objectIdInfoWithAlwaysAsId._scope);
        assertEquals(objectIdInfo._generator, objectIdInfoWithAlwaysAsId._generator);
        assertEquals(true, objectIdInfoWithAlwaysAsId._alwaysAsId);
    }
}