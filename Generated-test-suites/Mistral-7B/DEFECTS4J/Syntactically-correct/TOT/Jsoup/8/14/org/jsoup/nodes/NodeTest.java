import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");

        Node node1 = new Node(baseUri, attributes);
        assertEquals(baseUri, node1.baseUri());
        assertEquals(attributes, node1.attributes());
        assertNull(node1.parentNode);
        assertTrue(node1.childNodes().isEmpty());

        Node node2 = new Node(baseUri);
        assertEquals(baseUri, node2.baseUri());
        assertNull(node2.attributes());
        assertNull(node2.parentNode);
        assertTrue(node2.childNodes().isEmpty());

        Node node3 = new Node();
        assertNull(node3.baseUri());
        assertNull(node3.attributes());
        assertNull(node3.parentNode);
        assertTrue(node3.childNodes().isEmpty());
    }

    @Test
    public void testNodeHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");

        Node node = new Element("div", baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("class"));

        node.attr("class", "test");
        assertTrue(node.hasAttr("class"));
    }

    @Test
    public void testNodeAbsUrl() {
        String baseUri = "http://example.com";
        String relativeUrl = "images/logo.png";

        Node node = new Element("img", baseUri, new Attributes().put("src", relativeUrl));
        String absUrl = node.absUrl("src");
        URL url = new URL(absUrl);
        assertEquals("http://example.com/images/logo.png", url.toString());

        node.baseUri("http://subdomain.example.com");
        absUrl = node.absUrl("src");
        url = new URL(absUrl);
        assertEquals("http://subdomain.example.com/images/logo.png", url.toString());
    }

    @Test
    public void testNodeChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);

        Node childNode1 = new Element("p", baseUri, new Attributes().put("class", "child1"));
        parentNode.addChildren(childNode1);

        Node childNode2 = new TextNode("Text node 2");
        parentNode.addChildren(childNode2);

        List<Node> childNodes = parentNode.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));
        assertEquals(childNode2, childNodes.get(1));

        Node childNode3 = new Element("p", baseUri, new Attributes().put("class", "child3"));
        parentNode.replaceChild(childNode2, childNode3);
        childNodes = parentNode.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));
        assertEquals(childNode3, childNodes.get(1));

        List<Node> childNodesArray = parentNode.childNodesAsArray();
        assertEquals(2, childNodesArray.length);
        assertEquals(childNode1, childNodesArray[0]);
        assertEquals(childNode3, childNodesArray[1]);
    }

    @Test
    public void testNodeParentNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node childNode = new Element("p", baseUri, new Attributes().put("class", "child"));

        Node parentNode = new Element("div", baseUri, attributes);
        parentNode.addChildren(childNode);

        assertEquals(parentNode, childNode.parent());
        assertNull(childNode.parentNode
());

        parentNode.removeChild(childNode);
        assertNull(childNode.parent());
    }

    @Test(expected = NullPointerException.class)
    public void testNodeNullParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node childNode = new Element("p", baseUri, new Attributes().put("class", "child"));

        childNode.setParentNode(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNodeGetChildAtInvalidIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);

        Node childNode1 = new Element("p", baseUri, new Attributes().put("class", "child1"));
        Node childNode2 = new Element("p", baseUri, new Attributes().put("class", "child2"));
        parentNode.addChildren(childNode1, childNode2);

        Node nonExistentChild = parentNode.childNode(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeNegativeChildIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);

        Node childNode1 = new Element("p", baseUri, new Attributes().put("class", "child1"));
        parentNode.addChildren(childNode1);

        parentNode.removeChild(childNode1);

        parentNode.removeChild(-1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNodeModifyUnmodifiableList() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);

        List<Node> childNodes = parentNode.childNodes();
        Node childNode = new Element("p", baseUri, new Attributes().put("class", "child"));
        parentNode.addChildren(childNode);

        try {
            childNodes.add(new TextNode("Text node"));
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNodeNullChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);

        try {
            parentNode.removeChild(null);
        } catch (NullPointerException e) {
            // expected
        }

        try {
            parentNode.addChildren(null);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testNodeRemoveAndReplace() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);

        Node childNode1 = new Element("p", baseUri, new Attributes().put("class", "child1"));
        Node childNode2 = new Element("p", baseUri, new Attributes().put("class", "child2"));

        parentNode.addChildren(childNode1, childNode2);
        List<Node> childNodes = parentNode.childNodes();

        parentNode.removeChild(childNode1);
        childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(childNode2, childNodes.get(0));

        Node childNode3 = new Element("p", baseUri, new Attributes().put("class", "child3"));
        parentNode.replaceChild(childNode2, childNode3);
        childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(childNode3, childNodes.get(0));
    }
}