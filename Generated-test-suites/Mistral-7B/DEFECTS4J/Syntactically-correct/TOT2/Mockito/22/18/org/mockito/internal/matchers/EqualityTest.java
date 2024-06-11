import org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_whenBothObjectsNull_thenReturnTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_whenBothObjectsSameInstance_thenReturnTrue() {
        String obj1 = new String("test");
        assertTrue(Equality.areEqual(obj1, obj1));
    }

    @Test
    public void testAreEqual_whenObjectsAreEqual_thenReturnTrue() {
        String obj1 = new String("test");
        String obj2 = new String("test");
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_whenObjectsAreDifferentInstancesButEqual_thenReturnTrue() {
        String obj1 = new String("test");
        String obj2 = new String("test");
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_whenObjectsAreNotEqual_thenReturnFalse() {
        String obj1 = new String("test1");
        String obj2 = new String("test2");
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreArraysEqual_whenBothArraysNull_thenReturnTrue() {
        Object[] o1 = null;
        Object[] o2 = null;
        assertTrue(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqual_whenBothArraysSameInstance_thenReturnTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = arr1;
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_whenArraysAreEqual_thenReturnTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_whenArraysAreDifferentLengths_thenReturnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_whenArraysAreNotEqual_thenReturnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 3, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayLengthsEqual_whenArraysAreBothNull_thenReturnTrue() {
        Object[] o1 = null;
        Object[] o2 = null;
        assertTrue(Equality.areArrayLengthsEqual(o1, o2));
    }

    @Test
    public void testAreArrayLengthsEqual_whenArraysAreBothEmpty_thenReturnTrue() {
        Object[] o1 = new Object[0];
        Object[] o2 = new Object[0];
        assertTrue(Equality.areArrayLengthsEqual(o1, o2));
    }

    @Test
    public void testAreArrayLengthsEqual_whenArraysHaveDifferentLengths_thenReturnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_whenArrayIsEmpty_thenReturnTrue() {
        Object[] o1 = new Object[0];
        Object[] o2 = new Object[0];
        assertTrue(Equality.areArrayElementsEqual(o1, o2));
    }

    @Test
    public void testAreArrayElementsEqual_whenArraysAreBothNull_thenReturnTrue() {
        Object[] o1 = null;
        Object[] o2 = null;
        assertTrue(Equality.areArrayElementsEqual(o1, o2));
    }

    @Test
    public void testAreArrayElementsEqual_whenArraysAreEqual_thenReturnTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testAreArrayElementsEqual_whenArraysHaveDifferentLengths_thenReturnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
    }

    @Test
    public void testIsArray_whenObjectIsArray_thenReturnTrue() {
        int[] arr = {1, 2, 3};
        assertTrue(Equality.isArray(arr));
    }

    @Test
    public void testIsArray_whenObjectIsNotArray_thenReturnFalse() {
        String str = new String("test");
        assertFalse(Equality.isArray(str));
    }
}