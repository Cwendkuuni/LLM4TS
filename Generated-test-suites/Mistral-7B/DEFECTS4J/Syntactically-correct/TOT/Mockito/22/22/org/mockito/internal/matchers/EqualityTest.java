import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqualNullObjects() {
        // Test null objects
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqualSameObject() {
        // Test same object
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqualObjects() {
        // Test different but equal objects
        String str1 = new String("hello world");
        String str2 = new String("hello world");
        assertTrue(Equality.areEqual(str1, str2));
    }

    @Test
    public void testAreEqualDifferentTypes() {
        // Test different types
        assertFalse(Equality.areEqual(new Object(), "hello world"));
    }

    @Test
    public void testAreEqualArraysSameLength() {
        // Test arrays with same length and equal elements
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqualArraysSameLengthDifferentElements() {
        // Test arrays with same length but different elements
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqualArraysDifferentLengths() {
        // Test arrays with different lengths
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqualNullArray() {
        // Test null array
        Equality.areEqual(null, new Object[]{});
    }

    @Test
    public void testAreEqualArrayElementsNull() {
        // Test array elements as null
        int[] arr1 = {1, null, 3};
        int[] arr2 = {1, 2, 3};
        assertFalse(Equality.areEqual(arr1, arr2));
    }
}