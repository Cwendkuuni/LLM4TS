import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;
import java.io.*;
import static org.junit.Assert.assertEquals;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        root.addContent(new Text("Hello World"));

        XMLOutputter xmlo = new XMLOutputter();
        OutputStream out = new ByteArrayOutputStream();
        xmlo.output(doc, out);

        String expected = "<root><Text>Hello World</Text></root>";
        String actual = new String(out.toByteArray());
        assertEquals(expected, actual);
    }
}