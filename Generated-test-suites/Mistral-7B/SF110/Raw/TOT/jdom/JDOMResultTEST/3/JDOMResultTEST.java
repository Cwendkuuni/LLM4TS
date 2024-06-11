package org.jdom2.transform;

import org.jdom2.*;
import org.jdom2.input.sax.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

    @Test
    public void testGetResultEmpty() {
        JDOMResult result = new JDOMResult();
        Assert.assertEquals(Collections.emptyList(), result.getResult());
    }

    @Test
    public void testGetResultList() {
        JDOMResult result = new JDOMResult();
        SAXBuilder builder = new SAXBuilder();
        List<Content> expected = new ArrayList<>();
        expected.add(new Element("element1"));
        expected.add(new Text("text1"));
        builder.setContentHandler(new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (localName.equals("element1")) {
                    result.setResult(expected);
                }
            }
        });
        builder.build(getClass().getResource("/test.xml").openStream());
        List<Content> actual = result.getResult();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetResultDocument() {
        JDOMResult result = new JDOMResult();
        Document document = new JDOMBuilder().buildDocument();
        result.setDocument(document);
        Document actual = result.getDocument();
        Assert.assertEquals(document, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testGetResultNullDocument() {
        JDOMResult result = new JDOMResult();
        result.setDocument(null);
        result.getDocument();
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory() {
            @Override
            public Document createDocument() {
                return null;
            }
        };
        result.setFactory(factory);
        JDOMFactory actual = result.getFactory();
        Assert.assertEquals(factory, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testSetFactoryNull() {
        JDOMResult result = new JDOMResult();
        result.setFactory(null);
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult result = new JDOMResult();
        Document document = new JDOMBuilder().buildDocument();
        result.setDocument(document);
        List<Content> expected = new ArrayList<>();
        expected.add(new Element("element1"));
        expected.add(new Text("text1"));
        SAXBuilder builder = new SAXBuilder();
        builder.setContentHandler(new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (localName.equals("element1")) {
                    result.setResult(expected);
                }
            }
        });
        builder.build(getClass().getResource("/test.xml").openStream());
        List<Content> actual = result.getResult();
        Assert.assertEquals(expected, actual);
        Document actualDocument = result.getDocument();
        Assert.assertEquals(document, actualDocument);
    }

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> expected = new ArrayList<>();
        expected.add(new Element("element1"));
        expected.add(new Text("text1"));
        result.setResult(expected);
        List<Content> actual = result.getResult();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testSetResultNull() {
        JDOMResult result = new JDOMResult();
        result.setResult(null);
    }

    private static class JDOMBuilder {
        public Document buildDocument() {
            Document document = new Document();
            Element root = new Element("root");
            document.setRootElement(root);
            return document;
        }
    }
}