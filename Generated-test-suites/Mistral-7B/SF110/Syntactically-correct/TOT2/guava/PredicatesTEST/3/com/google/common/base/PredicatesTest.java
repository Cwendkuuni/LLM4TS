import static org.junit.Assert.*;
import static com.google.common.base.Predicates.*;

public class PredicatesTest {
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = alwaysTrue();
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(2));
        assertTrue(predicate.apply(3));
    }
}