import org.junit.Test;
import org.junit.Assert.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        Assert.assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        XMLEncoder encoder = XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() {
        String encoding = "invalidEncoding";
        XMLEncoder encoder = null;
        try {
            encoder = XMLEncoder.getEncoder(encoding);
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            encoder.getEncoding();
            fail("Unexpected result");
        } catch (UnsupportedEncodingException e) {
            // expected
        }
    }
}