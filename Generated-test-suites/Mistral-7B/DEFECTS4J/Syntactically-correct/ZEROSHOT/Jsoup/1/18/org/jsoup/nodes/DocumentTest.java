package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
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
        // Test creating a new, empty Document
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertNull(document.head());
        assertNull(document.body());
    }

    @Test
    public void testCreateShell() {
        // Test creating a valid, empty shell of a document
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testGetHeadElement() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testGetBodyElement() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testGetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
        assertNull(document.getElementsByTag("title").first().text());
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);

        Element titleElement = document.getElementsByTag("title").first();
        String titleText = titleElement.text().trim();
        assertEquals("Test Title", titleText);
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element newElement = document.createElement("a");
        assertNotNull(newElement);
        assertEquals("a", newElement.tagName());
    }

    @Test
    public void testNormaliseDocument() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        document.head().appendElement("p").text("Test Head Text");
        document.body().appendElement("p").text("Test Body Text 1");
        document.body().appendText(" Test Body Text 2 ");

        document.normalise();

        List<Element> titleElements = document.getElementsByTag("title");
        assertEquals(1, titleElements.size());
        Element titleElement = titleElements.get(0);
        assertEquals("Test Title", titleElement.text().trim());

        List<Element> bodyElements = document.getElementsByTag("body");
        assertEquals(1, bodyElements.size());
        Element bodyElement = bodyElements.get(0);
        List<TextNode> textNodes = bodyElement.textNodes();
        assertEquals(2, textNodes.size());
        TextNode firstTextNode = textNodes.get(0);
        assertEquals("Test Body Text 1", firstTextNode.text());
        TextNode secondTextNode = textNodes.get(1);
        assertEquals(" Test Body Text 2 ", secondTextNode.text().trim());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        document.head().appendElement("p").text("Test Head Text");
        document.body().appendElement("p").text("Test Body Text 1");
        document.body().appendText(" Test Body Text 2 ");

        String outerHtml = document.outerHtml();
        String expectedOuterHtml = "<html><head><title>Test Title</title><p>Test Head Text</p></head><body><p>Test Body Text 1</p> Test Body Text 2 </body></html>";
        assertEquals(expectedOuterHtml, outerHtml);
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.body().text("Test Body Text");

        String text = document.body().text();
        assertEquals("Test Body Text", text);
    }
}