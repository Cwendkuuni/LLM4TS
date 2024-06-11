import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.DOMOutputter;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws Exception {
        Document doc = new JDOMFormatter().format(new DOMOutputter().output(new Document("root")));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(doc, outStream);
        String expectedXML = "<root/>"; // This should be replaced with the expected XML based on the input document
        String actualXML = outStream.toString();
        assertEquals(expectedXML, actualXML);
    }
}