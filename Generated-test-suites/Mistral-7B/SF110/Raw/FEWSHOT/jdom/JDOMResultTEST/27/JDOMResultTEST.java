package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testGetResult_EmptyResultList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = jdomResult.getResult();
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testGetResult_SetResultList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        Content content = new Element("content");
        contentList.add(content);
        jdomResult.setResult(contentList);
        List<Content> result = jdomResult.getResult();
        assertEquals(contentList, result);
    }

    @Test
    public void testGetResult_SetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new JDOMBuilder().setContent(new Element("document")).getDocument();
        jdomResult.setDocument(document);
        List<Content> result = jdomResult.getResult();
        Document doc = jdomResult.getDocument();
        assertEquals(doc.getContent(), result);
    }

    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        JDOMFactory resultFactory = jdomResult.getFactory();
        assertEquals(factory, resultFactory);
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.retrieveResult();
        List<Content> result = jdomResult.getResult();
        assertNotNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocument_NullResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = null;
        jdomResult.setResult(resultList);
        Document result = jdomResult.getDocument();
        fail("Expected RuntimeException");
    }

    @Test
    public void testGetDocument_SetResultList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        Content content = new Element("content");
        contentList.add(content);
        jdomResult.setResult(contentList);
        Document result = jdomResult.getDocument();
        Document doc = result.getDocument();
        assertEquals(doc.getContent(), contentList.toArray(new Content[0]));
    }

    @Test
    public void testGetDocument_SetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new JDOMBuilder().setContent(new Element("document")).getDocument();
        jdomResult.setDocument(document);
        Document result = jdomResult.getDocument();
        assertEquals(document, result);
    }
}