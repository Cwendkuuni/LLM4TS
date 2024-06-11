package org.jdom2.transform;

import org.jdom2.*;
import org.jdom2.input.sax.JDOMBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        assertEquals(0, result.size());
    }

    @org.junit.Test
    public void testSetResult() {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("test"));
        jdomResult.setResult(contentList);
        List<Content> result = jdomResult.getResult();
        assertEquals(1, result.size());
        assertEquals(new Element("test"), result.get(0));
    }

    @org.junit.Test
    public void testSetDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new JDOMFactory().newDocument();
        Element root = new Element("root");
        document.setRootElement(root);
        jdomResult.setDocument(document);
        Document doc = jdomResult.getDocument();
        assertEquals(root, doc.getRootElement());
    }

    @org.junit.Test
    public void testGetDocument_whenResultListIsSet_returnDocument() throws SAXException {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("test"));
        jdomResult.setResult(contentList);
        Document document = jdomResult.getDocument();
        assertEquals(1, document.getContentSize());

        JDOMBuilder builder = new JDOMBuilder(new DefaultHandler(), factory);
        builder.bind(jdomResult);
        document.setContent(contentList);

        Document doc = jdomResult.getDocument();
        assertEquals(document, doc);
    }

    @org.junit.Test
    public void testGetDocument_whenResultDocumentIsSet_returnDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new JDOMFactory().newDocument();
        jdomResult.setDocument(document);
        Document doc = jdomResult.getDocument();
        assertEquals(document, doc);
    }

    @org.junit.Test
    public void testGetDocument_whenBothResultListAndDocumentAreSet_returnDocument() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new JDOMFactory().newDocument();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("test"));
        jdomResult.setDocument(document);
        jdomResult.setResult(contentList);

        Document doc = jdomResult.getDocument();
        assertEquals(document, doc);
    }

    @org.junit.Test
    public void testFactoryGet() {
        JDOMResult jdomResult = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        JDOMFactory factory2 = jdomResult.getFactory();
        assertEquals(factory, factory2);
    }

    @org.junit.Test
    public void testGetResult_whenBothResultListAndDocumentAreSet_returnResultList() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new JDOMFactory().newDocument();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("test"));
        jdomResult.setDocument(document);
        jdomResult.setResult(contentList);

        List<Content> result = jdomResult.getResult();
        assertEquals(contentList, result);
    }

    @org.junit.Test
    public void testRetrieveResult() {
        JDOMResult jdomResult = new JDOMResult();
        Document document = new JDOMFactory().newDocument();
        List<Content> contentList = new ArrayList<>();
        contentList.add(new Element("test"));
        jdomResult.setResult(contentList);
        jdomResult.setDocument(document);

        jdomResult.retrieveResult();
        Document doc = jdomResult.getDocument();
        assertEquals(contentList, doc.getContent());
    }
}