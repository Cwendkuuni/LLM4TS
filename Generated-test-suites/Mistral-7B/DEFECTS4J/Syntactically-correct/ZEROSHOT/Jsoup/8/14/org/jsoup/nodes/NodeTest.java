package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test class for Node.java
 */
public class NodeTest {

    @org.junit.Before
    public void setup() {
        // Set up test environment
    }

    @org.junit.After
    public void teardown() {
        // Clean up test environment
    }

    /**
     * Test constructor with baseUri and attributes
     */
    @org.junit.Test
    public void testNodeConstructionWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");

        Node node = new Node(baseUri, attributes);

        assertEquals(baseUri, node.baseUri());
        assertEquals("testNode", node.nodeName());
        assertEquals(attributes, node.attributes());
    }

    /**
     * Test constructor with baseUri only
     */
    @org.junit.Test
    public void testNodeConstructionWithBaseUriOnly() {
        String baseUri = "http://example.com";

        Node node = new Node(baseUri);

        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
    }

    /**
     * Test default constructor
     */
    @org.junit.Test
    public void testNodeDefaultConstructor() {
        Node node = new Node();

        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    /**
     * Test getNodeName
     */
    @org.junit.Test
    public void testNodegetName() {
        String nodeName = "div";
        Node node = new Element(nodeName);

        assertEquals(nodeName, node.nodeName());
    }

    /**
     * Test getAttribute
     */
    @org.junit.Test
    public void testgetNodeAttribute() {
        String attributeKey = "class";
        String attributeValue = "testClass";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey, attributeValue);
        Node node = new Element("div", attributes);

        assertEquals(attributeValue, node.attr(attributeKey));
    }

    /**
     * Test getAttribute with abs: prefix
     */
    @org.junit.Test
    public void testgetNodeAttributeWithAbsPrefix() {
        String attributeKey = "abs:href";
        String attributeValue = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey, attributeValue);
        Node node = new Element("a", attributes);

        assertEquals(attributeValue, node.absUrl(attributeKey.substring("abs:".length())));
    }

    /**
     * Test getAllAttributes
     */
    @org.junit.Test
    public void testgetNodeAllAttributes() {
        String attributeKey1 = "class";
        String attributeValue1 = "testClass1";
        String attributeKey2 = "id";
        String attributeValue2 = "testId";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey1, attributeValue1);
        attributes.put(attributeKey2, attributeValue2);
        Node node = new Element("div", attributes);

        List<String> attributesList = new ArrayList<>();
        attributesList.add(attributeKey1);
        attributesList.add(attributeKey2);

        assertEquals(attributes, node.attributes());
        assertEquals(attributesList, new ArrayList<>(node.attributes().names()));
    }

    /**
     * Test setAttribute
     */
    @org.junit.Test
    public void testNodeSetAttribute() {
        String attributeKey = "class";
        String attributeValue = "testClass";
        Node node = new Element("div");

        node.attr(attributeKey, attributeValue);

        assertEquals(attributeValue, node.attr(attributeKey));
    }

    /**
     * Test hasAttribute
     */
    @org.junit.Test
    public void testNodeHasAttribute() {
        String attributeKey = "class";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey, "testClass");
        Node node = new Element("div", attributes);

        assertTrue(node.hasAttr(attributeKey));
    }

    /**
     * Test removeAttribute
     */
    @org.junit.Test
    public void testNodeRemoveAttribute() {
        String attributeKey = "class";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey, "testClass");
        Node node = new Element("div", attributes);

        node.removeAttr(attributeKey);

        assertFalse(node.hasAttr(attributeKey));
    }

    /**
     * Test getBaseUri
     */
    @org.junit.Test
    public void testgetNodeBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Element("div", new Attributes(), baseUri);

        assertEquals(baseUri, node.baseUri());
    }

    /**
     * Test setBaseUri
     */
    @org.junit.Test
    public void testNodeSetBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Element("div");

        node.setBaseUri(baseUri);

        assertEquals(baseUri, node.baseUri());
    }

    /**
     * Test getChildNodeByIndex
     */
    @org.junit.Test
    public void testgetNodeChildNodeByIndex() {
        String nodeName = "div";
        Node parentNode = new Element(nodeName);
        Element childNode = new Element(nodeName);

        parentNode.addChild(childNode);

        Node child = parentNode.childNode(0);

        assertEquals(childNode, child);
    }

    /**
     * Test getChildren
     */
    @org.junit.Test
    public void testgetNodeChildren() {
        String nodeName = "div";
        Node parentNode = new Element(nodeName);
        Element childNode1 = new Element(nodeName);
        Element childNode2 = new Element(nodeName);

        parentNode.addChildren(childNode1, childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertTrue(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }

    /**
     * Test getParentNode
     */
    @org.junit.Test
    public void testgetNodeParentNode() {
        String nodeName = "div";
        Node childNode = new Element(nodeName);
        Node parentNode = new Element(nodeName);

        childNode.setParentNode(parentNode);

        Node parent = childNode.parent();

        assertEquals(parentNode, parent);
    }

    /**
     * Test getOwnerDocument
     */
    @org.junit.Test
    public void testgetNodeOwnerDocument() {
        String nodeName = "div";
        Node childNode = new Element(nodeName);
        Document document = new Document();

        childNode.setParentNode(document);

        assertEquals(document, childNode.ownerDocument());
    }

    /**
     * Test removeNode
     */
    @org.junit.Test
    public void testNodeRemoveNode() {
        String nodeName = "div";
        Node parentNode = new Element(nodeName);
        Element childNode = new Element(nodeName);

        parentNode.addChild(childNode);

        parentNode.removeChild(childNode);

        List<Node> children = parentNode.childNodes();

        assertFalse(children.contains(childNode));
    }

    /**
     * Test replaceChild
     */
    @org.junit.Test
    public void testNodeReplaceChild() throws Exception {
        String nodeName = "div";
        Node parentNode = new Element(nodeName);
        Element childNode1 = new Element(nodeName);
        Element childNode2 = new Element(nodeName);

        parentNode.addChild(childNode1);

        parentNode.replaceChild(childNode1, childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(1, children.size());
        assertTrue(children.contains(childNode2));
        assertFalse(children.contains(childNode1));
    }

    /**
     * Test addChildren
     */
    @org.junit.Test
    public void testNodeAddChildren() {
        String nodeName = "div";
        Node parentNode = new Element(nodeName);
        Element childNode1 = new Element(nodeName);
        Element childNode2 = new Element(nodeName);

        parentNode.addChildren(childNode1, childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertTrue(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }

    /**
     * Test addChildren with index
     */
    @org.junit.Test
    public void testNodeAddChildrenWithIndex() {
        String nodeName = "div";
        Node parentNode = new Element(nodeName);
        Element childNode1 = new Element(nodeName);
        Element childNode2 = new Element(nodeName);

        parentNode.addChildren(1, childNode1, childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertTrue(children.get(0).equals(childNode1));
        assertTrue(children.get(1).equals(childNode2));
    }

    /**
     * Test siblingNodes
     */
    @org.junit.Test
    public void testNodesiblingNodes() {
        String nodeName = "div";
        Node parentNode = new Element(nodeName);
        Element sibling1 = new Element(nodeName);
        Element childNode = new Element(nodeName);
        Element sibling2 = new Element(nodeName);

        parentNode.addChildren(sibling1, childNode, sibling2);

        List<Node> siblings = parentNode.siblingNodes();

        assertEquals(3, siblings.size());
        assertTrue(siblings.contains(sibling1));
        assertTrue(siblings.contains(childNode));
        assertTrue(siblings.contains(sibling2));
    }

    /**
     * Test nextSibling
     */
    @org.junit.Test
    public void testNodesiblingNextSibling() {
        String nodeName = "div";
        Node parentNode = new Element(nodeName);
        Element sibling1 = new Element(nodeName);
        Element childNode = new Element(nodeName);
        Element sibling2 = new Element(nodeName);

        parentNode.addChildren(sibling1, childNode, sibling2);

        Node sibling = childNode.nextSibling();

        assertEquals(sibling2, sibling);
    }

    /**
     * Test previousSibling
     */
    @org.junit.Test
    public void testNodesiblingPreviousSibling() {
        String nodeName = "div";
        Node parentNode = new Element(nodeName);
        Element sibling1 = new Element(nodeName);
        Element childNode = new Element(nodeName);
        Element sibling2 = new Element(nodeName);

        parentNode.addChildren(sibling1, childNode, sibling2);

        Node sibling = childNode.previousSibling();

        assertEquals(sibling1, sibling);
    }

    /**
     * Test siblingIndex
     */
    @org.junit.Test
    public void testNodesiblingSiblingIndex() {
        String nodeName = "div";
        Node parentNode = new Element(nodeName);
        Element sibling1 = new Element(nodeName);
        Element childNode = new Element(nodeName);
        Element sibling2 = new Element(nodeName);

        parentNode.addChildren(sibling1, childNode, sibling2);

        int index = childNode.siblingIndex();

        assertEquals(1, index);
    }

    /**
     * Test outerHtml
     */
    @org.junit.Test
    public void testNodeouterHtml() throws Exception {
        String nodeName = "div";
        String tagOpen = "<" + nodeName;
        String tagClose = "</" + nodeName + ">";
        String attributeKey = "class";
        String attributeValue = "testClass";
        Attributes attributes = new Attributes();
        attributes.put(attributeKey, attributeValue);

        Node node = new Element(nodeName, attributes);

        StringBuilder accum = new StringBuilder(128);
        node.outerHtml(accum);

        String expected = tagOpen + " " + attributeKey + "=\"" + attributeValue + "\"" + ">\n" +
                "   " + tagOpen + " " + attributeKey + "=\"" + attributeValue + "\"" + ">\n" +
                "   " + tagClose + "\n" + tagClose;

        assertEquals(expected, accum.toString());
    }
}