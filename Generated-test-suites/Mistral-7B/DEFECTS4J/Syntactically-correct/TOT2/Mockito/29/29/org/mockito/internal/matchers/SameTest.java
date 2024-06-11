import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameObjects() {
        Object wanted = new Object();
        Same matcher = new Same(wanted);
        Object actual = wanted;
        assertTrue(matcher.matches(actual));
    }
}