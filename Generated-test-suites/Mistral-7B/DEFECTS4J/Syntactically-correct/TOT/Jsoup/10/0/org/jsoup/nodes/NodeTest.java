package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

public class NodeTest {

    private Node node;
    private Document doc;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        doc = new Document(baseUri, attributes);
        node = new Element("div", baseUri, attributes);
        doc.appendChild(node);
    }

    @Test
    public void testNodeName() {
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        node.attr("class", "test-class");
        assertEquals("test-class", node.attr("class"));
        assertEquals("", node.attr("non-existent-attr"));
    }

    @Test
    public void testAttributes() {
        assertEquals("class=test-class", node.attributes().toString());
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("non-existent-attr"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("class", "test-class");
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("http://example.com", node.baseUri());
        node.setBaseUri("http://newbase.com");
        assertEquals("http://newbase.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        node.attr("href", "relative.html");
        assertEquals("http://newbase.com/relative.html", node.absUrl("href"));
        node.attr("abs:href", "absolute.html");
        assertEquals("http://newbase.com/absolute.html", node.absUrl("abs:href"));
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlWithInvalidBaseUri() {
        node.baseUri = "jsoup://example.com";
        node.absUrl("href");
    }

    @Test
    public void testChildNode() {
        Element child = new Element("p", baseUri, new Attributes());
        node.appendChild(child);
        assertEquals(child, node.childNode(0));
    }

    @Test
    public void testChildNodes() {
        Element child1 = new Element("p", baseUri, new Attributes());
        Element child2 = new Element("p", baseUri, new Attributes());
        node.appendChild(child1);
        node.appendChild(child2);
        List<Node> children = node.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testParent() {
        assertNull(node.parent());
        doc.appendChild(node);
        assertEquals(doc, node.parent());
    }

    @Test
    public void testOwnerDocument() {
        assertEquals(doc, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        doc.appendChild(node);
        doc.removeChild(node);
        assertFalse(doc.containsNode(node));
    }

    @Test
    public void testReplaceWith() {
        Element oldChild = new Element("p", baseUri, new Attributes());
        Element newChild = new Element("p", baseUri, new Attributes());
        doc.appendChild(oldChild);
        doc.appendChild(newChild);
        doc.replaceChild(oldChild, newChild);
        assertEquals(newChild, doc.getElementsByTagName("p").item(0));
    }

    @Test
    public void testClone() {
        Element clonedNode = node.clone();
        assertEquals(node.outerHtml(), clonedNode.outerHtml());
    }
}