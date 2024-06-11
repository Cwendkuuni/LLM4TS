package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

/**
 * Test class for XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testEmptyXml() {
        String xml = "<root/>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xml, "http://example.com");
        assertEquals("root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.childNodes());
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testSimpleXml() {
        String xml = "<root><child>Hello World</child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xml, "http://example.com");
        assertEquals("root", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        Element root = (Element) childNodes.get(0);
        assertEquals("child", root.tagName());
        Text textNode = (Text) root.childNodes().get(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testXmlWithComments() {
        String xml = "<root><!-- comment1 --><child>Hello World</child><!-- comment2 --></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xml, "http://example.com");
        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        Element root = (Element) childNodes.get(0);
        List<Node> rootChildNodes = root.childNodes();
        assertEquals(1, rootChildNodes.size());
        Element child = (Element) rootChildNodes.get(0);
        List<Node> childChildNodes = child.childNodes();
        assertEquals(0, childChildNodes.size());
        assertEquals(2, root.childrenSize());
        Comment comment1 = (Comment) root.child(0);
        assertEquals("comment1", comment1.data());
        Comment comment2 = (Comment) root.child(1);
        assertEquals("comment2", comment2.data());
    }

    @Test
    public void testXmlWithSelfClosingTags() {
        String xml = "<root><child/><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xml, "http://example.com");
        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        Element root = (Element) childNodes.get(0);
        assertEquals("child", root.child(0).tagName());
        assertEquals("child", root.child(1).tagName());
        assertEquals(2, root.childrenSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidXml() {
        String xml = "<root<child/>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        try {
            xmlTreeBuilder.parse(xml, "http://example.com");
            fail("Expected an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><child>Hello World</child></root>";
        Document doc = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals("root", doc.tagName());
        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        Element root = (Element) childNodes.get(0);
        assertEquals("child", root.tagName());
        Text textNode = (Text) root.childNodes().get(0);
        assertEquals("Hello World", textNode.text());
    }
}