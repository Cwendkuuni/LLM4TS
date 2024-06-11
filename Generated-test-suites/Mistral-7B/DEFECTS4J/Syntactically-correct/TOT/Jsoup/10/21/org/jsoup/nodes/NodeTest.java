package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link Node} class.
 */
public class NodeTest {

    private Document doc;
    private Element root;

    @Before
    public void setUp() {
        doc = new Document();
        root = doc.createElement("html");
        doc.appendChild(root);
    }

    @Test
    public void testAbstractNode() {
        Node node1 = new Node("baseUri1", new Attributes());
        Node node2 = new Node("baseUri2", new Attributes());

        assertEquals("baseUri1", node1.baseUri());
        assertEquals("baseUri2", node2.baseUri());

        node1.setBaseUri("baseUri3");
        assertEquals("baseUri3", node1.baseUri());

        String attrValue = node1.attr("attrKey");
        assertNull(attrValue);

        String absUrl = node1.absUrl("relativeUrl");
        assertNull(absUrl);

        node1.attr("attrKey", "value");
        attrValue = node1.attr("attrKey");
        assertEquals("value", attrValue);

        absUrl = node1.absUrl("relativeUrl");
        assertNull(absUrl);

        node1.removeAttr("attrKey");
        attrValue = node1.attr("attrKey");
        assertNull(attrValue);

        absUrl = node1.absUrl("relativeUrl");
        assertNull(absUrl);

        node1.addChildren(new TextNode("child1"));
        assertEquals(1, node1.childNodes().size());
        assertEquals("child1", node1.childNodes().get(0).outerHtml());

        Node child2 = new Node("child2");
        node1.addChildren(child2);
        assertEquals(2, node1.childNodes().size());
        assertEquals("child2", node1.childNodes().get(1).outerHtml());

        Node node3 = node1.clone();
        assertEquals(node1, node3);
        assertEquals(node1.hashCode(), node3.hashCode());

        node1.remove();
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testElementNode() {
        Element elem = new Element("div", new Attributes());
        elem.attr("id", "test");
        elem.attr("class", "testClass");

        assertEquals("div", elem.nodeName());
        assertEquals("id", elem.attr("id"));
        assertEquals("testClass", elem.attr("class"));

        elem.attr("id", "newId");
        assertEquals("newId", elem.attr("id"));

        String absUrl = elem.absUrl("href");
        assertNull(absUrl);

        elem.removeAttr("id");
        assertFalse(elem.hasAttr("id"));

        absUrl = elem.absUrl("href");
        assertNull(absUrl);

        elem.setTextContent("testContent");
        assertEquals("testContent", elem.outerHtml());

        elem.setBaseUri("baseUri");
        elem.addChildren(new TextNode("child1"));
        elem.addChildren(new Element("p", new Attributes()).setTextContent("child2"));

        assertEquals("baseUri/div", elem.baseUri());
        assertEquals(3, elem.childNodes().size());
        assertEquals("child1", elem.childNodes().get(0).outerHtml());
        assertEquals("child2", elem.childNodes().get(1).outerHtml());
        assertEquals("p", elem.childNodes().get(1).nodeName());
        assertEquals("child2", elem.childNodes().get(1).outerHtml());

        Node child3 = new Element("span", new Attributes()).setTextContent("child3");
        elem.addChildren(child3);
        assertEquals(4, elem.childNodes().size());
        assertEquals("child3", elem.childNodes().get(2).outerHtml());
        assertEquals("span", elem.childNodes().get(2).nodeName());

        elem.remove();
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testTextNode() {
        TextNode textNode = new TextNode("testText");

        assertEquals("testText", textNode.outerHtml());

        textNode.setBaseUri("baseUri");

        String absUrl = textNode.absUrl("textUrl");
        assertNull(absUrl);

        textNode.setTextContent("newText");
        assertEquals("newText", textNode.outerHtml());

        textNode.remove();
        assertEquals(0, doc.childNodes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException1() {
        new Node(null, new Attributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException2() {
        new Node("baseUri", null);
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedURLException() {
        Node node = new Node("baseUri", new Attributes());
        URL url = new URL("javascript:alert('test')");
        node.attr("href", url.toExternalForm());
    }
}