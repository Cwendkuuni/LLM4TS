package org.jdom2.output.test;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.*;
import java.util.*;

import org.junit.Test;

/**
 * SAXOutputter test class.
 */
public class SAXOutputterTest {

    

    /**
     * Test constructor without any arguments.
     */
    @Test
    public void testSAXOutputterDefaultConstructor() {
        final SAXOutputter out = new SAXOutputter();
        assertNotNull(out);
    }

    /**
     * Test constructor with ContentHandler argument.
     */
    @Test
    public void testSAXOutputterContentHandlerConstructor() {
        final ContentHandler handler = new Handler();
        final SAXOutputter out = new SAXOutputter(handler);
        assertNotNull(out);
        assertEquals(handler, out.getContentHandler());
    }

    /**
     * Test constructor with ContentHandler, ErrorHandler, DTDHandler and EntityResolver arguments.
     */
    @Test
    public void testSAXOutputterAllHandlersConstructor() {
        final ContentHandler contentHandler = new Handler();
        final ErrorHandler errorHandler = new Handler();
        final DTDHandler dtdHandler = new Handler();
        final EntityResolver entityResolver = new Handler();
        final SAXOutputter out = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver);
        assertNotNull(out);
        assertEquals(contentHandler, out.getContentHandler());
        assertEquals(errorHandler, out.getErrorHandler());
        assertEquals(dtdHandler, out.getDTDHandler());
        assertEquals(entityResolver, out.getEntityResolver());
    }

    /**
     * Test constructor with all arguments.
     */
    @Test
    public void testSAXOutputterAllArgumentsConstructor() {
        final ContentHandler contentHandler = new Handler();
        final ErrorHandler errorHandler = new Handler();
        final DTDHandler dtdHandler = new Handler();
        final EntityResolver entityResolver = new Handler();
        final LexicalHandler lexicalHandler = new Handler();
        final Format format = new DefaultFormat();
        final SAXOutputProcessor processor = new SAXOutputProcessor();
        final SAXOutputter out = new SAXOutputter(processor, format, contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler);
        assertNotNull(out);
        assertEquals(processor, out.getSAXOutputProcessor());
        assertEquals(format, out.getFormat());
        assertEquals(contentHandler, out.getContentHandler());
        assertEquals(errorHandler, out.getErrorHandler());
        assertEquals(dtdHandler, out.getDTDHandler());
        assertEquals(entityResolver, out.getEntityResolver());
        assertEquals(lexicalHandler, out.getLexicalHandler());
    }

    /**
     * Test setters for ContentHandler.
     */
    @Test
    public void testSetGetContentHandler() {
        final ContentHandler handler = new Handler();
        final SAXOutputter out = new SAXOutputter();
        assertNull(out.getContentHandler());
        out.setContentHandler(handler);
        assertEquals(handler, out.getContentHandler());
    }

    /**
     * Test setters for ErrorHandler.
     */
    @Test
    public void testSetGetErrorHandler() {
        final ErrorHandler handler = new Handler();
        final SAXOutputter out = new SAXOutputter();
        assertNull(out.getErrorHandler());
        out.setErrorHandler(handler);
        assertEquals(handler, out.getErrorHandler());
    }

    /**
     * Test setters for DTDHandler.
     */
    @Test
    public void testSetGetDTDHandler() {
        final DTDHandler handler = new Handler();
        final SAXOutputter out = new SAXOutputter();
        assertNull(out.getDTDHandler());
        out.setDTDHandler(handler);
        assertEquals(handler, out.getDTDHandler());
    }

    /**
     * Test setters for EntityResolver.
     */
    @Test
    public void testSetGetEntityResolver() {
        final EntityResolver resolver = new Handler();
        final SAXOutputter out = new SAXOutputter();
        assertNull(out.getEntityResolver());
        out.setEntityResolver(resolver);
        assertEquals(resolver, out.getEntityResolver());
    }

    /**
     * Test setters for LexicalHandler.
     */
    @Test
    public void testSetGetLexicalHandler() {
        final LexicalHandler handler = new Handler();
        final SAXOutputter out = new SAXOutputter();
        assertNull(out.getLexicalHandler());
        out.setLexicalHandler(handler);
        assertEquals(handler, out.getLexicalHandler());
    }

    /**
     * Test setters for DeclHandler.
     */
    @Test
    public void testSetGetDeclHandler() {
        final DeclHandler handler = new Handler();
        final SAXOutputter out = new SAXOutputter();
        assertNull(out.getDeclHandler());
        out.setDeclHandler(handler);
        assertEquals(handler, out.getDeclHandler());
    }

    /**
     * Test setters for declareNamespaces.
     */
    @Test
    public void testSetGetReportNamespaceDeclarations() {
        final SAXOutputter out = new SAXOutputter();
        assertFalse(out.getReportNamespaceDeclarations());
        out.setReportNamespaceDeclarations(true);
        assertTrue(out.getReportNamespaceDeclarations());
        out.setReportNamespaceDeclarations(false);
        assertFalse(out.getReportNamespaceDeclarations());
    }

    /**
     * Test setters for reportDtdEvents.
     */
    @Test
    public void testSetGetReportDTDEvents() {
        final SAXOutputter out = new SAXOutputter();
        assertTrue(out.getReportDTDEvents());
        out.setReportDTDEvents(false);
        assertFalse(out.getReportDTDEvents());
        out.setReportDTDEvents(true);
        assertTrue(out.getReportDTDEvents());
    }

    /**
     * Test setters for SAXOutputProcessor.
     */
    @Test
    public void testSetGetSAXOutputProcessor() {
        final SAXOutputProcessor processor = new SAXOutputProcessor();
        final SAXOutputter out = new SAXOutputter();
        assertNull(out.getSAXOutputProcessor());
        out.setSAXOutputProcessor(processor);
        assertEquals(processor, out.getSAXOutputProcessor());
    }

    /**
     * Test setters for Format.
     */
    @Test
    public void testSetGetFormat() {
        final Format format = new DefaultFormat();
        final SAXOutputter out = new SAXOutputter();
        assertNotNull(out.getFormat());
        out.setFormat(format);
        assertEquals(format, out.getFormat());
    }

    /**
     * Test setFeature.
     */
    @Test
    public void testSetFeature() {
        final SAXOutputter out = new SAXOutputter();
        assertThrows(SAXNotRecognizedException.class, () -> out.setFeature("http://xml.org/sax/features/invalid-feature", true));
        assertThrows(SAXNotSupportedException.class, () -> out.setFeature("http://xml.org/sax/features/namespaces", false));
        assertThrows(SAXNotRecognizedException.class, () -> out.setFeature("http://xml.org/sax/features/validation", false));
        assertThrows(SAXNotRecognizedException.class, () -> out.setFeature("http://xml.org/sax/features/unknown-feature", true));
        out.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        assertTrue(out.getReportNamespaceDeclarations());
    }

    /**
     * Test getFeature.
     */
    @Test
    public void testGetFeature() {
        final SAXOutputter out = new SAXOutputter();
        assertFalse(out.getReportNamespaceDeclarations());
        assertFalse(out.getReportDTDEvents());
        assertThrows(SAXNotRecognizedException.class, () -> out.getFeature("http://xml.org/sax/features/invalid-feature"));
        out.setReportNamespaceDeclarations(true);
        assertTrue(out.getReportNamespaceDeclarations());
        assertTrue(out.getReportDTDEvents());
    }

    /**
     * Test setProperty.
     */
    @Test
    public void testSetProperty() {
        final SAXOutputter out = new SAXOutputter();
        assertThrows(SAXNotRecognizedException.class, () -> out.setProperty("http://xml.org/sax/properties/invalid-property", new Object()));
        assertThrows(SAXNotRecognizedException.class, () -> out.setProperty("http://xml.org/sax/properties/lexical-handler", new Object()));
        out.setProperty("http://xml.org/sax/properties/declaration-handler", new Handler());
        assertEquals(new Handler(), out.getDeclHandler());
    }

    /**
     * Test getProperty.
     */
    @Test
    public void testGetProperty() {
        final SAXOutputter out = new SAXOutputter();
        assertNull(out.getDeclHandler());
        out.setDeclHandler(new Handler());
        assertEquals(new Handler(), out.getDeclHandler());
        assertThrows(SAXNotRecognizedException.class, () -> out.getProperty("http://xml.org/sax/properties/invalid-property"));
        assertNull(out.getProperty("http://xml.org/sax/properties/lexical-handler"));
    }

    /**
     * Test output method for Document.
     */
    @Test
    public void testOutputDocument() throws Exception {
        final Document doc = new JDOMFactory().newDocument();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final SAXOutputter out = new SAXOutputter();
        out.setContentHandler(new Handler());
        out.output(doc);
        final String xmlString = outputStream.toString();
        assertTrue(xmlString.length() > 0);
    }

    /**
     * Test output method for List of Content.
     */
    @Test
    public void testOutputList() throws Exception {
        final Element root = new JDOMFactory().newElement("root");
        final Element child1 = new JDOMFactory().newElement("child1");
        final Element child2 = new JDOMFactory().newElement("child2");
        root.addContent(child1);
        root.addContent(child2);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final SAXOutputter out = new SAXOutputter();
        out.setContentHandler(new Handler());
        out.output(new JDOMFactory().newDocument(root));
        final String xmlString = outputStream.toString();
        assertTrue(xmlString.length() > 0);
    }

    /**
     * Test output method for Element.
     */
    @Test
    public void testOutputElement() throws Exception {
        final Element element = new JDOMFactory().newElement("element");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final SAXOutputter out = new SAXOutputter();
        out.setContentHandler(new Handler());
        out.output(new JDOMFactory().newDocument(element));
        final String xmlString = outputStream.toString();
        assertTrue(xmlString.length() > 0);
    }

    /**
     * Test outputFragment method for List of Content.
     */
    @Test
    public void testOutputFragmentList() throws Exception {
        final Element root = new JDOMFactory().newElement("root");
        final Element child1 = new JDOMFactory().newElement("child1");
        final Element child2 = new JDOMFactory().newElement("child2");
        root.addContent(child1);
        root.addContent(child2);
        final SAXOutputter out = new SAXOutputter();
        out.outputFragment(new JDOMFactory().newDocument(root).getContent());
    }

    /**
     * Test outputFragment method for Content.
     */
    @Test
    public void testOutputFragmentContent() throws Exception {
        final Element child = new JDOMFactory().newElement("child");
        final SAXOutputter out = new SAXOutputter();
        out.outputFragment(child);
    }

    /**
     * Test handleError method.
     */
    @Test
    public void testHandleError() throws Exception {
        final SAXOutputter out = new SAXOutputter();
        assertThrows(JDOMException.class, () -> out.handleError(new JDOMException("Invalid document")));
    }

    
}