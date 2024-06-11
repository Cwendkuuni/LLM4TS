import static org.junit.Assert.*;
import com.google.common.base.Predicates;

public class PredicatesTest {

    @org.junit.Test
    public void testAlwaysTrue() {
        assertTrue(Predicates.alwaysTrue().apply(new Object()));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAlwaysTrueWithNonSerializableClass() {
        Predicates.alwaysTrue(); // Call the method with a non-serializable class
    }

    @org.junit.Test
    public void testAlwaysTrueWithNullObject() {
        assertTrue(Predicates.alwaysTrue().apply(null));
    }

    @org.junit.Test
    public void testAlwaysTrueWithDifferentDataType() {
        assertTrue(Predicates.alwaysTrue().apply(123));
        assertTrue(Predicates.alwaysTrue().apply("Hello World"));
    }
}