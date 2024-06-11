package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "elementId");
        Node node = new Element("div", baseUri, attributes);

        Assert.assertEquals("div", node.nodeName());
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals(attributes, node.attributes());
        Assert.assertNull(node.parentNode());
        Assert.assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testNodeAttrMethod() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "elementId");
        Node node = new Element("div", baseUri, attributes);

        Assert.assertEquals("", node.attr("nonexistentattr"));
        Assert.assertEquals("elementId", node.attr("id"));
        Assert.assertEquals("https://example.com/someurl", node.absUrl("someurl"));
        Assert.assertEquals("https://example.com/someurl", node.attr("abs:someurl"));

        node.attr("class", "testClass");
        Assert.assertEquals("testClass", node.attr("class"));

        node.removeAttr("id");
        Assert.assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testNodeChildManipulation() throws Exception {
        String baseUri = "https://example.com";
        Node parentNode = new Document(baseUri);

        Node child1 = new Element("p", baseUri, new AttributesImpl());
        parentNode.appendChild(child1);

        Node child2 = new TextNode("Hello World");
        parentNode.appendChild(child2);

        Node child3 = new Element("a", baseUri, new AttributesImpl().put("href", "https://google.com"));
        parentNode.appendChild(child3);

        Assert.assertEquals(3, parentNode.childNodes().size());
        Assert.assertEquals("p", parentNode.childNodes().get(0).nodeName());
        Assert.assertEquals("Hello World", parentNode.childNodes().get(1).text());
        Assert.assertEquals("a", parentNode.childNodes().get(2).nodeName());
        Assert.assertEquals("https://google.com", parentNode.childNodes().get(2).absUrl("href"));

        Node removedChild = parentNode.removeChild(1);
        Assert.assertEquals(2, parentNode.childNodes().size());
        Assert.assertEquals("p", removedChild.nodeName());

        Node replacedChild = new Element("span", baseUri, new AttributesImpl().put("class", "newClass"));
        parentNode.replaceChild(parentNode.childNodes().get(0), replacedChild);
        Assert.assertEquals("span", parentNode.childNodes().get(0).nodeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeNullArguments() {
        String baseUri = null;
        Attributes attributes = new AttributesImpl();
        new Element("div", baseUri, attributes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeEmptyAttributes() {
        String baseUri = "https://example.com";
        new Element("div", baseUri, null);
    }

    @Test
    public void testNodeEquals() {
        String baseUri = "https://example.com";
        Attributes attributes1 = new AttributesImpl().put("id", "elementId1");
        Attributes attributes2 = new AttributesImpl().put("id", "elementId2");

        Node node1 = new Element("div", baseUri, attributes1);
        Node node2 = new Element("div", baseUri, attributes1);
        Node node3 = new Element("div", baseUri, attributes2);

        Assert.assertEquals(node1, node1);
        Assert.assertEquals(node1, node1);
        Assert.assertEquals(node1, node2);
        Assert.assertNotEquals(node1, node3);
    }

    @Test
    public void testNodeClone() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "elementId");
        Node originalNode = new Element("div", baseUri, attributes);
        Node clonedNode = originalNode.clone();

        Assert.assertEquals(originalNode, clonedNode);
        Assert.assertNotSame(originalNode, clonedNode);
        Assert.assertEquals(originalNode.childNodes(), clonedNode.childNodes());

        // modifying cloned node
        Node child = new Element("p", baseUri, new AttributesImpl());
        clonedNode.appendChild(child);
        Assert.assertEquals(1, clonedNode.childNodes().size());
        Assert.assertEquals("p", clonedNode.childNodes().get(0).nodeName());
        Assert.assertNotEquals(originalNode.childNodes(), clonedNode.childNodes());
    }
}