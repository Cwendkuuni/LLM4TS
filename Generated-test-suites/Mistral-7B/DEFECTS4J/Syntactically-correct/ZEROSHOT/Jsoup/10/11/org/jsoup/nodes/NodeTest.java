package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals(attributes, node.attributes());
        Assert.assertNull(node.childNodes());
        Assert.assertNull(node.parentNode());
    }

    @Test
    public void testNodeConstructorWithEmptyAttributes() {
        String baseUri = "http://www.example.com";
        Node node = new Node(baseUri);

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertNull(node.attributes());
        Assert.assertNull(node.childNodes());
        Assert.assertNull(node.parentNode());
    }

    @Test
    public void testDefaultConstructor() {
        Node node = new Node();

        Assert.assertNull(node.baseUri());
        Assert.assertNull(node.attributes());
        Assert.assertEquals(Collections.EMPTY_LIST, node.childNodes());
        Assert.assertNull(node.parentNode());
    }

    @Test
    public void testAbstractNodeMethods() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals("Node", node.nodeName());
        Assert.assertNull(node.attr("nonExistentAttribute"));
        List<Node> children = new ArrayList<>(3);
        children.add(new Node());
        children.add(new Node());
        children.add(new Node());
        node.childNodes = children;
        Assert.assertEquals(children, node.childNodes());
        Assert.assertEquals(node, node.parent());
        Assert.assertNull(node.ownerDocument());

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals(attributes, node.attributes());
        Assert.assertEquals(node, node.clone());
    }

    @Test
    public void testAttributeMethods() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");
        attributes.put("class", "testClass");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals("testNode", node.attr("id"));
        Assert.assertEquals("testClass", node.attr("class"));
        Assert.assertEquals("", node.attr("nonExistentAttribute"));
        Assert.assertFalse(node.hasAttr("id"));
        Assert.assertTrue(node.hasAttr("class"));
        Assert.assertNull(node.removeAttr("id"));
        Assert.assertEquals("", node.removeAttr("class"));
        Assert.assertFalse(node.hasAttr("id"));
        Assert.assertFalse(node.hasAttr("class"));

        node.attr("id", "newID");
        Assert.assertEquals("newID", node.attr("id"));
        Assert.assertTrue(node.hasAttr("id"));

        node.attr("class", "newClass");
        Assert.assertEquals("newClass", node.attr("class"));
        Assert.assertTrue(node.hasAttr("class"));
    }

    @Test
    public void testAbsUrlMethod() {
        String baseUri = "http://www.example.com";
        String childUri = " about.html";
        Node node = new Node(baseUri, new Attributes());
        node.setAttribute("href", childUri);

        String absoluteUrl = node.absUrl("href");
        Assert.assertEquals("http://www.example.com/about.html", absoluteUrl);

        String relativeUrl = "rel.html";
        absoluteUrl = node.absUrl(relativeUrl);
        Assert.assertEquals("http://www.example.com/rel.html", absoluteUrl);

        String baseUriWithTrailingSlash = baseUri + "/";
        node = new Node(baseUriWithTrailingSlash, new Attributes());
        absoluteUrl = node.absUrl(relativeUrl);
        Assert.assertEquals("http://www.example.com/rel.html", absoluteUrl);

        absoluteUrl = node.absUrl("href");
        Assert.assertEquals("http://www.example.com/about.html", absoluteUrl);

        // test with empty baseURI
        node = new Node("");
        absoluteUrl = node.absUrl("href");
        Assert.assertEquals("", absoluteUrl);

        // test with null baseURI
        node = new Node(null);
        absoluteUrl = node.absUrl("href");
        Assert.assertEquals("", absoluteUrl);

        // test with empty attribute value
        node.setAttribute("href", "");
        absoluteUrl = node.absUrl("href");
        Assert.assertEquals("", absoluteUrl);

        // test with null attribute value
        node.setAttribute("href", null);
        absoluteUrl = node.absUrl("href");
        Assert.assertEquals("", absoluteUrl);
    }

    @Test
    public void testChildNodeMethods() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        List<Node> children = new ArrayList<>(2);
        children.add(childNode1);
        children.add(childNode2);
        parentNode.childNodes = children;

        Assert.assertEquals(children, parentNode.childNodes());
        Assert.assertEquals(childNode1, parentNode.childNode(0));
        Assert.assertEquals(childNode2, parentNode.childNode(1));
        Assert.assertEquals(2, parentNode.childNodes().size());
        Assert.assertEquals(parentNode, childNode1.parent());
        Assert.assertEquals(parentNode, childNode2.parent());
        Assert.assertNull(childNode1.parent());
        Assert.assertNull(childNode2.parent());

        parentNode.removeChild(childNode1);
        Assert.assertEquals(childNode2, parentNode.childNode(0));
        Assert.assertEquals(0, parentNode.childNodes().size());

        parentNode.addChildren(childNode1, childNode2);
        Assert.assertEquals(childNode1, parentNode.childNode(0));
        Assert.assertEquals(childNode2, parentNode.childNode(1));
        Assert.assertEquals(2, parentNode.childNodes().size());

        Node node = new Node();
        parentNode.addChildren(node);
        Assert.assertEquals(3, parentNode.childNodes().size());

        Node[] childrenArray = parentNode.childNodesAsArray();
        Assert.assertEquals(3, childrenArray.length);
        Assert.assertEquals(childNode1, childrenArray[0]);
        Assert.assertEquals(childNode2, childrenArray[1]);
        Assert.assertEquals(node, childrenArray[2]);

        parentNode.remove();
        Assert.assertNull(parentNode.parent());
        Assert.assertEquals(0, parentNode.childNodes().size());
    }
}