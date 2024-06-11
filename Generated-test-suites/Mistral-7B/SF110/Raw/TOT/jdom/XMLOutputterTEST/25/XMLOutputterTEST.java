import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        Document doc = new JDOMFactory().createRootElement("root");
        doc.addContent(new Element("element1").setText("element content"));
        doc.addContent(new Element("element2").setText("another element content"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc, new OutputStreamWriter(outputStream));
        String expectedXml = "<root><element1>element content</element1><element2>another element content</element2></root>";
        String actualXml = outputStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputDocumentWithFormat() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter(new Format().setEncoding("UTF-8").setOmitDeclaration(true));
        Document doc = new JDOMFactory().createRootElement("root");
        doc.addContent(new Element("element1").setText("element content"));
        doc.addContent(new Element("element2").setText("another element content"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc, new OutputStreamWriter(outputStream));
        String expectedXml = "<root><element1>element content</element1><element2>another element content</element2></root>";
        String actualXml = outputStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputDocumentWithProcessor() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter(new XMLOutputProcessor() {
            @Override
            public void process(Writer out, Format format, Content content) throws IOException {
                out.write("<" + content.getName() + ">");
                out.write(content.getTextValue());
                out.write("</" + content.getName() + ">");
            }
        });
        Document doc = new JDOMFactory().createRootElement("root");
        doc.addContent(new Element("element1").setText("element content"));
        doc.addContent(new Element("element2").setText("another element content"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(doc, new OutputStreamWriter(outputStream));
        String expectedXml = "<root><element1>element content</element1><element2>another element content</element2></root>";
        String actualXml = outputStream.toString();
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testOutputDocumentWithNullDocument() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            xmlOutputter.output(null, new OutputStreamWriter(outputStream));
            fail("IOException expected for null document");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testOutputDocumentWithNullOutputStream() {
        XMLOutputter xmlOutputter = new XMLOutputter();

        try {
            xmlOutputter.output(new Document(), null);
            fail("IOException expected for null output stream");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testOutputDocumentWithEmptyList() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        List<Content> contentList = new ArrayList<>();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xmlOutputter.output(contentList, new OutputStreamWriter(outputStream));
        String expectedXml = "<>";
        String actualXml = outputStream.toString();
        assertEquals(expectedXml, actualXml);
    }
}