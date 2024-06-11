package org.mockito.internal.matchers.test; // Add the test package to avoid potential conflicts with the production code

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;

import java.util.ObjectSizedArray; // Assuming this is the class used for ObjectSizedArray in your production code

public class EqualityTest {

    @Test
    public void testAreEqualNullObjects() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqualNullAndNonNullObjects() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqualSameObject() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqualObjectsWithSameContent() {
        String str1 = "mockito";
        String str2 = "mockito";
        assertTrue(Equality.areEqual(str1, str2));
    }

    @Test
    public void testAreEqualObjectsWithDifferentContent() {
        String str1 = "mockito";
        String str2 = "jUnit";
        assertFalse(Equality.areEqual(str1, str2));
    }

    @Test
    public void testAreEqualArraysWithSameLengthAndSameContent() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqualArraysWithSameLengthButDifferentContent() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 4};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqualArraysWithDifferentLengthsButSameContent() {
        Object[] array1 = new Object[]{1, 2};
        Object[] array2 = new Object[]{1, 2, 3};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqualArraysWithDifferentTypes() {
        int[] intArray = {1, 2, 3};
        String[] strArray = {"mockito", "jUnit", "junit"};
        assertFalse(Equality.areEqual(intArray, strArray));
    }

    @Test
    public void testAreEqualCustomArrayTypes() {
        ObjectSizedArray<Integer> intArray1 = new ObjectSizedArray<>(3);
        intArray1.add(1);
        intArray1.add(2);
        intArray1.add(3);

        ObjectSizedArray<Integer> intArray2 = new ObjectSizedArray<>(3);
        intArray2.add(1);
        intArray2.add(2);
        intArray2.add(3);

        ObjectSizedArray<Double> doubleArray = new ObjectSizedArray<>(3);
        doubleArray.add(1.0);
        doubleArray.add(2.0);
        doubleArray.add(3.0);

        assertTrue(Equality.areEqual(intArray1, intArray2));
        assertFalse(Equality.areEqual(intArray1, doubleArray));
    }
}