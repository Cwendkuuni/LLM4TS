import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class PredicatesTest {
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        // Test with null input
        assertTrue(predicate.apply(null));
        // Test with any valid input
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(2.0));
        assertTrue(predicate.apply("Hello"));
    }
}