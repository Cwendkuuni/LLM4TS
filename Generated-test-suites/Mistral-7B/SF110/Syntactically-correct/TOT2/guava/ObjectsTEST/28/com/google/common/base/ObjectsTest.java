import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {
    @Test
    public void testEqual() {
        Object a = new Object();
        Object b = a;
        Object c = new Object();
        Object d = null;
        assertFalse(Objects.equal(a, b, d)); // a == b, d is null
        assertFalse(Objects.equal(a, c, d)); // a != c, d is null
        assertTrue(Objects.equal(a, a, d)); // a == a, d is null
        assertFalse(Objects.equal(a, null, d)); // a != null, d is null
        assertTrue(Objects.equal(null, null, d)); // both are null
        assertTrue(Objects.equal(a, a, new Object())); // a == a, d is not null
    }
}