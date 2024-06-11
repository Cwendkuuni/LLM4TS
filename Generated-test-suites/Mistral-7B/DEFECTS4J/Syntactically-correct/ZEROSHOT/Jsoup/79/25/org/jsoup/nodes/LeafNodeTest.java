package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helpers.Validate;

import java.util.List;

public class LeafNodeTest {

    @org.junit.Test
    public void testHasAttributes() {
        LeafNode leafNode = new TextNode("Test String");
        assertFalse(leafNode.hasAttributes());

        LeafNode leafNodeWithAttributes = new Attributes();
        leafNodeWithAttributes.put("testKey1", "testValue1");
        leafNodeWithAttributes.put("testKey2", "testValue2");
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @org.junit.Test
    public void testGetAttributes() {
        LeafNode leafNode = new TextNode("Test String");
        Attributes attributes = leafNode.attributes();
        assertNull(attributes);

        LeafNode leafNodeWithAttributes = new Attributes();
        leafNodeWithAttributes.put("testKey1", "testValue1");
        leafNodeWithAttributes.put("testKey2", "testValue2");
        Attributes attributes = leafNodeWithAttributes.attributes();
        assertNotNull(attributes);
        assertEquals("testValue1", attributes.get("testKey1"));
        assertEquals("testValue2", attributes.get("testKey2"));
    }

    @org.junit.Test
    public void testCoreValue() {
        LeafNode leafNode = new TextNode("Test String");
        assertEquals("Test String", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new Attributes();
        leafNodeWithAttributes.put("testKey", "Test String");
        assertEquals("Test String", leafNodeWithAttributes.coreValue());
    }

    @org.junit.Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new TextNode("Test String");
        leafNode.coreValue("New Test String");
        assertEquals("New Test String", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new Attributes();
        leafNodeWithAttributes.put("testKey", "Test String");
        leafNodeWithAttributes.coreValue("New Test String");
        assertEquals("New Test String", leafNodeWithAttributes.coreValue());
        assertEquals("Test String", leafNodeWithAttributes.attr("testKey"));
    }

    @org.junit.Test
    public void testAttr() {
        LeafNode leafNode = new TextNode("Test String");
        assertEquals("Test String", leafNode.attr(nodeName()));

        LeafNode leafNodeWithAttributes = new Attributes();
        leafNodeWithAttributes.put("testKey1", "testValue1");
        leafNodeWithAttributes.put("testKey2", "testValue2");
        assertEquals("testValue1", leafNodeWithAttributes.attr("testKey1"));
        assertEquals("", leafNodeWithAttributes.attr("nonExistentKey"));

        LeafNode leafNodeWithCustomName = new Attributes();
        leafNodeWithCustomName.put("customName", "Test String");
        assertEquals("Test String", leafNodeWithCustomName.attr("customName"));
        assertEquals("", leafNodeWithCustomName.attr("nodeName"));
    }

    @org.junit.Test
    public void testAttrSetter() {
        LeafNode leafNode = new TextNode("Test String");
        leafNode.attr(nodeName(), "New Test String");
        assertEquals("New Test String", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new Attributes();
        leafNodeWithAttributes.put("testKey1", "testValue1");
        leafNodeWithAttributes.put("testKey2", "testValue2");
        leafNodeWithAttributes.attr(nodeName(), "New Test String");
        assertEquals("New Test String", leafNodeWithAttributes.coreValue());
        assertEquals("New Test String", leafNodeWithAttributes.attr(nodeName()));
        assertEquals("testValue1", leafNodeWithAttributes.attr("testKey1"));
        assertEquals("testValue2", leafNodeWithAttributes.attr("testKey2"));
    }

    @org.junit.Test
    public void testHasAttr() {
        LeafNode leafNode = new TextNode("Test String");
        assertFalse(leafNode.hasAttr(nodeName()));
        assertFalse(leafNode.hasAttr("nonExistentKey"));

        LeafNode leafNodeWithAttributes = new Attributes();
        leafNodeWithAttributes.put("testKey1", "testValue1");
        leafNodeWithAttributes.put("testKey2", "testValue2");
        assertTrue(leafNodeWithAttributes.hasAttr(nodeName()));
        assertTrue(leafNodeWithAttributes.hasAttr("testKey1"));
        assertTrue(leafNodeWithAttributes.hasAttr("testKey2"));
        assertFalse(leafNodeWithAttributes.hasAttr("nonExistentKey"));
    }

    @org.junit.Test
    public void testRemoveAttr() {
        LeafNode leafNodeWithAttributes = new Attributes();
        leafNodeWithAttributes.put("testKey1", "testValue1");
        leafNodeWithAttributes.put("testKey2", "testValue2");

        leafNodeWithAttributes.removeAttr(nodeName());
        assertTrue(leafNodeWithAttributes.hasAttributes());
        assertFalse(leafNodeWithAttributes.hasAttr(nodeName()));
        assertFalse(leafNodeWithAttributes.hasAttr("testKey1"));
        assertFalse(leafNodeWithAttributes.hasAttr("testKey2"));

        leafNodeWithAttributes.removeAttr("testKey1");
        assertTrue(leafNodeWithAttributes.hasAttributes());
        assertFalse(leafNodeWithAttributes.hasAttr(nodeName()));
        assertFalse(leafNodeWithAttributes.hasAttr("testKey1"));
        assertTrue(leafNodeWithAttributes.hasAttr("testKey2"));
    }

    @org.junit.Test
    public void testAbsUrl() {
        LeafNode leafNode = new TextNode("Test String");
        assertEquals("", leafNode.absUrl(nodeName()));

        LeafNode leafNodeWithAttributes = new Attributes();
        leafNodeWithAttributes.put("abs:url", "http://test.com/test");
        assertEquals("http://test.com/test", leafNodeWithAttributes.absUrl(nodeName()));
    }

    @org.junit.Test
    public void testBaseUri() {
        TextNode leafNode = new TextNode("Test String");
        Node parentNode = new Elements.Element("ParentNode");
        parentNode.appendText("Parent Node");
        parentNode.coreValue("Base Uri: http://test.com");
        leafNode.parent(parentNode);
        assertEquals("http://test.com", leafNode.baseUri());

        Elements.Element parentNodeWithNoBaseUri = new Elements.Element("ParentNode");
        parentNodeWithNoBaseUri.appendText("Parent Node");
        leafNode.parent(parentNodeWithNoBaseUri);
        assertEquals("", leafNode.baseUri());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        TextNode leafNode = new TextNode("Test String");
        leafNode.childNodeSize();
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        TextNode leafNode = new TextNode("Test String");
        leafNode.ensureChildNodes();
    }
}