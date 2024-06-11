package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment(input, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testProcessStartTag() {
        String input = "<root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment(input, null, null);
        Element root = doc.getRootElement();
        Element child = root.getFirstChild();
        assertNotNull(root);
        assertNotNull(child);
    }

    @Test
    public void testProcessEndTag() {
        String input = "<root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment(input, null, null);
        Element root = doc.getRootElement();
        Element child = root.getFirstChild();
        assertNotNull(root);
        assertNotNull(child);
        assertEquals(root.childNodes().size(), 1);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
        assertEquals(root.childNodes().size(), 0);
    }

    @Test
    public void testProcessComment() {
        String input = "<!-- This is a comment -->";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment(input, null, null);
        List<Node> commentNodes = doc.childNodes();
        assertNotNull(commentNodes);
        assertEquals(commentNodes.size(), 1);
        assertNotNull(commentNodes.get(0));
        assertTrue(commentNodes.get(0) instanceof Comment);
    }

    @Test
    public void testProcessCharacter() {
        String input = "<root>&#x26;<child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment(input, null, null);
        List<Node> textNodes = doc.getElementsByTag("text"); // assuming a custom tag 'text' is added for testing
        assertNotNull(textNodes);
        assertEquals(textNodes.size(), 1);
        assertNotNull(textNodes.get(0));
        assertTrue(textNodes.get(0) instanceof TextNode);
        assertEquals(textNodes.get(0).text(), "&amp;");
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedToken() {
        String input = "<root><!-- This is an comment --><child></child></root>"; // intentional mistake in comment tag
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parseFragment(input, null, null); // expected AssertionError
    }
}