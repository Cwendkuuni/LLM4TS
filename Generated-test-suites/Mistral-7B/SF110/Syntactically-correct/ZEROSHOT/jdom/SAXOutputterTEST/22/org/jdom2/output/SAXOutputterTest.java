package org.jdom2.output;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jdom2.*;
import org.jdom2.output.*;
import org.xml.sax.*;
import org.xml.sax.ext.*;

import org.junit.Before;
import org.junit.Test;

public class SAXOutputterTest {

    private ContentHandler contentHandler;
    private ErrorHandler errorHandler;
    private DTDHandler dtdHandler;
    private EntityResolver entityResolver;
    private LexicalHandler lexicalHandler;
    private DeclHandler declHandler;
    private SAXOutputter saxOutputter;

    @Before
    public void setup() {
        contentHandler = mock(ContentHandler.class);
        errorHandler = mock(ErrorHandler.class);
        dtdHandler = mock(DTDHandler.class);
        entityResolver = mock(EntityResolver.class);
        lexicalHandler = mock(LexicalHandler.class);
        declHandler = mock(DeclHandler.class);
        saxOutputter = new SAXOutputter();
    }

    @Test
    public void testDefaultConstructor() {
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
    public void testContentHandlerConstructor() {
        saxOutputter = new SAXOutputter(contentHandler);
        assertSame(contentHandler, saxOutputter.getContentHandler());
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
    public void testContentHandlerErrorHandlerConstructor() {
        saxOutputter = new SAXOutputter(contentHandler, errorHandler);
        assertSame(contentHandler, saxOutputter.getContentHandler());
        assertSame(errorHandler, saxOutputter.getErrorHandler());
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
    public void testContentHandlerErrorHandlerDTDHandlerConstructor() {
        saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler);
        assertSame(contentHandler, saxOutputter.getContentHandler());
        assertSame(errorHandler, saxOutputter.getErrorHandler());
        assertSame(dtdHandler, saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
        saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver);
        assertSame(contentHandler, saxOutputter.getContentHandler());
        assertSame(errorHandler, saxOutputter.getErrorHandler());
        assertSame(dtdHandler, saxOutputter.getDTDHandler());
        assertSame(entityResolver, saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testContentHandlerErrorHandlerDTDHandlerEntityResolverLexicalHandlerConstructor() {
        saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler);
        assertSame(contentHandler, saxOutputter.getContentHandler());
        assertSame(errorHandler, saxOutputter.getErrorHandler());
        assertSame(dtdHandler, saxOutputter.getDTDHandler());
        assertSame(entityResolver, saxOutputter.getEntityResolver());
        assertSame(lexicalHandler, saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputProcessorAndFormatConstructor() {
        final SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        final Format format = new DefaultFormat();
        saxOutputter = new SAXOutputter(processor, format, contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler);
        assertSame(processor, saxOutputter.getSAXOutputProcessor());
        assertSame(format, saxOutputter.getFormat());
        assertSame(contentHandler, saxOutputter.getContentHandler());
        assertSame(errorHandler, saxOutputter.getErrorHandler());
        assertSame(dtdHandler, saxOutputter.getDTDHandler());
        assertSame(entityResolver, saxOutputter.getEntityResolver());
        assertSame(lexicalHandler, saxOutputter.getLexicalHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testSetters() {
        saxOutputter = new SAXOutputter();
        final ContentHandler contentHandler = mock(ContentHandler.class);
        saxOutputter.setContentHandler(contentHandler);
        assertSame(contentHandler, saxOutputter.getContentHandler());
        final ErrorHandler errorHandler = mock(ErrorHandler.class);
        saxOutputter.setErrorHandler(errorHandler);
        assertSame(errorHandler, saxOutputter.getErrorHandler());
        final DTDHandler dtdHandler = mock(DTDHandler.class);
        saxOutputter.setDTDHandler(dtdHandler);
        assertSame(dtdHandler, saxOutputter.getDTDHandler());
        final EntityResolver entityResolver = mock(EntityResolver.class);
        saxOutputter.setEntityResolver(entityResolver);
        assertSame(entityResolver, saxOutputter.getEntityResolver());
        final LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        saxOutputter.setLexicalHandler(lexicalHandler);
        assertSame(lexicalHandler, saxOutputter.getLexicalHandler());
        final DeclHandler declHandler = mock(DeclHandler.class);
        saxOutputter.setDeclHandler(declHandler);
        assertSame(declHandler, saxOutputter.getDeclHandler());
        saxOutputter.setReportNamespaceDeclarations(true);
        assertTrue(saxOutputter.getReportNamespaceDeclarations());
        saxOutputter.setReportNamespaceDeclarations(false);
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        saxOutputter.setReportDTDEvents(true);
        assertTrue(saxOutputter.getReportDTDEvents());
        saxOutputter.setReportDTDEvents(false);
        assertFalse(saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testSettersWithNull() {
        saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler(null);
        assertNull(saxOutputter.getContentHandler());
        saxOutputter.setErrorHandler(null);
        assertNull(saxOutputter.getErrorHandler());
        saxOutputter.setDTDHandler(null);
        assertNull(saxOutputter.getDTDHandler());
        saxOutputter.setEntityResolver(null);
        assertNull(saxOutputter.getEntityResolver());
        saxOutputter.setLexicalHandler(null);
        assertNull(saxOutputter.getLexicalHandler());
        saxOutputter.setDeclHandler(null);
        assertNull(saxOutputter.getDeclHandler());
    }

    @Test(expected = SAXNotSupportedException.class)
    public void testSetNamespacesFeature() {
        saxOutputter = new SAXOutputter();
        saxOutputter.setFeature("http://xml.org/sax/features/namespaces", false);
        saxOutputter.setFeature("http://xml.org/sax/features/namespaces", true);
        saxOutputter.setFeature("http://xml.org/sax/features/namespaces", false);
    }

    @Test(expected = SAXNotRecognizedException.class)
    public void testSetUnknownFeature() {
        saxOutputter = new SAXOutputter();
        saxOutputter.setFeature("http://xml.org/sax/features/unknown", true);
    }

    @Test
    public void testSetProperty() {
        saxOutputter = new SAXOutputter();
        final LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        saxOutputter.setProperty("http://xml.org/sax/properties/lexical-handler", lexicalHandler);
        assertSame(lexicalHandler, saxOutputter.getLexicalHandler());
        final DeclHandler declHandler = mock(DeclHandler.class);
        saxOutputter.setProperty("http://xml.org/sax/properties/declaration-handler", declHandler);
        assertSame(declHandler, saxOutputter.getDeclHandler());
    }

    @Test(expected = SAXNotRecognizedException.class)
    public void testSetUnknownProperty() {
        saxOutputter = new SAXOutputter();
        saxOutputter.setProperty("http://xml.org/sax/properties/unknown", "UnknownProperty");
    }

    @Test
    public void testOutputDocument() throws JDOMException {
        final Document document = new Document();
        saxOutputter.output(document);
    }

    @Test
    public void testOutputListOfContent() throws JDOMException {
        final Document document = new Document();
        final List<Content> nodes = new JDOMNamespacedList<>(document);
        saxOutputter.output(nodes);
    }

    @Test
    public void testOutputElement() throws JDOMException {
        final Document document = new Document();
        final Element element = new Element("Test");
        document.setRootElement(element);
        saxOutputter.output(element);
    }

    @Test
    public void testOutputFragmentListOfContent() throws JDOMException {
        final Document document = new Document();
        final List<Content> nodes = new JDOMNamespacedList<>(document);
        saxOutputter.outputFragment(nodes);
    }

    @Test
    public void testOutputFragmentContent() throws JDOMException {
        final Document document = new Document();
        final Element element = new Element("Test");
        document.setRootElement(element);
        saxOutputter.outputFragment(element);
    }
}