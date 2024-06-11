package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

public class TagTest {

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        String expected = "p";
        assertEquals(expected, tag.getName());

        tag = Tag.valueOf("DIV");
        expected = "DIV";
        assertEquals(expected, tag.getName());

        tag = Tag.valueOf("UNDEFINED_TAG");
        Tag expectedTag = new Tag("UNDEFINED_TAG");
        assertEquals(expectedTag, tag);
    }

    @Test
    public void testCanContain() {
        Tag body = new Tag("BODY");
        Tag p = new Tag("P");
        Tag h1 = new Tag("H1");
        Tag ul = new Tag("UL");
        Tag li = new Tag("LI");
        Tag script = new Tag("SCRIPT");

        assertTrue(body.canContain(p));
        assertTrue(body.canContain(h1));
        assertTrue(body.canContain(ul));
        assertFalse(body.canContain(body));
        assertFalse(body.canContain(script));

        assertTrue(p.canContain(p));
        assertTrue(p.canContain(h1));
        assertFalse(p.canContain(ul));
        assertFalse(p.canContain(script));

        assertTrue(h1.canContain(p));
        assertTrue(h1.canContain(h1));
        assertFalse(h1.canContain(ul));
        assertFalse(h1.canContain(script));

        assertTrue(ul.canContain(p));
        assertTrue(ul.canContain(li));
        assertFalse(ul.canContain(ul));
        assertFalse(ul.canContain(script));

        assertTrue(li.canContain(p));
        assertTrue(li.canContain(li));
        assertFalse(li.canContain(ul));
        assertFalse(li.canContain(script));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanContainNegativeScenario() {
        Tag body = new Tag("BODY");
        Tag head = new Tag("HEAD");
        body.canContain(head); // Passes, but should throw an exception since HEAD cannot be a child of BODY
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanContainInvalidTagName() {
        Tag body = new Tag("BODY");
        Tag tag = new Tag("INVALID_TAG_NAME");
        body.canContain(tag); // Should throw an exception since INVALID_TAG_NAME is not a valid tag name
    }

    @Test
    public void testIsBlock() {
        Tag body = new Tag("BODY");
        Tag p = new Tag("P");
        Tag h1 = new Tag("H1");
        Tag ul = new Tag("UL");
        Tag li = new Tag("LI");

        assertTrue(body.isBlock());
        assertTrue(p.isInline());
        assertTrue(h1.isBlock());
        assertTrue(ul.isBlock());
        assertTrue(li.isInline());
    }

    @Test
    public void testIsData() {
        Tag body = new Tag("BODY");
        Tag p = new Tag("P");
        Tag script = new Tag("SCRIPT");

        assertFalse(body.isData());
        assertFalse(p.isData());
        assertTrue(script.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag body = new Tag("BODY");
        Tag p = new Tag("P");
        Tag img = new Tag("IMG");

        assertFalse(body.isEmpty());
        assertFalse(p.isEmpty());
        assertTrue(img.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag body = new Tag("BODY");
        Tag pre = new Tag("PRE");

        assertFalse(body.preserveWhitespace());
        assertTrue(pre.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag body = new Tag("BODY");
        Tag p = new Tag("P");
        p.ancestors.add(body);

        assertEquals(body, p.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag body = new Tag("BODY");
        Tag p = new Tag("P");
        Tag head = new Tag("HEAD");

        assertTrue(body.isValidParent(p));
        assertFalse(head.isValidParent(p));
    }

    @Test
    public void testEquals() {
        Tag body1 = new Tag("BODY");
        Tag body2 = new Tag("BODY");
        Tag p = new Tag("P");

        assertTrue(body1.equals(body2));
        assertFalse(body1.equals(p));
    }

    @Test
    public void testHashCode() {
        Tag body1 = new Tag("BODY");
        Tag body2 = new Tag("BODY");
        Tag p = new Tag("P");

        int body1HashCode = body1.hashCode();
        int body2HashCode = body2.hashCode();
        int pHashCode = p.hashCode();

        assertEquals(body1HashCode, body2HashCode); // Since equal objects should have equal hash codes
        assertNotEquals(body1HashCode, pHashCode); // Since objects of different classes should have different hash codes
    }

    @Test
    public void testToString() {
        Tag body = new Tag("BODY");
        Tag p = new Tag("P");

        assertEquals("BODY", body.toString());
        assertEquals("P", p.toString());
    }
}