package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class CleanerTest {
    // Previous test cases

    @Test
    public void testGetCleanDocument() {
        Document dirtyDocument = Document.fromString("<html><body><script>alert('XSS!')</script></body></html>");
        Whitelist whitelist = new Whitelist(); // empty whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.outerHtml()); // No script tags should remain
    }

    @Test
    public void testGetInvalidDocument() {
        Document dirtyDocument = Document.fromString("<html><body><script>alert('XSS!')</script></body></html>");
        Whitelist whitelist = new Whitelist(); // empty whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        assertFalse(cleaner.isValid(dirtyDocument));
        assertTrue(dirtyDocument.body().text().contains("alert('XSS!'"));
    }

    @Test
    public void testGetValidDocument() {
        Document dirtyDocument = Document.fromString("<html><body><h1>Hello, World!</h1></body></html>");
        Whitelist whitelist = new Whitelist(); // whitelist allowing h1 tag
        Cleaner cleaner = new Cleaner(whitelist);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><h1>Hello, World!</h1></body></html>", cleanDocument.outerHtml());
        assertTrue(cleanDocument.body().text().contains("Hello, World!"));
    }

    // Complete JUnit test file
}