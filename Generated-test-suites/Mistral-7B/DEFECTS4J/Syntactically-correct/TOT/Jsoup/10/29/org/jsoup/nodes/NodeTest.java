package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testAbstractNode() {
        // Test empty Node
        Node emptyNode = new Node();
        assertEquals("", emptyNode.nodeName());
        assertNull(emptyNode.attributes());
        assertNull(emptyNode.baseUri());
        assertEquals(Collections.EMPTY_LIST, emptyNode.childNodes());

        // Test Node with attributes
        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        String baseUri = "http://example.com";
        Node nodeWithAttributes = new Node(baseUri, attributes);
        assertEquals("", nodeWithAttributes.nodeName());
        assertEquals(attributes, nodeWithAttributes.attributes());
        assertEquals(baseUri, nodeWithAttributes.baseUri());
        assertEquals(Collections.EMPTY_LIST, nodeWithAttributes.childNodes());
        assertEquals("testClass", nodeWithAttributes.attr("class"));
        assertFalse(nodeWithAttributes.hasAttr("nonexistentAttr"));

        // Test Node with children
        Node child1 = new Node("Child1");
        Node child2 = new Node("Child2");
        List<Node> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);
        Node nodeWithChildren = new Node("Root") {
            @Override
            public String nodeName() {
                return "Root";
            }
        };
        nodeWithChildren.setBaseUri("http://example.com");
        nodeWithChildren.setAttributes(attributes);
        nodeWithChildren.setChildNodes(children);
        assertEquals("Root", nodeWithChildren.nodeName());
        assertEquals(attributes, nodeWithChildren.attributes());
        assertEquals(baseUri, nodeWithChildren.baseUri());
        assertEquals(children, nodeWithChildren.childNodes());
        assertEquals(child1, nodeWithChildren.childNode(0));
        assertEquals(child2, nodeWithChildren.childNode(1));

        // Test get absolute URL from attribute
        assertEquals("", nodeWithAttributes.absUrl(""));
        assertEquals("http://example.com/Child1", nodeWithChildren.absUrl("child1"));
        assertEquals("http://example.com/Child2", nodeWithChildren.absUrl("child2"));

        // Test set and get base URI
        nodeWithChildren.setBaseUri("http://newbase.com");
        assertEquals("http://newbase.com", nodeWithChildren.baseUri());

        // Test get and set attributes
        nodeWithChildren.attr("testAttr", "testValue");
        assertEquals("testValue", nodeWithChildren.attr("testAttr"));
        assertFalse(nodeWithChildren.hasAttr("nonexistentAttr"));
        nodeWithChildren.removeAttr("testAttr");
        assertFalse(nodeWithChildren.hasAttr("testAttr"));

        // Test remove child
        nodeWithChildren.removeChild(child1);
        assertEquals(child2, nodeWithChildren.childNode(0));
        assertEquals(1, nodeWithChildren.childNodes().size());

        // Test add children
        Node newChild = new Node("NewChild");
        nodeWithChildren.addChildren(newChild);
        assertEquals(newChild, nodeWithChildren.childNodes().get(2));
        assertEquals(3, nodeWithChildren.childNodes().size());

        // Test remove
        nodeWithChildren.remove(); // Will remove the node from the owner document
    }
}