import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import java.io.*;
import static org.junit.Assert.assertEquals;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        root.addContent(new Text("Hello, World!"));
        doc.setRootElement(root);

        XMLOutputter xmlo = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();
        xmlo.output(doc, out);
        String expectedXml = "<root><Text>Hello, World!</Text></root>";
        String actualXml = new String(out.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(expectedXml, actualXml);
    }
}