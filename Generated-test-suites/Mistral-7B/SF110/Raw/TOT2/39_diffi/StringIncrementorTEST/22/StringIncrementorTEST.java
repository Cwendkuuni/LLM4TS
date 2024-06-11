import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcd";
        String b = "efghabcd";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(4, 5, 3));
        assertEquals(expected, StringIncrementor.diff(a, b));
    }
}