
package org.junit.test; // Replace with the actual package name

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.matchers.Equality.*;

public class EqualityTest {
    // Test cases go here

    // 
    @Test
    public void testAreEqual() {
        Object o1 = new Object();
        Object o2 = new Object();
        assertTrue(areEqual(o1, o2));
        Object o3 = new Object();
        assertFalse(areEqual(o1, o3));
        Object o4 = null;
        assertFalse(areEqual(o1, o4));
        Object o5 = new Object[] {o1, o2};
        Object o6 = new Object[] {o1, o3};
        assertFalse(areEqual(o5, o6));
    }

    @Test
    public void testAreArraysEqual() {
        Object[] o1 = new Object[] {new Object(), new Object()};
        Object[] o2 = new Object[] {new Object(), new Object()};
        assertTrue(areArraysEqual(o1, o2));

        Object[] o3 = new Object[] {new Object(), new Object()};
        Object[] o4 = new Object[] {new Object(), new Object(), new Object()};
        assertFalse(areArraysEqual(o3, o4));

        Object[] o5 = new Object[] {null};
        Object[] o6 = new Object[] {new Object()};
        assertFalse(areArraysEqual(o5, o6));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        Object[] o1 = new Object[2];
        Object[] o2 = new Object[2];
        assertTrue(areArrayLengthsEqual(o1, o2));

        Object[] o3 = new Object[2];
        Object[] o4 = new Object[3];
        assertFalse(areArrayLengthsEqual(o3, o4));

        Object[] o5 = new Object[0];
        Object[] o6 = new Object[1];
        assertFalse(areArrayLengthsEqual(o5, o6));
    }

    @Test
    public void testAreArrayElementsEqual() {
        Object[] o1 = new Object[] {1, 2};
        Object[] o2 = new Object[] {1, 2};
        assertTrue(areArrayElementsEqual(o1, o2));

        Object[] o3 = new Object[] {1, 2};
        Object[] o4 = new Object[] {1, 3};
        assertFalse(areArrayElementsEqual(o3, o4));

        Object[] o5 = new Object[] {null};
        Object[] o6 = new Object[] {new Object()};
        assertFalse(areArrayElementsEqual(o5, o6));
    }

    @Test
    public void testIsArray() {
        Object o1 = new Object();
        assertFalse(isArray(o1));

        Object[] o2 = new Object[0];
        assertTrue(isArray(o2));
    }

    @Test
    public void testAreArraysEqualWithDifferentLengthsButSameElements() {
        Object[] o1 = new Object[] {1, 2, 3, 3, 3};
        Object[] o2 = new Object[] {1, 2, 3, 3, 3, 3};
        assertTrue(areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqualWithSameLengthButDifferentElements() {
        Object[] o1 = new Object[] {1, 2, 3};
        Object[] o2 = new Object[] {1, 2, 4};
        assertFalse(areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqualWithNullElements() {
        Object[] o1 = new Object[] {1, null, 3};
        Object[] o2 = new Object[] {1, 2, 3};
        assertFalse(areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqualWithDifferentBaseTypes() {
        Object[] o1 = new Object[] {1, "two", 3};
        Object[] o2 = new Object[] {1, 2, 3};
        assertFalse(areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqualWithPrimitiveTypes() {
        int[] o1 = new int[] {1, 2, 3};
        int[] o2 = new int[] {1, 2, 3};
        assertTrue(areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqualWithCustomClasses() {
        CustomClass[] o1 = new CustomClass[] {new CustomClass(1), new CustomClass(2)};
        CustomClass[] o2 = new CustomClass[] {new CustomClass(1), new CustomClass(2)};
        assertTrue(areArraysEqual(o1, o2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenComparingNonArrays() {
        Object o1 = new Object();
        Object o2 = new Object();
        assertFalse(areEqual(o1, o2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenComparingNullArrays() {
        Object[] o1 = null;
        Object[] o2 = new Object[0];
        assertFalse(areEqual(o1, o2));
    }
}
