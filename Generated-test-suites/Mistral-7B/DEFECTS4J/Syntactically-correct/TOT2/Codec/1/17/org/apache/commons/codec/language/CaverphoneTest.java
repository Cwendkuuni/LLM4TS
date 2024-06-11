
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphoneWithBasicString() {
        String input = "HelloWorld";
        String expected = "H2oWoRlDl2";

        Caverphone caverphone = new Caverphone();

        String result = caverphone.caverphone(input);

        assertEquals(expected, result);
    }

    @Test
    public void testEncodeWithBasicString() {
        String input = "HelloWorld";
        String expected = "H2oWoRlDl2";

        Caverphone caverphone = new Caverphone();

        String result = caverphone.encode(input);

        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNullInput() {
        String input = null;
        String expectedMessage = "Parameter supplied to Caverphone encode is not of type java.lang.String";

        try {
            Caverphone caverphone = new Caverphone();
            caverphone.encode(input);
        } catch (EncoderException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testCaverphoneWithEmptyString() {
        String input = "";
        String expected = "1111111111";

        Caverphone caverphone = new Caverphone();

        String result = caverphone.caverphone(input);

        assertEquals(expected, result);
    }

    @Test
    public void testEncodeWithSpecialCharacters() {
        String input = "Hello@World!";
        String expected = "H2oWoRlDl2@!";

        Caverphone caverphone = new Caverphone();

        String result = caverphone.encode(input);

        assertEquals(expected, result);
    }

    @Test
    public void testCaverphoneWithStringContainingDigits() {
        String input = "Hello123World";
        String expected = "H2oWoRlDl23";

        Caverphone caverphone = new Caverphone();

        String result = caverphone.caverphone(input);

        assertEquals(expected, result);
    }

    @Test
    public void testEncodeWithStringContainingUppercaseLetters() {
        String input = "HELLOWorld";
        String expected = "H2oWoRlDl2";

        Caverphone caverphone = new Caverphone();

        String result = caverphone.encode(input);

        assertEquals(expected, result);
    }

    @Test
    public void testEncodeWithMinimumLengthString() {
        String input = "a";
        String expected = "A";

        Caverphone caverphone = new Caverphone();

        String result = caverphone.encode(input);

        assertEquals(expected, result);
    }

    @Test
    public void testEncodeWithMaximumLengthString() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        String expected = "A2B2C2D2E2F2G2H2I2J2K2L2M2N2O2P2Q2R2S2T2U2V2W2X2Y2Z2";

        Caverphone caverphone = new Caverphone();

        String result = caverphone.encode(input);

        assertEquals(expected, result);
    }
}