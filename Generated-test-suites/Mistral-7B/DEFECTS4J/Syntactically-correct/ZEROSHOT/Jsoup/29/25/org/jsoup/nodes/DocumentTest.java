package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    /**
     * Test constructor.
     */
    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());
    }

    /**
     * Test createShell method.
     */
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.head().tagName());
        assertNotNull(doc.body());
        assertNotNull(doc.body().tagName());
        assertNull(doc.title());
    }

    /**
     * Test head method.
     */
    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());

        TextNode headTitle = doc.createElement("title").text("Test Title");
        doc.head().appendChild(headTitle);
        assertEquals("Test Title", doc.title());
        assertEquals("Test Title", headTitle.text());
    }

    /**
     * Test body method.
     */
    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());

        TextNode bodyText = doc.createElement("p").text("Test Body");
        doc.body().appendChild(bodyText);
        assertEquals("Test Body", doc.body().text());
        assertEquals("Test Body", bodyText.text());
    }

    /**
     * Test title method.
     */
    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        Element title = doc.getElementsByTag("title").first();
        assertEquals("Test Title", title.text());
    }

    /**
     * Test createElement method.
     */
    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element p = doc.createElement("p");
        Element div = doc.createElement("div");

        doc.body().appendChild(p);
        doc.body().appendChild(div);

        TextNode pText = doc.createElement("p").text("Test Paragraph");
        p.appendChild(pText);
        TextNode divText = doc.createElement("p").text("Test Div");
        div.appendChild(divText);

        assertEquals("Test Paragraph", p.text());
        assertEquals("Test Div", div.text());
    }

    /**
     * Test normalise method.
     */
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        Element p1 = doc.createElement("p").text("Test Paragraph 1");
        Element p2 = doc.createElement("p").text("Test Paragraph 2");
        Element p3 = doc.createElement("p").text("Test Paragraph 3");
        Element div = doc.createElement("div");
        Element divText = doc.createElement("p").text("Test Div Text");

        doc.head().appendChild(p1);
        doc.head().appendChild(p2);
        doc.head().appendChild(p3);
        doc.body().appendChild(div);
        div.appendChild(divText);

        doc.normalise();

        Element head = doc.head();
        List<Element> headElements = new ArrayList<>(Arrays.asList(head.children()));
        assertEquals(1, headElements.size());
        Element body = doc.body();
        List<Element> bodyElements = new ArrayList<>(Arrays.asList(body.children()));
        assertEquals(1, bodyElements.size());

        Element bodyText = body.getElementsByTag("p").first();
        List<Node> bodyTextNodes = new ArrayList<>(Arrays.asList(bodyText.childNodes()));
        assertEquals(2, bodyTextNodes.size());
        TextNode p1Text = (TextNode) bodyTextNodes.get(0);
        assertEquals("Test Paragraph 1Test Paragraph 2Test Paragraph 3", p1Text.text());

        TextNode p2Text = (TextNode) bodyTextNodes.get(1);
        List<Node> p2TextNodes = new ArrayList<>(Arrays.asList(p2Text.childNodes()));
        assertEquals(1, p2TextNodes.size());
        TextNode divTextNode = (TextNode) p2TextNodes.get(0);
        assertEquals("Test Div Text", divTextNode.text());
    }
}