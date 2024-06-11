package org.jdom2.output; // Import necessary packages

import org.jdom2.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SAXOutputterTest {
    // Test variables
    SAXOutputter saxOutputter;
    Document document;
    SAXParser parser;
    DefaultHandler defaultHandler;

    @Before
    public void setup() {
        // Initialize JDOM and SAXParser
        JDOMFactory factory = JDOMFactory.instance();
        parser = new SAXParser();
        defaultHandler = new DefaultHandler();
    }

    

    // Test constructor without arguments
    @Test
    public void testSAXOutputterConstructor() {
        saxOutputter = new SAXOutputter();
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

    // Test constructor with ContentHandler argument
    @Test
    public void testSAXOutputterContentHandlerConstructor() {
        saxOutputter = new SAXOutputter(defaultHandler);
        assertEquals(defaultHandler, saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }

    // Test setter methods for handlers and processors
    @Test
    public void testSetters() {
        saxOutputter = new SAXOutputter();

        ContentHandler testContentHandler = new ContentHandler() {
            @Override
            public void startDocument() throws SAXException {
            }

            @Override
            public void endDocument() throws SAXException {
            }

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
            }
        };

        ErrorHandler testErrorHandler = new ErrorHandler() {
            @Override
            public void error(SAXParseException exception) throws SAXException {
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
            }

            @Override
            public void warning(SAXParseException exception) throws SAXException {
            }
        };

        DTDHandler testDTDHandler = new DefaultDTDHandler();
        EntityResolver testEntityResolver = new DefaultEntityResolver();
        LexicalHandler testLexicalHandler = new DefaultLexicalHandler();
        DeclHandler testDeclHandler = new DefaultDeclHandler();
        SAXOutputProcessor testSAXOutputProcessor = new DefaultSAXOutputProcessor();
        Format testFormat = new DocumentFormat("test.xml");

        saxOutputter.setContentHandler(testContentHandler);
        assertEquals(testContentHandler, saxOutputter.getContentHandler());

        saxOutputter.setErrorHandler(testErrorHandler);
        assertEquals(testErrorHandler, saxOutputter.getErrorHandler());

        saxOutputter.setDTDHandler(testDTDHandler);
        assertEquals(testDTDHandler, saxOutputter.getDTDHandler());

        saxOutputter.setEntityResolver(testEntityResolver);
        assertEquals(testEntityResolver, saxOutputter.getEntityResolver());

        saxOutputter.setLexicalHandler(testLexicalHandler);
        assertEquals(testLexicalHandler, saxOutputter.getLexicalHandler());

        saxOutputter.setDeclHandler(testDeclHandler);
        assertEquals(testDeclHandler, saxOutputter.getDeclHandler());

        saxOutputter.setSAXOutputProcessor(testSAXOutputProcessor);
        assertEquals(testSAXOutputProcessor, saxOutputter.getSAXOutputProcessor());

        saxOutputter.setFormat(testFormat);
        assertEquals(testFormat, saxOutputter.getFormat());
    }

    // Test output method with Document
    @Test
    public void testOutputDocument() throws JDOMException, SAXException {
        document = parser.parse(getClass().getResource("/test.xml"));
        saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler(defaultHandler);
        saxOutputter.output(document);
    }

    // Test output method with List of Content
    @Test
    public void testOutputListContent() throws JDOMException, SAXException {
        Document doc = parser.newDocument();
        Element root = doc.getRootElement();
        root.addContent(new Element("test"));
        root.addContent(new Text("Hello World"));

        saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler(defaultHandler);
        List<Content> contentList = doc.getRootElement().content();
        saxOutputter.output(contentList);
    }

    // Test output method with Element
    @Test
    public void testOutputElement() throws JDOMException, SAXException {
        Document doc = parser.newDocument();
        Element root = doc.getRootElement();
        root.addContent(new Element("test"));

        saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler(defaultHandler);
        saxOutputter.output(root);
    }

    // Test outputFragment method with List of Content
    @Test
    public void testOutputFragmentListContent() throws JDOMException, SAXException {
        Document doc = parser.newDocument();
        Element root = doc.getRootElement();
        root.addContent(new Element("test"));
        root.addContent(new Text("Hello World"));

        saxOutputter = new SAXOutputter();
        List<Content> contentList = doc.getRootElement().content();
        saxOutputter.outputFragment(contentList);
    }

    // Test outputFragment method with Content
    @Test
    public void testOutputFragmentContent() throws JDOMException, SAXException {
        Document doc = parser.newDocument();
        Element root = doc.getRootElement();
        Text text = new Text("Hello World");

        saxOutputter = new SAXOutputter();
        saxOutputter.outputFragment(text);
    }

    // Test setting and getting features
    @Test
    public void testFeatures() throws SAXNotRecognizedException, SAXNotSupportedException, JDOMException, SAXException {
        saxOutputter = new SAXOutputter();

        saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        assertTrue(saxOutputter.getFeature("http://xml.org/sax/features/namespace-prefixes"));

        saxOutputter.setFeature("http://xml.org/sax/features/validation", false);
        assertFalse(saxOutputter.getFeature("http://xml.org/sax/features/validation"));

        try {
            saxOutputter.setFeature("http://xml.org/sax/features/namespaces", false);
            fail("Invalid feature name");
        } catch (SAXNotRecognizedException e) {
            // Expected
        }

        try {
            saxOutputter.setFeature("http://xml.org/sax/features/validation", true);
            fail("Invalid feature name");
        } catch (SAXNotSupportedException e) {
            // Expected
        }
    }

    // Test setting and getting properties
    @Test
    public void testProperties() throws SAXNotRecognizedException, SAXNotSupportedException, JDOMException, SAXException {
        saxOutputter = new SAXOutputter();

        LexicalHandler testLexicalHandler = new DefaultLexicalHandler();
        DeclHandler testDeclHandler = new DefaultDeclHandler();

        saxOutputter.setProperty("http://xml.org/sax/properties/lexical-handler", testLexicalHandler);
        assertEquals(testLexicalHandler, saxOutputter.getProperty("http://xml.org/sax/properties/lexical-handler"));

        saxOutputter.setProperty("http://xml.org/sax/properties/declaration-handler", testDeclHandler);
        assertEquals(testDeclHandler, saxOutputter.getProperty("http://xml.org/sax/properties/declaration-handler"));

        try {
            saxOutputter.setProperty("http://xml.org/sax/properties/nonExistentProperty", null);
            fail("Invalid property name");
        } catch (SAXNotRecognizedException e) {
            // Expected
        }
    }

    // Test getting SAXOutputProcessor and Format
    @Test
    public void testGetters() {
        saxOutputter = new SAXOutputter();

        assertInstanceOf(DefaultSAXOutputProcessor.class, saxOutputter.getSAXOutputProcessor());
        assertInstanceOf(Format.class, saxOutputter.getFormat());
    }

    
}