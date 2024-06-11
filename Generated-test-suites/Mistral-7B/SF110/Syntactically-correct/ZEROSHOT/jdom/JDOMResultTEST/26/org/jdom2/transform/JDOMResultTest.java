package org.jdom2.transform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.junit.Test;
import org.xml.sax.SAXException;

public class JDOMResultTest {

    @Test
    public void testJDOMResultConstruction() {
        JDOMResult jdomResult = new JDOMResult();
        assertNotNull(jdomResult);
        assertNull(jdomResult.getResult());
        assertNull(jdomResult.getDocument());
        assertNull(jdomResult.getFactory());
    }

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        jdomResult.setResult(contentList);
        List<Content> result = jdomResult.getResult();
        assertSame(contentList, result);
    }

    @Test
    public void testGetResultWhenNotQueried() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new Document();
        jdomResult.setDocument(doc);
        List<Content> result = jdomResult.getResult();
        assertSame(Collections.emptyList(), result);
    }

    @Test
    public void testGetResultWhenQueriedAndHasResultList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        jdomResult.setResult(contentList);
        List<Content> result = jdomResult.getResult();
        assertSame(contentList, result);
        result = jdomResult.getResult();
        assertSame(contentList, result);
    }

    @Test
    public void testGetResultWhenQueriedAndHasDocument() throws SAXException {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new Document();
        jdomResult.setDocument(doc);
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        JDOMResult jdomResultWithDocument = new JDOMResult();
        jdomResultWithDocument.setFactory(factory);
        jdomResultWithDocument.setHandler(builder);
        builder.build(JDOMResultTest.class.getClassLoader().getResourceAsStream("test.xml"));
        jdomResult.setDocument(jdomResultWithDocument.getDocument());
        List<Content> result = jdomResult.getResult();
        assertFalse(result.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testGetResultWhenQueriedAndHasNullDocument() {
        JDOMResult jdomResult = new JDOMResult();
        jdomResult.setDocument(null);
        jdomResult.getResult();
    }

    @Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document doc = new Document();
        jdomResult.setDocument(doc);
        Document result = jdomResult.getDocument();
        assertSame(doc, result);
    }
}