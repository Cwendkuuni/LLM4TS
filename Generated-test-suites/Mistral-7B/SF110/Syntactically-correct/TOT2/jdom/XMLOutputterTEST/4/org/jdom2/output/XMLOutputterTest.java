import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {
    @Test
    public void testOutputDocument() throws IOException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        root.addContent(new Element("child", "value"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();
        xmlOutputter.output(document, out);
        String expectedXml = "<root><child>value</child></root>";
        String actualXml = new String(out.toByteArray());
        assertEquals(expectedXml, actualXml);
    }
}