package org.jdom2.output;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.support.SAXTarget;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SAXOutputterTest {

    @Test
    public void testEmptyDocumentOutput() throws JDOMException, SAXException {
        final Document emptyDocument = new Document();
        final SAXOutputter outputter = new SAXOutputter();
        outputter.output(emptyDocument);

        final SAXTarget target = outputter.getSAXOutputProcessor().getOutputHandler();
        assertNotNull(target.getContentHandler());
        assertNotNull(target.getErrorHandler());
        assertNotNull(target.getDTDHandler());
        assertNotNull(target.getEntityResolver());
        assertNotNull(target.getLexicalHandler());
        assertNotNull(target.getDeclHandler());

        // Empty document, no content to check
    }

    @Test
    public void testSimpleElementOutput() throws JDOMException, SAXException {
        final Document document = new Document(new Element("root"));
        document.getRootElement().addContent(new Element("child").setText("Hello World"));

        final SAXOutputter outputter = new SAXOutputter();
        outputter.output(document);

        final SAXTarget target = outputter.getSAXOutputProcessor().getOutputHandler();
        assertNotNull(target.getContentHandler());
        assertNotNull(target.getErrorHandler());
        assertNotNull(target.getDTDHandler());
        assertNotNull(target.getEntityResolver());
        assertNotNull(target.getLexicalHandler());
        assertNotNull(target.getDeclHandler());

        // Check root and child elements are processed correctly
        assertEquals("<root><child>Hello World</child></root>", target.getContentHandler().getDocument().getRootElement().getText());
    }

    @Test(expected = SAXException.class)
    public void testErrorHandling() throws JDOMException, SAXException {
        final Document document = new Document();
        document.getRootElement().addContent(new Element("root").setText("<?xml version='1.0'?><error>Invalid XML</error>"));

        final SAXOutputter outputter = new SAXOutputter();
        outputter.setErrorHandler((errorHandler) -> { throw new SAXException("Test error"); });
        outputter.output(document);

        // Error handler should be called, but test fails due to expected exception
    }

    @Test
    public void testDTDEventHandling() throws JDOMException, SAXException {
        final String publicID = "test.dtd";
        final String systemID = "file:test.dtd";
        final Document document = new Document();
        document.setDocType(new DefaultDocType("test", publicID, systemID));

        final SAXOutputter outputter = new SAXOutputter();
        outputter.setReportDTDEvents(true);
        outputter.output(document);

        final SAXTarget target = outputter.getSAXOutputProcessor().getOutputHandler();
        assertNotNull(target.getDTDHandler());

        // Check DTD events are processed correctly
        assertEquals(publicID, target.getDTDHandler().getPublicId());
        assertEquals(systemID, target.getDTDHandler().getSystemId());
    }
}