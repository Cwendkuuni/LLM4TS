package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.SAXOutputter;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.StringWriter;
import java.util.*;

public class SAXOutputterTest {

    

    @org.junit.Before
    public void setUp() {
        // Initialize test data
    }

    @org.junit.After
    public void tearDown() {
        // Release test resources
    }

    @org.junit.Test
    public void testSAXOutputterConstructor() {
        new SAXOutputter();
    }

    @org.junit.Test
    public void testSAXOutputterContentHandlerConstructor() {
        new SAXOutputter(new DefaultContentHandler());
    }

    @org.junit.Test
    public void testSAXOutputterContentHandlerErrorHandlerDTDHandlerEntityResolverLexicalHandlerConstructor() {
        new SAXOutputter(new DefaultContentHandler(), new DefaultErrorHandler(), new DefaultDTDHandler(), new DefaultEntityResolver(), new DefaultLexicalHandler());
    }

    @org.junit.Test
    public void testSAXOutputterProcessorFormatContentHandlerErrorHandlerDTDHandlerEntityResolverLexicalHandlerConstructor() {
        new SAXOutputter(new DefaultSAXOutputProcessor(), Format.getRawFormat(), new DefaultContentHandler(), new DefaultErrorHandler(), new DefaultDTDHandler(), new DefaultEntityResolver(), new DefaultLexicalHandler());
    }

    @org.junit.Test
    public void testSetContentHandler() {
        SAXOutputter out = new SAXOutputter();
        ContentHandler handler = new DefaultContentHandler();
        out.setContentHandler(handler);
        assertEquals(handler, out.getContentHandler());
    }

    @org.junit.Test
    public void testGetContentHandler() {
        SAXOutputter out = new SAXOutputter();
        ContentHandler handler = new DefaultContentHandler();
        out.setContentHandler(handler);
        assertEquals(handler, out.getContentHandler());
    }

    @org.junit.Test
    public void testSetErrorHandler() {
        SAXOutputter out = new SAXOutputter();
        ErrorHandler handler = new DefaultErrorHandler();
        out.setErrorHandler(handler);
        assertEquals(handler, out.getErrorHandler());
    }

    @org.junit.Test
    public void testGetErrorHandler() {
        SAXOutputter out = new SAXOutputter();
        ErrorHandler handler = new DefaultErrorHandler();
        out.setErrorHandler(handler);
        assertEquals(handler, out.getErrorHandler());
    }

    @org.junit.Test
    public void testSetDTDHandler() {
        SAXOutputter out = new SAXOutputter();
        DTDHandler handler = new DefaultDTDHandler();
        out.setDTDHandler(handler);
        assertEquals(handler, out.getDTDHandler());
    }

    @org.junit.Test
    public void testGetDTDHandler() {
        SAXOutputter out = new SAXOutputter();
        DTDHandler handler = new DefaultDTDHandler();
        out.setDTDHandler(handler);
        assertEquals(handler, out.getDTDHandler());
    }

    @org.junit.Test
    public void testSetEntityResolver() {
        SAXOutputter out = new SAXOutputter();
        EntityResolver resolver = new DefaultEntityResolver();
        out.setEntityResolver(resolver);
        assertEquals(resolver, out.getEntityResolver());
    }

    @org.junit.Test
    public void testGetEntityResolver() {
        SAXOutputter out = new SAXOutputter();
        EntityResolver resolver = new DefaultEntityResolver();
        out.setEntityResolver(resolver);
        assertEquals(resolver, out.getEntityResolver());
    }

    @org.junit.Test
    public void testSetLexicalHandler() {
        SAXOutputter out = new SAXOutputter();
        LexicalHandler handler = new DefaultLexicalHandler();
        out.setLexicalHandler(handler);
        assertEquals(handler, out.getLexicalHandler());
    }

    @org.junit.Test
    public void testGetLexicalHandler() {
        SAXOutputter out = new SAXOutputter();
        LexicalHandler handler = new DefaultLexicalHandler();
        out.setLexicalHandler(handler);
        assertEquals(handler, out.getLexicalHandler());
    }

    @org.junit.Test
    public void testSetDeclHandler() {
        SAXOutputter out = new SAXOutputter();
        DeclHandler handler = new DefaultDeclHandler();
        out.setDeclHandler(handler);
        assertEquals(handler, out.getDeclHandler());
    }

    @org.junit.Test
    public void testGetDeclHandler() {
        SAXOutputter out = new SAXOutputter();
        DeclHandler handler = new DefaultDeclHandler();
        out.setDeclHandler(handler);
        assertEquals(handler, out.getDeclHandler());
    }

    @org.junit.Test
    public void testSetReportNamespaceDeclarations() {
        SAXOutputter out = new SAXOutputter();
        out.setReportNamespaceDeclarations(true);
        assertTrue(out.getReportNamespaceDeclarations());

        out.setReportNamespaceDeclarations(false);
        assertFalse(out.getReportNamespaceDeclarations());
    }

