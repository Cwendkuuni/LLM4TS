import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {
    @Test
    public void testAreEqual_SameObjects() {
        Object o1 = new Object();
        Object o2 = o1;
        assertTrue(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_DifferentObjectsButEqual() {
        Object o1 = new Object();
        Object o2 = new Object();
        o1 = o2;
        assertTrue(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqual_NullAndNull() {
        assertNull(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_NullAndObject() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_ObjectAndNull() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_ObjectAndDifferentObjects() {
        Object o1 = new Object();
        Object o2 = new Object();
        assertFalse(Equality.areEqual(o1, o2));
    }
}