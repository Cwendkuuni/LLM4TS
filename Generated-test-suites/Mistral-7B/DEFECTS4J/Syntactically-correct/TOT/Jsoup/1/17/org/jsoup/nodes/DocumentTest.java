package org.jsoup.nodes;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = Document.createShell("https://example.com");
        assertEquals("https://example.com", doc.baseUri());
        assertTrue(doc.head() != null);
        assertTrue(doc.body() != null);
    }

    @Test
    public void testTitleAccessors() {
        Document doc = Document.createShell("https://example.com");
        String title = "Test Title";
        doc.title(title);
        assertEquals(title, doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("https://example.com");
        Element p = doc.createElement("p");
        assertEquals("p", p.tagName());
        assertEquals("https://example.com", p.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        doc.normalise();
        assertEquals("<html><head></head><body><title>Test</title></body></html>", doc.outerHtml());
    }

    @Test
    public void testTitleSettingNull() {
        Document doc = Document.createShell("https://example.com");
        doc.title(null);
        assertEquals("", doc.title());
    }

    @Test
    public void testTitleSettingEmptyString() {
        Document doc = Document.createShell("https://example.com");
        doc.title("");
        assertEquals("", doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleSettingNullTitle() {
        Document doc = Document.createShell("https://example.com");
        doc.title(null);
    }

    @Test
    public void testCreateShellWithNullBaseUri() {
        try {
            Document.createShell(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("baseUri is marked non-null but is null", e.getMessage());
        }
    }
}