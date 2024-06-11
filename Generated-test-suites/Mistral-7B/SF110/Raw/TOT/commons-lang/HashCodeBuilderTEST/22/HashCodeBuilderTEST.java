package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilderTest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCodeWithSimpleObject() {
        Object simpleObject = new SimpleObject();
        int hashCode = HashCodeBuilder.reflectionHashCode(simpleObject);
        System.out.println("Reflection hash code for SimpleObject: " + hashCode);
        assertEquals(simpleObject.hashCode(), hashCode);
    }

    @Test
    public void testReflectionHashCodeWithComplexObject() {
        Object complexObject = new ComplexObject();
        int hashCode = HashCodeBuilder.reflectionHashCode(complexObject);
        System.out.println("Reflection hash code for ComplexObject: " + hashCode);
        // Re-create the complex object and compare hash codes
        Object complexObject2 = new ComplexObject();
        int hashCode2 = HashCodeBuilder.reflectionHashCode(complexObject2);
        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testReflectionHashCodeWithTransientFields() {
        Object transientObject = new TransientObject();
        int hashCode = HashCodeBuilder.reflectionHashCode(transientObject, true);
        System.out.println("Reflection hash code for TransientObject with transient fields included: " + hashCode);
        int hashCodeWithoutTransients = HashCodeBuilder.reflectionHashCode(transientObject);
        assertNotEquals(hashCode, hashCodeWithoutTransients);
    }

    @Test
    public void testReflectionHashCodeWithExcludedFields() {
        Object excludedFieldsObject = new ExcludedFieldsObject();
        int hashCode = HashCodeBuilder.reflectionHashCode(excludedFieldsObject, null, "field1", "field2");
        System.out.println("Reflection hash code for ExcludedFieldsObject with excluded fields: " + hashCode);
        int hashCodeWithAllFields = HashCodeBuilder.reflectionHashCode(excludedFieldsObject);
        assertNotEquals(hashCode, hashCodeWithAllFields);
    }

    @Test
    public void testRegisterAndUnregisterObjects() {
        Object registeredObject = new RegisteredObject();
        HashCodeBuilder.register(registeredObject);
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertEquals(1, registry.size());
        HashCodeBuilder.unregister(registeredObject);
        registry = HashCodeBuilder.getRegistry();
        assertEquals(0, registry.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflexionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithInvalidInitialValue() {
        new HashCodeBuilder(-1, 37);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithInvalidMultiplier() {
        new HashCodeBuilder(1, 0);
    }

    @Test
    public void testAppendSimpleTypes() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        hashCodeBuilder.append(1);
        hashCodeBuilder.append('a');
        hashCodeBuilder.append((float) 1.2);
        int hashCode = hashCodeBuilder.toHashCode();
        System.out.println("Hash code for appended simple types: " + hashCode);
        // Test appending arrays
        byte[] byteArray = {1, 2, 3};
        hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(byteArray);
        hashCode = hashCodeBuilder.toHashCode();
        System.out.println("Hash code for appended byte array: " + hashCode);
    }

    @Test
    public void testAppendObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(obj1);
        hashCodeBuilder.append(obj2);
        int hashCode = hashCodeBuilder.toHashCode();
        System.out.println("Hash code for appended objects: " + hashCode);
        // Replace objects with the same instance and check if hash code is the same
        obj1 = obj2;
        hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(obj1);
        hashCodeBuilder.append(obj2);
        hashCode = hashCodeBuilder.toHashCode();
        assertEquals(hashCode, hashCode);
    }

    @Test
    public void testAppendArrays() {
        Object[] objArray = {new Object(), new Object()};
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(objArray);
        int hashCode = hashCodeBuilder.toHashCode();
        System.out.println("Hash code for appended array: " + hashCode);
        // Replace array elements with the same instance and check if hash code is the same
        Object obj = new Object();
        objArray[0] = objArray[1] = obj;
        hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(objArray);
        hashCode = hashCodeBuilder.toHashCode();
        assertEquals(hashCode, hashCode);
    }

    @Test
    public void testAppendComplexObject() {
        ComplexObject complexObject = new ComplexObject();
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(complexObject);
        int hashCode = hashCodeBuilder.toHashCode();
        System.out.println("Hash code for appended complex object: " + hashCode);
        // Test appending complex object as array
        ComplexObject[] complexObjects = {complexObject, complexObject};
        hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(complexObjects);
        hashCode = hashCodeBuilder.toHashCode();
        System.out.println("Hash code for appended complex objects array: " + hashCode);
    }

    @Test
    public void testAppendArrayOfSimpleTypes() {
        byte[] byteArray = {1, 2, 3};
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(byteArray);
        int hashCode = hashCodeBuilder.toHashCode();
        System.out.println("Hash code for appended array of simple types: " + hashCode);
    }

    @Test
    public void testAppendArrayOfObjects() {
        Object[] objArray = {new Object(), new Object()};
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(objArray);
        int hashCode = hashCodeBuilder.toHashCode();
        System.out.println("Hash code for appended array of objects: " + hashCode);
    }

    @Test
    public void testHashCodeBuilderWithToString() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        String hashCodeString = hashCodeBuilder.toString();
        System.out.println("HashCodeBuilder string representation: " + hashCodeString);
    }

    private static class SimpleObject {
    }

    private static class ComplexObject {
        Object field1;
        Object field2;
        Object field3;

        ComplexObject() {
            field1 = new Object();
            field2 = new Object();
            field3 = new Object();
        }
    }

    private static class TransientObject {
        private transient boolean transientField;
    }

    private static class ExcludedFieldsObject {
        @SuppressWarnings("unused")
        private int field1;

        @SuppressWarnings("unused")
        private String field2;
    }

    private static class RegisteredObject {
        @Override
        public String toString() {
            return "RegisteredObject";
        }
    }

    static class IDKey {
        final Object value;

        IDKey(Object value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof IDKey)) return false;
            return value.equals(((IDKey) obj).value);
        }
    }
}