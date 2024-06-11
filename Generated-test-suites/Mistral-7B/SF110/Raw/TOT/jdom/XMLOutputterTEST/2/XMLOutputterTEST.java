import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.OutputFormat;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

public class XMLOutputterTest {

    @Test
    public void testEmptyDocumentOutput() throws IOException, JDOMException {
        Document document = new Document();
        XMLOutputter xmlOutputter = new XMLOutputter();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(document, outputStream);

        String expectedXml = "<document/>";
        String actualXml = outputStream.toString();
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testElementOutput() throws IOException, JDOMException {
        Document document = new Document();
        Element rootElement = new Element("root");
        document.setRootElement(rootElement);

        Element childElement = new Element("child");
        childElement.setText("Child content");
        rootElement.addContent(childElement);

        XMLOutputter xmlOutputter = new XMLOutputter();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(document, outputStream);

        String expectedXml = "<root><child>Child content</child></root>";
        String actualXml = outputStream.toString();
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testFormatSettings() throws IOException, JDOMException {
        Document document = new Document();
        Element rootElement = new Element("root");
        document.setRootElement(rootElement);

        Element childElement = new Element("child");
        childElement.setText("Child content");
        rootElement.addContent(childElement);

        OutputFormat outputFormat = new OutputFormat();
        outputFormat.setOmitDeclaration(true);
        outputFormat.setEncoding("UTF-8");
        outputFormat.setLineSeparator("\r\n");
        outputFormat.setExpandEmptyElements(false);

        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(outputFormat);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(document, outputStream);

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root><child></child></root>";
        String actualXml = outputStream.toString();
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDocumentInput() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();

        xmlOutputter.output(null, new StringWriter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOutputStreamInput() throws IOException {
        Document document = new Document();
        XMLOutputter xmlOutputter = new XMLOutputter();

        xmlOutputter.output(document, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormatInput() {
        Document document = new Document();
        XMLOutputter xmlOutputter = new XMLOutputter();

        xmlOutputter.setFormat(null);
    }
}