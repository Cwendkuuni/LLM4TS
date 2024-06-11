import org.junit.Test;
import org.junit.Assert.*;
import org.znerd.xmlenc.XMLEncoder;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        XMLEncoder encoder = XMLEncoder.getEncoder(null);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() {
        String encoding = "XYZ-123";
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            // Expected
        }
    }
}