import org.junit.Test;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testConstructor() {
        Object wanted = new Object();
        Same sameInstance = new Same(wanted);
        assertTrue(sameInstance.matches(wanted));
    }

    // Continue with other test cases below.
}