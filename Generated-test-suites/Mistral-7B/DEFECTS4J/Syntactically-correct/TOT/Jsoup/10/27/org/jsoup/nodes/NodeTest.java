package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    private Document doc;

    @Before
    public void setUp() {
        doc = new Document();
    }

    @Test
    public void testNewNode() {
        Node node = new Element("div", doc);
        assertSame(doc, node.ownerDocument());
        assertNull(node.parentNode());
        assertEquals("div", node.nodeName());
        assertFalse(node.hasAttr("id"));
        assertNull(node.attr("id"));
        assertNull(node.baseUri());
        assertEquals(0, node.siblingIndex());
        assertNull(node.childNodes().get(0));
        assertEquals("", node.toString());
    }

    @Test
    public void testNewNodeWithAttributes() {
        Attributes attrs = new Attributes();
        attrs.put("id", "test");
        attrs.put("class", "testClass");
        Node node = new Element("div", doc, attrs);
        assertSame(doc, node.ownerDocument());
        assertNull(node.parentNode());
        assertEquals("div", node.nodeName());
        assertTrue(node.hasAttr("id"));
        assertEquals("test", node.attr("id"));
        assertEquals("class", node.attr("class"));
        assertEquals("test class", node.toString());
    }

    @Test
    public void testNewNodeWithBaseUri() {
        String baseUri = "https://example.com/";
        Node node = new Element("div", doc, new Attributes(), baseUri);
        assertSame(doc, node.ownerDocument());
        assertNull(node.parentNode());
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertFalse(node.hasAttr("id"));
        assertNull(node.attr("id"));
        assertNull(node.childNodes().get(0));
        assertEquals("", node.toString());
    }

    @Test
    public void testNewEmptyNode() {
        Node node = new Node("");
        assertSame(doc, node.ownerDocument());
        assertNull(node.parentNode());
        assertEquals("", node.nodeName());
        assertFalse(node.hasAttr("id"));
        assertNull(node.attr("id"));
        assertNull(node.baseUri());
        assertEquals(0, node.siblingIndex());
        assertTrue(node instanceof TextNode);
        assertEquals("", node.toString());
    }

    @Test
    public void testGetAttribute() {
        Element elem = new Element("div", doc);
        elem.attr("id", "test");
        elem.attr("class", "testClass");
        assertEquals("test", elem.attr("id"));
        assertEquals("testClass", elem.attr("class"));
        assertEquals("", elem.attr("nonExistentAttr"));
        assertFalse(elem.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testGetAttributeWithPrefix() {
        Element elem = new Element("a", doc);
        elem.attr("href", "/test.html");
        assertEquals("https://example.com/test.html", elem.absUrl("href"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeWithNullAttributeKey() {
        Element elem = new Element("div", doc);
        elem.attr("id", "test");
        elem.attr("class", "testClass");
        elem.attr("href", "/test.html");
        elem.attr("abs:href", "https://example.com");
        elem.attr(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeWithEmptyAttributeKey() {
        Element elem = new Element("div", doc);
        elem.attr("id", "test");
        elem.attr("class", "testClass");
        elem.attr("href", "/test.html");
        elem.attr("abs:href", "https://example.com");
        elem.attr("", "test");
    }

    @Test
    public void testSetAttribute() {
        Element elem = new Element("div", doc);
        elem.attr("id", "test1");
        elem.attr("class", "testClass1");
        elem.attr("id", "test2");
        assertEquals("test2", elem.attr("id"));
        assertEquals("testClass1 test2", elem.toString());
    }

    @Test
    public void testSetAttributeWithPrefix() {
        Element elem = new Element("a", doc);
        elem.attr("href", "/test.html");
        elem.attr("abs:href", "https://example.com/test.html");
        assertEquals("https://example.com/test.html", elem.absUrl("abs:href"));
    }

    @Test
    public void testHasAttribute() {
        Element elem = new Element("div", doc);
        elem.attr("id", "test");
        elem.attr("class", "testClass");
        assertTrue(elem.hasAttr("id"));
        assertTrue(elem.hasAttr("class"));
        assertFalse(elem.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttribute() {
        Element elem = new Element("div", doc);
        elem.attr("id", "test1");
        elem.attr("class", "testClass1");
        elem.removeAttr("id");
        assertFalse(elem.hasAttr("id"));
        assertFalse(elem.hasAttr("class"));
        assertEquals("testClass1", elem.toString());
    }

    @Test
    public void testGetBaseUri() {
        Element elem = new Element("div", doc, new Attributes(), "http://example.com");
        assertEquals("http://example.com", elem.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        Element elem = new Element("div", doc);
        elem.setBaseUri("http://example.com");
        assertEquals("http://example.com", elem.baseUri());
    }

    @Test
    public void testClone() {
        Element parent = new Element("div", doc);
        Element child = new Element("p", doc);
        parent.appendChild(child);
        Element clonedParent = parent.clone();
        Element clonedChild = clonedParent.child(0);
        assertEquals(parent, clonedParent);
        assertEquals(child, clonedChild);
        assertFalse(parent.equals(clonedParent));
        assertFalse(child.equals(clonedChild));

        parent.removeChild(0);
        clonedParent.removeChild(0);
        assertEquals(0, parent.childNodes().size());
        assertEquals(0, clonedParent.childNodes().size());
    }

    @Test
    public void testTraversingChildren() {
        Element parent = new Element("div", doc);
        Element child1 = new Element("p", doc);
        Element child2 = new Element("p", doc);
        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> children = new ArrayList<>();
        new NodeTraversor(new ElementVisitor() {
            @Override
            public void head(Node node, int depth) {
                children.add(node);
            }

            @Override
            public Element visit(Element element) {
                return null;
            }
        }).traverse(parent);

        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testGetTextContent() {
        Element parent = new Element("div", doc);
        Element child = new TextNode("Hello World!");
        parent.appendChild(child);
        assertEquals("Hello World!", parent.text());
    }

    @Test
    public void testGetTextContentEmpty() {
        Element parent = new Element("div", doc);
        assertEquals("", parent.text());
    }

    @Test
    public void testGetTextContentWithChildren() {
        Element parent = new Element("div", doc);
        Element child1 = new Element("p", doc);
        child1.text("Hello ");
        Element child2 = new TextNode("World!");
        parent.appendChild(child1);
        parent.appendChild(child2);
        assertEquals("Hello World!", parent.text());
    }

    @Test
    public void testGetTextContentWithChildrenAndTextNodeFirst() {
        Element parent = new Element("div", doc);
        TextNode textNode = new TextNode("Hello ");
        parent.appendChild(textNode);
        Element child = new Element("p", doc);
        child.text("World!");
        parent.appendChild(child);
        assertEquals("Hello World!", parent.text());
    }

    @Test
    public void testGetTextContentWithChildrenAndTextNodeLast() {
        Element parent = new Element("div", doc);
        Element child = new Element("p", doc);
        child.text("Hello ");
        TextNode textNode = new TextNode("World!");
        parent.appendChild(child);
        parent.appendChild(textNode);
        assertEquals("Hello World!", parent.text());
    }

    @Test
    public void testGetTextContentWithEmptyChild() {
        Element parent = new Element("div", doc);
        Element emptyChild = new Element("p", doc);
        parent.appendChild(emptyChild);
        assertEquals("", parent.text());
    }

    @Test
    public void testGetTextContentWithMultipleEmptyChildren() {
        Element parent = new Element("div", doc);
        Element emptyChild1 = new Element("p", doc);
        Element emptyChild2 = new Element("p", doc);
        parent.appendChild(emptyChild1);
        parent.appendChild(emptyChild2);
        assertEquals("", parent.text());
    }

    @Test
    public void testGetTextContentWithMixedTextAndElements() {
        Element parent = new Element("div", doc);
        Element child1 = new Element("p", doc);
        child1.text("Hello ");
        TextNode textNode1 = new TextNode("World!");
        parent.appendChild(child1);
        parent.appendChild(textNode1);
        Element child2 = new Element("p", doc);
        child2.text("Java");
        parent.appendChild(child2);
        assertEquals("Hello World! Java", parent.text());
    }

    @Test
    public void testGetTextContentWithMixedTextAndElementsAndEmptyChildren() {
        Element parent = new Element("div", doc);
        Element child1 = new Element("p", doc);
        child1.text("Hello ");
        TextNode textNode1 = new TextNode("World!");
        parent.appendChild(child1);
        parent.appendChild(textNode1);
        Element emptyChild1 = new Element("p", doc);
        parent.appendChild(emptyChild1);
        Element child2 = new Element("p", doc);
        child2.text("Java");
        parent.appendChild(child2);
        assertEquals("Hello World!", parent.text());
    }

    @Test
    public void testGetTextContentWithMixedTextAndElementsAndTextNodesFirstLast() {
        Element parent = new Element("div", doc);
        TextNode textNode1 = new TextNode("Hello ");
        parent.appendChild(textNode1);
        Element child1 = new Element("p", doc);
        child1.text("World!");
        parent.appendChild(child1);
        TextNode textNode2 = new TextNode(" Java");
        parent.appendChild(textNode2);
        assertEquals("Hello World! Java", parent.text());
    }

    @Test
    public void testGetTextContentWithMixedTextAndElementsAndTextNodesMiddle() {
        Element parent = new Element("div", doc);
        TextNode textNode1 = new TextNode("Hello ");
        parent.appendChild(textNode1);
        Element child1 = new Element("p", doc);
        child1.text("World!");
        TextNode textNode2 = new TextNode(" Java");
        parent.appendChild(child1);
        parent.appendChild(textNode2);
        assertEquals("Hello World! Java", parent.text());
    }

    @Test
    public void testGetTextContentWithMixedTextAndElementsAndTextNodesMiddleEmpty() {
        Element parent = new Element("div", doc);
        TextNode textNode1 = new TextNode("Hello ");
        parent.appendChild(textNode1);
        Element emptyChild = new Element("p", doc);
        parent.appendChild(emptyChild);
        Element child1 = new Element("p", doc);
        child1.text("World!");
        TextNode textNode2 = new TextNode(" Java");
        parent.appendChild(child1);
        parent.appendChild(textNode2);
        assertEquals("Hello World!", parent.text());
    }

    @Test
    public void testGetTextContentWithMixedTextAndElementsAndTextNodesMiddleEmptyLast() {
        Element parent = new Element("div", doc);
        TextNode textNode1 = new TextNode("Hello ");
        parent.appendChild(textNode1);
        Element emptyChild = new Element("p", doc);
        parent.appendChild(emptyChild);
        TextNode textNode2 = new TextNode(" Java");
        parent.appendChild(textNode2);
        Element child1 = new Element("p", doc);
        child1.text("World!");
        parent.appendChild(child1);
        assertEquals("Hello World!", parent.text());
    }

    @Test
    public void testGetTextContentWithMixedTextAndElementsAndTextNodesMiddleEmptyLastWithTextNodeBefore() {
        Element parent = new Element("div", doc);
        TextNode textNode1 = new TextNode("Hello ");
        parent.appendChild(textNode1);
        TextNode textNode2 = new TextNode(" Java");
        parent.appendChild(textNode2);
        Element emptyChild = new Element("p", doc);
        parent.appendChild(emptyChild);
        Element child1 = new Element("p", doc);
        child1.text("World!");
        parent.appendChild(child1);
        assertEquals("Hello Java World!", parent.text());
    }

    @Test
    public void testGetTextContentWithMixedTextAndElementsAndTextNodesMiddleEmptyLastWithTextNodeAfter() {
        Element parent = new Element("div", doc);
        TextNode textNode1 = new TextNode("Hello ");
        parent.appendChild(textNode1);
        Element child1 = new Element("p", doc);
        child1.text("World!");
        parent.appendChild(child1);
        TextNode textNode2 = new TextNode(" Java");
        parent.appendChild(textNode2);
        Element emptyChild = new Element("p", doc);
        parent.appendChild(emptyChild);
        assertEquals("Hello World! Java", parent.text());
    }

    @Test
    public void testGetTextContentWithMixedTextAndElementsAndTextNodesMiddleEmptyLastWithTextNodeBeforeAndAfter() {
        Element parent = new Element("div", doc);
        TextNode textNode1 = new TextNode("Hello ");
        parent.appendChild(textNode1);
        TextNode textNode2 = new TextNode(" Java");
        parent.appendChild(textNode2);
        Element emptyChild1 = new Element("p", doc);
        parent.appendChild(emptyChild1);
        Element child1 = new Element("p", doc);
        child1.text("World!");
        parent.appendChild(child1);
        TextNode textNode3 = new TextNode(" Text");
        parent.appendChild(textNode3);
        assertEquals("Hello Java Text World!", parent.text());
    }
}