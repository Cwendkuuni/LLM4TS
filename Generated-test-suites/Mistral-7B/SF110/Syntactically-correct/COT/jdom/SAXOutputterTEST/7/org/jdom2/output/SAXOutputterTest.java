package org.jdom2.output; // Import necessary packages

import static org.junit.Assert.*;

import org.jdom2.*;
import org.jdom2.output.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.xml.sax.ext.*;
import org.xml.sax.Attributes;
import org.junit.*;

import java.io.*;
import java.util.*;

public class SAXOutputterTest {
    

    // Test constructor without arguments
    @Test
    public void testSAXOutputterDefaultConstructor() {
        final SAXOutputter saxOutputter = new SAXOutputter();
        assertNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertNotNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
    }

    // Test constructor with ContentHandler argument
    @Test
    public void testSAXOutputterContentHandlerConstructor() {
        final ContentHandler handler = mock(ContentHandler.class);
        final SAXOutputter saxOutputter = new SAXOutputter(handler);
        assertEquals(handler, saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertNotNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
    }

    // Test setter methods for handlers and processors
    @Test
    public void testSAXOutputterSetters() {
        final ContentHandler handler = mock(ContentHandler.class);
        final ErrorHandler errorHandler = mock(ErrorHandler.class);
        final DTDHandler dtdHandler = mock(DTDHandler.class);
        final EntityResolver entityResolver = mock(EntityResolver.class);
        final LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        final DeclHandler declHandler = mock(DeclHandler.class);

        final SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler(handler);
        assertEquals(handler, saxOutputter.getContentHandler());

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

        final SAXOutputProcessor processor = mock(SAXOutputProcessor.class);
        saxOutputter.setSAXOutputProcessor(processor);
        assertEquals(processor, saxOutputter.getSAXOutputProcessor());

        final Format format = new DefaultFormat();
        saxOutputter.setFormat(format);
        assertEquals(format, saxOutputter.getFormat());
    }

    // Test output method with Document argument
    @Test
    public void testOutputDocument() throws Exception {
        final Document document = new JDOMFactory().createDocument();
        final ContentHandler handler = mock(ContentHandler.class);
        final SAXOutputter saxOutputter = new SAXOutputter(handler);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        saxOutputter.setOutput(outputStream);
        saxOutputter.setFormat(new PrettyFormat());
        saxOutputter.output(document);

        final String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<document>\n</document>";
        assertEquals(expectedOutput, outputStream.toString());
    }

    // Test output method with List<Content> argument
    @Test
    public void testOutputListContent() throws Exception {
        final JDOMFactory factory = new JDOMFactory();
        final List<Content> nodes = new ArrayList<>();
        nodes.add(factory.createElement("root"));
        nodes.add(factory.createText("Some text"));

        final ContentHandler handler = mock(ContentHandler.class);
        final SAXOutputter saxOutputter = new SAXOutputter(handler);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        saxOutputter.setOutput(outputStream);
        saxOutputter.setFormat(new PrettyFormat());

        saxOutputter.output(nodes);

        final String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\nSome text\n</root>";
        assertEquals(expectedOutput, outputStream.toString());
    }

    // Test output method with Element argument
    @Test
    public void testOutputElement() throws Exception {
        final JDOMFactory factory = new JDOMFactory();
        final Element root = factory.createElement("root");

        final ContentHandler handler = mock(ContentHandler.class);
        final SAXOutputter saxOutputter = new SAXOutputter(handler);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        saxOutputter.setOutput(outputStream);
        saxOutputter.setFormat(new PrettyFormat());

        saxOutputter.output(root);

        final String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>";
        assertEquals(expectedOutput, outputStream.toString());
    }

    // Test outputFragment method with List<Content> argument
    @Test
    public void testOutputFragmentListContent() throws Exception {
        final JDOMFactory factory = new JDOMFactory();
        final List<Content> nodes = new ArrayList<>();
        nodes.add(factory.createElement("root"));
        nodes.add(factory.createText("Some text"));

        final SAXOutputter saxOutputter = new SAXOutputter();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        saxOutputter.setOutput(outputStream);
        saxOutputter.setFormat(new PrettyFormat());

        saxOutputter.outputFragment(nodes);

        final String expectedOutput = "<root>Some text</root>";
        assertEquals(expectedOutput, outputStream.toString());
    }

    // Test outputFragment method with Content argument
    @Test
    public void testOutputFragmentContent() throws Exception {
        final JDOMFactory factory = new JDOMFactory();
        final Element root = factory.createElement("root");

        final SAXOutputter saxOutputter = new SAXOutputter();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        saxOutputter.setOutput(outputStream);
        saxOutputter.setFormat(new PrettyFormat());

        saxOutputter.outputFragment(root);

        final String expectedOutput = "<root/>";
        assertEquals(expectedOutput, outputStream.toString());
    }

    // Test error handling when document is null in output method
    @Test(expected = JDOMException.class)
    public void testOutputDocumentNull() throws Exception {
        final SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.output(null);
    }

    // Test error handling when nodes are null in output method
    @Test(expected = JDOMException.class)
    public void testOutputListContentNull() throws Exception {
        final SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.output(null);
    }

    // Test error handling when node is null in outputFragment method
    @Test(expected = JDOMException.class)
    public void testOutputFragmentContentNull() throws Exception {
        final SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.outputFragment(null);
    }

    // Test setting and getting features
    @Test
    public void testSetAndGetFeatures() throws Exception {
        final SAXOutputter saxOutputter = new SAXOutputter();

        saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        assertTrue(saxOutputter.getFeature("http://xml.org/sax/features/namespace-prefixes"));

        saxOutputter.setFeature("http://xml.org/sax/features/validation", true);
        assertTrue(saxOutputter.getFeature("http://xml.org/sax/features/validation"));

        saxOutputter.setFeature("http://xml.org/sax/features/namespaces", false);
        assertFalse(saxOutputter.getFeature("http://xml.org/sax/features/namespaces"));

        try {
            saxOutputter.setFeature("http://xml.org/sax/features/namespaces", true);
            fail("SAXNotSupportedException expected");
        } catch (SAXNotSupportedException e) {
            // Expected
        }

        try {
            saxOutputter.setFeature("http://xml.org/sax/features/validation2", true);
            fail("SAXNotRecognizedException expected");
        } catch (SAXNotRecognizedException e) {
            // Expected
        }
    }

    // Test setting and getting properties
    @Test
    public void testSetAndGetProperties() throws Exception {
        final SAXOutputter saxOutputter = new SAXOutputter();

        final LexicalHandler lexicalHandler = mock(LexicalHandler.class);
        saxOutputter.setProperty("http://xml.org/sax/properties/lexical-handler", lexicalHandler);
        assertEquals(lexicalHandler, saxOutputter.getProperty("http://xml.org/sax/properties/lexical-handler"));

        final DeclHandler declHandler = mock(DeclHandler.class);
        saxOutputter.setProperty("http://xml.org/sax/properties/declaration-handler", declHandler);
        assertEquals(declHandler, saxOutputter.getProperty("http://xml.org/sax/properties/declaration-handler"));

        try {
            saxOutputter.setProperty("http://xml.org/sax/properties/invalidProperty", 1);
            fail("SAXNotRecognizedException expected");
        } catch (SAXNotRecognizedException e) {
            // Expected
        }
    }

    // Test getLocator method (deprecated)
    @Test
    public void testGetLocator() {
        final SAXOutputter saxOutputter = new SAXOutputter();
        assertNull(saxOutputter.getLocator());
    }

    
}