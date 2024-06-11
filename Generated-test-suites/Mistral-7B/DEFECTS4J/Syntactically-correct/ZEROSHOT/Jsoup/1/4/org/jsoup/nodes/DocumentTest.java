package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertFalse(doc.title().isEmpty());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.select("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();

        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
        assertEquals("", doc.title());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        Element head = doc.head();

        assertEquals("Test Title", doc.title());
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();

        assertNull(body);
        assertFalse(doc.title().isEmpty());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title 1");
        doc.title("Test Title 2");

        assertEquals("Test Title 2", doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");

        assertNotNull(p);
        assertEquals("p", p.nodeName());
        assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("http://example.com");
        doc.head().appendElement("p").text("Test Heading");
        doc.body().appendElement("p").text("Test Body 1");
        doc.body().appendElement("p").text("Test Body 2");
        doc.body().appendElement("p").text("Test Body 3");

        doc.normalise();

        List<Element> bodyElements = doc.body().select("p");
        assertEquals(3, bodyElements.size());
        String bodyText = doc.body().text();
        assertEquals("Test Body 1Test Body 2Test Body 3", bodyText.trim());
        assertEquals("Test Heading", doc.head().text().trim());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.head().appendElement("p").text("Test Heading");
        doc.body().appendElement("p").text("Test Body 1");
        doc.body().appendElement("p").text("Test Body 2");
        doc.body().appendElement("p").text("Test Body 3");

        String htmlString = doc.outerHtml();

        String expectedHtml = "<html><head><p>Test Heading</p></head><body><p>Test Body 1</p><p>Test Body 2</p><p>Test Body 3</p></body></html>";
        assertEquals(expectedHtml, htmlString);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Test Document");

        assertEquals("Test Document", doc.body().text().trim());
    }

    @Test
    public void testNormaliseEmptyDocument() {
        Document doc = new Document("http://example.com");
        doc.normalise();

        Element html = doc.select("html").first();
        assertNotNull(html);
        assertEquals("", html.text().trim());
    }
}