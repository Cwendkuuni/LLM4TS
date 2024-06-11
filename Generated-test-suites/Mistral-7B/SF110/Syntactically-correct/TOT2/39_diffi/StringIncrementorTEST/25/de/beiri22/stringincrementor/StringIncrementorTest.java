import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcdefghijklmnopqrst";
        String b = "abcdefghijklmnopqrstuvw";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(7, 7, 6, "uvw"));
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }
}