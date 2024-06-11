package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.support.SAXTarget;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class SAXOutputterTest {

    @Test
    public void testOutputDocument() throws JDOMException, SAXException {
        String expectedXML = "<root><element>Hello World</element></root>";
        Document document = new JDOMFactory().createDocument("root", new Element("root"));;
        Element root = document.getRootElement();
        root.addContent(new Element("element").setText("Hello World"));

        SAXOutputter outputter = new SAXOutputter();
        StringWriter writer = new StringWriter();
        outputter.setContentHandler(new DocumentContentHandler(writer));
        outputter.output(document);

        String actualXML = writer.toString();
        Assert.assertEquals(expectedXML, actualXML);
    }

    @Test
    public void testOutputEmptyDocument() throws JDOMException, SAXException {
        Document document = new JDOMFactory().createDocument();

        SAXOutputter outputter = new SAXOutputter();
        StringWriter writer = new StringWriter();
        outputter.setContentHandler(new DocumentContentHandler(writer));
        outputter.output(document);

        String actualXML = writer.toString();
        Assert.assertEquals("", actualXML);
    }

    @Test
    public void testOutputListContent() throws JDOMException, SAXException {
        String expectedXML = "<root><element>Hello</element><element>World</element></root>";
        List<Content> list = new ArrayList<>();
        list.add(new Element("root").setText("root"));
        list.add(new Element("element").setText("Hello"));
        list.add(new Element("element").setText("World"));

        SAXOutputter outputter = new SAXOutputter();
        StringWriter writer = new StringWriter();
        outputter.setContentHandler(new DocumentContentHandler(writer));
        outputter.output(list);

        String actualXML = writer.toString();
        Assert.assertEquals(expectedXML, actualXML);
    }

    @Test
    public void testOutputEntityRef() throws JDOMException, SAXException {
        String expectedXML = "<root>&amp;entity;</root>";
        Document document = new JDOMFactory().createDocument();
        document.addContent(new EntityRef("amp;entity;"));

        SAXOutputter outputter = new SAXOutputter();
        StringWriter writer = new StringWriter();
        outputter.setContentHandler(new DocumentContentHandler(writer));
        outputter.output(document);

        String actualXML = writer.toString();
        Assert.assertEquals(expectedXML, actualXML);
    }

    @Test
    public void testOutputProcessingInstruction() throws JDOMException, SAXException {
        String expectedXML = "<?pi test=\"value\"?>\n<root><element>Hello World</element></root>";
        Document document = new JDOMFactory().createDocument();
        document.addProcessingInstruction("pi", "test", "value");
        Element root = document.getRootElement();
        root.addContent(new Element("element").setText("Hello World"));

        SAXOutputter outputter = new SAXOutputter();
        StringWriter writer = new StringWriter();
        outputter.setContentHandler(new DocumentContentHandler(writer));
        outputter.output(document);

        String actualXML = writer.toString();
        Assert.assertEquals(expectedXML, actualXML);
    }

    @Test(expected = JDOMException.class)
    public void testOutputNullDocument() throws JDOMException, SAXException {
        SAXOutputter outputter = new SAXOutputter();
        StringWriter writer = new StringWriter();
        outputter.setContentHandler(new DocumentContentHandler(writer));
        outputter.output(null);
    }

    @Test(expected = JDOMException.class)
    public void testOutputNullList() throws JDOMException, SAXException {
        SAXOutputter outputter = new SAXOutputter();
        StringWriter writer = new StringWriter();
        outputter.setContentHandler(new DocumentContentHandler(writer));
        outputter.output(null);
    }

    @Test(expected = SAXException.class)
    public void testOutputError() throws JDOMException, SAXException {
        Document document = new JDOMFactory().createDocument();
        document.setErrorHandler(new ErrorHandler() {
            @Override
            public void error(SAXParseException exception) throws SAXException {
                throw new SAXException(exception);
            }
        });
        document.addContent(new Element("root").setText("root"));

        SAXOutputter outputter = new SAXOutputter();
        StringWriter writer = new StringWriter();
        outputter.setContentHandler(new DocumentContentHandler(writer));
        outputter.output(document);
    }

    @Test
    public void testSetReportNamespaceDeclarations() throws JDOMException, SAXException {
        String xmlWithNS = "<root xmlns:ns=\"http://example.com\">\n<ns:element>Hello World</ns:element>\n</root>";
        Document document = new JDOMFactory().createDocument(xmlWithNS);

        SAXOutputter outputter = new SAXOutputter();
        StringWriter writer = new StringWriter();

        // Test with default behavior
        outputter.setContentHandler(new DocumentContentHandler(writer));
        outputter.output(document);
        String actualXML = writer.toString();
        Assert.assertFalse(actualXML.contains("http://example.com"));

        // Test with reportNamespaceDeclarations=true
        outputter.setReportNamespaceDeclarations(true);
        writer = new StringWriter();
        outputter.setContentHandler(new DocumentContentHandler(writer));
        outputter.output(document);
        actualXML = writer.toString();
        Assert.assertTrue(actualXML.contains("http://example.com"));
    }

    @Test
    public void testSetReportDTDEvents() throws JDOMException, SAXException {
        String expectedDTD = "<!DOCTYPE root [ <!ELEMENT root (element)> <!ELEMENT element (#PCDATA)> ]>";
        Document document = new JDOMFactory().createDocument();
        document.setDTD(new DocumentType("root", new Element("root"), new Element("element")));

        SAXOutputter outputter = new SAXOutputter();
        StringWriter writer = new StringWriter();

        // Test with reportDTDEvents=true
        outputter.setReportDTDEvents(true);
        outputter.setContentHandler(new DocumentContentHandler(writer));
        outputter.output(document);
        String actualXML = writer.toString();
        String actualDTD = actualXML.substring(actualXML.indexOf("<?xml") + 7, actualXML.length());
        Assert.assertEquals(expectedDTD, actualDTD);

        // Test with reportDTDEvents=false
        outputter.setReportDTDEvents(false);
        writer = new StringWriter();
        outputter.setContentHandler(new DocumentContentHandler(writer));
        outputter.output(document);
        actualXML = writer.toString();
        Assert.assertFalse(actualXML.contains(expectedDTD));
    }
}

class DocumentContentHandler extends DefaultHandler {
    private final StringWriter writer;

    DocumentContentHandler(StringWriter writer) {
        this.writer = writer;
    }

    @Override
    public void startDocument() throws SAXException {
        this.writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        this.writer.write("<" + qName);
        if (atts != null) {
            for (int i = 0; i < atts.getLength(); i++) {
                this.writer.write(" " + atts.getLocalName(i) + "=\"" + atts.getValue(i) + "\"");
            }
        }
        this.writer.write(">");
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.writer.write(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.writer.write("</" + qName + ">");
    }

    @Override
    public void endDocument() throws SAXException {
        this.writer.write(" ");
    }
}