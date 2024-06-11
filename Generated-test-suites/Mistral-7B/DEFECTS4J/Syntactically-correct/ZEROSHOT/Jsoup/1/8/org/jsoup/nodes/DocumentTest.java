package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import org.junit.Test;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertTrue(doc instanceof Document);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertTrue(doc instanceof Document);

        Element html = doc.select("html").first();
        assertNotNull(html);
        assertEquals("html", html.tagName());

        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        assertEquals("head", head.tagName());

        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testHeadAccess() {
        Document doc = Document.createShell("baseUri");
        Element head = doc.head();

        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBodyAccess() {
        Document doc = Document.createShell("baseUri");
        Element body = doc.body();

        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitleAccess() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");

        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleSetting() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");

        Element titleEl = doc.getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("Test Title", titleEl.text().trim());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("baseUri");
        Element newElement = doc.createElement("a");

        assertNotNull(newElement);
        assertEquals("a", newElement.tagName());
        assertEquals("baseUri", newElement.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        doc.body().appendText("Test Body");
        doc.appendText("Test Before Body");

        doc.normalise();

        Element body = doc.body();
        String bodyText = body.text();
        assertEquals("Test BodyTest Title", bodyText);

        List<Element> titles = doc.select("title");
        assertEquals(1, titles.size());
        assertEquals("Test Title", titles.get(0).text().trim());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        doc.body().appendText("Test Body");

        String html = doc.outerHtml();
        assertTrue(html.contains("<html><head><title>Test Title</title></head><body>Test Body</body></html>"));
    }

    @Test
    public void testTextSetting() {
        Document doc = Document.createShell("baseUri");
        doc.text("Test Text");

        Element body = doc.body();
        String bodyText = body.text();
        assertEquals("Test Text", bodyText);
    }
}