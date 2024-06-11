package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {

    private Node node;
    private Document document;
    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
        String baseUri = "http://example.com";
        node = new Node(baseUri, attributes);
        document = new Document();
        document.setBaseUri(baseUri);
    }

    @Test
    public void testNodeName() {
        assertNull(node.nodeName());
    }

    @Test
    public void testAttr() {
        node.attr("key", "value");
        assertEquals("value", node.attr("key"));
        assertEquals("", node.attr("nonExistentKey"));
    }

    @Test
    public void testAttributes() {
        node.attr("key1", "value1");
        node.attr("key2", "value2");
        List<String> keys = new ArrayList<>(Arrays.asList("key1", "key2"));
        List<String> values = new ArrayList<>(Arrays.asList("value1", "value2"));
        assertEquals(values, node.attributes().getAll());
        assertEquals(keys, node.attributes().keySet());
    }

    @Test
    public void testHasAttr() {
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertFalse(node.hasAttr("nonExistentKey"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("key", "value");
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testBaseUri() {
        assertEquals(baseUri, node.baseUri());
        node.setBaseUri("http://newbase.com");
        assertEquals("http://newbase.com", node.baseUri());
    }

    @Test
    public void testGetChildNode() {
        Node childNode = new Node(baseUri, attributes);
        node.addChild(childNode);
        assertEquals(childNode, node.childNode(0));
    }

    @Test
    public void testGetChildren() {
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);
        node.addChildren(childNode1, childNode2);
        List<Node> children = node.childNodes();
        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testGetParent() {
        Document doc = new Document();
        doc.setBaseUri(baseUri);
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChild(node);
        assertEquals(parentNode, node.parent());
    }

    @Test
    public void testRemove() {
        Document doc = new Document();
        doc.setBaseUri(baseUri);
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChild(node);
        node.remove();
        List<Node> children = parentNode.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testReplaceWith() {
        Document doc = new Document();
        doc.setBaseUri(baseUri);
        Node parentNode = new Node(baseUri, attributes);
        Node nodeToReplace = new Node(baseUri, attributes);
        parentNode.addChild(nodeToReplace);
        Node newNode = new Node(baseUri, attributes);
        parentNode.replaceWith(newNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(newNode, children.get(0));
    }

    @Test
    public void testAbsUrl() {
        node.attr("href", "relative.html");
        assertEquals("", node.absUrl("href"));
        assertEquals("http://example.com/relative.html", node.absUrl("abs:href"));
    }

    @Test
    public void testSetParentNode() {
        Node parentNode = new Node(baseUri, attributes);
        node.setParentNode(parentNode);
        assertEquals(parentNode, node.parent());
    }

    @Test
    public void testReplaceChild() {
        Node oldChild = new Node(baseUri, attributes);
        Node newChild = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChild(oldChild);
        parentNode.replaceChild(oldChild, newChild);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(newChild, children.get(0));
    }

    @Test
    public void testRemoveChild() {
        Node oldChild = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChild(oldChild);
        parentNode.removeChild(oldChild);
        List<Node> children = parentNode.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testAddChildren() {
        Node parentNode = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parentNode.addChildren(child1, child2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node parentNode = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parentNode.addChildren(1, child1, child2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testSiblingNodes() {
        Node sibling1 = new Node(baseUri, attributes);
        Node sibling2 = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChild(node);
        parentNode.addChild(sibling1);
        parentNode.addChild(sibling2);
        List<Node> siblings = node.siblingNodes();
        assertEquals(2, siblings.size());
        assertEquals(sibling1, siblings.get(0));
        assertEquals(sibling2, siblings.get(1));
    }

    @Test
    public void testNextSibling() {
        Node sibling1 = new Node(baseUri, attributes);
        Node sibling2 = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChild(node);
        parentNode.addChild(sibling1);
        parentNode.addChild(sibling2);
        assertEquals(sibling1, node.nextSibling());
        assertEquals(null, node.nextSibling(sibling1));
    }

    @Test
    public void testPreviousSibling() {
        Node sibling1 = new Node(baseUri, attributes);
        Node sibling2 = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);
        parentNode.addChild(node);
        parentNode.addChild(sibling1);
        parentNode.addChild(sibling2);
        assertEquals(null, node.previousSibling());
        assertEquals(sibling1, node.previousSibling(sibling2));
    }
}