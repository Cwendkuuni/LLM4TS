import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Test;

public class XMLOutputterTest {
    @Test
    public void testOutputDocument() throws Exception {
        XMLOutputter xmlOutputter = new XMLOutputter();
        Document doc = new JDomFactory().createDocument();
        OutputStream out = new ByteArrayOutputStream();
        xmlOutputter.output(doc, out);
        String expectedXml = "<?xml version='1.0' encoding='UTF-8'?>\n<root/>"; // Replace with actual expected XML
        String actualXml = out.toString();
        Assert.assertEquals(expectedXml, actualXml);
    }
}