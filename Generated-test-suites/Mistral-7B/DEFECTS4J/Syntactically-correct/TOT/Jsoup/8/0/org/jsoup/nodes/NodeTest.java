package org.jsoup.nodes;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");

        Node rootNode = new Node(baseUri, attributes);
        Node textNode = new TextNode("Hello World!");

        Element elementNode = new Element("div", attributes);
        elementNode.appendText("Test Element");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri, attributes);
        Node node3 = new Node(baseUri, attributes);

        node1.appendChild(textNode);
        node1.appendChild(elementNode);
        node1.appendChild(node2);
        node1.appendChild(node3);

        // Test baseUri
        assertEquals(baseUri, node1.baseUri());

        // Test attributes() method
        Assert.assertEquals(attributes, node1.attributes());
        Assert.assertEquals(attributes, node1.attributes());

        // Test getAttribute and removeAttribute methods
        Assert.assertEquals("testNode", node1.attr("id"));
        node1.removeAttr("id");
        Assert.assertNull(node1.attr("id"));

        // Test hasAttr method
        Assert.assertTrue(node1.hasAttr("id"));
        Assert.assertFalse(node1.hasAttr("nonexistentattr"));

        // Test absUrl method
        Assert.assertEquals("http://www.example.com/href", node1.absUrl("href"));
        Assert.assertEquals("", node1.absUrl("nonexistentattr"));

        // Test childNodes and childNode methods
        List<Node> childNodes = node1.childNodes();
        Assert.assertEquals(3, childNodes.size());
        assertEquals(textNode, node1.getChildNodes().get(0));
        assertEquals(elementNode, node1.getChildNodes().get(1));
        assertEquals(node2, node1.getChildNodes().get(2));
        Node childNode = node1.childNode(1);
        Assert.assertEquals(elementNode, childNode);

        // Test parent method
        Node parentNode = node1.parent();
        Assert.assertNull(parentNode);

        // Test remove method
        node1.remove();
        Assert.assertNull(node1.ownerDocument());

        // Test replaceWith method
        Node replacedNode = new Element("p", attributes);
        node2.replaceWith(replacedNode);
        Assert.assertFalse(node2.hasParent());
        Assert.assertEquals(replacedNode, node1.childNode(1));
    }

    @Test
    public void testNodeManipulation() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");

        Node rootNode = new Node(baseUri, attributes);
        Node textNode = new TextNode("Hello World!");
        Node elementNode = new Element("div", attributes);
        elementNode.appendText("Test Element");

        rootNode.appendChild(textNode);
        rootNode.appendChild(elementNode);

        // Test setBaseUri
        rootNode.setBaseUri("http://www.newexample.com");
        assertEquals("http://www.newexample.com", rootNode.baseUri());

        // Test attr method
        rootNode.attr("test", "value");
        assertEquals("value", rootNode.attr("test"));
        rootNode.removeAttr("test");
        Assert.assertNull(rootNode.attr("test"));

        // Test addChild, removeChild and replaceChild methods
        Node newChild = new Element("p", attributes);
        rootNode.appendChild(newChild);
        rootNode.appendChild(new TextNode("Test"));
        rootNode.removeChild(newChild);
        rootNode.appendChild(newChild);
        rootNode.replaceChild(newTextNode("Test"), newTextNode("Test Replaced"));

        // Test addChildren method
        Node[] newChildren = {new TextNode("Test 1"), new TextNode("Test 2"), new Element("p", attributes)};
        rootNode.addChildren(newChildren);

        // Test siblingIndex method
        Node sibling1 = new Element("p", attributes);
        rootNode.appendChild(sibling1);
        Node sibling2 = new TextNode("Test 2");
        rootNode.appendChild(sibling2);
        assertEquals(0, sibling1.siblingIndex());
        assertEquals(1, sibling2.siblingIndex());

        // Test remove and replaceChild methods with siblings
        rootNode.removeChild(sibling1);
        rootNode.replaceChild(sibling2, new TextNode("Test Replaced"));
    }
}