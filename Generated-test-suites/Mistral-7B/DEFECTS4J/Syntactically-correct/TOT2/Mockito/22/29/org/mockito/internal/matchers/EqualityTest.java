package org.mockito.internal.matchers.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Test with same objects
        assertTrue(Equality.areEqual(new Integer(1), new Integer(1)));

        // Test with null values
        assertTrue(Equality.areEqual(null, null));

        // Test with different types
        assertFalse(Equality.areEqual("Hello", new Integer(1)));

        // Test with different objects of the same class
        Integer i1 = new Integer(1);
        Integer i2 = new Integer(1);
        assertFalse(i1.equals(i2)); // Different objects, equals method returns false
        assertTrue(Equality.areEqual(i1, i2)); // Equality class checks if they are equal
    }

    @Test
    public void testAreArraysEqual() {
        int[] array1 = new int[]{1, 2, 3};
        int[] array2 = new int[]{1, 2, 3};
        int[] array3 = new int[]{1, 2, 4};

        // Test with same arrays
        assertTrue(Equality.areArraysEqual(array1, array2));

        // Test with arrays of different lengths
        assertFalse(Equality.areArraysEqual(array1, array3));

        // Test with arrays of different types
        String[] stringArray1 = new String[]{"Hello", "World"};
        int[] intArray = new int[]{1, 2, 3};
        assertFalse(Equality.areArraysEqual(stringArray1, intArray));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        int[] array1 = new int[]{1, 2, 3};
        int[] array2 = new int[]{1, 2, 3};
        int[] array3 = new int[]{1, 2, 3, 4};

        // Test with arrays of the same length
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));

        // Test with arrays of different lengths
        assertFalse(Equality.areArrayLengthsEqual(array1, array3));
    }

    @Test
    public void testAreArrayElementsEqual() {
        int[] array1 = new int[]{1, 2, 3};
        int[] array2 = new int[]{1, 2, 3};
        int[] array3 = new int[]{1, 2, 4};

        // Test with arrays with equal elements
        assertTrue(Equality.areArrayElementsEqual(array1, array2));

        // Test with arrays with different elements
        assertFalse(Equality.areArrayElementsEqual(array1, array3));

        // Test with arrays containing null values
        int[] array4 = new int[]{1, null, 3};
        int[] array5 = new int[]{1, 2, 3};
        assertFalse(Equality.areArrayElementsEqual(array4, array5));
    }

    @Test
    public void testIsArray() {
        String str = "Hello";
        int[] intArray = new int[]{1, 2, 3};

        // Test with primitive type
        assertFalse(Equality.isArray(1));

        // Test with String
        assertFalse(Equality.isArray(str));

        // Test with array
        assertTrue(Equality.isArray(intArray));
    }
}