package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Tag class.
 */
public class TagTest {

    @Test
    public void testTagValues() {
        // Test valueOf method with known tags
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        // Test equals and hashCode methods
        Tag equalPTag = Tag.valueOf("p");
        Tag equalDivTag = Tag.valueOf("div");
        Tag notEqualTag = Tag.valueOf("span");

        assertEquals(pTag, equalPTag);
        assertEquals(divTag, equalDivTag);
        assertNotEquals(pTag, notEqualTag);
        assertNotEquals(divTag, notEqualTag);

        // Test valueOf method with unknown tag
        Tag unknownTag = Tag.valueOf("UnknownTag");
        Tag defaultTag = new Tag("UnknownTag");
        assertEquals(defaultTag, unknownTag);

        // Test isKnownTag method
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("UnknownTag"));

        // Test isBlock, formatAsBlock, canContainInline, empty, isSelfClosing, preserveWhitespace, isFormListed and isFormSubmittable methods
        Tag pTagWithAttributes = new Tag("p").setSelfClosing().setEmpty().setPreserveWhitespace();
        Tag spanTag = new Tag("span");

        assertTrue(pTag.isBlock());
        assertFalse(spanTag.isBlock());
        assertFalse(pTag.formatAsBlock());
        assertFalse(spanTag.formatAsBlock());
        assertFalse(pTag.canContainInline());
        assertFalse(spanTag.canContainInline());
        assertFalse(pTag.isEmpty());
        assertFalse(spanTag.isEmpty());
        assertFalse(pTag.isSelfClosing());
        assertFalse(spanTag.isSelfClosing());
        assertTrue(pTagWithAttributes.preserveWhitespace());
        assertFalse(spanTag.preserveWhitespace());
        assertFalse(pTag.isFormListed());
        assertFalse(pTag.isFormSubmittable());

        // Test internal static initialisers
        Tag htmlTag = Tag.valueOf("html");
        Tag titleTag = Tag.valueOf("title");
        Tag inputTag = Tag.valueOf("input");

        assertTrue(htmlTag.isBlock());
        assertTrue(titleTag.isInline());
        assertTrue(inputTag.isFormSubmittable());
    }
}