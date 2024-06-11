import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;

public class XMLEncoderTest {
    @Test
    public void testGetTextEncoder() throws Exception {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        encoder.text(writer, "Hello, World!");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nHello, World!", writer.toString());
    }
}