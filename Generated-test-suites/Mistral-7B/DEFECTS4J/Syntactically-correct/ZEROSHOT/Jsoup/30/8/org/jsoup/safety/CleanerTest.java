package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

/**
 * Test file for Cleaner class.
 */
public class CleanerTest {

    @org.junit.Before
    public void setUp() {
        // Initialize a whitelist for testing
        Whitelist whitelist = Whitelist.simpleText();
    }

    @org.junit.Test
    public void testCreateCleaner() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testCreateCleanerNullWhitelist() {
        Whitelist whitelist = null;
        Cleaner cleaner = new Cleaner(whitelist);
    }

    @org.junit.Test
    public void testCleanDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.simpleText());
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertTrue(cleanDocument.body().children().isEmpty());
        assertNull(cleanDocument.outputSettings().prettyPrint());
    }

    @org.junit.Test
    public void testIsValidDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.simpleText());
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);

        html = "<html><head><title>Test</title></head><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>";
        Document invalidDocument = Jsoup.parse(html);
        isValid = cleaner.isValid(invalidDocument);
        assertFalse(isValid);
    }

    @org.junit.Test
    public void testCopySafeNodes() {
        String html = "<html><head><title>Test</title></head><body><h1 id='test' class='test' style='color: red;'>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.simpleText());
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(0, numDiscarded);
        Element h1 = cleanDocument.body().element("h1");
        assertEquals("test", h1.id());
        assertEquals("test", h1.classes().get(0));
        assertEquals("color: red;", h1.attr("style"));
    }

    @org.junit.Test
    public void testIsSafeTagAndAttribute() {
        Whitelist whitelist = Whitelist.simpleText();
        assertFalse(whitelist.isSafeTag("script", null, null));
        assertFalse(whitelist.isSafeTag("h1", null, null));

        Element element = new Element("div");
        assertFalse(whitelist.isSafeTag(element.tagName(), element, null));

        Attribute attribute = new Attribute("onclick", "alert('XSS')");
        assertFalse(whitelist.isSafeAttribute("div", null, attribute));

        assertFalse(whitelist.isSafeAttribute("h1", null, null));

        element.setAttribute("data-test", "test");
        assertTrue(whitelist.isSafeAttribute("div", element, new Attribute("data-test", "test")));
    }

    @org.junit.Test
    public void testGetEnforcedAttributes() {
        Whitelist whitelist = new Whitelist() {
            @Override
            public void addTagNameAttributeSelectors(Tag tag, List<String> selectors) {
                selectors.add("class:test");
            }
        };

        List<String> selectors = whitelist.getEnforcedAttributes("div");
        assertEquals(1, selectors.size());
        assertEquals("class:test", selectors.get(0));
    }

}