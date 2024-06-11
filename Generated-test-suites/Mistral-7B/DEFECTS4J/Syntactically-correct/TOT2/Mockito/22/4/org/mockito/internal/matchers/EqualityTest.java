package org.junit.test;

import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        Object o1 = new Object();
        Object o2 = new Object();
        assertTrue(Equality.areEqual(o1, o2)); // both objects are equal

        Object o3 = new Object();
        Object o4 = new Object();
        assertFalse(Equality.areEqual(o3, o4)); // two different objects

        Object o5 = new Object();
        Object o6 = o5;
        assertTrue(Equality.areEqual(o5, o6)); // same object

        Object o7 = new String("test");
        Object o8 = new String("test");
        assertTrue(Equality.areEqual(o7, o8)); // strings are equal

        Object o9 = new StringBuilder("test");
        Object o10 = new StringBuilder("test");
        assertFalse(Equality.areEqual(o9, o10)); // StringBuilder objects are not equal, even if they have the same content

        Object o11 = new byte[0];
        Object o12 = new byte[0];
        assertTrue(Equality.areEqual(o11, o12)); // arrays of primitives are equal when they have the same length and all elements are the same
    }

    @Test
    public void testAreArraysEqual() {
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(array1, array2)); // both arrays have the same elements in the same order

        Object[] array3 = new Object[]{1, 2, 3, 4};
        assertFalse(Equality.areArraysEqual(array1, array3)); // arrays have different lengths

        Object[] array4 = new Object[]{1, 2, 3};
        Object[] array5 = new Object[]{3, 2, 1};
        assertFalse(Equality.areArraysEqual(array1, array5)); // arrays have different elements in different order

        Object[] array6 = new Object[]{"test1", "test2", "test3"};
        Object[] array7 = new Object[]{"test2", "test1", "test3"};
        assertFalse(Equality.areArraysEqual(array6, array7)); // arrays have different elements, even if they are strings

        Object[] array8 = new Object[]{1, 2, 3};
        Object[] array9 = new Object[]{"1", "2", "3"};
        assertFalse(Equality.areArraysEqual(array8, array9)); // arrays have different types of elements

        Object[] array10 = new Object[3];
        for (int i = 0; i < array10.length; i++) {
            array10[i] = new Object();
        }
        Object[] array11 = new Object[3];
        for (int i = 0; i < array11.length; i++) {
            array11[i] = new Object();
        }
        assertTrue(Equality.areArraysEqual(array10, array11)); // arrays with the same length and same type of elements are equal, even if their contents are different
    }
}