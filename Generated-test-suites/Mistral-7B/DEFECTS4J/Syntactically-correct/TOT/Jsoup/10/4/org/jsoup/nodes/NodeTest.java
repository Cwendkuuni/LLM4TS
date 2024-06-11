package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.jsoup.nodes.attributes.NamedAttribute;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Jsoup.*;

public class NodeTest {

    private Node root;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><div id='test'><h1>Hello</h1><p id='p'>World</p></div></body></html>";
        root = parse(html);
    }

    @Test
    public void testNodeName() {
        assertEquals("html", root.nodeName());
        assertEquals("head", root.childNodes().get(0).nodeName());
        assertEquals("title", root.childNodes().get(0).childNodes().get(0).nodeName());
        assertEquals("div", root.childNodes().get(1).nodeName());
        assertEquals("h1", root.childNodes().get(1).childNodes().get(0).nodeName());
        assertEquals("p", root.childNodes().get(1).childNodes().get(1).nodeName());
    }

    @Test
    public void testAttr() {
        Element div = (Element) root.childNodes().get(1);
        Element h1 = (Element) div.childNodes().get(0);

        assertEquals("Test", h1.attr("title"));
        assertEquals("", h1.attr("notexist"));

        Element p = (Element) div.childNodes().get(1);

        assertEquals("World", p.attr("text")); // no attribute in HTML
        assertEquals("", p.attr("abs:notexist")); // no attribute in HTML

        Attributes attrs = new AttributesImpl();
        attrs.put("class", "test");
        attrs.put("style", "color:red;");

        Element newP = p.clone();
        newP.attributes = attrs;

        assertEquals("test", newP.attr("class"));
        assertEquals("red", newP.attr("style"));

        // test with relative URL
        h1.absUrl("href", "test.html");
        String href = h1.attr("abs:href");
        assertEquals("test.html", href);

        // test with absolute URL
        URL base = new URL("http://example.com");
        h1.setBaseUri(base.toString());
        href = h1.attr("abs:href", "test.html");
        assertEquals("http://example.com/test.html", href);
    }

    @Test
    public void testAttributes() {
        Element div = (Element) root.childNodes().get(1);
        Attributes attributes = div.attributes();

        List<NamedAttribute> attributesList = new ArrayList<>(attributes.size());
        for (String name : attributes.keySet()) {
            attributesList.add(new NamedAttribute(name, attributes.get(name)));
        }

        List<NamedAttribute> expectedList = Arrays.asList(
                new NamedAttribute("id", "test"),
                new NamedAttribute("class", ""),
                new NamedAttribute("style", ""),
                new NamedAttribute("title", "Test")
        );

        assertEquals(expectedList, attributesList);
    }

    @Test
    public void testHasAttr() {
        Element div = (Element) root.childNodes().get(1);
        assertTrue(div.hasAttr("id"));
        assertTrue(div.hasAttr("class"));
        assertTrue(div.hasAttr("style"));
        assertTrue(div.hasAttr("title"));
        assertFalse(div.hasAttr("notexist"));
    }

    @Test
    public void testRemoveAttr() {
        Element div = (Element) root.childNodes().get(1);
        div.attr("class", "test");

        assertEquals("test", div.attr("class"));
        div.removeAttr("class");
        assertEquals("", div.attr("class"));
    }

    @Test
    public void testChildNode() {
        Node firstChild = root.childNodes().get(0);
        assertEquals("head", firstChild.nodeName());
        assertEquals(0, firstChild.siblingIndex());

        Node h1 = root.childNodes().get(1).childNodes().get(0);
        assertEquals("Hello", h1.outerHtml());
        assertEquals(1, h1.siblingIndex());

        Node p = root.childNodes().get(1).childNodes().get(1);
        assertEquals("World", p.outerHtml());
        assertEquals(2, p.siblingIndex());
    }

    @Test
    public void testChildNodes() {
        List<Node> expectedChildren = new ArrayList<>();
        expectedChildren.add(parse("<head><title>Test</title></head>").body());
        expectedChildren.add(parse("<div id='test'><h1>Hello</h1><p id='p'>World</p></div>"));

        List<Node> actualChildren = root.childNodes();
        assertEquals(expectedChildren, actualChildren);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testChildNodeOutOfBounds() {
        root.childNodes().get(100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetParentNodeNull() {
        Node node = parse("<div></div>");
        node.setParentNode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildNull() {
        Node parent = parse("<div></div>");
        parent.addChild(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceChildNull() {
        Node parent = parse("<div><p>Something</p></div>");
        parent.replaceChild(null, parent.childNodes().get(0));
    }

    @Test
    public void testAddChildren() {
        Node parent = parse("<div></div>");
        Node child1 = parse("<p>Child 1</p>");
        Node child2 = parse("<p>Child 2</p>");

        parent.addChildren(child1, child2);

        List<Node> expectedChildren = new ArrayList<>();
        expectedChildren.add(child1);
        expectedChildren.add(child2);

        List<Node> actualChildren = parent.childNodes();
        assertEquals(expectedChildren, actualChildren);
    }

    @Test
    public void testRemoveChild() {
        Node parent = parse("<div><p>Child 1</p><p>Child 2</p></div>");
        Node childToRemove = parent.childNodes().get(0);

        parent.removeChild(childToRemove);

        List<Node> expectedChildren = new ArrayList<>();
        expectedChildren.add(parent.childNodes().get(1));

        List<Node> actualChildren = parent.childNodes();
        assertEquals(expectedChildren, actualChildren);
    }

    @Test
    public void testSetBaseUri() {
        Element h1 = parse("<h1>Hello</h1>").attr("id", "test");
        h1.setBaseUri("http://example.com");

        assertEquals("http://example.com/test", h1.absUrl("href"));
        assertEquals("http://example.com/test.html", h1.absUrl("abs:href"));
    }

    @Test(expected = MalformedURLException.class)
    public void testSetBaseUriMalformedUrl() {
        Element h1 = parse("<h1>Hello</h1>").attr("id", "test");
        h1.setBaseUri("foo:bar");
    }

    @Test
    public void testAbsUrl() {
        Element h1 = parse("<h1>Hello</h1>").attr("href", "test.html");

        assertEquals("test.html", h1.attr("href"));
        assertEquals("http://example.com/test.html", h1.absUrl("href"));

        h1.setBaseUri("http://example.com");

        assertEquals("test.html", h1.attr("href"));
        assertEquals("http://example.com/test.html", h1.absUrl("href"));

        assertEquals("", h1.attr("abs:href"));
        assertEquals("http://example.com/test.html", h1.absUrl("abs:href"));
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlMalformedUrl() {
        Element h1 = parse("<h1>Hello</h1>").attr("abs:href", "foo:bar");
        h1.absUrl("abs:href");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbsUrlNoAttribute() {
        Element h1 = parse("<h1>Hello</h1>").attr("id", "test");
        h1.absUrl("nonexistent");
    }
}