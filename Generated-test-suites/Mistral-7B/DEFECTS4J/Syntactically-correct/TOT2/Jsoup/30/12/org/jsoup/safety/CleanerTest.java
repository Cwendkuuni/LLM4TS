package org.jsoup.safety;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class CleanerTest {
    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.body().text()); // Check for empty body since the original document is not modified
    }

    // Test with an empty document
    @Test
    public void testCleanEmptyDocument() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.body().text());
    }

    // Test with a document containing only whitespace
    @Test
    public void testCleanWhitespace() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body>&nbsp;</body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.body().text());
    }

    // Test with a document containing comments
    @Test
    public void testCleanWithComments() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><!-- comment --></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.body().text());
    }

    // Test with a document containing invalid HTML (missing start tag)
    @Test
    public void testCleanWithMissingStartTag() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body<script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.body().text());
    }

    // Test with a document containing a script tag with an inline script
    @Test
    public void testCleanWithInlineScript() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script src='script.js' onload='alert('XSS')'></script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.body().text());
    }

    // Test with a document having an iframe tag
    @Test
    public void testCleanWithIframeTag() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><iframe src='iframe.html'></iframe></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.body().text());
    }

    // Test with a document having an image tag with a malformed URL
    @Test
    public void testCleanWithMalformedImageUrl() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><img src='image://localhost/image.jpg'></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.body().text());
    }

    // Test isValid(Document) method
    // Add test cases for isValid(Document) method here

    // Test copySafeNodes(Element, Element) method
    // Add test cases for copySafeNodes(Element, Element) method here

}

//