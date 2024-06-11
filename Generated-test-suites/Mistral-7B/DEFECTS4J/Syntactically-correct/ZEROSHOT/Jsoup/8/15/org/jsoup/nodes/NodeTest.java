package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for Node.java
 */
public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals(baseUri, node1.baseUri());
        assertEquals(attributes, node1.attributes());
        assertEquals(baseUri, node2.baseUri());
        assertNull(node2.attributes());
        assertNull(node3.attributes());
    }

    @Test
    public void testAbstractMethods() {
        throw new RuntimeException("Abstract methods cannot be tested directly.");
    }

    @Test
    public void testAttr() {
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");

        Node node = new Element("testElement", "", attributes);
        String attrValue = node.attr("testKey");
        assertEquals("testValue", attrValue);

        // Test absUrl method
        String baseUri = "http://example.com";
        node.setBaseUri(baseUri);
        String relUrl = "relativeUrl";
        String absUrl = node.absUrl("testKey:relUrl");
        assertEquals(baseUri + "/relativeUrl", absUrl);

        // Test hasAttr method
        assertTrue(node.hasAttr("testKey"));
        assertFalse(node.hasAttr("nonExistentKey"));

        // Test removeAttr method
        node.removeAttr("testKey");
        assertFalse(node.hasAttr("testKey"));
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("parent", "", attributes).setTextNode("parent text");

        Node child1 = new Element("child1", "", attributes);
        Node child2 = new Element("child2", "", attributes);
        Node child3 = new TextNode("child3 text");

        parentNode.appendChild(child1);
        parentNode.appendChild(child2);
        parentNode.appendChild(child3);

        assertEquals(3, parentNode.childNodes().size());
        assertEquals(child1, parentNode.childNode(0));
        assertEquals(child2, parentNode.childNode(1));
        assertEquals(child3, parentNode.childNode(2));

        List<Node> childrenAsList = new ArrayList<>(3);
        childrenAsList.add(child1);
        childrenAsList.add(child2);
        childrenAsList.add(child3);
        assertArrayEquals(parentNode.childNodesAsArray(), childrenAsList.toArray(new Node[0]));

        parentNode.removeChild(child1);
        assertEquals(2, parentNode.childNodes().size());
        assertFalse(parentNode.hasChildNode(child1));
    }

    @Test
    public void testParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("parent", "", attributes).setTextNode("parent text");
        Node childNode = new Element("child", "", attributes);

        childNode.setParentNode(parentNode);
        assertEquals(parentNode, childNode.parent());
        assertNull(childNode.parent());

        parentNode.appendChild(childNode);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(childNode, parentNode.childNode(0));

        parentNode.removeChild(childNode);
        assertEquals(0, parentNode.childNodes().size());
        assertNull(childNode.parent());
    }

    @Test
    public void testDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("element", "", attributes).setTextNode("text");

        Document document = new Document();
        document.appendChild(node);
        assertEquals(document, node.ownerDocument());

        node.setBaseUri(baseUri);
        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("parent", "", attributes).setTextNode("parent text");
        Node nodeToRemove = new Element("nodeToRemove", "", attributes);

        parentNode.appendChild(nodeToRemove);
        assertEquals(1, parentNode.childNodes().size());

        parentNode.remove();
        assertEquals(0, parentNode.childNodes().size());
        assertNull(nodeToRemove.parent());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("parent", "", attributes).setTextNode("parent text");
        Node oldChild = new Element("oldChild", "", attributes);
        Node newChild = new Element("newChild", "", attributes);

        parentNode.appendChild(oldChild);
        parentNode.replaceChild(newChild, oldChild);

        assertEquals(1, parentNode.childNodes().size());
        assertEquals(newChild, parentNode.childNode(0));
        assertNull(oldChild.parent());
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("parent", "", attributes);
        Node child1 = new Element("child1", "", attributes);
        Node child2 = new Element("child2", "", attributes);

        parentNode.addChildren(child1, child2);
        assertEquals(2, parentNode.childNodes().size());
        assertEquals(child1, parentNode.childNode(0));
        assertEquals(child2, parentNode.childNode(1));
    }

    @Test
    public void testSiblingNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Element("parent", "", attributes).setTextNode("parent text");

        Node sibling1 = new Element("sibling1", "", attributes);
        Node nodeUnderTest = new Element("nodeUnderTest", "", attributes);
        Node sibling2 = new Element("sibling2", "", attributes);

        parentNode.appendChild(sibling1);
        parentNode.appendChild(nodeUnderTest);
        parentNode.appendChild(sibling2);

        List<Node> siblingNodes = nodeUnderTest.siblingNodes();
        assertEquals(2, siblingNodes.size());
        assertEquals(sibling1, siblingNodes.get(0));
        assertEquals(sibling2, siblingNodes.get(1));

        Node previousSibling = nodeUnderTest.previousSibling();
        assertEquals(sibling1, previousSibling);

        Node nextSibling = nodeUnderTest.nextSibling();
        assertEquals(sibling2, nextSibling);
    }

    @Test
    public void testIndent() {
        StringBuilder stringBuilder = new StringBuilder();
        int depth = 3;

        Node node = new Element("test", "", new Attributes());
        node.outerHtmlHead(stringBuilder, depth, null);
        node.outerHtmlTail(stringBuilder, depth, null);

        String indent = StringUtil.padding(depth * 4);
        String expectedOutput = "test\n" + indent + "<test>\n" + indent + indent + "</test>\n";
        assertEquals(expectedOutput, stringBuilder.toString());
    }
}