package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit 4 test file for Tag class.
 *
 * @author [Your Name]
 */
public class TagTest {

    @Test
    public void testTagValues() {
        // Test Tag valueOf method
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.getName());
        assertTrue(pTag.isInline());
        assertFalse(pTag.formatAsBlock());
        assertFalse(pTag.canContainBlock());
        assertFalse(pTag.canContainInline());
        assertFalse(pTag.isEmpty());
        assertFalse(pTag.isSelfClosing());
        assertFalse(pTag.preserveWhitespace());

        Tag divTag = Tag.valueOf("div");
        assertEquals("div", divTag.getName());
        assertTrue(divTag.isBlock());
        assertTrue(divTag.formatAsBlock());
        assertTrue(divTag.canContainBlock());
        assertTrue(divTag.canContainInline());
        assertFalse(divTag.isEmpty());
        assertFalse(divTag.isSelfClosing());
        assertFalse(divTag.preserveWhitespace());

        Tag unknownTag = Tag.valueOf("unknownTag");
        assertEquals("unknownTag", unknownTag.getName());
        assertFalse(unknownTag.isBlock());
        assertFalse(unknownTag.formatAsBlock());
        assertTrue(unknownTag.canContainBlock());
        assertTrue(unknownTag.canContainInline());
        assertFalse(unknownTag.isEmpty());
        assertFalse(unknownTag.isSelfClosing());
        assertFalse(unknownTag.preserveWhitespace());

        // Test isKnownTag method
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testTagProperties() {
        // Test Tag properties
        Tag pTag = Tag.valueOf("p");

        pTag = pTag.setSelfClosing();
        assertFalse(pTag.isEmpty());
        assertTrue(pTag.isSelfClosing());

        pTag = pTag.setSelfClosing().preserveWhitespace();
        assertTrue(pTag.preserveWhitespace());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isEmpty());
        assertFalse(imgTag.isSelfClosing());
        assertFalse(imgTag.preserveWhitespace());

        imgTag = imgTag.preserveWhitespace();
        assertTrue(imgTag.preserveWhitespace());
    }

    @Test
    public void testTagStaticProperties() {
        // Test Tag static properties
        String[] blockTags = Tag.blockTags;
        for (String tagName : blockTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock());
            assertTrue(tag.formatAsBlock());
            assertTrue(tag.canContainBlock());
            assertFalse(tag.isEmpty());
            assertFalse(tag.isSelfClosing());
            assertFalse(tag.preserveWhitespace());
        }

        String[] inlineTags = Tag.inlineTags;
        for (String tagName : inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.isEmpty());
            assertFalse(tag.isSelfClosing());
            assertFalse(tag.preserveWhitespace());
        }

        String[] emptyTags = Tag.emptyTags;
        for (String tagName : emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertTrue(tag.isEmpty());
            assertFalse(tag.isSelfClosing());
            assertFalse(tag.preserveWhitespace());
        }

        String[] formatAsInlineTags = Tag.formatAsInlineTags;
        for (String tagName : formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.isEmpty());
            assertFalse(tag.isSelfClosing());
            assertFalse(tag.preserveWhitespace());
        }

        String[] preserveWhitespaceTags = Tag.preserveWhitespaceTags;
        for (String tagName : preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.isEmpty());
            assertFalse(tag.isSelfClosing());
            assertTrue(tag.preserveWhitespace());
        }
    }

    @Test
    public void testTagMap() {
        // Test Tag map
        Map<String, Tag> tagMap = new HashMap<String, Tag>();
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            tagMap.put(tagName, tag);
        }

        Tag divTag = tagMap.get("div");
        assertEquals("div", divTag.getName());
        assertTrue(divTag.isBlock());
        assertTrue(divTag.formatAsBlock());
        assertTrue(divTag.canContainBlock());
        assertFalse(divTag.isEmpty());
        assertFalse(divTag.isSelfClosing());
        assertFalse(divTag.preserveWhitespace());

        Tag pTag = tagMap.get("p");
        assertEquals("p", pTag.getName());
        assertFalse(pTag.isBlock());
        assertFalse(pTag.formatAsBlock());
        assertFalse(pTag.canContainBlock());
        assertFalse(pTag.isEmpty());
        assertFalse(pTag.isSelfClosing());
        assertFalse(pTag.preserveWhitespace());
    }
}