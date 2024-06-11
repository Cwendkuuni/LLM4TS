package org.jsoup.safety;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {
    @Test
    public void testCleanDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assertions.assertFalse(cleanDocument.body().hasMetaTags()); // Check if meta tags are removed
        Assertions.assertFalse(cleanDocument.body().hasText()); // Check if text is removed
        Assertions.assertTrue(cleanDocument.body().hasClass("mw-parser-output")); // Check if specific class is preserved
        Assertions.assertFalse(cleanDocument.body().select("script").size() > 0); // Check if script tags are removed
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document validDocument = Jsoup.parse("<html><body><p>Valid HTML.</p></body></html>");
        Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");

        Assertions.assertTrue(cleaner.isValid(validDocument));
        Assertions.assertFalse(cleaner.isValid(invalidDocument));
    }

    // Add test cases for edge cases and exception handling scenarios here

    @Test
    public void testCleanEmptyDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document emptyDocument = Jsoup.parse("<html></html>");
        Document cleanEmptyDocument = cleaner.clean(emptyDocument);

        Assertions.assertTrue(cleanEmptyDocument.body().text().isEmpty());
    }

    @Test
    public void testCleanDocumentWithInvalidWhitelist() {
        Cleaner cleaner = new Cleaner(null); // Null whitelist

        Document dirtyDocument = Jsoup.parse("<html><body></body></html>");

        Assertions.assertThrows(NullPointerException.class, () -> cleaner.clean(dirtyDocument));
    }

    @Test
    public void testCleanDocumentWithEmptyInput() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document emptyDocument = Jsoup.parse("");

        Document cleanEmptyDocument = cleaner.clean(emptyDocument);

        Assertions.assertNull(cleanEmptyDocument);
    }

    @Test
    public void testCreateSafeElementWithNullElement() {
        Cleaner cleaner = new Cleaner(new Whitelist());

        Assertions.assertNull(cleaner.createSafeElement(null));
    }

    // Add other test cases for edge cases and exception handling scenarios here

}