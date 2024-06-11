package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
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
        assertEquals("#document", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
        assertNull(document.head());
        assertNull(document.body());
        assertTrue(document.title().isEmpty());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Element html = document.select("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
        assertTrue(document.title().isEmpty());
    }

    @Test
    public void testDocumentTitle() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testDocumentTitleSetNull() {
        Document document = Document.createShell("http://example.com");
        document.title(null);
        assertTrue(document.title().isEmpty());
    }

    @Test
    public void testDocumentTitleSetEmptyString() {
        Document document = Document.createShell("http://example.com");
        document.title("");
        assertTrue(document.title().isEmpty());
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("http://example.com");
        Element p = document.createElement("p");
        assertEquals("p", p.tagName());
        assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://example.com");
        Element div = document.createElement("div");
        TextNode textNode = document.createElement("TextNode");
        textNode.text("Test text");
        div.appendChild(textNode);
        document.body().appendChild(div);

        document.normalise();
        Element body = document.body();
        List<Node> bodyChildren = body.childNodes();
        assertFalse(bodyChildren.get(0) instanceof TextNode);
        assertEquals(1, bodyChildren.size());
        TextNode bodyTextNode = (TextNode) bodyChildren.get(0);
        assertEquals("Test text", bodyText.text());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://example.com");
        Element div = document.createElement("div");
        TextNode textNode = document.createElement("TextNode");
        textNode.text("Test text");
        div.appendChild(textNode);
        document.body().appendChild(div);

        document.normalise();

        assertEquals("<html><head></head><body>" + textNode.outerHtml() + "</body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://example.com");
        document.body().text("Test text");
        assertEquals("Test text", document.body().text());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleWithNullBaseUri() {
        new Document(null);
    }
}