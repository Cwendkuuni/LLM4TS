package org.jsoup.nodes;

import static org.junit.Assert.*;

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
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEmpty(doc.title());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(0, doc.title().length());
    }

    @Test
    public void testGetHeadElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        assertEquals("head", head.tagName());
        assertNull(head.text());
    }

    @Test
    public void testGetBodyElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertNull(body.text());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title("Test Title 1");
        doc.title("Test Title 2");
        assertEquals("Test Title 2", doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testSetTitleNull() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title(null);
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element element = doc.createElement("div");
        assertEquals("div", element.tagName());
        assertEquals(baseUri, element.baseUri());
        assertNull(element.text());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element title = doc.createElement("title").text("Test Title");
        head.appendChild(title);
        Element body = doc.createElement("body").text("Test Body");

        html.appendChild(head);
        html.appendChild(body);

        doc.normalise();

        Element expectedTitle = doc.getElementsByTag("title").first();
        Element actualTitle = html.getElementsByTag("title").first();
        assertEquals(expectedTitle, actualTitle);

        Element expectedBody = doc.getElementsByTag("body").first();
        Element actualBody = html.getElementsByTag("body").first();
        assertEquals(expectedBody.text(), actualBody.text());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element title = doc.createElement("title").text("Test Title");
        head.appendChild(title);
        Element body = doc.createElement("body").text("Test Body");

        html.appendChild(head);
        html.appendChild(body);

        doc.normalise();

        String expectedHtml = "<html><head><title>Test Title</title></head><body>Test Body</body></html>";
        String actualHtml = doc.outerHtml();
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);

        Element body = doc.body();
        body.text("Test Body");

        String expectedText = "Test Body";
        String actualText = doc.text();
        assertEquals(expectedText, actualText);
    }

}