package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.helper.Attributes;
import org.jsoup.helper.Validate;
import org.junit.Test;

/**
 * Test class for Node.java
 *
 * @author John Doe
 */
public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("node1", node1.nodeName());
        assertEquals("https://example.com", node1.baseUri());
        assertNotNull(node1.attributes());
        assertEquals("node1", node1.attr("id"));
        assertEquals("", node1.attr("nonExistentAttr"));

        assertEquals(null, node2.nodeName());
        assertEquals("https://example.com", node2.baseUri());
        assertNull(node2.attributes());

        assertEquals(null, node3.nodeName());
        assertEquals(null, node3.baseUri());
        assertNull(node3.attributes());
    }

    @Test
    public void testAttributeGettersAndSetters() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "testClass");
        Node node = new Node(baseUri, attributes);

        assertEquals("id", node.attr("id"));
        assertEquals("testClass", node.attr("class"));
        assertEquals("", node.attr("nonExistentAttr"));

        node.attr("id", "newId");
        assertEquals("newId", node.attr("id"));

        node.attr("class", "newClass");
        assertEquals("newClass", node.attr("class"));

        node.removeAttr("class");
        assertEquals("", node.attr("class"));

        assertFalse(node.hasAttr("nonExistentAttr"));
        assertTrue(node.hasAttr("id"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "https://example.com";
        Node node = new Node(baseUri, new Attributes());
        node.attr("href", "/relative/path.html");

        assertEquals("https://example.com/relative/path.html", node.absUrl("href"));
        assertEquals("", node.absUrl("nonExistentAttr"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        new Node(null, new Attributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAttributes() {
        new Node("https://example.com", null);
    }

    @Test
    public void testChildNodes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Node(baseUri, attributes);

        Node child1 = new Node(baseUri, new Attributes());
        Node child2 = new Node(baseUri, new Attributes());

        parent.addChildren(child1, child2);

        List<Node> childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(child1, childNodes.get(0));
        assertEquals(child2, childNodes.get(1));

        Node child3 = new Node(baseUri, new Attributes());
        parent.replaceChild(child1, child3);
        childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(child3, childNodes.get(0));
        assertEquals(child2, childNodes.get(1));

        parent.removeChild(child2);
        childNodes = parent.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(child3, childNodes.get(0));

        List<Node> childrenArray = parent.childNodesAsArray();
        assertEquals(2, childrenArray.length);
        assertEquals(child1, childrenArray[0]);
        assertEquals(child2, childrenArray[1]);

        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNodes().get(0));
        assertEquals(child2, parent.childNodes().get(1));
    }

    @Test
    public void testParentNodeAndOwnerDocument() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Node(baseUri, attributes);
        Node child = new Node(baseUri, new Attributes());

        child.setParentNode(parent);
        assertEquals(parent, child.parent());
        assertEquals(parent, child.ownerDocument());

        parent.addChildren(child);
        assertEquals(1, parent.childNodes().size());
        assertEquals(child, parent.childNodes().get(0));

        List<Node> childNodes = parent.childNodes();
        childNodes.remove(0);
        assertEquals(0, parent.childNodes().size());
        assertEquals(null, child.parent());
        assertEquals(null, child.ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, new Attributes());
        Node child2 = new Node(baseUri, new Attributes());

        parent.addChildren(child1, child2);

        parent.remove();
        assertEquals(0, child1.childNodes().size());
        assertEquals(0, child2.childNodes().size());
        assertEquals(null, child1.parent());
        assertEquals(null, child2.parent());
        assertEquals(null, child1.ownerDocument());
        assertEquals(null, child2.ownerDocument());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, new Attributes());
        Node child2 = new Node(baseUri, new Attributes());

        parent.addChildren(child1, child2);

        Node newChild = new Node(baseUri, new Attributes());
        parent.replaceWith(newChild);

        assertEquals(1, parent.childNodes().size());
        assertEquals(newChild, parent.childNodes().get(0));
        assertEquals(null, child1.parent());
        assertEquals(null, child2.parent());
    }

    @Test
    public void testSiblingIndex() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node parent = new Node(baseUri, attributes);
        Node sibling1 = new Node(baseUri, new Attributes());
        Node sibling2 = new Node(baseUri, new Attributes());
        Node testNode = new Node(baseUri, new Attributes());

        parent.addChildren(sibling1, testNode, sibling2);

        assertEquals(0, sibling1.siblingIndex());
        assertEquals(1, testNode.siblingIndex());
        assertEquals(2, sibling2.siblingIndex());

        parent.removeChild(sibling1);
        assertEquals(-1, sibling1.siblingIndex());
        assertEquals(0, testNode.siblingIndex());
        assertEquals(1, sibling2.siblingIndex());

        parent.removeChild(testNode);
        assertEquals(-1, testNode.siblingIndex());
        assertEquals(0, sibling1.siblingIndex());
        assertEquals(1, sibling2.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.outerHtmlHead(new StringBuilder(), 0, new Document.OutputSettings());
        node.outerHtmlTail(new StringBuilder(), 0, new Document.OutputSettings());
        assertEquals("", node.outerHtml());

        attributes.put("id", "testNode");
        node.attr("class", "testClass");

        StringBuilder accum = new StringBuilder(32 * 1024);
        node.outerHtml(accum);
        String expectedOutput = "<html><head></head><body><node id=\"testNode\" class=\"testClass\"></node></body></html>";
        assertEquals(expectedOutput, accum.toString());

        Node child = new Node(baseUri, new Attributes());
        node.appendChild(child);

        accum.setLength(0);
        node.outerHtml(accum);
        expectedOutput += "\n<node>";
        assertEquals(expectedOutput, accum.toString());

        node.removeChild(child);
        accum.setLength(0);
        node.outerHtml(accum);
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testClone() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");

        Node original = new Node(baseUri, attributes);
        Node clone = original.clone();

        original.attr("class", "testClass");
        original.childNodes().add(new Node(baseUri, new Attributes()));

        assertEquals(1, original.childNodes().size());
        assertEquals(0, clone.childNodes().size());
        assertEquals("testNode", original.attr("id"));
        assertEquals("testNode", clone.attr("id"));
        assertEquals("", clone.attr("class"));
        assertEquals("testClass", original.attr("class"));
    }
}