package org.jsoup.nodes;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNewNodeWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "element1").put("class", "class1");
        Node node = new Node(baseUri, attributes);

        assertEquals("element1", node.nodeName());
        assertEquals("class1", node.attr("class"));
        assertEquals("http://example.com", node.baseUri());
        assertEquals(2, node.attributes().size());
        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonexistent"));
        assertEquals("", node.attr("nonexistent"));
    }

    @Test
    public void testNewNodeWithoutBaseUriOrAttributes() {
        Node node = new Node();

        assertNull(node.parentNode);
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
        assertNull(node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node(null, null);
        node.setBaseUri(baseUri);

        assertEquals(baseUri, node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testSetBaseUriWithNullBaseUri() {
        Node node = new Node(null, null);
        node.setBaseUri(null);
    }

    @Test
    public void testGetAttribute() {
        String attributeKey = "class";
        String attributeValue = "class1";
        Node node = new Node(null, new Attributes().put(attributeKey, attributeValue));

        assertEquals(attributeValue, node.attr(attributeKey));
    }

    @Test
    public void testGetAttributeWithMissingKey() {
        Node node = new Node(null, new Attributes());

        assertEquals("", node.attr("missing"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetAttributeWithNullAttributeKey() {
        Node node = new Node(null, new Attributes());
        node.attr("missing", "");
        node.attr("nullKey", null);
        node.attr(null, "");

        node.attr("missing", "");
        node.removeAttr("nullKey");

        node.attr("key", "value");

        try {
            node.attr(null, "");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testHasAttr() {
        Node node = new Node(null, new Attributes().put("class", "class1"));

        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("missing"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new Node(null, new Attributes().put("class", "class1"));

        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes().put("href", "relative.html"));

        assertEquals("http://example.com/relative.html", node.absUrl("href"));
    }

    @Test
    public void testAbsUrlWithAbsoluteBaseUri() {
        String baseUri = "http://example.com/absolute.html";
        Node node = new Node(baseUri, new Attributes().put("href", "relative.html"));

        assertEquals("http://example.com/relative.html", node.absUrl("href"));
    }

    @Test
    public void testAbsUrlWithMissingAttribute() {
        Node node = new Node(null, null);

        assertEquals("", node.absUrl("missing"));
    }

    @Test
    public void testChildNode() {
        Node parentNode = new Element("div");
        String childNodeText = "child";
        Node childNode = new TextNode(childNodeText);

        parentNode.appendChild(childNode);
        Node child = parentNode.childNode(0);

        assertEquals("child", child.toString());
        assertEquals(child, parentNode.childNode(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testChildNodeWithInvalidIndex() {
        Node parentNode = new Element("div");
        parentNode.appendChild(new TextNode("child"));

        parentNode.childNode(1);
    }

    @Test
    public void testChildren() {
        Node parentNode = new Element("div");
        String child1Text = "child1";
        String child2Text = "child2";
        Node child1 = new TextNode(child1Text);
        Node child2 = new TextNode(child2Text);

        parentNode.appendChild(child1);
        parentNode.appendChild(child2);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testGetParent() {
        Node parentNode = new Element("div");
        Node childNode = new Element("p");

        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testGetParentWithNullParent() {
        Node childNode = new Element("p");

        assertNull(childNode.parent());
    }

    @Test
    public void testReplaceChild() {
        Node parentNode = new Element("div");
        String child1Text = "child1";
        String child2Text = "child2";

        Node child1 = new TextNode(child1Text);
        Node child2 = new TextNode(child2Text);

        parentNode.appendChild(child1);
        parentNode.replaceChild(child1, child2);

        assertEquals(child2, parentNode.childNode(0));
    }

    @Test
    public void testReplaceChildWithSameNode() {
        Node parentNode = new Element("div");
        String nodeText = "node";

        Node node = new TextNode(nodeText);
        parentNode.appendChild(node);

        parentNode.replaceChild(node, node);

        assertEquals(node, parentNode.childNode(0));
    }

    @Test
    public void testReplaceChildWithNullNode() {
        Node parentNode = new Element("div");
        String nodeText = "node";

        Node node = new TextNode(nodeText);
        parentNode.appendChild(node);

        parentNode.replaceChild(node, null);

        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testRemoveChild() {
        Node parentNode = new Element("div");
        String childText = "child";

        Node child = new TextNode(childText);
        parentNode.appendChild(child);

        parentNode.removeChild(child);

        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testRemoveChildWithNullChild() {
        Node parentNode = new Element("div");

        parentNode.removeChild(null);

        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testAddChildren() {
        Node parentNode = new Element("div");
        String child1Text = "child1";
        String child2Text = "child2";

        Node child1 = new TextNode(child1Text);
        Node child2 = new TextNode(child2Text);

        parentNode.addChildren(child1, child2);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testAddChildrenWithEmptyArray() {
        Node parentNode = new Element("div");

        parentNode.addChildren();

        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testAddChildrenWithNullChild() {
        Node parentNode = new Element("div");
        Node nullChild = null;

        parentNode.addChildren(new Node[] { nullChild });

        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testGetDocument() {
        Node node = new Element("p");

        Document document = new Document();
        document.appendChild(node);

        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testGetDocumentWithNullOwnerDocument() {
        Node node = new Element("p");

        assertNull(node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parentNode = new Element("div");
        String childText = "child";

        Node child = new TextNode(childText);
        parentNode.appendChild(child);

        parentNode.remove();

        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testRemoveWithNullNode() {
        Node parentNode = new Element("div");

        parentNode.remove(null);

        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testSetParentNode() {
        Node childNode = new TextNode("child");
        Node parentNode = new Element("div");

        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testSetParentNodeWithNullParent() {
        Node childNode = new TextNode("child");

        childNode.setParentNode(null);

        assertNull(childNode.parent());
    }

    @Test
    public void testOuterHtml() {
        Node node = new Element("p").attr("id", "element1").attr("class", "class1");
        node.outerHtml("depth", new Document.OutputSettings());

        String expected = "<p id=\"element1\" class=\"class1\" >\n";
        String actual = node.outerHtml();

        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlWithIndentation() {
        Node node = new Element("p").attr("id", "element1").attr("class", "class1");
        StringBuilder accum = new StringBuilder(128);

        node.outerHtml(accum, 0, new Document.OutputSettings());

        String expected = "<p id=\"element1\" class=\"class1\" >\n";
        String actual = accum.toString();

        assertEquals(expected, actual);
    }
}