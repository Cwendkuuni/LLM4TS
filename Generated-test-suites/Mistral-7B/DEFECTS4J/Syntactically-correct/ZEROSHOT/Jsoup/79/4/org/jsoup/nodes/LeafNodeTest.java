package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TextNode("Test");
        Assert.assertFalse(leafNode.hasAttributes());

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode leafNodeWithAttributes = new TextNode("Test", attributes);
        Assert.assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testGetAttributes() {
        LeafNode leafNode = new TextNode("Test");
        Attributes expectedAttributes = new Attributes();
        Assert.assertNull(leafNode.attributes());

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode leafNodeWithAttributes = new TextNode("Test", attributes);
        Assert.assertEquals(attributes, leafNodeWithAttributes.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new TextNode("Test");
        Assert.assertEquals("Test", leafNode.coreValue());

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode leafNodeWithAttributes = new TextNode("Test", attributes);
        Assert.assertEquals("Test", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue("New Value");
        Assert.assertEquals("New Value", leafNode.coreValue());

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode leafNodeWithAttributes = new TextNode("Test", attributes);
        leafNodeWithAttributes.coreValue("New Value");
        Assert.assertEquals("New Value", leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("Test");
        Assert.assertEquals("Test", leafNode.attr("name"));
        Assert.assertEquals("", leafNode.attr("otherName"));

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode leafNodeWithAttributes = new TextNode("Test", attributes);
        Assert.assertEquals("value", leafNodeWithAttributes.attr("test"));
        Assert.assertEquals("", leafNodeWithAttributes.attr("otherName"));
    }

    @Test
    public void testAttrSetter() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.attr("test", "value");
        Assert.assertEquals("value", leafNode.attr("test"));

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode leafNodeWithAttributes = new TextNode("Test", attributes);
        leafNodeWithAttributes.attr("test", "newValue");
        Assert.assertEquals("newValue", leafNodeWithAttributes.attr("test"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("Test");
        Assert.assertFalse(leafNode.hasAttr("test"));

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode leafNodeWithAttributes = new TextNode("Test", attributes);
        Assert.assertTrue(leafNodeWithAttributes.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new TextNode("Test");
        LeafNode removedAttrNode = leafNode.removeAttr("test");
        Assert.assertNotNull(removedAttrNode);
        Assert.assertFalse(leafNode.hasAttr("test"));

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode leafNodeWithAttributes = new TextNode("Test", attributes);
        LeafNode removedAttrNodeWithAttributes = leafNodeWithAttributes.removeAttr("test");
        Assert.assertNotNull(removedAttrNodeWithAttributes);
        Assert.assertFalse(leafNodeWithAttributes.hasAttr("test"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TextNode("Test");
        Assert.assertEquals("", leafNode.absUrl(""));

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        LeafNode leafNodeWithAttributes = new TextNode("Test", attributes);
        Assert.assertEquals("", leafNodeWithAttributes.absUrl(""));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new TextNode("Test");
        Assert.assertEquals("", leafNode.baseUri());

        Node parentNode = new Elements.EmptyElement("parent");
        leafNode.parent(parentNode);
        Assert.assertEquals("", leafNode.baseUri());

        Node parentNodeWithBaseUri = new Elements.EmptyElement("parent");
        parentNodeWithBaseUri.baseUri("/path/to/base");
        leafNode.parent(parentNodeWithBaseUri);
        Assert.assertEquals("/path/to/base", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodes() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.childNodes();
    }
}