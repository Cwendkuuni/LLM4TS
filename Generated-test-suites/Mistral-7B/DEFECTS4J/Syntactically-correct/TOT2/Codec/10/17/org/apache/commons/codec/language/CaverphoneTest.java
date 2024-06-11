import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.Caverphone;

public class CaverphoneTest {

    @Test
    public void testCaverphoneNullInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphoneEmptyInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testSimpleInput() {
        Caverphone caverphone = new Caverphone();
        String input = "hello";
        String result = caverphone.caverphone(input);
        String expected = "h3l3o2l3o1";
        assertEquals(expected, result);
    }

    @Test
    public void testCapitalLetters() {
        Caverphone caverphone = new Caverphone();
        String input = "HELLO";
        String result = caverphone.caverphone(input);
        String expected = "h3l3o2l3o1";
        assertEquals(expected, result);
    }

    // Add test cases for edge cases and exception handling scenarios here
}