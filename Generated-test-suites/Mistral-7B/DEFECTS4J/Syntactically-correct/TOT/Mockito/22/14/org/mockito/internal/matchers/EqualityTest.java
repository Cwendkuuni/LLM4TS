import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual_nullAndNull() {
        // null and null
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_nullAndNotNull() {
        // null and not null
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_sameObject() {
        // same object
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_differentObjectsWithSameValues() {
        // different objects with same values
        String value = "test";
        String obj1 = new String(value);
        String obj2 = new String(value);
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_differentObjectsWithDifferentValues() {
        // different objects with different values
        String value1 = "test1";
        String value2 = "test2";
        String obj1 = new String(value1);
        String obj2 = new String(value2);
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_arrayOfSameLengthAndSameElements() {
        // array of same length and same elements
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_arrayOfSameLengthAndDifferentElements() {
        // array of same length and different elements
        Object[] array1 = new Object[]{1, 2, 3};
        Object[] array2 = new Object[]{1, 3, 3};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqual_invalidArgument() {
        // invalid argument (non-object)
        Equality.areEqual(1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreArraysEqual_invalidArguments() {
        // invalid arguments (non-arrays)
        Object[] array1 = new Object[]{1, 2, 3};
        String string1 = new String("test");
        assertFalse(Equality.areArraysEqual(array1, string1));
    }
}