package org.jdom2.transform;

import junit.framework.Assert;
import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class JDOMResultTest {

    @Test
    public void testJDOMResultConstructor() {
        JDOMResult jdomResult = new JDOMResult();
        // Assert that default values are set
        assertNull(jdomResult.getResult());
        assertNull(jdomResult.getDocument());
        assertFalse(jdomResult.isQueried());
        assertNull(jdomResult.getFactory());
    }

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = Arrays.asList(new Element("element1"), new Element("element2"));
        jdomResult.setResult(contentList);
        // Assert that result is set correctly
        assertEquals(contentList, jdomResult.getResult());
        assertFalse(jdomResult.isQueried());
    }

    @Test(expected = NullPointerException.class)
    public void testSetResultNullList() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(null);
    }

    @Test
    public void testGetDocumentEmptyResult() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(Collections.emptyList());
        Document doc = jdomResult.getDocument();
        // Assert that document is null and queried flag is set
        assertNull(doc);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testSetDocumentEmptyDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document emptyDoc = new Document();
        jdomResult.setDocument(emptyDoc);
        // Assert that document is set correctly and result is null
        assertSame(emptyDoc, jdomResult.getDocument());
        assertNull(jdomResult.getResult());
        assertFalse(jdomResult.isQueried());
    }

    @Test(expected = NullPointerException.class)
    public void testSetDocumentNullDocument() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(null);
    }

    @Test
    public void testGetDocumentWithResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = Arrays.asList(new Element("element1"), new Element("element2"));
        jdomResult.setResult(contentList);
        Document doc = jdomResult.getDocument();
        // Assert that document is created from the result and queried flag is set
        JDOMFactory factory = new DefaultJDOMFactory();
        Document createdDoc = factory.document(null);
        createdDoc.setContent(contentList);
        assertSame(createdDoc, doc);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        // Assert that factory is set correctly
        assertSame(factory, jdomResult.getFactory());
    }

    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        // Assert that factory is retrieved correctly
        assertSame(factory, jdomResult.getFactory());
    }

    @Test
    public void testRetrieveResultEmptyResult() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setResult(Collections.emptyList());
        Document doc = jdomResult.getDocument();
        // Assert that document is null and queried flag is set
        assertNull(doc);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testRetrieveResultSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = Arrays.asList(new Element("element1"), new Element("element2"));
        jdomResult.setResult(contentList);
        Document emptyDoc = new Document();
        jdomResult.setDocument(emptyDoc);
        Document doc = jdomResult.getDocument();
        // Assert that document is created from the result and queried flag is set
        JDOMFactory factory = new DefaultJDOMFactory();
        Document createdDoc = factory.document(null);
        createdDoc.setContent(contentList);
        assertSame(createdDoc, doc);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testRetrieveResultSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = Arrays.asList(new Element("element1"), new Element("element2"));
        jdomResult.setResult(contentList);
        Document doc = jdomResult.getDocument();
        // Assert that document is null and queried flag is set
        assertNull(doc);
        assertTrue(jdomResult.isQueried());
    }
}