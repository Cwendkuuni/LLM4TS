import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatchesSameObject() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testMatchesNullWantedObject() {
        Same sameMatcher = new Same(null);
        Object actual = new Object();

        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesNullActualObject() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = null;

        sameMatcher.matches(actual);
    }

    @Test
    public void testMatchesDifferentClasses() {
        Object wanted = new String("Hello");
        Object actual = new Integer(123);

        assertFalse(new Same(wanted).matches(actual));
    }

    // Test cases for edge cases and exceptions with arrays, collections, and custom classes can be added here.

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();
        Description description = new Description();
        Same sameMatcher = new Same(wanted);

        sameMatcher.describeTo(description);
    }
}