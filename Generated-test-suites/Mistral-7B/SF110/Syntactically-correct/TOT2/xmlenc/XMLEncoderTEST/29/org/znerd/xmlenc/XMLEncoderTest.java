import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import static org.junit.Assert.assertInstanceOf;

public class XMLEncoderTest {
    @Test
    public void testGetEncoder() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertInstanceOf(XMLEncoder.class, encoder);
    }
}