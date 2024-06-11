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
        Text text = new Text("Hello World");
        root.addContent(text);

        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(doc, new ByteArrayOutputStream());

        String xmlString = xmlOutputter.outputString(doc);
        assertEquals("<root><Text>Hello World</Text></root>", xmlString);
    }
}