package org.jsoup.nodes;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testAbstractNodeConstruction() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node1 = new Node(baseUri, attributes);
        assertNotNull(node1.nodeName());
        assertEquals("", node1.attr(""));
        assertEquals(baseUri, node1.baseUri());
        assertFalse(node1.hasAttr("class"));
        assertTrue(node1.attributes().isEmpty());

        Node node2 = new Node(baseUri);
        assertEquals(node1.getClass(), node2.getClass());
        assertNull(node2.attributes());
        assertNull(node2.parentNode());
    }

    @Test
    public void testAbstractNodeAttributeMethods() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        node.attr("id", "test");
        assertEquals("test", node.attr("class"));
        assertEquals("test", node.attributes().get("class"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("id"));
        assertEquals("id", node.attributes().keySet().toString());
        assertEquals("test", node.attr("id"));
        assertEquals("test", node.attributes().get("id"));
        assertEquals("", node.attr(""));
        assertEquals("", node.attributes().get(""));

        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
        assertTrue(node.attributes().isEmpty());

        node.setBaseUri("http://newtest.com");
        assertEquals("http://newtest.com", node.baseUri());
    }

    @Test
    public void testAbstractNodeAbsUrl() {
        String baseUri = "http://test.com";
        String relativeUrl = "relative.html";
        Node node = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "a";
            }
        };
        node.attr("href", relativeUrl);

        String expectedAbsUrl = "http://test.com/relative.html";
        String actualAbsUrl = node.absUrl("href");
        assertEquals(expectedAbsUrl, actualAbsUrl);

        String absoluteUrl = "http://test2.com/absolute.html";
        node.attr("href", absoluteUrl);
        assertEquals(absoluteUrl, node.absUrl("href"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractNodeNullBaseUri() {
        String baseUri = null;
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.setBaseUri(baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractNodeNullAttributeKey() {
        Node node = new Node("http://test.com", new Attributes());
        node.attr(null, "");
    }

    @Test
    public void testAbstractNodeChildNodeMethods() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Node childNode1 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };

        Node childNode2 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "span";
            }
        };

        parentNode.addChildren(childNode1, childNode2);
        assertEquals(2, parentNode.childNodes().size());
        assertEquals(childNode1, parentNode.childNode(0));
        assertEquals(childNode2, parentNode.childNode(1));
        assertEquals(childNode1, parentNode.childNodesAsArray()[0]);
        assertEquals(childNode2, parentNode.childNodesAsArray()[1]);

        parentNode.removeChild(childNode1);
        assertEquals(1, parentNode.childNodes().size());
        assertNull(parentNode.childNode(0));
        assertNull(parentNode.childNodesAsArray()[0]);

        parentNode.addChildren(childNode1, childNode2);
        Element el1 = new Element("p");
        el1.appendText("Hello");
        parentNode.replaceChild(childNode1, el1);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(el1, parentNode.childNode(0));
        assertEquals("Hello", el1.text());
    }

    @Test
    public void testAbstractNodeParentNodeMethods() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Node childNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };
        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parent());
        assertNull(childNode.ownerDocument());

        parentNode.remove();
        assertNull(childNode.parent());
    }

    @Test
    public void testAbstractNodeDocumentOwnerMethods() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Document doc = new Document() {
            @Override
            public String baseUri() {
                return baseUri;
            }
        };
        node.ownerDocument(doc);

        assertEquals(doc, node.ownerDocument());
        assertNull(node.parentNode());
    }

    @Test
    public void testAbstractNodeRemove() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Node childNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };
        parentNode.addChild(childNode);

        parentNode.remove();
        assertNull(childNode.parentNode());
    }

    @Test
    public void testAbstractNodeReplaceWith() {
        String baseUri = "http://test.com";
        Attributes attributes1 = new Attributes();
        attributes1.put("class", "old");
        Node node1 = new Node(baseUri, attributes1) {
            @Override
            public String nodeName() {
                return "p";
            }
        };

        Attributes attributes2 = new Attributes();
        attributes2.put("class", "new");
        Node node2 = new Node(baseUri, attributes2) {
            @Override
            public String nodeName() {
                return "p";
            }
        };
        Node parentNode = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "div";
            }
        };
        parentNode.addChild(node1);

        parentNode.replaceChild(node1, node2);
        assertEquals(node2, parentNode.childNode(0));
        assertNull(node1.parentNode());
    }

    @Test
    public void testAbstractNodeSetParentNode() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };
        Node parentNode = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        childNode.setParentNode(parentNode);
        assertEquals(parentNode, childNode.parentNode());
        assertEquals(childNode, parentNode.childNodes().get(0));
    }

    @Test
    public void testAbstractNodeSetSiblingIndex() {
        String baseUri = "http://test.com";
        Attributes attributes1 = new Attributes();
        attributes1.put("class", "old");
        Node node1 = new Node(baseUri, attributes1) {
            @Override
            public String nodeName() {
                return "p";
            }
        };

        Attributes attributes2 = new Attributes();
        attributes2.put("class", "new");
        Node node2 = new Node(baseUri, attributes2) {
            @Override
            public String nodeName() {
                return "p";
            }
        };
        Node parentNode = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "div";
            }
        };
        parentNode.addChild(node1);
        parentNode.addChild(node2);

        node1.setSiblingIndex(1);
        assertEquals(node2, parentNode.childNode(0));
        assertEquals(node1, parentNode.childNode(1));
    }

    @Test
    public void testAbstractNodeSiblingNodes() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Node node1 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };
        Node node2 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };
        Node node3 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };
        parentNode.addChildren(node1, node2, node3);

        List<Node> siblingNodes = parentNode.siblingNodes();
        assertEquals(3, siblingNodes.size());
        assertEquals(node1, siblingNodes.get(0));
        assertEquals(node2, siblingNodes.get(1));
        assertEquals(node3, siblingNodes.get(2));

        Node siblingNode = parentNode.nextSibling();
        if (siblingNode != null) {
            assertEquals(node2, siblingNode);
        } else {
            assertNull(siblingNode);
        }

        siblingNode = parentNode.previousSibling();
        if (siblingNode != null) {
            assertEquals(node1, siblingNode);
        } else {
            assertNull(siblingNode);
        }
    }

    @Test
    public void testAbstractNodeOuterHtml() {
        String baseUri = "http://test.com";
        String indentAmount = "4";
        Document.OutputSettings out = new Document.OutputSettings();
        out.indentAmount = Integer.parseInt(indentAmount);

        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node1 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }

            @Override
            protected void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
                accum.append("<div class=\"test\" id=\"div1\">");
            }

            @Override
            protected void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
                indent(accum, depth, out);
                accum.append("</div>");
            }
        };

        Node node2 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }

            @Override
            protected void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
                accum.append("<p class=\"test\">");
            }

            @Override
            protected void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
                indent(accum, depth, out);
                accum.append("</p>");
            }
        };

        Node node3 = new TextNode("Hello World!");
        node3.outerHtmlHead(accum, 0, out);
        node3.outerHtmlTail(accum, 0, out);

        parentNode.addChildren(node1, node2, node3);
        String expectedOuterHtml = "<div class=\"test\" id=\"div1\">" +
                "<p class=\"test\">" +
                "Hello World!" +
                "</p>" +
                "</div>";
        String actualOuterHtml = node1.outerHtml();
        assertEquals(expectedOuterHtml, actualOuterHtml);
    }
}