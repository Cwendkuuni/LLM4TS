import org.junit.Test;
import static org.mockito.internal.matchers.Equality.*;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_SameObjects() {
        Object o1 = new Object();
        Object o2 = o1;
        assertTrue(areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_DifferentObjects_Null() {
        Object o1 = null;
        Object o2 = new Object();
        assertFalse(areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_DifferentObjects_DifferentClasses() {
        Object o1 = new Object();
        int o2 = 42;
        assertFalse(areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_DifferentObjects_SameValues() {
        Object o1 = "hello";
        Object o2 = "hello";
        assertTrue(areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_Arrays_SameLengthAndSameElements() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_Arrays_DifferentLength() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3, 4};
        assertFalse(areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_Arrays_DifferentElements() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(areEqual(arr1, arr2));
    }

    // ... add more test cases for other methods in the Equality class
}