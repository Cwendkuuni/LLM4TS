package org.jdom2.transform;

import org.jdom2.*;
import org.jdom2.input.sax.FragmentHandler;
import org.jdom2.input.sax.XMLFilterImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

    @Test
    public void testJDOMResultInstance() {
        JDOMResult jdomResult = new JDOMResult();
        Assert.assertNull(jdomResult.getResult());
        Assert.assertNull(jdomResult.getDocument());
        Assert.assertNull(jdomResult.getFactory());
    }

    @Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        result.add(new Element("test"));
        jdomResult.setResult(result);
        List<Content> actualResult = jdomResult.getResult();
        Assert.assertEquals(result, actualResult);
        Assert.assertNull(jdomResult.getDocument());
    }

    @Test
    public void testGetDocumentFromResultList() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        Element element = new Element("test");
        result.add(element);
        jdomResult.setResult(result);
        JDOMFactory factory = new DefaultJDOMFactory();
        Document document = jdomResult.getDocument();
        Assert.assertNotNull(document);
        Assert.assertEquals(element, document.getRootElement());
    }

    @Test
    public void testGetDocumentFromResultDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new Document();
        jdomResult.setDocument(document);
        JDOMFactory factory = new DefaultJDOMFactory();
        Document actualDocument = jdomResult.getDocument();
        Assert.assertEquals(document, actualDocument);
    }

    @Test
    public void testSetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        Assert.assertEquals(factory, jdomResult.getFactory());
    }

    @Test
    public void testGetFactory() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        jdomResult.setFactory(factory);
        Assert.assertEquals(factory, jdomResult.getFactory());
        JDOMFactory anotherFactory = new DefaultJDOMFactory();
        jdomResult.setFactory(anotherFactory);
        Assert.assertEquals(anotherFactory, jdomResult.getFactory());
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult jdomResult = new JDOMResult();
        XMLFilterImpl handler = new DocumentBuilder();
        jdomResult.setHandler(handler);
        handler.parse(getClass().getResource("/test.xml"));
        List<Content> expectedResult = new ArrayList<>();
        expectedResult.add(new Element("test"));
        List<Content> actualResult = jdomResult.getResult();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDocumentException() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> result = new ArrayList<>();
        jdomResult.setResult(result);
        try {
            jdomResult.getDocument();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}