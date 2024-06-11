package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = "value";
            }
        };
        assertFalse(leafNode.hasAttributes());

        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
            }
        };
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("attributeKey", "attributeValue");
            }
        };

        Attributes attributes = leafNodeWithAttributes.attributes();
        Assert.assertEquals("attributeValue", attributes.get("attributeKey"));
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = "value";
            }
        };
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testCoreValueSetter() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = new Attributes();
            }
        };
        leafNode.coreValue("newValue");
        assertEquals("newValue", leafNode.coreValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAttr() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = new Attributes();
            }
        };
        leafNode.attr("attributeKey", "attributeValue");
    }

    @Test
    public void testAttrWithNoAttributes() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = "value";
            }
        };
        assertEquals("value", leafNode.attr("nodeName"));
        assertEquals("value", leafNode.attr("nonExistentAttribute"));
    }

    @Test
    public void testAttrWithAttributes() {
        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("attributeKey", "attributeValue");
            }
        };
        assertEquals("attributeValue", leafNodeWithAttributes.attr("attributeKey"));
        assertEquals(EmptyString, leafNodeWithAttributes.attr("nonExistentAttribute"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("attributeKey", "attributeValue");
            }
        };
        assertTrue(leafNodeWithAttributes.hasAttr("attributeKey"));
        assertFalse(leafNodeWithAttributes.hasAttr("nonExistentAttribute"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("attributeKey", "attributeValue");
            }
        };
        leafNodeWithAttributes.removeAttr("attributeKey");
        assertFalse(leafNodeWithAttributes.hasAttr("attributeKey"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNodeWithAttributes = new LeafNode("nodeName") {
            {
                value = new Attributes();
                value.put("abs:url", "http://example.com");
            }
        };
        assertEquals("http://example.com", leafNodeWithAttributes.absUrl("abs:url"));
        assertEquals(EmptyString, leafNodeWithAttributes.absUrl("nonExistentUrl"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNodeWithParent = new LeafNode("nodeName") {
            Node parent = new ElementNode("parentNodeName");
            {
                this.parent = parent;
                value = "value";
            }
        };
        assertEquals("", leafNodeWithParent.baseUri());

        ElementNode parentWithBaseUri = new ElementNode("parentNodeName") {
            {
                setBaseUri("http://example.com");
            }
        };
        LeafNode leafNodeWithParentAndBaseUri = new LeafNode("nodeName") {
            {
                this.parent = parentWithBaseUri;
                value = "value";
            }
        };
        assertEquals("http://example.com", leafNodeWithParentAndBaseUri.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = new Attributes();
            }
        };
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode("nodeName") {
            {
                value = new Attributes();
            }
        };
        leafNode.ensureChildNodes();
    }
}