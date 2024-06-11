import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Same sameInstance = new Same(wanted);
        assertTrue(sameInstance.matches(wanted));
    }

    @Test
    public void testNotSameInstances() {
        Object wanted = new Object();
        Object other = new Object();
        Same sameInstance = new Same(wanted);
        assertFalse(sameInstance.matches(other));
    }

    @Test
    public void testMatchesString() {
        String wanted = "test";
        Same sameInstance = new Same(wanted);
        String actual = "test";
        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testMatchesCharacter() {
        char wanted = 'a';
        Same sameInstance = new Same(wanted);
        char actual = 'a';
        assertTrue(sameInstance.matches(actual));
    }
}