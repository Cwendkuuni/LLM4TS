import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new JDOMParser().parse(getClass().getResource("/sample.xml"));
        OutputStream out = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(doc, out);
        String expectedXml = "<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<element1>Content 1</element1>\n<element2>\n<childElement>Child Content</childElement>\n</element2>\n</root>";
        String actualXml = new String(out.toByteArray());
        assertEquals(expectedXml, actualXml);
    }
}