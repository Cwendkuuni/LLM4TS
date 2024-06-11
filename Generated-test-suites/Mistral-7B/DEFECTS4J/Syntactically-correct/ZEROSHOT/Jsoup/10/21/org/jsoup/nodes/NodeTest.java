package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Attributes;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "https://example.com";
        Node node = new Node(baseUri, new Attributes());
        assertEquals("Node", node.getClass().getSimpleName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(Collections.emptyList(), node.childNodes());
        assertNull(node.parentNode());
        assertNull(node.attributes());
    }

    @Test
    public void testElementCreation() {
        String baseUri = "https://example.com";
        String elementName = "div";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element(elementName, baseUri, attributes);
        assertEquals(elementName, node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(Collections.emptyList(), node.childNodes());
        assertNull(node.parentNode());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testTextNodeCreation() {
        String baseUri = "https://example.com";
        String text = "Hello World";
        Node node = new TextNode(text, baseUri);
        assertEquals("TextNode", node.getClass().getSimpleName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(Collections.emptyList(), node.childNodes());
        assertNull(node.parentNode());
        assertEquals(text, node.toString());
    }

    @Test
    public void testAttributeGettersAndSetters() {
        String baseUri = "https://example.com";
        String attributeName = "test";
        String attributeValue = "attributeValue";

        Attributes attributes = new Attributes();
        attributes.put(attributeName, attributeValue);

        Node node = new Element("div", baseUri, attributes);

        assertEquals(attributeValue, node.attr(attributeName));
        assertEquals(attributeValue, node.attr(attributeName.toLowerCase()));
        assertEquals(attributeValue, node.attr("abs:" + attributeName));
        assertTrue(node.hasAttr(attributeName));
        assertTrue(node.hasAttr("abs:test"));

        node.attr(attributeName, "updatedValue");
        assertEquals("updatedValue", node.attr(attributeName));
        assertEquals("updatedValue", node.attr("abs:" + attributeName));
    }

    @Test
    public void testAttributeRemoval() {
        String baseUri = "https://example.com";
        String attributeName = "test";
        Attributes attributes = new Attributes();
        attributes.put(attributeName, "attributeValue");

        Node node = new Element("div", baseUri, attributes);

        node.removeAttr(attributeName);
        assertFalse(node.hasAttr(attributeName));
        assertFalse(node.hasAttr("abs:test"));
        assertEquals("", node.attr(attributeName));
        assertEquals("", node.attr("abs:test"));
    }

    @Test
    public void testBaseUriSetterAndGetter() {
        String baseUriOld = "https://example.com";
        String baseUriNew = "https://example.org";
        Node node = new Element("div", baseUriOld, new Attributes());

        node.setBaseUri(baseUriNew);
        assertEquals(baseUriNew, node.baseUri());
    }

    @Test
    public void testChildNodesGettersAndSetters() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);

        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);

        List<Node> childNodes = parentNode.childNodes();
        assertEquals(2, childNodes.size());

        Node childNode1FromList = childNodes.get(0);
        assertEquals(childNode1, childNode1FromList);

        List<Node> childNodes2 = parentNode.childNodesAsArray();
        assertEquals(2, childNodes2.length);

        parentNode.removeChild(childNode1);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(childNode2, parentNode.childNodes().get(0));
    }

    @Test
    public void testGetParentNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parentNode());
        assertEquals(childNode, parentNode.childNodes().get(0));
    }

    @Test
    public void testGetOwnerDocument() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node node = new Element("div", baseUri, attributes);
        Document document = new Document(baseUri);
        document.setRootElement(node);

        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemoveNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);
        parentNode.addChildren(childNode);

        parentNode.removeChild(childNode);
        assertEquals(0, parentNode.childNodes().size());
        assertNull(childNode.parentNode());
    }

    @Test
    public void testReplaceChildNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChildNode = new Element("p", baseUri, attributes);
        Node newChildNode = new Element("p", baseUri, attributes);

        parentNode.addChildren(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);

        assertEquals(newChildNode, parentNode.childNodes().get(0));
        assertEquals(newChildNode, oldChildNode.parentNode());
        assertEquals(oldChildNode, newChildChildNode.previousSibling());
    }

    @Test
    public void testSiblingNodes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);
        Node sibling1 = new Element("p", baseUri, attributes);
        Node node = new Element("p", baseUri, attributes);
        Node sibling2 = new Element("p", baseUri, attributes);

        parentNode.addChildren(sibling1, node, sibling2);

        List<Node> siblingNodes = node.siblingNodes();

        assertEquals(2, siblingNodes.size());
        assertEquals(sibling1, siblingNodes.get(0));
        assertEquals(sibling2, siblingNodes.get(1));

        assertEquals(sibling1, node.previousSibling());
        assertEquals(sibling2, node.nextSibling());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "https://example.com";
        String indent = "\t";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setIndentAmount(indent.length());

        Node node = new Element("div", baseUri, new Attributes());
        node.outerHtml("test", outputSettings);

        StringBuilder expected = new StringBuilder();
        expected.append("<div>");
        expected.append(indent).append("test");
        expected.append("</div>");

        StringBuilder actual = new StringBuilder();
        node.outerHtml(actual, outputSettings);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testCloneNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node originalNode = new Element("div", baseUri, attributes);
        Node clonedNode = originalNode.clone();

        assertEquals(originalNode.getClass().getSimpleName(), clonedNode.getClass().getSimpleName());
        assertEquals(originalNode.baseUri(), clonedNode.baseUri());
        assertEquals(originalNode.childNodes(), clonedNode.childNodes());
        assertEquals(originalNode.parentNode(), clonedNode.parentNode());
        assertEquals(originalNode.attributes(), clonedNode.attributes());
    }
}