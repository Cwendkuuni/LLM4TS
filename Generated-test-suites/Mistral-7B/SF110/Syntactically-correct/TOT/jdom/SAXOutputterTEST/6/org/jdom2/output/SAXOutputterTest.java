package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.SAXOutputter;
import org.xml.sax.SAXException;

import java.io.StringWriter;
import java.util.Collections;

import static org.junit.Assert.*;

public class SAXOutputterTest {

    @org.junit.Test
    public void testEmptyDocument() throws SAXException, JDOMException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document("");

        StringWriter stringWriter = new StringWriter();
        saxOutputter.setContentHandler(new DocumentContentHandler(stringWriter));
        saxOutputter.output(document);

        assertEquals("", stringWriter.toString());
    }

    @org.junit.Test
    public void testSimpleElement() throws SAXException, JDOMException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Element root = new Element("root");
        root.addContent(new Text("Hello World"));

        StringWriter stringWriter = new StringWriter();
        saxOutputter.setContentHandler(new DocumentContentHandler(stringWriter));
        saxOutputter.output(root);

        assertEquals("<root>Hello World</root>", stringWriter.toString());
    }

    @org.junit.Test
    public void testAttributes() throws SAXException, JDOMException {
        SAXOutputter saxOutputter = new SAXOutputter();

        Element root = new Element("root");
        root.setAttribute("attr1", "value1");
        root.setAttribute("attr2", "value2");

        StringWriter stringWriter = new StringWriter();
        saxOutputter.setContentHandler(new DocumentContentHandler(stringWriter));
        saxOutputter.output(root);

        assertEquals("<root attr1=\"value1\" attr2=\"value2\"></root>", stringWriter.toString());
    }

    @org.junit.Test
    public void testMixedContent() throws SAXException, JDOMException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Element root = new Element("root");
        root.addContent(new Text("Text1"));
        root.addContent(new Element("child"));
        root.addContent(new Text("Text2"));

        StringWriter stringWriter = new StringWriter();
        saxOutputter.setContentHandler(new DocumentContentHandler(stringWriter));
        saxOutputter.output(root);

        assertEquals("<root>Text1<child></child>Text2</root>", stringWriter.toString());
    }

    @org.junit.Test
    public void testXmlDeclarations() throws SAXException, JDOMException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document(Collections.singletonMap("xml-decl:version", "1.0"), new Element("root"));

        StringWriter stringWriter = new StringWriter();
        saxOutputter.setContentHandler(new DocumentContentHandler(stringWriter));
        saxOutputter.output(document);

        assertEquals("<?xml version=\"1.0\"?>\n<root></root>", stringWriter.toString());
    }

    @org.junit.Test
    public void testDTDEvents() throws SAXException, JDOMException {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document(new Element("!DOCTYPE root [ <!ATTLIST attr1 CDATA #REQUIRED> ]"));
        Element root = new Element("root");
        root.setAttribute("attr1", "value");

        StringWriter stringWriter = new StringWriter();
        saxOutputter.setContentHandler(new DocumentContentHandler(stringWriter));
        saxOutputter.setReportDTDEvents(true);
        saxOutputter.output(document);

        assertEquals("<!DOCTYPE root [ <!ATTLIST attr1 CDATA #REQUIRED> ]>\n<root attr1=\"value\"></root>", stringWriter.toString());
    }

    @org.junit.Test
    public void testErrorHandling() throws SAXException, JDOMException {
        SAXOutputter saxOutputter = new SAXOutputter();
        StringWriter stringWriter = new StringWriter();
        saxOutputter.setErrorHandler(new ErrorHandler() {
            @Override
            public void error(SAXParseException exception) throws SAXException {
                assertEquals("Invalid element content: " + exception.getException(), exception.getMessage());
            }
        });
        Element root = new Element("root", null, new Element("child", null, new Element("grandchild", null, new Text("XSS Attack"))));

        try {
            saxOutputter.output(root);
            fail();
        } catch (JDOMException e) {
            // Expected
        }

        assertEquals("", stringWriter.toString());
    }
}