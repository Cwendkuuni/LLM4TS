package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testTagValueOf() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        assertEquals("p", pTag.getName());
        assertEquals("div", divTag.getName());

        // Test unknown tag
        Tag unknownTag = Tag.valueOf("unknown");
        assertTrue(unknownTag.isData());
        assertEquals("unknown", unknownTag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");

        assertTrue(pTag.isBlock());
        assertTrue(divTag.isBlock());
        assertFalse(imgTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");

        assertTrue(pTag.formatAsBlock());
        assertTrue(divTag.formatAsBlock());
        assertFalse(imgTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag ulTag = Tag.valueOf("ul");

        assertTrue(divTag.canContainBlock());
        assertTrue(ulTag.canContainBlock());
        assertFalse(imgTag.canContainBlock());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        Tag inputTag = Tag.valueOf("input");

        assertFalse(imgTag.isEmpty());
        assertFalse(inputTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        Tag inputTag = Tag.valueOf("input");

        assertFalse(imgTag.isSelfClosing());
        assertFalse(inputTag.isSelfClosing());

        imgTag.setSelfClosing();
        assertTrue(imgTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        Tag pTag = Tag.valueOf("p");

        assertFalse(pTag.preserveWhitespace());
        assertTrue(preTag.preserveWhitespace());
    }
}