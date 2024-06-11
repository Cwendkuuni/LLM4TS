import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        String encodingUTF8 = "UTF-8";
        String encodingUTF16 = "UTF-16";
        String encodingUSASCII = "US-ASCII";
        String encodingCustom = "CustomEncoding";

        XMLEncoder encoderUTF8 = XMLEncoder.getEncoder(encodingUTF8);
        XMLEncoder encoderUTF16 = XMLEncoder.getEncoder(encodingUTF16);
        XMLEncoder encoderUSASCII = XMLEncoder.getEncoder(encodingUSASCII);
        XMLEncoder encoderCustom = null; // exception expected

        assertEquals(encodingUTF8, encoderUTF8.getEncoding());
        assertEquals(encodingUTF16, encoderUTF16.getEncoding());
        assertEquals(encodingUSASCII, encoderUSASCII.getEncoding());

        try {
            encoderCustom = XMLEncoder.getEncoder(encodingCustom);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}