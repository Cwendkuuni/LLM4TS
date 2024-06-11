package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test cases for Node class.
 */
public class NodeTest {

    private Node rootNode;
    private Node elementNode;
    private Node attributeNode;
    private Node textNode;

    @Before
    public void setUp() {
        rootNode = new Document(); // root node is a document
        elementNode = new Element("div");
        attributeNode = new Attribute("class", "test");
        textNode = new TextNode("Test text");

        rootNode.appendChild(elementNode);
        elementNode.appendChild(attributeNode);
        elementNode.appendChild(textNode);
    }

    @Test
    public void testNodeName() {
        assertEquals("div", elementNode.nodeName());
        assertFalse(elementNode.nodeName().equals("text"));
    }

    @Test
    public void testAttr() {
        assertEquals("class", attributeNode.attr("name"));
        assertEquals("test", attributeNode.attr("value"));
        assertNull(attributeNode.attr("nonExistentAttr"));

        assertEquals("test", elementNode.attr("class"));
        assertEquals("", elementNode.attr("nonExistentAttr"));

        // test absUrl
        assertEquals("", elementNode.absUrl("nonExistentAttr"));
        String baseUri = "http://example.com/";
        elementNode.setBaseUri(baseUri);
        assertEquals(baseUri + "/div/@class", elementNode.absUrl("class"));
        assertEquals(baseUri + "/div/@abs:class", elementNode.absUrl("abs:class"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(elementNode.hasAttr("class"));
        assertFalse(elementNode.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        elementNode.removeAttr("class");
        assertFalse(elementNode.hasAttr("class"));

        elementNode.attr("class", "updatedClass");
        elementNode.removeAttr("class");
        assertFalse(elementNode.hasAttr("class"));
    }

    @Test
    public void testAttributeList() {
        List<Node> attributes = elementNode.childNodesAsArray();
        Node attr1 = Arrays.asList(attributes)[0];
        assertEquals("class", attr1.nodeName());
        assertEquals("test", attr1.attributes().get("value"));

        List<Node> attributes2 = elementNode.childNodesAsArray();
        Node attr2 = Arrays.asList(attributes2)[1];
        assertEquals("Text text", attr2.nodeName());
        assertFalse(attr2.hasAttr("name"));
    }

    @Test
    public void testChildNodes() {
        List<Node> children = elementNode.childNodes();
        Node child1 = children.get(0);
        assertEquals("class", child1.nodeName());
        assertEquals("test", child1.attributes().get("value"));

        Node child2 = children.get(1);
        assertEquals("Text text", child2.nodeName());
        assertFalse(child2.hasAttr("name"));

        // test getting child by index
        Node child3 = elementNode.childNode(2);
        assertEquals("Text text", child3.nodeName());
    }

    @Test
    public void testParentNode() {
        assertEquals(rootNode, elementNode.parent());
        assertNull(textNode.parent());
    }
}