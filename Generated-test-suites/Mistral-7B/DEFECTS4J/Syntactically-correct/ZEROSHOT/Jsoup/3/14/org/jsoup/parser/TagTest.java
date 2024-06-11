package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertFalse(tag.isInline());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertNull(tag.getImplicitParent());
        List<String> ancestors = Arrays.asList("html", "head", "body");
        tag.setAncestor(ancestors.toArray(new String[0]));
        assertEquals(ancestors, Arrays.asList(tag.ancestors.get(0).getName(), tag.ancestors.get(1).getName(), tag.ancestors.get(2).getName()));
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());

        Tag unknownTag = Tag.valueOf("unknownTag");
        assertEquals("unknownTag", tag.getName());
        assertFalse(tag.equals(Tag.valueOf("p")));
    }

    @Test
    public void testCanContain() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");
        Tag h1Tag = Tag.valueOf("h1");

        assertTrue(pTag.canContain(divTag));
        assertTrue(pTag.canContain(imgTag));
        assertTrue(pTag.canContain(h1Tag));
        assertFalse(h1Tag.canContain(pTag));
        assertFalse(pTag.canContain(pTag));
        assertFalse(pTag.canContain(imgTag)); // same tag cannot be contained within itself
    }

    @Test
    public void testIsData() {
        Tag pTag = Tag.valueOf("p");
        Tag scriptTag = Tag.valueOf("script");
        Tag textareaTag = Tag.valueOf("textarea");

        assertFalse(pTag.isData());
        assertTrue(scriptTag.isData());
        assertTrue(textareaTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        Tag inputTag = Tag.valueOf("input");
        Tag pTag = Tag.valueOf("p");

        assertFalse(imgTag.isEmpty());
        assertFalse(inputTag.isEmpty());
        assertFalse(pTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        Tag textAreaTag = Tag.valueOf("textarea");

        assertFalse(preTag.preserveWhitespace());
        assertFalse(textAreaTag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag pTag = Tag.valueOf("p");
        Tag bodyTag = Tag.valueOf("body");
        Tag htmlTag = Tag.valueOf("html");

        pTag.setAncestor(htmlTag.getName(), bodyTag.getName(), pTag.getName());
        assertEquals(bodyTag, pTag.getImplicitParent());
        assertEquals(pTag, pTag.getImplicitParent().getImplicitParent());
        assertEquals(htmlTag, pTag.getImplicitParent().getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("body");

        assertTrue(pTag.isValidParent(divTag));
        assertTrue(bodyTag.isValidParent(pTag));
        assertFalse(pTag.isValidParent(pTag));
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("div");

        assertEquals(tag1, tag1);
        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        int hashCode3 = tag3.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }
}