package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(""), "baseUri");
        assertNotNull(xmlDoc);
        assertEquals(0, xmlDoc.childNodeSize());
    }

    @Test
    public void testParseSimpleXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root><child>Hello World</child></root>"), "baseUri");
        assertNotNull(xmlDoc);
        Element rootElement = xmlDoc.select("root").first();
        assertNotNull(rootElement);
        Element childElement = rootElement.child(0);
        assertNotNull(childElement);
        assertEquals("Hello World", childElement.text());
    }

    @Test
    public void testParseXmlWithComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root><!-- This is a comment --><child>Hello World</child></root>"), "baseUri");
        assertNotNull(xmlDoc);
        List<Node> nodes = xmlDoc.childNodes();
        Node nodeWithComment = nodes.get(0);
        assertNotNull(nodeWithComment);
        assertInstanceOf(Comment.class, nodeWithComment);
        assertEquals("This is a comment", nodeWithComment.toString());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root><img src=\"image.jpg\" /></root>"), "baseUri");
        assertNotNull(xmlDoc);
        Element rootElement = xmlDoc.select("root").first();
        assertNotNull(rootElement);
        List<Node> nodes = rootElement.childNodes();
        Node imgNode = nodes.get(0);
        assertNotNull(imgNode);
        assertInstanceOf(Element.class, imgNode);
        assertEquals("img", imgNode.nodeName());
        assertNull(imgNode.childNodes().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader("Invalid input"), "baseUri");
    }

    @Test
    public void testParseXmlFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><child>Hello</child><child>World</child></root>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase).childNodes();
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        Element child1 = (Element) nodes.get(0);
        assertEquals("child", child1.nodeName());
        TextNode textNode1 = (TextNode) child1.childNodes().get(0);
        assertEquals("Hello", textNode1.text());

        Element child2 = (Element) nodes.get(1);
        assertEquals("child", child2.nodeName());
        TextNode textNode2 = (TextNode) child2.childNodes().get(0);
        assertEquals("World", textNode2.text());
    }
}