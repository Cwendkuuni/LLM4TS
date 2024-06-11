import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Predicates;

public class PredicatesTest {
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply("Hello")); // Should not throw an exception
    }
}