import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object actual = wanted;
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object actual = new Object();
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testNullAndSameObject() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object actual = wanted;
        assertNull(sameMatcher.matches(null));
        assertTrue(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWantedObject() {
        Same sameMatcher = new Same(null);
        throw new IllegalArgumentException("wanted argument cannot be null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSerializableWantedObject() {
        Object wanted = new NonSerializableObject();
        Same sameMatcher = new Same(wanted);
        throw new IllegalArgumentException("wanted argument must be serializable");
    }
}

class NonSerializableObject implements Serializable {
    // This class is not serializable for testing error scenarios
}