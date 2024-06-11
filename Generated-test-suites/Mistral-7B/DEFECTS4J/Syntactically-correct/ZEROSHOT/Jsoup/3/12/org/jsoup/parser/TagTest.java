package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("DIV");
        String name = tag.getName();
        assertTrue(name.equals("div"));
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("DIV");
        Tag tag2 = Tag.valueOf("DIV");

        assertTrue(tag == tag2);
    }

    @Test
    public void testTagValueOfUnknown() {
        Tag tag = Tag.valueOf("UNKNOWN");
        Tag tag2 = Tag.valueOf("DIV");

        assertFalse(tag == tag2);
        assertTrue(tag.equals(tag2));
    }

    @Test
    public void testCanContain() {
        Tag pTag = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");
        Tag pTag2 = Tag.valueOf("P");

        assertTrue(pTag.canContain(divTag));
        assertFalse(pTag.canContain(pTag2));
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");

        assertTrue(pTag.isBlock());
        assertTrue(divTag.isBlock());
    }

    @Test
    public void testIsInline() {
        Tag pTag = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");

        assertFalse(pTag.isInline());
        assertFalse(divTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag scriptTag = Tag.valueOf("SCRIPT");
        Tag pTag = Tag.valueOf("P");

        assertTrue(scriptTag.isData());
        assertFalse(pTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("IMG");
        Tag pTag = Tag.valueOf("P");

        assertFalse(imgTag.isEmpty());
        assertFalse(pTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("PRE");
        Tag pTag = Tag.valueOf("P");

        assertFalse(pTag.preserveWhitespace());
        assertTrue(preTag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag pTag = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");
        Tag bodyTag = Tag.valueOf("BODY");
        Tag htmlTag = Tag.valueOf("HTML");

        assertEquals(htmlTag, pTag.getImplicitParent());
        assertEquals(bodyTag, divTag.getImplicitParent());
        assertEquals(null, pTag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag pTag = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");
        Tag pTag2 = Tag.valueOf("P");
        Tag bodyTag = Tag.valueOf("BODY");

        assertTrue(pTag.isValidParent(bodyTag));
        assertTrue(pTag.isValidParent(pTag2));
        assertFalse(pTag.isValidParent(divTag));
    }

    @Test
    public void testEquals() {
        Tag pTag1 = Tag.valueOf("P");
        Tag pTag2 = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");

        assertTrue(pTag1.equals(pTag2));
        assertFalse(pTag1.equals(divTag));
    }

    @Test
    public void testHashCode() {
        Tag pTag1 = Tag.valueOf("P");
        Tag pTag2 = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");

        int hashCode1 = pTag1.hashCode();
        int hashCode2 = pTag2.hashCode();
        int hashCode3 = divTag.hashCode();

        assertTrue(hashCode1 == hashCode2);
        assertFalse(hashCode1 == hashCode3);
    }

    @Test
    public void testToString() {
        Tag pTag = Tag.valueOf("P");

        assertEquals("p", pTag.toString());
    }

    @Test
    public void testCreateBlock() {
        Tag htmlTag = createBlock("HTML");
        Tag headTag = createBlock("HEAD");
        Tag bodyTag = createBlock("BODY");

        assertEquals("html", htmlTag.getName());
        assertEquals("head", headTag.getName());
        assertEquals("body", bodyTag.getName());
    }

    @Test
    public void testCreateInline() {
        Tag fontTag = createInline("FONT");

        assertEquals("font", fontTag.getName());
    }

    @Test
    public void testRegister() {
        Tag pTag = new Tag("P");
        Tag registeredPTag = register(pTag);

        assertEquals(pTag, registeredPTag);
    }

    private Tag register(Tag tag) {
        Tag tagFromMap = Tag.values.get(tag.tagName);

        if (tagFromMap != null) {
            return tagFromMap;
        }

        Tag.values.put(tag.tagName, tag);
        return tag;
    }

    private static List<Tag> values = new ArrayList<>();

    static {
        Tag.values.add(new Tag("HTML"));
        Tag.values.add(new Tag("HEAD"));
        Tag.values.add(new Tag("BODY"));
        // Add other tags here...
    }
}