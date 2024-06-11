import org.junit.Assert.*;
import de.beiri22.stringincrementor.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

    @Test
    public void testDiff_SameStrings() {
        String a = "hello";
        String b = "hello";

        RelativeString expectedResult = new RelativeString();

        RelativeString result = StringIncrementor.diff(a, b);

        Assert.assertEquals(expectedResult, result);
    }
}