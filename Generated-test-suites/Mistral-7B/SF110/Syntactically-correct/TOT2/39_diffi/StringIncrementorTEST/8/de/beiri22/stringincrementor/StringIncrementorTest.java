import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "HelloWorld";
        String b = "HelloHelloWorld";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNotNull(result);
    }
}