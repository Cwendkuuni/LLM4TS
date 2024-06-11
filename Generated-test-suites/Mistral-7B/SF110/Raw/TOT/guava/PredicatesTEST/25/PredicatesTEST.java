import static org.junit.Assert.*;
import static com.google.common.base.Predicates.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(0));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2.0f));
        assertTrue(alwaysTrue.apply("hello"));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(0));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2.0f));
        assertFalse(alwaysFalse.apply("hello"));
    }

    // Add more test cases for edge cases and error scenarios if necessary
}