package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.xml.sax.*;
import org.jdom2.output.support.*;
import org.junit.Test;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterInstance() {
        SAXOutputter outputter = new SAXOutputter();
        assertNotNull(outputter);
    }

    @Test
    public void testSAXOutputterInstanceWithContentHandler() {
        ContentHandler handler = mock(ContentHandler.class);
        SAXOutputter outputter = new SAXOutputter(handler);
        assertNotNull(outputter);
        assertEquals(handler, outputter.getContentHandler());
    }

    @Test
    public void testSAXOutputterInstanceWithContentHandlerAndErrorHandler() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler);
        assertNotNull(outputter);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
    }

    @Test
    public void testSAXOutputterInstanceWithAllHandlers() {
        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        EntityResolver entityResolver = mock(EntityResolver.class);
        LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler);
        assertNotNull(outputter);
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(dtdHandler, outputter.getDTDHandler());
        assertEquals(entityResolver, outputter.getEntityResolver());
        assertEquals(lexicalHandler, outputter.getLexicalHandler());
    }

    @Test
    public void testSAXOutputterInstanceWithProcessorAndFormat() {
        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        Format format = mock(Format.class);
        SAXOutputter outputter = new SAXOutputter(processor, format, null, null, null, null, null);
        assertNotNull(outputter);
        assertEquals(processor, outputter.getSAXOutputProcessor());
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testSetContentHandler() {
        SAXOutputter outputter = new SAXOutputter();
        ContentHandler handler = mock(ContentHandler.class);
        outputter.setContentHandler(handler);
        assertEquals(handler, outputter.getContentHandler());
    }

    @Test
    public void testGetContentHandler() {
        ContentHandler handler = mock(ContentHandler.class);
        SAXOutputter outputter = new SAXOutputter(handler);
        assertEquals(handler, outputter.getContentHandler());
    }

    @Test
    public void testSetErrorHandler() {
        SAXOutputter outputter = new SAXOutputter();
        ErrorHandler handler = mock(ErrorHandler.class);
        outputter.setErrorHandler(handler);
        assertEquals(handler, outputter.getErrorHandler());
    }

    @Test
    public void testGetErrorHandler() {
        ErrorHandler handler = mock(ErrorHandler.class);
        SAXOutputter outputter = new SAXOutputter(null, handler);
        assertEquals(handler, outputter.getErrorHandler());
    }

    @Test
    public void testSetDTDHandler() {
        SAXOutputter outputter = new SAXOutputter();
        DTDHandler handler = mock(DTDHandler.class);
        outputter.setDTDHandler(handler);
        assertEquals(handler, outputter.getDTDHandler());
    }

    @Test
    public void testGetDTDHandler() {
        DTDHandler handler = mock(DTDHandler.class);
        SAXOutputter outputter = new SAXOutputter(null, null, handler);
        assertEquals(handler, outputter.getDTDHandler());
    }

    @Test
    public void testSetEntityResolver() {
        SAXOutputter outputter = new SAXOutputter();
        EntityResolver resolver = mock(EntityResolver.class);
        outputter.setEntityResolver(resolver);
        assertEquals(resolver, outputter.getEntityResolver());
    }

    @Test
    public void testGetEntityResolver() {
        EntityResolver resolver = mock(EntityResolver.class);
        SAXOutputter outputter = new SAXOutputter(null, null, null, resolver);
        assertEquals(resolver, outputter.getEntityResolver());
    }

    @Test
    public void testSetLexicalHandler() {
        SAXOutputter outputter = new SAXOutputter();
        LexicalHandler handler = mock(LexicalHandler.class);
        outputter.setLexicalHandler(handler);
        assertEquals(handler, outputter.getLexicalHandler());
    }

    @Test
    public void testGetLexicalHandler() {
        LexicalHandler handler = mock(LexicalHandler.class);
        SAXOutputter outputter = new SAXOutputter(null, null, null, null, handler);
        assertEquals(handler, outputter.getLexicalHandler());
    }

    @Test
    public void testSetDeclHandler() {
        SAXOutputter outputter = new SAXOutputter();
        DeclHandler handler = mock(DeclHandler.class);
        outputter.setDeclHandler(handler);
        assertEquals(handler, outputter.getDeclHandler());
    }

    @Test
    public void testGetDeclHandler() {
        DeclHandler handler = mock(DeclHandler.class);
        SAXOutputter outputter = new SAXOutputter(null, null, null, null, null, handler);
        assertEquals(handler, outputter.getDeclHandler());
    }

    @Test
    public void testGetReportNamespaceDeclarations() {
        SAXOutputter outputter = new SAXOutputter();
        assertTrue(outputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testSetReportNamespaceDeclarations() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setReportNamespaceDeclarations(false);
        assertFalse(outputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testGetReportDTDEvents() {
        SAXOutputter outputter = new SAXOutputter();
        assertTrue(outputter.getReportDTDEvents());
    }

    @Test
    public void testSetReportDTDEvents() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setReportDTDEvents(false);
        assertFalse(outputter.getReportDTDEvents());
    }

    @Test
    public void testSetFormat() {
        SAXOutputter outputter = new SAXOutputter();
        Format format = mock(Format.class);
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testGetFormat() {
        Format format = mock(Format.class);
        SAXOutputter outputter = new SAXOutputter(null, null, null, null, null, null, null, format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testBuildTarget() {
        Document document = new Document();
        SAXOutputter outputter = new SAXOutputter();
        SAXTarget target = outputter.buildTarget(document);
        assertNotNull(target);
    }

    @Test(expected = JDOMException.class)
    public void testOutputNullDocument() {
        Document document = null;
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(document);
    }

    @Test
    public void testOutputDocument() throws JDOMException {
        Document document = new Document();
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(document);
    }

    @Test
    public void testOutputList() throws JDOMException {
        Document document = new Document();
        List<Content> nodes = new ArrayList<>();
        nodes.add(document.getRootElement());
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(nodes);
    }

    @Test
    public void testOutputElement() throws JDOMException {
        Document document = new Document();
        Element node = document.getRootElement();
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(node);
    }

    @Test
    public void testOutputFragmentList() throws JDOMException {
        List<Content> nodes = new ArrayList<>();
        nodes.add(new StringText("Test"));
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(nodes);
    }

    @Test
    public void testOutputFragmentContent() throws JDOMException {
        Content node = new StringText("Test");
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(node);
    }
}