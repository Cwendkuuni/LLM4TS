import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Equality.areEqual(obj1, obj2));

        Object obj3 = new Object();
        assertFalse(Equality.areEqual(obj1, obj3));

        Object obj4 = null;
        assertFalse(Equality.areEqual(obj1, obj4));
        assertFalse(Equality.areEqual(obj4, obj1));
    }
}