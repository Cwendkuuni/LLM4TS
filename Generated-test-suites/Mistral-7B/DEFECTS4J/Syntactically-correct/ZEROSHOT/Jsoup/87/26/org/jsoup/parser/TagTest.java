package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * JUnit 4 test file for the Tag class.
 */
public class TagTest {

    @Test
    public void testTagConstruction() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());

        Tag unknownTag = Tag.valueOf("unknownTag");
        assertEquals("unknownTag", unknownTag.getName());
        assertFalse(unknownTag.isBlock());
        assertFalse(unknownTag.formatAsBlock());
        assertFalse(unknownTag.canContainInline());
        assertTrue(unknownTag.isEmpty());
        assertFalse(unknownTag.isSelfClosing());
        assertFalse(unknownTag.preserveWhitespace());
        assertFalse(unknownTag.isFormListed());
        assertFalse(unknownTag.isFormSubmittable());
    }

    @Test
    public void testTagIsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());

        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testTagFormatAsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.formatAsBlock());

        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.formatAsBlock());
    }

    @Test
    public void testTagCanContainInline() {
        Tag tag = new Tag("p");
        assertTrue(tag.canContainInline());

        Tag emptyTag = new Tag("img");
        assertFalse(emptyTag.canContainInline());
    }

    @Test
    public void testTagIsEmpty() {
        Tag tag = new Tag("p");
        assertFalse(tag.isEmpty());

        Tag emptyTag = new Tag("img");
        assertTrue(emptyTag.isEmpty());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag tag = new Tag("p");
        assertFalse(tag.isSelfClosing());

        Tag selfClosingTag = new Tag("img");
        assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testTagIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());

        Tag nonPreserveTag = new Tag("p");
        assertFalse(nonPreserveTag.preserveWhitespace());
    }

    @Test
    public void testTagIsFormListed() {
        Tag tag = new Tag("input");
        assertTrue(tag.isFormListed());

        Tag nonFormListedTag = new Tag("p");
        assertFalse(nonFormListedTag.isFormListed());
    }

    @Test
    public void testTagIsFormSubmittable() {
        Tag tag = new Tag("input");
        assertTrue(tag.isFormSubmittable());

        Tag nonFormSubmittableTag = new Tag("p");
        assertFalse(nonFormSubmittableTag.isFormSubmittable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagNullTagName() {
        new Tag(null);
    }
}