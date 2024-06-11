import org.junit.Test;
import static org.junit.Assert.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
        encoding = null;
        try {
            XMLEncoder encoderNull = XMLEncoder.getEncoder(encoding);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
        encoding = "invalidEncoding";
        try {
            XMLEncoder encoderInvalid = XMLEncoder.getEncoder(encoding);
            fail("UnsupportedEncodingException expected");
        } catch (UnsupportedEncodingException e) {
            // expected
        }
    }
}