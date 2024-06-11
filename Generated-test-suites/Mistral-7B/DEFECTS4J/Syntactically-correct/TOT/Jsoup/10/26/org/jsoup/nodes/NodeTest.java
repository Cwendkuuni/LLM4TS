package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    private Node baseNode;
    private Document doc;

    @Before
    public void setup() {
        baseNode = new Element("base", "http://example.com");
        doc = new Document();
        doc.head().append(baseNode);
    }

    @Test
    public void testNodeName() {
        Node node = new Comment("#comment");
        assertEquals("#comment", node.nodeName());
    }

    @Test
    public void testAttr() {
        Element link = new Element("a", "href", "http://example.com");
        assertEquals("http://example.com", link.attr("href"));
        assertEquals("", link.attr("nonExistentAttr"));
    }

    @Test
    public void testAttributes() {
        Element link = new Element("a", "href", "http://example.com");
        Attributes attrs = link.attributes();
        assertEquals("href", attrs.keySet().iterator().next());
        assertEquals("http://example.com", attrs.get("href"));
    }

    @Test
    public void testHasAttr() {
        Element link = new Element("a", "href", "http://example.com");
        assertTrue(link.hasAttr("href"));
        assertFalse(link.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        Element link = new Element("a", "href", "http://example.com");
        link.removeAttr("href");
        assertFalse(link.hasAttr("href"));
    }

    @Test
    public void testBaseUri() {
        Element link = new Element("a", "href", "", new Attributes().put("abs:href", "http://sub.example.com"));
        assertEquals("http://sub.example.com", link.absUrl("href"));
    }

    @Test
    public void testAbsUrlWithBaseUri() {
        Element link = new Element("a", "href", "path/to/file.html", new Attributes());
        baseNode.attr("abs:href", "http://example.com");
        assertEquals("http://example.com/path/to/file.html", link.absUrl("href"));
    }

    @Test
    public void testAbsUrlWithRelativeUrl() {
        Element link = new Element("a", "href", "path/to/file.html", new Attributes());
        assertEquals("", link.absUrl(""));
        assertEquals("path/to/file.html", link.absUrl("href"));
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlWithInvalidUrl() {
        Element link = new Element("a", "href", "", new Attributes().put("abs:href", "invalid:url"));
        link.absUrl("href");
    }

    @Test
    public void testChildNode() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.addChild(child);
        Node node = parent.childNode(0);
        assertEquals(child, node);
    }

    @Test
    public void testChildren() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");
        parent.addChildren(child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testParent() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.addChild(child);
        assertEquals(parent, child.parent());
        assertEquals(null, child.parent()); // for root element
    }

    @Test
    public void testOwnerDocument() {
        Element child = new Element("p");
        assertEquals(null, child.ownerDocument()); // for standalone node
        Element parent = new Element("div");
        parent.addChild(child);
        assertEquals(parent.ownerDocument(), child.ownerDocument());
    }

    @Test
    public void testRemove() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.addChild(child);
        parent.removeChild(child);
        List<Node> children = parent.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testReplaceWith() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("p");
        parent.addChild(child1);
        parent.replaceChild(child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertEquals(child2, children.get(0));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Element original = new Element("p", "id", "original");
        Element clone = original.clone();
        assertEquals("p", clone.nodeName());
        assertEquals("original", original.attr("id"));
        assertEquals("", clone.attr("id"));
        assertNotSame(original, clone);
        assertTrue(original.hashCode() != clone.hashCode());
    }

    @Test
    public void testOuterHtml() {
        Element p = new Element("p");
        p.outerHtml("Hello <strong>world</strong>!");
        assertEquals("Hello <strong>world</strong>", p.outerHtml());
    }

    @Test
    public void testOuterHtmlWithChildren() {
        Element div = new Element("div");
        Element p1 = new Element("p", "id", "p1");
        Element p2 = new Element("p", "id", "p2");
        div.addChildren(p1, p2);
        div.outerHtml("<div><p id=\"p1\">Text 1</p><p id=\"p2\">Text 2</p></div>");
    }

    @Test
    public void testOuterHtmlWithTextNode() {
        Element p = new Element("p");
        p.addChild(new TextNode("Hello world"));
        p.outerHtml("Hello world");
    }

    @Test
    public void testOuterHtmlWithEmptyTextNode() {
        Element p = new Element("p");
        p.addChild(new TextNode(""));
        p.outerHtml("");
    }

    @Test
    public void testOuterHtmlWithEmptyElement() {
        Element p = new Element("p");
        p.outerHtml("<p></p>");
    }

    @Test
    public void testOuterHtmlWithElementsWithText() {
        Element div = new Element("div");
        Element p1 = new Element("p", "id", "p1").addChild(new TextNode("Text 1"));
        Element p2 = new Element("p", "id", "p2").addChild(new TextNode("Text 2"));
        div.addChildren(p1, p2);
        div.outerHtml("<div><p id=\"p1\">Text 1</p><p id=\"p2\">Text 2</p></div>");
    }

    @Test
    public void testOuterHtmlWithNestedElements() {
        Element div = new Element("div");
        Element p1 = new Element("p").addChild(new TextNode("Outer text"));
        Element p2 = new Element("p").addChild(new TextNode("Inner text"));
        p1.addChild(p2);
        div.addChild(p1);
        div.outerHtml("<div><p>Outer text<p><p>Inner text</p></p></div>");
    }

    @Test
    public void testOuterHtmlWithEmptyDocument() {
        Document doc = new Document();
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testOuterHtmlWithWhitespacePreserved() {
        Element p = new Element("p");
        p.outerHtml("<p> Text with leading and trailing whitespace </p>", new Document.OutputSettings(true));
        assertEquals("\n Text with leading and trailing whitespace \n", p.outerHtml());
    }

    @Test
    public void testOuterHtmlWithIndentation() {
        Element p = new Element("p");
        p.outerHtml("<p>\nText with indentation\n</p>", new Document.OutputSettings(4));
        assertEquals("\n<p>\n Text with indentation\n</p>\n", p.outerHtml());
    }

    @Test
    public void testOuterHtmlWithSelect() {
        Element div = new Element("div");
        Element p1 = new Element("p", "class", "class1").addChild(new TextNode("Text 1"));
        Element p2 = new Element("p", "class", "class2").addChild(new TextNode("Text 2"));
        div.addChildren(p1, p2);
        Elements select = doc.select("div p");
        assertEquals(2, select.size());
        assertEquals(p1, select.get(0));
        assertEquals(p2, select.get(1));
        StringBuilder accum = new StringBuilder();
        new NodeTraversor(new OuterHtmlVisitor(accum, doc.outputSettings())).traverse(select.first());
        assertEquals("<div><p class=\"class1\">Text 1</p><p class=\"class2\">Text 2</p></div>", accum.toString());
    }
}

class OuterHtmlVisitor implements NodeVisitor {
    private StringBuilder accum;
    private Document.OutputSettings out;

    OuterHtmlVisitor(StringBuilder accum, Document.OutputSettings out) {
        this.accum = accum;
        this.out = out;
    }

    public void head(Node node, int depth) {
        node.outerHtmlHead(accum, depth, out);
    }

    public void tail(Node node, int depth) {
        node.outerHtmlTail(accum, depth, out);
    }
}