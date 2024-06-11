import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document document = new Document();
        Element root = new Element("root");
        root.addContent(new Element("child1").setText("Hello"));
        root.addContent(new Element("child2").setText("World"));
        document.setRootElement(root);

        XMLOutputter outputter = new XMLOutputter();
        String expectedXml = "<root><child1>Hello</child1><child2>World</child2></root>";
        StringWriter writer = new StringWriter();
        outputter.output(document, writer);

        assertEquals(expectedXml, writer.toString());
    }
}