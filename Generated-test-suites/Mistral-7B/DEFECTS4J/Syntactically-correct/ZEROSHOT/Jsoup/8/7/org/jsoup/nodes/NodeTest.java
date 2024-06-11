package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Attributes;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.AttributesImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {

    @org.junit.Test
    public void testNodeWithBaseUriAndAttributes() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "myId");
        attributes.put("class", "myClass");

        Node node = new Element("div", baseUri, attributes);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals("myId", node.attr("id"));
        assertEquals("myClass", node.attr("class"));
        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonexistent"));
        assertEquals("myId myClass", node.attr("class", ""));
        assertFalse(node.hasAttr("abs:nonexistent"));
        assertEquals("", node.absUrl("nonexistent"));
    }

    @org.junit.Test
    public void testNodeWithoutBaseUriOrAttributes() {
        Node node = new Node();

        assertEquals("", node.nodeName());
        assertNull(node.baseUri());
        assertFalse(node.hasAttr("id"));
        assertFalse(node.hasAttr("class"));
        assertEquals(0, node.childNodes().size());
        assertNull(node.parent());
    }

    @org.junit.Test
    public void testNodeWithChildren() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "myId");
        attributes.put("class", "myClass");

        Node parentNode = new Element("div", baseUri, attributes);

        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new TextNode("Text node");

        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
        assertEquals(parentNode, childNode1.parent());
        assertEquals(parentNode, childNode2.parent());
        assertEquals(parentNode, childNode1.ownerDocument());
        assertEquals(parentNode, childNode2.ownerDocument());
    }

    @org.junit.Test
    public void testNodeWithParent() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "myId");
        attributes.put("class", "myClass");

        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);

        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parent());
        assertEquals(parentNode, childNode.ownerDocument());
    }

    @org.junit.Test
    public void testAbsUrl() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new AttributesImpl();

        Node node = new Element("a", baseUri, attributes);
        node.attr("href", "relative.html");

        String absUrl = node.absUrl("href");

        assertEquals("http://www.example.com/relative.html", absUrl);

        attributes.put("abs:href", "absolute.html");

        absUrl = node.absUrl("href");

        assertEquals("http://www.example.com/absolute.html", absUrl);
    }

    @org.junit.Test
    public void testNodeWithSiblingNodes() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new AttributesImpl();

        Node parentNode = new Element("div", baseUri, attributes);

        Node sibling1 = new Element("p", baseUri, attributes);
        Node node = new Element("p", baseUri, attributes);
        Node sibling2 = new Element("p", baseUri, attributes);

        parentNode.appendChild(sibling1);
        parentNode.appendChild(node);
        parentNode.appendChild(sibling2);

        assertEquals(3, parentNode.childNodes().size());
        assertEquals(sibling1, node.previousSibling());
        assertEquals(sibling2, node.nextSibling());
        assertNull(node.previousSibling(1));
    }

    @org.junit.Test
    public void testNodeWithListOfSiblingNodes() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new AttributesImpl();

        Node parentNode = new Element("div", baseUri, attributes);

        Node sibling1 = new Element("p", baseUri, attributes);
        Node sibling2 = new Element("p", baseUri, attributes);
        Node node = new Element("p", baseUri, attributes);

        parentNode.appendChild(sibling1);
        parentNode.appendChild(node);
        parentNode.appendChild(sibling2);

        List<Node> siblings = parentNode.siblingNodes();

        assertEquals(3, siblings.size());
        assertEquals(sibling1, siblings.get(0));
        assertEquals(node, siblings.get(1));
        assertEquals(sibling2, siblings.get(2));
    }
}