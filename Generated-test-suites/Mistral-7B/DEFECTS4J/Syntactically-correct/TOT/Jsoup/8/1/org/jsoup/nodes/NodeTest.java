package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testElement");
        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        // Test nodeName() method
        Assert.assertNull(node3.nodeName());
        Assert.assertEquals("testElement", node1.nodeName());

        // Test attr() method
        Assert.assertEquals("", node3.attr("testKey"));
        Assert.assertEquals("testValue", node1.attr("testKey", "testValue"));
        Assert.assertEquals("", node1.attr("testKey", ""));
        Assert.assertEquals("testValue2", node1.attr("testKey", "testValue2"));
        Assert.assertEquals("", node1.attr("testKey2"));
        Assert.assertEquals("http://example.com/testPage", node1.absUrl("testLink"));

        // Test attributes() method
        Assert.assertEquals(attributes, node1.attributes());

        // Test hasAttr() method
        Assert.assertFalse(node3.hasAttr("testKey"));
        Assert.assertTrue(node1.hasAttr("id"));

        // Test removeAttr() method
        Assert.assertTrue(node1.hasAttr("id"));
        node1.removeAttr("id");
        Assert.assertFalse(node1.hasAttr("id"));

        // Test baseUri() method
        Assert.assertEquals(baseUri, node1.baseUri());
        node1.setBaseUri("http://newbase.com");
        Assert.assertEquals("http://newbase.com", node1.baseUri());

        // Test childNodes() method
        Node child1 = new Element("child1", attributes);
        Node child2 = new TextNode("child2 text");
        node1.addChildren(child1, child2);

        List<Node> children = node1.childNodes();
        Assert.assertEquals(2, children.size());
        Assert.assertEquals(child1, children.get(0));
        Assert.assertEquals(child2, children.get(1));

        // Test parent() method
        Assert.assertNull(child1.parent());
        Assert.assertEquals(node1, child1.parentNode());

        // Test remove() method
        node1.removeChild(child1);
        children = node1.childNodes();
        Assert.assertEquals(1, children.size());
        Assert.assertNull(children.get(0).parentNode());

        // Test replaceChild() method
        Node replacedChild = new Element("replacedChild", attributes);
        node1.replaceChild(child1, replacedChild);
        children = node1.childNodes();
        Assert.assertEquals(1, children.size());
        Assert.assertEquals(replacedChild, children.get(0));

        // Test ownerDocument() method
        Document doc = new Document();
        doc.setBaseUri("http://document.com");
        node1.setOwnerDocument(doc);
        Assert.assertEquals(doc, node1.ownerDocument());

        // Test setParentNode() and getParentNode() methods
        Node parentNode = new Element("parentNode", attributes);
        node1.setParentNode(parentNode);
        Assert.assertEquals(parentNode, node1.parentNode());

        // Test reindexChildren() method
        Node sibling1 = new Element("sibling1", attributes);
        Node sibling2 = new Element("sibling2", attributes);
        node1.addChildren(sibling1, sibling2);
        node1.removeChild(sibling1);
        List<Node> siblings = node1.siblingNodes();
        Assert.assertEquals(1, siblings.size());
        Assert.assertEquals(sibling2, siblings.get(0));
    }

    @Test
    public void testNodeTraversal() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testElement");

        Node root = new Document();
        root.setBaseUri(baseUri);

        Element element1 = new Element("element1", attributes);
        TextNode textNode1 = new TextNode("text1");
        Element element2 = new Element("element2", attributes);

        root.addChildren(element1, textNode1, element2);

        NodeTraversor traversor = new NodeTraversor();
        traversor.addVisitor(new NodeVisitor() {
            int count = 0;

            @Override
            public void head(Node node, int depth) {
                System.out.println("Visiting head of node: " + node.nodeName());
            }

            @Override
            public void tail(Node node, int depth) {
                System.out.println("Visiting tail of node: " + node.nodeName());
                count++;
            }
        });

        traversor.traverse(root);
        Assert.assertEquals(6, count); // 4 nodes, 1 head and 1 tail for each
    }

    @Test
    public void testNodeCreationWithNullArguments() {
        try {
            new Node(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNodeCreationWithEmptyAttributes() {
        new Node("http://example.com", new Attributes());
    }

    @Test
    public void testNodeCreationWithNullBaseUri() {
        try {
            new Node(null, new Attributes());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNodeCreationWithNullAttributes() {
        new Node("http://example.com", null);
    }

    @Test
    public void testNodeCreationWithEmptyList() {
        new Node();
    }
}