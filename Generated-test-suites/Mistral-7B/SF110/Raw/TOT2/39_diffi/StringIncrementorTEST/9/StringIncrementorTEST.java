import static org.junit.Assert.assertEquals;
import de.beiri22.stringincrementor.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        String a = "abcdefghijklmno";
        String b = "abcdefghijklmnopqrst";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(5, 5, 6, "defghij"));
        assertEquals(expectedResult, StringIncrementor.diff(a, b));
    }
}