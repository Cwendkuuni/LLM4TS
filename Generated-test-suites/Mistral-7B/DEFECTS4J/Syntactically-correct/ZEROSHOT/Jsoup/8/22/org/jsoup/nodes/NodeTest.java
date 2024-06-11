package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Attributes;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {

    @org.junit.Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("name", "value");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("value", node1.attr("name"));
        assertNull(node2.attr("name"));
        assertNull(node3.attr("name"));

        assertEquals(baseUri, node1.baseUri());
        assertEquals(baseUri, node2.baseUri());
        assertNull(node3.baseUri());

        assertTrue(node1 instanceof AbstractNode);
        assertFalse(node2 instanceof AbstractNode);
        assertFalse(node3 instanceof AbstractNode);
    }

    @org.junit.Test
    public void testAbstractMethods() {
        assertTrue(false); // cannot instantiate abstract class to test abstract methods
    }

    @org.junit.Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("name", "value");

        Node node = new Element("div", baseUri, attributes);
        node.attr("name", "newValue");

        assertEquals("newValue", node.attr("name"));
        assertEquals("value", node.attr("oldName"));
        assertTrue(node.hasAttr("name"));
        assertTrue(node.hasAttr("abs:name"));
        assertFalse(node.hasAttr("abs:oldName"));
        assertFalse(node.hasAttr("abs:nonExistent"));

        assertEquals("", node.attr("nonExistent"));

        node.removeAttr("name");
        assertFalse(node.hasAttr("name"));

        node.setBaseUri("http://example2.com");
        assertEquals("http://example2.com", node.baseUri());
        assertEquals("http://example2.com/foo", node.absUrl("foo"));
        assertEquals("", node.absUrl(""));
        assertEquals("http://example2.com/foo#bar", node.absUrl("foo#bar"));
    }

    @org.junit.Test
    public void testChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div", baseUri, attributes);
        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parent.appendChild(child1);
        parent.appendChild(child2);

        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));

        Node grandChild = new Element("span", baseUri, attributes);
        child1.appendChild(grandChild);

        assertEquals(3, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
        assertEquals(grandChild, parent.childNode(2));

        Node childNodesAsArray[] = parent.childNodesAsArray();
        assertEquals(grandChild, childNodesAsArray[2]);

        Element childElement = (Element) parent.childNodes().get(0);
        List<Node> childList = childElement.childNodes();
        assertEquals(1, childList.size());
        assertEquals(grandChild, childList.get(0));
    }

    @org.junit.Test
    public void testParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node child = new Element("p", baseUri, attributes);
        Node parent = new Element("div", baseUri, attributes);

        parent.appendChild(child);

        assertEquals(child, parent.childNode(0));
        assertEquals(parent, child.parent());
        assertEquals(null, child.parent().childNode(1));

        child.setParentNode(null);
        assertNull(child.parent());
    }

    @org.junit.Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div", baseUri, attributes);
        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parent.appendChild(child1);
        parent.appendChild(child2);

        parent.removeChild(child1);

        assertEquals(1, parent.childNodes().size());
        assertEquals(child2, parent.childNode(0));

        parent.remove();
        assertNull(child2.parent());

        Element newChild = new Element("p", baseUri, attributes);
        parent.appendChild(newChild);
        assertEquals(1, parent.childNodes().size());
        assertEquals(newChild, parent.childNode(0));
    }

    @org.junit.Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div", baseUri, attributes);
        Node oldChild = new Element("p", baseUri, attributes);
        Node newChild = new Element("p", baseUri, attributes);

        parent.appendChild(oldChild);
        parent.appendChild(newChild);

        parent.replaceChild(oldChild, newChild);

        assertEquals(1, parent.childNodes().size());
        assertEquals(newChild, parent.childNode(0));
        assertEquals(null, oldChild.parent());
    }

    @org.junit.Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div", baseUri, attributes);

        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        parent.addChildren(child1, child2);

        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));

        List<Node> childrenList = parent.childNodes();
        assertEquals(child1, childrenList.get(0));
        assertEquals(child2, childrenList.get(1));
    }

    @org.junit.Test
    public void testSiblingNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div", baseUri, attributes);
        Node sibling1 = new Element("p", baseUri, attributes);
        Node sibling2 = new Element("p", baseUri, attributes);
        Node node = new Element("p", baseUri, attributes);

        parent.appendChild(sibling1);
        parent.appendChild(node);
        parent.appendChild(sibling2);

        List<Node> siblingNodes = node.siblingNodes();
        assertEquals(2, siblingNodes.size());
        assertEquals(sibling1, siblingNodes.get(0));
        assertEquals(sibling2, siblingNodes.get(1));

        Node prevSibling = node.previousSibling();
        Node nextSibling = node.nextSibling();

        assertEquals(sibling1, prevSibling);
        assertEquals(sibling2, nextSibling);
    }

    @org.junit.Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");

        Node node = new Element("p", baseUri, attributes);
        node.outerHtml("test");

        assertEquals("<p class=\"testClass\">test</p>", node.outerHtml());

        Document doc = new Document(baseUri);
        doc.setOutputSettings(Document.OutputSettings.prettyPrint());

        node.outerHtmlHead(new StringBuilder(), 0, doc.outputSettings());
        node.outerHtmlTail(new StringBuilder(), 0, doc.outputSettings());
        node.outerHtml(new StringBuilder(), 0, doc.outputSettings());

        assertEquals("<p class=\"testClass\">test</p>", node.outerHtml());
    }

    @org.junit.Test
    public void testToString() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new TextNode("Test text", baseUri, attributes);

        assertEquals("Test text", node.toString());
        assertEquals("Test text", node.outerHtml());
    }

    @org.junit.Test
    public void testHashCodeAndEquals() {
        String baseUri1 = "http://example1.com";
        String baseUri2 = "http://example2.com";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        Node node1 = new Element("p", baseUri1, attributes1);
        Node node2 = new Element("p", baseUri2, attributes2);

        Node node3 = new Element("p", baseUri1, attributes1);
        Node node4 = new Element("p", baseUri1, attributes2);

        Node node5 = new TextNode("Text", baseUri1, attributes1);

        assertEquals(node1, node1);
        assertEquals(node1.hashCode(), node1.hashCode());

        assertEquals(node1, node3);
        assertEquals(node1.hashCode(), node3.hashCode());

        assertNotEquals(node1, node2);
        assertNotEquals(node1.hashCode(), node2.hashCode());

        assertNotEquals(node1, node4);
        assertNotEquals(node1.hashCode(), node4.hashCode());

        assertNotEquals(node1, node5);
        assertNotEquals(node1.hashCode(), node5.hashCode());

        assertNotEquals(node1, null);
    }
}