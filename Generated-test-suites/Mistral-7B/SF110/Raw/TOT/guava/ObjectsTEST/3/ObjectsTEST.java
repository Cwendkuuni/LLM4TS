import static org.junit.Assert.*;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Test with null objects
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));

        // Test with same object
        Object obj1 = new Object();
        assertTrue(Objects.equal(obj1, obj1));

        // Test with same value objects
        String str1 = "test";
        String str2 = "test";
        assertTrue(Objects.equal(str1, str2));

        // Test with different objects
        String str1_1 = "test1";
        String str2_1 = "test2";
        assertFalse(Objects.equal(str1_1, str2_1));

        // Test with different types
        int i = 1;
        double d = 1.0;
        assertFalse(Objects.equal(i, d));
    }
}