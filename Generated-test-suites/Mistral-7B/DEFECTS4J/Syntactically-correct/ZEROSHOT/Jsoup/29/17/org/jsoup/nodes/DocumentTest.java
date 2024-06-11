package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java.
 */
public class DocumentTest {

    /**
     * Test constructor.
     */
    @Test
    public void testDocumentConstruction() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("#root", document.nodeName());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
    }

    /**
     * Test createShell method.
     */
    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("#document", document.nodeName());
        Element html = document.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.nodeName());
        Element head = html.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.nodeName());
        Element body = html.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName());
    }

    /**
     * Test head and body accessors.
     */
    @Test
    public void testHeadAndBodyAccessors() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.head();
        Element head = html.head();
        Element body = html.body();

        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
        Assert.assertEquals(head, document.head());
        Assert.assertEquals(body, document.body());
    }

    /**
     * Test title method.
     */
    @Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        String title = "Test Title";
        document.title(title);
        Assert.assertEquals(title, document.title());
    }

    /**
     * Test createElement method.
     */
    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element link = document.createElement("a");
        Assert.assertEquals("a", link.nodeName());
        Assert.assertEquals(baseUri, link.baseUri());
    }

    /**
     * Test normalise method.
     */
    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        document.appendElement("html").appendElement("body").appendText("Test Body");

        document.normalise();

        Element html = document.head();
        Element head = html.head();
        Element title = head.getElementsByTag("title").first();
        Element body = html.body();

        Assert.assertEquals(1, html.childNodes().size());
        Assert.assertEquals(1, head.childNodes().size());
        Assert.assertEquals(1, title.childNodes().size());
        Assert.assertEquals(1, body.childNodes().size());
        Assert.assertEquals("Test Title", title.text().trim());
        Assert.assertEquals("Test Body", body.childNodes().text().trim());
    }

    /**
     * Test findFirstElementByTagName method.
     */
    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.head();
        html.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        html.appendElement("html").appendElement("body").appendText("Test Body");

        Element title = document.findFirstElementByTagName("title", this);
        Assert.assertNotNull(title);
        Assert.assertEquals("title", title.nodeName());
        Assert.assertEquals("Test Title", title.text().trim());

        title = document.findFirstElementByTagName("body", this);
        Assert.assertNull(title);
    }

    /**
     * Test outerHtml method.
     */
    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
        document.appendElement("html").appendElement("body").appendText("Test Body");

        String html = document.outerHtml();
        Assert.assertEquals("<html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Test Title</title></head><body></body></html>", html);
    }

    /**
     * Test text method.
     */
    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.appendElement("html").appendElement("body").appendText("Test Body");

        String text = document.text("New Text");
        Assert.assertEquals("New Text", document.text().trim());
        Assert.assertEquals("Test Body\nNew Text", document.body().text().trim());
    }

    /**
     * Test nodeName method.
     */
    @Test
    public void testNodeName() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.head();
        Element head = html.head();
        Element title = head.getElementsByTag("title").first();

        Assert.assertEquals("#document", document.nodeName());
        Assert.assertEquals("html", html.nodeName());
        Assert.assertEquals("head", head.nodeName());
        Assert.assertEquals("title", title.nodeName());
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.head();
        Element head = html.head();
        Element title = head.getElementsByTag("title").first();

        OutputSettings outputSettings = document.outputSettings();
        OutputSettings clonedOutputSettings = outputSettings.clone();

        Document clonedDocument = document.clone();
        Document clonedDocumentWithoutSettings = document.clone();

        Assert.assertNotSame(outputSettings, clonedOutputSettings);
        Assert.assertEquals(document, clonedDocument);
        Assert.assertNotEquals(document, clonedDocumentWithoutSettings);
        Assert.assertEquals(outputSettings, clonedDocument.outputSettings());
    }

    /**
     * Test QuirksMode enum.
     */
    @Test
    public void testQuirksMode() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);

        Document documentNoQuirks = document.quirksMode(QuirksMode.noQuirks);
        Document documentQuirks = document.quirksMode(QuirksMode.quirks);
        Document documentLimitedQuirks = document.quirksMode(QuirksMode.limitedQuirks);

        Assert.assertEquals(QuirksMode.noQuirks, documentNoQuirks.quirksMode());
        Assert.assertEquals(QuirksMode.quirks, documentQuirks.quirksMode());
        Assert.assertEquals(QuirksMode.limitedQuirks, documentLimitedQuirks.quirksMode());
    }
}