import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new JDOMFactory().newDocument();
        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc, outStream);
        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>"; // Replace with valid XML
        assertEquals(expectedXml, outStream.toString());
    }

    // Add edge cases and exception handling tests here
}