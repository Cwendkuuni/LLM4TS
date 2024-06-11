import static org.junit.Assert.*;
import de.beiri22.stringincrementor.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

    @Test
    public void testDiff_sameStrings() {
        String a = "hello";
        String b = "hello";
        RelativeString expected = new RelativeString();
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }

    // ... (Next expert's turn)
}