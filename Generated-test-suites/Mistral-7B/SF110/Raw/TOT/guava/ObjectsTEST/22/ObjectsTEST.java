import org.junit.Test;
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

        // Test with non-null and non-equal objects
        Object obj2 = new Object();
        assertFalse(Objects.equal(obj1, obj2));

        // Test with non-null and equal objects
        Object obj3 = new Object();
        obj1 = obj3;
        assertTrue(Objects.equal(obj1, obj3));
    }
}