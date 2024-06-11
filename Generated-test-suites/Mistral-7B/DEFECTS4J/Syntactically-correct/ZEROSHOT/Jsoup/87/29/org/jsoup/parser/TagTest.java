package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * JUnit test file for the Tag class
 */
public class TagTest {

    @Test
    public void testTagValues() {
        // Test getting tag values

        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag formTag = Tag.valueOf("form");

        assertEquals("p", pTag.getName());
        assertEquals("div", divTag.getName());
        assertEquals("img", imgTag.getName());
        assertEquals("form", formTag.getName());

        assertEquals(true, pTag.isBlock());
        assertEquals(true, divTag.isBlock());
        assertEquals(false, imgTag.isBlock());
        assertEquals(true, formTag.isBlock());

        // Test getting tag values with ParseSettings
        ParseSettings settings = new ParseSettings();
        Tag pTagLower = Tag.valueOf("p", settings);
        Tag DivTagLower = Tag.valueOf("DIV", settings);

        assertEquals("p", pTagLower.getName());
        assertEquals("DIV", DivTagLower.getName());
    }

    @Test
    public void testUnknownTagValue() {
        // Test getting an unknown tag value
        Tag unknownTag = Tag.valueOf("unknownTag");

        assertFalse(unknownTag.isBlock());
        assertFalse(unknownTag.formatAsBlock());
        assertFalse(unknownTag.canContainInline);
        assertFalse(unknownTag.empty);
        assertFalse(unknownTag.selfClosing);
        assertFalse(unknownTag.preserveWhitespace);
        assertFalse(unknownTag.formList);
        assertFalse(unknownTag.formSubmit);

        assertTrue(unknownTag.equals(Tag.valueOf("unknownTag")));
    }

    @Test
    public void testIsKnownTag() {
        // Test isKnownTag method
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("img"));
        assertTrue(Tag.isKnownTag("form"));

        assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testPreserveWhitespaceTags() {
        // Test preserveWhitespaceTags
        Tag preTag = Tag.valueOf("pre");
        Tag plaintextTag = Tag.valueOf("textarea");

        assertTrue(preTag.preserveWhitespace());
        assertTrue(plaintextTag.preserveWhitespace());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.preserveWhitespace());
    }

    @Test
    public void testFormListedTags() {
        // Test formListedTags
        Tag buttonTag = Tag.valueOf("button");
        Tag inputTag = Tag.valueOf("input");
        Tag textareaTag = Tag.valueOf("textarea");

        assertTrue(textareaTag.isFormListed());
        assertTrue(inputTag.isFormListed());
        assertTrue(buttonTag.isFormListed());

        assertFalse(Tag.valueOf("p").isFormListed());
    }

    @Test
    public void testFormSubmittableTags() {
        // Test formSubmittableTags
        Tag inputTag = Tag.valueOf("input");
        Tag textareaTag = Tag.valueOf("textarea");
        Tag buttonTag = Tag.valueOf("button");

        assertTrue(textareaTag.isFormSubmittable());
        assertTrue(inputTag.isFormSubmittable());
        assertTrue(buttonTag.isFormSubmittable());

        assertFalse(Tag.valueOf("p").isFormSubmittable());
    }
}