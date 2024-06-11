import static org.junit.Assert.*;
import static com.google.common.base.Predicates.*;

public class PredicatesTest {
    // Test for alwaysTrue()
    @Test
    public void testAlwaysTrue() {
        Object obj = new Object();
        assertTrue(alwaysTrue().apply(obj));
        assertNull(alwaysTrue().apply(null));
    }
}