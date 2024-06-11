import static org.junit.Assert.*;
import com.google.common.base.Predicates;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(new Object()));
        assertTrue(alwaysTrue.apply(new int[10]));
    }
}