import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ObjectsTest {
    @Test
    public void testEqualNullNull() {
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testEqualNullObject() {
        assertFalse(Objects.equal(null, "test"));
    }

    @Test
    public void testEqualSameObject() {
        Integer num = new Integer(10);
        assertTrue(Objects.equal(num, num));
    }

    @Test
    public void testEqualDifferentObjects() {
        Integer num1 = new Integer(10);
        Integer num2 = new Integer(20);
        assertFalse(Objects.equal(num1, num2));
    }

    @Test
    public void testEqualSameString() {
        String str = "test";
        assertTrue(Objects.equal(str, str));
    }

    @Test
    public void testEqualDifferentStrings() {
        String str1 = "test1";
        String str2 = "test2";
        assertFalse(Objects.equal(str1, str2));
    }
}