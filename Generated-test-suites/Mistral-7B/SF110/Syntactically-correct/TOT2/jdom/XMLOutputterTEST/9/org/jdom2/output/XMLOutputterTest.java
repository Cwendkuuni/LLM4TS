import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import static org.junit.Assert.assertEquals;

public class XMLOutputterTest {
    @Test
    public void testOutputDocument() {
        Document doc = new JDOMFactory().createDocument("root", null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(doc, new OutputStreamWriter(outputStream));
        String expectedXml = "<?xml version='1.0' encoding='UTF-8'?>\n<root></root>"; // Replace with the expected XML
        assertEquals(expectedXml, outputStream.toString());
    }
}