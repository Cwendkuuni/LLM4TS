package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Node.*;
import static org.jsoup.helper.StringUtil.isBlank;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "root");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("root", node1.nodeName());
        assertEquals(baseUri, node1.baseUri());
        assertEquals(attributes, node1.attributes());
        assertNull(node2.nodeName());
        assertEquals(baseUri, node2.baseUri());
        assertNull(node2.attributes());
        assertNull(node3.nodeName());
        assertNull(node3.baseUri());
        assertNull(node3.attributes());
    }

    @Test
    public void testAttrGettersAndSetters() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "root");
        Node node = new Node(baseUri, attributes);

        String attrValue = "testValue";
        node.attr("test", attrValue);
        assertEquals(attrValue, node.attr("test"));
        Assert.assertFalse(isBlank(node.attr("test")));

        node.removeAttr("test");
        assertEquals("", node.attr("test"));
        Assert.assertTrue(isBlank(node.attr("test")));

        node.hasAttr("test");
        Assert.assertFalse(node.hasAttr("test"));

        node.attr("test2", "value2");
        node.hasAttr("test2");
        assertEquals("value2", node.attr("test2"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Node node = new Elements.Tag("a", new Attributes().put("href", "relative.html"));
        node.setBaseUri(baseUri);

        String absUrl = node.absUrl("href");
        assertEquals("http://example.com/relative.html", absUrl);

        String emptyUrl = node.absUrl("");
        assertEquals(baseUri, emptyUrl);

        String noBaseUri = new TextNode("test").absUrl("test");
        assertEquals("", noBaseUri);
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "root");
        Node root = new Element("div", attributes, baseUri);

        Node child1 = new Element("p", new Attributes().put("class", "child1"), baseUri);
        Node child2 = new Element("p", new Attributes().put("class", "child2"), baseUri);

        root.addChildren(child1, child2);

        List<Node> children = root.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));

        Node child1FromRoot = root.childNode(0);
        assertEquals(child1, child1FromRoot);

        Node nonExistingChild = root.childNode(100);
        assertNull(nonExistingChild);

        Node child1FromRootAsArray = root.childNodesAsArray()[0];
        assertEquals(child1, child1FromRootAsArray);

        root.removeChild(child1);
        List<Node> childrenAfterRemoval = root.childNodes();
        assertEquals(1, childrenAfterRemoval.size());
        assertEquals(child2, childrenAfterRemoval.get(0));
    }

    @Test
    public void testParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "root");
        Node root = new Element("div", attributes, baseUri);

        Node child = new Element("p", new Attributes().put("class", "child"), baseUri);
        root.addChild(child);

        assertEquals(root, child.parent());
        assertEquals(null, child.parentNode());

        assertNull(root.parent());
        assertEquals(null, root.parentNode());

        root.setParentNode(new Document());
        assertEquals(new Document(), root.parent());
        assertEquals(new Document(), root.parentNode());
    }
}