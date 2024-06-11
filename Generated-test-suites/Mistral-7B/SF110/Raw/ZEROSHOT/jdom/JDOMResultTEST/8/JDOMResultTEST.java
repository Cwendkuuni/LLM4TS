package org.jdom2.transform;

import org.jdom2.*;
import org.jdom2.input.sax.JDOMInputFactory;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMResultTest {

    @org.junit.Before
    public void setUp() {
        JDOMResult result = new JDOMResult();
        result.setFactory(new JDOMFactory());
    }

    @org.junit.Test
    public void testEmptyResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = jdomResult.getResult();
        assertEquals(Collections.emptyList(), result);
    }

    @org.junit.Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("element1"));
        jdomResult.setResult(list);
        List<Content> expectedResult = jdomResult.getResult();
        assertEquals(list, expectedResult);
    }

    @org.junit.Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        doc.setRootElement(root);
        jdomResult.setDocument(doc);
        Document resultDoc = jdomResult.getDocument();
        assertEquals(doc, resultDoc);
    }

    @org.junit.Test
    public void testGetDocumentWhenResultIsSet() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("element1"));
        jdomResult.setFactory(new JDOMFactory());
        jdomResult.setResult(list);
        Document resultDoc = jdomResult.getDocument();
        assertEquals(jdomResult.getFactory().document(list), resultDoc);
    }

    @org.junit.Test
    public void testGetDocumentWhenResultIsNull() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(new JDOMFactory());
        jdomResult.setDocument(null);
        Document resultDoc = jdomResult.getDocument();
        assertNull(resultDoc);
    }

    @org.junit.Test
    public void testRetrieveResult() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = jdomResult.getDocument();
        assertEquals(Collections.emptyList(), jdomResult.getResult());
        jdomResult.setDocument(doc);
        assertEquals(doc.getContent(), jdomResult.getResult());
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testGetFactoryWhenNull() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(null);
        jdomResult.getFactory();
    }

    @org.junit.Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        jdomResult.setFactory(factory);
        JDOMFactory expectedFactory = jdomResult.getFactory();
        assertEquals(factory, expectedFactory);
    }

    @org.junit.Test
    public void testFragmentHandlerGetResult() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setFactory(new JDOMFactory());
        FragmentHandler fragmentHandler = new FragmentHandler(jdomResult.getFactory());
        List<Content> result = fragmentHandler.getResult();
        assertEquals(Collections.emptyList(), result);
    }

    @org.junit.Test
    public void testDocumentBuilder() {
        JDOMResult jdomResult = new JDOMResult();
        DocumentBuilder documentBuilder = jdomResult.new DocumentBuilder();
        List<Content> resultList = documentBuilder.getResult();
        assertEquals(Collections.emptyList(), resultList);
    }
}