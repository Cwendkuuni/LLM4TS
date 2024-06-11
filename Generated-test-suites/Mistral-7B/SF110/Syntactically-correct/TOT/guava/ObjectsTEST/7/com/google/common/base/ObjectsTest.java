import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqualNullObjects() {
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testEqualSameNonNullObjects() {
        String str1 = new String("test");
        String str2 = new String("test");
        assertTrue(Objects.equal(str1, str2));
    }

    @Test
    public void testInequalDifferentNonNullObjects() {
        String str1 = new String("test1");
        String str2 = new String("test2");
        assertFalse(Objects.equal(str1, str2));
    }
}