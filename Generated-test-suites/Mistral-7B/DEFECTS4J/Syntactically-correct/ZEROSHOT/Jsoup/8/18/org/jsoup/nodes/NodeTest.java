package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for the Node class.
 */
public class NodeTest {

    /**
     * Test constructor with baseUri and attributes.
     */
    @Test
    public void testNodeWithBaseUriAndAttributes() {
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes().put("id", "myId");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    /**
     * Test constructor without baseUri and with attributes.
     */
    @Test
    public void testNodeWithoutBaseUriAndAttributes() {
        Attributes attributes = new Attributes().put("id", "myId");
        Node node = new Element("div", attributes);
        assertEquals("div", node.nodeName());
        assertNull(node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    /**
     * Test constructor without baseUri and without attributes.
     */
    @Test
    public void testNodeWithoutBaseUriAndWithoutAttributes() {
        Node node = new Element("div");
        assertEquals("div", node.nodeName());
        assertNull(node.baseUri());
        assertNull(node.attributes());
    }

    /**
     * Test getNodeName method.
     */
    @Test
    public void testgetNodeName() {
        Node node = new Element("div");
        assertEquals("div", node.nodeName());
    }

    /**
     * Test getAttribute method.
     */
    @Test
    public void testgetAttribute() {
        Attributes attributes = new Attributes().put("id", "myId").put("class", "myClass");
        Node node = new Element("div", attributes);
        assertEquals("myId", node.attr("id"));
        assertEquals("myClass", node.attr("class"));
        assertEquals("", node.attr("nonexistent"));
    }

    /**
     * Test getAttributes method.
     */
    @Test
    public void testgetAttributes() {
        Attributes attributes = new Attributes().put("id", "myId").put("class", "myClass");
        Node node = new Element("div", attributes);
        Attributes nodeAttributes = node.attributes();
        assertEquals(attributes, nodeAttributes);
    }

    /**
     * Test hasAttr method.
     */
    @Test
    public void testHasAttr() {
        Attributes attributes = new Attributes().put("id", "myId").put("class", "myClass");
        Node node = new Element("div", attributes);
        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonexistent"));
    }

    /**
     * Test setAttribute method.
     */
    @Test
    public void testSetAttribute() {
        Node node = new Element("div");
        node.attr("id", "newId");
        assertEquals("newId", node.attr("id"));
    }

    /**
     * Test removeAttribute method.
     */
    @Test
    public void testRemoveAttribute() {
        Attributes attributes = new Attributes().put("id", "oldId").put("class", "oldClass");
        Node node = new Element("div", attributes);
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
        assertEquals("oldClass", node.attr("class"));
    }

    /**
     * Test getBaseUri method.
     */
    @Test
    public void testGetBaseUri() {
        String baseUri = "https://www.example.com";
        Node node = new Element("div", baseUri, new Attributes());
        assertEquals(baseUri, node.baseUri());
    }

    /**
     * Test setBaseUri method.
     */
    @Test
    public void testSetBaseUri() {
        Node node = new Element("div");
        String newBaseUri = "https://www.newexample.com";
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    /**
     * Test getChildNode method.
     */
    @Test
    public void testGetChildNode() {
        Node parent = new Element("div");
        Node child = new Element("p");
        parent.addChild(child);
        Node retrievedChild = parent.childNode(0);
        assertEquals(child, retrievedChild);
    }

    /**
     * Test getChildNodes method.
     */
    @Test
    public void testGetChildNodes() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        parent.addChildren(child1, child2);
        List<Node> childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(child1, childNodes.get(0));
        assertEquals(child2, childNodes.get(1));
    }

    /**
     * Test childNodesAsArray method.
     */
    @Test
    public void testChildNodesAsArray() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        parent.addChildren(child1, child2);
        Node[] childNodesArray = parent.childNodesAsArray();
        assertEquals(2, childNodesArray.length);
        assertEquals(child1, childNodesArray[0]);
        assertEquals(child2, childNodesArray[1]);
    }

    /**
     * Test getParent method.
     */
    @Test
    public void testGetParent() {
        Node child = new Element("p");
        Node parent = new Element("div");
        parent.addChild(child);
        assertEquals(parent, child.parent());
    }

    /**
     * Test getOwnerDocument method.
     */
    @Test
    public void testGetOwnerDocument() {
        Node node = new Element("div");
        Document document = new Document();
        document.setRootElement(node);
        assertEquals(document, node.ownerDocument());
    }

    /**
     * Test remove method.
     */
    @Test
    public void testRemove() {
        Node parent = new Element("div");
        Node child = new Element("p");
        parent.addChild(child);
        parent.removeChild(child);
        List<Node> childNodes = parent.childNodes();
        assertEquals(0, childNodes.size());
    }

    /**
     * Test replaceWith method.
     */
    @Test
    public void testReplaceWith() {
        Node parent = new Element("div");
        Node oldChild = new Element("p");
        Node newChild = new Element("span");
        parent.addChild(oldChild);
        parent.replaceChild(oldChild, newChild);
        List<Node> childNodes = parent.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(newChild, childNodes.get(0));
    }

    /**
     * Test setParentNode method.
     */
    @Test
    public void testSetParentNode() {
        Node child = new Element("p");
        Node parent = new Element("div");
        child.setParentNode(parent);
        assertEquals(parent, child.parent());
    }

    /**
     * Test replaceChild method.
     */
    @Test
    public void testReplaceChild() {
        Node parent = new Element("div");
        Node oldChild = new Element("p");
        Node newChild = new Element("span");
        parent.addChild(oldChild);
        parent.replaceChild(oldChild, newChild);
        List<Node> childNodes = parent.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(newChild, childNodes.get(0));
    }

    /**
     * Test removeChild method.
     */
    @Test
    public void testRemoveChild() {
        Node parent = new Element("div");
        Node child = new Element("p");
        parent.addChild(child);
        parent.removeChild(child);
        List<Node> childNodes = parent.childNodes();
        assertEquals(0, childNodes.size());
    }

    /**
     * Test addChildren method.
     */
    @Test
    public void testAddChildren() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        parent.addChildren(child1, child2);
        List<Node> childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(child1, childNodes.get(0));
        assertEquals(child2, childNodes.get(1));
    }

    /**
     * Test addChildren method with index.
     */
    @Test
    public void testAddChildrenWithIndex() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        parent.addChildren(1, child1, child2);
        List<Node> childNodes = parent.childNodes();
        assertEquals(3, childNodes.size());
        assertEquals(child1, childNodes.get(1));
        assertEquals(child2, childNodes.get(2));
    }

