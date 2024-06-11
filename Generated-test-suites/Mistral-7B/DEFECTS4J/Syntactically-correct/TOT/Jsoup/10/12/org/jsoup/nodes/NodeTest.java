package org.jsoup.nodes;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testAbstractNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Node(baseUri, attributes);

        assertEquals("test", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertTrue(node instanceof AbstractNode);
    }

    @Test
    public void testAbstractNodeDefaultConstructor() {
        Node node = new Node();

        assertTrue(node instanceof AbstractNode);
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
        assertNull(node.baseUri());
    }

    @Test
    public void testNodeName() {
        Node node = new Element("div");
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testGetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("test", node.attr("class"));
        assertTrue(node.hasAttr("class"));
        assertEquals("", node.attr("nonExistent"));
    }

    @Test
    public void testSetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);

        node.attr("class", "test");
        assertEquals("test", node.attr("class"));
        assertTrue(node.hasAttr("class"));
    }

    @Test
    public void testHasAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);

        node.attr("class", "test");
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistent"));
    }

    @Test
    public void testRemoveAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
        assertEquals("", node.attr("class"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetAttributeWithNullAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes);

        node.attr("class", "test");
        node.removeAttr("class");
        String nullAttributeKey = null;
        node.attr(nullAttributeKey);
    }

    @Test
    public void testGetAbsoluteUrl() {
        String baseUri = "http://example.com";
        String relativeUrl = "images/logo.png";
        Node node = new Element("img", baseUri, new Attributes().put("src", relativeUrl));

        String absoluteUrl = node.absUrl("src");
        assertEquals("http://example.com/images/logo.png", absoluteUrl);
    }

    @Test
    public void testGetAbsoluteUrlWithEmptyBaseUri() {
        String relativeUrl = "images/logo.png";
        Node node = new Element("img", "", new Attributes().put("src", relativeUrl));

        String absoluteUrl = node.absUrl("src");
        assertTrue(absoluteUrl.isEmpty());
    }

    @Test
    public void testGetAbsoluteUrlWithMalformedBaseUri() {
        String baseUri = "http://example.com/";
        String relativeUrl = "images/logo.png";
        Node node = new Element("img", baseUri, new Attributes().put("src", relativeUrl));

        try {
            node.absUrl("src");
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {}
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);

        parentNode.addChild(childNode);
        Node retrievedChildNode = parentNode.childNode(0);

        assertEquals(childNode, retrievedChildNode);
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertTrue(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }

    @Test
    public void testGetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Element("p", baseUri, attributes);
        Node parentNode = new Element("div", baseUri, attributes);

        parentNode.addChild(childNode);
        Node retrievedParentNode = childNode.parent();

        assertEquals(parentNode, retrievedParentNode);
    }

    @Test
    public void testRemoveNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);

        parentNode.addChild(childNode);
        parentNode.removeChild(childNode);

        List<Node> children = parentNode.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testReplaceNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChildNode = new Element("p", baseUri, attributes);
        Node newChildNode = new Element("span", baseUri, attributes);

        parentNode.addChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);

        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertTrue(children.contains(newChildNode));
        assertFalse(children.contains(oldChildNode));
    }
}