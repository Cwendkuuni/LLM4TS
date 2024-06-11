package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(AtomicReference.class, new AtomicReferenceDeserializer(null, null, null, null));
        mapper.registerModule(module);
    }

    @org.junit.Test
    public void testAtomicReferenceDeserializer() throws IOException {
        // Test constructor
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
                null, null, null);
        assertNotNull(deserializer);

        // Test getNullValue method
        DeserializationContext context = mock(DeserializationContext.class);
        AtomicReference<Object> nullValue = deserializer.getNullValue(context);
        assertNotNull(nullValue);
        assertEquals(null, nullValue.get());

        // Test getEmptyValue method
        Object emptyValue = deserializer.getEmptyValue(context);
        assertNotNull(emptyValue);
        assertNotNull(emptyValue instanceof AtomicReference);
        assertEquals(null, ((AtomicReference<Object>) emptyValue).get());

        // Test referenceValue method
        Object referenceValue = new Object();
        AtomicReference<Object> referencedValue = deserializer.referenceValue(referenceValue);
        assertNotNull(referencedValue);
        assertSame(referenceValue, referencedValue.get());

        // Test getReferenced method
        AtomicReference<Object> atomicReference = new AtomicReference<Object>(new Object());
        Object referencedObject = deserializer.getReferenced(atomicReference);
        assertNotNull(referencedObject);
        assertSame(atomicReference.get(), referencedObject);

        // Test updateReference method
        Object updatedValue = new Object();
        AtomicReference<Object> updatedReference = new AtomicReference<Object>(atomicReference);
        AtomicReference<Object> updatedAtomicReference = deserializer.updateReference(updatedReference, updatedValue);
        assertNotNull(updatedAtomicReference);
        assertSame(updatedReference, updatedAtomicReference);
        assertSame(updatedValue, updatedReference.get());

        // Test supportsUpdate method
        assertTrue(deserializer.supportsUpdate(mapper.getDeserializationConfig()));

        // Test deserialization
        String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializerTest$TestObject\",\"value\":42}";
        TypeReference<AtomicReference<TestObject>> typeReference = new TypeReference<AtomicReference<TestObject>>() {};
        AtomicReference<TestObject> deserializedObject = mapper.readValue(json, typeReference);
        assertNotNull(deserializedObject);
        assertSame(TestObject.class, deserializedObject.get().getClass());
        assertEquals(42, deserializedObject.get().getValue());
    }

    static class TestObject {
        private int value;

        public int getValue() {
            return value;
        }
    }
}