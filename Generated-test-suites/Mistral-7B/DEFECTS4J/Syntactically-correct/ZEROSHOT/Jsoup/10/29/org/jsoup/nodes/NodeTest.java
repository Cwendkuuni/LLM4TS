package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

/**
 * Test class for Node.java
 *
 * @author John Doe
 */
public class NodeTest {

    @Test
    public void testCreateNode() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);
        assertEquals("node1", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
        assertFalse(node.hasAttr("class"));
        assertTrue(node.hasAttr("id"));
        assertEquals("", node.attr("class"));
        assertEquals("node1", node.attr("id"));
        assertNull(node.parent());
        assertFalse(node.hasChildren());
    }

    @Test
    public void testCreateNodeWithEmptyAttributes() {
        String baseUri = "http://example.com/";
        Node node = new Node(baseUri);
        assertEquals("", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
        assertFalse(node.hasAttr("class"));
        assertFalse(node.hasAttr("id"));
        assertNull(node.parent());
        assertFalse(node.hasChildren());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNodeNullBaseUri() {
        Attributes attributes = new Attributes();
        new Node(null, attributes);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNodeNullAttributes() {
        String baseUri = "http://example.com/";
        new Node(baseUri, null);
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        String newBaseUri = "http://example.net/";
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "myclass");
        Node node = new Node(baseUri, attributes);
        assertEquals("myclass", node.attr("class"));
        assertEquals("node1", node.attr("id"));
        assertEquals("", node.attr("non-existing-attr"));
        assertEquals("node1", node.attr("id"));

        node.attr("class", "newClass");
        assertEquals("newClass", node.attr("class"));

        node.attr("newAttribute", "value");
        assertTrue(node.hasAttr("newAttribute"));
        assertEquals("value", node.attr("newAttribute"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "myclass");
        Node node = new Node(baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("non-existing-attr"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "myclass");
        Node node = new Node(baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));

        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));

        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        String childNodeName = "child";
        Node childNode = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return childNodeName;
            }
        };
        parentNode.addChild(childNode);

        Node child = parentNode.childNode(0);
        assertEquals(childNodeName, child.nodeName());
        assertEquals(parentNode, child.parent());
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        String childNodeName1 = "child1";
        Node childNode1 = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return childNodeName1;
            }
        };
        parentNode.addChild(childNode1);

        String childNodeName2 = "child2";
        Node childNode2 = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return childNodeName2;
            }
        };
        parentNode.addChild(childNode2);

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        List<String> childNames = new ArrayList<>(children.size());
        for (Node child : children) {
            childNames.add(child.nodeName());
        }
        assertTrue(Arrays.asList(childNodeName1, childNodeName2).equals(childNames));
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        String childNodeName1 = "child1";
        Node childNode1 = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return childNodeName1;
            }
        };

        String childNodeName2 = "child2";
        Node childNode2 = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return childNodeName2;
            }
        };

        List<Node> children = new ArrayList<>();
        children.add(childNode1);
        children.add(childNode2);

        parentNode.addChildren(children.toArray(new Node[children.size()]));

        List<Node> parentChildren = parentNode.childNodes();
        assertEquals(2, parentChildren.size());
        List<String> childNames = new ArrayList<>(parentChildren.size());
        for (Node child : parentChildren) {
            childNames.add(child.nodeName());
        }
        assertTrue(Arrays.asList(childNodeName1, childNodeName2).equals(childNames));

        List<Node> grandChildren = parentNode.childNodes().get(0).childNodes();
        assertEquals(0, grandChildren.size());
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        String childNodeName1 = "child1";
        Node childNode1 = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return childNodeName1;
            }
        };
        parentNode.addChild(childNode1);

        String childNodeName2 = "child2";
        Node childNode2 = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return childNodeName2;
            }
        };
        parentNode.addChild(childNode2);

        parentNode.removeChild(childNode1);
        List<Node> parentChildren = parentNode.childNodes();
        assertEquals(1, parentChildren.size());
        List<String> childNames = new ArrayList<>(parentChildren.size());
        for (Node child : parentChildren) {
            childNames.add(child.nodeName());
        }
        assertEquals(childNodeName2, childNames.get(0));
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        String childNodeName1 = "child1";
        Node childNode1 = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return childNodeName1;
            }
        };
        parentNode.addChild(childNode1);

        String childNodeName2 = "child2";
        Node childNode2 = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return childNodeName2;
            }
        };
        parentNode.addChild(childNode2);

        parentNode.replaceChild(childNode1, childNode2);
        List<Node> parentChildren = parentNode.childNodes();
        assertEquals(1, parentChildren.size());
        List<String> childNames = new ArrayList<>(parentChildren.size());
        for (Node child : parentChildren) {
            childNames.add(child.nodeName());
        }
        assertEquals(childNodeName2, childNames.get(0));

        assertNull(childNode1.parent());
        assertNull(childNode2.parent());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("href", "relative.html");
        Node node = new Node(baseUri, attributes);
        String childUrl = node.absUrl("href");
        assertEquals("http://example.com/relative.html", childUrl);

        String absoluteUri = "http://example.com/absolute.html";
        Node absoluteNode = new Node(absoluteUri, new Attributes());
        String absoluteChildUrl = absoluteNode.absUrl("href");
        assertEquals(absoluteUri, absoluteChildUrl);
    }

    @Test
    public void testHasChildren() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        String childNodeName1 = "child1";
        Node childNode1 = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return childNodeName1;
            }
        };
        parentNode.addChild(childNode1);

        String childNodeName2 = "child2";
        Node childNode2 = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return childNodeName2;
            }
        };
        parentNode.addChild(childNode2);

        assertTrue(parentNode.hasChildren());
    }

    @Test
    public void testGetParent() {
        String baseUri = "http://example.com/";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();

        Node parentNode1 = new Node(baseUri, attributes1);

        String childNodeName1 = "child1";
        Node childNode1 = new Node(baseUri, attributes2) {
            @Override
            public String nodeName() {
                return childNodeName1;
            }
        };
        childNode1.setParentNode(parentNode1);

        String childNodeName2 = "child2";
        Node childNode2 = new Node(baseUri, attributes2) {
            @Override
            public String nodeName() {
                return childNodeName2;
            }
        };
        childNode2.setParentNode(parentNode1);

        Node child1 = parentNode1.childNode(0);
        Node child2 = parentNode1.childNode(1);

        assertEquals(parentNode1, child1.parent());
        assertEquals(parentNode1, child2.parent());
    }

    @Test
    public void testGetDocument() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        String childNodeName = "child";
        Node childNode = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return childNodeName;
            }
        };
        parentNode.addChild(childNode);

        Document document = new Document();
        childNode.setParentNode(document);

        Document documentFromNode = childNode.ownerDocument();
        assertEquals(document, documentFromNode);
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);

        String childNodeName1 = "child1";
        Node childNode1 = new Element("div") {
            @Override
            public String nodeName() {
                return childNodeName1;
            }

            @Override
            public void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
                accum.append("<").append(nodeName()).append(" id='child1'>");
            }

            @Override
            public void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
                accum.append("</").append(nodeName()).append(">");
            }
        };
        parentNode.addChild(childNode1);

        String childNodeName2 = "child2";
        Node childNode2 = new TextNode("Child 2 text");
        parentNode.addChild(childNode2);

        String expectedOuterHtml = "<div id='child1'><div>Child 2 text</div></div>";
        String actualOuterHtml = parentNode.outerHtml();
        assertEquals(expectedOuterHtml, actualOuterHtml);
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node originalNode = new Node(baseUri, attributes);

        Node clonedNode = originalNode.clone();
        assertEquals(originalNode.nodeName(), clonedNode.nodeName());
        assertEquals(originalNode.baseUri(), clonedNode.baseUri());
        assertEquals(originalNode.attributes(), clonedNode.attributes());
        assertEquals(originalNode.childNodes().size(), clonedNode.childNodes().size());

        List<Node> originalChildren = originalNode.childNodes();
        List<Node> clonedChildren = clonedNode.childNodes();

        for (int i = 0; i < originalChildren.size(); i++) {
            Node originalChild = originalChildren.get(i);
            Node clonedChild = clonedChildren.get(i);
            assertEquals(originalChild.nodeName(), clonedChild.nodeName());
            assertEquals(originalChild.baseUri(), clonedChild.baseUri());
            assertEquals(originalChild.attributes(), clonedChild.attributes());
        }
    }
}