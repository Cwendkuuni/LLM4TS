import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphoneWithEmptyString() {
        Caverphone caverphone = new Caverphone();
        String expected = "1111111111";
        String result = caverphone.caverphone("");
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneWithSingleLetter() {
        Caverphone caverphone = new Caverphone();
        String expected = "A";
        String result = caverphone.caverphone("a");
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneWithUpperCaseLetter() {
        Caverphone caverphone = new Caverphone();
        String expected = "A";
        String result = caverphone.caverphone("A");
        assertEquals(expected, result);
    }

    // Add edge cases and exception handling test cases here
}