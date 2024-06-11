package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testAbstractNodeConstructor() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        assertEquals("http://test.com", node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode);
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testAbstractNodeStringConstructor() {
        String baseUri = "http://test.com";
        Node node = new Node(baseUri);

        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
        assertNull(node.parentNode);
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testAbstractNodeDefaultConstructor() {
        Node node = new Node();

        assertNull(node.baseUri());
        assertNull(node.attributes());
        assertNull(node.parentNode);
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testAbstractNodeGetNodeName() {
        Node node = new Node() {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        assertEquals("div", node.nodeName());
    }

    @Test(expected = AssertionError.class)
    public void testAbstractNodeGetNodeNameException() {
        Node node = new Node();

        try {
            node.nodeName();
        } catch (AssertionError e) {
            throw e;
        }
        fail("Expected AssertionError");
    }

    @Test
    public void testAbstractNodeSetBaseUri() {
        String baseUri = "http://test.com";
        Node node = new Node(baseUri);

        node.setBaseUri("http://another.com");
        assertEquals("http://another.com", node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testAbstractNodeSetBaseUriNull() {
        Node node = new Node();

        try {
            node.setBaseUri(null);
        } catch (NullPointerException e) {
            throw e;
        }
        fail("Expected NullPointerException");
    }

    @Test
    public void testAbstractNodeGetBaseUri() {
        String baseUri = "http://test.com";
        Node node = new Node(baseUri);

        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testAbstractNodeHasAttr() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");
        Node node = new Node(baseUri, attributes);

        assertTrue(node.hasAttr("test"));
        assertTrue(node.hasAttr("abs:test"));
        assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testAbstractNodeAttr() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");
        Node node = new Node(baseUri, attributes);

        assertEquals("testValue", node.attr("test"));
        assertEquals("", node.attr("nonexistent"));
        assertEquals("", node.attr("abs:nonexistent"));
    }

    @Test
    public void testAbstractNodeAttrSet() {
        String baseUri = "http://test.com";
        Node node = new Node(baseUri);

        node.attr("test", "newTestValue");
        assertEquals("newTestValue", node.attr("test"));
    }

    @Test
    public void testAbstractNodeRemoveAttr() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("test", "testValue");
        Node node = new Node(baseUri, attributes);

        node.removeAttr("test");
        assertFalse(node.hasAttr("test"));
    }

    @Test
    public void testAbstractNodeGetChildren() {
        Node parentNode = new Node();
        Node child1 = new Node();
        Node child2 = new Node();

        parentNode.addChildren(child1, child2);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testAbstractNodeGetChildNode() {
        Node parentNode = new Node();
        Node child = new Node();

        parentNode.addChild(child);

        Node retrievedChild = parentNode.childNode(0);
        assertEquals(child, retrievedChild);
    }

    @Test
    public void testAbstractNodeGetEmptyChildNodes() {
        Node node = new Node();

        List<Node> children = node.childNodes();
        assertTrue(children.isEmpty());
    }

    @Test
    public void testAbstractNodeSetParentNode() {
        Node child = new Node();
        Node parent = new Node();

        child.setParentNode(parent);
        assertEquals(parent, child.parentNode);
    }

    @Test
    public void testAbstractNodeSetParentNodeNull() {
        Node node = new Node();

        try {
            node.setParentNode(null);
        } catch (NullPointerException e) {
            throw e;
        }
        fail("Expected NullPointerException");
    }

    @Test
    public void testAbstractNodeRemove() {
        Node parent = new Node();
        Node child = new Node();
        parent.addChild(child);

        parent.removeChild(child);
        assertNull(child.parentNode);
    }

    @Test
    public void testAbstractNodeReplaceChild() {
        Node parent = new Node();
        Node child1 = new Node();
        Node child2 = new Node();
        parent.addChild(child1);

        parent.replaceChild(child1, child2);
        assertEquals(child2, parent.childNode(0));
    }

    @Test
    public void testAbstractNodeReplaceChildWithSelf() {
        Node parent = new Node();
        Node child = new Node();
        parent.addChild(child);

        try {
            parent.replaceChild(child, child);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractNodeReplaceChildNull() {
        Node parent = new Node();
        Node child = new Node();
        parent.addChild(child);

        try {
            parent.replaceChild(null, child);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractNodeReplaceChildWithDifferentType() {
        Node parent = new Node();
        Node child1 = new Node();
        Object child2 = new Object();
        parent.addChild(child1);

        try {
            parent.replaceChild(child1, child2);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractNodeAddChildren() {
        Node parent = new Node();
        Node child1 = new Node();
        Node child2 = new Node();

        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testAbstractNodeAddChildrenNull() {
        Node parent = new Node();
        Node child = new Node();

        try {
            parent.addChildren((Node[]) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractNodeAddChildrenEmptyArray() {
        Node parent = new Node();

        parent.addChildren(new Node[0]);
        assertTrue(parent.childNodes().isEmpty());
    }

    @Test
    public void testAbstractNodeAddChildrenWithSelf() {
        Node parent = new Node();
        Node child = new Node();

        try {
            parent.addChildren(child, child);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractNodeAddChildrenWithDifferentType() {
        Node parent = new Node();
        Object child = new Object();

        try {
            parent.addChildren(child);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }
}