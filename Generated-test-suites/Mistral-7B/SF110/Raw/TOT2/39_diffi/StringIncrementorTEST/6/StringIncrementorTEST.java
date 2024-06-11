import org.junit.Test;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "hello world";
        String b = "hello there world";
        RelativeString expectedResult = new RelativeString("insert 5 hello, delete 6 world");
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }
}