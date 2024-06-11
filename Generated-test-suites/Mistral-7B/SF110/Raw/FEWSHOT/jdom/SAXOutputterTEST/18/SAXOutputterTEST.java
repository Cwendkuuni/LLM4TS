package org.jdom2.output;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.support.SAXTarget;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterDefaultConstructor() {
        SAXOutputter outputter = new SAXOutputter();
        assertSame(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertSame(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerConstructor() {
        ContentHandler handler = new DefaultHandler();
        SAXOutputter outputter = new SAXOutputter(handler);
        assertSame(handler, outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertSame(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertSame(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerConstructor() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler);
        assertSame(contentHandler, outputter.getContentHandler());
        assertSame(errorHandler, outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertSame(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertSame(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerConstructor() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler);
        assertSame(contentHandler, outputter.getContentHandler());
        assertSame(errorHandler, outputter.getErrorHandler());
        assertSame(dtdHandler, outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertSame(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertSame(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverConstructor() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        EntityResolver entityResolver = new DefaultHandler();
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver);
        assertSame(contentHandler, outputter.getContentHandler());
        assertSame(errorHandler, outputter.getErrorHandler());
        assertSame(dtdHandler, outputter.getDTDHandler());
        assertSame(entityResolver, outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertSame(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertSame(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerLexicalHandlerConstructor() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        LexicalHandler lexicalHandler = new DefaultHandler();
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, lexicalHandler);
        assertSame(contentHandler, outputter.getContentHandler());
        assertSame(errorHandler, outputter.getErrorHandler());
        assertSame(dtdHandler, outputter.getDTDHandler());
        assertSame(lexicalHandler, outputter.getLexicalHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertSame(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertSame(Format.getRawFormat(), outputter.getFormat());
    }

    @Test
    public void testSAXOutputterCustomConstructor() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        EntityResolver entityResolver = new DefaultHandler();
        LexicalHandler lexicalHandler = new DefaultHandler();
        DeclHandler declHandler = new DefaultHandler();
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler, declHandler);
        assertSame(contentHandler, outputter.getContentHandler());
        assertSame(errorHandler, outputter.getErrorHandler());
        assertSame(dtdHandler, outputter.getDTDHandler());
        assertSame(entityResolver, outputter.getEntityResolver());
        assertSame(lexicalHandler, outputter.getLexicalHandler());
        assertSame(declHandler, outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertNull(outputter.getSAXOutputProcessor());
        assertNull(outputter.getFormat());
    }

    @Test
    public void testSetContentHandler() {
        SAXOutputter outputter = new SAXOutputter();
        ContentHandler handler = new DefaultHandler();
        outputter.setContentHandler(handler);
        assertSame(handler, outputter.getContentHandler());
    }

    @Test
    public void testSetErrorHandler() {
        SAXOutputter outputter = new SAXOutputter();
        ErrorHandler errorHandler = new DefaultHandler();
        outputter.setErrorHandler(errorHandler);
        assertSame(errorHandler, outputter.getErrorHandler());
    }

    @Test
    public void testSetDTDHandler() {
        SAXOutputter outputter = new SAXOutputter();
        DTDHandler dtdHandler = new DefaultHandler();
        outputter.setDTDHandler(dtdHandler);
        assertSame(dtdHandler, outputter.getDTDHandler());
    }

    @Test
    public void testSetEntityResolver() {
        SAXOutputter outputter = new SAXOutputter();
        EntityResolver entityResolver = new DefaultHandler();
        outputter.setEntityResolver(entityResolver);
        assertSame(entityResolver, outputter.getEntityResolver());
    }

    @Test
    public void testSetLexicalHandler() {
        SAXOutputter outputter = new SAXOutputter();
        LexicalHandler lexicalHandler = new DefaultHandler();
        outputter.setLexicalHandler(lexicalHandler);
        assertSame(lexicalHandler, outputter.getLexicalHandler());
    }

    @Test
    public void testSetDeclHandler() {
        SAXOutputter outputter = new SAXOutputter();
        DeclHandler declHandler = new DefaultHandler();
        outputter.setDeclHandler(declHandler);
        assertSame(declHandler, outputter.getDeclHandler());
    }

    @Test
    public void testSetReportNamespaceDeclarations() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());

        outputter.setReportNamespaceDeclarations(false);
        assertFalse(outputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testSetReportDTDEvents() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setReportDTDEvents(true);
        assertTrue(outputter.getReportDTDEvents());

        outputter.setReportDTDEvents(false);
        assertFalse(outputter.getReportDTDEvents());
    }

    @Test
    public void testSetSAXOutputProcessor() {
        SAXOutputter outputter = new SAXOutputter();
        SAXOutputProcessor processor = new DefaultSAXOutputProcessor();
        outputter.setSAXOutputProcessor(processor);
        assertSame(processor, outputter.getSAXOutputProcessor());
    }

    @Test
    public void testSetFormat() {
        SAXOutputter outputter = new SAXOutputter();
        Format format = Format.getPrettyFormat();
        outputter.setFormat(format);
        assertSame(format, outputter.getFormat());
    }

    @Test
    public void testOutputDocument() throws JDOMException, SAXException {
        Document document = new Document();
        SAXOutputter outputter = new SAXOutputter();
        SAXTarget target = outputter.buildTarget(document);
        outputter.output(document);
        outputter.getSAXOutputProcessor().process(target, outputter.getFormat(), document);
    }

    @Test
    public void testOutputNodes() throws JDOMException, SAXException {
        Element root = new Element("root");
        List<Content> nodes = new ArrayList<>();
        nodes.add(new Text("text"));
        nodes.add(new Comment("comment"));
        SAXOutputter outputter = new SAXOutputter();
        SAXTarget target = outputter.buildTarget(null);
        outputter.output(nodes);
        outputter.getSAXOutputProcessor().processAsDocument(target, outputter.getFormat(), nodes);
    }

    @Test
    public void testOutputElement() throws JDOMException, SAXException {
        Element root = new Element("root");
        SAXOutputter outputter = new SAXOutputter();
        SAXTarget target = outputter.buildTarget(null);
        outputter.output(root);
        outputter.getSAXOutputProcessor().processAsDocument(target, outputter.getFormat(), root);
    }

    @Test
    public void testOutputFragmentNodes() throws JDOMException, SAXException {
        Element root = new Element("root");
        List<Content> nodes = new ArrayList<>();
        nodes.add(new Text("text"));
        nodes.add(new Comment("comment"));
        SAXOutputter outputter = new SAXOutputter();
        SAXTarget target = outputter.buildTarget(null);
        outputter.outputFragment(nodes);
        outputter.getSAXOutputProcessor().process(target, outputter.getFormat(), nodes);
    }

    @Test
    public void testOutputFragmentContent() throws JDOMException, SAXException {
        Element root = new Element("root");
        Text text = new Text("text");
        SAXOutputter outputter = new SAXOutputter();
        SAXTarget target = outputter.buildTarget(null);
        outputter.outputFragment(text);
        outputter.getSAXOutputProcessor().process(target, outputter.getFormat(), text);
    }

    @Test
    public void testInvalidElementContent() throws JDOMException, SAXException {
        Element root = new Element("root");
        SAXOutputter outputter = new SAXOutputter();
        assertThrows(JDOMException.class, () -> outputter.outputFragment(root));
    }

    @Test
    public void testErrorHandler() throws JDOMException, SAXException {
        ErrorHandler errorHandler = new DefaultHandler();
        SAXOutputter outputter = new SAXOutputter();
        outputter.setErrorHandler(errorHandler);
        Document document = new Document();
        SAXTarget target = outputter.buildTarget(document);
        outputter.output(document);
        outputter.getSAXOutputProcessor().process(target, outputter.getFormat(), document);

        String errorMessage = "Invalid element content: ";
        assertThrows(JDOMException.class, () -> outputter.outputFragment(document));
        assertEquals(errorMessage, errorHandler.getMessage());
    }
}