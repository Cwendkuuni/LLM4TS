package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("testClass", node1.attr("class"));
        assertNull(node2.attr("class"));
        assertNull(node3.attr("class"));

        assertEquals(baseUri, node1.baseUri());
        assertEquals(baseUri, node2.baseUri());
        assertNull(node3.baseUri());
    }

    @Test
    public void testAbstractMethods() {
        // Since Node is abstract, we cannot instantiate it directly to test its abstract methods.
        // Instead, we can create a subclass (e.g. Element) and test its implementation of the abstract methods.
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");

        Node node = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        node.attr("class", "newClass");

        assertEquals("newClass", node.attr("class"));
        assertEquals("testClass", node.attr("class", "testClass")); //TEST: check if the old value is replaced
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonexistent"));
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String relativeUrl = "relative.html";

        Node node = new Node(baseUri) {
            @Override
            public String nodeName() {
                return "a";
            }
        };
        node.attr("href", relativeUrl);

        String absUrl = node.absUrl("href");
        String expectedAbsUrl = "http://example.com/relative.html";

        assertEquals(expectedAbsUrl, absUrl);
    }

    @Test
    public void testChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        List<Node> children = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            Node child = new Node(baseUri, attributes) {
                @Override
                public String nodeName() {
                    return "p";
                }
            };
            parentNode.addChild(child);
            children.add(child);
        }

        assertEquals(3, parentNode.childNodes().size());
        assertArrayEquals(children.toArray(new Node[0]), parentNode.childNodesAsArray());

        Node child = parentNode.childNode(1);
        assertEquals(child, children.get(1));

        parentNode.removeChild(child);
        assertFalse(parentNode.hasChildNode(child));
        assertEquals(2, parentNode.childNodes().size());
    }

    @Test
    public void testParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node childNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };

        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };
        parentNode.setParentNode(null);
        parentNode.addChild(childNode);

        assertEquals(childNode, childNode.parent());
        assertEquals(parentNode, parentNode.parent());
        assertNull(parentNode.parent());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Node childNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };
        parentNode.addChild(childNode);

        parentNode.remove();

        assertFalse(parentNode.hasChildNodes());
    }
}