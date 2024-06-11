package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputter() {
        SAXOutputter outputter = new SAXOutputter();
        assertNotNull(outputter);
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterWithContentHandler() {
        ContentHandler contentHandler = new DefaultHandler();
        SAXOutputter outputter = new SAXOutputter(contentHandler);
        assertNotNull(outputter);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterWithContentHandlerErrorHandler() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler);
        assertNotNull(outputter);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterWithContentHandlerErrorHandlerDTDHandler() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler);
        assertNotNull(outputter);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(dtdHandler, outputter.getDTDHandler());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        EntityResolver entityResolver = new DefaultHandler();
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver);
        assertNotNull(outputter);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(dtdHandler, outputter.getDTDHandler());
        assertEquals(entityResolver, outputter.getEntityResolver());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolverLexicalHandler() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        EntityResolver entityResolver = new DefaultHandler();
        LexicalHandler lexicalHandler = new DefaultHandler();
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler);
        assertNotNull(outputter);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(dtdHandler, outputter.getDTDHandler());
        assertEquals(entityResolver, outputter.getEntityResolver());
        assertEquals(lexicalHandler, outputter.getLexicalHandler());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterWithSAXOutputProcessorAndFormat() {
        SAXOutputProcessor processor = new DefaultSAXOutputProcessor();
        Format format = Format.getPrettyFormat();
        SAXOutputter outputter = new SAXOutputter(processor, format);
        assertNotNull(outputter);
        assertEquals(processor, outputter.getSAXOutputProcessor());
        assertEquals(format, outputter.getFormat());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getDefaultProcessor());
        assertEquals(Format.getRawFormat(), outputter.getDefaultFormat());
    }

    @Test
    public void testSAXOutputterSetContentHandler() {
        SAXOutputter outputter = new SAXOutputter();
        ContentHandler contentHandler = new DefaultHandler();
        outputter.setContentHandler(contentHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
    }

    @Test
    public void testSAXOutputterSetErrorHandler() {
        SAXOutputter outputter = new SAXOutputter();
        ErrorHandler errorHandler = new DefaultHandler();
        outputter.setErrorHandler(errorHandler);
        assertEquals(errorHandler, outputter.getErrorHandler());
    }

    @Test
    public void testSAXOutputterSetDTDHandler() {
        SAXOutputter outputter = new SAXOutputter();
        DTDHandler dtdHandler = new DefaultHandler();
        outputter.setDTDHandler(dtdHandler);
        assertEquals(dtdHandler, outputter.getDTDHandler());
    }

    @Test
    public void testSAXOutputterSetEntityResolver() {
        SAXOutputter outputter = new SAXOutputter();
        EntityResolver entityResolver = new DefaultHandler();
        outputter.setEntityResolver(entityResolver);
        assertEquals(entityResolver, outputter.getEntityResolver());
    }

    @Test
    public void testSAXOutputterSetLexicalHandler() {
        SAXOutputter outputter = new SAXOutputter();
        LexicalHandler lexicalHandler = new DefaultHandler();
        outputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, outputter.getLexicalHandler());
    }

    @Test
    public void testSAXOutputterSetDeclHandler() {
        SAXOutputter outputter = new SAXOutputter();
        DeclHandler declHandler = new DefaultHandler();
        outputter.setDeclHandler(declHandler);
        assertEquals(declHandler, outputter.getDeclHandler());
    }

    @Test
    public void testSAXOutputterSetReportNamespaceDeclarations() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testSAXOutputterSetReportDTDEvents() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setReportDTDEvents(true);
        assertTrue(outputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterSetReportDTDEventsThrowsExceptionIfNamespacesNotSupported() {
        SAXOutputter outputter = new SAXOutputter();
        try {
            outputter.setReportDTDEvents(false);
            fail();
        } catch (SAXNotSupportedException e) {
            // expected
        }
    }

    @Test
    public void testSAXOutputterSetFeature() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        assertTrue(outputter.getReportNamespaceDeclarations());

        try {
            outputter.setFeature("http://xml.org/sax/features/validation", false);
            fail();
        } catch (SAXNotRecognizedException e) {
            // expected
        }

        try {
            outputter.setFeature("http://xml.org/sax/features/validation", true);
            outputter.setReportDTDEvents(false);
            fail();
        } catch (SAXNotSupportedException e) {
            // expected
        }
    }

    @Test
    public void testSAXOutputterGetFeature() {
        SAXOutputter outputter = new SAXOutputter();
        assertFalse(outputter.getReportDTDEvents());
        assertFalse(outputter.getReportNamespaceDeclarations());

        outputter.setReportDTDEvents(true);
        assertTrue(outputter.getReportDTDEvents());

        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());

        try {
            outputter.getFeature("http://xml.org/sax/features/validation");
            fail();
        } catch (SAXNotRecognizedException e) {
            // expected
        }

        try {
            outputter.getFeature("http://xml.org/sax/features/validation2");
            fail();
        } catch (SAXNotRecognizedException e) {
            // expected
        }
    }

    @Test
    public void testSAXOutputterSetProperty() {
        SAXOutputter outputter = new SAXOutputter();
        LexicalHandler lexicalHandler = new DefaultHandler();
        outputter.setProperty("http://xml.org/sax/properties/lexical-handler", lexicalHandler);
        assertEquals(lexicalHandler, outputter.getLexicalHandler());

        DeclHandler declHandler = new DefaultHandler();
        outputter.setProperty("http://xml.org/sax/properties/declaration-handler", declHandler);
        assertEquals(declHandler, outputter.getDeclHandler());

        try {
            outputter.setProperty("http://xml.org/sax/properties/someInvalidProperty", "foo");
            fail();
        } catch (SAXNotRecognizedException e) {
            // expected
        }
    }

    @Test
    public void testSAXOutputterGetProperty() {
        SAXOutputter outputter = new SAXOutputter();
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());

        LexicalHandler lexicalHandler = new DefaultHandler();
        outputter.setProperty("http://xml.org/sax/properties/lexical-handler", lexicalHandler);
        assertEquals(lexicalHandler, outputter.getLexicalHandler());

        DeclHandler declHandler = new DefaultHandler();
        outputter.setProperty("http://xml.org/sax/properties/declaration-handler", declHandler);
        assertEquals(declHandler, outputter.getDeclHandler());

        try {
            outputter.getProperty("http://xml.org/sax/properties/someInvalidProperty");
            fail();
        } catch (SAXNotRecognizedException e) {
            // expected
        }
    }

    @Test
    public void testOutputDocument() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(document);
    }

    @Test
    public void testOutputList() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        List<Content> nodes = document.getContent();
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(nodes);
    }

    @Test
    public void testOutputElement() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        Element root = new Element("root");
        document.setRootElement(root);
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(root);
    }

    @Test
    public void testOutputFragmentList() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        List<Content> nodes = document.getContent();
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(nodes);
    }

    @Test
    public void testOutputFragmentText() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        Text text = new Text("foo");
        document.getRootElement().addContent(text);
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(text);
    }

    @Test
    public void testHandleError() {
        SAXOutputter outputter = new SAXOutputter();
        JDOMException exception = new JDOMException("Test exception");
        try {
            outputter.handleError(exception);
            fail();
        } catch (JDOMException e) {
            // expected
        }
    }
}