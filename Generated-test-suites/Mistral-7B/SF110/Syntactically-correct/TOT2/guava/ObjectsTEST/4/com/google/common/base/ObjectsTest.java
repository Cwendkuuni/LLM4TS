import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal(null, null));
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Objects.equal(obj1, obj2));
        assertFalse(Objects.equal(obj1, new Object()));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[0];
        int hashCode = Objects.hashCode(objects);
        assertEquals(hashCode, Objects.hashCode(objects));
        Objects obj1 = new Object();
        Objects obj2 = new Object();
        assertNotEquals(Objects.hashCode(obj1), Objects.hashCode(obj2));
    }

    // Test cases for toStringHelper methods and firstNonNull method will be added later.
}