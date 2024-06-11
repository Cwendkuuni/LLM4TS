import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import static org.junit.Assert.assertTrue;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        Object o1 = new Object();
        Object o2 = new Object();
        assertTrue(Equality.areEqual(o1, o2)); // Both are null

        Object o3 = new String("Hello");
        Object o4 = new String("Hello");
        assertTrue(Equality.areEqual(o3, o4)); // Both are strings

        Object o5 = new int[5];
        Object o6 = new int[5];
        assertTrue(Equality.areEqual(o5, o6)); // Both are arrays of ints

        Object o7 = new int[] {1, 2, 3};
        Object o8 = new int[] {1, 2, 3};
        assertTrue(Equality.areEqual(o7, o8)); // Both arrays have the same content and length

        Object o9 = new Object();
        Object o10 = null;
        assertFalse(Equality.areEqual(o9, o10)); // One is null and the other is not

        Object o11 = null;
        Object o12 = null;
        assertTrue(Equality.areEqual(o11, o12)); // Both are null
    }
}