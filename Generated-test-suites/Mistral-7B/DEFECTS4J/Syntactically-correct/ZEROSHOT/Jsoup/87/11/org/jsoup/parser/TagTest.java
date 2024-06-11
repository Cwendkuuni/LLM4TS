package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Tag class
 */
public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
        tag = new Tag("DIV");
        assertEquals("DIV", tag.getName());
    }

    @Test
    public void testTagValues() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("DIV");

        // Test isBlock()
        assertTrue(pTag.isBlock());
        assertFalse(divTag.isBlock());

        // Test formatAsBlock()
        assertTrue(pTag.formatAsBlock());
        assertFalse(divTag.formatAsBlock());

        // Test canContainInline()
        assertTrue(pTag.canContainInline());
        assertFalse(divTag.canContainInline());

        // Test empty()
        assertFalse(pTag.isEmpty());
        assertFalse(divTag.isEmpty());

        // Test selfClosing()
        assertFalse(pTag.isSelfClosing());
        assertFalse(divTag.isSelfClosing());

        // Test isKnownTag()
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("UNKNOWN"));

        // Test preserveWhitespace()
        assertFalse(pTag.preserveWhitespace());
        assertFalse(divTag.preserveWhitespace());

        // Test isFormListed()
        assertFalse(pTag.isFormListed());
        assertFalse(divTag.isFormListed());

        // Test isFormSubmittable()
        assertFalse(pTag.isFormSubmittable());
        assertFalse(divTag.isFormSubmittable());
    }

    @Test
    public void testTagValueOf() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("DIV");

        // Test case insensitive tag name
        Tag PTag = Tag.valueOf("P");
        assertEquals(pTag, PTag);

        // Test case sensitive tag name
        Tag DIVTag = Tag.valueOf("DIV");
        assertEquals(divTag, DIVTag);

        // Test unknown tag name
        Tag unknownTag = Tag.valueOf("UNKNOWN");
        assertNotEquals(pTag, unknownTag);
        assertNotEquals(divTag, unknownTag);
        assertTrue(unknownTag.isBlock());
    }

    @Test
    public void testTagStaticMethods() {
        // Test valueOf(String, ParseSettings)
        Tag pTag = Tag.valueOf("p", null);
        Tag divTag = Tag.valueOf("DIV", null);

        assertEquals(Tag.class, pTag.getClass());
        assertEquals(Tag.class, divTag.getClass());

        Tag pTagWithSettings = Tag.valueOf("p", null);
        Tag divTagWithSettings = Tag.valueOf("DIV", null);

        assertEquals(pTag, pTagWithSettings);
        assertEquals(divTag, divTagWithSettings);

        // Test valueOf(String)
        Tag pTagWithoutSettings = Tag.valueOf("p");
        Tag divTagWithoutSettings = Tag.valueOf("DIV");

        assertEquals(pTag, pTagWithoutSettings);
        assertEquals(divTag, divTagWithoutSettings);

        // Test isBlock()
        assertTrue(Tag.isBlock("p"));
        assertFalse(Tag.isBlock("div"));

        // Test isKnownTag()
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("UNKNOWN"));
    }
}