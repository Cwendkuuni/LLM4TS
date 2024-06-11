package org.mockito.internal.matchers.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        Object obj1 = null;
        Object obj2 = null;
        assertTrue(Equality.areEqual(obj1, obj2));

        Object str1 = "Test";
        Object str2 = "Test";
        assertTrue(Equality.areEqual(str1, str2));

        Object int1 = 10;
        Object int2 = 10;
        assertTrue(Equality.areEqual(int1, int2));

        Object arr1 = new Object[]{"Test", 10};
        Object arr2 = new Object[]{"Test", 10};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual() {
        Object arr1 = new Object[]{1, 2, 3};
        Object arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));

        Object arr3 = new Object[]{1, 2, 3, 4};
        Object arr4 = new Object[]{1, 2, 3};
        assertFalse(Equality.areArraysEqual(arr3, arr4));

        Object arr5 = new Object[]{1, 1, 3};
        Object arr6 = new Object[]{1, 2, 3};
        assertFalse(Equality.areArraysEqual(arr5, arr6));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        Object arr1 = new Object[]{1, 2, 3};
        Object arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));

        Object arr3 = new Object[]{1, 2, 3};
        Object arr4 = new Object[]{1, 2};
        assertFalse(Equality.areArrayLengthsEqual(arr3, arr4));

        Object arr5 = new Object[0];
        Object arr6 = new Object[]{};
        assertTrue(Equality.areArrayLengthsEqual(arr5, arr6));
    }

    @Test
    public void testAreArrayElementsEqual() {
        Object arr1 = new Object[]{1, 2, 3};
        Object arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));

        Object arr3 = new Object[]{1, 2, 3, 4};
        Object arr4 = new Object[]{1, 2, 3, 4};
        assertTrue(Equality.areArrayElementsEqual(arr3, arr4));

        Object arr5 = new Object[]{1, 2, 3, 4};
        Object arr6 = new Object[]{1, 2, 3, 5};
        assertFalse(Equality.areArrayElementsEqual(arr5, arr6));

        Object arr7 = new Object[]{1, 2, 3, 4};
        Object arr8 = new Object[]{1, 2, 3};
        assertFalse(Equality.areArrayElementsEqual(arr7, arr8));
    }

    @Test
    public void testIsArray() {
        Object obj1 = new Object();
        assertFalse(Equality.isArray(obj1));

        Object arr1 = new Object[]{1, 2, 3};
        assertTrue(Equality.isArray(arr1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsArrayWithNull() {
        Object obj1 = null;
        Equality.isArray(obj1);
    }
}