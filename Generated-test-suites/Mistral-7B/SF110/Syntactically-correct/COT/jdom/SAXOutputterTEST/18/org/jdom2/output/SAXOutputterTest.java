package org.jdom2.output; // make sure to import necessary classes at the beginning

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.support.SAXTarget;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class SAXOutputterTest {

    private SAXOutputter saxOutputter;
    private DefaultHandler handler;

    @Before
    public void setup() {
        saxOutputter = new SAXOutputter();
        handler = new DefaultHandler();
    }

    

    // Test constructor without any argument
    @Test
    public void testDefaultConstructor() {
        saxOutputter = new SAXOutputter();
        assertNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
    }

    // Test constructor with ContentHandler argument
    @Test
    public void testContentHandlerConstructor() {
        saxOutputter = new SAXOutputter(handler);
        assertEquals(handler, saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
    }

    // Test setter methods for handlers and processors
    @Test
    public void testSetters() {
        SAXOutputProcessor processor = new DefaultSAXOutputProcessor();
        Format format = Format.getRawFormat();
        ContentHandler customContentHandler = new DefaultHandler();
        ErrorHandler customErrorHandler = new DefaultHandler();
        DTDHandler customDTDHandler = new DefaultHandler();
        EntityResolver customEntityResolver = new DefaultHandler();
        LexicalHandler customLexicalHandler = new DefaultHandler();
        DeclHandler customDeclHandler = new DefaultHandler();

        saxOutputter = new SAXOutputter();

        saxOutputter.setContentHandler(customContentHandler);
        assertEquals(customContentHandler, saxOutputter.getContentHandler());

        saxOutputter.setErrorHandler(customErrorHandler);
        assertEquals(customErrorHandler, saxOutputter.getErrorHandler());

        saxOutputter.setDTDHandler(customDTDHandler);
        assertEquals(customDTDHandler, saxOutputter.getDTDHandler());

        saxOutputter.setEntityResolver(customEntityResolver);
        assertEquals(customEntityResolver, saxOutputter.getEntityResolver());

        saxOutputter.setLexicalHandler(customLexicalHandler);
        assertEquals(customLexicalHandler, saxOutputter.getLexicalHandler());

        saxOutputter.setDeclHandler(customDeclHandler);
        assertEquals(customDeclHandler, saxOutputter.getDeclHandler());

        saxOutputter.setSAXOutputProcessor(processor);
        assertEquals(processor, saxOutputter.getSAXOutputProcessor());

        saxOutputter.setFormat(format);
        assertEquals(format, saxOutputter.getFormat());
    }

    // Test output method with Document argument
    @Test
    public void testOutputDocument() throws JDOMException, SAXException {
        Document doc = new Document();
        SAXTarget target = saxOutputter.buildTarget(doc);
        saxOutputter.output(doc);
        assertTrue("Output should not be empty", handler.isStartDocument());
        assertTrue("Output should not be empty", handler.isEndDocument());
        assertTrue("Output should contain root element", handler.isStartElement() && handler.getName().equals("root"));
    }

    // Test output method with List<? extends Content> argument
    @Test
    public void testOutputList() throws JDOMException, SAXException {
        List<Element> elements = new ArrayList<>();
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        elements.add(element1);
        elements.add(element2);

        SAXTarget target = saxOutputter.buildTarget(null);
        saxOutputter.output(elements);

        assertTrue("Output should not be empty", handler.isStartDocument());
        assertTrue("Output should not be empty", handler.isEndDocument());
        for (int i = 0; i < elements.size(); i++) {
            assertTrue("Output should contain element " + (i + 1), handler.isStartElement());
            assertEquals("Output should contain correct tag name for element " + (i + 1), elements.get(i).getName(), handler.getName());
            handler.next(); // consume start tag
            assertTrue("Output should contain element content", handler.isCharacters());
            assertEquals("Output should contain correct content for element " + (i + 1), elements.get(i).getText(), handler.getText().trim());
            handler.next(); // consume content
            assertTrue("Output should contain end tag for element " + (i + 1), handler.isEndElement());
            assertEquals("Output should contain correct tag name for end tag of element " + (i + 1), elements.get(i).getName(), handler.getName());
        }
    }

    // Test output method with Element argument
    @Test
    public void testOutputElement() throws JDOMException, SAXException {
        Element element = new Element("element");

        SAXTarget target = saxOutputter.buildTarget(null);
        saxOutputter.output(element);

        assertTrue("Output should not be empty", handler.isStartDocument());
        assertTrue("Output should not be empty", handler.isEndDocument());
        assertTrue("Output should contain start tag for element", handler.isStartElement());
        assertEquals("Output should contain correct tag name for start tag of element", element.getName(), handler.getName());
        handler.next(); // consume start tag
        assertTrue("Output should contain element content", handler.isCharacters());
        assertEquals("Output should contain correct content for element", element.getText(), handler.getText().trim());
        handler.next(); // consume content
        assertTrue("Output should contain end tag for element", handler.isEndElement());
        assertEquals("Output should contain correct tag name for end tag of element", element.getName(), handler.getName());
    }

    // Test outputFragment method with List<? extends Content> argument
    @Test
    public void testOutputFragmentList() throws JDOMException, SAXException {
        List<Element> elements = new ArrayList<>();
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        elements.add(element1);
        elements.add(element2);

        saxOutputter.outputFragment(elements);

        for (int i = 0; i < elements.size(); i++) {
            assertTrue("Output should contain start tag for element " + (i + 1), handler.isStartElement());
            assertEquals("Output should contain correct tag name for start tag of element " + (i + 1), elements.get(i).getName(), handler.getName());
            handler.next(); // consume start tag
            assertTrue("Output should contain element content", handler.isCharacters());
            assertEquals("Output should contain correct content for element " + (i + 1), elements.get(i).getText(), handler.getText().trim());
            handler.next(); // consume content
            assertTrue("Output should contain end tag for element " + (i + 1), handler.isEndElement());
            assertEquals("Output should contain correct tag name for end tag of element " + (i + 1), elements.get(i).getName(), handler.getName());
        }
    }

    // Test outputFragment method with Content argument
    @Test
    public void testOutputFragmentContent() throws JDOMException, SAXException {
        Element element = new Element("element");

        saxOutputter.outputFragment(element);

        assertTrue("Output should contain start tag for element", handler.isStartElement());
        assertEquals("Output should contain correct tag name for start tag of element", element.getName(), handler.getName());
        handler.next(); // consume start tag
        assertTrue("Output should contain element content", handler.isCharacters());
        assertEquals("Output should contain correct content for element", element.getText(), handler.getText().trim());
        handler.next(); // consume content
        assertTrue("Output should contain end tag for element", handler.isEndElement());
        assertEquals("Output should contain correct tag name for end tag of element", element.getName(), handler.getName());
    }

    // Test edge cases and exception handling
    @Test(expected = JDOMException.class)
    public void testOutputNullDocument() throws JDOMException, SAXException {
        saxOutputter.output(null);
    }

    @Test(expected = JDOMException.class)
    public void testOutputNullList() throws JDOMException, SAXException {
        saxOutputter.output(null);
    }

    @Test(expected = JDOMException.class)
    public void testOutputNullElement() throws JDOMException, SAXException {
        saxOutputter.output(null);
    }

    @Test
    public void testOutputFragmentNullList() {
        saxOutputter.outputFragment(null);
        assertTrue("Output should be empty", handler.isEndDocument());
    }

    @Test
    public void testOutputFragmentNullElement() {
        saxOutputter.outputFragment(null);
        assertTrue("Output should be empty", handler.isEndDocument());
    }

    
}