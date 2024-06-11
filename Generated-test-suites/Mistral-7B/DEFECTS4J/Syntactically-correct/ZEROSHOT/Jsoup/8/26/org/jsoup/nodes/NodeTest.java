package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Attributes;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.AttributesImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test file for Node class.
 */
public class NodeTest {

    @org.junit.Before
    public void setUp() {
    }

    @org.junit.After
    public void tearDown() {
    }

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        String baseUri = "http://test.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("name", "value");

        Node node = new Node(baseUri, attributes);
        Node nodeWithoutAttributes = new Node(baseUri);
        Node nodeWithoutBaseUri = new Node();

        assertNotNull(node);
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertEquals(0, node.childNodes().size());

        assertNotNull(nodeWithoutAttributes);
        assertNull(nodeWithoutAttributes.baseUri());
        assertNull(nodeWithoutAttributes.attributes());
        assertEquals(0, nodeWithoutAttributes.childNodes().size());

        assertNotNull(nodeWithoutBaseUri);
        assertNull(nodeWithoutBaseUri.baseUri());
        assertNull(nodeWithoutBaseUri.attributes());
        assertEquals(0, nodeWithoutBaseUri.childNodes().size());
    }

    /**
     * Test nodeName method.
     */
    @org.junit.Test
    public void testNodeName() {
        Element element = new Element("div");
        TextNode textNode = new TextNode("text");

        assertEquals("div", element.nodeName());
        assertEquals("text", textNode.nodeName());
    }

    /**
     * Test getAttribute method.
     */
    @org.junit.Test
    public void testAttr() {
        Attributes attributes = new AttributesImpl();
        attributes.put("name", "value");

        Node node = new Element("div", attributes);

        assertEquals("value", node.attr("name"));
        assertEquals("", node.attr("nonExistentAttribute"));
        assertNull(node.attr("nonExistentAttribute").trim());
    }

    /**
     * Test getAttributes method.
     */
    @org.junit.Test
    public void testAttributes() {
        Attributes attributes = new AttributesImpl();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");

        Node node = new Element("div", attributes);

        assertEquals(attributes, node.attributes());

        List<String> attributesList = new ArrayList<>(Arrays.asList(
                "name1", "name2"
        ));

        assertEquals(attributesList, new ArrayList<>(node.attributes().names()));
    }

    /**
     * Test hasAttr method.
     */
    @org.junit.Test
    public void testHasAttr() {
        Attributes attributes = new AttributesImpl();
        attributes.put("name", "value");

        Node node = new Element("div", attributes);

        assertTrue(node.hasAttr("name"));
        assertFalse(node.hasAttr("nonExistentAttribute"));
    }

    /**
     * Test removeAttribute method.
     */
    @org.junit.Test
    public void testRemoveAttr() {
        Attributes attributes = new AttributesImpl();
        attributes.put("name", "value");
        attributes.put("nonExistentAttribute", "nonExistentValue");

        Node node = new Element("div", attributes);

        node.removeAttr("name");
        assertEquals(0, node.childNodes().size());
        assertFalse(node.hasAttr("name"));

        node.removeAttr("nonExistentAttribute");
        assertEquals(0, node.childNodes().size());
        assertFalse(node.hasAttr("nonExistentAttribute"));
    }

    /**
     * Test getBaseUri method.
     */
    @org.junit.Test
    public void testBaseUri() {
        String baseUri = "http://test.com";
        Node node = new Element("div", new AttributesImpl(), baseUri);

        assertEquals(baseUri, node.baseUri());
    }

    /**
     * Test setBaseUri method.
     */
    @org.junit.Test
    public void testSetBaseUri() {
        Node node = new Element("div", new AttributesImpl());

        String newBaseUri = "http://new.test.com";
        node.setBaseUri(newBaseUri);

        assertEquals(newBaseUri, node.baseUri());
    }

    /**
     * Test absUrl method.
     */
    @org.junit.Test
    public void testAbsUrl() {
        String baseUri = "http://test.com";
        Node node = new Element("a", new AttributesImpl(), baseUri);
        node.attr("href", "/path/to/test.html");

        String absUrl = node.absUrl("href");
        assertEquals("http://test.com/path/to/test.html", absUrl);

        absUrl = node.absUrl("nonExistentAttribute");
        assertEquals("", absUrl);
    }

    /**
     * Test getChildNode method.
     */
    @org.junit.Test
    public void testChildNode() {
        Element parent = new Element("div");
        Element child = new Element("p");

        parent.appendChild(child);

        Node node = parent.childNode(0);

        assertEquals(child, node);
    }

    /**
     * Test getChildNodes method.
     */
    @org.junit.Test
    public void testChildNodes() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");

        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> children = parent.childNodes();

        assertEquals(2, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
    }

    /**
     * Test getParent method.
     */
    @org.junit.Test
    public void testParent() {
        Element parent = new Element("div");
        Element child = new Element("p");

        parent.appendChild(child);

        Node node = child;

        assertEquals(parent, node.parent());
        assertNull(node.parent()); // for default Node instance
    }

    /**
     * Test ownerDocument method.
     */
    @org.junit.Test
    public void testOwnerDocument() {
        Document document = new Document();
        Element element = new Element("p");
        document.appendChild(element);

        Node node = element;

        assertEquals(document, node.ownerDocument());
        assertNull(new Node().ownerDocument());
    }

    /**
     * Test remove method.
     */
    @org.junit.Test
    public void testRemove() {
        Element parent = new Element("div");
        Element child = new Element("p");

        parent.appendChild(child);

        Node node = child;
        parent.removeChild(node);

        assertEquals(0, parent.childNodes().size());
        assertNull(node.parent());
    }

    /**
     * Test replaceWith method.
     */
    @org.junit.Test
    public void testReplaceWith() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");

        parent.appendChild(child1);

        Node node1 = child1;
        parent.replaceChild(node1, child2);

        assertEquals(1, parent.childNodes().size());
        assertNotNull(node1.parent()); // should still have a reference to parent
        assertEquals(child2, parent.childNodes().get(0));
    }

    /**
     * Test setParentNode method.
     */
    @org.junit.Test
    public void testSetParentNode() {
        Element parent1 = new Element("div");
        Element child = new Element("p");

        child.setParentNode(parent1);

        Element parent2 = new Element("div");
        child.setParentNode(parent2);

        assertEquals(parent1, child.parent());
        assertEquals(parent2, child.parent()); // should not affect previous parent
    }

    /**
     * Test replaceChild method.
     */
    @org.junit.Test
    public void testReplaceChild() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");

        parent.appendChild(child1);

        Node node1 = child1;
        parent.replaceChild(node1, child2);

        assertEquals(1, parent.childNodes().size());
        assertNotNull(node1.parent()); // should still have a reference to parent
        assertEquals(child2, parent.childNodes().get(0));
    }

    /**
     * Test removeChild method.
     */
    @org.junit.Test
    public void testRemoveChild() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");

        parent.appendChild(child1);
        parent.appendChild(child2);

        Node node1 = child1;
        parent.removeChild(node1);

        assertEquals(1, parent.childNodes().size());
        assertNull(node1.parent());
    }

    /**
     * Test addChildren method.
     */
    @org.junit.Test
    public void testAddChildren() {
        Element parent = new Element("div");

        Node node1 = new Element("p");
        Node node2 = new Element("p");
        Node node3 = new Element("p");

        parent.addChildren(node1, node2, node3);

        List<Node> children = parent.childNodes();
        assertEquals(3, children.size());
        assertTrue(children.contains(node1));
        assertTrue(children.contains(node2));
        assertTrue(children.contains(node3));
    }

    /**
     * Test siblingNodes method.
     */
    @org.junit.Test
    public void testSiblingNodes() {
        Element parent = new Element("div");
        Element sibling1 = new Element("p");
        Element child1 = new Element("p");
        Element child2 = new Element("p");
        Element sibling2 = new Element("p");

        parent.appendChild(sibling1);
        parent.appendChild(child1);
        parent.appendChild(child2);
        parent.appendChild(sibling2);

        List<Node> siblings = parent.siblingNodes();

        assertEquals(3, siblings.size());
        assertTrue(siblings.contains(sibling1));
        assertTrue(siblings.contains(child1));
        assertTrue(siblings.contains(child2));
        assertTrue(siblings.contains(sibling2));
    }

    /**
     * Test nextSibling method.
     */
    @org.junit.Test
    public void testNextSibling() {
        Element parent = new Element("div");
        Element sibling1 = new Element("p");
        Element child1 = new Element("p");
        Element sibling2 = new Element("p");

        parent.appendChild(sibling1);
        parent.appendChild(child1);
        parent.appendChild(sibling2);

        Node node = child1;

        assertEquals(sibling2, node.nextSibling());
        assertNull(new Node().nextSibling());
    }

    /**
     * Test previousSibling method.
     */
    @org.junit.Test
    public void testPreviousSibling() {
        Element parent = new Element("div");
        Element sibling1 = new Element("p");
        Element child1 = new Element("p");
        Element sibling2 = new Element("p");

        parent.appendChild(sibling1);
        parent.appendChild(child1);
        parent.appendChild(sibling2);

        Node node = child1;

        assertEquals(sibling1, node.previousSibling());
        assertNull(new Node().previousSibling());
    }

    /**
     * Test siblingIndex method.
     */
    @org.junit.Test
    public void testSiblingIndex() {
        Element parent = new Element("div");
        Element sibling1 = new Element("p");
        Element child1 = new Element("p");
        Element sibling2 = new Element("p");

        parent.appendChild(sibling1);
        parent.appendChild(child1);
        parent.appendChild(sibling2);

        Node node = child1;

        assertEquals(1, node.siblingIndex());
    }

    /**
     * Test setSiblingIndex method.
     */
    @org.junit.Test
    public void testSetSiblingIndex() {
        Element parent = new Element("div");
        Element sibling1 = new Element("p");
        Element child1 = new Element("p");
        Element sibling2 = new Element("p");

        parent.appendChild(sibling1);
        parent.appendChild(child1);
        parent.appendChild(sibling2);

        Node node = child1;

        node.setSiblingIndex(0);

        assertEquals(sibling2, node.previousSibling());
        assertEquals(sibling1, node.nextSibling());
    }

    /**
     * Test outerHtml method.
     */
    @org.junit.Test
    public void testOuterHtml() {
        Element element = new Element("div");
        element.attr("id", "test");
        element.attr("class", "test-class");

        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        element.appendChild(child1);
        element.appendChild(child2);

        StringBuilder sb = new StringBuilder();
        element.outerHtml(sb);

        String html = sb.toString();

        String expectedHtml = "<div id=\"test\" class=\"test-class\">\n" +
                "  <p>Child 1</p>\n" +
                "  <p>Child 2</p>\n" +
                "</div>";

        assertEquals(expectedHtml, html);
    }

    /**
     * Test toString method.
     */
    @org.junit.Test
    public void testToString() {
        Element element = new Element("div");
        element.attr("id", "test");
        element.attr("class", "test-class");

        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        element.appendChild(child1);
        element.appendChild(child2);

        String stringRepresentation = element.toString();

        String expectedStringRepresentation = "<div id=\"test\" class=\"test-class\">\n" +
                "  <p>Child 1</p>\n" +
                "  <p>Child 2</p>\n" +
                "</div>";

        assertEquals(expectedStringRepresentation, stringRepresentation);
    }
}