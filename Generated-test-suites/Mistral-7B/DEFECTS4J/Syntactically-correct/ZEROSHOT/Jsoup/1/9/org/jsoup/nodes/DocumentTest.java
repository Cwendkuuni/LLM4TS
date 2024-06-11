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
 * Test file for Document class
 */
public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("#document", doc.nodeName());

        Element html = doc.head();
        assertEquals("html", html.tagName());

        Element head = html.child(0);
        assertEquals("head", head.tagName());

        Element body = doc.body();
        assertEquals("body", body.tagName());
    }

    @Test
    public void testGetTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");

        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title 1");
        doc.title("Test Title 2");

        assertEquals("Test Title 2", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.createElement("a");

        assertEquals("a", element.tagName());
        assertEquals("http://example.com", element.baseUri());

        doc.appendBody(element);

        assertEquals(1, doc.body().childSize());
        assertEquals(element, doc.body().child(0));
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        Element title = doc.head().appendElement("title");
        title.text("Test Title 1");
        Element textNode = doc.appendElement("text");
        textNode.text("Test Text");

        doc.normalise();

        assertEquals("Test Title 1", doc.title());
        assertEquals(1, doc.body().childSize());
        assertEquals("Test Text", doc.body().child(0).text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        Element title = doc.head().appendElement("title");
        title.text("Test Title 1");
        Element textNode = doc.appendElement("text");
        textNode.text("Test Text");

        doc.normalise();

        String outerHtml = doc.outerHtml();

        String expected = "<html><head><title>Test Title 1</title></head><body>" +
                "<text>Test Text</text></body></html>";

        assertEquals(expected, outerHtml);
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");

        doc.text("Test Text");

        assertEquals("Test Text", doc.body().text());
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidBaseUri() {
        new Document(null);
    }
}