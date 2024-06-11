package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
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

    private Node node;
    private Document doc;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        node = new Element("element", baseUri, attributes);
        doc = new Document(baseUri);
        doc.appendElement("body").html("<element key1=\"value1\"><child/></element>");
    }

    @Test
    public void testNodeName() {
        assertEquals("element", node.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("value1", node.attr("key1"));
        assertNull(node.attr("nonExistentAttr"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = node.attributes();
        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testSetAttr() {
        node.attr("key2", "value2");
        assertEquals("value2", node.attr("key2"));
        assertTrue(node.hasAttr("key2"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("key2", "value2");
        node.removeAttr("key2");
        assertFalse(node.hasAttr("key2"));
        assertNull(node.attr("key2"));
    }

    @Test
    public void testAbsUrl() {
        String url = node.absUrl("key1");
        assertEquals("http://example.com/element#key1", url);
        url = node.absUrl("child:href");
        Element child = (Element) node.childNodes().get(0);
        assertEquals("http://example.com/child", url);
        url = node.absUrl("./../child:href");
        Element grandChild = (Element) child.childNodes().get(0);
        assertEquals("http://example.com/child", url);
    }

    @Test
    public void testChildNodes() {
        assertEquals(1, node.childNodes().size());
        Element child = (Element) node.childNodes().get(0);
        assertEquals(0, child.childNodes().size());

        List<Node> children = new ArrayList<>(Arrays.asList(new TextNode("text"), child));
        node.addChildren(children.toArray(new Node[0]));
        assertEquals(2, node.childNodes().size());
    }

    @Test
    public void testParent() {
        Element body = (Element) doc.getElementsByTag("body").get(0);
        assertEquals(body, node.parent());
        assertNull(node.parent());
    }

    @Test
    public void testOwnerDocument() {
        assertEquals(doc, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Element parent = (Element) node.parent();
        parent.removeChild(node);
        assertFalse(node.parentNode() instanceof Node);
    }

    @Test
    public void testReplaceWith() {
        Element parent = (Element) node.parent();
        Element newNode = new Element("newNode");
        parent.replaceChild(node, newNode);
        assertEquals(newNode, node.parentNode());
    }

    @Test
    public void testSetParentNode() {
        Element newParent = new Element("newParent");
        node.setParentNode(newParent);
        assertEquals(newParent, node.parentNode());
    }

    @Test
    public void testReplaceChild() {
        Element newChild = new Element("newChild");
        node.replaceChild(node.childNodes().get(0), newChild);
        assertEquals(newChild, node.childNodes().get(0));
    }

    @Test
    public void testRemoveChild() {
        Element newChild = new Element("newChild");
        node.addChildren(newChild);
        node.removeChild(newChild);
        assertTrue(newChild.parentNode() == null);
    }

    @Test
    public void testAddChildren() {
        Element newChild1 = new Element("newChild1");
        Element newChild2 = new Element("newChild2");
        node.addChildren(newChild1, newChild2);
        assertEquals(3, node.childNodes().size());
    }

    @Test
    public void testAddChildrenWithIndex() {
        Element newChild1 = new Element("newChild1");
        Element newChild2 = new Element("newChild2");
        node.addChildren(1, newChild1, newChild2);
        assertEquals(3, node.childNodes().size());
        assertEquals(newChild1, node.childNodes().get(1));
        assertEquals(newChild2, node.childNodes().get(2));
    }

    @Test
    public void testOuterHtml() {
        StringBuilder accum = new StringBuilder();
        node.outerHtml(accum);
        String expected = "<element key1=\"value1\">text<child></child></element>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testSiblingNodes() {
        Element sibling = new Element("sibling");
        Element parent = (Element) doc.getElementsByTag("body").get(0);
        parent.appendChild(node);
        parent.appendChild(sibling);
        List<Node> siblings = node.siblingNodes();
        assertEquals(1, siblings.size());
        assertEquals(sibling, siblings.get(0));
    }

    @Test
    public void testNextSibling() {
        Element sibling = new Element("sibling");
        Element parent = (Element) doc.getElementsByTag("body").get(0);
        parent.appendChild(node);
        parent.appendChild(sibling);
        assertEquals(sibling, node.nextSibling());
        assertNull(node.nextSibling(1));
    }

    @Test
    public void testPreviousSibling() {
        Element sibling = new Element("sibling");
        Element parent = (Element) doc.getElementsByTag("body").get(0);
        parent.appendChild(node);
        parent.appendChild(sibling);
        assertNull(node.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        Element sibling = new Element("sibling");
        Element parent = (Element) doc.getElementsByTag("body").get(0);
        parent.appendChild(node);
        parent.appendChild(sibling);
        assertEquals(0, sibling.siblingIndex());
        assertEquals(1, node.siblingIndex());
    }
}