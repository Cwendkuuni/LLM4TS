package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.xml.sax.*;
import org.junit.Test;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterCreation() {
        SAXOutputter outputter = new SAXOutputter();
        assertNull(outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
        assertNull(outputter.getDeclHandler());
        assertFalse(outputter.getReportNamespaceDeclarations());
        assertTrue(outputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputter.getFormat());

        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        EntityResolver entityResolver = mock(EntityResolver.class);
        LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        DeclHandler declHandler = mock(DeclHandler.class);
        SAXOutputter outputterWithHandlers = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler, declHandler);
        assertEquals(contentHandler, outputterWithHandlers.getContentHandler());
        assertEquals(errorHandler, outputterWithHandlers.getErrorHandler());
        assertEquals(dtdHandler, outputterWithHandlers.getDTDHandler());
        assertEquals(entityResolver, outputterWithHandlers.getEntityResolver());
        assertEquals(lexicalHandler, outputterWithHandlers.getLexicalHandler());
        assertEquals(declHandler, outputterWithHandlers.getDeclHandler());
        assertFalse(outputterWithHandlers.getReportNamespaceDeclarations());
        assertTrue(outputterWithHandlers.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, outputterWithHandlers.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), outputterWithHandlers.getFormat());

        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        Format format = mock(Format.class);
        SAXOutputter outputterWithProcessor = new SAXOutputter(processor, format, contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler, declHandler);
        assertEquals(processor, outputterWithProcessor.getSAXOutputProcessor());
        assertEquals(format, outputterWithProcessor.getFormat());
        assertEquals(contentHandler, outputterWithProcessor.getContentHandler());
        assertEquals(errorHandler, outputterWithProcessor.getErrorHandler());
        assertEquals(dtdHandler, outputterWithProcessor.getDTDHandler());
        assertEquals(entityResolver, outputterWithProcessor.getEntityResolver());
        assertEquals(lexicalHandler, outputterWithProcessor.getLexicalHandler());
        assertEquals(declHandler, outputterWithProcessor.getDeclHandler());
        assertFalse(outputterWithProcessor.getReportNamespaceDeclarations());
        assertTrue(outputterWithProcessor.getReportDTDEvents());
    }

    @Test
    public void testSetters() {
        SAXOutputter outputter = new SAXOutputter();

        ContentHandler contentHandler = mock(ContentHandler.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        DTDHandler dtdHandler = mock(DTDHandler.class);
        EntityResolver entityResolver = mock(EntityResolver.class);
        LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        DeclHandler declHandler = mock(DeclHandler.class);

        outputter.setContentHandler(contentHandler);
        assertEquals(contentHandler, outputter.getContentHandler());

        outputter.setErrorHandler(errorHandler);
        assertEquals(errorHandler, outputter.getErrorHandler());

        outputter.setDTDHandler(dtdHandler);
        assertEquals(dtdHandler, outputter.getDTDHandler());

        outputter.setEntityResolver(entityResolver);
        assertEquals(entityResolver, outputter.getEntityResolver());

        outputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, outputter.getLexicalHandler());

        outputter.setDeclHandler(declHandler);
        assertEquals(declHandler, outputter.getDeclHandler());

        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());

        outputter.setReportNamespaceDeclarations(false);
        assertFalse(outputter.getReportNamespaceDeclarations());

        outputter.setReportDTDEvents(false);
        assertFalse(outputter.getReportDTDEvents());

        outputter.setReportDTDEvents(true);
        assertTrue(outputter.getReportDTDEvents());

        SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        Format format = mock(Format.class);
        outputter.setSAXOutputProcessor(processor);
        assertEquals(processor, outputter.getSAXOutputProcessor());

        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testSetFeaturesAndProperties() throws SAXNotRecognizedException, SAXNotSupportedException {
        SAXOutputter outputter = new SAXOutputter();

        outputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        assertTrue(outputter.getReportNamespaceDeclarations());

        outputter.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
        assertFalse(outputter.getReportNamespaceDeclarations());

        outputter.setFeature("http://xml.org/sax/features/validation", true);
        assertTrue(outputter.getReportDTDEvents());

        outputter.setFeature("http://xml.org/sax/features/validation", false);
        assertFalse(outputter.getReportDTDEvents());

        outputter.setProperty("http://xml.org/sax/properties/declaration-handler", mock(DeclHandler.class));
        assertEquals(mock(DeclHandler.class), outputter.getDeclHandler());

        outputter.setProperty("http://xml.org/sax/properties/lexical-handler", mock(LexicalHandler.class));
        assertEquals(mock(LexicalHandler.class), outputter.getLexicalHandler());
    }

    @Test
    public void testOutputDocument() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(document);
        // Add assertions here for the generated SAX events
    }

    @Test
    public void testOutputList() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        Element root = document.getRootElement();
        root.addContent(new Element("child1"));
        root.addContent(new Element("child2"));
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(root.getContent());
        // Add assertions here for the generated SAX events
    }

    @Test
    public void testOutputElement() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        Element root = document.getRootElement();
        Element child = new Element("child");
        root.addContent(child);
        SAXOutputter outputter = new SAXOutputter();
        outputter.output(child);
        // Add assertions here for the generated SAX events
    }

    @Test
    public void testOutputFragment() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        Element root = document.getRootElement();
        Element child = new Element("child");
        root.addContent(child);
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(child);
        // Add assertions here for the generated SAX events
    }
}