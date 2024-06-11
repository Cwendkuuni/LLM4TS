import static org.junit.Assert.*;

public class ObjectsTest {
    // Test start
    @Test
    public void testEqual() {
        // Test with null objects
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));

        // Test with same object
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Objects.equal(obj1, obj2));

        // Test with different objects of the same class
        Object obj3 = new Object();
        assertFalse(Objects.equal(obj1, obj3));

        // Test with different objects of different classes
        String str1 = new String("test");
        assertFalse(Objects.equal(obj1, str1));

        // Test with null and non-null objects
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        assertFalse(Objects.equal(null, "test"));
        assertFalse(Objects.equal("test", null));

        // Test with equal null objects
        assertFalse(Objects.equal(null, null)); // Should not be equal according to the contract, but some JVMs may return true
        // In the following test, we check the contract is respected
        assertFalse(Objects.equal(null, null));
    }
    // Test end
}