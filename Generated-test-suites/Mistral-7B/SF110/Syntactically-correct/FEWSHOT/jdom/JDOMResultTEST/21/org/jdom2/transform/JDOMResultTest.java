package org.jdom2.transform;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.sax.JDOMInputFactory;
import org.xml.sax.SAXException;

import org.junit.Test;

public class JDOMResultTest {

    @Test
    public void testJDOMResult() {
        JDOMResult jdomResult = new JDOMResult();
        assertNull(jdomResult.getFactory());
        assertEquals(Collections.emptyList(), jdomResult.getResult());
        assertNull(jdomResult.getDocument());
        assertFalse(jdomResult.isQueried());
    }

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        jdomResult.setResult(contentList);
        assertEquals(contentList, jdomResult.getResult());
        assertFalse(jdomResult.isQueried());
    }

    @Test
    public void testGetResultWhenQueriedForFirstTime() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new JDOMFactory().document(null);
        jdomResult.setDocument(doc);

        List<Content> result = jdomResult.getResult();
        assertFalse(jdomResult.isQueried());
        assertNull(result);

        result = jdomResult.getResult();
        assertTrue(jdomResult.isQueried());
        assertNotNull(result);
        assertEquals(Collections.emptyList(), result);

        jdomResult.setResult(Collections.singletonList(JDOMFactory.getInstance().element("test")));
        result = jdomResult.getResult();
        assertNotNull(result);
        assertEquals(Collections.singletonList(JDOMFactory.getInstance().element("test")), result);
        assertFalse(jdomResult.isQueried());

        result = jdomResult.getResult();
        assertTrue(jdomResult.isQueried());
        assertEquals(Collections.singletonList(JDOMFactory.getInstance().element("test")), result);
    }

    @Test
    public void testGetResultWhenQueriedTwice() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new JDOMFactory().document(null);
        jdomResult.setDocument(doc);

        jdomResult.setResult(Collections.singletonList(JDOMFactory.getInstance().element("test1")));
        List<Content> result1 = jdomResult.getResult();
        assertEquals(Collections.singletonList(JDOMFactory.getInstance().element("test1")), result1);

        jdomResult.setResult(Collections.singletonList(JDOMFactory.getInstance().element("test2")));
        List<Content> result2 = jdomResult.getResult();
        assertEquals(Collections.singletonList(JDOMFactory.getInstance().element("test2")), result2);

        jdomResult.setResult(null);
        List<Content> result3 = jdomResult.getResult();
        assertEquals(Collections.emptyList(), result3);

        jdomResult.setResult(Collections.singletonList(JDOMFactory.getInstance().element("test3")));
        List<Content> result4 = jdomResult.getResult();
        assertEquals(Collections.singletonList(JDOMFactory.getInstance().element("test3")), result4);

        result1 = jdomResult.getResult();
        assertEquals(Collections.singletonList(JDOMFactory.getInstance().element("test3")), result1);
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc1 = new JDOMFactory().document(null);
        Document doc2 = new JDOMFactory().document(null);
        jdomResult.setDocument(doc1);
        assertEquals(doc1, jdomResult.getDocument());

        jdomResult.setDocument(doc2);
        assertEquals(doc2, jdomResult.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory1 = new JDOMFactory();
        JDOMFactory factory2 = new JDOMFactory();
        jdomResult.setFactory(factory1);
        assertEquals(factory1, jdomResult.getFactory());

        jdomResult.setFactory(factory2);
        assertEquals(factory2, jdomResult.getFactory());
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new JDOMFactory().document(null);
        jdomResult.setDocument(doc);
        jdomResult.retrieveResult();

        List<Content> result = jdomResult.getResult();
        assertFalse(jdomResult.isQueried());
        assertNull(result);

        result = jdomResult.getResult();
        assertTrue(jdomResult.isQueried());
        assertEquals(doc.getContent(), result);
    }

    @Test
    public void testEmptyResultListWhenNoContentElement() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new JDOMFactory().document(null);
        jdomResult.setDocument(doc);
        jdomResult.retrieveResult();

        List<Content> result = jdomResult.getResult();
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testResultListWhenContentElementPresent() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new JDOMFactory().document(null);
        Element root = doc.getRootElement();
        root.addContent(JDOMFactory.getInstance().getText("test"));
        jdomResult.setDocument(doc);
        jdomResult.retrieveResult();

        List<Content> result = jdomResult.getResult();
        assertEquals(Collections.singletonList(JDOMFactory.getInstance().getText("test")), result);
    }

    @Test
    public void testResultListWhenContentElementPresentWithMultipleTypes() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new JDOMFactory().document(null);
        Element root = doc.getRootElement();
        root.addContent(JDOMFactory.getInstance().getText("testText"));
        root.addContent(JDOMFactory.getInstance().getChild("testElement", null));
        jdomResult.setDocument(doc);
        jdomResult.retrieveResult();

        List<Content> result = jdomResult.getResult();
        List<Content> expectedResult = new ArrayList<>();
        expectedResult.add(JDOMFactory.getInstance().getText("testText"));
        expectedResult.add(JDOMFactory.getInstance().getChild("testElement", null));
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetFactoryThrowsNullPointerExceptionWhenFactoryIsNull() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(null);
        assertThrows(NullPointerException.class, () -> jdomResult.getFactory());
    }

    @Test
    public void testGetDocumentThrowsNullPointerExceptionWhenDocumentIsNull() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(null);
        assertThrows(NullPointerException.class, () -> jdomResult.getDocument());
    }

    @Test
    public void testGetDocumentThrowsNullPointerExceptionWhenContentIsEmpty() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(new JDOMFactory().document(null));
        jdomResult.retrieveResult();
        assertNull(jdomResult.getDocument());
    }

    @Test
    public void testGetDocumentThrowsNullPointerExceptionWhenContentIsNotEmpty() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new JDOMFactory().document(null);
        Element root = doc.getRootElement();
        root.addContent(JDOMFactory.getInstance().getText("test"));
        jdomResult.setDocument(doc);
        jdomResult.retrieveResult();

        Document result = jdomResult.getDocument();
        assertNotNull(result);
        assertSame(doc, result);
    }

    @Test
    public void testGetDocumentThrowsNullPointerExceptionWhenResultListIsEmpty() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new JDOMFactory().document(null);
        jdomResult.setDocument(doc);
        jdomResult.retrieveResult();

        Document result = jdomResult.getDocument();
        assertNull(result);
    }

    @Test
    public void testGetDocumentThrowsNullPointerExceptionWhenResultListIsNotEmpty() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new JDOMFactory().document(null);
        Element root = doc.getRootElement();
        root.addContent(JDOMFactory.getInstance().getText("test"));
        jdomResult.setDocument(doc);
        jdomResult.retrieveResult();

        Document result = jdomResult.getDocument();
        assertNotNull(result);
        assertEquals(doc.getContent(), result.getContent());
    }

    @Test
    public void testGetDocumentThrowsNullPointerExceptionWhenResultListIsNotEmptyAndFactoryIsDifferent() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc1 = new JDOMFactory().document(null);
        Document doc2 = new DefaultJDOMFactory().document(null);
        jdomResult.setDocument(doc1);
        jdomResult.retrieveResult();

        JDOMFactory factory1 = jdomResult.getFactory();
        JDOMFactory factory2 = jdomResult.getFactory();
        assertSame(factory1, factory2);

        jdomResult.setFactory(new DefaultJDOMFactory());
        Document result = jdomResult.getDocument();
        assertNotNull(result);
        assertNotSame(doc1, result);
    }
}