
import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testSameConstructor() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        assertEquals(wanted, same.getWanted());
    }

    @Test
    public void testMatchesSameObjects() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        Object actual = wanted;
        assertTrue(same.matches(actual));
    }

    @Test
    public void testMatchesNullObjects() {
        Object wanted = null;
        Same same = new Same(wanted);
        Object actual = null;
        assertTrue(same.matches(actual));
    }

    @Test
    public void testMatchesDifferentObjects() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        Object actual = new Object();
        assertFalse(same.matches(actual));
    }

    @Test(expected = NullPointerException.class)
    public void testSameConstructorNullWanted() {
        Same same = new Same(null);
        assertNull(same.getWanted());
    }

    @Test(expected = NullPointerException.class)
    public void testMatchesNullWanted() {
        Object wanted = null;
        Same same = new Same(wanted);
        Object actual = new Object();
        assertFalse(same.matches(actual));
    }

    @Test(expected = NullPointerException.class)
    public void testMatchesNullActual() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        assertFalse(same.matches(null));
    }

    @Test(expected = NullPointerException.class)
    public void testMatchesNullArguments() {
        Same same = new Same(null);
        assertFalse(same.matches(null));
    }

    @Test
    public void testMatchesDifferentClasses() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        String actual = new String();
        assertFalse(same.matches(actual));
    }
}