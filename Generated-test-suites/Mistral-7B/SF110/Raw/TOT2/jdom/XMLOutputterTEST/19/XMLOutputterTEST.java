import org.jdom2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        root.addContent(new Element("child1").setText("child1"));
        root.addContent(new Element("child2").setText("child2"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();

        xmlOutputter.output(doc, out);

        String expectedXml = "<root><child1>child1</child1><child2>child2</child2></root>";
        String actualXml = new String(out.toByteArray());

        assertEquals(expectedXml, actualXml);
    }

    // Edge case: empty document
    @Test
    public void testOutputEmptyDocument() {
        Document doc = new Document();
        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();

        xmlOutputter.output(doc, out);

        String expectedXml = "<root/>";
        String actualXml = new String(out.toByteArray());

        assertEquals(expectedXml, actualXml);
    }

    // Error scenario: null document
    @Test(expected = IllegalArgumentException.class)
    public void testOutputNullDocument() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();
        Document doc = null;

        xmlOutputter.output(doc, out);
    }
}