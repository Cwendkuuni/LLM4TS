package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit 4 test file for the Tag class.
 */
public class TagTest {

    @Test
    public void testTagConstruction() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
        assertEquals(true, tag.isBlock());
        assertEquals(true, tag.formatAsBlock());
        assertEquals(true, tag.canContainBlock());
        assertEquals(true, tag.canContainInline());
        assertEquals(false, tag.isEmpty());
        assertEquals(false, tag.isSelfClosing());
        assertEquals(false, tag.preserveWhitespace());
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("P");
        Tag tag3 = Tag.valueOf("DIV");
        assertEquals("p", tag.getName());
        assertEquals("p", tag2.getName());
        assertEquals("div", tag3.getName());
        assertEquals(tag, tag); // is known tag
        assertFalse(tag.equals(new Object())); // not equals to other object
        assertTrue(tag.isEqual(tag2)); // equals to same tag
        assertFalse(tag.isEqual(tag3)); // not equals to different tag
    }

    @Test
    public void testTagIsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        assertTrue(pTag.isBlock());
        assertTrue(divTag.isBlock());
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isBlock());
    }

    @Test
    public void testTagFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        assertTrue(pTag.formatAsBlock());
        assertTrue(divTag.formatAsBlock());
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.formatAsBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        assertTrue(pTag.canContainBlock());
        assertTrue(divTag.canContainBlock());
        assertFalse(imgTag.canContainBlock());
    }

    @Test
    public void testTagIsInline() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        assertFalse(pTag.isInline());
        assertFalse(divTag.isInline());
        assertTrue(imgTag.isInline());
    }

    @Test
    public void testTagIsData() {
        Tag pTag = Tag.valueOf("p");
        Tag imgTag = Tag.valueOf("img");
        assertFalse(pTag.isData());
        assertTrue(imgTag.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        Tag brTag = Tag.valueOf("br");
        assertTrue(imgTag.isEmpty());
        assertTrue(brTag.isEmpty());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        Tag pTag = Tag.valueOf("p");
        assertTrue(imgTag.isSelfClosing());
        assertFalse(pTag.isSelfClosing());
    }

    @Test
    public void testTagIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateTagName() {
        Validate.notNull(null);
        Validate.notNull("");
        Validate.notNull("  ");
        Validate.notNull("\t");
    }

    @Test
    public void testTagEqualsAndHashCode() {
        Tag pTag = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        assertEquals(pTag, pTag);
        assertEquals(pTag, pTag2);
        assertNotEquals(pTag, divTag);
        assertEquals(pTag.hashCode(), pTag.hashCode());
        assertEquals(pTag.hashCode(), pTag2.hashCode());
        assertNotEquals(pTag.hashCode(), divTag.hashCode());
    }

    @Test
    public void testTagToString() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.toString());
    }

    // Test static initializers
    @Test
    public void testStaticInitializers() {
        assertEquals(36, Tag.blockTags.length);
        assertEquals(73, Tag.inlineTags.length);
        assertEquals(3, Tag.emptyTags.length);
        assertEquals(3, Tag.formatAsInlineTags.length);
        assertEquals(3, Tag.preserveWhitespaceTags.length);
    }
}