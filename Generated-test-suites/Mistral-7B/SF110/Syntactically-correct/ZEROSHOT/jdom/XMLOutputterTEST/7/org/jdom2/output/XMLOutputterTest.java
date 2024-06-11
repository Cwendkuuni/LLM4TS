import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.DefaultXMLProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

public class XMLOutputterTest {

    @Test
    public void testXMLOutputter() {
        XMLOutputter outputter = new XMLOutputter();
        Assert.assertNotNull(outputter);

        XMLOutputter outputterWithFormat = new XMLOutputter(new Format());
        Assert.assertNotNull(outputterWithFormat);

        XMLOutputter outputterWithProcessor = new XMLOutputter(new XMLOutputProcessor());
        Assert.assertNotNull(outputterWithProcessor);

        XMLOutputter outputterWithFormatAndProcessor = new XMLOutputter(new Format(), new XMLOutputProcessor());
        Assert.assertNotNull(outputterWithFormatAndProcessor);
    }

    @Test
    public void testSetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        Format format = new Format();
        outputter.setFormat(format);
        Assert.assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testGetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        Format format = new Format();
        outputter.setFormat(format);
        Assert.assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputProcessor processor = new XMLOutputProcessor();
        outputter.setXMLOutputProcessor(processor);
        Assert.assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testGetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputProcessor processor = new XMLOutputProcessor();
        outputter.setXMLOutputProcessor(processor);
        Assert.assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputDocument() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Document document = new JDOMFactory().createDocument("test", new Element("root"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(document, outputStream);
        String actual = new String(outputStream.toByteArray());
        String expected = DefaultXMLProcessor.escapeElementEntities(document.getRootElement().getName().toString(), XMLOutputter.DEFAULTPROCESSOR.getFormat());
        Assert.assertEquals(expected, actual.substring(0, actual.indexOf(System.lineSeparator())));
    }

    @Test
    public void testOutputDocType() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        DocType doctype = new JDOMFactory().createDocType("html", null, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(doctype, outputStream);
        String actual = new String(outputStream.toByteArray());
        String expected = "<!DOCTYPE html []>\n";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOutputElement() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new JDOMFactory().createElement("test");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(element, outputStream);
        String actual = new String(outputStream.toByteArray());
        String expected = DefaultXMLProcessor.escapeElementEntities(element.getName().toString(), XMLOutputter.DEFAULTPROCESSOR.getFormat());
        Assert.assertEquals(expected, actual.substring(0, actual.indexOf(System.lineSeparator())));
    }

    @Test
    public void testOutputElementContent() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new JDOMFactory().createElement("test");
        element.addContent(new Text("Test content"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.outputElementContent(element, outputStream);
        String actual = new String(outputStream.toByteArray());
        String expected = DefaultXMLProcessor.escapeText(XMLOutputter.DEFAULTPROCESSOR.getFormat().getEscapeStrategy(), XMLOutputter.DEFAULTPROCESSOR.getFormat().getLineSeparator(), "Test content");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOutputList() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Element rootElement = new JDOMFactory().createElement("root");
        rootElement.addContent(new Text("Item 1"));
        rootElement.addContent(new Text("Item 2"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(rootElement.getContent(), outputStream);
        String actual = new String(outputStream.toByteArray());
        String expected = "<text>Item 1</text>\n<text>Item 2</text>\n";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOutputCDATA() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        CDATA cdata = new JDOMFactory().createCDATA("Test CDATA");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(cdata, outputStream);
        String actual = new String(outputStream.toByteArray());
        String expected = "<![CDATA[Test CDATA]]>";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOutputText() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Text text = new Text("Test text");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(text, outputStream);
        String actual = new String(outputStream.toByteArray());
        String expected = DefaultXMLProcessor.escapeText(XMLOutputter.DEFAULTPROCESSOR.getFormat().getEscapeStrategy(), XMLOutputter.DEFAULTPROCESSOR.getFormat().getLineSeparator(), "Test text");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOutputComment() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Comment comment = new JDOMFactory().createComment("Test comment");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(comment, outputStream);
        String actual = new String(outputStream.toByteArray());
        String expected = "<!--Test comment-->";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        ProcessingInstruction pi = new JDOMFactory().createProcessingInstruction("xml-declaration", "version=\"1.0\" encoding=\"UTF-8\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(pi, outputStream);
        String actual = new String(outputStream.toByteArray());
        String expected = "<?xml-declaration version=\"1.0\" encoding=\"UTF-8\"?>\n";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        EntityRef entityRef = new EntityRef("entity1");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputter.output(entityRef, outputStream);
        String actual = new String(outputStream.toByteArray());
        String expected = "&entity1;";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOutputElementContentString() throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new JDOMFactory().createElement("test");
        element.addContent(new Text("Test content"));
        StringWriter stringWriter = new StringWriter();
        outputter.outputElementContent(element, stringWriter);
        String actual = stringWriter.toString();
        String expected = DefaultXMLProcessor.escapeText(XMLOutputter.DEFAULTPROCESSOR.getFormat().getEscapeStrategy(), XMLOutputter.DEFAULTPROCESSOR.getFormat().getLineSeparator(), "Test content").replaceAll(System.lineSeparator(), "");
        Assert.assertEquals(expected, actual);
    }
}