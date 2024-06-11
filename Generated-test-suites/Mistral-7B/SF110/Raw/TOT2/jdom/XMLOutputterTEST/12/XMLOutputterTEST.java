import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        root.addContent(new Element("child1").setText("Test"));
        root.addContent(new Element("child2").setText("Test2"));
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();
        xmlOutputter.output(doc, out);

        String expectedXml = "<root>\n<child1>Test</child1>\n<child2>Test2</child2>\n</root>";
        String actualXml = new String(out.toByteArray());
        assertEquals(expectedXml, actualXml);
    }
}