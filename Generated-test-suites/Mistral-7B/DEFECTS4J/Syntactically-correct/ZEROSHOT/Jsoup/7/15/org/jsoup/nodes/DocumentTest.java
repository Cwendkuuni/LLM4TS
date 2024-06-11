package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    /**
     * Test constructor Document(String baseUri).
     */
    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertEquals("http://example.com", document.baseUri());
        assertEquals(Tag.valueOf("#root"), document.tagName());
        assertTrue(document.hasParent());
        assertNull(document.head());
        assertNull(document.body());
        assertFalse(document.title().isEmpty());
        assertEquals("", document.title());
    }

    /**
     * Test method Document.createShell(String baseUri).
     */
    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertEquals("http://example.com", document.baseUri());
        assertEquals(Tag.HTML, document.head().tagName());
        assertEquals(Tag.HEAD, document.head().firstChild().tagName());
        assertEquals(Tag.BODY, document.body().tagName());
        assertNull(document.title());
    }

    /**
     * Test method Document#title().
     */
    @Test
    public void testTitle() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        assertEquals("Test", document.title());
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    /**
     * Test method Document#title(String title).
     */
    @Test
    public void testTitleString() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        document.title("Test");
        assertEquals("Test", document.title());
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    /**
     * Test method Document#createElement(String tagName).
     */
    @Test
    public void testCreateElement() {
        Document document = Jsoup.parse("<html><body></body></html>");
        Element newElement = document.createElement("a");
        assertEquals(Tag.A, newElement.tagName());
        assertNull(newElement.parent());
    }

    /**
     * Test method Document#normalise().
     */
    @Test
    public void testNormalise() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body><p>Inside<p>body</p></p></body></html>");
        document.normalise();
        Element body = document.body();
        Element title = document.title();
        assertEquals("Test", title.text());
        assertEquals(1, body.childNodes().size());
        assertEquals("Inside", body.childNodes().get(0).text());
    }

    /**
     * Test method Document#findFirstElementByTagName(String tag, Node node).
     */
    @Test
    public void testFindFirstElementByTagName() {
        Document document = Jsoup.parse("<html><body><div><p>First paragraph</p><p>Second paragraph</p></div></body></html>");
        Element div = document.body().firstChild();
        Element firstP = document.findFirstElementByTagName("p", div);
        Element secondP = document.findFirstElementByTagName("p", div);
        assertEquals(Tag.P, firstP.tagName());
        assertEquals(Tag.P, secondP.tagName());
        assertEquals("First paragraph", firstP.text());
        assertEquals("Second paragraph", secondP.text());
    }

    /**
     * Test method Document#outerHtml().
     */
    @Test
    public void testOuterHtml() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
        assertEquals("<html><head><title>Test</title></head><body><p>Test</p></body></html>", document.outerHtml());
    }

    /**
     * Test method Document#text(String text).
     */
    @Test
    public void testText() {
        Document document = Jsoup.parse("<html><body></body></html>");
        document.body().text("Test");
        assertEquals("Test", document.body().text());
    }

    /**
     * Test method Document#nodeName().
     */
    @Test
    public void testNodeName() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>");
        assertEquals("#document", document.nodeName());
    }
}