package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.HelperResolver;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Nodes;

public class NodeTest {

    @org.junit.Before
    public void setUp() {
        HelperResolver.setupHtmlParser();
    }

    @org.junit.Test
    public void testNodeAbstractClass() {
        // Test the abstract methods
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test-class");
        Node node = new Node(baseUri, attributes);

        String nodeName = node.nodeName();
        assertNull(nodeName);

        String attrValue = node.attr("test-attribute");
        assertEquals("", attrValue);

        List<Node> children = node.childNodes();
        assertTrue(children.isEmpty());

        Node parent = node.parent();
        assertNull(parent);

        Document ownerDocument = node.ownerDocument();
        assertNull(ownerDocument);

        node.remove();
        assertFalse(node.hasParentNode());

        Node child = new Element("div");
        node.appendChild(child);
        assertEquals(child, node.lastChild());

        node.removeChild(child);
        assertTrue(node.childNodes().isEmpty());

        node.setTextNode("Test Text");
        TextNode textNode = (TextNode) node.childNodes().get(0);
        assertEquals("Test Text", textNode.text());

        assertEquals(baseUri, node.baseUri());
        assertEquals("http://www.example.com", node.absUrl("test-attribute"));

        Node childNode = new Element("p");
        node.appendChild(childNode);
        assertEquals(childNode, node.childNodes().get(0));

        Node sibling = new Element("ul");
        node.appendChild(sibling);
        assertEquals(sibling, node.nextSibling());

        node.remove();
        assertFalse(node.hasParentNode());
    }

    @org.junit.Test
    public void testNodeMethods() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test-class");

        // Test Node contructor
        Node node = new Node(baseUri, attributes);
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertTrue(node.childNodes().isEmpty());

        // Test Node constructor with no arguments
        Node node2 = new Node();
        assertNull(node2.baseUri());
        assertNull(node2.attributes());
        assertTrue(node2.childNodes().isEmpty());

        // Test Node constructor with only baseUri argument
        Node node3 = new Node(baseUri);
        assertEquals(baseUri, node3.baseUri());
        assertNull(node3.attributes());
        assertTrue(node3.childNodes().isEmpty());

        // Test Node.nodeName()
        Element element = new Element("div");
        assertEquals("div", element.nodeName());

        // Test Node.attr()
        element.attr("test-attribute", "Test Value");
        assertEquals("Test Value", element.attr("test-attribute"));
        assertEquals("", element.attr("non-existent-attribute"));

        // Test Node.attributes()
        assertEquals(attributes, element.attributes());

        // Test Node.hasAttr()
        assertTrue(element.hasAttr("class"));
        assertFalse(element.hasAttr("non-existent-attribute"));

        // Test Node.removeAttr()
        element.removeAttr("class");
        assertFalse(element.hasAttr("class"));

        // Test Node.absUrl()
        element.attr("href", "relative-url.html");
        assertEquals("http://www.example.com/relative-url.html", element.absUrl("href"));
        assertEquals("", element.absUrl("non-existent-attribute"));

        // Test Node.childNodes()
        Node child = new Element("p");
        element.appendChild(child);
        List<Node> children = element.childNodes();
        assertEquals(1, children.size());
        assertEquals(child, children.get(0));

        // Test Node.childNodesAsArray()
        Node[] childNodesArray = element.childNodesAsArray();
        assertEquals(1, childNodesArray.length);
        assertEquals(child, childNodesArray[0]);

        // Test Node.parentNode
        Element parentElement = new Element("body");
        element.setParentNode(parentElement);
        assertEquals(parentElement, element.parentNode());

        // Test Node.ownerDocument()
        Document document = new Document();
        parentElement.setOwnerDocument(document);
        assertEquals(document, element.ownerDocument());

        // Test Node.remove()
        parentElement.removeChild(element);
        assertFalse(element.hasParentNode());

        // Test Node.replaceWith()
        Node replacedNode = new Element("span");
        Element newElement = new Element("em");
        parentElement.replaceChild(element, newElement);
        assertEquals(newElement, parentElement.getElementsByTag("em").get(0));
        assertEquals(replacedNode, element);

        // Test Node.setParentNode()
        Element newParentElement = new Element("section");
        element.setParentNode(newParentElement);
        assertEquals(newParentElement, element.parentNode());

