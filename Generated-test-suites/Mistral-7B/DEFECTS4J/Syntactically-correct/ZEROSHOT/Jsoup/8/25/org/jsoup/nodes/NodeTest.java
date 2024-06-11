package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NodeTest {

    private Node node;

    @Before
    public void setup() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");
        this.node = new Node(baseUri, attributes);
    }

    @Test
    public void testNodeName() {
        assertNull(node.nodeName());
    }

    @Test(expected = AbstractMethodError.class)
    public void testAbstractMethodNodeName() {
        node.nodeName();
    }

    @Test
    public void testAttr() {
        assertEquals("", node.attr(""));
        assertEquals("myClass", node.attr("class"));
        assertEquals("", node.attr("nonExistentAttribute"));
    }

    @Test
    public void testAttributes() {
        List<String> keys = new ArrayList<>(Arrays.asList("class", "id", "title"));
        Attributes attributes = node.attributes();
        for (String key : keys) {
            assertTrue(attributes.hasKey(key));
            assertEquals(node.attr(key), attributes.get(key));
        }
        assertEquals(1, attributes.size());
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistentAttribute"));
    }

    @Test
    public void testSetAttr() {
        node.attr("newAttribute", "newValue");
        assertEquals("newValue", node.attr("newAttribute"));
        assertEquals(2, node.attributes().size());
    }

    @Test
    public void testRemoveAttr() {
        node.attr("newAttribute", "newValue");
        node.removeAttr("newAttribute");
        assertFalse(node.hasAttr("newAttribute"));
        assertEquals(1, node.attributes().size());
    }

    @Test
    public void testBaseUri() {
        assertEquals("http://www.example.com", node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String newBaseUri = "http://www.newsite.com";
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    @Test(expected = AbstractMethodError.class)
    public void testAbstractMethodBaseUri() {
        node.baseUri();
    }

    @Test
    public void testChildNode() {
        Node childNode = new Node();
        node.appendChild(childNode);
        assertEquals(childNode, node.childNode(0));
    }

    @Test
    public void testChildNodes() {
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        node.appendChild(childNode1);
        node.appendChild(childNode2);
        List<Node> childNodes = node.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));
        assertEquals(childNode2, childNodes.get(1));
    }

    @Test
    public void testChildNodesAsArray() {
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        node.appendChild(childNode1);
        node.appendChild(childNode2);
        Node[] childNodesAsArray = node.childNodesAsArray();
        assertEquals(2, childNodesAsArray.length);
        assertEquals(childNode1, childNodesAsArray[0]);
        assertEquals(childNode2, childNodesAsArray[1]);
    }

    @Test
    public void testParent() {
        Node parentNode = new Node();
        parentNode.appendChild(node);
        assertEquals(parentNode, node.parent());
        assertEquals(null, node.parent());
    }

    @Test
    public void testOwnerDocument() {
        assertNull(node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parentNode = new Node();
        parentNode.appendChild(node);
        parentNode.removeChild(node);
        assertEquals(0, parentNode.childNodes().size());
        assertEquals(null, node.parent());
    }

    @Test
    public void testReplaceWith() {
        Node parentNode = new Node();
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        parentNode.appendChild(childNode1);
        parentNode.appendChild(node);
        parentNode.replaceChild(node, childNode2);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(childNode2, parentNode.childNode(0));
        assertEquals(childNode2, node.parent());
    }

    @Test
    public void testSetParentNode() {
        Node parentNode1 = new Node();
        Node parentNode2 = new Node();
        node.setParentNode(parentNode1);
        assertEquals(parentNode1, node.parent());
        node.setParentNode(parentNode2);
        assertEquals(parentNode2, node.parent());
    }

    @Test
    public void testReplaceChild() {
        Node parentNode = new Node();
        Node out = new Node();
        Node in = new Node();
        parentNode.appendChild(out);
        parentNode.appendChild(node);
        parentNode.replaceChild(out, in);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(in, parentNode.childNode(0));
        assertEquals(in, node.parent());
    }

    @Test
    public void testRemoveChild() {
        Node parentNode = new Node();
        Node out = new Node();
        Node node2 = new Node();
        parentNode.appendChild(out);
        parentNode.appendChild(node);
        parentNode.appendChild(node2);
        parentNode.removeChild(node);
        assertEquals(2, parentNode.childNodes().size());
        assertEquals(node2, parentNode.childNode(1));
        assertEquals(null, node.parent());
    }

    @Test
    public void testAddChildren() {
        Node parentNode = new Node();
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        parentNode.addChildren(childNode1, childNode2);
        assertEquals(2, parentNode.childNodes().size());
        assertEquals(childNode1, parentNode.childNode(0));
        assertEquals(childNode2, parentNode.childNode(1));
        assertEquals(childNode1, childNode2.parent());
    }

    @Test
    public void testAddChildrenIndex() {
        Node parentNode = new Node();
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        parentNode.addChildren(1, childNode1, childNode2);
        assertEquals(2, parentNode.childNodes().size());
        assertEquals(childNode1, parentNode.childNode(0));
        assertEquals(childNode2, parentNode.childNode(1));
        assertEquals(childNode1, childNode2.parent());
    }

    @Test
    public void testSiblingNodes() {
        Node parentNode = new Node();
        Node sibling1 = new Node();
        Node node = new Node();
        Node sibling2 = new Node();
        parentNode.appendChild(sibling1);
        parentNode.appendChild(node);
        parentNode.appendChild(sibling2);
        List<Node> siblingNodes = node.siblingNodes();
        assertEquals(2, siblingNodes.size());
        assertEquals(sibling1, siblingNodes.get(0));
        assertEquals(sibling2, siblingNodes.get(1));
    }

    @Test
    public void testNextSibling() {
        Node parentNode = new Node();
        Node sibling1 = new Node();
        Node node = new Node();
        Node sibling2 = new Node();
        parentNode.appendChild(sibling1);
        parentNode.appendChild(node);
        parentNode.appendChild(sibling2);
        assertEquals(sibling2, node.nextSibling());
        assertEquals(null, node.previousSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node parentNode = new Node();
        Node sibling1 = new Node();
        Node node = new Node();
        Node sibling2 = new Node();
        parentNode.appendChild(sibling1);
        parentNode.appendChild(node);
        parentNode.appendChild(sibling2);
        assertEquals(sibling1, node.previousSibling());
        assertEquals(null, node.nextSibling());
    }

    @Test
    public void testSiblingIndex() {
        Node parentNode = new Node();
        Node sibling1 = new Node();
        Node node = new Node();
        Node sibling2 = new Node();
        parentNode.appendChild(sibling1);
        parentNode.appendChild(node);
        parentNode.appendChild(sibling2);
        assertEquals(1, node.siblingIndex());
    }

    @Test
    public void testSetSiblingIndex() {
        Node parentNode = new Node();
        Node sibling1 = new Node();
        Node node = new Node();
        Node sibling2 = new Node();
        parentNode.appendChild(sibling1);
        parentNode.appendChild(node);
        parentNode.appendChild(sibling2);
        node.setSiblingIndex(2);
        assertEquals(2, node.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        StringBuilder accum = new StringBuilder();
        node.outerHtml(accum);
        assertEquals("", accum.toString());
    }

    @Test
    public void testOuterHtmlWithChildren() {
        StringBuilder accum = new StringBuilder();
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        node.appendChild(childNode1);
        node.appendChild(childNode2);
        node.outerHtml(accum);
        assertEquals("<node>\n<childNode1></childNode1>\n<childNode2></childNode2>\n</node>", accum.toString());
    }

    @Test
    public void testOuterHtmlWithAttributes() {
        StringBuilder accum = new StringBuilder();
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");
        node.setAttributes(attributes);
        node.outerHtml(accum);
        assertEquals("<node class=\"myClass\"></node>", accum.toString());
    }

    @Test
    public void testEquals() {
        Node node1 = new Node();
        Node node2 = new Node();
        assertFalse(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        Node node1 = new Node();
        Node node2 = new Node();
        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        assertFalse(hashCode1 == hashCode2);
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://www.example.com";
        String relativeUrl = "relative.html";
        String expectedAbsoluteUrl = "http://www.example.com/relative.html";
        String absolutUrl = node.absUrl("href");
        assertEquals(expectedAbsoluteUrl, absolutUrl);
    }

    @Test
    public void testAbsUrlWithAbsoluteUrl() {
        String baseUri = "http://www.example.com";
        String absoluteUrl = "http://www.example.com/absolute.html";
        String absolutUrl = node.absUrl("href");
        assertEquals(absoluteUrl, absolutUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlWithInvalidUrl() {
        String baseUri = "http://www.example.com";
        String invalidUrl = "example";
        node.absUrl(invalidUrl);
    }

    @Test
    public void testHasAttrWithAbsoluteUrl() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "relative.html");
        node = new Node(baseUri, attributes);
        assertTrue(node.hasAttr("href"));
    }

}