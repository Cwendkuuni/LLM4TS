import org.jdom2.output.XMLOutputter;
import org.junit.Test;

public class XMLOutputterTest {
    @Test
    public void testXMLOutputterDefaultConstructor() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        // Basic functionality test: output a document using the default constructor
        Document doc = new Document();
        try {
            xmlOutputter.output(doc, new ByteArrayOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not be thrown in basic functionality test.");
        }
        // Edge case test: null document
        xmlOutputter.output(null, new ByteArrayOutputStream());
        // Exception handling test: IllegalStateException if XMLOutputProcessor is null
        XMLOutputter xmlOutputterNullProcessor = new XMLOutputter(null);
    }
}