import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyzabc";
        RelativeString expectedResult = new RelativeString();
        StringLink link1 = new StringLink(1, 0, 6);
        StringLink link2 = new StringLink(13, 12, 6);
        expectedResult.addLink(link1);
        expectedResult.addLink(link2);
        assertEquals(expectedResult, StringIncrementor.diff(a, b));
    }
}