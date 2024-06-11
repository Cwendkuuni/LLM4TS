import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {
    @Test
    void testAreEqual_Null_Null() {
        // null equals null
        Assertions.assertTrue(Equality.areEqual(null, null));
    }

    @Test
    void testAreEqual_Null_Object() {
        // null does not equal object
        Assertions.assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    void testAreEqual_Object_Null() {
        // object does not equal null
        Assertions.assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    void testAreEqual_Object_Object() {
        Object o1 = new Object();
        Object o2 = new Object();

        // two objects with the same value are equal
        o1 = new Object();
        o2 = new Object();
        o1.setValue(o2.getValue());
        Assertions.assertTrue(Equality.areEqual(o1, o2));

        // two objects with different classes are not equal
        o1 = new String("hello");
        o2 = new Integer(42);
        Assertions.assertFalse(Equality.areEqual(o1, o2));
    }

    @Test
    void testAreArraysEqual_ArraysEqual() {
        Object[] array1 = new Object[]{"one", "two"};
        Object[] array2 = new Object[]{"one", "two"};

        Assertions.assertTrue(Equality.areArraysEqual(array1, array2));
    }

    @Test
    void testAreArraysEqual_ArraysNotEqual_Length() {
        Object[] array1 = new Object[]{"one", "two"};
        Object[] array2 = new Object[]{1, "two"};

        Assertions.assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    void testAreArraysEqual_ArraysNotEqual_Element() {
        Object[] array1 = new Object[]{"one", "two"};
        Object[] array2 = new Object[]{"one", "three"};

        Assertions.assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    void testAreArraysEqual_ArraysNotEqual_NullElement() {
        Object[] array1 = new Object[]{"one", null};
        Object[] array2 = new Object[]{null, "two"};

        Assertions.assertFalse(Equality.areArraysEqual(array1, array2));
    }

    @Test
    void testAreArraysEqual_ArraysNotEqual_DifferentClass() {
        Object[] array1 = new Object[]{new Integer(1), new Integer(2)};
        Object[] array2 = new Object[]{new Double(1.0), new Double(2.0)};

        Assertions.assertFalse(Equality.areArraysEqual(array1, array2));
    }
}