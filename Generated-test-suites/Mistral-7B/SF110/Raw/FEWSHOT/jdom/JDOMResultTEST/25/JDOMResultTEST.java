package org.jdom2.transform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;
import org.jdom2.input.sax.AbstractSAXBuilder;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

    @Test
    public void testConstructor() {
        JDOMResult jdomResult = new JDOMResult();
        assertNotNull(jdomResult);
        assertNull(jdomResult.getResult());
        assertFalse(jdomResult.isQueried());
        assertNull(jdomResult.getFactory());
    }

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = Collections.singletonList(new Element("result"));
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        assertEquals(document, jdomResult.getDocument());
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetDocument_EmptyResult() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        Document returnedDocument = jdomResult.getDocument();
        assertEquals(document, returnedDocument);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetDocument_NonEmptyResult() throws SAXException, JDOMException {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        AbstractSAXBuilder builder = new AbstractSAXBuilder(factory);
        builder.setContentHandler(new JDOMResult.FragmentHandler(factory));
        builder.parse("test.xml"); // Assuming there is a valid XML file named test.xml
        List<Content> result = jdomResult.getResult();
        assertFalse(result.isEmpty());
        Document document = jdomResult.getDocument();
        assertEquals(result.get(0), document.getRootElement());
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testGetFactory_NullFactory() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(null);
        assertNull(jdomResult.getFactory());
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult jdomResult = new JDOMResult();
        DocumentBuilder documentBuilder = jdomResult.new DocumentBuilder();
        documentBuilder.startDocument();
        documentBuilder.startElement("root", null, null, null);
        documentBuilder.characters("Test string".toCharArray(), 0, "Test string".length());
        documentBuilder.endElement();
        documentBuilder.endDocument();
        List<Content> result = jdomResult.getResult();
        assertEquals(1, result.size());
        Content content = result.get(0);
        assertTrue(content instanceof Element);
        Element element = (Element) content;
        assertEquals("root", element.getName());
        assertEquals("Test string", element.getTextNormalize());
    }
}