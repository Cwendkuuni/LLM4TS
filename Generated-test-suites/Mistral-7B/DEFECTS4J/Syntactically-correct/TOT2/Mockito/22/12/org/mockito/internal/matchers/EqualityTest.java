import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_SameObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    // ... Tests for other methods and edge cases ...

    @Test
    public void testAreArraysEqual_SubArray() {
        int[] arr1 = {1, 2, 3, 4};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_NegativeIndices() {
        int[] arr1 = {1, 2, 3, 4};
        int[] arr2 = {1, 2, 3, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr2, -1, -1));
    }

    @Test
    public void testAreArraysEqual_LargeArrays() {
        int[] arr1 = new int[100_000];
        int[] arr2 = new int[100_000];
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArray_NullArray() {
        Object[] arr1 = null;
        Object[] arr2 = new Object[0];
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqual_ArrayWithNegativeLength() {
        int[] arr1 = {1, 2, 3, 4};
        int[] arr2 = new int[-1];
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreArraysEqual_ArraysWithDifferentTypes() {
        int[] arr1 = {1, 2, 3};
        String[] arr2 = {"1", "2", "3"};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    // ... Other tests for edge cases and error scenarios ...

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("EqualityTest");
    }

}