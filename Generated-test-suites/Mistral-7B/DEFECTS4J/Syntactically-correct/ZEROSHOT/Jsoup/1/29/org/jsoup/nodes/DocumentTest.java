package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document class.
 */
public class DocumentTest {

    @Test
    public void testCreateEmptyDocument() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertNull(document.title());
        assertNull(document.body());
        assertFalse(document.head().hasText());
        assertFalse(document.body().hasText());
    }

    @Test
    public void testCreateShellDocument() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertNotNull(document.title());
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertFalse(document.title().isEmpty());
        assertFalse(document.head().hasText());
        assertFalse(document.body().hasText());
    }

    @Test
    public void testDocumentTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        Element titleElement = document.getElementsByTag("title").first();
        assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testDocumentSetTitle() {
        Document document = new Document("baseUri");
        document.title(null);
        assertFalse(document.title().isEmpty());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        Element titleElement = document.getElementsByTag("title").first();
        assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("p");
        assertEquals("p", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document document = Document.createShell("baseUri");
        document.title("Test Title");
        document.body().text("Test Body Text");
        document.head().appendElement("p").text("Head Text");
        document.body().appendChild(document.createElement("p")).text("Body Text 2");

        document.normalise();

        Element bodyElement = document.body();
        assertEquals("Test Body Text", bodyElement.text());
        assertEquals(2, bodyElement.childElementCount());
        Element titleElement = document.getElementsByTag("title").first();
        assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testGetElementsByTag() {
        Document document = Document.createShell("baseUri");
        document.title("Test Title");
        document.body().text("Test Body Text");
        document.head().appendElement("p").text("Head Text 1");
        document.head().appendElement("p").text("Head Text 2");
        document.body().appendChild(document.createElement("p")).text("Body Text 2");

        List<Element> elements = document.getElementsByTag("p");
        assertEquals(3, elements.size());
        Element headP1 = elements.get(0);
        assertEquals("Head Text 1", headP1.text());
        Element headP2 = elements.get(1);
        assertEquals("Head Text 2", headP2.text());
        Element bodyP = elements.get(2);
        assertEquals("Body Text 2", bodyP.text());
    }

    @Test(expected = NullPointerException.class)
    public void testGetElementsByTagNullUri() {
        Document document = new Document(null);
        document.getElementsByTag("p");
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("baseUri");
        document.title("Test Title");
        document.body().text("Test Body Text");
        document.head().appendElement("p").text("Head Text");
        document.body().appendChild(document.createElement("p")).text("Body Text 2");

        String html = document.outerHtml();
        String expectedHtml = "<html><head><title>Test Title</title><p>Head Text</p></head><body><p>Test Body Text</p><p>Body Text 2</p></body></html>";
        assertEquals(expectedHtml, html);
    }

    @Test
    public void testText() {
        Document document = new Document("baseUri");
        document.text("Test Text");
        assertEquals("Test Text", document.outerHtml());
    }
}