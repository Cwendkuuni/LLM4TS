import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        // Test with various input objects
        assertEquals(true, Predicates.alwaysTrue().apply(new Integer(5)));
        assertEquals(true, Predicates.alwaysTrue().apply("Hello"));
        assertEquals(true, Predicates.alwaysTrue().apply(new StringBuffer()));

        // Test with null input
        assertEquals(true, Predicates.alwaysTrue().apply(null));
    }
}