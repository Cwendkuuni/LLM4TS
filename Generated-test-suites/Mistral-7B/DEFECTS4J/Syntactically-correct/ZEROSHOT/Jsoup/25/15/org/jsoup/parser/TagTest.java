package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit 4 test file for Tag class
 */
public class TagTest {

    @Test
    public void testTagValues() {
        // Test getting Tag values
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");

        assertEquals("p", pTag.getName());
        assertEquals("div", divTag.getName());
        assertEquals("img", imgTag.getName());

        // Test known tags
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("img"));

        // Test unknown tag
        assertNotNull(Tag.valueOf("unknownTag"));

        // Test empty tag
        assertTrue(imgTag.isEmpty());

        // Test block tag
        assertTrue(pTag.isBlock());
        assertTrue(divTag.isBlock());

        // Test inline tag
        assertFalse(imgTag.isBlock());
        assertFalse(imgTag.isInline());
        assertTrue(pTag.isInline());
        assertTrue(divTag.isInline());

        // Test data tag
        assertFalse(pTag.isData());
        assertFalse(divTag.isData());
        assertTrue(imgTag.isData());

        // Test self closing tag
        assertFalse(pTag.isSelfClosing());
        assertFalse(divTag.isSelfClosing());
        assertTrue(imgTag.isSelfClosing());

        // Test preserve whitespace tag
        assertFalse(pTag.preserveWhitespace());
        assertFalse(divTag.preserveWhitespace());
        assertTrue(imgTag.preserveWhitespace());
    }

    @Test
    public void testTagCategories() {
        // Test block tags
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock());
        }

        // Test inline tags
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
        }

        // Test empty tags
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isEmpty());
        }

        // Test format as inline tags
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.formatAsBlock());
        }

        // Test preserve whitespace tags
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.preserveWhitespace());
        }
    }
}