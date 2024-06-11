package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testJDOMResult() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getResult());
        assertNull(jdomResult.getDocument());
        assertFalse(jdomResult.isQueried());
        assertNull(jdomResult.getFactory());
    }

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        resultList.add(new Element("element1"));
        jdomResult.setResult(resultList);
        List<Content> actualResult = jdomResult.getResult();
        assertEquals(resultList, actualResult);
        assertTrue(jdomResult.isQueried());
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        Element rootElement = new Element("root");
        document.setRootElement(rootElement);
        jdomResult.setDocument(document);
        Document actualDocument = jdomResult.getDocument();
        assertEquals(document, actualDocument);
        List<Content> actualResult = jdomResult.getResult();
        assertTrue(actualResult.isEmpty());
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        JDOMFactory actualFactory = jdomResult.getFactory();
        assertEquals(factory, actualFactory);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocument_whenResultlistIsEmptyAndQueriedIsFalse() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(new DefaultJDOMFactory());
        Document document = jdomResult.getDocument();
        assertNull(document);
    }

    @Test
    public void testGetDocument_whenResultlistIsNotEmptyAndQueriedIsFalse() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        resultList.add(new Element("element1"));
        jdomResult.setFactory(new DefaultJDOMFactory());
        jdomResult.setResult(resultList);
        Document actualDocument = jdomResult.getDocument();
        Document expectedDocument = new Document();
        expectedDocument.setContent(resultList);
        assertEquals(expectedDocument, actualDocument);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetDocument_whenResultlistIsEmptyAndQueriedIsTrue() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(new DefaultJDOMFactory());
        Document actualDocument = jdomResult.getDocument();
        assertNull(actualDocument);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetDocument_whenResultlistIsNotEmptyAndQueriedIsTrue() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> resultList = new ArrayList<>();
        resultList.add(new Element("element1"));
        jdomResult.setFactory(new DefaultJDOMFactory());
        jdomResult.setResult(resultList);
        Document actualDocument = jdomResult.getDocument();
        Document expectedDocument = new Document();
        expectedDocument.setContent(resultList);
        assertEquals(expectedDocument, actualDocument);
        assertTrue(jdomResult.isQueried());
    }
}