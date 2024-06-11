package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * JavaDocTest for Document class.
 */
public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHeadElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element head = doc.head();
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBodyElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element body = doc.body();
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitleElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        String title = "Test Title";
        doc.title(title);
        assertEquals(title, doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element newElement = doc.createElement("a");
        assertEquals("a", newElement.tagName());
        assertEquals(baseUri, newElement.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.body().appendText("Text before normalize");
        doc.head().appendElement(new Element("test", ""));
        doc.head().appendText("Text in head before normalize");

        doc.normalise();

        Element body = doc.body();
        TextNode textNode = (TextNode) body.child(0);
        assertEquals("Text before normalize ", textNode.text());

        Element testElement = doc.head().selectFirst("test");
        assertNotNull(testElement);
    }

    @Test(expected = NullPointerException.class)
    public void testTitleWithNullTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        doc.title(null);
    }
}