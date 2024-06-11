package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;
    private Document dirtyDocument;
    private Document cleanDocument;

    @Before
    public void setUp() {
        // Create a new whitelist
        whitelist = new Whitelist.SimpleTextWhitelist();

        // Create a new cleaner with the whitelist
        cleaner = new Cleaner(whitelist);

        // Create a dirty document with some HTML code
        dirtyDocument = Document.createFromString("<html><head><script>alert('XSS')</script></head><body><h1 id='title'>Hello World</h1><p>This is a test.</p></body></html>", "http://example.com");
    }

    @Test
    public void testCreateCleaner() {
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        // Clean the document and check that the output is as expected
        cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        assertEquals("Hello World", body.getElementsByTag("h1").text());
        assertNull(body.getElementsByTag("script").first());
    }

    @Test
    public void testIsValidDocument() {
        // Check that the document is valid according to the whitelist
        assertTrue(cleaner.isValid(dirtyDocument));

        // Create a new document with an invalid tag
        Document invalidDocument = Document.createFromString("<html><body><script>alert('XSS')</script></body></html>", "http://example.com");
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        // Test copying safe nodes
        Element root = new Element(Tag.BODY, null, new Attributes());
        copySafeNodes(dirtyDocument.body(), root);
        Element safeElement = root.getElementsByTag("h1").first();
        assertEquals("Hello World", safeElement.text());

        // Test copying safe nodes with attributes
        Element elementWithAttributes = new Element(Tag.DIV, null, new Attributes().put("id", "testId").put("class", "testClass"));
        root.appendChild(elementWithAttributes);
        copySafeNodes(dirtyDocument.body(), root);
        Element safeElementWithAttributes = root.getElementsByTag("div").first();
        assertEquals("testId", safeElementWithAttributes.attributes().get("id"));
        assertEquals("testClass", safeElementWithAttributes.attributes().get("class"));
    }

    @Test
    public void testIsSafeTagAndAttribute() {
        // Test isSafeTag method
        assertTrue(cleaner.whitelist.isSafeTag("h1"));
        assertFalse(cleaner.whitelist.isSafeTag("script"));

        // Test isSafeAttribute method
        Element elementWithSafeAttribute = new Element("div", null, new Attributes().put("data-safe", "safeValue"));
        assertTrue(cleaner.whitelist.isSafeAttribute("div", dirtyDocument, elementWithSafeAttribute.tagName(), "data-safe", "safeValue"));

        Element elementWithUnsafeAttribute = new Element("div", null, new Attributes().put("data-xss", "XSS"));
        assertFalse(cleaner.whitelist.isSafeAttribute("div", dirtyDocument, elementWithUnsafeAttribute.tagName(), "data-xss", "XSS"));
    }
}