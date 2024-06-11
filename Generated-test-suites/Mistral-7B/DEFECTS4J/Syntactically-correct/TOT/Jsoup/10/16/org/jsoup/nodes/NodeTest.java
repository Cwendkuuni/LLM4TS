package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Whitespace.whitespace;

public class NodeTest {

    private Document doc;

    @Before
    public void setUp() {
        doc = new Document();
    }

    @Test
    public void testNodeCreation() {
        Node node = new Node("", new Attributes());
        assertNotNull(node);
        assertEquals("", node.nodeName());
        assertNull(node.parentNode());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeCreationWithBaseUriAndAttributes() {
        Node node = new Node("", new Attributes().put("id", "test"));
        assertNotNull(node);
        assertEquals("", node.nodeName());
        assertNull(node.parentNode());
        assertEquals("test", node.attr("id"));
        assertNull(node.attributes());
        assertEquals("", node.baseUri());
    }

    @Test
    public void testNodeCreationWithBaseUri() {
        Node node = new Node("http://test.com/", new Attributes());
        assertNotNull(node);
        assertEquals("", node.nodeName());
        assertNull(node.parentNode());
        assertEquals("", node.attributes().toString());
        assertEquals("http://test.com/", node.baseUri());
    }

    @Test
    public void testNodeCreationWithBaseUriAndAttributesEmpty() {
        Node node = new Node("", new Attributes());
        assertNotNull(node);
        assertEquals("", node.nodeName());
        assertNull(node.parentNode());
        assertNotNull(node.attributes());
        assertTrue(node.attributes().isEmpty());
        assertEquals("", node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testNodeCreationWithNullBaseUri() {
        new Node(null, new Attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testNodeCreationWithNullAttributes() {
        new Node("", null);
    }

    @Test
    public void testAttributeGetters() {
        Attributes attributes = new Attributes().put("id", "test").put("class", "test-class");
        Node node = new Node("", attributes);
        assertEquals("test", node.attr("id"));
        assertEquals("test-class", node.attr("class"));
        assertEquals("test", node.attr("id").toLowerCase());
        assertFalse(node.hasAttr("non-existent-attribute"));
        assertEquals("", node.attr("non-existent-attribute"));
    }

    @Test
    public void testAttributeSetting() {
        Node node = new Node("", new Attributes());
        node.attr("id", "test");
        node.attr("class", "test-class");
        assertEquals("test", node.attr("id"));
        assertEquals("test-class", node.attr("class"));
    }

    @Test
    public void testAttributeRemoval() {
        Node node = new Node("", new Attributes().put("id", "test").put("class", "test-class"));
        node.removeAttr("id");
        node.removeAttr("class");
        assertFalse(node.hasAttr("id"));
        assertFalse(node.hasAttr("class"));
        assertEquals("", node.attr("id"));
        assertEquals("", node.attr("class"));
    }

    @Test
    public void testGetBaseUri() {
        Node node = new Node("http://test.com/", new Attributes());
        assertEquals("http://test.com/", node.baseUri());

        Node node2 = new Node("", new Attributes().put("base", "http://test.com/"));
        assertEquals("http://test.com/", node2.baseUri());

        Node node3 = new Node("", new Attributes().put("base", "http://test.com/"));
        Node child = new Node("", new Attributes().put("href", "child.html"));
        node3.appendChild(child);
        assertEquals("http://test.com/child.html", child.absUrl("href"));
    }

    @Test(expected = MalformedURLException.class)
    public void testGetBaseUriWithMalformedUrl() {
        Node node = new Node("", new Attributes().put("base", "http://test.com:/80/"));
        node.childNodes().add(new TextNode(""));
        new Node("", new Attributes().put("href", "test"));
        node.childNodes().get(0).appendChild(node.childNodes().get(1));
        node.childNodes().get(1).absUrl("href");
    }

    @Test
    public void testGetChildNodes() {
        Element element = new Element("div", new Attributes());
        TextNode textNode1 = new TextNode("Text node 1");
        TextNode textNode2 = new TextNode("Text node 2");
        TextNode textNode3 = new TextNode("Text node 3");
        element.appendChild(textNode1);
        element.appendChild(textNode2);
        element.appendChild(textNode3);

        List<Node> childNodes = element.childNodes();
        assertEquals(3, childNodes.size());
        assertEquals(textNode1, childNodes.get(0));
        assertEquals(textNode2, childNodes.get(1));
        assertEquals(textNode3, childNodes.get(2));
    }

    @Test
    public void testGetFirstChild() {
        Element element = new Element("div", new Attributes());
        TextNode textNode1 = new TextNode("Text node 1");
        TextNode textNode2 = new TextNode("Text node 2");
        TextNode textNode3 = new TextNode("Text node 3");
        element.appendChild(textNode1);
        element.appendChild(textNode2);
        element.appendChild(textNode3);

        Node firstChild = element.childNode(0);
        assertEquals(textNode1, firstChild);
    }

    @Test
    public void testGetLastChild() {
        Element element = new Element("div", new Attributes());
        TextNode textNode1 = new TextNode("Text node 1");
        TextNode textNode2 = new TextNode("Text node 2");
        TextNode textNode3 = new TextNode("Text node 3");
        element.appendChild(textNode1);
        element.appendChild(textNode2);
        element.appendChild(textNode3);

        Node lastChild = element.childNode(2);
        assertEquals(textNode3, lastChild);
    }

    @Test
    public void testParentNode() {
        Element element = new Element("div", new Attributes());
        TextNode textNode = new TextNode("Text node");
        element.appendChild(textNode);

        assertEquals(element, textNode.parentNode());
        assertEquals(null, textNode.parent());
    }

    @Test
    public void testParentNodeNull() {
        Node node = new Node("");
        assertNull(node.parentNode());
    }

    @Test
    public void testSetParentNode() {
        Element parent = new Element("div", new Attributes());
        Node child = new Node("", new Attributes());
        parent.appendChild(child);
        child.setParentNode(parent);
        assertEquals(parent, child.parentNode());
    }

    @Test
    public void testReplaceChild() {
        Element parent = new Element("div", new Attributes());
        Node child1 = new Node("", new Attributes().put("id", "child1"));
        Node child2 = new Node("", new Attributes().put("id", "child2"));
        parent.appendChild(child1);
        parent.replaceChild(child1, child2);

        assertEquals(child2, parent.childNode(0));
        assertEquals(child1, parent.childNode(1));
        assertEquals(child2, child1.parentNode().childNode(1));
        assertEquals(child1, child2.parentNode().childNode(0));
    }

    @Test
    public void testRemoveChild() {
        Element parent = new Element("div", new Attributes());
        Node child1 = new Node("", new Attributes().put("id", "child1"));
        Node child2 = new Node("", new Attributes().put("id", "child2"));
        parent.appendChild(child1);
        parent.appendChild(child2);
        parent.removeChild(child1);

        assertEquals(child2, parent.childNode(0));
        assertEquals(null, parent.childNode(1));
        assertEquals(null, child1.parentNode());
    }

    @Test
    public void testAddChildren() {
        Element parent = new Element("div", new Attributes());
        Node child1 = new Node("", new Attributes().put("id", "child1"));
        Node child2 = new Node("", new Attributes().put("id", "child2"));
        parent.addChildren(child1, child2);

        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNodes().get(0));
        assertEquals(child2, parent.childNodes().get(1));
    }

    @Test
    public void testRemove() {
        Element parent = new Element("div", new Attributes());
        Node child = new Node("", new Attributes());
        parent.appendChild(child);
        child.remove();

        assertEquals(0, parent.childNodes().size());
        assertEquals(null, child.parentNode());
    }

    @Test
    public void testOuterHtml() {
        Element element = new Element("div", new Attributes().put("id", "test"));
        TextNode textNode1 = new TextNode("Text node 1");
        TextNode textNode2 = new TextNode("Text node 2");
        element.appendChild(textNode1);
        element.appendChild(textNode2);

        StringBuilder accum = new StringBuilder(32 * 1024);
        element.outerHtml(accum);

        assertEquals("<div id=\"test\">Text node 1Text node 2</div>", accum.toString());
    }

    @Test
    public void testOuterHtmlWithIndentation() {
        Element element = new Element("div", new Attributes().put("id", "test"));
        TextNode textNode1 = new TextNode("Text node 1");
        TextNode textNode2 = new TextNode("Text node 2");
        element.appendChild(textNode1);
        element.appendChild(textNode2);

        Document.OutputSettings outputSettings = new Document.OutputSettings().indentAmount(2);
        StringBuilder accum = new StringBuilder(32 * 1024);
        element.outerHtml(accum, 0, outputSettings);

        assertEquals("<div id=\"test\">\nText node 1\nText node 2\n</div>", accum.toString());
    }

    @Test
    public void testOuterHtmlWithWhitespace() {
        Element element = new Element("div", new Attributes().put("id", "test"));
        TextNode textNode1 = new TextNode("Text node 1 ");
        TextNode textNode2 = new TextNode("Text node 2\n");
        element.appendChild(textNode1);
        element.appendChild(textNode2);

        Document.OutputSettings outputSettings = new Document.OutputSettings().indentAmount(2).preserveWhitespace(true);
        StringBuilder accum = new StringBuilder(32 * 1024);
        element.outerHtml(accum, 0, outputSettings);

        assertEquals("<div id=\"test\">\nText node 1 \nText node 2\n\n</div>", accum.toString());
    }

    @Test
    public void testOuterHtmlWithCustomIndentation() {
        Element element = new Element("div", new Attributes().put("id", "test"));
        TextNode textNode1 = new TextNode("Text node 1");
        TextNode textNode2 = new TextNode("Text node 2");
        element.appendChild(textNode1);
        element.appendChild(textNode2);

        Document.OutputSettings outputSettings = new Document.OutputSettings().indentAmount(4).indentStrings(whitespace(5, " "));
        StringBuilder accum = new StringBuilder(32 * 1024);
        element.outerHtml(accum, 0, outputSettings);

        assertEquals("<div id=\"test\">\t\tText node 1\n\t\tText node 2\n</div>", accum.toString());
    }
}