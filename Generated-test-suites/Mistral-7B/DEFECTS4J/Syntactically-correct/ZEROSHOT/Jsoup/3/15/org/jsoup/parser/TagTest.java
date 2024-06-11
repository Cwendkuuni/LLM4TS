package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("P");
        assertEquals("p", tag.tagName);
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertFalse(tag.optionalClosing());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertNull(tag.getImplicitParent());
        List<String> ancestors = Arrays.asList("html", "body");
        Tag tagWithAncestors = new Tag("div").setAncestor(ancestors.toArray(new String[0]));
        assertEquals(ancestors, tagWithAncestors.ancestors);
    }

    @Test
    public void testCanContain() {
        Tag pTag = new Tag("P");
        Tag divTag = new Tag("DIV");
        Tag h1Tag = new Tag("H1");
        Tag scriptTag = new Tag("SCRIPT");

        assertTrue(pTag.canContain(divTag));
        assertTrue(pTag.canContain(pTag));
        assertTrue(pTag.canContain(h1Tag));
        assertFalse(pTag.canContain(scriptTag));
        assertFalse(h1Tag.canContain(pTag));
        assertFalse(h1Tag.canContain(scriptTag));
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag tag1 = new Tag("P");
        Tag tag2 = new Tag("P");
        Tag tag3 = new Tag("DIV");

        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
        assertEquals(tag1.hashCode(), tag2.hashCode());
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    public void testIsData() {
        Tag pTag = new Tag("P");
        Tag inputTag = new Tag("INPUT");

        assertFalse(pTag.isData());
        assertTrue(inputTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = new Tag("IMG");
        Tag brTag = new Tag("BR");

        assertTrue(imgTag.isEmpty());
        assertTrue(brTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = new Tag("PRE");
        Tag textareaTag = new Tag("TEXTAREA");

        assertFalse(preTag.preserveWhitespace());
        assertFalse(textareaTag.preserveWhitespace());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("DIV");
        assertEquals("div", tag.toString());
    }

    @Test
    public void testCreateBlockAndInlineTags() {
        Tag htmlTag = Tag.valueOf("HTML");
        Tag headTag = Tag.valueOf("HEAD");
        Tag bodyTag = Tag.valueOf("BODY");
        Tag pTag = Tag.valueOf("P");
        Tag h1Tag = Tag.valueOf("H1");
        Tag divTag = Tag.valueOf("DIV");
        Tag scriptTag = Tag.valueOf("SCRIPT");
        Tag inputTag = Tag.valueOf("INPUT");

        assertEquals(htmlTag, Tag.valueOf("HTML"));
        assertEquals(headTag, Tag.valueOf("HEAD"));
        assertEquals(bodyTag, Tag.valueOf("BODY"));
        assertEquals(pTag, Tag.valueOf("p"));
        assertEquals(h1Tag, Tag.valueOf("H1"));
        assertEquals(divTag, Tag.valueOf("DIV"));
        assertEquals(scriptTag, Tag.valueOf("SCRIPT"));
        assertEquals(inputTag, Tag.valueOf("INPUT"));

        assertTrue(htmlTag.canContain(headTag));
        assertTrue(htmlTag.canContain(bodyTag));
        assertTrue(headTag.canContain(scriptTag));
        assertFalse(headTag.canContain(inputTag));
        assertFalse(bodyTag.canContain(inputTag));

        assertFalse(pTag.canContain(h1Tag));
        assertFalse(h1Tag.canContain(pTag));
        assertTrue(pTag.canContain(pTag));
        assertTrue(pTag.canContain(h1Tag));
        assertFalse(h1Tag.canContain(pTag));
        assertFalse(h1Tag.canContain(h1Tag));

        assertTrue(pTag.canContain(scriptTag));
        assertFalse(scriptTag.canContain(pTag));
        assertFalse(inputTag.canContain(scriptTag));
        assertFalse(scriptTag.canContain(inputTag));
    }

    @Test
    public void testValidParent() {
        Tag htmlTag = Tag.valueOf("HTML");
        Tag headTag = Tag.valueOf("HEAD");
        Tag bodyTag = Tag.valueOf("BODY");
        Tag formTag = Tag.valueOf("FORM");
        Tag inputTag = Tag.valueOf("INPUT");

        assertTrue(htmlTag.isValidParent(headTag));
        assertTrue(htmlTag.isValidParent(bodyTag));
        assertTrue(headTag.isValidParent(scriptTag));
        assertFalse(headTag.isValidParent(inputTag));
        assertFalse(bodyTag.isValidParent(inputTag));
        assertTrue(formTag.isValidParent(inputTag));
    }

    @Test(expected = AssertionError.class)
    public void testInvalidParent() {
        Tag pTag = Tag.valueOf("P");
        Tag h1Tag = Tag.valueOf("H1");

        pTag.canContain(h1Tag);
    }
}