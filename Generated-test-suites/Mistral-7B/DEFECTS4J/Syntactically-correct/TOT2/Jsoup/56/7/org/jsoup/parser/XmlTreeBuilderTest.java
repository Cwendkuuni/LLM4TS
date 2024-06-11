package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        String xmlInput = "<root><child></child></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "");
        assertNotNull(xmlDoc);
        assertNotNull(xmlDoc.rootElement());
    }

    @Test
    public void testParseMethod() {
        String xmlInput = "<root><child><grandChild></grandChild></child></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "");
        Element root = xmlDoc.rootElement();
        Element child = root.element("child");
        Element grandChild = child.element("grandChild");
        assertNotNull(root);
        assertNotNull(child);
        assertNotNull(grandChild);
    }

    @Test
    public void testProcessStartTag() {
        String xmlInput = "<root><child></child></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "");
        Element root = xmlDoc.rootElement();
        Element child = root.element("child");
        assertNotNull(root);
        assertNotNull(child);
    }

    @Test
    public void testProcessEndTag() {
        String xmlInput = "<root><child><grandChild></grandChild></child></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "");
        Element root = xmlDoc.rootElement();
        Element child = root.element("child");
        Element grandChild = child.element("grandChild");
        root.remove(child);
        assertNotNull(root);
        assertNull(child);
    }

    @Test
    public void testProcessComment() {
        String xmlInput = "<!-- This is a comment -->";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "");
        List<Node> nodes = xmlDoc.childNodes();
        Node comment = nodes.get(0);
        assertNotNull(comment);
        assertTrue(comment instanceof Comment);
    }

    @Test
    public void testProcessCharacter() {
        String xmlInput = "<root>&#x304;abc&#x0308;</root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "");
        Element root = xmlDoc.rootElement();
        List<Node> nodes = root.childNodes();
        TextNode textNode = (TextNode) nodes.get(0);
        String text = textNode.text();
        assertEquals("ābc̄", text);
    }

    @Test(expected = RuntimeException.class)
    public void testProcessUnknownTag() {
        String xmlInput = "<root><unknownTag></unknownTag></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "");
    }

    @Test
    public void testProcessSelfClosingTag() {
        String xmlInput = "<root><selfClosingTag/></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "");
        Element root = xmlDoc.rootElement();
        Element selfClosingTag = root.element("selfClosingTag");
        assertNotNull(root);
        assertNotNull(selfClosingTag);
        assertEquals(2, root.childNodeSize());
    }

    @Test
    public void testPopStackToClose() {
        String xmlInput = "<root><child><grandChild></grandChild></child></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "");
        Element root = xmlDoc.rootElement();
        Element child = root.element("child");
        Element grandChild = child.element("grandChild");
        root.remove(child);
        assertEquals(1, root.childNodeSize());
    }

    @Test
    public void testParseFragment() {
        String xmlInput = "<root><child><grandChild></grandChild></child></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlInput, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0).childNodes();
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        Element grandChild = (Element) nodes.get(2);
        assertNotNull(root);
        assertNotNull(child);
        assertNotNull(grandChild);
    }
}