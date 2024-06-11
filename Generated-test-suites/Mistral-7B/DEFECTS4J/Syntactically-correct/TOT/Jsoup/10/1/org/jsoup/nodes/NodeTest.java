package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import static org.junit.Assert.*;

public class NodeTest {

    private static final String HTML_TEMPLATE = "<html><head></head><body><%s></body></html>";

    private Document doc;

    @Before
    public void setUp() throws Exception {
        doc = Parser.parseHtml(String.format(HTML_TEMPATE, ""));
    }

    @Test
    public void testCreateElementWithBaseUriAndAttributes() {
        String baseUri = "https://example.com";
        String attributeKey = "class";
        String attributeValue = "someClass";

        Node node = new Element("div", baseUri, new Attributes().put(attributeKey, attributeValue));

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributeValue, node.attr(attributeKey));
        assertEquals(Collections.singletonMap(attributeKey, attributeValue), node.attributes());
    }

    @Test
    public void testCreateElementWithBaseUriOnly() {
        String baseUri = "https://example.com";

        Node node = new Element("div", baseUri);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testCreateElementWithoutBaseUriOrAttributes() {
        Node node = new Element("div");

        assertEquals("div", node.nodeName());
        assertNull(node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testGetAttribute() {
        String attributeKey = "class";
        String attributeValue = "someClass";

        Node node = new Element("div").attr(attributeKey, attributeValue);

        assertEquals(attributeValue, node.attr(attributeKey));
    }

    @Test
    public void testGetAttributeAbsUrl() {
        String baseUri = "https://example.com";
        String attributeKey = "href";
        String attributeValue = "/path/to/somewhere";

        Node node = new Element("a", baseUri).attr(attributeKey, attributeValue);

        assertEquals("https://example.com/path/to/somewhere", node.absUrl(attributeKey));
    }

    @Test
    public void testGetAttributeMissing() {
        Node node = new Element("div");

        assertEquals("", node.attr("missingAttribute"));
    }

    @Test
    public void testGetAttributeWithPrefix() {
        String baseUri = "https://example.com";
        String attributeKey = "abs:href";

        Node node = new Element("a", baseUri);

        assertEquals("", node.attr(attributeKey));

        node.attr("href", "/path/to/somewhere");

        assertEquals("https://example.com/path/to/somewhere", node.absUrl(attributeKey));
    }

    @Test
    public void testGetChildren() {
        Node parent = doc.body();

        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> children = parent.childNodes();

        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testGetParent() {
        Element parent = doc.body();
        Element child = new Element("p").text("Child");

        parent.appendChild(child);

        assertEquals(parent, child.parent());
        assertNull(child.parent());
    }

    @Test
    public void testRemoveAttribute() {
        String attributeKey = "class";
        String attributeValue = "someClass";

        Node node = new Element("div").attr(attributeKey, attributeValue);

        node.removeAttr(attributeKey);

        assertFalse(node.hasAttr(attributeKey));
    }

    @Test
    public void testSetAttribute() {
        String attributeKey = "class";
        String attributeValue = "someClass";

        Node node = new Element("div");

        node.attr(attributeKey, attributeValue);

        assertTrue(node.hasAttr(attributeKey));
        assertEquals(attributeValue, node.attr(attributeKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAttributeNullBaseUri() {
        String attributeKey = "class";
        String attributeValue = "someClass";

        new Element("div", null).attr(attributeKey, attributeValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAttributeNullAttributes() {
        new Element("div", "").attr(null);
    }

    @Test
    public void testSetBaseUri() {
        String newBaseUri = "https://another-example.com";

        Node node = new Element("div").baseUri("https://example.com");

        node.setBaseUri(newBaseUri);

        assertEquals(newBaseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUriNull() {
        Node node = new Element("div").baseUri("https://example.com");

        node.setBaseUri(null);

        assertNull(node.baseUri());
    }

    @Test
    public void testGetAttributeWithEmptyBaseUri() {
        Node node = new Element("a").baseUri("");

        assertEquals("", node.absUrl("href"));
    }

    @Test
    public void testGetChildNodeByIndex() {
        Element parent = doc.body();

        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        parent.appendChild(child1);
        parent.appendChild(child2);

        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetChildNodeByInvalidIndex() {
        Element parent = doc.body();

        Element child1 = new Element("p").text("Child 1");
        parent.appendChild(child1);

        parent.childNode(1);
    }

    @Test
    public void testGetUnmodifiableListOfChildren() {
        Element parent = doc.body();

        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> children = parent.childNodes();

        children.get(0).setTextContent("Updated");

        assertEquals("Child 1", child1.text());
        assertEquals(2, children.size());
    }

    @Test
    public void testGetChildrenAsArray() {
        Element parent = doc.body();

        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        parent.appendChild(child1);
        parent.appendChild(child2);

        Node[] children = parent.childNodesAsArray();

        assertEquals(child1, children[0]);
        assertEquals(child2, children[1]);
    }

    @Test
    public void testRemoveChild() {
        Element parent = doc.body();

        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        parent.appendChild(child1);
        parent.appendChild(child2);

        parent.removeChild(child1);

        assertEquals(1, parent.childNodes().size());
        assertNull(parent.childNode(0).previousSibling());
    }

    @Test
    public void testReplaceChild() {
        Element parent = doc.body();

        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        parent.appendChild(child1);
        parent.appendChild(child2);

        parent.replaceChild(child1, new TextNode("Updated"));

        assertEquals(1, parent.childNodes().size());
        assertEquals("Updated", parent.childNode(0).text());
    }

    @Test
    public void testAddChildren() {
        Element parent = doc.body();

        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        parent.addChildren(child1, child2);

        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNodes().get(0));
        assertEquals(child2, parent.childNodes().get(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Element parent = doc.body();

        Element child1 = new Element("p").text("Child 1");
        Element child2 = new Element("p").text("Child 2");

        parent.addChildren(1, child1, child2);

        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNodes().get(1));
        assertEquals(child2, parent.childNodes().get(2));
    }
}