    @org.junit.Test
    public void testGetReportNamespaceDeclarations() {
        SAXOutputter out = new SAXOutputter();
        out.setReportNamespaceDeclarations(true);
        assertTrue(out.getReportNamespaceDeclarations());

        out.setReportNamespaceDeclarations(false);
        assertFalse(out.getReportNamespaceDeclarations());
    }

    @org.junit.Test(expected = SAXNotRecognizedException.class)
    public void testSetFeatureNamespacePrefixes() {
        SAXOutputter out = new SAXOutputter();
        out.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
    }

    @org.junit.Test
    public void testGetFeatureNamespaces() {
        SAXOutputter out = new SAXOutputter();
        assertTrue(out.getFeature("http://xml.org/sax/features/namespaces"));
    }

    @org.junit.Test(expected = SAXNotRecognizedException.class)
    public void testSetFeatureValidation() {
        SAXOutputter out = new SAXOutputter();
        out.setFeature("http://xml.org/sax/features/validation", false);
    }

    @org.junit.Test(expected = SAXNotRecognizedException.class)
    public void testGetFeatureInvalidName() {
        SAXOutputter out = new SAXOutputter();
        out.getFeature("http://xml.org/sax/features/invalid");
    }

    @org.junit.Test(expected = SAXNotSupportedException.class)
    public void testSetFeatureValidationNotSupported() {
        SAXOutputter out = new SAXOutputter();
        out.setFeature("http://xml.org/sax/features/validation", false);
    }

    @org.junit.Test
    public void testSetPropertyLexicalHandler() {
        SAXOutputter out = new SAXOutputter();
        LexicalHandler handler = new DefaultLexicalHandler();
        out.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
        assertEquals(handler, out.getProperty("http://xml.org/sax/properties/lexical-handler"));
    }

    @org.junit.Test(expected = SAXNotRecognizedException.class)
    public void testSetPropertyInvalidName() {
        SAXOutputter out = new SAXOutputter();
        out.setProperty("http://xml.org/sax/properties/invalid", new Object());
    }

    @org.junit.Test
    public void testGetFormat() {
        SAXOutputter out = new SAXOutputter();
        assertEquals(Format.getRawFormat(), out.getFormat());
    }

    @org.junit.Test
    public void testSetFormat() {
        SAXOutputter out = new SAXOutputter();
        Format format = new DefaultFormat();
        out.setFormat(format);
        assertEquals(format, out.getFormat());
    }

    @org.junit.Test
    public void testOutputDocument() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        StringWriter writer = new StringWriter();
        SAXOutputter out = new SAXOutputter();
        out.setContentHandler(new StringWriterContentHandler(writer));
        out.output(document);
        assertEquals("<document/>", writer.toString());
    }

    @org.junit.Test
    public void testOutputListContent() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        Element root = new Element("root");
        document.setRootElement(root);
        Content content = new Text("Test");
        root.addContent(content);
        StringWriter writer = new StringWriter();
        SAXOutputter out = new SAXOutputter();
        out.setContentHandler(new StringWriterContentHandler(writer));
        out.output(new JDOMFactory().createList(content));
        assertEquals("Test", writer.toString());
    }

    @org.junit.Test
    public void testOutputElement() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        Element root = new Element("root");
        document.setRootElement(root);
        Element element = new Element("element");
        root.addContent(element);
        StringWriter writer = new StringWriter();
        SAXOutputter out = new SAXOutputter();
        out.setContentHandler(new StringWriterContentHandler(writer));
        out.output(element);
        assertEquals("<element/>", writer.toString());
    }

    @org.junit.Test
    public void testOutputFragmentListContent() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        Element root = new Element("root");
        document.setRootElement(root);
        Content content = new Text("Test");
        root.addContent(content);
        SAXOutputter out = new SAXOutputter();
        out.setContentHandler(new StringWriterContentHandler(new StringWriter()));
        out.outputFragment(new JDOMFactory().createList(content));
        // Verify that the outputHandler was not called
    }

    @org.junit.Test
    public void testOutputFragmentElement() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        Element root = new Element("root");
        document.setRootElement(root);
        Element element = new Element("element");
        root.addContent(element);
        SAXOutputter out = new SAXOutputter();
        out.setContentHandler(new StringWriterContentHandler(new StringWriter()));
        out.outputFragment(element);
        // Verify that the outputHandler was not called
    }

    @org.junit.Test(expected = JDOMException.class)
    public void testOutputFragmentInvalidContent() throws JDOMException {
        Document document = new JDOMFactory().newDocument();
        Element root = new Element("root");
        document.setRootElement(root);
        SAXOutputter out = new SAXOutputter();
        out.setContentHandler(new StringWriterContentHandler(new StringWriter()));
        out.outputFragment(new JDOMFactory().newDocument());
    }

    @org.junit.Test
    public void testHandleError() throws JDOMException {
        JDOMException exception = new JDOMException("Test Exception");
        SAXOutputter out = new SAXOutputter();
        out.handleError(exception);
        fail("An error should have been thrown from the errorHandler.");
    }

    
}