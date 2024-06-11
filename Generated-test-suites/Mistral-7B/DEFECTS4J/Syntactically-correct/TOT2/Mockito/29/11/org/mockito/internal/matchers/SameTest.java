import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Same;

public class SameTest {
    @Test
    public void testMatchesSameObject() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;

        boolean result = sameMatcher.matches(actual);
        assertTrue(result);
    }
}