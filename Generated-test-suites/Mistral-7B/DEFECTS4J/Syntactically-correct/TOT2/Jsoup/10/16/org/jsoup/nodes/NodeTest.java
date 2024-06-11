package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the abstract Node class.
 */
public class NodeTest {

    @org.junit.Test
    public void testNewNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        // Test default constructor
        Node node1 = new Node();
        assertEquals("", node1.nodeName());
        assertNull(node1.parentNode);
        assertNotNull(node1.childNodes);
        assertNotNull(node1.attributes);
        assertEquals(baseUri, node1.baseUri());
        assertNull(node1.attributes().get("id"));

        // Test constructor with baseUri and attributes
        Node node2 = new Node(baseUri, attributes);
        assertEquals("", node2.nodeName());
        assertEquals(baseUri, node2.baseUri());
        assertEquals(attributes, node2.attributes());
        assertNull(node2.parentNode);
        assertNotNull(node2.childNodes);

        // Test constructor with baseUri only
        Node node3 = new Node(baseUri);
        assertEquals("", node3.nodeName());
        assertEquals(baseUri, node3.baseUri());
        assertNull(node3.parentNode);
        assertNotNull(node3.childNodes);
        assertNull(node3.attributes);
    }

    @org.junit.Test
    public void testBasicAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);

        String id = node.attr("id");
        assertEquals("node1", id);

        String nonExistentAttr = node.attr("nonExistent");
        assertEquals("", nonExistentAttr);

        Node nodeWithAbsUrl = new Element("a", attributes);
        String absUrl = nodeWithAbsUrl.absUrl("href");
        assertEquals("", absUrl); // empty baseUri, so no URL can be made

        nodeWithAbsUrl.absUrl("href", "http://example.com/test.html");
        absUrl = nodeWithAbsUrl.absUrl("href");
        assertEquals("http://example.com/test.html", absUrl);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Node(null, new Attributes());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNullAttributes() {
        new Node("http://example.com", null);
    }

    @org.junit.Test(expected = MalformedURLException.class)
    public void testMalformedBaseUri() {
        new Node("malformed://example.com", new Attributes());
    }

    @org.junit.Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";

        Attributes attributes = new Attributes();
        attributes.put("id", "parent");

        Node parentNode = new Element("div", attributes);
        parentNode.setBaseUri(baseUri);

        Attributes childAttributes = new Attributes();
        childAttributes.put("id", "child");

        Node childNode = new Element("p", childAttributes);
        childNode.setBaseUri(baseUri);

        parentNode.appendChild(childNode);

        Node testNode = parentNode.childNode(0);
        assertEquals("p", testNode.nodeName());
        assertEquals("child", testNode.attr("id"));
        assertEquals(baseUri, testNode.baseUri());
    }

    @org.junit.Test
    public void testGetChildren() {
        String baseUri = "http://example.com";

        Attributes attributes = new Attributes();
        attributes.put("id", "parent");

        Node parentNode = new Element("div", attributes);
        parentNode.setBaseUri(baseUri);

        Attributes child1Attributes = new Attributes();
        child1Attributes.put("id", "child1");
        Node child1Node = new Element("p", child1Attributes);
        child1Node.setBaseUri(baseUri);

        Attributes child2Attributes = new Attributes();
        child2Attributes.put("id", "child2");
        Node child2Node = new Element("p", child2Attributes);
        child2Node.setBaseUri(baseUri);

        parentNode.appendChild(child1Node);
        parentNode.appendChild(child2Node);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());

        Node testChild1 = children.get(0);
        assertEquals("p", testChild1.nodeName());
        assertEquals("child1", testChild1.attr("id"));
        assertEquals(baseUri, testChild1.baseUri());

        Node testChild2 = children.get(1);
        assertEquals("p", testChild2.nodeName());
        assertEquals("child2", testChild2.attr("id"));
        assertEquals(baseUri, testChild2.baseUri());
    }

    @org.junit.Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";

        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "parent");
        Node parentNode = new Element("div", parentAttributes);
        parentNode.setBaseUri(baseUri);

        Attributes child1Attributes = new Attributes();
        child1Attributes.put("id", "child1");
        Node child1Node = new Element("p", child1Attributes);
        child1Node.setBaseUri(baseUri);

        Attributes child2Attributes = new Attributes();
        child2Attributes.put("id", "child2");
        Node child2Node = new Element("p", child2Attributes);
        child2Node.setBaseUri(baseUri);

        parentNode.appendChild(child1Node);
        parentNode.appendChild(child2Node);

        parentNode.removeChild(child1Node);

        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals("p", children.get(0).nodeName());
        assertEquals("child2", children.get(0).attr("id"));
        assertEquals(baseUri, children.get(0).baseUri());
    }

    @org.junit.Test
    public void testAddChildren() {
        String baseUri = "http://example.com";

        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "parent");
        Node parentNode = new Element("div", parentAttributes);
        parentNode.setBaseUri(baseUri);

        Attributes child1Attributes = new Attributes();
        child1Attributes.put("id", "child1");
        Node child1Node = new Element("p", child1Attributes);
        child1Node.setBaseUri(baseUri);

        Attributes child2Attributes = new Attributes();
        child2Attributes.put("id", "child2");
        Node child2Node = new Element("p", child2Attributes);
        child2Node.setBaseUri(baseUri);

        parentNode.addChildren(child1Node, child2Node);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());

        Node testChild1 = children.get(0);
        assertEquals("p", testChild1.nodeName());
        assertEquals("child1", testChild1.attr("id"));
        assertEquals(baseUri, testChild1.baseUri());

        Node testChild2 = children.get(1);
        assertEquals("p", testChild2.nodeName());
        assertEquals("child2", testChild2.attr("id"));
        assertEquals(baseUri, testChild2.baseUri());
    }

    @org.junit.Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";

        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "grandparent");
        Node grandParentNode = new Element("div", parentAttributes);
        grandParentNode.setBaseUri(baseUri);

        Attributes parentAttributes2 = new Attributes();
        parentAttributes2.put("id", "parent");
        Node parentNode = new Element("div", parentAttributes2);
        parentNode.setBaseUri(baseUri);

        Attributes childAttributes = new Attributes();
        childAttributes.put("id", "child");
        Node childNode = new Element("p", childAttributes);
        childNode.setBaseUri(baseUri);

        grandParentNode.addChildren(parentNode);

        childNode.setParentNode(parentNode);

        List<Node> children = grandParentNode.childNodes();
        assertEquals(1, children.size());

        Node testGrandParent = children.get(0);
        assertEquals("div", testGrandParent.nodeName());
        assertEquals("grandparent", testGrandParent.attr("id"));
        assertEquals(baseUri, testGrandParent.baseUri());

        Node testParent = testGrandParent.childNode(0);
        assertEquals("div", testParent.nodeName());
        assertEquals("parent", testParent.attr("id"));
        assertEquals(baseUri, testParent.baseUri());

        Node testChild = testParent.childNode(0);
        assertEquals("p", testChild.nodeName());
        assertEquals("child", testChild.attr("id"));
        assertEquals(baseUri, testChild.baseUri());
    }

    @org.junit.Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";

        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "parent");
        Node parentNode = new Element("div", parentAttributes);
        parentNode.setBaseUri(baseUri);

        Attributes child1Attributes = new Attributes();
        child1Attributes.put("id", "child1");
        Node child1Node = new Element("p", child1Attributes);
        child1Node.setBaseUri(baseUri);

        Attributes child2Attributes = new Attributes();
        child2Attributes.put("id", "child2");
        Node child2Node = new Element("p", child2Attributes);
        child2Node.setBaseUri(baseUri);

        parentNode.appendChild(child1Node);

        parentNode.replaceChild(child1Node, child2Node);

        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());

        Node testChild = children.get(0);
        assertEquals("p", testChild.nodeName());
        assertEquals("child2", testChild.attr("id"));
        assertEquals(baseUri, testChild.baseUri());

        assertEquals(child1Node, parentNode.childNode(1));
        assertNull(parentNode.childNode(0).parentNode);
    }

    @org.junit.Test
    public void testGetParent() {
        String baseUri = "http://example.com";

        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "grandparent");
        Node grandParentNode = new Element("div", parentAttributes);
        grandParentNode.setBaseUri(baseUri);

        Attributes parentAttributes2 = new Attributes();
        parentAttributes2.put("id", "parent");
        Node parentNode = new Element("div", parentAttributes2);
        parentNode.setBaseUri(baseUri);

        Attributes childAttributes = new Attributes();
        childAttributes.put("id", "child");
        Node childNode = new Element("p", childAttributes);
        childNode.setBaseUri(baseUri);

        grandParentNode.addChildren(parentNode);

        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parent());
        assertEquals(grandParentNode, parentNode.parent());
        assertEquals(grandParentNode, childNode.ownerDocument());
    }

    @org.junit.Test
    public void testRemove() {
        String baseUri = "http://example.com";

        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "parent");
        Node parentNode = new Element("div", parentAttributes);
        parentNode.setBaseUri(baseUri);

        Attributes childAttributes = new Attributes();
        childAttributes.put("id", "child");
        Node childNode = new Element("p", childAttributes);
        childNode.setBaseUri(baseUri);

        parentNode.appendChild(childNode);

        childNode.remove();

        List<Node> children = parentNode.childNodes();
        assertEquals(0, children.size());
    }
}