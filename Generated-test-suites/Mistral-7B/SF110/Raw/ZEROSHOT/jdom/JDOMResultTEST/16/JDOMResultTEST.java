package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMResultTest {

    @Test
    public void testJDOMResult() {
        JDOMResult jdomResult = new JDOMResult();
        assertEquals(null, jdomResult.getResult());
        assertEquals(null, jdomResult.getDocument());
        assertFalse(jdomResult.isQueried());
        assertNull(jdomResult.getFactory());
    }

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(Element.valueOf("<test></test>"));
        jdomResult.setResult(result);
        List<Content> actualResult = jdomResult.getResult();
        assertEquals(Collections.singletonList(result.get(0)), actualResult);
        assertEquals(result, jdomResult.getResult());
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = JDOMFactory.instance.newDocument();
        Element root = new Element("root", null, null);
        document.setRootElement(root);
        jdomResult.setDocument(document);
        Document actualDocument = jdomResult.getDocument();
        assertEquals(document, actualDocument);
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = JDOMFactory.instance;
        jdomResult.setFactory(factory);
        JDOMFactory actualFactory = jdomResult.getFactory();
        assertEquals(factory, actualFactory);
    }

    @Test(expected = RuntimeException.class)
    public void testRetrieveResultWhenNullResultAndDocument() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.retrieveResult();
    }

    @Test
    public void testRetrieveResultWhenNotNullDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = JDOMFactory.instance.newDocument();
        jdomResult.setDocument(document);
        jdomResult.retrieveResult();
        assertTrue(jdomResult.isQueried());
    }

    @Test
    public void testRetrieveResultWhenNotNullResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(Element.valueOf("<test></test>"));
        jdomResult.setResult(result);
        jdomResult.retrieveResult();
        assertTrue(jdomResult.isQueried());
    }
}