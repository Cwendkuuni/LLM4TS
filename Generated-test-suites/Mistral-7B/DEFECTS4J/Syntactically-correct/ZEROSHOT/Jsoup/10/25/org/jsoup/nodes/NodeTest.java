package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test file for the Node class.
 */
public class NodeTest {

    /**
     * Test constructor with baseUri and attributes.
     */
    @Test
    public void testNodeWithBaseUriAndAttributes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes().put("id", "testId").put("class", "testClass");
        Node node = new Element("testElement", baseUri, attributes);

        assertEquals("testElement", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    /**
     * Test constructor without baseUri, attributes, or children.
     */
    @Test
    public void testNodeWithoutBaseUriOrAttributesOrChildren() {
        Node node = new Node();

        assertNull(node.parentNode);
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
        assertEquals("", node.baseUri());
    }

    /**
     * Test getting attribute value.
     */
    @Test
    public void testGetAttributeValue() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes().put("id", "testId").put("class", "testClass");
        Node node = new Element("testElement", baseUri, attributes);

        assertEquals("testId", node.attr("id"));
        assertEquals("testClass", node.attr("class"));
        assertEquals("", node.attr("nonexistent"));
        assertEquals(baseUri, node.absUrl("href"));
        assertEquals("", node.absUrl("nonexistent"));
    }

    /**
     * Test setting attribute value.
     */
    @Test
    public void testSetAttributeValue() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes().put("id", "testId").put("class", "testClass");
        Node node = new Element("testElement", baseUri, attributes);

