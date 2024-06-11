package org.jdom2.transform;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.input.sax.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;

public class JDOMResultTest {

    @Test
    public void testGetResult_EmptyResultlist() {
        JDOMResult result = new JDOMResult();
        List<Content> nodes = result.getResult();
        assertNull(nodes);
        assertNull(result.getDocument());
    }

    @Test
    public void testGetResult_WithResultlist() {
        JDOMResult result = new JDOMResult();
        Element root = new Element("root", null, null);
        Element child1 = new Element("child1", null, null);
        root.addContent(child1);
        ContentHandler handler = new ContentHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                if (qName.equals("root")) {
                    result.setResult(Collections.singletonList(root));
                }
            }
        };
        SaxBuilder builder = new SaxBuilder(handler);
        builder.build(new StringReader("<root></root>"));
        List<Content> nodes = result.getResult();
        assertEquals(1, nodes.size());
        assertEquals(child1, nodes.get(0));
        Document doc = result.getDocument();
        assertEquals(root, doc);
    }

    @Test
    public void testGetResult_WithDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root", null, null);
        doc.setContent(Collections.singletonList(root));
        result.setDocument(doc);
        List<Content> nodes = result.getResult();
        assertEquals(1, nodes.size());
        assertEquals(root, nodes.get(0));
        assertEquals(doc, result.getDocument());
    }

    @Test
    public void testRetrieveResult() {
        JDOMResult result = new JDOMResult();
        DocumentBuilder builder = new DocumentBuilder();
        builder.setHandler(new FragmentHandler(result.getFactory()));
        StringBuilder xmlString = new StringBuilder("<root><child></child></root>");
        InputSource xmlInput = new InputSource(new StringReader(xmlString.toString()));
        builder.parse(xmlInput);
        List<Content> nodes = result.getResult();
        assertEquals(1, nodes.size());
        assertEquals(builder.getResult().getRootElement(), nodes.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testGetDocument_NullResultlist() {
        JDOMResult result = new JDOMResult();
        result.setResult(null);
        Document doc = result.getDocument();
        assertNull(doc);
    }

    @Test
    public void testGetDocument_WithResultlist() {
        JDOMResult result = new JDOMResult();
        Element root = new Element("root", null, null);
        Element child1 = new Element("child1", null, null);
        root.addContent(child1);
        ContentHandler handler = new ContentHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                if (qName.equals("root")) {
                    result.setResult(Collections.singletonList(root));
                }
            }
        };
        SaxBuilder builder = new SaxBuilder(handler);
        builder.build(new StringReader("<root></root>"));
        result.setFactory(new JDOMFactory());
        Document doc = result.getDocument();
        assertEquals(root, doc.getRootElement());
    }

    @Test
    public void testGetDocument_WithDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root", null, null);
        doc.setContent(Collections.singletonList(root));
        result.setDocument(doc);
        Document doc2 = result.getDocument();
        assertEquals(doc, doc2);
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new JDOMFactory();
        result.setFactory(factory);
        JDOMFactory factory2 = result.getFactory();
        assertEquals(factory, factory2);
    }
}