import static org.junit.Assert.*;
import static com.google.common.base.Predicates.*;

public class PredicatesTest {
    // Test starts here
    

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(new Object()));
        assertTrue(alwaysTrue.apply(new Integer(1)));
    }

    // Test ends here
    
}