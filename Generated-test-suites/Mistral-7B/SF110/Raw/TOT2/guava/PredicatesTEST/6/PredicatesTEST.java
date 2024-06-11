import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(2.5));
        assertTrue(predicate.apply(null));
        assertTrue(predicate.apply(""));
        assertTrue(predicate.apply(new Object()));
    }
}