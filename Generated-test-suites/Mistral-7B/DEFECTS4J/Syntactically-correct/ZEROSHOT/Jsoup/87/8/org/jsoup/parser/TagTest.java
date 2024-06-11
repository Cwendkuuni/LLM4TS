package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the Tag class.
 */
public class TagTest {

    @Test
    public void testTagCreation() {
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

        Tag divTag = new Tag("div");
        assertTrue(divTag.isBlock());

        Tag spanTag = new Tag("span");
        assertFalse(spanTag.isBlock());
    }

    @Test
    public void testTagFormatAsBlock() {
        Tag pTag = new Tag("p");
        assertTrue(pTag.formatAsBlock());

        Tag spanTag = new Tag("span");
        assertFalse(spanTag.formatAsBlock());

        Tag preTag = new Tag("pre");
        assertTrue(preTag.formatAsBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag pTag = new Tag("p");
        assertTrue(pTag.canContainBlock());
        assertTrue(pTag.isBlock());

        Tag divTag = new Tag("div");
        assertTrue(divTag.canContainBlock());
        assertTrue(divTag.isBlock());

        Tag spanTag = new Tag("span");
        assertFalse(spanTag.canContainBlock());
        assertFalse(spanTag.isBlock());
    }

    @Test
    public void testTagIsEmpty() {
        Tag imgTag = new Tag("img");
        assertFalse(imgTag.isEmpty());

        Tag inputTag = new Tag("input");
        assertFalse(inputTag.isEmpty());

        Tag imgTagEmpty = new Tag("img").setSelfClosing();
        assertTrue(imgTagEmpty.isEmpty());

        Tag areaTag = new Tag("area");
        assertTrue(areaTag.isEmpty());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag imgTag = new Tag("img");
        assertFalse(imgTag.isSelfClosing());

        Tag imgTagEmpty = new Tag("img").setSelfClosing();
        assertTrue(imgTagEmpty.isSelfClosing());

        Tag inputTag = new Tag("input");
        assertFalse(inputTag.isSelfClosing());

        Tag areaTag = new Tag("area");
        assertTrue(areaTag.isSelfClosing());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag preTag = new Tag("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag pTag = new Tag("p");
        assertFalse(pTag.preserveWhitespace());

        Tag textareaTag = new Tag("textarea");
        assertTrue(textareaTag.preserveWhitespace());
    }

    @Test
    public void testTagIsFormListed() {
        Tag buttonTag = new Tag("button");
        assertTrue(buttonTag.isFormListed());

        Tag inputTag = new Tag("input");
        assertTrue(inputTag.isFormListed());

        Tag textareaTag = new Tag("textarea");
        assertTrue(textareaTag.isFormListed());

        Tag pTag = new Tag("p");
        assertFalse(pTag.isFormListed());
    }

    @Test
    public void testTagIsFormSubmittable() {
        Tag inputTag = new Tag("input");
        assertTrue(inputTag.isFormSubmittable());

        Tag textareaTag = new Tag("textarea");
        assertTrue(textareaTag.isFormSubmittable());

        Tag pTag = new Tag("p");
        assertFalse(pTag.isFormSubmittable());
    }

    @Test
    public void testTagEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("div");

        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testTagHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("div");

        int hash1 = tag1.hashCode();
        int hash2 = tag2.hashCode();
        int hash3 = tag3.hashCode();

        assertTrue(tag1.hashCode() == hash1);
        assertTrue(tag2.hashCode() == hash2);
        assertNotEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
    }

    @Test
    public void testTagToString() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }
}