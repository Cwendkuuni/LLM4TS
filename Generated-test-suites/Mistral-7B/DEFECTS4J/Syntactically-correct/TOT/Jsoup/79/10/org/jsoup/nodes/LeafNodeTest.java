
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode node = new Elements.SingleTagElement("div");
        assertFalse(node.hasAttributes());

        Attributes attributes = new Attributes();
        attributes.put("class", "someclass");
        node.attributes().putAll(attributes);
        assertTrue(node.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode node = new Element("p");
        node.coreValue("Hello World");
        assertEquals("Hello World", node.coreValue());

        LeafNode element = new Element("p");
        element.value = "Test Value";
        assertEquals("Test Value", element.coreValue());

        LeafNode attrNode = new Attributes();
        attrNode.put("nodeName", "input");
        attrNode.put("value", "Test Attribute Value");
        assertEquals("Test Attribute Value", attrNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode node = new Element("p");
        node.coreValue("Hello World");

        node.attr("class", "someclass");
        assertEquals("someclass", node.attr("class"));

        node.attr("title", "Test Title");
        assertEquals("Test Title", node.attr("title"));

        LeafNode attrNode = new Attributes();
        attrNode.put("nodeName", "input");
        attrNode.put("value", "Test Attribute Value");
        attrNode.attr("type", "text");
        node.attributes().putAll(attrNode);
        assertEquals("text", node.attr("type"));
    }

    @Test
    public void testAttributeMap() {
        LeafNode node = new Elements.SingleTagElement("img");
        node.attributes().put("src", "test.jpg");
        node.attributes().put("alt", "Test Image");

        Attributes attributes = node.attributes();
        assertEquals("test.jpg", attributes.get("src"));
        assertEquals("Test Image", attributes.get("alt"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodes() {
        LeafNode node = new Elements.SingleTagElement("p");
        node.childNodes(); // Throws UnsupportedOperationException
    }

    @Test
    public void testHasParent() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.add(child);
        assertTrue(child.hasParent());
    }

    @Test
    public void testBaseUri() {
        Element parent = new Element("html");
        Element child = new Element("head");
        parent.add(child);
        child.coreValue("Test Child Base URI");

        assertEquals("", child.baseUri());
        parent.baseUri("http://test.com");
        assertEquals("http://test.com", child.baseUri());
    }
}