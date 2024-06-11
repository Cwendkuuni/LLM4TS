import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XMLEncoderTest {
    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() {
        String encoding = null;
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() throws IllegalArgumentException {
        String encoding = "InvalidEncoding";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
    }
}