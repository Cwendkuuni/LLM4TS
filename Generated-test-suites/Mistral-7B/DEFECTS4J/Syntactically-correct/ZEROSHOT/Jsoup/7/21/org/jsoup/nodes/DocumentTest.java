package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document class
 */
public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        String baseUri = "https://example.com";
        Document document = new Document(baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(Tag.HTML, document.tagName());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "https://example.com";
        Document document = Document.createShell(baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(Tag.HTML, document.tagName());
        Assert.assertEquals(baseUri, document.baseUri());
        Element html = document.head();
        Assert.assertNotNull(html);
        Assert.assertEquals(Tag.HEAD, html.tagName());
        html = document.body();
        Assert.assertNotNull(html);
        Assert.assertEquals(Tag.BODY, html.tagName());
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "https://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "https://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
        document.title("New Test Title");
        Assert.assertEquals("New Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "https://example.com";
        Document document = Document.createShell(baseUri);
        Element element = document.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals(Tag.DIV, element.tagName());
        Assert.assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "https://example.com";
        Document document = Document.createShell(baseUri);
        Element head = document.head();
        head.appendText("Head Content");
        Element body = document.body();
        body.appendText("Body Content ");
        body.appendText("Body Content 2 ");
        document.normalise();
        Assert.assertEquals("Body Content ", document.body().firstTextChild().text());
        Assert.assertEquals("Head Content", head.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "https://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.findFirstElementByTagName("html", document);
        Assert.assertNotNull(html);
        Assert.assertEquals(Tag.HTML, html.tagName());
        Element head = document.findFirstElementByTagName("head", document);
        Assert.assertNotNull(head);
        Assert.assertEquals(Tag.HEAD, head.tagName());
        Element body = document.findFirstElementByTagName("body", document);
        Assert.assertNotNull(body);
        Assert.assertEquals(Tag.BODY, body.tagName());
        Element nonExistent = document.findFirstElementByTagName("nonExistent", document);
        Assert.assertNull(nonExistent);
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "https://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element title = document.appendElement("title").text("Test Title");
        Element body = document.appendElement("body").text("Test Body");
        document.title("New Test Title");
        document.normalise();
        Assert.assertEquals("<html><head><title>New Test Title</title></head><body>Test Body</body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        String baseUri = "https://example.com";
        Document document = Document.createShell(baseUri);
        Element body = document.body();
        body.text("Test Body");
        Assert.assertEquals("Test Body", document.text());
        document.title("New Test Title");
        Assert.assertEquals("Test Body\nNew Test Title", document.outerHtml());
    }
}