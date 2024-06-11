import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XMLOutputterTest {
    @Test
    public void testConstructXMLOutputter() {
        Format format = new Format();
        XMLOutputProcessor processor = new XMLOutputProcessor();
        XMLOutputter outputter = new XMLOutputter(format, processor);
        assertEquals(format, outputter.getFormat());
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }
}