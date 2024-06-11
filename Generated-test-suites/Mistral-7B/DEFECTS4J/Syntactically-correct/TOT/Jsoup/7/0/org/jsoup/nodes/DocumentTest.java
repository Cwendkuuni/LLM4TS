package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertNull(doc.head());
        Assert.assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());

        Element head = doc.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());

        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testGetTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");

        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title(null);
        doc.title("Test Title");

        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test(expected = NullPointerException.class)
    public void testSetTitleNull() {
        Document doc = Document.createShell("http://example.com");
        doc.title(null);
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element p = doc.createElement("p");

        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("", p.outerHtml());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        doc.body().appendText("Test Body");
        Element p = doc.createElement("p").text("Test Paragraph");
        doc.body().appendElement(p);

        doc.normalise();

        Assert.assertEquals("html\n<head>\n<title>Test Title</title>\n</head>\n<body>\nTest Body\nTest Paragraph\n</body>\n", doc.outerHtml());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        Element head = doc.findFirstElementByTagName("head", html);
        Assert.assertSame(html, head);

        Element body = doc.findFirstElementByTagName("body", doc);
        Assert.assertNotNull(body);

        Element p = doc.findFirstElementByTagName("p", doc);
        Assert.assertNull(p);
    }

    @Test
    public void testTextDocument() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Test Text");

        Assert.assertEquals("Test Text", doc.text().get());
    }

    @Test
    public void testOuterHtmlDocument() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Test Text");

        Assert.assertEquals("<html><head><title></title></head><body>\nTest Text\n</body>\n</html>", doc.outerHtml());
    }
}