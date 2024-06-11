import static org.junit.Assert.*;

import com.google.common.base.Predicates;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<String> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply("Hello"));
        assertTrue(predicate.apply(null));
        assertTrue(predicate.apply(123));
    }
}