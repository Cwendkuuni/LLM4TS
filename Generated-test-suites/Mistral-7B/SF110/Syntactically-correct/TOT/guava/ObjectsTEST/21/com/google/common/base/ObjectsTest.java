import static org.junit.Assert.*;

public class ObjectsTest {
    // ... (other imports)

    @Test
    public void testEqual() {
        // Test with null objects
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));

        // Test with same object
        Object obj1 = new Object();
        assertTrue(Objects.equal(obj1, obj1));

        // Test with different objects of the same class
        Object obj2 = new Object();
        assertFalse(Objects.equal(obj1, obj2));

        // Test with different objects of different classes
        String str1 = new String("hello");
        String str2 = new String("world");
        assertFalse(Objects.equal(obj1, str1));
        assertFalse(Objects.equal(str1, obj1));
        assertFalse(Objects.equal(obj1, str2));
        assertFalse(Objects.equal(str1, str2));

        // Test with equal objects of different classes
        Number num1 = 5;
        Number num2 = 5;
        assertTrue(Objects.equal(num1, num2));
    }
}