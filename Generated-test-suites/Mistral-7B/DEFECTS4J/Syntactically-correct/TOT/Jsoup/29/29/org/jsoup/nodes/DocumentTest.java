package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;

public class DocumentTest {

    @Test
    public void testCreateEmptyDocument() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("#root", doc.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertNull(doc.head());
        Assert.assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("http://example.com");
        Assert.assertEquals("#document", doc.nodeName());
        Assert.assertEquals("#root", doc.getElementsByTag("html").first().tagName());
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());
        Assert.assertNull(doc.getElementsByTag("title").first().text());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());
        Assert.assertEquals("Test Title", doc.getElementsByTag("title").first().text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element link = doc.createElement("a");
        Assert.assertEquals("a", link.tagName());
        Assert.assertEquals("http://example.com", link.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.head().appendText("Test Head");
        doc.body().appendText("Test Body 1");
        doc.appendText("Test Body 2");
        doc.normalise();
        Assert.assertEquals(3, doc.childNodeSize());
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(2, doc.body().childNodeSize());
        Assert.assertEquals("Test Body 1", doc.body().get(0).text());
        Assert.assertEquals("Test Body 2", doc.body().get(1).text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.findFirstElementByTagName("html", doc);
        Assert.assertEquals("#document", html.tagName());
        Element head = doc.findFirstElementByTagName("head", doc);
        Assert.assertEquals("#head", head.tagName());
        Element body = doc.findFirstElementByTagName("body", doc);
        Assert.assertEquals("#body", body.tagName());
    }

    @Test
    public void testClone() {
        Document originalDoc = new Document("http://example.com");
        OutputSettings originalSettings = originalDoc.outputSettings();
        Document clonedDoc = originalDoc.clone();
        OutputSettings clonedSettings = clonedDoc.outputSettings();
        Assert.assertNotSame(originalDoc, clonedDoc);
        Assert.assertNotSame(originalSettings, clonedSettings);
        Assert.assertEquals(originalSettings, clonedSettings);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = new OutputSettings();
        settings.prettyPrint(false);
        doc.outputSettings(settings);
        Assert.assertFalse(doc.prettyPrint());
    }
}