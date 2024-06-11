import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        CharSequence sequence = "abcdef";
        int expectedResult = 6;
        int actualResult = Utf8.encodedLength(sequence);
        assertEquals(expectedResult, actualResult);
    }
}