package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Elements;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import static org.junit.Assert.*;

public class NodeTest {

    private Document doc;

    @Before
    public void setup() {
        doc = new Parser().parseFromString("<html><body><div id='test'><p>Test<a href='test.html'>Link</a></p></div></body></html>");
    }

    @Test
    public void testNodeName() {
        Node node = doc.getElementById("test");
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        Element link = (Element) doc.selectFirst("a[href]");
        assertEquals("test.html", link.attr("abs:href"));
        assertEmpty(link.attr("nonExistentAttr"));
    }

    @Test
    public void testAttributes() {
        Element p = (Element) doc.selectFirst("p");
        Attributes attrs = p.attributes();
        assertNotNull(attrs);
        assertTrue(attrs.hasKey("class"));
        assertEquals("", attrs.get("class"));
    }

    @Test
    public void testHasAttr() {
        Element p = (Element) doc.selectFirst("p");
        assertTrue(p.hasAttr("class"));
        assertFalse(p.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        Element p = (Element) doc.selectFirst("p");
        p.removeAttr("class");
        assertFalse(p.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        Element link = (Element) doc.selectFirst("a[href]");
        assertEquals("http://localhost:8080/test.html", link.absUrl("href"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbsUrlInvalidAttribute() {
        Element p = (Element) doc.selectFirst("p");
        p.absUrl("nonExistentAttr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbsUrlNullBaseUri() {
        Element link = new Element("a");
        link.absUrl("href");
    }

    @Test
    public void testChildNode() {
        Node p = doc.selectFirst("p");
        Element child = p.childNode(0).getElementSibling(1);
        assertEquals("Link", child.text());
    }

    @Test
    public void testChildNodes() {
        Element div = doc.getElementById("test");
        List<Node> children = div.childNodes();
        assertEquals(3, children.size());
        assertEquals("Test", children.get(0).text());
        assertEquals("Link", children.get(1).text());
        assertNull(children.get(2));
    }

    @Test
    public void testParent() {
        Node link = doc.selectFirst("a");
        assertEquals(link.parent(), doc.body());
    }

    @Test
    public void testOwnerDocument() {
        Node textNode = doc.selectFirst("#test > text()");
        assertEquals(textNode.ownerDocument(), doc);
    }

    @Test
    public void testRemove() {
        Element p = (Element) doc.selectFirst("p");
        Node child = p.childNode(0);
        p.remove();
        assertEquals(0, p.childNodes().size());
        assertNull(child.parentNode());
    }

    @Test
    public void testReplaceWith() {
        Element p = (Element) doc.selectFirst("p");
        Node textNode = doc.createTextNode("New text");
        p.replaceWith(textNode);
        assertEquals("New text", p.text());
    }

    @Test
    public void testSetParentNode() {
        Element div = doc.createElement("div");
        Node p = doc.selectFirst("p");
        p.setParentNode(div);
        assertEquals(div, p.parentNode());
    }

    @Test
    public void testReindexChildren() {
        Element div = doc.createElement("div");
        Element p1 = doc.createElement("p");
        Element p2 = doc.createElement("p");
        Node p3 = doc.selectFirst("p");
        div.appendChild(p1);
        div.appendChild(p2);
        div.appendChild(p3);
        div.removeChild(p3);
        assertEquals(2, div.childNodes().size());
        assertEquals(p1, div.childNodes().get(0));
        assertEquals(p2, div.childNodes().get(1));
    }

    @Test
    public void testAddChildren() {
        Element div = doc.createElement("div");
        Element p1 = doc.createElement("p");
        Element p2 = doc.createElement("p");
        div.addChildren(p1, p2);
        assertEquals(2, div.childNodes().size());
        assertEquals(p1, div.childNodes().get(0));
        assertEquals(p2, div.childNodes().get(1));
    }

    @Test
    public void testAddChildrenAtIndex() {
        Element div = doc.createElement("div");
        Element p1 = doc.createElement("p");
        Element p2 = doc.createElement("p");
        div.addChildren(1, p1, p2);
        assertEquals(2, div.childNodes().size());
        assertEquals(p1, div.childNodes().get(0));
        assertEquals(p2, div.childNodes().get(1));
    }
}