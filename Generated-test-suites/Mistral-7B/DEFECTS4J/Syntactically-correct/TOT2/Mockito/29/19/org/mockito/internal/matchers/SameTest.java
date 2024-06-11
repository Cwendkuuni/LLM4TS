import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public class SameTest {
    @Test
    public void testSameConstructor() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        assertTrue(same instanceof Same);
        assertEquals(wanted, same.wanted);
    }

    @Test
    public void testMatchesSameObjects() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        Object actual = wanted;
        assertTrue(same.matches(actual));
    }

    @Test
    public void testMatchesDifferentObjects() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        Object actual = new Object();
        assertFalse(same.matches(actual));
    }

    @Test
    public void testDescribeToString() {
        Object wanted = "Hello World";
        Description description = mock(Description.class);
        Same same = new Same(wanted);
        same.describeTo(description);
        String expected = "same(\"Hello World\")";
        assertEquals(expected, description.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testMatchesNullActual() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        Object actual = null;
        same.matches(actual);
    }

    @Test
    public void testMatchesNullWanted() {
        Same same = new Same(null);
        Object actual = new Object();
        assertFalse(same.matches(actual));
    }

    @Test
    public void testMatchesSameClassButDifferentObjects() {
        Object wanted = new Object();
        Object actual = new Object();
        Same same = new Same(wanted.getClass());
        assertFalse(same.matches(actual));
    }
}