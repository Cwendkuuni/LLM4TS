import static org.junit.Assert.assertTrue;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        // Test with null object
        assertTrue(Predicates.alwaysTrue() != null);
        assertTrue(Predicates.alwaysTrue().apply(null));

        // Test with various object types
        Object obj1 = new Object();
        assertTrue(Predicates.alwaysTrue().apply(obj1));

        String str = "test";
        assertTrue(Predicates.alwaysTrue().apply(str));

        int num = 42;
        assertTrue(Predicates.alwaysTrue().apply(num));

        // Test with arrays
        Object[] objArray = new Object[5];
        assertTrue(Predicates.alwaysTrue().apply(objArray));

        // Test with custom classes
        MyCustomClass customClass = new MyCustomClass();
        assertTrue(Predicates.alwaysTrue().apply(customClass));
    }
}