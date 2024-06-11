package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class LeafNodeTest {
    @org.junit.Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode();
        assertTrue(leafNode.hasAttributes() == false);
        leafNode.coreValue("test");
        assertTrue(leafNode.hasAttributes() == true);
    }

    @org.junit.Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode();
        leafNode.coreValue("test");
        assertEquals("test", leafNode.coreValue());
    }

    @org.junit.Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode();
        leafNode.coreValue("test");
        leafNode.attr("test", "value");
        assertEquals("value", leafNode.attr("test"));

        leafNode.coreValue("test2");
        leafNode.attr("test2", null);
        assertEquals(null, leafNode.attr("test2"));
    }

    @org.junit.Test
    public void testEdgeCases() {
        LeafNode leafNode = new LeafNode();

        // Edge case: empty string for value
        leafNode.coreValue("");
        assertEquals("", leafNode.coreValue());

        // Edge case: null attributes
        leafNode.ensureAttributes();
        Attributes attributes = (Attributes) leafNode.value;
        assertNull(attributes);

        // Edge case: null nodeName
        try {
            leafNode.attr(null, "value");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Edge case: null key for attr()
        try {
            leafNode.attr(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Exception handling: attempt to set value and attributes concurrently
        leafNode.coreValue("test");
        leafNode.ensureAttributes();
        try {
            leafNode.attr("test", "value");
            leafNode.coreValue("test2");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Edge case: attempt to add a child node
        leafNode.coreValue("test");
        Node childNode = new TextNode("child");
        try {
            leafNode.add(childNode);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}