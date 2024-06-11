import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        root.addContent(new Element("child1").setText("Child 1"));
        root.addContent(new Element("child2").setText("Child 2"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();

        xmlOutputter.output(doc, out);

        String expectedXml = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
        String actualXml = new String(out.toByteArray());

        assertEquals(expectedXml, actualXml);
    }
}