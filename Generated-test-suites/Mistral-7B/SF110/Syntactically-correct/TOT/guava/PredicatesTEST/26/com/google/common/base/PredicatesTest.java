import static org.junit.Assert.*;

public class PredicatesTest {
    

    @Test
    public void testAlwaysTrueWithNull() {
        Predicate<Object> predicate = Predicates.alwaysTrue();
        assertFalse(predicate.apply(null)); // False because apply should return true for alwaysTrue predicate
    }

    @Test
    public void testAlwaysTrueWithObject() {
        Predicate<Object> predicate = Predicates.alwaysTrue();
        Object anyObject = new Object();
        assertTrue(predicate.apply(anyObject));
    }

    @Test
    public void testAlwaysTrueWithDifferentTypes() {
        Predicate<Object> predicate = Predicates.alwaysTrue();
        Integer number = 10;
        String string = "Example";
        assertTrue(predicate.apply(number));
        assertTrue(predicate.apply(string));
    }

    
}