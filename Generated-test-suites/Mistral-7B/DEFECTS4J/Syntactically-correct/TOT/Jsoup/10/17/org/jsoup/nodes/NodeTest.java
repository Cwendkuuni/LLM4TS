package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link Node} abstract class.
 */
public class NodeTest {

    private Document doc;

    @Before
    public void setUp() {
        doc = new Document();
    }

    /**
     * Test constructor with baseUri and attributes.
     */
    @Test
    public void testConstructorWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    /**
     * Test constructor with baseUri only.
     */
    @Test
    public void testConstructorWithBaseUriOnly() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Node node = new Element();

        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.attributes());
    }

    /**
     * Test method getNodeName.
     */
    @Test
    public void testgetNodeName() {
        Node node = new Element("p");

        assertEquals("p", node.nodeName());
    }

    /**
     * Test method attr.
     */
    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("test", node.attr("class"));

        String relUrl = "href.html";
        node.attr("abs:href", relUrl);
        String absUrl = node.absUrl("href");
        assertEquals("http://example.com" + relUrl, absUrl);

        assertEquals("", node.attr("nonExistentAttr"));
        assertEquals("", node.attr(""));
        assertEquals("", node.attr(null));
    }

    /**
     * Test method attributes.
     */
    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        List<String> expectedKeys = new ArrayList<>();
        expectedKeys.add("class");

        List<String> actualKeys = new ArrayList<>();
        for (String key : node.attributes().keys()) {
            actualKeys.add(key);
        }

        assertEquals(expectedKeys, actualKeys);

        String attrValue = "testValue";
        node.attr("class", attrValue);

        String expectedAttrValue = attrValue;
        String actualAttrValue = node.attributes().get("class");

        assertEquals(expectedAttrValue, actualAttrValue);

        node.removeAttr("class");

        assertFalse(node.hasAttr("class"));
    }

    /**
     * Test method hasAttr.
     */
    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonExistentAttr"));

        node.removeAttr("class");

        assertFalse(node.hasAttr("class"));
    }

    /**
     * Test method removeAttr.
     */
    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        assertSame(node, node.removeAttr("class"));

        assertFalse(node.hasAttr("class"));
    }

    /**
     * Test method baseUri.
     */
    @Test
    public void testBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri);

        assertEquals(baseUri, node.baseUri());
    }

    /**
     * Test method setBaseUri.
     */
    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com";
        String newBaseUri = "http://another-example.com";
        Node node = new Element("div", baseUri);

        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    /**
     * Test method getChildNode.
     */
    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";
        Node parent = new Element("div", baseUri);

        Node child = new Element("p", baseUri);
        parent.appendChild(child);

        Node expectedChild = child;
        Node actualChild = parent.childNode(0);

        assertEquals(expectedChild, actualChild);
    }

    /**
     * Test method getChildren.
     */
    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Node parent = new Element("div", baseUri);

        Node child1 = new Element("p", baseUri);
        Node child2 = new Element("p", baseUri);
        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> expectedChildren = new ArrayList<>();
        expectedChildren.add(child1);
        expectedChildren.add(child2);

        List<Node> actualChildren = parent.childNodes();

        assertEquals(expectedChildren, actualChildren);
    }

    /**
     * Test method clone.
     */
    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node originalNode = new Element("div", baseUri, attributes);

        Node clonedNode = originalNode.clone();

        assertEquals(originalNode.nodeName(), clonedNode.nodeName());
        assertEquals(originalNode.baseUri(), clonedNode.baseUri());
        assertEquals(originalNode.attributes(), clonedNode.attributes());
        assertEquals(originalNode.childNodes(), clonedNode.childNodes());

        // Test cloning a cloned node
        Node clonedClonedNode = clonedNode.clone();

        assertNotSame(originalNode, clonedNode);
        assertNotSame(originalNode, clonedClonedNode);
        assertNotSame(clonedNode, clonedClonedNode);

        // Test cloning a node with children
        Node parent = new Element("div", baseUri);
        Node child1 = new Element("p", baseUri);
        Node child2 = new Element("p", baseUri);
        parent.appendChild(child1);
        parent.appendChild(child2);

        Node clonedParent = parent.clone();

        List<Node> expectedChildren = new ArrayList<>();
        expectedChildren.add(child1.clone());
        expectedChildren.add(child2.clone());

        List<Node> actualChildren = clonedParent.childNodes();

        assertEquals(expectedChildren, actualChildren);
    }

    /**
     * Test method remove.
     */
    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Node parent = new Element("div", baseUri);
        Node child = new Element("p", baseUri);
        parent.appendChild(child);

        parent.removeChild(child);

        List<Node> expectedChildren = new ArrayList<>();

        List<Node> actualChildren = parent.childNodes();

        assertEquals(expectedChildren, actualChildren);
        assertNull(child.parent());
    }

    /**
     * Test method replaceWith.
     */
    @Test
    public void testReplaceWith() {
        String baseUri = "http://example.com";
        Node parent = new Element("div", baseUri);
        Node oldChild = new Element("p", baseUri);
        Node newChild = new Element("span", baseUri);

        parent.appendChild(oldChild);
        parent.replaceChild(oldChild, newChild);

        List<Node> expectedChildren = new ArrayList<>();
        expectedChildren.add(newChild);

        List<Node> actualChildren = parent.childNodes();

        assertEquals(expectedChildren, actualChildren);
        assertSame(parent, newChild.parent());
    }

    /**
     * Test method setParentNode.
     */
    @Test
    public void testSetParentNode() {
        String baseUri = "http://example.com";
        Node child = new Element("p", baseUri);
        Node parent = new Element("div", baseUri);

        child.setParentNode(parent);

        assertSame(parent, child.parent());
    }

    /**
     * Test method replaceChild.
     */
    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Node parent = new Element("div", baseUri);
        Node oldChild = new Element("p", baseUri);
        Node newChild = new Element("span", baseUri);

        parent.appendChild(oldChild);
        parent.replaceChild(oldChild, newChild);

        List<Node> expectedChildren = new ArrayList<>();
        expectedChildren.add(newChild);

        List<Node> actualChildren = parent.childNodes();

        assertEquals(expectedChildren, actualChildren);
        assertSame(parent, newChild.parent());
    }

    /**
     * Test method removeChild.
     */
    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Node parent = new Element("div", baseUri);
        Node child1 = new Element("p", baseUri);
        Node child2 = new Element("p", baseUri);
        parent.appendChild(child1);
        parent.appendChild(child2);

        parent.removeChild(child1);

        List<Node> expectedChildren = new ArrayList<>();
        expectedChildren.add(child2);

        List<Node> actualChildren = parent.childNodes();

        assertEquals(expectedChildren, actualChildren);
        assertNull(child1.parent());
    }

    /**
     * Test method addChildren.
     */
    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Node parent = new Element("div", baseUri);

        Node child1 = new Element("p", baseUri);
        Node child2 = new Element("p", baseUri);

        parent.addChildren(child1, child2);

        List<Node> expectedChildren = new ArrayList<>();
        expectedChildren.add(child1);
        expectedChildren.add(child2);

        List<Node> actualChildren = parent.childNodes();

        assertEquals(expectedChildren, actualChildren);
    }

    /**
     * Test method addChildren.
     */
    @Test
    public void testAddChildrenWithIndex() {
        String baseUri = "http://example.com";
        Node parent = new Element("div", baseUri);

        Node child1 = new Element("p", baseUri);
        Node child2 = new Element("p", baseUri);

        parent.addChildren(1, child1, child2);

        List<Node> expectedChildren = new ArrayList<>();
        expectedChildren.add(child1);
        expectedChildren.add(child2);

        List<Node> actualChildren = parent.childNodes();

        assertEquals(expectedChildren, actualChildren);
        assertEquals(child1, parent.childNode(1));
    }

    /**
     * Test method outerHtml.
     */
    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Node node = new Element("p", baseUri).attr("id", "test");
        node.appendChild(new TextNode("Test"));

        String expectedOuterHtml = "<p id=\"test\">Test</p>";

        String actualOuterHtml = node.outerHtml();

        assertEquals(expectedOuterHtml, actualOuterHtml);
    }
}