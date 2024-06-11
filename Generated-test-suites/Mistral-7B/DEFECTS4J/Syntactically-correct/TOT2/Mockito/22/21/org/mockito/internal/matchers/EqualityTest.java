import org.junit.Test;
import org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqualWithEqualObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqualWithNullObjects() {
        assertFalse(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
        assertFalse(Equality.areEqual(null, new int[0]));
        assertFalse(Equality.areEqual(new int[0], null));
    }

    @Test
    public void testAreEqualWithArrayEqualObjects() {
        Object[] arr1 = new Object[] {new Object(), new Object()};
        Object[] arr2 = new Object[] {new Object(), new Object()};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqualWithArraysDifferentLengths() {
        Object[] arr1 = new Object[] {new Object(), new Object()};
        Object[] arr2 = new Object[] {new Object(), new Object(), new Object()};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqualWithNullArray() {
        Object[] arrNull = null;
        Object[] arr1 = new Object[] {new Object()};
        try {
            Equality.areEqual(arrNull, arr1);
        } catch (IllegalArgumentException e) {
            assertEquals("Arrays or at least one of the objects are null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqualWithObjectsOfDifferentTypes() {
        Object obj1 = new Object();
        int intVal = 1;
        try {
            Equality.areEqual(obj1, intVal);
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected type", e.getMessage());
        }
    }
}