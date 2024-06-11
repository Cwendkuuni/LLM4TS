import static org.junit.Assert.*;
import com.google.common.base.Objects;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Test with null objects
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));

        // Test with same object
        assertTrue(Objects.equal(new Object(), new Object()));

        // Test with equal non-null objects
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2));
    }
}