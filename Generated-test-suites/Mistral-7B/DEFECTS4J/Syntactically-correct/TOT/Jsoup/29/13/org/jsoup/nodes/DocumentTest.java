package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.head();
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Assert.assertEquals("#document", doc.nodeName());
        Assert.assertEquals("head", head.nodeName());
        Assert.assertEquals("body", body.nodeName());
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals(null, doc.title());
        Assert.assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.head();
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Assert.assertEquals("#document", doc.nodeName());
        Assert.assertEquals("html", html.nodeName());
        Assert.assertEquals("head", head.nodeName());
        Assert.assertEquals("body", body.nodeName());
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals(null, doc.title());
        Assert.assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.title(null);
        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element p = doc.createElement("p");
        Assert.assertEquals("p", p.nodeName());
        Assert.assertEquals(baseUri, p.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        doc.appendText("Hello World");
        doc.appendText(" Test ");
        doc.text("Goodbye World");
        doc.normalise();
        Element body = doc.body();
        Assert.assertEquals("Hello World Test Goodbye World", body.text().trim());
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameNull() {
        Document doc = new Document("");
        doc.findFirstElementByTagName(null, doc);
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameNullNode() {
        Document doc = new Document("");
        doc.findFirstElementByTagName("tag", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameInvalidTag() {
        Document doc = new Document("");
        doc.findFirstElementByTagName("invalidTag", doc);
    }

    @Test
    public void testOutputSettings() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        OutputSettings outputSettings = doc.outputSettings();
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals("UTF-8", outputSettings.charset().name());
        Assert.assertEquals(true, outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsClone() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        OutputSettings originalSettings = doc.outputSettings();
        OutputSettings clonedSettings = originalSettings.clone();
        Assert.assertEquals(originalSettings.escapeMode(), clonedSettings.escapeMode());
        Assert.assertEquals(originalSettings.charset(), clonedSettings.charset());
        Assert.assertEquals(originalSettings.prettyPrint(), clonedSettings.prettyPrint());
        Assert.assertEquals(originalSettings.indentAmount(), clonedSettings.indentAmount());
    }
}