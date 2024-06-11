package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test file for Cleaner class
 */
public class CleanerTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        Whitelist whitelist = new SimpleHtmlWhitelist.Builder().build();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    /**
     * Test clean method with valid input
     */
    @Test
    public void testCleanValidInput() {
        Whitelist whitelist = new SimpleHtmlWhitelist.Builder()
                .addTag("p")
                .addTag("a")
                .build();

        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><p><a href=\"test\">Test</a></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><body><p><a href=\"test\">Test</a></p></body></html>", cleanDocument.html());
    }

    /**
     * Test clean method with invalid input
     */
    @Test
    public void testCleanInvalidInput() {
        Whitelist whitelist = new SimpleHtmlWhitelist.Builder()
                .addTag("p")
                .build();

        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    /**
     * Test isValid method with valid input
     */
    @Test
    public void testIsValidValidInput() {
        Whitelist whitelist = new SimpleHtmlWhitelist.Builder()
                .addTag("p")
                .addTag("a")
                .build();

        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><p><a href=\"test\">Test</a></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        assertFalse(cleaner.isValid(dirtyDocument));
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleaner.isValid(cleanDocument));
    }

    /**
     * Test isValid method with invalid input
     */
    @Test
    public void testIsValidInvalidInput() {
        Whitelist whitelist = new SimpleHtmlWhitelist.Builder()
                .addTag("p")
                .build();

        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        assertFalse(cleaner.isValid(dirtyDocument));
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleaner.isValid(cleanDocument));
    }

    /**
     * Test copySafeNodes method with valid input
     */
    @Test
    public void testCopySafeNodesValidInput() {
        Whitelist whitelist = new SimpleHtmlWhitelist.Builder()
                .addTag("p")
                .addTag("a")
                .build();

        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><p><a href=\"test\">Test</a></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element body = dirtyDocument.body();

        Element newBody = new Element(Tag.BODY);
        int numDiscarded = cleaner.copySafeNodes(body, newBody);

        assertEquals(0, numDiscarded);
        assertEquals(html, newBody.html());
    }

    /**
     * Test copySafeNodes method with invalid input
     */
    @Test
    public void testCopySafeNodesInvalidInput() {
        Whitelist whitelist = new SimpleHtmlWhitelist.Builder()
                .addTag("p")
                .build();

        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element body = dirtyDocument.body();

        Element newBody = new Element(Tag.BODY);
        int numDiscarded = cleaner.copySafeNodes(body, newBody);

        assertEquals(1, numDiscarded);
        assertNull(newBody.child(0));
    }

    /**
     * Test createSafeElement method
     */
    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new SimpleHtmlWhitelist.Builder()
                .addTag("p")
                .addAttribute("a", "href")
                .build();

        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("a", "test", new Attributes().put("href", "test"));

        ElementMeta result = cleaner.createSafeElement(sourceEl);
        Element destChild = result.el;

        assertEquals("a", destChild.tagName());
        assertEquals("test", destChild.baseUri());
        assertEquals(1, result.numAttribsDiscarded);
        assertEquals("test", destChild.attributes().get("href").getValue());
    }

    /**
     * Test createSafeElement method with invalid input
     */
    @Test
    public void testCreateSafeElementInvalidInput() {
        Whitelist whitelist = new SimpleHtmlWhitelist.Builder()
                .addTag("p")
                .build();

        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("script", null);

        ElementMeta result = cleaner.createSafeElement(sourceEl);
        assertNull(result.el);
    }

    /**
     * Test ElementMeta class
     */
    @Test
    public void testElementMeta() {
        Element el = new Element("p");
        Attributes attrs = new Attributes().put("id", "test");
        Element metaEl = new Element("p", attrs);

        ElementMeta elementMeta = new ElementMeta(el, 0);
        ElementMeta metaElement = new ElementMeta(metaEl, 1);

        assertSame(el, elementMeta.el);
        assertSame(metaEl, metaElement.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertEquals(1, metaElement.numAttribsDiscarded);
    }

    /**
     * Test SimpleHtmlWhitelist class
     */
    @Test
    public void testSimpleHtmlWhitelist() {
        List<String> tags = Arrays.asList("p", "a");
        Whitelist whitelist = new SimpleHtmlWhitelist.Builder().addTags(tags).build();

        Tag tagP = Tag.valueOf("p");
        Tag tagA = Tag.valueOf("a");

        assertEquals(2, whitelist.allowedTagNames().size());
        assertTrue(whitelist.isSafeTag(tagP));
        assertTrue(whitelist.isSafeTag(tagA));
        assertFalse(whitelist.isSafeTag(Tag.SCRIPT));
    }
}