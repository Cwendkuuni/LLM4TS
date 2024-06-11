import static org.junit.Assert.*;

public class PredicatesTest {
    

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        int value = 42;
        assertTrue(predicate.apply(value));
        assertEquals(value, predicate.apply(value));
        assertNotNull(predicate.apply(null));
        assertFalse(predicate.negate().apply(value));

        // Test with different types
        Predicate<String> stringPredicate = Predicates.alwaysTrue();
        String stringValue = "test";
        assertTrue(stringPredicate.apply(stringValue));
        assertEquals(stringValue, stringPredicate.apply(stringValue));
        assertNotNull(stringPredicate.apply(null));
        assertFalse(stringPredicate.negate().apply(stringValue));
    }

    
}