package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;

import static org.junit.Assert.*;

public class LeafNodeTest {
    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                value.put("testAttribute", "testValue");
            }
        };
        assertTrue(leafNode.hasAttributes());

        LeafNode leafNodeWithoutAttributes = new LeafNode();
        assertFalse(leafNodeWithoutAttributes.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "Test Value";
            }
        };
        assertEquals("Test Value", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new LeafNode() {
            {
                value = new Attributes();
                value.put("testAttribute", "testValue");
            }
        };
        assertEquals("testAttribute", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };

        leafNode.attr("testAttribute", "testValue");
        assertEquals("testValue", leafNode.attr("testAttribute"));

        leafNode.attr("testAttribute2", "testValue2");
        assertEquals("testValue2", leafNode.attr("testAttribute2"));

        leafNode.attr("testAttribute3", null);
        assertNull(leafNode.attr("testAttribute3"));

        leafNode.attr("testAttribute", null);
        assertNull(leafNode.attr("testAttribute"));

        leafNode.attr("nodeName", "testNodeName");
        assertEquals("testNodeName", leafNode.nodeName());

        LeafNode leafNodeWithInitialValue = new LeafNode() {
            {
                value = "Initial Value";
            }
        };

        leafNodeWithInitialValue.attr("testAttribute", "testValue");
        assertEquals("testValue", leafNodeWithInitialValue.attr("testAttribute"));
        assertEquals("Initial Value", leafNodeWithInitialValue.coreValue());
    }
}