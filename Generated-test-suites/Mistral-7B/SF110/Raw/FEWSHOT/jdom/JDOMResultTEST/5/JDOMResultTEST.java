package org.jdom2.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;

public class JDOMResultTest {

    @Test
    public void testEmptyConstructor() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getResult());
        assertNull(result.getDocument());
        assertNull(result.getFactory());
    }

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        result.setResult(list);
        assertEquals(list, result.getResult());
    }

    @Test
    public void testGetResultEmpty() {
        JDOMResult result = new JDOMResult();
        List<Content> emptyList = Collections.emptyList();
        result.retrieveResult();
        assertEquals(emptyList, result.getResult());
    }

    @Test
    public void testGetResultWithList() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        Element root = new Element("root");
        list.add(new Text("Test"));
        result.setResult(list);
        result.retrieveResult();
        Document doc = result.getDocument();
        assertNotNull(doc);
        Element rootDoc = doc.getRootElement();
        assertEquals(root, rootDoc);
    }

    @Test
    public void testGetResultWithDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        result.setDocument(document);
        result.retrieveResult();
        List<Content> list = result.getResult();
        assertEquals(Collections.singletonList(root), list);
    }

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        DefaultJDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        JDOMFactory factoryResult = result.getFactory();
        assertSame(factory, factoryResult);
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult result = new JDOMResult();
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        result.setDocument(document);
        List<Content> list = new ArrayList<>();
        list.add(new Text("Test"));
        result.setResult(list);
        result.retrieveResult();
        Document doc = result.getDocument();
        assertNotNull(doc);
        Element rootDoc = doc.getRootElement();
        assertEquals(root, rootDoc);
        List<Content> listResult = result.getResult();
        assertEquals(list, listResult);
    }

    @Test
    public void testSetHandler() {
        JDOMResult result = new JDOMResult();
        ContentHandler handler = new DefaultHandler();
        result.setHandler(handler);
    }

    @Test
    public void testSetLexicalHandler() {
        JDOMResult result = new JDOMResult();
        LexicalHandler handler = new DefaultHandler();
        result.setLexicalHandler(handler);
    }

    @Test
    public void testFragmentHandlerGetResult() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        FragmentHandler fragmentHandler = result.new FragmentHandler(factory);
        List<Content> list = fragmentHandler.getResult();
        assertTrue(list.isEmpty());
    }

    @Test
    public void testDocumentBuilderGetResult() throws Exception {
        JDOMResult result = new JDOMResult();
        SAXBuilder builder = new SAXBuilder();
        List<Content> list = result.new DocumentBuilder(builder).getResult();
        assertTrue(list.isEmpty());
    }
}