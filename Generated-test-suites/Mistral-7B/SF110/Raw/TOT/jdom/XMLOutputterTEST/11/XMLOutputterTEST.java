import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        root.addContent(new Element("child1").setText("Child 1"));
        root.addContent(new Element("child2").setText("Child 2"));
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc, outputStream);

        String expectedXml = "<root>\n<child1>Child 1</child1>\n<child2>Child 2</child2>\n</root>";
        String actualXml = new String(outputStream.toByteArray(), "UTF-8");

        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputDocumentWithEmptyElements() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        root.addContent(new Element("child1").setText("Child 1"));
        root.addContent(new Element("child2").setText("Child 2"));
        root.addContent(new Element("emptyChild"));
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.setFormat(new Format().setExpandEmptyElements(true));
        xmlOutputter.output(doc, outputStream);

        String expectedXml = "<root>\n<child1>Child 1</child1>\n<child2>Child 2</child2>\n<emptyChild></emptyChild>\n";
        String actualXml = new String(outputStream.toByteArray(), "UTF-8");

        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputDocumentWithInvalidFormat() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        root.addContent(new Element("child1").setText("Child 1"));
        root.addContent(new Element("child2").setText("Child 2"));
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            xmlOutputter.output(doc, outputStream, new Format().setEncoding("invalidEncoding"));
            Assert.fail("Expected IOException when using invalid encoding.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testOutputDocumentWithNullDocument() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            xmlOutputter.output(null, outputStream);
            Assert.fail("Expected IOException when passing null document.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testOutputStringDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        root.addContent(new Element("child1").setText("Child 1"));
        root.addContent(new Element("child2").setText("Child 2"));
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        StringWriter stringWriter = new StringWriter();
        xmlOutputter.output(doc, stringWriter);

        String expectedXml = "<root>\n<child1>Child 1</child1>\n<child2>Child 2</child2>\n</root>";
        String actualXml = stringWriter.toString();

        Assert.assertEquals(expectedXml, actualXml);
    }
}