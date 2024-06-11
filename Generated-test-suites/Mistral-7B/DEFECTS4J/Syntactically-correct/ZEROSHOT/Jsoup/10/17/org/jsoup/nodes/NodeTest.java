import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "test-node");

        Node node = new Node(baseUri, attributes);
        assertEquals("test-node", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.parent());
        assertEquals(Collections.emptyList(), node.childNodes());
        assertTrue(node.hasAttr("id"));
        assertEquals("test-node", node.attr("id"));
        assertFalse(node.hasAttr("class"));
        assertEquals("", node.attr("class"));
        assertFalse(node.hasAttr("abs:href"));
        assertEquals("", node.absUrl("href"));
    }

    @Test
    public void testTextNodeCreation() {
        String baseUri = "http://example.com/";
        Node textNode = new TextNode("Hello World", baseUri);
        assertEquals("text", textNode.nodeName());
        assertEquals(baseUri, textNode.baseUri());
        assertTrue(textNode instanceof TextNode);
        assertNull(textNode.parent());
        assertEquals(Collections.emptyList(), textNode.childNodes());
        assertEquals("Hello World", textNode.outerHtml());
    }

    @Test
    public void testElementNodeCreation() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "test-element");
        attributes.put("class", "test-class");

        Element element = new Element("div", baseUri, attributes);
        assertEquals("div", element.nodeName());
        assertEquals(baseUri, element.baseUri());
        assertTrue(element instanceof Element);
        assertNull(element.parent());
        assertEquals(Collections.emptyList(), element.childNodes());
        assertTrue(element.hasAttr("id"));
        assertEquals("test-element", element.attr("id"));
        assertTrue(element.hasAttr("class"));
        assertEquals("test-class", element.attr("class"));
        assertFalse(element.hasAttr("abs:href"));
        assertEquals("", element.absUrl("href"));
    }

    @Test
    public void testNodeAttributes() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "test-node");
        attributes.put("class", "test-class");
        Node node = new Node(baseUri, attributes);

        node.attr("data-test", "Test Value");
        assertTrue(node.hasAttr("data-test"));
        assertEquals("Test Value", node.attr("data-test"));

        String absUrl = node.absUrl("href");
        assertEquals("", absUrl);

        node.setBaseUri("http://example.org/");
        absUrl = node.absUrl("href");
        assertEquals("http://example.org/", absUrl);

        node.removeAttr("data-test");
        assertFalse(node.hasAttr("data-test"));
    }

    @Test
    public void testNodeChildren() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new TextNode("Child 1", baseUri);
        Node childNode2 = new Element("p", baseUri, attributes);
        Node childNode3 = new TextNode("Child 3", baseUri);

        parentNode.addChildren(childNode1, childNode2, childNode3);

        List<Node> children = parentNode.childNodes();
        assertEquals(3, children.size());
        List<Node> expectedChildren = Arrays.asList(childNode1, childNode2, childNode3);
        assertEquals(expectedChildren, children);

        Node childNodeToRemove = childNode2;
        parentNode.removeChild(childNodeToRemove);
        children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertFalse(children.contains(childNodeToRemove));

        Node newChild = new TextNode("New Child", baseUri);
        parentNode.addChild(newChild);
        children = parentNode.childNodes();
        assertEquals(3, children.size());
        assertTrue(children.contains(newChild));
    }

    @Test
    public void testNodeTraversal() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Element("div", baseUri, attributes);
        Node childNode1Child1 = new TextNode("Child 1.1", baseUri);
        Node childNode1Child2 = new TextNode("Child 1.2", baseUri);
        Node childNode2 = new Element("p", baseUri, attributes);
        Node childNode3 = new TextNode("Child 3", baseUri);

        parentNode.addChildren(childNode1, childNode2, childNode3);
        childNode1.addChildren(childNode1Child1, childNode1Child2);

        List<Node> traversedNodes = new ArrayList<>();
        Node visitor = new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                traversedNodes.add(node);
            }

            @Override
            public void tail(Node node, int depth) {
                // empty
            }
        };

        NodeTraversor traversor = new NodeTraversor(visitor);
        traversor.traverse(parentNode);

        List<Node> expectedTraversedNodes = Arrays.asList(parentNode, childNode1, childNode1Child1, childNode1Child2, childNode2, childNode3);
        assertEquals(expectedTraversedNodes, traversedNodes);
    }
}