import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "hello world";
        String b = "hello there world";
        RelativeString expected = new RelativeString("1 4 del hell o, 7 ins th e, 3 del r e, 3 ins t h e, 5 del w, 4 ins w o r l d");
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }
}