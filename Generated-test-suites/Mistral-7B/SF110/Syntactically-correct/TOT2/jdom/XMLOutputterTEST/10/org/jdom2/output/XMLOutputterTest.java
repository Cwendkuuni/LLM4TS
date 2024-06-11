import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {
    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        root.addContent(new Element("child1").setText("child1"));
        root.addContent(new Element("child2").setText("child2"));

        XMLOutputter xmlo = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();
        xmlo.output(doc, out);

        String xml = out.toString();
        String expected = "<root><child1>child1</child1><child2>child2</child2></root>";
        assertEquals(expected, xml);
    }

    // Edge case: Empty document
    @Test
    public void testOutputEmptyDocument() throws IOException {
        XMLOutputter xmlo = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();
        Document doc = new Document();
        xmlo.output(doc, out);

        String xml = out.toString();
        String expected = "<document/>";
        assertEquals(expected, xml);
    }

    // Exception handling: Null document
    @Test(expected = NullPointerException.class)
    public void testOutputNullDocument() throws IOException {
        XMLOutputter xmlo = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();
        Document doc = null;
        xmlo.output(doc, out);
    }

    // Exception handling: Null output stream
    @Test(expected = NullPointerException.class)
    public void testOutputNullOutputStream() throws IOException {
        XMLOutputter xmlo = new XMLOutputter();
        Document doc = new Document();
        OutputStream out = null;
        xmlo.output(doc, out);
    }
}