        node.attr("data", "testData");
        assertEquals("testData", node.attr("data"));
    }

    /**
     * Test removing attribute.
     */
    @Test
    public void testRemoveAttribute() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes().put("id", "testId").put("class", "testClass");
        Node node = new Element("testElement", baseUri, attributes);

        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    /**
     * Test getting base URI.
     */
    @Test
    public void testGetBaseUri() {
        String baseUri = "https://example.com";
        Node node = new Element("testElement", baseUri, new Attributes());

        assertEquals(baseUri, node.baseUri());
    }

    /**
     * Test setting base URI.
     */
    @Test
    public void testSetBaseUri() {
        String newBaseUri = "https://another-example.com";
        Node node = new Element("testElement", "https://example.com", new Attributes());

        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    /**
     * Test getting child node by index.
     */
    @Test
    public void testGetChildNodeByIndex() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes().put("id", "testId");
        Node parentNode = new Element("parentElement", baseUri, attributes);
        Node childNode1 = new Element("childElement1", baseUri, attributes);
        Node childNode2 = new Element("childElement2", baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);

        Node childNode1FromParent = parentNode.childNode(0);
        Node childNode2FromParent = parentNode.childNode(1);

        assertEquals(childNode1, childNode1FromParent);
        assertEquals(childNode2, childNode2FromParent);
    }

    /**
     * Test getting children.
     */
    @Test
    public void testGetChildren() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes().put("id", "testId");
        Node parentNode = new Element("parentElement", baseUri, attributes);
        Node childNode1 = new Element("childElement1", baseUri, attributes);
        Node childNode2 = new Element("childElement2", baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        assertTrue(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }

    /**
     * Test getting children as array.
     */
    @Test
    public void testGetChildrenAsArray() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes().put("id", "testId");
        Node parentNode = new Element("parentElement", baseUri, attributes);
        Node childNode1 = new Element("childElement1", baseUri, attributes);
        Node childNode2 = new Element("childElement2", baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);

        Node[] childrenAsArray = parentNode.childNodesAsArray();

        assertEquals(2, childrenAsArray.length);
        assertTrue(Arrays.asList(childrenAsArray).contains(childNode1));
        assertTrue(Arrays.asList(childrenAsArray).contains(childNode2));
    }

    /**
     * Test getting parent node.
     */
    @Test
    public void testGetParentNode() {
        String baseUri = "https://example.com";
        Attributes parentAttributes = new Attributes().put("id", "testParentId");
        Node parentNode = new Element("parentElement", baseUri, parentAttributes);
        Node childNode = new Element("childElement", baseUri, new Attributes());

        parentNode.addChild(childNode);

        assertEquals(parentNode, childNode.parent());
        assertNull(childNode.parent());
    }

    /**
     * Test getting document associated with this node.
     */
    @Test
    public void testGetDocument() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes().put("id", "testId");
        Node rootNode = new Document(baseUri, attributes);
        Node childNode = new Element("childElement", baseUri, attributes);

        rootNode.addChild(childNode);

        assertEquals(rootNode, childNode.ownerDocument());
        assertNotNull(childNode.ownerDocument());
    }

    /**
     * Test removing node from DOM tree.
     */
    @Test
    public void testRemoveNodeFromDomTree() {
        String baseUri = "https://example.com";
        Attributes parentAttributes = new Attributes().put("id", "testParentId");
        Node parentNode = new Element("parentElement", baseUri, parentAttributes);
        Node childNode = new Element("childElement", baseUri, new Attributes());

        parentNode.addChild(childNode);
        parentNode.removeChild(childNode);

        assertEquals(0, parentNode.childNodes().size());
        assertFalse(parentNode.childNodes().contains(childNode));
        assertNull(childNode.parent());
    }

    /**
     * Test replacing node in DOM tree.
     */
    @Test
    public void testReplaceNodeInDomTree() {
        String baseUri = "https://example.com";
        Attributes parentAttributes = new Attributes().put("id", "testParentId");
        Node parentNode = new Element("parentElement", baseUri, parentAttributes);
        Node oldChildNode = new Element("oldChildElement", baseUri, new Attributes());
        Node newChildNode = new Element("newChildElement", baseUri, new Attributes());

        parentNode.addChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);

        assertEquals(1, parentNode.childNodes().size());
        assertTrue(parentNode.childNodes().contains(newChildNode));
        assertEquals(newChildNode, parentNode.childNode(0));
        assertNull(oldChildNode.parent());
    }

    /**
     * Test setting parent node.
     */
    @Test
    public void testSetParentNode() {
        String baseUri = "https://example.com";
        Attributes parentAttributes = new Attributes().put("id", "testParentId");
        Node parentNode = new Element("parentElement", baseUri, parentAttributes);
        Node childNode = new Element("childElement", baseUri, new Attributes());

        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parent());
    }

    /**
     * Test replacing child.
     */
    @Test
    public void testReplaceChild() {
        String baseUri = "https://example.com";
        Attributes parentAttributes = new Attributes().put("id", "testParentId");
        Node parentNode = new Element("parentElement", baseUri, parentAttributes);
        Node oldChildNode = new Element("oldChildElement", baseUri, new Attributes());
        Node newChildNode = new Element("newChildElement", baseUri, new Attributes());

        parentNode.addChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);

        assertEquals(1, parentNode.childNodes().size());
        assertTrue(parentNode.childNodes().contains(newChildNode));
        assertEquals(newChildNode, parentNode.childNode(0));
        assertNull(oldChildNode.parent());
    }

    /**
     * Test removing child.
     */
    @Test
    public void testRemoveChild() {
        String baseUri = "https://example.com";
        Attributes parentAttributes = new Attributes().put("id", "testParentId");
        Node parentNode = new Element("parentElement", baseUri, parentAttributes);
        Node childNode = new Element("childElement", baseUri, new Attributes());

        parentNode.addChild(childNode);
        parentNode.removeChild(childNode);

        assertEquals(0, parentNode.childNodes().size());
        assertFalse(parentNode.childNodes().contains(childNode));
        assertNull(childNode.parent());
    }

    /**
     * Test adding children.
     */
    @Test
    public void testAddChildren() {
        String baseUri = "https://example.com";
        Attributes parentAttributes = new Attributes().put("id", "testParentId");
        Node parentNode = new Element("parentElement", baseUri, parentAttributes);
        Node childNode1 = new Element("childElement1", baseUri, new Attributes());
        Node childNode2 = new Element("childElement2", baseUri, new Attributes());

        parentNode.addChildren(childNode1, childNode2);

        assertEquals(2, parentNode.childNodes().size());
        assertTrue(parentNode.childNodes().containsAll(Arrays.asList(childNode1, childNode2)));
    }

    /**
     * Test adding children with index.
     */
    @Test
    public void testAddChildrenWithIndex() {
        String baseUri = "https://example.com";
        Attributes parentAttributes = new Attributes().put("id", "testParentId");
        Node parentNode = new Element("parentElement", baseUri, parentAttributes);
        Node childNode1 = new Element("childElement1", baseUri, new Attributes());
        Node childNode2 = new Element("childElement2", baseUri, new Attributes());

        parentNode.addChildren(1, childNode1, childNode2);

        assertEquals(2, parentNode.childNodes().size());
        assertTrue(parentNode.childNodes().get(0).equals(childNode1));
        assertTrue(parentNode.childNodes().get(1).equals(childNode2));
    }

    /**
     * Test cloning node.
     */
    @Test
    public void testCloneNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes().put("id", "testId").put("class", "testClass");
        Node node = new Element("testElement", baseUri, attributes);

        Node clonedNode = node.clone();

        assertEquals(node, clonedNode);
        assertNotSame(node, clonedNode);
        assertEquals(node.nodeName(), clonedNode.nodeName());
        assertEquals(node.baseUri(), clonedNode.baseUri());
        assertEquals(node.attributes(), clonedNode.attributes());

        List<Node> children = node.childNodes();
        List<Node> clonedChildren = clonedNode.childNodes();

        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            Node clonedChild = clonedChildren.get(i);

            assertEquals(child, clonedChild);
            assertNotSame(child, clonedChild);
        }
    }
}