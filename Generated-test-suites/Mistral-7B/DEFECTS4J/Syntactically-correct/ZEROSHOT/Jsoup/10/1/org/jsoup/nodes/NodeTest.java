package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    private Node node;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        node = new Node(baseUri, attributes);
    }

    @Test
    public void testNodeName() {
        Assert.assertEquals("Abstract Node", node.nodeName());
    }

    @Test
    public void testAttr() {
        Assert.assertEquals("", node.attr(""));
        Assert.assertEquals("test", node.attr("class"));
        Assert.assertEquals("", node.attr("nonExistent"));

        node.attr("id", "testId");
        Assert.assertEquals("testId", node.attr("id"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = node.attributes();
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("test", attributes.get("class"));
    }

    @Test
    public void testHasAttr() {
        Assert.assertTrue(node.hasAttr("class"));
        Assert.assertFalse(node.hasAttr("nonExistent"));
    }

    @Test
    public void testRemoveAttr() {
        node.removeAttr("class");
        Assert.assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        Assert.assertEquals(baseUri, node.baseUri());
        node.setBaseUri("http://another-example.com");
        Assert.assertEquals("http://another-example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Assert.assertEquals("", node.absUrl(""));

        node.attr("href", "relative.html");
        Assert.assertEquals("http://example.com/relative.html", node.absUrl("href"));

        node.setBaseUri("http://example.com/path");
        Assert.assertEquals("http://example.com/path/relative.html", node.absUrl("href"));
    }

    @Test
    public void testChildNodes() {
        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);

        node.addChildren(child1, child2);

        List<Node> childNodes = node.childNodes();
        Assert.assertEquals(2, childNodes.size());
        Assert.assertEquals(child1, childNodes.get(0));
        Assert.assertEquals(child2, childNodes.get(1));

        List<Node> childNodesAsArray = node.childNodesAsArray();
        Assert.assertArrayEquals(new Node[] { child1, child2 }, childNodesAsArray);
    }

    @Test
    public void testParent() {
        Node parent = new Node(baseUri);
        parent.addChildren(node);

        Assert.assertEquals(parent, node.parent());
        Assert.assertNull(node.parent()); // after remove
    }

    @Test
    public void testOwnerDocument() {
        Document document = new Document();
        document.appendChild(node);

        Assert.assertEquals(document, node.ownerDocument());
        Assert.assertNull(node.ownerDocument()); // after remove
    }

    @Test
    public void testRemove() {
        Node parent = new Node(baseUri);
        parent.addChildren(node);

        Assert.assertEquals(1, parent.childNodes().size());
        node.remove();
        Assert.assertEquals(0, parent.childNodes().size());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new Node(baseUri);
        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);

        parent.addChildren(child1);
        parent.addChildren(child2);

        Assert.assertEquals(2, parent.childNodes().size());

        child1.replaceWith(new Node(baseUri));

        Assert.assertEquals(1, parent.childNodes().size());
        Assert.assertEquals(child2, parent.childNodes().get(0));
    }

    @Test
    public void testClone() {
        Node parent = new Node(baseUri);
        Node clonedNode = parent.clone();

        parent.addChildren(node.clone());
        Assert.assertEquals(1, parent.childNodes().size());

        node.setBaseUri("http://cloned-example.com");
        Assert.assertEquals("http://example.com", parent.childNodes().get(0).baseUri());
        Assert.assertEquals("http://cloned-example.com", clonedNode.baseUri());
    }
}