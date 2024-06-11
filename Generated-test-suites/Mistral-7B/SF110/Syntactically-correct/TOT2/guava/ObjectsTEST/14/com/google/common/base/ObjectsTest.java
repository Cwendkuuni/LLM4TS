import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ObjectsTest {
    @Test
    public void testEqual() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        Object obj3 = new Object();

        // Test for null objects
        assertEquals(true, Objects.equal(obj1, obj2));
        assertEquals(false, Objects.equal(obj1, obj3));
        assertEquals(false, Objects.equal(null, obj1));
        assertEquals(false, Objects.equal(obj1, null));

        // Test for non-null objects with same reference
        assertEquals(true, Objects.equal(obj1, obj1));

        // Test for non-null objects with different references and equal values
        String str1 = "test";
        String str2 = "test";
        assertEquals(true, Objects.equal(str1, str2));

        // Test for non-null objects with different references and unequal values
        String str3 = "test";
        String str4 = "test1";
        assertEquals(false, Objects.equal(str3, str4));
    }
}