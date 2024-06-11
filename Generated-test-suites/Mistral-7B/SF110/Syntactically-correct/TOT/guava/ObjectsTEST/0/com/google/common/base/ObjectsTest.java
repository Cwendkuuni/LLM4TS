import static org.junit.Assert.*;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Test with null objects
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));

        // Test with same objects
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Objects.equal(obj1, obj2));

        // Test with non-null and non-equal objects
        Object obj3 = new Object();
        Object obj4 = new Object();
        assertFalse(Objects.equal(obj3, obj4));

        // Test with objects of different classes
        Object obj5 = new Object();
        String obj6 = "test";
        assertFalse(Objects.equal(obj5, obj6));
    }
}