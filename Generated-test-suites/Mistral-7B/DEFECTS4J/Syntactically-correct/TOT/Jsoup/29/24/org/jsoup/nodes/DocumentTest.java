package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("#document", doc.nodeName());
        Assert.assertNull(doc.head());
        Assert.assertNull(doc.body());
        Assert.assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Assert.assertEquals("#document", doc.nodeName());
        Element html = doc.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());
        Element head = html.child(0);
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
        Element body = html.child(1);
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testElementAccessors() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        Assert.assertEquals("Test Title", doc.title());
        Assert.assertNull(doc.title("New Title"));
        Assert.assertEquals("Test Title", doc.title());
        Element head = doc.head();
        Assert.assertNotNull(head);
        Element body = doc.body();
        Assert.assertNotNull(body);
    }

    @Test
    public void testElementCreation() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        Assert.assertEquals("p", p.tagName());
        Assert.assertNull(p.text());
        Assert.assertNull(p.parent());
        Assert.assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Text <b>in body</b></p><p>Text in head</p></body></html>", "http://example.com");
        doc.normalise();
        Element body = doc.body();
        Element title = doc.getElementsByTag("title").first();
        Assert.assertNull(title.parent());
        Assert.assertEquals("Text in body", body.text());
        Assert.assertEquals(0, body.childElementCount());
        Assert.assertEquals("Test Title", title.text());
        Assert.assertEquals(1, title.childElementCount());
        Assert.assertEquals("", title.get(0).text());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Document(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyBaseUri() {
        new Document("");
    }
}