    /**
     * Test absUrl method with absolute URL.
     */
    @Test
    public void testAbsUrlWithAbsoluteUrl() {
        Node node = new Element("a", "http://www.example.com");
        String result = node.absUrl("href");
        assertEquals("http://www.example.com", result);
    }

    /**
     * Test absUrl method with relative URL.
     */
    @Test
    public void testAbsUrlWithRelativeUrl() {
        Node node = new Element("a", "http://www.example.com/");
        node.attr("href", "relative.html");
        String result = node.absUrl("href");
        assertEquals("http://www.example.com/relative.html", result);
    }

    /**
     * Test absUrl method with an empty string.
     */
    @Test
    public void testAbsUrlWithEmptyString() {
        Node node = new Element("a", "http://www.example.com/");
        String result = node.absUrl("");
        assertEquals("", result);
    }

    /**
     * Test nextSibling method.
     */
    @Test
    public void testNextSibling() {
        Node parent = new Element("div");
        Node olderSibling = new Element("p");
        Node node = new Element("p");
        parent.addChildren(olderSibling, node);
        Node nextSibling = node.nextSibling();
        assertEquals(null, nextSibling);
    }

    /**
     * Test nextSibling method with a next sibling.
     */
    @Test
    public void testNextSiblingWithNextSibling() {
        Node parent = new Element("div");
        Node olderSibling = new Element("p");
        Node node = new Element("p");
        Node youngerSibling = new Element("p");
        parent.addChildren(olderSibling, node, youngerSibling);
        Node nextSibling = node.nextSibling();
        assertEquals(youngerSibling, nextSibling);
    }

