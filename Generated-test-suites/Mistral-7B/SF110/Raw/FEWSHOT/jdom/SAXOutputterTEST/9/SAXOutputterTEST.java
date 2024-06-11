package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.jdom2.output.support.*;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputter() {
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
    public void testSAXOutputterWithContentHandler() {
        ContentHandler contentHandler = MockContentHandler.create();
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
    public void testSAXOutputterWithContentHandlerErrorHandler() {
        ContentHandler contentHandler = MockContentHandler.create();
        ErrorHandler errorHandler = MockErrorHandler.create();
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
    public void testSAXOutputterWithContentHandlerErrorHandlerDTDHandler() {
        ContentHandler contentHandler = MockContentHandler.create();
        ErrorHandler errorHandler = MockErrorHandler.create();
        DTDHandler dtdHandler = MockDTDHandler.create();
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
    public void testSAXOutputterWithContentHandlerErrorHandlerDTDHandlerEntityResolver() {
        ContentHandler contentHandler = MockContentHandler.create();
        ErrorHandler errorHandler = MockErrorHandler.create();
        DTDHandler dtdHandler = MockDTDHandler.create();
        EntityResolver entityResolver = MockEntityResolver.create();
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
    public void testSAXOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandler() {
        ContentHandler contentHandler = MockContentHandler.create();
        ErrorHandler errorHandler = MockErrorHandler.create();
        DTDHandler dtdHandler = MockDTDHandler.create();
        LexicalHandler lexicalHandler = MockLexicalHandler.create();
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, lexicalHandler);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterWithContentHandlerErrorHandlerDTDHandlerLexicalHandlerDeclHandler() {
        ContentHandler contentHandler = MockContentHandler.create();
        ErrorHandler errorHandler = MockErrorHandler.create();
        DTDHandler dtdHandler = MockDTDHandler.create();
        LexicalHandler lexicalHandler = MockLexicalHandler.create();
        DeclHandler declHandler = MockDeclHandler.create();
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, lexicalHandler, declHandler);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
        assertEquals(declHandler, saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterWithSAXOutputProcessor() {
        SAXOutputProcessor processor = MockSAXOutputProcessor.create();
        SAXOutputter saxOutputter = new SAXOutputter(processor);
        assertEquals(processor, saxOutputter.getSAXOutputProcessor());
        assertNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    @Test
    public void testSAXOutputterWithFormat() {
        Format format = Format.createPrettyPrintFormat();
        SAXOutputter saxOutputter = new SAXOutputter(null, null, null, null, null, null, null, null, null, null, format);
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
        saxOutputter.setFormat(format);
        assertEquals(format, saxOutputter.getFormat());
    }

    @Test
    public void testOutputDocument() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.output(document);
        // Add asserts here based on the expected output.
    }

    @Test
    public void testOutputList() throws JDOMException {
        JDOMFactory factory = new JDOMFactory();
        Element root = factory.newElement("root");
        Element child1 = factory.newElement("child1");
        Element child2 = factory.newElement("child2");
        root.addContent(child1);
        root.addContent(child2);
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.output(new JDOMUtil().listChildren(root));
        // Add asserts here based on the expected output.
    }

    @Test
    public void testOutputElement() throws JDOMException {
        JDOMFactory factory = new JDOMFactory();
        Element element = factory.newElement("element");
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.output(element);
        // Add asserts here based on the expected output.
    }

    @Test
    public void testOutputFragmentList() throws JDOMException {
        JDOMFactory factory = new JDOMFactory();
        Element root = factory.newElement("root");
        Element child1 = factory.newElement("child1");
        Element child2 = factory.newElement("child2");
        root.addContent(child1);
        root.addContent(child2);
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.outputFragment(new JDOMUtil().listChildren(root));
        // Add asserts here based on the expected output.
    }

    @Test
    public void testOutputFragmentElement() throws JDOMException {
        JDOMFactory factory = new JDOMFactory();
        Element element = factory.newElement("element");
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.outputFragment(element);
        // Add asserts here based on the expected output.
    }
}