package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.CleaningVisitor;
import org.jsoup.safety.ElementMeta;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testCleanMethod() {
        // Given
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        String dirtyHtml = "<html><head><script>alert('XSS')</script></head><body><h1>Hello</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // When
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Then
        Element root = cleanDocument.body();
        assertEquals("Hello", root.text());
        assertNull(root.head());
    }

    @Test
    public void testCleanEmptyDocument() {
        // Given
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        // When
        Document cleanDocument = cleaner.clean(null);

        // Then
        assertNull(cleanDocument);
    }

    @Test
    public void testIsValidMethod() {
        // Given
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        String validHtml = "<html><body><h1>Hello</h1></body></html>";
        Document validDocument = Jsoup.parse(validHtml);

        String invalidHtml = "<html><body><h1>Hello</h1><script>alert('XSS')</script></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);

        // When
        boolean isValid_valid = cleaner.isValid(validDocument);
        boolean isValid_invalid = cleaner.isValid(invalidDocument);

        // Then
        assertTrue(isValid_valid);
        assertFalse(isValid_invalid);
    }

    @Test
    public void testIsValidNOBodyTag() {
        // Given
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        String invalidHtmlWithoutBody = "<html><head></html>";
        Document invalidDocumentWithoutBody = Jsoup.parse(invalidHtmlWithoutBody);

        String validHtmlWithoutBody = "<html></html>";
        Document validDocumentWithoutBody = Jsoup.parse(validHtmlWithoutBody);

        // When
        boolean isValid_invalid = cleaner.isValid(invalidDocumentWithoutBody);
        boolean isValid_valid = cleaner.isValid(validDocumentWithoutBody);

        // Then
        assertFalse(isValid_invalid);
        assertNull(isValid_valid); // The method should return null if the document is empty.
    }

    @Test
    public void testIsValidMixedTags() {
        // Given
        Whitelist whitelist = Whitelist.simpleText();
        whitelist.addTags("p");

        Cleaner cleaner = new Cleaner(whitelist);

        String mixedHtml = "<html><head></head><body><h1>Disallowed</h1><p>Allowed</p><script>alert('XSS')</script></body></html>";
        Document mixedDocument = Jsoup.parse(mixedHtml);

        // When
        boolean isValid = cleaner.isValid(mixedDocument);

        // Then
        assertFalse(isValid);
    }

    @Test
    public void testCleanFramesetDocument() {
        // Given
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        String framesetHtml = "<html><head></head><body><frameset rows=\"50%,50%\"></frameset></body></html>";
        Document framesetDocument = Jsoup.parse(framesetHtml);

        // When
        Document cleanDocument = cleaner.clean(framesetDocument);

        // Then
        Element root = cleanDocument.body();
        assertNull(root.getElementsByTag("frameset").first());
    }

    // Add any additional test cases here.
}