package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Tag.java
 */
public class TagTest {

    @Test
    public void testTagValues() {
        // Test valueOf with known tags
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag h1Tag = Tag.valueOf("h1");

        // Check tag names
        assertEquals("p", pTag.getName());
        assertEquals("div", divTag.getName());
        assertEquals("h1", h1Tag.getName());

        // Test valueOf with unknown tags
        Tag unknownTag = Tag.valueOf("unknownTag");
        assertFalse(unknownTag.isKnownTag());
        assertEquals("unknownTag", unknownTag.getName());

        // Test getters
        assertTrue(pTag.isBlock());
        assertFalse(pTag.formatAsBlock());
        assertFalse(pTag.canContainInline());
        assertFalse(pTag.isEmpty());
        assertFalse(pTag.isSelfClosing());
        assertFalse(pTag.preserveWhitespace());
        assertFalse(pTag.isFormListed());
        assertFalse(pTag.isFormSubmittable());

        assertTrue(divTag.isBlock());
        assertTrue(divTag.formatAsBlock());
        assertTrue(divTag.canContainInline());
        assertFalse(divTag.isEmpty());
        assertFalse(divTag.isSelfClosing());
        assertFalse(divTag.preserveWhitespace());
        assertFalse(divTag.isFormListed());
        assertFalse(divTag.isFormSubmittable());

        assertFalse(h1Tag.isBlock());
        assertFalse(h1Tag.formatAsBlock());
        assertFalse(h1Tag.canContainInline());
        assertFalse(h1Tag.isEmpty());
        assertFalse(h1Tag.isSelfClosing());
        assertFalse(h1Tag.preserveWhitespace());
        assertFalse(h1Tag.isFormListed());
        assertFalse(h1Tag.isFormSubmittable());
    }

    @Test
    public void testTagMethods() {
        // Test isBlock
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());

        Tag brTag = Tag.valueOf("br");
        assertFalse(brTag.isBlock());

        // Test formatAsBlock
        Tag pTag1 = Tag.valueOf("p");
        assertTrue(pTag1.formatAsBlock());

        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.formatAsBlock());

        Tag aTag = Tag.valueOf("a");
        assertFalse(aTag.formatAsBlock());

        // Test canContainInline
        Tag pTag2 = Tag.valueOf("p");
        assertTrue(pTag2.canContainInline());

        Tag preTag1 = Tag.valueOf("pre");
        assertFalse(preTag1.canContainInline());

        Tag scriptTag = Tag.valueOf("script");
        assertFalse(scriptTag.canContainInline());

        // Test isEmpty
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isEmpty());

        Tag inputTag = Tag.valueOf("input");
        assertFalse(inputTag.isEmpty());

        Tag areaTag = Tag.valueOf("area");
        assertFalse(areaTag.isEmpty());

        Tag metaTag = Tag.valueOf("meta");
        assertFalse(metaTag.isEmpty());

        Tag brTag1 = Tag.valueOf("br");
        assertTrue(brTag1.isEmpty());

        Tag imgTag1 = Tag.valueOf("img").setSelfClosing();
        assertTrue(imgTag1.isEmpty());

        // Test isSelfClosing
        Tag imgTag2 = Tag.valueOf("img");
        assertFalse(imgTag2.isSelfClosing());

        Tag imgTag3 = Tag.valueOf("img").setSelfClosing();
        assertTrue(imgTag3.isSelfClosing());

        Tag areaTag1 = Tag.valueOf("area");
        assertFalse(areaTag1.isSelfClosing());

        Tag areaTag2 = Tag.valueOf("area").setSelfClosing();
        assertTrue(areaTag2.isSelfClosing());

        // Test preserveWhitespace
        Tag preTag2 = Tag.valueOf("pre");
        assertTrue(preTag2.preserveWhitespace());

        Tag textareaTag = Tag.valueOf("textarea");
        assertTrue(textareaTag.preserveWhitespace());

        Tag scriptTag1 = Tag.valueOf("script");
        assertFalse(scriptTag1.preserveWhitespace());

        // Test isFormListed
        Tag inputTag1 = Tag.valueOf("input");
        assertTrue(inputTag1.isFormListed());

        Tag selectTag = Tag.valueOf("select");
        assertTrue(selectTag.isFormListed());

        Tag textareaTag1 = Tag.valueOf("textarea");
        assertTrue(textareaTag1.isFormListed());

        Tag buttonTag = Tag.valueOf("button");
        assertTrue(buttonTag.isFormListed());

        // Test isFormSubmittable
        Tag inputTag2 = Tag.valueOf("input");
        assertTrue(inputTag2.isFormSubmittable());

        Tag selectTag1 = Tag.valueOf("select");
        assertTrue(selectTag1.isFormSubmittable());

        Tag textareaTag2 = Tag.valueOf("textarea");
        assertTrue(textareaTag2.isFormSubmittable());

        Tag buttonTag1 = Tag.valueOf("button");
        assertTrue(buttonTag1.isFormSubmittable());

        // Test equals and hashCode
        Tag pTag3 = Tag.valueOf("p");
        Tag pTag4 = Tag.valueOf("p");

        assertEquals(pTag3, pTag4);
        assertEquals(pTag3.hashCode(), pTag4.hashCode());

        Tag pTag5 = new Tag("p");
        assertNotEquals(pTag3, pTag5);
        assertNotEquals(pTag3.hashCode(), pTag5.hashCode());

        Tag aTag1 = Tag.valueOf("a");
        Tag aTag2 = Tag.valueOf("a");

        assertEquals(aTag1, aTag2);
        assertEquals(aTag1.hashCode(), aTag2.hashCode());

        Tag aTag3 = new Tag("a");
        assertNotEquals(aTag1, aTag3);
        assertNotEquals(aTag1.hashCode(), aTag3.hashCode());
    }

    @Test
    public void testTagConstants() {
        // Test blockTags
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock());
        }

        // Test inlineTags
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
        }

        // Test emptyTags
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isEmpty());
        }

        // Test formatAsInlineTags
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.formatAsBlock());
        }

        // Test preserveWhitespaceTags
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.preserveWhitespace());
        }

        // Test formListedTags
        for (String tagName : Tag.formListedTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isFormListed());
        }

        // Test formSubmitTags
        for (String tagName : Tag.formSubmitTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isFormSubmittable());
        }
    }

    @Test
    public void testTagRegistration() {
        // Test registration of known tags
        Tag htmlTag = Tag.valueOf("html");
        assertNotNull(htmlTag);

        Tag headTag = Tag.valueOf("head");
        assertNotNull(headTag);

        Tag bodyTag = Tag.valueOf("body");
        assertNotNull(bodyTag);

        // Test registration of unknown tag
        Tag unknownTag = new Tag("unknownTag");
        Map<String, Tag> tags = Tag.tags;
        assertNull(tags.get("unknownTag"));
    }
}