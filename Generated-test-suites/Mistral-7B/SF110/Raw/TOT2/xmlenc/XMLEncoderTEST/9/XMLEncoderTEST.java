import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import static org.junit.Assert.*;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() throws Exception {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        XMLEncoder encoder = XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() {
        String encoding = "unknown";
        XMLEncoder encoder = null;
        try {
            encoder = XMLEncoder.getEncoder(encoding);
        } catch (IllegalArgumentException e) {
            // Ignore the IllegalArgumentException as expected
        }
        assertNull(encoder);
    }
}