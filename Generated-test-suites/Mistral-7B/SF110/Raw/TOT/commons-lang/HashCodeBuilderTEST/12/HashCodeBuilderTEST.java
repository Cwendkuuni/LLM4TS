package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    private static Object testObject;
    private static Class<?> testObjectClass;

    @BeforeClass
    public static void setup() {
        testObject = new TestObject();
        testObjectClass = testObject.getClass();
    }

    @Test
    public void testReflectionHashCodeWithDefaults() {
        int hashCode = HashCodeBuilder.reflectionHashCode(testObject);
        // Typical use case: test with default arguments
        assertEquals(139638873, hashCode); // Expected hash code based on the class and fields of TestObject
    }

    @Test
    public void testReflectionHashCodeWithCustomInitialAndMultiplier() {
        int hashCode = HashCodeBuilder.reflectionHashCode(31, 37, testObject);
        // Test with custom initial value and multiplier
        assertEquals(239211327, hashCode); // Expected hash code based on the class and fields of TestObject, with custom initial value and multiplier
    }

    @Test
    public void testReflectionHashCodeWithTestTransients() {
        int hashCode = HashCodeBuilder.reflectionHashCode(testObject, true);
        // Test with testTransients set to true
        assertEquals(118153673, hashCode); // Expected hash code based on the class and fields of TestObject, including transient fields
    }

    @Test
    public void testReflectionHashCodeWithReflectionUpToClass() {
        int hashCode = HashCodeBuilder.reflectionHashCode(testObject, TestObject.class, true, null, new String[0]);
        // Test with reflectionUpToClass set to TestObject.class
        assertEquals(118153673, hashCode); // Expected hash code based on the class and fields of TestObject, including transient fields
    }

    @Test
    public void testReflectionHashCodeWithReflectionUpToSuperClass() {
        int hashCode = HashCodeBuilder.reflectionHashCode(testObject, Object.class, true, TestObject.class, new String[0]);
        // Test with reflectionUpToClass set to Object.class, but TestObject has a non-default hashCode() method
        assertEquals(118153673, hashCode); // Expected hash code based on the hashCode() method of TestObject
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        // Test with a null object
        HashCodeBuilder.reflectionHashCode(null, 17, 37);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionHashCodeWithNullExcludeFields() {
        // Test with null excludeFields
        HashCodeBuilder.reflectionHashCode(testObject, 17, 37, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithEmptyExcludeFields() {
        // Test with an empty excludeFields array
        HashCodeBuilder.reflectionHashCode(testObject, 17, 37, new String[0]);
    }

    @Test(expected = InternalError.class)
    public void testReflectionHashCodeWithIllegalAccess() {
        try {
            Class<?> illegalAccessTestObjectClass = Class.forName("org.apache.commons.lang3.builder.IllegalAccessTestObject");
            Object illegalAccessTestObject = illegalAccessTestObjectClass.newInstance();
            HashCodeBuilder.reflectionHashCode(17, 37, illegalAccessTestObject);
        } catch (ClassNotFoundException | InstantiationException e) {
            // Expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testReflectionHashCodeWithRegisteredObject() {
        HashCodeBuilder.register(testObject);
        int hashCode = HashCodeBuilder.reflectionHashCode(testObject);
        // Test with an object that has been registered
        fail("Expected IllegalStateException: Object is already registered for reflection.");
    }

    @Test
    public void testHashCodeBuilderAppendingValues() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        hashCodeBuilder.append('A');
        hashCodeBuilder.append(1);
        hashCodeBuilder.append(1.0);
        hashCodeBuilder.append('Z');
        hashCodeBuilder.append(Arrays.asList(new Integer[]{1, 2, 3}));
        int hashCode = hashCodeBuilder.toHashCode();
        // Test with various types of values appended to HashCodeBuilder
        assertEquals(133662164, hashCode); // Expected hash code based on the appended values
    }

    @Test
    public void testHashCodeBuilderAppendingArrays() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false};
        hashCodeBuilder.append(booleanArray);
        byte[] byteArray = {1, 2, 3};
        hashCodeBuilder.append(byteArray);
        char[] charArray = {'A', 'B', 'C'};
        hashCodeBuilder.append(charArray);
        double[] doubleArray = {1.0, 2.0, 3.0};
        hashCodeBuilder.append(doubleArray);
        float[] floatArray = {1.0f, 2.0f, 3.0f};
        hashCodeBuilder.append(floatArray);
        int[] intArray = {1, 2, 3};
        hashCodeBuilder.append(intArray);
        long[] longArray = {1, 2, 3};
        hashCodeBuilder.append(longArray);
        Object[] objectArray = {new TestObject(), new TestObject()};
        hashCodeBuilder.append(objectArray);
        short[] shortArray = {1, 2, 3};
        hashCodeBuilder.append(shortArray);
        int hashCode = hashCodeBuilder.toHashCode();
        // Test with various types of arrays appended to HashCodeBuilder
        assertEquals(372742632, hashCode); // Expected hash code based on the appended arrays
    }

    @Test
    public void testHashCodeBuilderAppendingNullValue() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(null);
        int hashCode = hashCodeBuilder.toHashCode();
        // Test with a null value appended to HashCodeBuilder
        assertEquals(0, hashCode); // Expected hash code for null value
    }

    @Test
    public void testHashCodeBuilderAppendingNullArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(null);
        int hashCode = hashCodeBuilder.toHashCode();
        // Test with a null array appended to HashCodeBuilder
        assertEquals(0, hashCode); // Expected hash code for null array
    }

    @Test
    public void testHashCodeBuilderWithAppendSuper() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.appendSuper(17);
        int hashCode = hashCodeBuilder.toHashCode();
        // Test with appendSuper method
        assertEquals(17, hashCode); // Expected hash code based on the superHashCode argument
    }

    private static class TestObject {
        private boolean booleanField;
        private byte byteField;
        private char charField;
        private double doubleField;
        private float floatField;
        private int intField;
        private long longField;
        private short shortField;
        private TestObject superClassObject;

        @Override
        public int hashCode() {
            return 118153673;
        }
    }
}