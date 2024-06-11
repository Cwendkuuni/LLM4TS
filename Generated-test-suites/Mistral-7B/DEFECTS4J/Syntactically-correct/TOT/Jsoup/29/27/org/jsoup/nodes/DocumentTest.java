package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        Element html = doc.head();
        assertNotNull(html);
        assertEquals("head", html.nodeName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testAccessors() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        Element body = doc.body();
        assertEquals(head, doc.head());
        assertEquals(body, doc.body());
        String title = doc.title();
        assertNull(title);
        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        assertNotNull(p);
        assertEquals("p", p.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.head().appendText("Test Heading");
        doc.appendText("Test Text");
        doc.body().appendText("Test Text");
        doc.normalise();
        Element head = doc.head();
        Element title = head.getElementsByTag("title").first();
        assertEquals("Test Heading", title.text());
        Element body = doc.body();
        Element text = body.getElementsByTag("text").first();
        assertNull(text);
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.findFirstElementByTagName("html", doc);
        assertNotNull(html);
        assertEquals("html", html.nodeName());
        Element head = doc.findFirstElementByTagName("head", doc);
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        Element title = doc.findFirstElementByTagName("title", doc);
        assertNull(title);
        Element body = doc.findFirstElementByTagName("body", doc);
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testClone() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        Document cloned = doc.clone();
        assertNotNull(cloned);
        assertEquals(doc.nodeName(), cloned.nodeName());
        assertNotSame(outputSettings, cloned.outputSettings());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Document(null);
    }
}