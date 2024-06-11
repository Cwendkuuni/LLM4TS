import org.junit.Test;
import org.junit.Assert.*;
import org.znerd.xmlenc.XMLEncoder;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() {
        XMLEncoder encoder = XMLEncoder.getEncoder(null);
        fail("IllegalArgumentException expected but not thrown.");
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithUnsupportedEncoding() throws IllegalArgumentException {
        String encoding = "InvalidEncoding";
        XMLEncoder encoder = null;
        try {
            encoder = XMLEncoder.getEncoder(encoding);
        } catch (IllegalArgumentException e) {
        }
        assertNull(encoder);
    }
}