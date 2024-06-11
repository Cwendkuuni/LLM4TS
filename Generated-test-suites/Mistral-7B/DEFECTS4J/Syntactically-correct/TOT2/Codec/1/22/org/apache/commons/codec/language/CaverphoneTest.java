import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

    @Test
    public void testCaverphoneWithEmptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        String expected = "1111111111";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneWithLowerCaseString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("hello");
        String expected = "h2l3o2";
        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneWithStringContainingDigits() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("abc123");
        String expected = "a2b2c";
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneWithNullString() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(null);
    }
}

