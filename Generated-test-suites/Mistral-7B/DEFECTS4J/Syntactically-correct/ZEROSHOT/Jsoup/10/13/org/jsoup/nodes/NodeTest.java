package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals("test", node.attr("id"));
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertTrue(node.childNodes().isEmpty());
        Assert.assertNull(node.parentNode());
    }

    @Test
    public void testNodeCreationWithoutAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertNull(node.attributes());
        Assert.assertTrue(node.childNodes().isEmpty());
        Assert.assertNull(node.parentNode());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        Attributes attributes = new Attributes();
        new Node(null, attributes);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributes() {
        String baseUri = "http://example.com";
        new Node(baseUri, null);
    }

    @Test
    public void testAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals("test", node.attr("id"));
        Assert.assertEquals("", node.attr("nonexistent"));

        String absUri = node.absUrl("href");
        Assert.assertEquals("", absUri);

        node.attr("href", "test.html");
        absUri = node.absUrl("href");
        Assert.assertEquals("test.html", absUri);
    }

    @Test
    public void testAttributesMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        Attributes attributesTest = node.attributes();
        Assert.assertEquals("id", attributesTest.keySet().iterator().next());
        Assert.assertEquals("test", attributesTest.get("id"));
    }

    @Test
    public void testHasAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        Assert.assertTrue(node.hasAttr("id"));
        Assert.assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testRemoveAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals("test", node.attr("id"));
        node.removeAttr("id");
        Assert.assertEquals("", node.attr("id"));
    }

    @Test
    public void testSetBaseUriMethod() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        node.setBaseUri("http://test.com");
        Assert.assertEquals("http://test.com", node.baseUri());
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);

        parentNode.childNodes.add(child1);
        parentNode.childNodes.add(child2);

        Assert.assertEquals(child1, parentNode.childNode(0));
        Assert.assertEquals(child2, parentNode.childNode(1));
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);

        parentNode.childNodes.add(child1);
        parentNode.childNodes.add(child2);

        List<Node> children = parentNode.childNodes();
        Assert.assertEquals(2, children.size());
        Assert.assertEquals(child1, children.get(0));
        Assert.assertEquals(child2, children.get(1));
    }

    @Test
    public void testChildNodesAsArray() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);

        parentNode.childNodes.add(child1);
        parentNode.childNodes.add(child2);

        Node[] children = parentNode.childNodesAsArray();
        Assert.assertArrayEquals(new Node[]{child1, child2}, children);
    }

    @Test
    public void testGetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);

        childNode.setParentNode(parentNode);

        Assert.assertEquals(parentNode, childNode.parentNode());
        Assert.assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testGetOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        Assert.assertNull(node.ownerDocument());

        Node parentNode = new Node(baseUri, attributes);
        parentNode.childNodes.add(node);

        Assert.assertEquals(parentNode, node.ownerDocument());
    }

    @Test
    public void testRemoveNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node nodeToRemove = new Node(baseUri, attributes);

        parentNode.childNodes.add(nodeToRemove);
        parentNode.removeChild(nodeToRemove);

        List<Node> children = parentNode.childNodes();
        Assert.assertEquals(0, children.size());
    }

    @Test
    public void testReplaceNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node nodeToReplace = new Node(baseUri, attributes);
        Node newNode = new Node(baseUri, attributes);

        parentNode.childNodes.add(nodeToReplace);
        parentNode.replaceChild(nodeToReplace, newNode);

        List<Node> children = parentNode.childNodes();
        Assert.assertEquals(1, children.size());
        Assert.assertEquals(newNode, children.get(0));
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri, attributes);
        Node parentNode = new Node(baseUri, attributes);

        childNode.setParentNode(parentNode);
        Assert.assertEquals(parentNode, childNode.parentNode());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);

        parentNode.childNodes.add(child1);
        parentNode.childNodes.add(child2);

        parentNode.replaceChild(child1, child2);

        List<Node> children = parentNode.childNodes();
        Assert.assertEquals(1, children.size());
        Assert.assertEquals(child2, children.get(0));
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);

        parentNode.addChildren(child1, child2);

        List<Node> children = parentNode.childNodes();
        Assert.assertEquals(2, children.size());
        Assert.assertEquals(child1, children.get(0));
        Assert.assertEquals(child2, children.get(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);

        parentNode.addChildren(1, child1, child2);

        List<Node> children = parentNode.childNodes();
        Assert.assertEquals(2, children.size());
        Assert.assertEquals(child1, children.get(0));
        Assert.assertEquals(child2, children.get(1));
    }

    @Test
    public void testSiblingNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node sibling1 = new Node(baseUri, attributes);
        Node sibling2 = new Node(baseUri, attributes);
        Node testNode = new Node(baseUri, attributes);

        parentNode.childNodes.add(sibling1);
        parentNode.childNodes.add(testNode);
        parentNode.childNodes.add(sibling2);

        List<Node> siblings = testNode.siblingNodes();
        Assert.assertEquals(2, siblings.size());
        Assert.assertEquals(sibling1, siblings.get(0));
        Assert.assertEquals(sibling2, siblings.get(1));
    }

    @Test
    public void testNextSibling() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node sibling1 = new Node(baseUri, attributes);
        Node testNode = new Node(baseUri, attributes);
        Node sibling2 = new Node(baseUri, attributes);

        parentNode.childNodes.add(sibling1);
        parentNode.childNodes.add(testNode);
        parentNode.childNodes.add(sibling2);

        Assert.assertNull(testNode.nextSibling());
        Assert.assertEquals(sibling2, testNode.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node sibling1 = new Node(baseUri, attributes);
        Node testNode = new Node(baseUri, attributes);
        Node sibling2 = new Node(baseUri, attributes);

        parentNode.childNodes.add(sibling1);
        parentNode.childNodes.add(testNode);
        parentNode.childNodes.add(sibling2);

        Assert.assertNull(testNode.previousSibling());
        Assert.assertEquals(sibling1, testNode.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node sibling1 = new Node(baseUri, attributes);
        Node testNode = new Node(baseUri, attributes);
        Node sibling2 = new Node(baseUri, attributes);

        parentNode.childNodes.add(sibling1);
        parentNode.childNodes.add(testNode);
        parentNode.childNodes.add(sibling2);

        Assert.assertEquals(1, testNode.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node = new Element("div", baseUri, attributes);
        node.text("Test Node");

        StringBuilder accum = new StringBuilder();
        node.outerHtml(accum);

        String expected = "<div class=\"test\">Test Node</div>";
        Assert.assertEquals(expected, accum.toString());
    }
}