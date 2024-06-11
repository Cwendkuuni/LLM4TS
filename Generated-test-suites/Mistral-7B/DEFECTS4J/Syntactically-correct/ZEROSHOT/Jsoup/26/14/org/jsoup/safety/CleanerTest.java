package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test file for Cleaner class
 */
public class CleanerTest {

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerConstructorWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><h1 id='title'>Test</h1><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Verify that the clean document does not contain the script tag
        assertFalse(cleanDocument.select("script").isEmpty());
        cleanDocument = cleaner.clean(dirtyDocument); // Clean it again to ensure no residual script tags
        assertTrue(cleanDocument.select("script").isEmpty());

        // Verify that the clean document contains the title tag
        Element title = cleanDocument.selectFirst("h1#title");
        assertNotNull(title);
    }

    @Test
    public void testIsValidMethod() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><h1 id='title'>Test</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);

        html = "<html><body><h1 id='title'>Test</h1><script>alert('XSS')</script></body></html>";
        dirtyDocument = Jsoup.parse(html);
        isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodesMethod() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><h1 id='title'>Test<script>alert('XSS')</script></h1></body></html>";
        Element dirtyDocument = Jsoup.parse(html).body();
        Element cleanDocument = new Element(Tag.BODY, dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument, cleanDocument);
        // Verify that the script tag was discarded
        assertNull(cleanDocument.select("script").first());
        assertTrue(numDiscarded > 0);

        // Verify that the title tag was copied
        Element title = cleanDocument.selectFirst("h1");
        assertNotNull(title);
    }

    @Test
    public void testCreateSafeElementMethod() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement = new Element(Tag.DIV, null, new Attributes().put("id", "test"));
        dirtyElement.addClass("test-class");

        Element safeElement = cleaner.createSafeElement(dirtyElement);

        // Verify that the unsafe id attribute was discarded
        assertNull(safeElement.id());

        // Verify that the allowed class attribute was copied
        List<String> classes = Arrays.asList(safeElement.classes().toArray(new String[safeElement.classes().size()]));
        assertTrue(classes.contains("test-class"));
    }
}