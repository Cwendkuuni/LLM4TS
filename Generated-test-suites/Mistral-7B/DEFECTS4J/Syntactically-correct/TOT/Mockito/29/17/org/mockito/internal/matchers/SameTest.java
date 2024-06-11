import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
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
    public void testSameStrings() {
        String wanted = "mockito";
        Same sameMatcher = new Same(wanted);

        String actual = "mockito";
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "mockito";
        Same sameMatcher = new Same(wanted);

        String actual = "junit";
        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalStateException.class)
    public void testNullWanted() {
        Same sameMatcher = new Same(null);
    }

    @Test
    public void testNullActual() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        assertNull(sameMatcher.matches(null));
    }

    @Test
    public void testSerializable() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Mockito.initMocks(this);
        Serializable serialize = Mockito.spy(sameMatcher);

        assertSame(sameMatcher, Mockito.unmock(serialize));
    }
}