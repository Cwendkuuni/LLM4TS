package org.jsoup.nodes;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        Element html = doc.head();
        assertNotNull(html);
        assertEquals("head", html.tagName());
        Element head = doc.head();
        assertNotNull(head);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
        assertNull(doc.title()); // reset title
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
        assertNull(doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        assertNotNull(p);
        assertEquals("p", p.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        doc.appendChild(p);
        TextNode textNode = doc.createTextNode("Test Text");
        p.appendChild(textNode);
        doc.normalise();
        Element body = doc.body();
        assertEquals("Test Text", body.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        new Document(null);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.createElement("html");
        Element head = doc.createElement("head");
        Element body = doc.createElement("body");
        doc.appendChild(html);
        html.appendChild(head);
        html.appendChild(body);
        Element element = doc.findFirstElementByTagName("body", doc);
        assertEquals("body", element.tagName());
        element = doc.findFirstElementByTagName("head", doc);
        assertEquals("head", element.tagName());
        element = doc.findFirstElementByTagName("html", doc);
        assertEquals("html", element.tagName());
        element = doc.findFirstElementByTagName("p", doc);
        assertNull(element);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", outputSettings.charset().name());
        assertEquals(true, outputSettings.prettyPrint());
        outputSettings.prettyPrint(false);
        assertEquals(false, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }
}