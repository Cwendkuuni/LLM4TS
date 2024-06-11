import static org.junit.Assert.*;
import com.google.common.base.Utf8;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        String testString = "test string";
        int expectedLength = Utf8.encodedLength(testString);
        int actualLength = Utf8.encodedLength(testString);
        assertEquals(expectedLength, actualLength);
    }
}