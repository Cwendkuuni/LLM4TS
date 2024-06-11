import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {
    @Test
    public void testEqualNullANull() {
        // Test case for a == b, where both a and b are null
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testEqualNullANotNull() {
        // Test case for a == b, where a is null and b is not
        assertFalse(Objects.equal(null, new Object()));
    }

    @Test
    public void testEqualNotNullANotEqualB() {
        // Test case for a != b, where a and b are not null but not equal
        assertFalse(Objects.equal(new Object(), new OtherObject()));
    }

    @Test
    public void testEqualNotNullAEqualB() {
        // Test case for a == b, where a and b are not null and equal
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Objects.equal(obj1, obj2));
    }
}