        // Test Node.replaceChild()
        Node newChild = new Element("h1");
        element.replaceChild(child, newChild);
        assertEquals(newChild, element.childNodes().get(0));

        // Test Node.removeChild()
        element.removeChild(child);
        assertTrue(element.childNodes().isEmpty());

        // Test Node.addChildren()
        Node newChild1 = new Element("p");
        Node newChild2 = new Element("ul");
        element.addChildren(newChild1, newChild2);
        List<Node> children2 = element.childNodes();
        assertEquals(2, children2.size());
        assertEquals(newChild1, children2.get(0));
        assertEquals(newChild2, children2.get(1));

        // Test Node.addChildren(int, Node...)
        Node newChild3 = new Element("li");
        element.addChildren(1, newChild3);
        List<Node> children3 = element.childNodes();
        assertEquals(3, children3.size());
        assertEquals(newChild1, children3.get(0));
        assertEquals(newChild2, children3.get(1));
        assertEquals(newChild3, children3.get(2));

        // Test Node.reparentChild()
        Node reparentedChild = new Element("table");
        element.reparentChild(reparentedChild);
        assertEquals(reparentedChild.parentNode(), null);
        assertEquals(element, reparentedChild.parentNode());

        // Test Node.reindexChildren()
        Node newChild4 = new Element("a");
        element.addChild(newChild4);
        List<Node> children4 = element.childNodes();
        assertEquals(4, children4.size());
        assertEquals(newChild1, children4.get(0));
        assertEquals(newChild2, children4.get(1));
        assertEquals(newChild3, children4.get(2));
        assertEquals(newChild4, children4.get(3));
        element.reindexChildren();
        assertEquals(newChild1, children4.get(1));
        assertEquals(newChild2, children4.get(2));
        assertEquals(newChild3, children4.get(3));
        assertEquals(newChild4, children4.get(0));

        // Test Node.siblingNodes()
        Node sibling = new Element("input");
        parentElement.appendChild(sibling);
        List<Node> siblingNodes = element.siblingNodes();
        assertEquals(1, siblingNodes.size());
        assertEquals(sibling, siblingNodes.get(0));

        // Test Node.nextSibling()
        Element nextSibling = new Element("script");
        parentElement.appendChild(nextSibling);
        parentElement.appendChild(element);
        parentElement.appendChild(new Element("h1"));
        Node nextElement = element.nextSibling();
        assertEquals(nextSibling, nextElement);

        // Test Node.previousSibling()
        Node previousElement = element.previousSibling();
        assertNull(previousElement);

        // Test Node.outerHtml()
        StringBuilder outerHtmlAccumulator = new StringBuilder(32 * 1024);
        element.outerHtml(outerHtmlAccumulator);
        String outerHtml = outerHtmlAccumulator.toString();
        String expectedOuterHtml = "<div>" +
                "<p></p>" +
                "</div>";
        assertEquals(expectedOuterHtml, outerHtml);

        // Test Node.outerHtmlHead() and Node.outerHtmlTail()
        StringBuilder outerHtmlHeadAccumulator = new StringBuilder(32 * 1024);
        StringBuilder outerHtmlTailAccumulator = new StringBuilder(32 * 1024);
        element.outerHtmlHead(outerHtmlHeadAccumulator, 0, null);
        element.outerHtmlTail(outerHtmlTailAccumulator, 0, null);
        String outerHtmlHead = outerHtmlHeadAccumulator.toString();
        String outerHtmlTail = outerHtmlTailAccumulator.toString();
        String expectedOuterHtmlHead = "<div>\n";
        String expectedOuterHtmlTail = "</div>";
        assertEquals(expectedOuterHtmlHead, outerHtmlHead);
        assertEquals(expectedOuterHtmlTail, outerHtmlTail);

        // Test Node.toString()
        String elementString = element.toString();
        assertEquals(outerHtml, elementString);

        // Test Node.equals() and Node.hashCode()
        Node anotherNode = new Element("div");
        assertFalse(element.equals(anotherNode));
        int hashCode = element.hashCode();
        Node anotherNodeWithSameProperties = new Element("div")
                .attr("class", "test-class")
                .attr("href", baseUri);
        assertEquals(hashCode, anotherNodeWithSameProperties.hashCode());
        assertEquals(element, anotherNodeWithSameProperties);
    }
}