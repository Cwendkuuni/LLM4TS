package org.jdom2.output; // adjust the package if needed

import org.jdom2.*;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SAXOutputterTest {
    // Test constructor with no arguments
    @Test
    public void testDefaultSAXOutputterConstruction() {
        final SAXOutputter saxOutputter = new SAXOutputter();
        assertNotNull(saxOutputter);
    }

    // Test constructor with ContentHandler argument
    @Test
    public void testSAXOutputterContentHandlerConstruction() {
        final ContentHandler contentHandler = mock(ContentHandler.class);
        final SAXOutputter saxOutputter = new SAXOutputter(contentHandler);
        assertNotNull(saxOutputter);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
    }

    // Test constructor with ContentHandler, ErrorHandler, DTDHandler and EntityResolver arguments
    @Test
    public void testSAXOutputterMultipleHandlerConstruction() {
        final ContentHandler contentHandler = mock(ContentHandler.class);
        final ErrorHandler errorHandler = mock(ErrorHandler.class);
        final DTDHandler dtdHandler = mock(DTDHandler.class);
        final EntityResolver entityResolver = mock(EntityResolver.class);

        final SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver);
        assertNotNull(saxOutputter);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        assertEquals(entityResolver, saxOutputter.getEntityResolver());
    }

    // Test constructor with SAXOutputProcessor, Format, ContentHandler, ErrorHandler, DTDHandler and EntityResolver arguments
    @Test
    public void testSAXOutputterCustomConstruction() {
        final ContentHandler contentHandler = mock(ContentHandler.class);
        final ErrorHandler errorHandler = mock(ErrorHandler.class);
        final DTDHandler dtdHandler = mock(DTDHandler.class);
        final EntityResolver entityResolver = mock(EntityResolver.class);
        final SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        final Format format = new DefaultFormat();

        final SAXOutputter saxOutputter = new SAXOutputter(processor, format, contentHandler, errorHandler, dtdHandler, entityResolver);
        assertNotNull(saxOutputter);
        assertEquals(processor, saxOutputter.getSAXOutputProcessor());
        assertEquals(format, saxOutputter.getFormat());
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        assertEquals(entityResolver, saxOutputter.getEntityResolver());
    }

    // Test setContentHandler method
    @Test
    public void testSetAndGetContentHandler() {
        final ContentHandler contentHandler = mock(ContentHandler.class);
        final SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler(contentHandler);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
    }

    // Test getters and setters for other handlers and properties
    @Test
    public void testGettersAndSettersForOtherHandlersAndProperties() {
        final ContentHandler contentHandler = mock(ContentHandler.class);
        final ErrorHandler errorHandler = mock(ErrorHandler.class);
        final DTDHandler dtdHandler = mock(DTDHandler.class);
        final EntityResolver entityResolver = mock(EntityResolver.class);
        final LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        final DeclHandler declHandler = mock(DeclHandler.class);
        final boolean declareNamespaces = true;
        final boolean reportDtdEvents = false;
        final SAXOutputter saxOutputter = new SAXOutputter();

        // Test setters
        saxOutputter.setContentHandler(contentHandler);
        saxOutputter.setErrorHandler(errorHandler);
        saxOutputter.setDTDHandler(dtdHandler);
        saxOutputter.setEntityResolver(entityResolver);
        saxOutputter.setLexicalHandler(lexicalHandler);
        saxOutputter.setDeclHandler(declHandler);
        saxOutputter.setReportNamespaceDeclarations(declareNamespaces);
        saxOutputter.setReportDTDEvents(reportDtdEvents);

        // Test getters
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        assertEquals(entityResolver, saxOutputter.getEntityResolver());
        assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
        assertEquals(declHandler, saxOutputter.getDeclHandler());
        assertEquals(declareNamespaces, saxOutputter.getReportNamespaceDeclarations());
        assertEquals(reportDtdEvents, saxOutputter.getReportDTDEvents());
    }

    // Test setFeature method
    @Test
    public void testSetFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        assertTrue(saxOutputter.getReportNamespaceDeclarations());
        saxOutputter.setFeature("http://xml.org/sax/features/validation", false);
        assertFalse(saxOutputter.getReportDTDEvents());
    }

    // Test getFeature method
    @Test
    public void testGetFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        assertTrue(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertFalse(saxOutputter.getFeature("http://xml.org/sax/features/validation2"));
    }

    // Test setProperty method
    @Test
    public void testSetProperty() throws SAXNotRecognizedException, SAXNotSupportedException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        final LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        saxOutputter.setProperty("http://xml.org/sax/properties/lexical-handler", lexicalHandler);
        assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());

        final DeclHandler declHandler = mock(DeclHandler.class);
        saxOutputter.setProperty("http://xml.org/sax/properties/declaration-handler", declHandler);
        assertEquals(declHandler, saxOutputter.getDeclHandler());
    }

    // Test getProperty method
    @Test
    public void testGetProperty() throws SAXNotRecognizedException, SAXNotSupportedException {
        final SAXOutputter saxOutputter = new SAXOutputter();
        final LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        saxOutputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, saxOutputter.getProperty("http://xml.org/sax/properties/lexical-handler"));

        final DeclHandler declHandler = mock(DeclHandler.class);
        saxOutputter.setDeclHandler(declHandler);
        assertEquals(declHandler, saxOutputter.getProperty("http://xml.org/sax/properties/declaration-handler"));
    }

    // Test outputDocument method with valid Document
    @Test
    public void testOutputDocumentValid() throws JDOMException, SAXException {
        final Document document = new JDOMFactory().newDocument();
        final StringWriter stringWriter = new StringWriter();
        final String expectedOutput = "<root/>";

        final SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler((contentHandler) -> contentHandler.startDocument());
        saxOutputter.setContentHandler((contentHandler) -> contentHandler.startElement("root"));
        saxOutputter.setContentHandler((contentHandler) -> contentHandler.endElement("root"));
        saxOutputter.setContentHandler((contentHandler) -> contentHandler.endDocument());

        saxOutputter.output(document);

        assertEquals(expectedOutput, stringWriter.toString());
    }

    // Test outputDocument method with null Document
    @Test
    public void testOutputDocumentNull() {
        final SAXOutputter saxOutputter = new SAXOutputter();

        try {
            saxOutputter.output(null);
            fail();
        } catch (JDOMException e) {
            // expected
        }
    }

    // Test outputNodes method with valid List of Content
    @Test
    public void testOutputNodesValid() throws JDOMException, SAXException {
        final Document document = new JDOMFactory().newDocument();
        final List<Content> nodes = Arrays.asList(document.getRootElement().getContent());

        final ByteArrayInputStream inputStream = new ByteArrayInputStream("<root><child>Child 1</child><child>Child 2</child></root>".getBytes());
        final StringWriter stringWriter = new StringWriter();

        final SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler((contentHandler) -> {
            if (contentHandler instanceof StartElement) {
                assertEquals("child", ((StartElement) contentHandler).getName());
            }
        });

        saxOutputter.output(nodes);

        inputStream.close();
        assertEquals(inputStream.toString(), stringWriter.toString());
    }

    // Test outputNodes method with empty List
    @Test
    public void testOutputNodesEmpty() throws JDOMException, SAXException {
        final SAXOutputter saxOutputter = new SAXOutputter();

        saxOutputter.output(Collections.emptyList());
    }

    // Test outputFragment method with valid Content
    @Test
    public void testOutputFragmentValid() throws JDOMException, SAXException {
        final Text text = new JDOMFactory().getText("Hello World!");

        final ByteArrayInputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><child>" + text.getText() + "</child></root>".getBytes());
        final StringWriter stringWriter = new StringWriter();

        final SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler((contentHandler) -> {
            if (contentHandler instanceof StartElement) {
                assertEquals("root", ((StartElement) contentHandler).getName());
            } else if (contentHandler instanceof Characters) {
                assertEquals("Hello World!", new String(((Characters) contentHandler).getChars(), 0, ((Characters) contentHandler).getLength()));
            }
        });

        saxOutputter.outputFragment(text);

        inputStream.close();
        assertEquals(inputStream.toString(), stringWriter.toString());
    }

    // Test outputFragment method with null Content
    @Test
    public void testOutputFragmentNull() {
        final SAXOutputter saxOutputter = new SAXOutputter();

        try {
            saxOutputter.outputFragment(null);
            fail();
        } catch (JDOMException e) {
            // expected
        }
    }
}