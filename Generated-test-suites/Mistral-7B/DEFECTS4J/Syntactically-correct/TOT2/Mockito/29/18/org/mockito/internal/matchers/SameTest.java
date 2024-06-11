import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testSameObjects() {
        Same sameMatcher = new Same(new Object());

        Object sameObject = new Object();
        assertTrue(sameMatcher.matches(sameObject));
    }

    @Test
    public void testNullObjects() {
        Same sameMatcher = new Same(null);

        Object nonNullObject = new Object();
        assertFalse(sameMatcher.matches(nonNullObject));

        Same nullMatcher = new Same(null);
        assertTrue(nullMatcher.matches(null));
    }

    @Test
    public void testDifferentClasses() {
        Same sameMatcher = new Same(new Object());

        String stringObject = new String("Test");
        assertFalse(sameMatcher.matches(stringObject));

        Character charObject = 'A';
        assertFalse(sameMatcher.matches(charObject));
    }
}