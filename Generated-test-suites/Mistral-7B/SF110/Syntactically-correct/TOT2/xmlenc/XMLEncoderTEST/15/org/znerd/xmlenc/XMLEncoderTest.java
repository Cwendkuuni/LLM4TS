import org.junit.Test;
import org.junit.Assert.*;

public class XMLEncoderTest {
    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        XMLEncoder encoder = XMLEncoder.getEncoder(null);
        fail("Should have thrown IllegalArgumentException");
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() {
        String encoding = "ABC123";
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
            fail("Should have thrown UnsupportedEncodingException");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedEncodingException);
        }
    }
}