package org.junit.test;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {
    @Test
    public void testDocumentConstruction() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.head();
        assertNotNull(html);
        Element head = html.head();
        assertNotNull(head);
        Element body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testBody() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleSetter() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element newElement = doc.createElement("a");
        assertNotNull(newElement);
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element div = doc.createElement("div");
        TextNode textNode = doc.createTextNode("Test Text");
        div.appendChild(textNode);
        doc.appendChild(div);

        doc.normalise();

        Element body = doc.body();
        TextNode bodyTextNode = body.getFirstTextNode();
        assertEquals("Test Text", bodyTextNode.text());
    }
}