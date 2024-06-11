package org.jdom2.transform;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class JDOMResultTest {

    @Test
    public void testJDOMResultConstruction() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getResult());
        assertNull(result.getDocument());
        assertNull(result.getFactory());
        assertFalse(result.isQueried());
    }

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = new ArrayList<>();
        result.setResult(content);
        assertEquals(content, result.getResult());
        assertFalse(result.isQueried());
    }

    @Test
    public void testGetResultEmpty() {
        JDOMResult result = new JDOMResult();
        assertEquals(Collections.emptyList(), result.getResult());
        assertTrue(result.isQueried());
    }

    @Test
    public void testGetResultSet() {
        JDOMResult result = new JDOMResult();
        List<Content> content = new ArrayList<>();
        result.setResult(content);
        assertEquals(content, result.getResult());
        assertTrue(result.isQueried());

        List<Content> resultList = result.getResult();
        List<Content> newContent = new ArrayList<>();
        newContent.add(new Element("test"));
        result.setResult(newContent);
        assertEquals(newContent, result.getResult());
        assertFalse(result.isQueried());
        assertNotSame(content, newContent);
    }

    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new SAXBuilder().buildDocument(new StringReader(""));
        result.setDocument(doc);
        assertEquals(doc, result.getDocument());
        assertNull(result.getResult());
        assertTrue(result.isQueried());
    }

    @Test
    public void testGetDocumentEmpty() {
        JDOMResult result = new JDOMResult();
        assertEquals(null, result.getDocument());
        assertTrue(result.isQueried());
    }

    @Test
    public void testGetDocumentSet() {
        JDOMResult result = new JDOMResult();
        Document doc = new SAXBuilder().buildDocument(new StringReader(""));
        result.setDocument(doc);
        assertEquals(doc, result.getDocument());
        assertTrue(result.isQueried());

        Document newDoc = new SAXBuilder().buildDocument(new StringReader("newDoc"));
        result.setDocument(newDoc);
        assertEquals(newDoc, result.getDocument());
        assertFalse(result.isQueried());
        assertNotSame(doc, newDoc);
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory() {};
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }

    @Test
    public void testGetFactoryEmpty() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getFactory());
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = new ArrayList<>();
        result.setHandler(new ContentHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                content.add(new Element(qName));
            }
        });
        result.parse(new InputSource(new StringReader("<root><child/></root>")));
        assertEquals(Collections.singletonList(new Element("root")), result.getResult());
        assertTrue(result.isQueried());
    }
}