package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;

/**
 * Test file for Cleaner class
 */
public class CleanerTest {

    @Test
    public void testCleanerConstruction() {
        // Test constructor with valid whitelist
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerInvalidWhitelist() {
        // Test constructor with null whitelist
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCleanDocument() {
        // Test clean method with valid document
        String dirtyHtml = "<html><body><div id='test'><p>Hello World</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextAndSelfClosingHTMLTags());
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Test if cleaned document does not contain any discarded nodes
        Element root = cleanDocument.body();
        assertEquals(0, root.childNodes().size());
        assertTrue(cleanDocument.body().text().contains("Hello World"));
    }

    @Test
    public void testInvalidDocument() {
        // Test isValid method with valid document
        String dirtyHtml = "<html><body><div id='test'><p>Hello World</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextAndSelfClosingHTMLTags());
        assertTrue(cleaner.isValid(dirtyDocument));

        // Test isValid method with invalid document
        String invalidHtml = "<html><body><script>alert('XSS Attack')</script></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);
        Cleaner cleaner1 = new Cleaner(Whitelist.simpleTextAndSelfClosingHTMLTags());
        assertFalse(cleaner1.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        // Test copySafeNodes method with valid elements and attributes
        String dirtyHtml = "<html><body><div id='test' class='testClass'><p id='p1'>Hello World <span class='testClass' style='color:red'>Test</span></p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Cleaner cleaner = new Cleaner(Whitelist.simpleTextAndSelfClosingHTMLTags());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), Document.createShell(dirtyDocument.baseUri()).body());
        assertEquals(0, numDiscarded);

        // Test if copied elements and attributes have the same tag name, class, and style
        Element root = dirtyDocument.body();
        Element rootClean = Document.createShell(dirtyDocument.baseUri()).body();
        assertEquals(root.tagName(), rootClean.tagName());
        assertEquals(root.classes().size(), rootClean.classes().size());
        for (String className : root.classes()) {
            assertTrue(rootClean.classes().contains(className));
        }
        assertEquals(root.style().toString(), rootClean.style().toString());

        // Test copySafeNodes method with invalid elements and attributes
        String invalidHtml = "<html><body><div id='test' class='testClass'><p id='p1'>Hello World <script>alert('XSS Attack')</script></p></div></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);
        Cleaner cleaner1 = new Cleaner(Whitelist.simpleTextAndSelfClosingHTMLTags());
        int numDiscarded1 = cleaner1.copySafeNodes(invalidDocument.body(), Document.createShell(invalidDocument.baseUri()).body());
        assertTrue(numDiscarded1 > 0);
    }
}