package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.xml.sax.*;
import org.jdom2.output.support.*;
import org.junit.Test;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterDefaultConstructor() {
        SAXOutputter outputter = new SAXOutputter();
        assertNull(outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        SAXOutputter outputter = new SAXOutputter(contentHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(dtdHandler, outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerLexicalHandlerConstructor() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, lexicalHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(dtdHandler, outputter.getDTDHandler());
        assertEquals(lexicalHandler, outputter.getLexicalHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterProcessorFormatConstructor() {
        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        Format format = mock(Format.class);
        SAXOutputter outputter = new SAXOutputter(processor, format);
        assertEquals(processor, outputter.getSAXOutputProcessor());
        assertEquals(format, outputter.getFormat());
        assertNull(outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterProcessorFormatContentHandlerConstructor() {
        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        Format format = mock(Format.class);
        ContentHandler contentHandler = mock(ContentHandler.class);
        SAXOutputter outputter = new SAXOutputter(processor, format, contentHandler);
        assertEquals(processor, outputter.getSAXOutputProcessor());
        assertEquals(format, outputter.getFormat());
        assertEquals(contentHandler, outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterProcessorFormatContentHandlerErrorHandlerConstructor() {
        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        Format format = mock(Format.class);
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        SAXOutputter outputter = new SAXOutputter(processor, format, contentHandler, errorHandler);
        assertEquals(processor, outputter.getSAXOutputProcessor());
        assertEquals(format, outputter.getFormat());
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterProcessorFormatContentHandlerErrorHandlerDTDHandlerConstructor() {
        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        Format format = mock(Format.class);
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        SAXOutputter outputter = new SAXOutputter(processor, format, contentHandler, errorHandler, dtdHandler);
        assertEquals(processor, outputter.getSAXOutputProcessor());
        assertEquals(format, outputter.getFormat());
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(dtdHandler, outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
    }

    @Test
    public void testSAXOutputterProcessorFormatContentHandlerErrorHandlerDTDHandlerLexicalHandlerConstructor() {
        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        Format format = mock(Format.class);
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        SAXOutputter outputter = new SAXOutputter(processor, format, contentHandler, errorHandler, dtdHandler, lexicalHandler);
        assertEquals(processor, outputter.getSAXOutputProcessor());
        assertEquals(format, outputter.getFormat());
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(dtdHandler, outputter.getDTDHandler());
        assertEquals(lexicalHandler, outputter.getLexicalHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
    }

    @Test
    public void testSetContentHandler() {
        SAXOutputter outputter = new SAXOutputter();
        ContentHandler contentHandler = mock(ContentHandler.class);
        outputter.setContentHandler(contentHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
    }

    @Test
    public void testGetContentHandler() {
        SAXOutputter outputter = new SAXOutputter();
        ContentHandler contentHandler = mock(ContentHandler.class);
        outputter.setContentHandler(contentHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
    }

    @Test
    public void testSetErrorHandler() {
        SAXOutputter outputter = new SAXOutputter();
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        outputter.setErrorHandler(errorHandler);
        assertEquals(errorHandler, outputter.getErrorHandler());
    }

    @Test
    public void testGetErrorHandler() {
        SAXOutputter outputter = new SAXOutputter();
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        outputter.setErrorHandler(errorHandler);
        assertEquals(errorHandler, outputter.getErrorHandler());
    }

    @Test
    public void testSetDTDHandler() {
        SAXOutputter outputter = new SAXOutputter();
        DTDHandler dtdHandler = mock(DTDHandler.class);
        outputter.setDTDHandler(dtdHandler);
        assertEquals(dtdHandler, outputter.getDTDHandler());
    }

    @Test
    public void testGetDTDHandler() {
        SAXOutputter outputter = new SAXOutputter();
        DTDHandler dtdHandler = mock(DTDHandler.class);
        outputter.setDTDHandler(dtdHandler);
        assertEquals(dtdHandler, outputter.getDTDHandler());
    }

    @Test
    public void testSetEntityResolver() {
        SAXOutputter outputter = new SAXOutputter();
        EntityResolver entityResolver = mock(EntityResolver.class);
        outputter.setEntityResolver(entityResolver);
        assertEquals(entityResolver, outputter.getEntityResolver());
    }

    @Test
    public void testGetEntityResolver() {
        SAXOutputter outputter = new SAXOutputter();
        EntityResolver entityResolver = mock(EntityResolver.class);
        outputter.setEntityResolver(entityResolver);
        assertEquals(entityResolver, outputter.getEntityResolver());
    }

    @Test
    public void testSetLexicalHandler() {
        SAXOutputter outputter = new SAXOutputter();
        LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        outputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, outputter.getLexicalHandler());
    }

    @Test
    public void testGetLexicalHandler() {
        SAXOutputter outputter = new SAXOutputter();
        LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        outputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, outputter.getLexicalHandler());
    }

    @Test
    public void testSetDeclHandler() {
        SAXOutputter outputter = new SAXOutputter();
        DeclHandler declHandler = mock(DeclHandler.class);
        outputter.setDeclHandler(declHandler);
        assertEquals(declHandler, outputter.getDeclHandler());
    }

    @Test
    public void testGetDeclHandler() {
        SAXOutputter outputter = new SAXOutputter();
        DeclHandler declHandler = mock(DeclHandler.class);
        outputter.setDeclHandler(declHandler);
        assertEquals(declHandler, outputter.getDeclHandler());
    }

    @Test
    public void testGetReportNamespaceDeclarations() {
        SAXOutputter outputter = new SAXOutputter();
        assertFalse(outputter.getReportNamespaceDeclarations());
        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testSetReportNamespaceDeclarations() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testGetReportDTDEvents() {
        SAXOutputter outputter = new SAXOutputter();
        assertTrue(outputter.getReportDTDEvents());
        outputter.setReportDTDEvents(false);
        assertFalse(outputter.getReportDTDEvents());
    }

    @Test
    public void testSetReportDTDEvents() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setReportDTDEvents(false);
        assertFalse(outputter.getReportDTDEvents());
    }

    @Test
    public void testOutputDocument() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(document);
        // Add assertions here based on expected output
    }

    @Test
    public void testOutputNodes() throws JDOMException {
        JDOMFactory factory = new JDOMFactory();
        List<Content> nodes = new ArrayList<>();
        nodes.add(factory.newProcessingInstruction("xml", "version=\"1.0\""));
        nodes.add(factory.newComment("This is a comment"));
        nodes.add(factory.newElement("root", null));
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(nodes);
        // Add assertions here based on expected output
    }

    @Test
    public void testOutputElement() throws JDOMException {
        JDOMFactory factory = new JDOMFactory();
        Element element = factory.newElement("element", null);
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(element);
        // Add assertions here based on expected output
    }

    @Test
    public void testOutputFragment() throws JDOMException {
        JDOMFactory factory = new JDOMFactory();
        Content fragment = factory.newProcessingInstruction("xml", "version=\"1.0\"");
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(fragment);
        // Add assertions here based on expected output
    }
}