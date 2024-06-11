package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testGetResult_NullResultlist() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = jdomResult.getResult();
        assertNull(result);
    }

    @Test
    public void testGetResult_NullResultdoc() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        List<Content> result = jdomResult.getResult();
        assertNull(result);
    }

    @Test
    public void testGetResult_WithResultlist() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        resultList.add(new Element("test"));
        jdomResult.setResult(resultList);
        List<Content> result = jdomResult.getResult();
        assertEquals(resultList.size(), result.size());
        for (Content content : result) {
            assertEquals(content, resultList.get(0));
        }
    }

    @Test
    public void testGetResult_WithResultdoc() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        Element rootElement = new Element("root");
        document.setContent(new Element[] { rootElement });
        jdomResult.setDocument(document);
        List<Content> result = jdomResult.getResult();
        assertEquals(1, result.size());
        assertEquals(rootElement, result.get(0));
    }

    @Test
    public void testSetResult_NullList() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getResult());
        jdomResult.setResult(null);
        assertNull(jdomResult.getResult());
    }

    @Test
    public void testSetResult_EmptyList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> emptyList = Collections.emptyList();
        jdomResult.setResult(emptyList);
        List<Content> result = jdomResult.getResult();
        assertEquals(emptyList.size(), result.size());
        assertNull(result.get(0));
    }

    @Test
    public void testSetDocument_NullDocument() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getDocument());
        Document nullDocument = null;
        jdomResult.setDocument(nullDocument);
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testSetDocument_ValidDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        Document result = jdomResult.getDocument();
        assertEquals(document, result);
    }

    @Test
    public void testSetFactory_NullFactory() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(null);
        assertNull(jdomResult.getFactory());
    }

    @Test
    public void testSetFactory_ValidFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        JDOMFactory result = jdomResult.getFactory();
        assertEquals(factory, result);
    }
}