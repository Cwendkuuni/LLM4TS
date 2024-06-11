package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.junit.Test;

import java.util.*;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertNull(tag.getImplicitParent());
        assertFalse(tag.isValidParent(tag));
        assertEquals(0, tag.hashCode());
        assertEquals(tag, tag);
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertNull(tag.getImplicitParent());
        assertFalse(tag.isValidParent(tag));
        assertEquals(tag, Tag.valueOf("DIV"));
        assertEquals(tag, Tag.valueOf("Div"));
        assertEquals(tag, Tag.valueOf("DIV").getValue());
        assertEquals(tag, Tag.valueOf("DIV").getValue().getValue());
    }

    @Test
    public void testCanContain() {
        Tag div = new Tag("div");
        Tag p = new Tag("p");
        Tag span = new Tag("span");

        assertTrue(div.canContain(p));
        assertTrue(div.canContain(span));
        assertFalse(p.canContain(div));
        assertFalse(p.canContain(p));
        assertFalse(span.canContain(div));
        assertFalse(span.canContain(span));
    }

    @Test
    public void testIsBlockAndInline() {
        Tag div = new Tag("div");
        Tag p = new Tag("p");
        Tag br = new Tag("br");

        assertTrue(div.isBlock());
        assertFalse(div.isInline());
        assertTrue(p.isInline());
        assertFalse(p.isBlock());
        assertFalse(br.isBlock());
        assertFalse(br.isInline());
    }

    @Test
    public void testIsData() {
        Tag div = new Tag("div");
        Tag script = new Tag("script");

        assertFalse(div.isData());
        assertTrue(script.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag img = new Tag("img");
        Tag div = new Tag("div");
        Tag br = new Tag("br");
        Tag input = new Tag("input");

        assertFalse(img.isEmpty());
        assertFalse(div.isEmpty());
        assertFalse(br.isEmpty());
        assertTrue(input.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = new Tag("pre");
        Tag textarea = new Tag("textarea");

        assertFalse(pre.preserveWhitespace());
        assertFalse(textarea.preserveWhitespace());

        pre.setPreserveWhitespace();
        textarea.setPreserveWhitespace();

        assertTrue(pre.preserveWhitespace());
        assertTrue(textarea.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag body = new Tag("body");
        Tag div = new Tag("div");
        Tag p = new Tag("p");

        div.setAncestors(new String[]{"body"});
        p.setAncestors(new String[]{"div", "body"});

        assertEquals(body, div.getImplicitParent());
        assertEquals(body, p.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag div = new Tag("div");
        Tag p = new Tag("p");

        assertFalse(div.isValidParent(p));
        assertTrue(p.isValidParent(div));
    }

    @Test
    public void testEqualsAndHashcode() {
        Tag div1 = new Tag("div");
        Tag div2 = new Tag("div");
        Tag p = new Tag("p");

        assertEquals(div1, div1);
        assertEquals(div1, div2);
        assertNotEquals(div1, p);
        assertNotEquals(p, div1);
        assertEquals(div1.hashCode(), div2.hashCode());
        assertNotEquals(div1.hashCode(), p.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTagCreation() {
        new Tag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTagNameCreation() {
        new Tag("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrimTagNameCreation() {
        new Tag("   DIV   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalTagNameCreation() {
        new Tag("1tag");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAncestorNull() {
        new Tag("div").setAncestor(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAncestorEmptyList() {
        new Tag("div").setAncestor(Collections.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAncestorInvalidTag() {
        new Tag("div").setAncestor(new Tag("InvalidTag"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanContainSelf() {
        new Tag("div").canContain(new Tag("div"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetContainDataOnlyInvalid() {
        new Tag("div").setContainDataOnly();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetContainInlineOnlyInvalid() {
        new Tag("div").setContainInlineOnly();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyInvalid() {
        new Tag("div").setEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOptionalClosingInvalid() {
        new Tag("div").setOptionalClosing();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPreserveWhitespaceInvalid() {
        new Tag("div").setPreserveWhitespace();
    }

    @Test
    public void testToString() {
        Tag div = new Tag("div");
        assertEquals("div", div.toString());
    }

    // TODO: add tests for specific tag properties and methods, as needed

}