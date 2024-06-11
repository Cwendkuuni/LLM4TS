package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Tag.java
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
    public void testTagEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("div");

        assertEquals(tag1, tag1);
        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
    }

    @Test
    public void testTagHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("div");

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        int hashCode3 = tag3.hashCode();

        assertTrue(hashCode1 == hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testTagToString() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }

    @Test
    public void testTagValueOf() {
        Tag tag1 = new Tag("p");
        Tag tag2 = Tag.valueOf("p");

        assertEquals(tag1, tag2);
    }

    @Test
    public void testTagValueOfCaseSensitive() {
        Tag tag1 = new Tag("p");
        Tag tag2 = Tag.valueOf("P");

        assertNotEquals(tag1, tag2);
    }

    @Test
    public void testTagIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
    }

    @Test
    public void testTagIsKnownTagUnknown() {
        assertFalse(Tag.isKnownTag("xyz"));
    }

    @Test
    public void testTagIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());

        tag = Tag.valueOf("input");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testTagFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());

        tag = Tag.valueOf("input");
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("input");
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testTagIsInline() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isInline());

        tag = Tag.valueOf("input");
        assertTrue(tag.isInline());
    }

    @Test
    public void testTagIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());

        tag = Tag.valueOf("script");
        assertTrue(tag.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isEmpty());

        tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isSelfClosing());

        tag = Tag.valueOf("img");
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.preserveWhitespace());

        tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testTagIsFormListed() {
        Tag tag = Tag.valueOf("input");
        assertTrue(tag.isFormListed());

        tag = Tag.valueOf("p");
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testTagIsFormSubmittable() {
        Tag tag = Tag.valueOf("input[type=submit]");
        assertTrue(tag.isFormSubmittable());

        tag = Tag.valueOf("p");
        assertFalse(tag.isFormSubmittable());
    }

}