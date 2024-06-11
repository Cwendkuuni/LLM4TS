package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * JUnit 4 test file for Tag class.
 */
public class TagTest {

    @Test
    public void testTagName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());

        tag = Tag.valueOf("input");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());

        tag = Tag.valueOf("input");
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("input");
        assertTrue(tag.canContainInline());

        tag = Tag.valueOf("p p"); // nested block
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testCanContainInline() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainInline());

        tag = Tag.valueOf("input");
        assertTrue(tag.canContainInline());

        tag = Tag.valueOf("script"); // no inline
        assertFalse(tag.canContainInline());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertFalse(tag.isEmpty());

        tag = Tag.valueOf("input");
        assertFalse(tag.isEmpty());

        tag = Tag.valueOf("img"); // empty image
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("img");
        assertFalse(tag.isSelfClosing());

        tag = tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());

        tag = Tag.valueOf("br"); // self-closing tag
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());

        tag = Tag.valueOf("p");
        assertFalse(tag.preserveWhitespace());
    }
}