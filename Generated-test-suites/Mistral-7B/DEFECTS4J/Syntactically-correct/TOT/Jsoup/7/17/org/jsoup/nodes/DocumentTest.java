package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

    @Test
    public void testCreateDocumentWithBaseUri() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
    }

    @Test
    public void testCreateShellDocumentWithBaseUri() {
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testGetTitleWithEmptyTitle() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElementWithTagName() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("a");
        Assert.assertEquals("a", element.tagName());
        Assert.assertNull(document.head(), "New element should not be a child of document yet");
        Assert.assertNull(document.body(), "New element should not be a child of document yet");
    }

    @Test
    public void testNormaliseDocument() {
        Document document = Jsoup.parse("<html><head><title>Test Title</title></head></html>");
        document.normalise();
        Assert.assertEquals("Test Title", document.title());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
        Element head = document.head();
        Element body = document.body();
        List<Node> headChildren = head.childNodes();
        List<Node> bodyChildren = body.childNodes();
        Assert.assertEquals(1, headChildren.size());
        Assert.assertEquals(1, bodyChildren.size());
        Assert.assertTrue(headChildren.get(0) instanceof Element);
        Assert.assertTrue(bodyChildren.get(0) instanceof TextNode);
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseDocumentWithNullDocument() {
        Document document = null;
        document.normalise();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDocumentWithNullBaseUri() {
        Document document = new Document(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShellDocumentWithNullBaseUri() {
        Document document = Document.createShell(null);
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = document.outputSettings();
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals("UTF-8", outputSettings.charset().name());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testSetOutputSettings() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = document.outputSettings();
        document.outputSettings()
                .escapeMode(Entities.EscapeMode.extended)
                .charset("ISO-8859-1")
                .prettyPrint(false)
                .indentAmount(3);
        Assert.assertEquals(Entities.EscapeMode.extended, document.outputSettings().escapeMode());
        Assert.assertEquals("ISO-8859-1", document.outputSettings().charset().name());
        Assert.assertFalse(document.outputSettings().prettyPrint());
        Assert.assertEquals(3, document.outputSettings().indentAmount());
    }
}