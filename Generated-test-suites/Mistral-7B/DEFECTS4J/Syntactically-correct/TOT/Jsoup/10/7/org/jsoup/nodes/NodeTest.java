package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    private Node node;
    private Node parentNode;
    private Document document;

    @Before
    public void setUp() throws Exception {
        parentNode = new Element("div");
        document = new Document();
        node = new Element("p");
        node.setBaseUri("http://example.com");
        node.attributes().put("class", "test");
        node.attributes().put("id", "myId");
        node.attributes().put("title", "My Title");
        parentNode.appendChild(node);
        document.appendChild(parentNode);
    }

    @Test
    public void testNodeName() {
        assertEquals("p", node.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("test", node.attr("class"));
        assertEquals("myId", node.attr("id"));
        assertEquals("My Title", node.attr("title"));
        assertFalse(node.hasAttr("notPresent"));
        assertEquals("", node.attr("notPresent"));
        assertFalse(node.hasAttr("abs:notPresent"));
        assertEquals("", node.attr("abs:notPresent"));

        node.attr("class", "newClass");
        assertEquals("newClass", node.attr("class"));

        node.attr("newAttr", "newValue");
        assertEquals("newValue", node.attr("newAttr"));

        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
        assertNull(node.attr("class"));

        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
        assertNull(node.attr("id"));

        node.removeAttr("title");
        assertFalse(node.hasAttr("title"));
        assertNull(node.attr("title"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("class"));
        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("title"));
        assertFalse(node.hasAttr("notPresent"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = node.attributes();
        assertEquals("class", attributes.keyAt(0));
        assertEquals("test", attributes.getValueAt(0));
        assertEquals("id", attributes.keyAt(1));
        assertEquals("myId", attributes.getValueAt(1));
        assertEquals("title", attributes.keyAt(2));
        assertEquals("My Title", attributes.getValueAt(2));
        assertFalse(attributes.hasKey("notPresent"));
        assertNull(attributes.getValue("notPresent"));

        attributes.put("newAttr", "newValue");
        assertEquals("newAttr", attributes.keyAt(3));
        assertEquals("newValue", attributes.getValueAt(3));

        attributes.remove("class");
        assertFalse(attributes.hasKey("class"));
        assertNull(attributes.getValue("class"));

        attributes.remove("id");
        assertFalse(attributes.hasKey("id"));
        assertNull(attributes.getValue("id"));

        attributes.remove("title");
        assertFalse(attributes.hasKey("title"));
        assertNull(attributes.getValue("title"));
    }

    @Test
    public void testAbsUrl() {
        assertEquals("http://example.com/p/class%3Dtest%2Fid%3DmyId%2Ftitle%3DMy%20Title", node.absUrl("href"));
        assertEquals("http://example.com/p/class%3Dtest%2Fid%3DmyId%2Ftitle%3DMy%20Title", node.absUrl("class"));
        assertEquals("http://example.com/p/class%3Dtest%2Fid%3DmyId%2Ftitle%3DMy%20Title", node.absUrl("id"));
        assertEquals("http://example.com/p/class%3Dtest%2Fid%3DmyId%2Ftitle%3DMy%20Title", node.absUrl("title"));
        assertEquals("", node.absUrl("notPresent"));
        assertEquals("", node.absUrl("abs:notPresent"));
    }

    @Test
    public void testChildNodes() {
        Node child = new TextNode("Hello World");
        node.appendChild(child);
        List<Node> childNodes = node.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(child, childNodes.get(0));

        node.removeChild(child);
        assertEquals(0, node.childNodes().size());
    }

    @Test
    public void testClone() {
        Node clonedNode = node.clone();
        assertEquals("p", clonedNode.nodeName());
        assertEquals("test", clonedNode.attr("class"));
        assertEquals("myId", clonedNode.attr("id"));
        assertEquals("My Title", clonedNode.attr("title"));
        assertFalse(clonedNode.hasAttr("notPresent"));
        assertEquals("", clonedNode.attr("notPresent"));
        assertTrue(clonedNode.hasAttr("class"));
        assertFalse(clonedNode.hasChildNodes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new Node("", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParentNodeConstructor() {
        new Node("", null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUriConstructor() {
        new Node(null, new Attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributesConstructor() {
        new Node("", null);
    }

    @Test
    public void testEmptyListConstructor() {
        Node node = new Element("p");
        assertEquals(0, node.childNodes().size());
    }
}