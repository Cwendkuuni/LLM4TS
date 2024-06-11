package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jdom2.*;
import org.jdom2.input.sax.JDOMSAXBuilder;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

public class JDOMResultTest {

    @Test
    public void testJDOMResultConstruction() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getResult());
        assertNull(result.getDocument());
        assertNull(result.getFactory());
    }

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("foo"));
        result.setResult(list);
        assertEquals(list, result.getResult());
        assertNull(result.getDocument());
    }

    @Test
    public void testGetDocumentFromResultList() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("root"));
        result.setResult(list);
        Document doc = result.getDocument();
        assertNotNull(doc);
        assertEquals(list.get(0), doc.getRootElement());
    }

    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        result.setDocument(doc);
        assertEquals(doc, result.getDocument());
        assertNull(result.getResult());
    }

    @Test
    public void testGetDocumentFromEmptyResult() {
        JDOMResult result = new JDOMResult();
        result.setDocument(new Document());
        assertSame(result.getDocument(), result.getDocument());
        assertNull(result.getResult());
    }

    @Test
    public void testGetDocumentFromResultListAndEmptyDocument() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("root"));
        result.setResult(list);
        Document doc = new Document();
        result.setDocument(doc);
        Document resultDoc = result.getDocument();
        assertSame(doc, resultDoc);
        assertEquals(list.get(0), resultDoc.getRootElement());
    }

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult result = new JDOMResult();
        SAXBuilder builder = new JDOMSAXBuilder();
        DefaultHandler handler = new DefaultHandler();
        builder.build(getClass().getResourceAsStream("/test.xml"), handler);
        List<Content> resultList = handler.getResult();
        result.setResult(resultList);
        List<Content> retrievedResult = result.getResult();
        assertEquals(resultList, retrievedResult);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocumentFromNullResult() {
        JDOMResult result = new JDOMResult();
        result.retrieveResult();
        result.getDocument();
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocumentFromEmptyDocumentAndNullResult() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        result.setDocument(doc);
        result.retrieveResult();
        result.getDocument();
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocumentFromResultListAndEmptyDocumentAndNullResult() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("root"));
        result.setResult(list);
        Document doc = new Document();
        result.setDocument(doc);
        result.retrieveResult();
        result.getDocument();
    }
}