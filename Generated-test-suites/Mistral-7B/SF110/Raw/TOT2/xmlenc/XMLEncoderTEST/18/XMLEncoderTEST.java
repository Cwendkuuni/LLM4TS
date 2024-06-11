import org.junit.Test;
import org.junit.Assert.*;
import org.znerd.xmlenc.XMLEncoder;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
        assertFalse(encoder._sevenBitEncoding);
    }

    // Add test cases for other valid encodings here
}