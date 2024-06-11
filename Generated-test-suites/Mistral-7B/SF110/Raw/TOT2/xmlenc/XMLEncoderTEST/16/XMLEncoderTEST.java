import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() {
        String encoding = "ABC123";
        XMLEncoder encoder = null;
        try {
            encoder = XMLEncoder.getEncoder(encoding);
        } catch (IllegalArgumentException e) {
        }
        encoder = XMLEncoder.getEncoder(encoding);
    }
}