package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.optionalClosing());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertEquals(new String[0], tag.getAncestors().toArray());
    }

    @Test
    public void testTagValues() {
        Tag pTag = Tag.valueOf("p");
        Tag bodyTag = Tag.valueOf("BODY");
        Tag htmlTag = Tag.valueOf("HTML");

        assertEquals("p", pTag.getName());
        assertEquals("BODY", bodyTag.getName());
        assertEquals("HTML", htmlTag.getName());

        assertTrue(pTag.equals(pTag));
        assertFalse(pTag.equals(bodyTag));
        assertTrue(pTag.equals(Tag.valueOf("p")));
        assertTrue(pTag.hashCode() == Tag.valueOf("p").hashCode());

        assertTrue(htmlTag.isAncestorOf(bodyTag));
        assertTrue(bodyTag.isAncestorOf(pTag));
        assertFalse(pTag.isAncestorOf(bodyTag));
    }

    @Test
    public void testCanContain() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("DIV");
        Tag imgTag = Tag.valueOf("IMG");
        Tag brTag = Tag.valueOf("BR");
        Tag formTag = Tag.valueOf("FORM");

        assertTrue(pTag.canContain(divTag));
        assertTrue(pTag.canContain(imgTag));
        assertFalse(pTag.canContain(pTag));
        assertFalse(pTag.canContain(brTag));
        assertFalse(pTag.canContain(formTag));
    }

    @Test
    public void testIsValidParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("DIV");
        Tag formTag = Tag.valueOf("FORM");
        Tag headTag = Tag.valueOf("HEAD");

        assertTrue(pTag.isValidParent(divTag));
        assertTrue(headTag.isValidParent(pTag));
        assertFalse(formTag.isValidParent(pTag));
    }

    @Test
    public void testSpecialCases() {
        Tag pTag = Tag.valueOf("P");
        Tag headTag = Tag.valueOf("HEAD");
        Tag htmlTag = Tag.valueOf("HTML");
        Tag bodyTag = Tag.valueOf("BODY");
        Tag formTag = Tag.valueOf("FORM");
        Tag inputTag = Tag.valueOf("INPUT");
        Tag textareaTag = Tag.valueOf("TEXTAREA");
        Tag labelTag = Tag.valueOf("LABEL");
        Tag buttonTag = Tag.valueOf("BUTTON");

        assertFalse(htmlTag.canContain(htmlTag));
        assertTrue(headTag.canContain(pTag));
        assertTrue(bodyTag.canContain(pTag));
        assertFalse(buttonTag.canContain(buttonTag));
        assertFalse(inputTag.canContain(inputTag));
        assertTrue(formTag.canContain(inputTag));
        assertTrue(formTag.canContain(textareaTag));
        assertTrue(formTag.canContain(labelTag));
    }
}