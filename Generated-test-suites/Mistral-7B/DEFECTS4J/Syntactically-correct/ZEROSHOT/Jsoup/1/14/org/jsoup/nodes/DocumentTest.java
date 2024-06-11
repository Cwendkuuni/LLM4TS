package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        Element html = document.select("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testGetHtmlElement() {
        Document document = Document.createShell("http://example.com");
        Element html = document.head();
        assertNull(html);
        html = document.body();
        assertNotNull(html);
    }

    @Test
    public void testGetHeadElement() {
        Document document = Document.createShell("http://example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testGetBodyElement() {
        Document document = Document.createShell("http://example.com");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testGetTitle() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
        document.title("New Title");
        title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("http://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://example.com");
        Element div = document.createElement("div");
        div.text("Text to be moved");
        document.body().appendChild(div);
        document.normalise();
        Element body = document.body();
        List<TextNode> textNodes = body.select("text()");
        assertEquals(1, textNodes.size());
        TextNode textNode = textNodes.get(0);
        assertEquals("Text to be moved", textNode.text());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://example.com");
        Element div = document.createElement("div");
        div.text("Text to be moved");
        document.body().appendChild(div);
        document.normalise();
        String outerHtml = document.outerHtml();
        String expected = "<html><head></head><body>Text to be moved</body></html>";
        assertEquals(expected, outerHtml);
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://example.com");
        document.body().text("Text to be set");
        String text = document.body().text();
        assertEquals("Text to be set", text);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Document(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTagName() {
        new Document("http://example.com").createElement("InvalidTagName");
    }
}