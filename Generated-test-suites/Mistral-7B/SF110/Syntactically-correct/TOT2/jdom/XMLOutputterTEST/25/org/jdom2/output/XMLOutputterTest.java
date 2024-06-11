import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class XMLOutputterTest {
    @Test
    public void testOutputDocument() throws Exception {
        Document doc = new Document();
        doc.setRootElement(new Element("root"));
        Element root = doc.getRootElement();
        root.addContent(new Element("child1").setText("Child 1"));
        root.addContent(new Element("child2").setText("Child 2"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc, outStream);

        String xmlString = outStream.toString();
        assertNotNull(xmlString);
        assertEquals("<root><child1>Child 1</child1><child2>Child 2</child2></root>", xmlString);
    }
}