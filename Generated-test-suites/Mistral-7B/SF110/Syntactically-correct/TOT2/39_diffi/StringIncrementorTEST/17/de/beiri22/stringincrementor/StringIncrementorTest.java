import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StringIncrementorTest {

    @Test
    public void testDiff_SameStrings() {
        String a = "hello";
        String b = "hello";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
        assertEquals(expected, StringIncrementor.diff(a, b, false));
    }

    @Test
    public void testDiff_IdenticalStringsWithDifferentCase() {
        String a = "hello";
        String b = "HELLO";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
        assertEquals(expected, StringIncrementor.diff(a, b, false));
    }

    // ... Add more test cases for different inputs here ...

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_NullStrings() {
        String a = null;
        String b = "abcdefghijklmnopqrstuvwxyz";
        StringIncrementor.diff(a, b);
        StringIncrementor.diff(a, b, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_NullStringB() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = null;
        StringIncrementor.diff(a, b);
        StringIncrementor.diff(a, b, false);
    }
}