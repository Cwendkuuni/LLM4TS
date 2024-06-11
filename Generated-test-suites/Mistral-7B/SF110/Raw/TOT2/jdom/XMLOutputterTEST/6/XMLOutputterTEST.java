import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.*;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws Exception {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        root.addContent(new Text("Hello World"));

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        XMLOutputter xmlOut = new XMLOutputter();
        xmlOut.output(doc, outStream);
        String expectedXml = "<root><Text>Hello World</Text></root>";
        assertEquals(expectedXml, outStream.toString());
    }
}