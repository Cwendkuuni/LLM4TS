import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.assertEquals;

public class XMLOutputterTest {
    @Test
    public void testOutputDocument() throws Exception {
        Document doc = new Document();
        Element root = new Element("root");
        Document docWithRoot = new Document(root);
        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(docWithRoot, outputStream);
        String expectedXml = "<root/>";
        String actualXml = outputStream.toString();
        assertEquals(expectedXml, actualXml);
    }
}