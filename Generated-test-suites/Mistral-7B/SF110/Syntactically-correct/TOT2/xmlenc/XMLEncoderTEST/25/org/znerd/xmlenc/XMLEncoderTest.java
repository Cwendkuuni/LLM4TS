import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        String actualEncoding = encoder.getEncoding();
        assert (actualEncoding.equals(encoding));
    }
}