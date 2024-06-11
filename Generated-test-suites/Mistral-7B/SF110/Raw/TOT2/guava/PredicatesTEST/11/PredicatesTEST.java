import static org.junit.Assert.*;

import com.google.common.base.Predicates;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTruePredicate = Predicates.alwaysTrue();
        assertTrue(alwaysTruePredicate.apply(null));
        assertTrue(alwaysTruePredicate.apply(new Object()));
        assertTrue(alwaysTruePredicate.apply("test"));
    }
}