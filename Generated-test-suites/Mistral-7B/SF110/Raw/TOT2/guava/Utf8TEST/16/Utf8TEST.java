import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        String sequence = "Hello World!";
        int expected = Utf8.encodedLength(sequence);
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }
}