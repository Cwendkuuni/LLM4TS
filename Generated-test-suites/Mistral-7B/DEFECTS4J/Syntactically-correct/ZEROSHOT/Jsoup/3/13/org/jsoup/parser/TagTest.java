package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.junit.Test;

import java.util.*;

/**
 * Test class for Tag.java
 */
public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("P");
        assertEquals("p", tag.tagName);
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.isEmpty());
        assertTrue(tag.canContainInline());
        assertFalse(tag.preserveWhitespace());
        assertNull(tag.ancestors);
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("P");
        assertEquals("p", tag.tagName);
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.isEmpty());
        assertTrue(tag.canContainInline());
        assertFalse(tag.preserveWhitespace());
        assertNull(tag.ancestors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagValueOfNull() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagValueOfEmptyString() {
        Tag.valueOf("");
    }

    @Test
    public void testTagCanContain() {
        Tag pTag = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");
        Tag imgTag = Tag.valueOf("IMG");
        Tag scriptTag = Tag.valueOf("SCRIPT");
        Tag h1Tag = Tag.valueOf("H1");

        assertTrue(pTag.canContain(divTag));
        assertTrue(pTag.canContain(pTag));
        assertTrue(pTag.canContain(imgTag));
        assertFalse(pTag.canContain(scriptTag));
        assertFalse(h1Tag.canContain(pTag));
    }

    @Test
    public void testTagIsBlock() {
        Tag pTag = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");
        Tag imgTag = Tag.valueOf("IMG");
        Tag scriptTag = Tag.valueOf("SCRIPT");

        assertTrue(pTag.isBlock());
        assertTrue(divTag.isBlock());
        assertFalse(imgTag.isBlock());
        assertFalse(scriptTag.isBlock());
    }

    @Test
    public void testTagIsData() {
        Tag pTag = Tag.valueOf("P");
        Tag scriptTag = Tag.valueOf("SCRIPT");

        assertFalse(pTag.isData());
        assertTrue(scriptTag.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag imgTag = Tag.valueOf("IMG");
        Tag pTag = Tag.valueOf("P");

        assertTrue(imgTag.isEmpty());
        assertFalse(pTag.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag preTag = Tag.valueOf("PRE");
        Tag pTag = Tag.valueOf("P");

        assertFalse(pTag.preserveWhitespace());
        assertTrue(preTag.preserveWhitespace());
    }

    @Test
    public void testTagGetImplicitParent() {
        Tag pTag = Tag.valueOf("P");
        Tag bodyTag = Tag.valueOf("BODY");
        Tag headTag = Tag.valueOf("HEAD");

        assertEquals(bodyTag.tagName, pTag.getImplicitParent().tagName);
        assertNull(headTag.getImplicitParent());
    }

    @Test
    public void testTagIsValidParent() {
        Tag pTag = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");
        Tag headTag = Tag.valueOf("HEAD");
        Tag bodyTag = Tag.valueOf("BODY");

        assertTrue(pTag.isValidParent(pTag));
        assertTrue(pTag.isValidParent(divTag));
        assertTrue(divTag.isValidParent(pTag));
        assertFalse(headTag.isValidParent(pTag));
        assertFalse(bodyTag.isValidParent(headTag));
    }

    @Test
    public void testTagEquals() {
        Tag pTag1 = Tag.valueOf("P");
        Tag pTag2 = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");

        assertEquals(pTag1, pTag2);
        assertNotEquals(pTag1, divTag);
    }

    @Test
    public void testTagHashCode() {
        Tag pTag1 = Tag.valueOf("P");
        Tag pTag2 = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");

        int hashCode1 = pTag1.hashCode();
        int hashCode2 = pTag2.hashCode();
        int hashCode3 = divTag.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testTagToString() {
        Tag pTag = Tag.valueOf("P");

        assertEquals("p", pTag.toString());
    }
}