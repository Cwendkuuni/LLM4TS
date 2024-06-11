package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tag unit tests.
 */
public class TagTest {

    @Test
    public void testTagCreation() {
        // Test creation of known tags
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag scriptTag = Tag.valueOf("script");

        // Test creation of unknown tags
        Tag unknownTag = Tag.valueOf("unknownTag");

        // Test that known tags are equal to themselves
        assertEquals(pTag, pTag);
        assertEquals(divTag, divTag);
        assertEquals(scriptTag, scriptTag);

        // Test that unknown tag is not equal to known tags
        assertFalse(pTag.equals(unknownTag));
        assertFalse(divTag.equals(unknownTag));
        assertFalse(scriptTag.equals(unknownTag));

        // Test that unknown tag is equal to itself
        assertEquals(unknownTag, unknownTag);

        // Test isKnownTag
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("script"));
        assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testTagProperties() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        // Test isBlock
        assertTrue(pTag.isBlock());
        assertFalse(divTag.isBlock());

        // Test formatAsBlock
        assertTrue(pTag.formatAsBlock());
        assertFalse(divTag.formatAsBlock());
        pTag = pTag.setFormatAsBlock(false);
        assertFalse(pTag.formatAsBlock());

        // Test canContainInline
        assertTrue(pTag.canContainInline());
        assertFalse(divTag.canContainInline());
        pTag = pTag.setCanContainInline(false);
        assertFalse(pTag.canContainInline());

        // Test empty
        assertFalse(pTag.isEmpty());
        assertFalse(divTag.isEmpty());
        pTag = pTag.setEmpty(true);
        assertTrue(pTag.isEmpty());

        // Test selfClosing
        assertFalse(pTag.isSelfClosing());
        assertFalse(divTag.isSelfClosing());
        pTag = pTag.setSelfClosing(true);
        assertTrue(pTag.isSelfClosing());

        // Test preserveWhitespace
        assertFalse(pTag.preserveWhitespace());
        assertFalse(divTag.preserveWhitespace());
        pTag = pTag.setPreserveWhitespace(true);
        assertTrue(pTag.preserveWhitespace());

        // Test formList
        assertFalse(pTag.isFormListed());
        assertFalse(divTag.isFormListed());
        pTag = pTag.setFormList(true);
        assertTrue(pTag.isFormListed());

        // Test formSubmit
        assertFalse(pTag.isFormSubmittable());
        assertFalse(divTag.isFormSubmittable());
        pTag = pTag.setFormSubmittable(true);
        assertTrue(pTag.isFormSubmittable());
    }

    @Test
    public void testTagEquality() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        // Test equality of same tag
        assertEquals(pTag1, pTag1);
        assertEquals(pTag1, pTag2);

        // Test inequality of different tags
        assertFalse(pTag1.equals(divTag));
        assertFalse(divTag.equals(pTag1));

        // Test inequality of same tag with different properties
        Tag pTag3 = new Tag("p");
        pTag3.setCanContainInline(false);
        assertFalse(pTag1.equals(pTag3));
        assertFalse(pTag3.equals(pTag1));
    }

    @Test
    public void testTagHashCode() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        // Test hashcode of same tag is same
        assertEquals(pTag1.hashCode(), pTag1.hashCode());
        assertEquals(pTag1.hashCode(), pTag2.hashCode());

        // Test hashcode of different tags is different
        assertNotEquals(pTag1.hashCode(), divTag.hashCode());
        assertNotEquals(divTag.hashCode(), pTag1.hashCode());
    }

    @Test
    public void testTagToString() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.toString());
    }
}