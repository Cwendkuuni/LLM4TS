import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import static org.junit.Assert.assertEquals;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
    }
}