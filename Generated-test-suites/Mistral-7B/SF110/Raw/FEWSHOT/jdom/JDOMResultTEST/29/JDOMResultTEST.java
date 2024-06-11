package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testEmptyJDOMResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = jdomResult.getResult();
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testSetResultWithList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element1"));
        jdomResult.setResult(contentList);
        List<Content> result = jdomResult.getResult();
        assertEquals(contentList, result);
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new JDOMFactory().newDocument();
        jdomResult.setDocument(document);
        Document result = jdomResult.getDocument();
        assertEquals(document, result);
    }

    @Test
    public void testGetDocumentWhenNoResult() {
        JDOMResult jdomResult = new JDOMResult();
        Document result = jdomResult.getDocument();
        assertNull(result);
    }

    @Test
    public void testGetDocumentWhenResultList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("element1"));
        jdomResult.setResult(contentList);
        Document result = jdomResult.getDocument();
        assertNotNull(result);
        JDOMFactory factory = jdomResult.getFactory();
        Document doc = factory.document(null);
        doc.setContent(contentList);
        assertEquals(doc, result);
    }

    @Test
    public void testGetDocumentWhenResultDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new JDOMFactory().newDocument();
        jdomResult.setDocument(document);
        Document result = jdomResult.getDocument();
        assertEquals(document, result);
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new DocumentBuilder().newDocument();
        jdomResult.setHandler(new DocumentBuilder());
        jdomResult.retrieveResult();
        List<Content> result = jdomResult.getResult();
        assertEquals(Collections.emptyList(), result);
        jdomResult.setDocument(document);
        result = jdomResult.getResult();
        assertEquals(document.getContent(), result);
    }
}