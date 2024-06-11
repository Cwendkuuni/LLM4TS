package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testCoreValue() {
        TextNode textNode = new TextNode("Test");
        assertEquals("Test", textNode.coreValue());

        Attributes attributes = new Attributes();
        attributes.put("test", "attributeValue");
        LeafNode attributesNode = new AttributesNode(attributes);
        assertEquals("", attributesNode.coreValue());

        textNode.coreValue("newValue");
        assertEquals("newValue", textNode.coreValue());
    }

    @Test
    public void testHasAttributes() {
        TextNode textNode = new TextNode("Test");
        assertFalse(textNode.hasAttributes());

        Attributes attributes = new Attributes();
        LeafNode attributesNode = new AttributesNode(attributes);
        assertTrue(attributesNode.hasAttributes());
    }

    @Test
    public void testAttr() {
        TextNode textNode = new TextNode("Test");
        assertEquals("", textNode.attr("test"));

        LeafNode attributesNode = new TextNode("TestAttribute");
        attributesNode.coreValue("TestValue");
        assertEquals("TestValue", attributesNode.attr("test"));

        Attributes attributes = new Attributes();
        attributes.put("test", "attributeValue");
        LeafNode attributesNodeWithAttributes = new AttributesNode(attributes);
        attributesNodeWithAttributes.attr("test", "newValue");
        assertEquals("newValue", attributesNodeWithAttributes.attr("test"));
    }
}