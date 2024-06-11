import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_SameObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1; // same object
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_DifferentObjectsButEqual() {
        Object obj1 = new Object();
        Object obj2 = new Object(); // different objects but equal
        obj1 = obj2;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_NullAndNull() {
        assertFalse(Equality.areEqual(null, null)); // false according to the class contract
    }

    @Test
    public void testAreEqual_NullAndNonNull() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_NonNullAndNull() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_DifferentTypes() {
        Integer intObj1 = 1;
        String strObj2 = "test";
        assertFalse(Equality.areEqual(intObj1, strObj2));
    }

    @Test
    public void testAreEqual_Arrays_SameLength_EqualElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_Arrays_SameLength_DifferentElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_Arrays_DifferentLengths() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_Arrays_SameReference() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = arr1;
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_Arrays_SameLength_SameElements_DifferentClasses() {
        Object[] arr1 = new Object[]{new Integer(1), new Double(2.0)};
        Object[] arr2 = new Object[]{new Integer(1), new Double(2.0)};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_Arrays_SameLength_SameElements_SameClass() {
        Object[] arr1 = new Object[]{1, 2};
        Object[] arr2 = new Object[]{1, 2};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_Arrays_SameLength_DifferentElements_SameClass() {
        Object[] arr1 = new Object[]{1, 2};
        Object[] arr2 = new Object[]{2, 1};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_Arrays_SameReference() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = arr1;
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_Arrays_SameValuesButDifferentReferences() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_Arrays_DifferentTypes() {
        int[] intArr1 = new int[]{1, 2, 3};
        String[] strArr2 = new String[]{"1", "2", "3"};
        assertFalse(Equality.areArraysEqual(intArr1, strArr2));
    }

    @Test
    public void testAreArraysEqual_Arrays_SameLength_SameElements_MixedTypes() {
        Object[] arr1 = new Object[]{1, new Double(2.0), "3"};
        Object[] arr2 = new Object[]{1, new Double(2.0), "3"};
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_Arrays_SameLength_DifferentElements_SameClass() {
        Object[] arr1 = new Object[]{1, 2};
        Object[] arr2 = new Object[]{2, 1};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_Arrays_SameLength_SameElements_SameClass_Primitive() {
        int[] intArr1 = {1, 2, 3};
        int[] intArr2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(intArr1, intArr2));
    }

    @Test
    public void testAreArraysEqual_Arrays_SameLength_DifferentElements_DifferentClasses() {
        Object[] arr1 = new Object[]{1, new Integer(2)};
        Object[] arr2 = new Object[]{1, new Double(2.0)};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public void testAreArraysEqual_Arrays_DifferentLengths() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreArraysEqual_ArrayWithNullElement() {
        Object[] arr1 = new Object[]{1, null, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        try {
            Equality.areArraysEqual(arr1, arr2);
        } catch (IllegalArgumentException e) {
            assertEquals("Array contains a null element", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreArraysEqual_ArrayWithDifferentLengthsAndNullElement() {
        Object[] arr1 = new Object[]{1, null, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        try {
            Equality.areArraysEqual(arr1, arr2);
        } catch (IllegalArgumentException e) {
            assertEquals("Arrays have different length", e.getMessage());
        }
    }
}