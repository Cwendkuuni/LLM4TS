
import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual_NullNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_SameInstance() {
        Object o1 = new Object();
        assertTrue(Equality.areEqual(o1, o1));
    }

    @Test
    public void testAreEqual_PrimitiveTypes() {
        byte b1 = 1;
        byte b2 = 1;
        short s1 = 1;
        short s2 = 1;
        int i1 = 1;
        int i2 = 1;
        long l1 = 1L;
        long l2 = 1L;
        float f1 = 1.0f;
        float f2 = 1.0f;
        double d1 = 1.0;
        double d2 = 1.0;

        // byte
        assertTrue(Equality.areEqual(b1, b2));

        // short
        assertTrue(Equality.areEqual(s1, s2));

        // int
        assertTrue(Equality.areEqual(i1, i2));

        // long
        assertTrue(Equality.areEqual(l1, l2));

        // float
        assertTrue(Equality.areEqual(f1, f2));

        // double
        assertTrue(Equality.areEqual(d1, d2));
    }

    @Test
    public void testAreEqual_DifferentInstances_SameValue() {
        String s1 = "Hello World";
        String s2 = new String("Hello World");
        assertTrue(Equality.areEqual(s1, s2));
    }

    @Test
    public void testAreArraysEqual_EqualLengthAndEqualElements() {
        Object[] o1 = {1, 2, 3};
        Object[] o2 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqual_EqualLengthButDifferentElements() {
        Object[] o1 = {1, 2, 3};
        Object[] o2 = {1, 3, 3};
        assertFalse(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqual_DifferentLength() {
        Object[] o1 = {1, 2, 3};
        Object[] o2 = {1, 2};
        assertFalse(Equality.areArraysEqual(o1, o2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqual_NullArray() {
        Object[] o1 = null;
        Object[] o2 = new Object[1];
        Equality.areArraysEqual(o1, o2);
    }

    @Test
    public void testAreArraysEqual_ArraysWithDifferentTypes() {
        Object[] o1 = {1, 2, 3};
        String[] o2 = {"1", "2", "3"};
        assertFalse(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqual_EmptyArrays() {
        Object[] o1 = new Object[0];
        Object[] o2 = new Object[0];
        assertTrue(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqual_Complex() {
        Object[] o1 = {"1", new Object(), new Object[]{1, 2}, 1.0};
        Object[] o2 = {"1", new Object(), new Object[]{1, 2}, 1.0};
        assertTrue(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqual_WrongTypes() {
        Object[] o1 = {1, 2, 3};
        String[] o2 = {"1", "2", "3"};
        assertFalse(Equality.areArraysEqual(o1, o2));
    }
}
