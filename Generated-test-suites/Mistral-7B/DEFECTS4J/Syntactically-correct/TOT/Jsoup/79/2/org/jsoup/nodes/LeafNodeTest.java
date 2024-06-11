package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.TextNode;
import org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testCoreValue() {
        String nodeName = "testNode";
        LeafNode leafNode = new TextNode(nodeName);
        String expectedCoreValue = nodeName;

        String coreValue = leafNode.coreValue();
        assertEquals(expectedCoreValue, coreValue);

        leafNode.coreValue("newValue");
        expectedCoreValue = "newValue";
        coreValue = leafNode.coreValue();
        assertEquals(expectedCoreValue, coreValue);
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new TextNode("testNode");
        assertFalse(leafNode.hasAttributes());

        LeafNode withAttributes = leafNode.ensureAttributes();
        assertTrue(withAttributes.hasAttributes());
        Attributes attributes = withAttributes.attributes();
        assertEquals(0, attributes.size());
    }

    @Test
    public void testAttrAndRemoveAttr() {
        LeafNode leafNode = new TextNode("testNode");
        String attributeKey = "testAttribute";
        String attributeValue = "testValue";

        leafNode.attr(attributeKey, attributeValue);
        assertTrue(leafNode.hasAttr(attributeKey));
        assertEquals(attributeValue, leafNode.attr(attributeKey));

        leafNode.removeAttr(attributeKey);
        assertFalse(leafNode.hasAttr(attributeKey));
        assertEquals(EmptyString, leafNode.attr(attributeKey));
    }
}