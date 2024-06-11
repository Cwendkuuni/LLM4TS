import org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Test with null
        assertFalse(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));

        // Test with primitives
        byte a = 10;
        byte b = 10;
        assertTrue(Equality.areEqual(a, b));

        int c = 10;
        int d = 10;
        assertTrue(Equality.areEqual(c, d));

        char e = 'a';
        char f = 'a';
        assertTrue(Equality.areEqual(e, f));

        short g = 10;
        short h = 10;
        assertTrue(Equality.areEqual(g, h));

        boolean i = true;
        boolean j = true;
        assertTrue(Equality.areEqual(i, j));

        // Test with arrays
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));

        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2, 4};
        assertFalse(Equality.areEqual(arr3, arr4));

        int[] arr5 = {1, 2, 3};
        int[] arr6 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr5, arr6));

        int[] arr7 = {1, 2, 3};
        Object[] arr8 = {1, 2, "three"};
        assertFalse(Equality.areEqual(arr7, arr8));

        // Test with custom classes
        MyClass obj1 = new MyClass();
        MyClass obj2 = new MyClass();
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    // Test areArraysEqual()
    @Test
    public void testAreArraysEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));

        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2, 4};
        assertFalse(Equality.areArraysEqual(arr3, arr4));

        int[] arr5 = {1, 2, 3};
        int[] arr6 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr5, arr6));

        int[] arr7 = {1, null, 3};
        int[] arr8 = {1, null, 3};
        assertTrue(Equality.areArraysEqual(arr7, arr8));

        int[] arr9 = {1, 2, 3};
        Object[] arr10 = {1, 2, "three"};
        assertFalse(Equality.areArraysEqual(arr9, arr10));
    }

    // Test areArrayLengthsEqual() and areArrayElementsEqual()
    @Test
    public void testAreArraysEqual_length_and_elements() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
        assertEquals(3, Equality.Array.getLength(arr1));
        assertEquals(3, Equality.Array.getLength(arr2));

        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2, 4};
        assertFalse(Equality.areArraysEqual(arr3, arr4));
        assertEquals(3, Equality.Array.getLength(arr3));
        assertEquals(4, Equality.Array.getLength(arr4));

        int[] arr5 = {1, 2, 3};
        int[] arr6 = {1, 1, 1};
        assertFalse(Equality.areArraysEqual(arr5, arr6));

        int[] arr7 = {1, 2, 3};
        int[] arr8 = {1, null, 3};
        assertTrue(Equality.areArraysEqual(arr7, arr8));

        int[] arr9 = {1, 2, new Object()};
        int[] arr10 = {1, 2, new Object()};
        assertTrue(Equality.areArraysEqual(arr9, arr10));
    }

    class MyClass {}
}