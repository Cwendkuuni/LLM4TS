package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.junit.Test;

/**
 * Test file for Node class.
 */
public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);

        assertEquals(baseUri, node.baseUri());
        assertEquals("node1", node.nodeName());
        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
        assertFalse(node.hasAttr("id"));
        assertFalse(node.hasAttr("class"));
        assertEquals("", node.attr("id"));
        assertEquals("", node.attr("class"));

        assertEquals(node, node.replaceWith(node));
        assertEquals(node, node.remove());
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node node = new Node();

        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testGetNodeName() {
        Node node = new Element("div");

        assertEquals("div", node.nodeName());
    }

    @Test
    public void testGetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "node1");
        attributes.put("class", "node-class");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("node1", node.attr("id"));
        assertEquals("node-class", node.attr("class"));
        assertEquals("", node.attr("non-existent-attr"));
    }

    @Test
    public void testGetAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "node1");
        attributes.put("class", "node-class");
        Node node = new Element("div", baseUri, attributes);

        Attributes nodeAttributes = node.attributes();

        assertEquals(2, nodeAttributes.size());
        assertTrue(nodeAttributes.get("id").equals("node1"));
        assertTrue(nodeAttributes.get("class").equals("node-class"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "node1");
        attributes.put("class", "node-class");
        Node node = new Element("div", baseUri, attributes);

        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("non-existent-attr"));
    }

    @Test
    public void testSetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node node = new Element("div", baseUri, attributes);

        node.attr("id", "new-id");
        assertEquals("new-id", node.attr("id"));

        node.attr("class", "new-class");
        assertEquals("new-class", node.attr("class"));

        node.attr("new-attribute", "new-value");
        assertEquals("new-value", node.attr("new-attribute"));
    }

    @Test
    public void testRemoveAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "node1");
        attributes.put("class", "node-class");
        Node node = new Element("div", baseUri, attributes);

        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
        assertEquals("", node.attr("id"));

        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
        assertEquals("", node.attr("class"));
    }

    @Test
    public void testGetBaseUri() {
        String baseUri = "http://example.com";
        String childBaseUri = "http://child.example.com";
        Attributes attributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", childBaseUri, attributes);
        childNode.setParentNode(parentNode);

        assertEquals(baseUri, parentNode.baseUri());
        assertEquals(childBaseUri, childNode.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node node = new Element("div", baseUri, attributes);

        String newBaseUri = "http://new-example.com";
        node.setBaseUri(newBaseUri);

        assertEquals(newBaseUri, node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testSetBaseUriNull() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node node = new Element("div", baseUri, attributes);

        node.setBaseUri(null);
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String childUri = "relative.html";
        String absoluteUrl = "http://example.com/relative.html";
        Attributes attributes = new AttributesImpl();
        Node node = new Element("div", baseUri, attributes);

        assertEquals("", node.absUrl(""));
        assertEquals("", node.absUrl("non-existent-attr"));
        assertEquals(childUri, node.absUrl("href"));
        assertEquals(absoluteUrl, node.absUrl("abs:href"));
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);

        assertEquals(childNode1, parentNode.childNode(0));
        assertEquals(childNode2, parentNode.childNode(1));
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testGetChildrenAsArray() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);
        parentNode.addChildren(childNode1, childNode2);

        Node[] childrenAsArray = parentNode.childNodesAsArray();

        assertEquals(2, childrenAsArray.length);
        assertEquals(childNode1, childrenAsArray[0]);
        assertEquals(childNode2, childrenAsArray[1]);
    }

    @Test
    public void testGetParentNode() {
        String baseUri = "http://example.com";
        Attributes parentAttributes = new AttributesImpl();
        Attributes childAttributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node childNode = new Element("p", baseUri, childAttributes);
        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parentNode());
        assertNull(parentNode.parentNode());
    }

    @Test
    public void testGetOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        childNode.setParentNode(parentNode);

        Document ownerDocument = parentNode.ownerDocument();

        assertEquals(parentNode, ownerDocument.getElementsByTag("div").first());
        assertNull(childNode.ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        parentNode.addChildren(childNode);

        parentNode.remove();

        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://example.com";
        Attributes parentAttributes = new AttributesImpl();
        Attributes childAttributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node childNode1 = new Element("p", baseUri, childAttributes);
        Node childNode2 = new Element("p", baseUri, childAttributes);
        parentNode.addChildren(childNode1, childNode2);

        Node newChildNode = new Element("span", baseUri, childAttributes);

        parentNode.replaceWith(newChildNode);

        assertEquals(1, parentNode.childNodes().size());
        assertEquals(newChildNode, parentNode.childNodes().get(0));
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Attributes parentAttributes = new AttributesImpl();
        Attributes childAttributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node childNode = new Element("p", baseUri, childAttributes);

        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parentNode());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes parentAttributes = new AttributesImpl();
        Attributes childAttributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node oldChildNode = new Element("p", baseUri, childAttributes);
        Node newChildNode = new Element("span", baseUri, childAttributes);
        parentNode.addChildren(oldChildNode);

        parentNode.replaceChild(oldChildNode, newChildNode);

        assertEquals(1, parentNode.childNodes().size());
        assertEquals(newChildNode, parentNode.childNodes().get(0));
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes parentAttributes = new AttributesImpl();
        Attributes childAttributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, parentAttributes);
        Node oldChildNode = new Element("p", baseUri, childAttributes);
        Node newChildNode = new Element("span", baseUri, childAttributes);
        parentNode.addChildren(oldChildNode);

        parentNode.removeChild(oldChildNode);

        assertEquals(1, parentNode.childNodes().size());
        assertEquals(newChildNode, parentNode.childNodes().get(0));
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);

        assertEquals(2, parentNode.childNodes().size());
        assertEquals(childNode1, parentNode.childNodes().get(0));
        assertEquals(childNode2, parentNode.childNodes().get(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);

        parentNode.addChildren(1, childNode1, childNode2);

        assertEquals(2, parentNode.childNodes().size());
        assertEquals(childNode1, parentNode.childNodes().get(0));
        assertEquals(childNode2, parentNode.childNodes().get(1));
    }
}