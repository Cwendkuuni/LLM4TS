package org.jdom2.output;

import org.jdom2.JDOMException;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.support.SAXTarget;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SAXOutputterTest {

    @org.junit.Before
    public void setUp() {
        // Initialize an instance of SAXOutputter for testing
        SAXOutputter saxOutputter = new SAXOutputter();
    }

    @org.junit.Test
    public void testSAXOutputterWithDefaultConstructor() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
        assertNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
    }

    @org.junit.Test
    public void testSAXOutputterWithContentHandler() {
        ContentHandler contentHandler = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler);
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
    }

    @org.junit.Test
    public void testSAXOutputterWithContentHandlerAndErrorHandler() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler);
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
    }

    @org.junit.Test
    public void testSAXOutputterWithContentHandlerAndErrorHandlerAndDTDHandler() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler);
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
    }

    @org.junit.Test
    public void testSAXOutputterWithContentHandlerAndErrorHandlerAndDTDHandlerAndEntityResolver() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        EntityResolver entityResolver = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver);
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        assertEquals(entityResolver, saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
    }

    @org.junit.Test
    public void testSAXOutputterWithContentHandlerAndErrorHandlerAndDTDHandlerAndEntityResolverAndLexicalHandler() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        EntityResolver entityResolver = new DefaultHandler();
        LexicalHandler lexicalHandler = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler);
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        assertEquals(entityResolver, saxOutputter.getEntityResolver());
        assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
    }

    @org.junit.Test
    public void testSAXOutputterWithAllParams() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        EntityResolver entityResolver = new DefaultHandler();
        LexicalHandler lexicalHandler = new DefaultHandler();
        DeclHandler declHandler = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter(null, null, null, null, null, declHandler);
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
        assertNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertEquals(declHandler, saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());

        // Test setters
        saxOutputter.setContentHandler(contentHandler);
        assertEquals(contentHandler, saxOutputter.getContentHandler());

        saxOutputter.setErrorHandler(errorHandler);
        assertEquals(errorHandler, saxOutputter.getErrorHandler());

        saxOutputter.setDTDHandler(dtdHandler);
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());

        saxOutputter.setEntityResolver(entityResolver);
        assertEquals(entityResolver, saxOutputter.getEntityResolver());

        saxOutputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());

        saxOutputter.setDeclHandler(declHandler);
        assertEquals(declHandler, saxOutputter.getDeclHandler());

        saxOutputter.setReportNamespaceDeclarations(true);
        assertTrue(saxOutputter.getReportNamespaceDeclarations());

        saxOutputter.setReportDTDEvents(false);
        assertFalse(saxOutputter.getReportDTDEvents());

        saxOutputter.setFormat(Format.getPrettyFormat());
        assertEquals(Format.getPrettyFormat(), saxOutputter.getFormat());

        saxOutputter.setSAXOutputProcessor(new DefaultSAXOutputProcessor());
        assertNotSame(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
    }

    @org.junit.Test
    public void testOutputDocument() throws JDOMException {
        Document document = new Document();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.output(document);
    }

    @org.junit.Test
    public void testOutputNodesList() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        List<Content> nodes = new ArrayList<>();
        nodes.add(root);
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.output(nodes);
    }

    @org.junit.Test
    public void testOutputNode() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.output(root);
    }

    @org.junit.Test
    public void testOutputFragmentList() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        List<Content> fragment = new ArrayList<>();
        fragment.add(new Text("Test fragment"));
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.outputFragment(fragment);
    }

    @org.junit.Test
    public void testOutputFragment() throws JDOMException {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        Text text = new Text("Test fragment");
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.outputFragment(text);
    }

    @org.junit.Test(expected = JDOMException.class)
    public void testOutputInvalidContent() throws JDOMException {
        Document document = new Document();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.outputFragment(null);
    }
}