    /**
     * Test previousSibling method.
     */
    @Test
    public void testPreviousSibling() {
        Node parent = new Element("div");
        Node youngerSibling = new Element("p");
        Node node = new Element("p");
        parent.addChildren(node, youngerSibling);
        Node previousSibling = node.previousSibling();
        assertEquals(null, previousSibling);
    }

    /**
     * Test previousSibling method with a previous sibling.
     */
    @Test
    public void testPreviousSiblingWithPreviousSibling() {
        Node parent = new Element("div");
        Node olderSibling = new Element("p");
        Node node = new Element("p");
        parent.addChildren(olderSibling, node);
        Node previousSibling = node.previousSibling();
        assertEquals(olderSibling, previousSibling);
    }

    /**
     * Test siblingNodes method.
     */
    @Test
    public void testSiblingNodes() {
        Node parent = new Element("div");
        Node olderSibling1 = new Element("p");
        Node olderSibling2 = new Element("p");
        Node node = new Element("p");
        Node youngerSibling = new Element("p");
        parent.addChildren(olderSibling1, olderSibling2, node, youngerSibling);
        List<Node> siblingNodes = node.siblingNodes();
        assertEquals(3, siblingNodes.size());
        assertEquals(olderSibling2, siblingNodes.get(0));
        assertEquals(youngerSibling, siblingNodes.get(1));
        assertEquals(olderSibling1, siblingNodes.get(2));
    }

    /**
     * Test siblingIndex method.
     */
    @Test
    public void testSiblingIndex() {
        Node parent = new Element("div");
        Node olderSibling1 = new Element("p");
        Node olderSibling2 = new Element("p");
        Node node = new Element("p");
        Node youngerSibling = new Element("p");
        parent.addChildren(olderSibling1, olderSibling2, node, youngerSibling);
        assertEquals(2, node.siblingIndex());
    }

    /**
     * Test outerHtml method.
     */
    @Test
    public void testOuterHtml() {
        Node node = new Element("div").attr("id", "myId");
        node.addChildren(new TextNode("Hello"), new Element("p").attr("class", "myClass"));
        String expectedOuterHtml = "<div id=\"myId\">Hello<p class=\"myClass\"></p></div>";
        String actualOuterHtml = node.outerHtml();
        assertEquals(expectedOuterHtml, actualOuterHtml);
    }

    /**
     * Test outerHtmlHead method.
     */
    @Test
    public void testOuterHtmlHead() {
        Node node = new Element("div").attr("id", "myId");
        StringBuilder accum = new StringBuilder();
        node.outerHtmlHead(accum, 0, null);
        String expectedOuterHtmlHead = "<div id=\"myId\">";
        String actualOuterHtmlHead = accum.toString();
        assertEquals(expectedOuterHtmlHead, actualOuterHtmlHead);
    }

    /**
     * Test outerHtmlTail method.
     */
    @Test
    public void testOuterHtmlTail() {
        Node node = new Element("div").attr("id", "myId");
        StringBuilder accum = new StringBuilder();
        node.outerHtmlTail(accum, 0, null);
        String expectedOuterHtmlTail = "</div>";
        String actualOuterHtmlTail = accum.toString();
        assertEquals(expectedOuterHtmlTail, actualOuterHtmlTail);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Node node1 = new Element("div").attr("id", "myId");
        Node node2 = new Element("div").attr("id", "myId");
        Node node3 = new Element("div").attr("id", "anotherId");
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        Node node1 = new Element("div").attr("id", "myId");
        Node node2 = new Element("div").attr("id", "myId");
        Node node3 = new Element("div").attr("id", "anotherId");
        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        int hashCode3 = node3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}