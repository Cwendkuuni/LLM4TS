package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.jdom2.output.support.*;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterDefaultConstructor() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        EntityResolver entityResolver = mock(EntityResolver.class);
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        assertEquals(entityResolver, saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverLexicalHandlerConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        EntityResolver entityResolver = mock(EntityResolver.class);
        LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        assertEquals(entityResolver, saxOutputter.getEntityResolver());
        assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterProcessorFormatConstructor() {
        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        Format format = Format.getRawFormat();
        SAXOutputter saxOutputter = new SAXOutputter(processor, format);
        assertEquals(processor, saxOutputter.getSAXOutputProcessor());
        assertEquals(format, saxOutputter.getFormat());
        assertNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterProcessDocument() throws JDOMException {
        Document document = new Document();
        SAXOutputter saxOutputter = new SAXOutputter();
        ContentHandler contentHandler = mock(ContentHandler.class);
        saxOutputter.setContentHandler(contentHandler);
        SAXTarget saxTarget = saxOutputter.buildTarget(document);
        Document expectedDocument = new Document();
        saxOutputter.output(expectedDocument);
        saxOutputter.process(saxTarget, saxOutputter.getFormat(), document);
        verify(contentHandler).startDocument();
        verify(contentHandler).startElement("document", expectedDocument.getRootElement().getName().getLocalPart());
        verify(contentHandler).characters(expectedDocument.getRootElement().getText().getValues());
        verify(contentHandler).endElement("document");
        verify(contentHandler).endDocument();
    }

    @Test
    public void testSAXOutputterProcessList() throws JDOMException {
        Document document = new Document();
        List<Content> nodes = new ArrayList<>();
        nodes.add(document.getRootElement());
        SAXOutputter saxOutputter = new SAXOutputter();
        ContentHandler contentHandler = mock(ContentHandler.class);
        saxOutputter.setContentHandler(contentHandler);
        SAXTarget saxTarget = saxOutputter.buildTarget(null);
        saxOutputter.output(nodes);
        verify(contentHandler).startDocument();
        verify(contentHandler).startElement("document", Element.NAMESPACE_UNSPECIFIED, "document");
        verify(contentHandler, times(nodes.size())).startElement("element", Element.NAMESPACE_UNSPECIFIED, Element.getName(nodes.get(0)));
        for (Content node : nodes) {
            if (node instanceof Element) {
                Element element = (Element)node;
                verify(contentHandler).characters(element.getText().getValues());
            }
        }
        verify(contentHandler).endElement("document");
        verify(contentHandler).endDocument();
    }

    @Test
    public void testSAXOutputterProcessElement() throws JDOMException {
        Document document = new Document();
        Element element = document.getRootElement();
        SAXOutputter saxOutputter = new SAXOutputter();
        ContentHandler contentHandler = mock(ContentHandler.class);
        saxOutputter.setContentHandler(contentHandler);
        SAXTarget saxTarget = saxOutputter.buildTarget(null);
        saxOutputter.output(element);
        verify(contentHandler).startDocument();
        verify(contentHandler).startElement("document", Element.NAMESPACE_UNSPECIFIED, "document");
        verify(contentHandler).startElement("element", Element.NAMESPACE_UNSPECIFIED, Element.getName(element));
        verify(contentHandler).characters(element.getText().getValues());
        verify(contentHandler).endElement("element");
        verify(contentHandler).endElement("document");
        verify(contentHandler).endDocument();
    }

    @Test
    public void testSAXOutputterProcessFragment() throws JDOMException {
        Document document = new Document();
        List<Content> nodes = new ArrayList<>();
        nodes.add(document.getRootElement());
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.outputFragment(nodes);
        verify(saxOutputter.getErrorHandler()).error(argThat(e -> e instanceof SAXParseException && e.getMessage().contains("Invalid element content:")));
    }

    @Test
    public void testSAXOutputterSetReportNamespaceDeclarations() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setReportNamespaceDeclarations(true);
        assertTrue(saxOutputter.getReportNamespaceDeclarations());
        saxOutputter.setReportNamespaceDeclarations(false);
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testSAXOutputterSetReportDTDEvents() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setReportDTDEvents(true);
        assertTrue(saxOutputter.getReportDTDEvents());
        saxOutputter.setReportDTDEvents(false);
        assertFalse(saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterSetFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        assertTrue(saxOutputter.getReportNamespaceDeclarations());
        saxOutputter.setFeature("http://xml.org/sax/features/validation", true);
        assertTrue(saxOutputter.getReportDTDEvents());
        saxOutputter.setFeature("http://xml.org/sax/features/validation", false);
        assertFalse(saxOutputter.getReportDTDEvents());
        try {
            saxOutputter.setFeature("http://xml.org/sax/features/colors", true);
            fail("Expected SAXNotRecognizedException");
        } catch (SAXNotRecognizedException e) {
            // Expected
        }
    }

    @Test
    public void testSAXOutputterGetFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setReportNamespaceDeclarations(true);
        saxOutputter.setReportDTDEvents(true);
        assertEquals("http://xml.org/sax/features/namespace-prefixes", saxOutputter.getFeature("http://xml.org/sax/features/namespace-prefixes"));
        assertEquals("http://xml.org/sax/features/validation", saxOutputter.getFeature("http://xml.org/sax/features/validation"));
        try {
            saxOutputter.getFeature("http://xml.org/sax/features/colors");
            fail("Expected SAXNotRecognizedException");
        } catch (SAXNotRecognizedException e) {
            // Expected
        }
    }

    @Test
    public void testSAXOutputterSetProperty() throws SAXNotRecognizedException, SAXNotSupportedException {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setProperty("http://xml.org/sax/properties/declaration-handler", mock(DeclHandler.class));
        assertEquals(mock(DeclHandler.class), saxOutputter.getDeclHandler());
        saxOutputter.setProperty("http://xml.org/sax/properties/lexical-handler", mock(LexicalHandler.class));
        assertEquals(mock(LexicalHandler.class), saxOutputter.getLexicalHandler());
        try {
            saxOutputter.setProperty("http://xml.org/sax/properties/declaration-handler", "Invalid");
            fail("Expected SAXNotRecognizedException");
        } catch (SAXNotRecognizedException e) {
            // Expected
        }
    }

    @Test
    public void testSAXOutputterGetProperty() throws SAXNotRecognizedException, SAXNotSupportedException {
        SAXOutputter saxOutputter = new SAXOutputter();
        DeclHandler declHandler = mock(DeclHandler.class);
        saxOutputter.setDeclHandler(declHandler);
        assertEquals(declHandler, saxOutputter.getProperty("http://xml.org/sax/properties/declaration-handler"));
        LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        saxOutputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, saxOutputter.getProperty("http://xml.org/sax/properties/lexical-handler"));
        try {
            saxOutputter.getProperty("http://xml.org/sax/properties/declaration-handler2");
            fail("Expected SAXNotRecognizedException");
        } catch (SAXNotRecognizedException e) {
            // Expected
        }
    }
}