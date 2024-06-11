import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import static org.junit.Assert.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }
}