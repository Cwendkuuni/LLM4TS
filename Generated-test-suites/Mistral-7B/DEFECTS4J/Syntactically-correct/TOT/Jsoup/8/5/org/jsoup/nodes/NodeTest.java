package org.jsoup.nodes;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    @Test
    public void testAbstractNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node node = new Node(baseUri, attributes);

        assertEquals("test", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.parentNode());
        assertSame(attributes, node.attributes());
        assertTrue(node.childNodes().isEmpty());
        assertEquals(0, node.siblingIndex());
    }

    @Test
    public void testAbstractNodeConstructorWithAttributesOnly() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertEquals("", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.parentNode());
        assertTrue(node.attributes().isEmpty());
        assertTrue(node.childNodes().isEmpty());
        assertEquals(0, node.siblingIndex());
    }

    @Test
    public void testAbstractNodeDefaultConstructor() {
        Node node = new Node();

        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
        assertTrue(node.attributes() == Node.EMPTY_ATTRIBUTES);
        assertEquals(0, node.siblingIndex());
    }

    @Test
    public void testNodeName() {
        Element element = new Element("div");
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test").put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("test", node.attr("id"));
        assertEquals("test", node.attr("class"));
        assertEquals("", node.attr("nonexistent"));

        attributes.put("title", "Test Title");
        assertEquals("Test Title", node.attr("title"));
        assertEquals("", node.attr("abs:nonexistent"));

        assertEquals(baseUri, node.absUrl("href"));
        assertEquals("", node.absUrl("nonexistent"));
    }

    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test").put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        assertEquals(2, node.attributes().size());
        assertEquals("test", node.attributes().get("id"));
        assertEquals("test", node.attributes().get("class"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test").put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonexistent"));
        assertFalse(node.hasAttr("abs:nonexistent"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test").put("class", "test");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("test", node.removeAttr("id").attr("id"));
        assertEquals("test", node.removeAttr("class").attr("class"));
        assertEquals("", node.removeAttr("nonexistent").attr("nonexistent"));
        assertNull(node.removeAttr("nonexistent").attr("abs:nonexistent"));
        assertFalse(node.hasAttr("id"));
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri);

        assertEquals(baseUri, node.baseUri());
        assertEquals("", node.absUrl(""));
        assertEquals("", node.absUrl("nonexistent"));

        node.setBaseUri("http://sub.example.com");
        assertEquals("http://sub.example.com", node.baseUri());
        assertEquals("http://sub.example.com/foo.html", node.absUrl("foo.html"));
        assertEquals("", node.absUrl("nonexistent"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String relativeUrl = "/foo.html";
        Node node = new Element("a", baseUri).attr("href", relativeUrl);

        assertEquals("http://example.com/foo.html", node.absUrl("href"));
        assertEquals("http://example.com/foo.html", node.absUrl("abs:href"));
        assertEquals("", node.absUrl(""));
        assertEquals("", node.absUrl("nonexistent"));

        node.setBaseUri("http://sub.example.com");
        assertEquals("http://sub.example.com/foo.html", node.absUrl("href"));
        assertEquals("http://sub.example.com/foo.html", node.absUrl("abs:href"));
        assertEquals("http://sub.example.com", node.absUrl(""));
    }

    @Test
    public void testChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);

        parentNode.addChild(childNode);
        Node child = parentNode.childNode(0);

        assertEquals(childNode, child);
        assertEquals(parentNode, child.parentNode());
        assertSame(parentNode.childNodes(), child.parentNode().childNodes());
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node childNode = new Element("p", baseUri, attributes);
        Node parentNode = new Element("div", baseUri, attributes);

        parentNode.addChild(childNode);

        assertEquals(parentNode, childNode.parentNode());
        assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node childNode = new Element("p", baseUri, attributes);
        Node parentNode = new Element("div", "http://sub.example.com", attributes);

        parentNode.addChild(childNode);

        assertEquals(parentNode, childNode.ownerDocument());
        assertEquals(parentNode.ownerDocument(), childNode.parentNode().ownerDocument());
        assertEquals(null, childNode.ownerDocument().ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);

        parentNode.addChild(childNode);
        List<Node> children = parentNode.childNodes();

        parentNode.removeChild(childNode);
        assertEquals(0, children.size());
        assertNull(childNode.parentNode());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChildNode = new Element("p", baseUri, attributes);
        Node newChildNode = new Element("em", baseUri, attributes);

        parentNode.addChild(oldChildNode);
        List<Node> children = parentNode.childNodes();

        parentNode.replaceChild(oldChildNode, newChildNode);
        assertEquals(1, children.size());
        assertEquals(newChildNode, children.get(0));
        assertEquals(parentNode, newChildNode.parentNode());
    }

    @Test
    public void testReplaceChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChildNode1 = new Element("p", baseUri, attributes);
        Node oldChildNode2 = new Element("p", baseUri, attributes);
        Node newChildNode1 = new Element("em", baseUri, attributes);
        Node newChildNode2 = new Element("em", baseUri, attributes);

        parentNode.addChildren(oldChildNode1, oldChildNode2);

        List<Node> children = parentNode.childNodes();
        parentNode.replaceChildren(newChildNode1, newChildNode2);
        assertEquals(2, children.size());
        assertEquals(newChildNode1, children.get(0));
        assertEquals(newChildNode2, children.get(1));
    }

    @Test
    public void testChildNodesAsArray() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);

        Node[] children = parentNode.childNodesAsArray();
        assertEquals(2, children.length);
        assertEquals(childNode1, children[0]);
        assertEquals(childNode2, children[1]);
    }

    @Test
    public void testNodeSiblingIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node node1 = new Element("p", baseUri, attributes);
        Node node2 = new Element("p", baseUri, attributes);
        Node parentNode = new Element("div", baseUri, attributes);

        parentNode.addChildren(node1, node2);

        assertEquals(0, node1.siblingIndex());
        assertEquals(1, node2.siblingIndex());
    }

    @Test
    public void testNodeOuterHtml() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node node = new Element("p", baseUri, attributes).attr("class", "test");

        StringBuilder output = new StringBuilder();
        node.outerHtml(output);

        assertEquals("<p class=\"test\"></p>", output.toString());
    }

    @Test
    public void testNodeOuterHtmlWithChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes).attr("class", "test1");
        Node childNode2 = new Element("p", baseUri, attributes).attr("class", "test2");

        parentNode.addChildren(childNode1, childNode2);

        StringBuilder output = new StringBuilder();
        parentNode.outerHtml(output);

        assertEquals("<div>\n<p class=\"test1\"></p>\n<p class=\"test2\"></p>\n</div>", output.toString());
    }

    @Test
    public void testNodeOuterHtmlWithTextNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new TextNode("Test node");

        parentNode.addChild(childNode);

        StringBuilder output = new StringBuilder();
        parentNode.outerHtml(output);

        assertEquals("<div>\nTest node\n</div>", output.toString());
    }

    @Test
    public void testNodeOuterHtmlWithTextNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new TextNode("Test node 1");
        Node childNode2 = new TextNode("Test node 2");

        parentNode.addChildren(childNode1, childNode2);

        StringBuilder output = new StringBuilder();
        parentNode.outerHtml(output);

        assertEquals("<div>\nTest node 1\nTest node 2\n</div>", output.toString());
    }

    @Test
    public void testNodeOuterHtmlWithCustomOutputSettings() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "test");
        Node parentNode = new Element("div", baseUri, attributes);

        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        outputSettings.indentAmount(4);

        StringBuilder output = new StringBuilder();
        parentNode.outerHtml(output, 0, outputSettings);

        assertEquals("<div>\n" +
                "   <p></p>\n" +
                "   <p></p>\n" +
                "</div>", output.toString());
    }

    @Test
    public void testNodeRemoveChildWithEmptyList() {
        Node node = new Node();

        assertNull(node.removeChild(new Node()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeRemoveChildWithNullChild() {
        Node node = new Node();
        node.removeChild(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeRemoveChildWithNullParent() {
        Node child = new Node();
        child.removeChild(new Node());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeSetParentNodeWithNullParent() {
        Node child = new Node();
        child.setParentNode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeReplaceChildWithNullChild() {
        Node parent = new Node();
        Node child = new Node();
        parent.replaceChild(null, child);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeReplaceChildWithNullParent() {
        Node child = new Node();
        Node parent = new Node();
        child.replaceChild(parent, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeAddChildrenWithNullArray() {
        Node node = new Node();
        node.addChildren((Node[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeAddChildrenWithNullChild() {
        Node node = new Node();
        node.addChildren(new Node[] {null});
    }
}