package org.jdom2.transform;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.JDOMFactory;
import org.jdom2.Document;
import org.jdom2.input.sax.AbstractSAXBuilder;

public class JDOMResultTest {

    @Test
    public void testJDOMResultEmpty() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getDocument());
        assertNull(result.getResult());
    }

    @Test
    public void testJDOMResultWithList() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("test"));
        result.setResult(list);

        List<Content> resultList = result.getResult();
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals("test", resultList.get(0).getName());
    }

    @Test
    public void testJDOMResultWithDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        result.setDocument(doc);

        Document resultDoc = result.getDocument();
        assertSame(doc, resultDoc);
    }

    @Test
    public void testJDOMResultWithListAndDocument() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("test"));
        result.setResult(list);
        Document doc = new Document();
        result.setDocument(doc);

        List<Content> resultList = result.getResult();
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals("test", resultList.get(0).getName());

        Document resultDoc = result.getDocument();
        assertNull(resultDoc);
    }

    @Test
    public void testGetFactory() {
        JDOMResult result = new JDOMResult();
        assertNull(result.getFactory());

        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);

        JDOMFactory resultFactory = result.getFactory();
        assertSame(factory, resultFactory);
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        result.setDocument(doc);

        result.retrieveResult();
        Document resultDoc = result.getDocument();
        List<Content> resultList = result.getResult();

        assertSame(doc, resultDoc);
        assertNull(resultList);
    }

    @Test
    public void testGetResultWithList() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("test"));
        result.setResult(list);

        List<Content> resultList = result.getResult();
        assertEquals(1, resultList.size());
        assertEquals("test", resultList.get(0).getName());
    }

    @Test
    public void testGetResultWithDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        result.setDocument(doc);

        List<Content> resultList = result.getResult();
        assertNull(resultList);

        Document resultDoc = result.getDocument();
        assertSame(doc, resultDoc);
    }

    @Test
    public void testSetResultWithNullList() {
        JDOMResult result = new JDOMResult();
        result.setResult(null);

        List<Content> resultList = result.getResult();
        assertNull(resultList);
    }

    @Test
    public void testSetResultWithList() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("test1"));
        list.add(new Element("test2"));
        result.setResult(list);

        List<Content> resultList = result.getResult();
        assertEquals(2, resultList.size());
        assertEquals("test1", resultList.get(0).getName());
        assertEquals("test2", resultList.get(1).getName());
    }

    @Test
    public void testSetDocumentWithNullDocument() {
        JDOMResult result = new JDOMResult();
        result.setDocument(null);

        Document resultDoc = result.getDocument();
        assertNull(resultDoc);
    }

    @Test
    public void testSetDocumentWithDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        result.setDocument(doc);

        Document resultDoc = result.getDocument();
        assertSame(doc, resultDoc);
    }

    @Test
    public void testSetFactoryWithNullFactory() {
        JDOMResult result = new JDOMResult();
        result.setFactory(null);

        JDOMFactory resultFactory = result.getFactory();
        assertNull(resultFactory);
    }

    @Test
    public void testSetFactoryWithFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);

        JDOMFactory resultFactory = result.getFactory();
        assertSame(factory, resultFactory);
    }

    @Test
    public void testGetDocumentWithResultList() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("test"));
        result.setResult(list);

        Document resultDoc = result.getDocument();
        assertNull(resultDoc);

        Document doc = new Document();
        result.setDocument(doc);
        resultList = result.getResult();

        resultDoc = result.getDocument();
        assertNull(resultDoc);
    }

    @Test
    public void testGetDocumentWithResultDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        result.setDocument(doc);

        Document resultDoc = result.getDocument();
        assertSame(doc, resultDoc);

        resultList = result.getResult();
        assertNull(resultList);
    }

    @Test
    public void testSetResultWithEmptyList() {
        JDOMResult result = new JDOMResult();
        result.setResult(Collections.emptyList());

        List<Content> resultList = result.getResult();
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testGetResultWithEmptyList() {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        result.setResult(list);

        List<Content> resultList = result.getResult();
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testJDOMResultWithContentHandler() throws JDOMException {
        JDOMResult result = new JDOMResult();
        List<Content> list = new ArrayList<>();
        list.add(new Element("test"));
        result.setResult(list);

        Document doc = new Document();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setDocument(doc);
        result.setFactory(factory);

        AbstractSAXBuilder builder = new AbstractSAXBuilder(factory);
        builder.setContentHandler(new MyContentHandler());
        builder.build(new StringReader("<test>Test</test>"));

        resultList = result.getResult();
        assertEquals(1, resultList.size());
        assertEquals("test", resultList.get(0).getName());

        Document resultDoc = result.getDocument();
        assertNotNull(resultDoc);
        assertEquals("Test", resultDoc.getRootElement().getText());
    }

    private static class MyContentHandler extends DefaultHandler {
        private List<Content> contents;

        public MyContentHandler() {
            this.contents = new ArrayList<>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            Element element = new Element(qName);
            contents.add(element);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
        }

        public List<Content> getResult() {
            return contents;
        }
    }
}