package org.jdom2.transform;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.transform.JDOMResult;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

    @Test
    public void testJDOMResultInstance() {
        JDOMResult jdomResult = new JDOMResult();
        assertNotNull(jdomResult);
        assertNull(jdomResult.getDocument());
        assertNull(jdomResult.getFactory());
        assertFalse(jdomResult.isQueried());
        assertNull(jdomResult.getResult());
    }

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        jdomResult.setResult(result);
        assertEquals(result, jdomResult.getResult());
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new SAXBuilder().buildDocument(new StringReader(""));
        jdomResult.setDocument(document);
        assertSame(document, jdomResult.getDocument());
    }

    @Test
    public void testGetDocument() throws SAXException {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        DocumentBuilder documentBuilder = jdomResult.new DocumentBuilder();
        documentBuilder.startDocument();
        documentBuilder.startElement("root", null, null, null);
        documentBuilder.endElement();
        documentBuilder.endDocument();
        jdomResult.setFactory(factory);
        jdomResult.setResult(documentBuilder.getResult());
        Document doc = jdomResult.getDocument();
        assertEquals("root", doc.getRootElement().getName());
    }

    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        jdomResult.setFactory(factory);
        assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.retrieveResult();
        assertNull(jdomResult.getDocument());
        assertNull(jdomResult.getResult());
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetResultWhenResultdocNotNull() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new SAXBuilder().buildDocument(new StringReader(""));
        jdomResult.setDocument(document);
        List<Content> result = jdomResult.getResult();
        assertNotNull(result);
        assertEquals(document.getContent(), result);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetResultWhenResultlistNotNull() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        jdomResult.setResult(result);
        List<Content> nodes = jdomResult.getResult();
        assertNotNull(nodes);
        assertEquals(result, nodes);
        assertTrue(jdomResult.isQueried());
    }
}