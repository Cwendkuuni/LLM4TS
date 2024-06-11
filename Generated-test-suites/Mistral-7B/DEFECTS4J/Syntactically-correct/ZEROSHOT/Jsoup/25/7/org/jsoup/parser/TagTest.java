package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit 4 test file for the Tag class.
 */
public class TagTest {

    @Test
    public void testTagValues() {
        // Test getting a known tag by name
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.getName());
        assertTrue(pTag.isBlock());
        // Test getting an unknown tag by name
        Tag unknownTag = Tag.valueOf("unknown");
        assertEquals("unknown", unknownTag.getName());
        assertFalse(unknownTag.isBlock());
        // Test equality between known tags
        Tag pTag2 = Tag.valueOf("p");
        assertEquals(pTag, pTag2);
        // Test equality between known and unknown tags
        assertNotEquals(pTag, unknownTag);
        // Test equality between two unknown tags
        Tag unknownTag2 = Tag.valueOf("unknown");
        assertNotEquals(unknownTag, unknownTag2);
    }

    @Test
    public void testIsBlock() {
        // Test block tags
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());
        Tag divTag = Tag.valueOf("div");
        assertTrue(divTag.isBlock());
        // Test inline tags
        Tag spanTag = Tag.valueOf("span");
        assertFalse(spanTag.isBlock());
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        // Test block tags
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.formatAsBlock());
        Tag divTag = Tag.valueOf("div");
        assertTrue(divTag.formatAsBlock());
        // Test inline tags
        Tag spanTag = Tag.valueOf("span");
        assertFalse(spanTag.formatAsBlock());
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        // Test block tags that can contain block tags
        Tag divTag = Tag.valueOf("div");
        assertTrue(divTag.canContainBlock());
        Tag tableTag = Tag.valueOf("table");
        assertTrue(tableTag.canContainBlock());
        // Test inline tags that cannot contain block tags
        Tag spanTag = Tag.valueOf("span");
        assertFalse(spanTag.canContainBlock());
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.canContainBlock());
    }

    @Test
    public void testIsData() {
        // Test data only tags
        Tag scriptTag = Tag.valueOf("script");
        assertTrue(scriptTag.isData());
        Tag styleTag = Tag.valueOf("style");
        assertTrue(styleTag.isData());
        // Test non-data only tags
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isData());
        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isData());
    }

    @Test
    public void testIsEmpty() {
        // Test empty tags
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isEmpty());
        Tag brTag = Tag.valueOf("br");
        assertTrue(brTag.isEmpty());
        // Test non-empty tags
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isEmpty());
        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        // Test self-closing tags
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isSelfClosing());
        Tag areaTag = Tag.valueOf("area");
        assertTrue(areaTag.isSelfClosing());
        // Test non-self-closing tags
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isSelfClosing());
        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        // Test known tags
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        // Test unknown tags
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        // Test tags that preserve whitespace
        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.preserveWhitespace());
        Tag textareaTag = Tag.valueOf("textarea");
        assertTrue(textareaTag.preserveWhitespace());
        // Test tags that do not preserve whitespace
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.preserveWhitespace());
        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.preserveWhitespace());
    }
}