package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test file for Node class.
 */
public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals(Node.class, node.getClass());
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertNotNull(node.attributes());
        Assert.assertEquals(attributes, node.attributes());
        Assert.assertNull(node.parentNode);
        Assert.assertEquals(Collections.emptyList(), node.childNodes());
    }

    @Test
    public void testNodeCreationWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        Assert.assertEquals(Node.class, node.getClass());
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertNull(node.attributes());
        Assert.assertNull(node.parentNode);
        Assert.assertEquals(Collections.emptyList(), node.childNodes());
    }

    @Test(expected = NullPointerException.class)
    public void testNodeCreationWithNullBaseUri() {
        Attributes attributes = new Attributes();
        new Node(null, attributes);
    }

    @Test(expected = NullPointerException.class)
    public void testNodeCreationWithNullAttributes() {
        String baseUri = "http://example.com";
        new Node(baseUri, null);
    }

    @Test
    public void testNodeAbstractMethods() {
        Assert.assertThrows(AbstractMethodError.class, Node::nodeName);
        Assert.assertThrows(AbstractMethodError.class, Node::outerHtmlHead);
        Assert.assertThrows(AbstractMethodError.class, Node::outerHtmlTail);
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals("example", node.attr("class"));
        Assert.assertEquals("", node.attr("non-existent-attribute"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Node(baseUri, attributes);

        Assert.assertTrue(node.hasAttr("class"));
        Assert.assertFalse(node.hasAttr("non-existent-attribute"));
    }

    @Test
    public void testSetAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("class", "new-class");
        Assert.assertEquals("new-class", node.attr("class"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Node(baseUri, attributes);

        node.removeAttr("class");
        Assert.assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        Assert.assertEquals("", node.absUrl("non-existent-attribute"));
        Assert.assertEquals("http://example.com/path/to/relative-url", node.absUrl("relative-url"));
        Assert.assertEquals("http://example.com/path/to/absolute-url", node.absUrl("http://example.com/path/to/absolute-url"));
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node rootNode = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);

        rootNode.addChildren(child1, child2);

        List<Node> childNodes = rootNode.childNodes();
        Assert.assertEquals(2, childNodes.size());
        Assert.assertEquals(child1, childNodes.get(0));
        Assert.assertEquals(child2, childNodes.get(1));

        List<Node> childNodesAsArray = rootNode.childNodesAsArray();
        Assert.assertArrayEquals(new Node[]{child1, child2}, childNodesAsArray);
    }

    @Test
    public void testParent() {
        String baseUri1 = "http://example1.com";
        Attributes attributes1 = new Attributes();
        Node parentNode1 = new Node(baseUri1, attributes1);

        String baseUri2 = "http://example2.com";
        Attributes attributes2 = new Attributes();
        Node childNode = new Node(baseUri2, attributes2);

        childNode.setParentNode(parentNode1);
        Assert.assertNotNull(childNode.parentNode);
        Assert.assertEquals(parentNode1, childNode.parentNode);

        List<Node> childNodes1 = parentNode1.childNodes();
        Assert.assertEquals(1, childNodes1.size());
        Assert.assertEquals(childNode, childNodes1.get(0));
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);
        List<Node> childNodes = parentNode.childNodes();

        parentNode.removeChild(childNode1);
        childNodes = parentNode.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertNotEquals(childNode1, childNodes.get(0));
        Assert.assertNull(childNode1.parentNode);

        childNodes = childNode2.siblingNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(childNode2, childNodes.get(0));
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node oldChild = new Node(baseUri, attributes);
        Node newChild = new Node(baseUri, attributes);

        parentNode.addChildren(oldChild);
        List<Node> childNodes = parentNode.childNodes();

        parentNode.replaceWith(newChild, oldChild);
        childNodes = parentNode.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(newChild, childNodes.get(0));
        Assert.assertNull(oldChild.parentNode);
    }
}