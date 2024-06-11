package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.jsoup.nodes.attributes.IBaseURI;
import org.jsoup.helper.ElementUtil;
import org.jsoup.helper.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for Node class.
 */
public class NodeTest {

    private Node node;
    private Node parentNode;
    private Document doc;

    @Before
    public void setUp() throws Exception {
        String baseUri = "http://example.com";
        Attributes attrs = new AttributesImpl();
        attrs.put("class", "test");
        node = new Element("div", baseUri, attrs);
        parentNode = new Element("body", baseUri);
        doc = new Document(baseUri);
        doc.setBody(parentNode);
        parentNode.appendChild(node);
    }

    @Test
    public void testNodeName() {
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        node.attr("test", "testValue");
        assertEquals("testValue", node.attr("test"));
    }

    @Test
    public void testHasAttr() {
        node.attr("class", "test");
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("class", "test");
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        node.attr("href", "test.html");
        String absUrl = node.absUrl("href");
        URL url = new URL(baseUri, absUrl);
        assertEquals("http://example.com/test.html", url.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbsUrlWithEmptyAttributeValue() {
        node.attr("abs:href", "");
        node.absUrl("abs:href");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbsUrlWithInvalidAttributeName() {
        node.attr("abs:invalid", "value");
        node.absUrl("abs:invalid");
    }

    @Test
    public void testChildNode() {
        Element child = new Element("p", baseUri);
        node.appendChild(child);
        Node childNode = node.childNode(0);
        assertSame(child, childNode);
    }

    @Test
    public void testChildren() {
        Element child1 = new Element("p", baseUri);
        Element child2 = new Element("span", baseUri);
        node.appendChild(child1);
        node.appendChild(child2);
        List<Node> children = node.childNodes();
        assertEquals(2, children.size());
        assertSame(child1, children.get(0));
        assertSame(child2, children.get(1));
    }

    @Test
    public void testParent() {
        assertNull(node.parent());
        node.setParentNode(parentNode);
        assertSame(parentNode, node.parent());
    }

    @Test
    public void testOwnerDocument() {
        assertNull(node.ownerDocument());
        node.setOwnerDocument(doc);
        assertSame(doc, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Element child = new Element("p", baseUri);
        node.appendChild(child);
        node.remove();
        assertFalse(parentNode.hasChild(node));
    }

    @Test
    public void testReplaceWith() {
        Element child = new Element("p", baseUri);
        node.appendChild(child);
        Element replacement = new Element("span", baseUri);
        node.replaceWith(replacement);
        assertFalse(parentNode.hasChild(child));
        assertSame(replacement, node.getNextSibling());
    }

    @Test
    public void testSetParentNode() {
        Element newParent = new Element("ul", baseUri);
        node.setParentNode(newParent);
        assertSame(newParent, node.parent());
        newParent.appendChild(node);
        assertSame(newParent, node.ownerDocument().queryFirst(new NodeTraversor(new TagNameFilter("ul"))).parent());
    }

    @Test
    public void testNodeTraversal() {
        Element child1 = new Element("p", baseUri);
        Element child2 = new Element("span", baseUri);
        node.appendChild(child1);
        node.appendChild(child2);
        Node next = node;
        List<Node> nodes = new ArrayList<>();
        nodes.add(node);
        for (int i = 0; i < 5; i++) {
            next = next.nextSibling();
            if (next == null) {
                break;
            }
            nodes.add(next);
        }
        List<Node> expected = Arrays.asList(child1, child2, parentNode);
        List<Node> actual = ElementUtil.toList(node.siblingNodes());
        assertEquals(expected, nodes);
    }

    @Test
    public void testBaseUri() {
        node.setBaseUri("http://base.com/");
        String absUrl = node.absUrl("href");
        URL url = new URL(node.baseUri(), absUrl);
        assertEquals("http://base.com/test.html", url.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseUriWithEmptyString() {
        node.setBaseUri("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseUriWithNull() {
        node.setBaseUri(null);
    }

    @Test
    public void testAttributes() {
        node.attr("test", "testValue");
        Attributes attributes = node.attributes();
        assertNotNull(attributes);
        assertEquals("testValue", attributes.get("test"));
    }

    @Test
    public void testSetAttributes() {
        Attributes attributes = new AttributesImpl();
        attributes.put("test", "testValue");
        node.setAttributes(attributes);
        assertEquals("testValue", node.attr("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testSetAttributesWithNullAttributes() {
        node.setAttributes(null);
    }

    @Test
    public void testSetAttributesMergeEmpty() {
        Attributes attributes = new AttributesImpl();
        node.setAttributes(attributes);
        node.attr("test", "testValue");
        attributes.put("test", "newValue");
        node.setAttributes(attributes);
        assertEquals("newValue", node.attr("test"));
    }

    @Test
    public void testSetAttributesMergeNotEmpty() {
        Attributes attributes = new AttributesImpl();
        attributes.put("test", "testValue");
        node.setAttributes(attributes);
        node.setAttributes(new AttributesImpl().put("test", "newValue"));
        assertEquals("newValue", node.attr("test"));
    }

    @Test
    public void testSetAttributesReplaceExisting() {
        Attributes attributes = new AttributesImpl();
        attributes.put("test", "testValue");
        node.setAttributes(attributes);
        node.setAttributes(new AttributesImpl().put("test", "newValue"));
        assertEquals("newValue", node.attr("test"));
    }

    @Test
    public void testSetAttributesAddNew() {
        Attributes attributes = new AttributesImpl();
        attributes.put("test", "testValue");
        node.setAttributes(attributes);
        node.setAttributes(new AttributesImpl().put("newTest", "newValue"));
        assertTrue(node.hasAttr("newTest"));
        assertEquals("newValue", node.attr("newTest"));
    }

    @Test
    public void testSetAttributesReplaceAll() {
        Attributes attributes = new AttributesImpl();
        attributes.put("test", "testValue");
        node.setAttributes(attributes);
        Attributes newAttributes = new AttributesImpl();
        node.setAttributes(newAttributes);
        assertFalse(node.hasAttr("test"));
        assertTrue(newAttributes.size() > 0);
    }

    @Test
    public void testSetSiblingIndex() {
        Element child1 = new Element("p", baseUri);
        Element child2 = new Element("span", baseUri);
        node.appendChild(child1);
        node.appendChild(child2);
        node.setSiblingIndex(1);
        assertSame(child2, node.previousSibling());
        assertSame(child1, node.nextSibling());
    }

    @Test
    public void testToString() {
        assertEquals("<div/>", node.toString());
        node.attr("class", "test");
        node.attr("id", "testId");
        assertEquals("<div class=\"test\" id=\"testId\"/>", node.toString());
    }

    @Test
    public void testOuterHtml() {
        StringBuilder html = new StringBuilder(128);
        node.outerHtml(html);
        String expected = "<div class=\"test\"></div>";
        assertEquals(expected, html.toString());
    }

    @Test
    public void testOuterHtmlWithElements() {
        StringBuilder html = new StringBuilder(128);
        Element child1 = new Element("p", baseUri);
        Element child2 = new Element("span", baseUri);
        node.appendChild(child1);
        node.appendChild(child2);
        node.outerHtml(html);
        String expected = "<div class=\"test\"><p></p><span></span></div>";
        assertEquals(expected, html.toString());
    }

    @Test
    public void testOuterHtmlWithTextNodes() {
        StringBuilder html = new StringBuilder(128);
        Element childText = new TextNode("testText");
        node.appendChild(childText);
        node.outerHtml(html);
        String expected = "<div class=\"test\"><textarea>testText</textarea></div>";
        assertEquals(expected, html.toString());
    }

    @Test
    public void testOuterHtmlWithScriptNodes() {
        StringBuilder html = new StringBuilder(128);
        Element script = new ScriptNode("testScript()");
        node.appendChild(script);
        node.outerHtml(html);
        String expected = "<div class=\"test\"><script>testScript()</script></div>";
        assertEquals(expected, html.toString());
    }

    @Test
    public void testOuterHtmlWithEmptyElements() {
        StringBuilder html = new StringBuilder(128);
        Element emptyElement = new Element("empty", baseUri);
        node.appendChild(emptyElement);
        node.outerHtml(html);
        String expected = "<div class=\"test\"><empty></empty></div>";
        assertEquals(expected, html.toString());
    }

    @Test
    public void testOuterHtmlWithEmptyTextNodes() {
        StringBuilder html = new StringBuilder(128);
        TextNode emptyText = new TextNode("");
        node.appendChild(emptyText);
        node.outerHtml(html);
        String expected = "<div class=\"test\"><textarea></textarea></div>";
        assertEquals(expected, html.toString());
    }

    @Test
    public void testOuterHtmlWithEmptyScriptNodes() {
        StringBuilder html = new StringBuilder(128);
        ScriptNode emptyScript = new ScriptNode("");
        node.appendChild(emptyScript);
        node.outerHtml(html);
        String expected = "<div class=\"test\"><script></script></div>";
        assertEquals(expected, html.toString());
    }

    @Test
    public void testOuterHtmlWithCommentNodes() {
        StringBuilder html = new StringBuilder(128);
        Element comment = new CommentNode("testComment");
        node.appendChild(comment);
        node.outerHtml(html);
        String expected = "<div class=\"test\"><!--testComment--></div>";
        assertEquals(expected, html.toString());
    }

    @Test
    public void testOuterHtmlWithSelfClosingElements() {
        StringBuilder html = new StringBuilder(128);
        Element img = new Element("img", baseUri);
        img.attr("src", "test.jpg");
        img.attr("alt", "testImage");
        node.appendChild(img);
        node.outerHtml(html);
        String expected = "<div class=\"test\"><img src=\"test.jpg\" alt=\"testImage\"/></div>";
        assertEquals(expected, html.toString());
    }

    @Test
    public void testOuterHtmlWithAttributesUsingAbsPrefix() {
        StringBuilder html = new StringBuilder(128);
        Element link = new Element("a", baseUri);
        link.attr("abs:href", "test.html");
        node.appendChild(link);
        node.outerHtml(html);
        String expected = "<div class=\"test\"><a href=\"test.html\"></a></div>";
        assertEquals(expected, html.toString());
    }

    @Test
    public void testOuterHtmlWithBaseUriAsAbsoluteUrl() {
        StringBuilder html = new StringBuilder(128);
        node.setBaseUri("http://example.com/");
        Element img = new Element("img", baseUri);
        img.attr("src", "test.jpg");
        node.appendChild(img);
        node.outerHtml(html);
        String expected = "<div class=\"test\"><img src=\"http://example.com/test.jpg\"/></div>";
        assertEquals(expected, html.toString());
    }
}