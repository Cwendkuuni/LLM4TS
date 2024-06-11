package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Whitelist;
import org.junit.Test;

public class CleanerTest {
    // Test cleaner constructor
    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    // Test clean method with valid input
    @Test
    public void testCleanMethod() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.createFromString("<html><body><div>Test</div></body></html>", "http://example.com");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        String expected = "<html><body></body></html>";
        assertEquals(expected, cleanDocument.outerHtml());
    }

    // Test isValid method with invalid input
    @Test
    public void testIsValidMethod() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.createFromString("<html><body><div>Test</div></body></html>", "http://example.com");
        boolean result = cleaner.isValid(dirtyDocument);
        assertFalse(result);
    }

    // Test null document
    @Test
    public void testNullDocument() {
        // Test clean() method with null input
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = null;
        try {
            Document cleanDocument = cleaner.clean(dirtyDocument);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test isValid() method with null input
        boolean result = cleaner.isValid(dirtyDocument);
        assertFalse(result);
    }

    // Test empty whitelist
    @Test
    public void testEmptyWhitelist() {
        Whitelist emptyWhitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(emptyWhitelist);

        Document dirtyDocument = Document.createFromString("<html><body><div>Test</div></body></html>", "http://example.com");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        String expected = "<html><body></body></html>";
        assertEquals(expected, cleanDocument.outerHtml());

        // Test isValid() method with empty whitelist
        boolean result = cleaner.isValid(dirtyDocument);
        assertFalse(result);
    }

    // Test malformed HTML
    @Test
    public void testMalformedHTML() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createFromString("<html<body><div>Test</div></html>", "http://example.com");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        String expected = "<html><body></body></html>";
        assertEquals(expected, cleanDocument.outerHtml());
    }

    // Test clean method with empty document
    @Test
    public void testEmptyDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createEmpty();
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertTrue(cleanDocument.outerHtml().isEmpty());
    }

    // Test clean method with document without a body
    @Test
    public void testDocumentWithoutBody() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createFromString("<html></html>", "http://example.com");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        String expected = "<html></html>";
        assertEquals(expected, cleanDocument.outerHtml());
    }

    // Test clean method with document with unallowed tags
    @Test
    public void testUnallowedTags() {
        Whitelist whitelist = new Whitelist().addTags("div", "p");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS');</script></body></html>", "http://example.com");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        String expected = "<html><body></body></html>";
        assertEquals(expected, cleanDocument.outerHtml());
    }

    // Test clean method with document with unallowed attributes
    @Test
    public void testUnallowedAttributes() {
        Whitelist whitelist = new Whitelist().addTags("div", "p");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createFromString("<html><body><div onclick='alert(\"XSS\")'></div></body></html>", "http://example.com");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        String expected = "<html><body><div></div></body></html>";
        assertEquals(expected, cleanDocument.outerHtml());
    }

    // Test clean method with document with enforced attributes
    @Test
    public void testEnforcedAttributes() {
        Whitelist whitelist = new Whitelist().addTags("div", "p")
                .addAttribute("div", "class", "test-class");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createFromString("<html><body><div onclick='alert(\"XSS\")' class='test-class'>Test</div></body></html>", "http://example.com");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        String expected = "<html><body><div class='test-class'>Test</div></body></html>";
        assertEquals(expected, cleanDocument.outerHtml());
    }
}