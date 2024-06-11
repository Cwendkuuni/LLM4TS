package org.jsoup.parser;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tag class tests")
public class TagTest {

    @DisplayName("Testing getName()")
    @ParameterizedTest(name = "{index}: getName for tag '{0}'")
    @MethodSource("getTagsForTest")
    void testGetName(Tag tag) {
        assertEquals(tag.getName(), tag.tagName);
    }

    @DisplayName("Testing valueOf()")
    @Test
    void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertEquals(tag.tagName, "div");

        Tag unknownTag = Tag.valueOf("unknown");
        assertEquals(unknownTag.tagName, "unknown");
        assertFalse(unknownTag.canContainBlock);
        assertFalse(unknownTag.canContainInline);
    }

    @DisplayName("Testing canContain()")
    @ParameterizedTest(name = "{index}: canContain between tag '{0}' and '{1}'")
    @MethodSource("getCanContainTestData")
    void testCanContain(Tag parent, Tag child) {
        assertTrue(parent.canContain(child));
    }

    @DisplayName("Testing canContain() with invalid child")
    @Test
    void testCanContainInvalidChild() {
        Tag parent = new Tag("p");
        Tag invalidChild = new Tag("p");
        assertFalse(parent.canContain(invalidChild));
    }

    @DisplayName("Testing isBlock()")
    @Test
    void testIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());

        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.isBlock());
    }

    @DisplayName("Testing canContainBlock()")
    @Test
    void testCanContainBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());

        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.canContainBlock());
    }

    @DisplayName("Testing isInline()")
    @Test
    void testIsInline() {
        Tag tag = new Tag("div");
        assertFalse(tag.isInline());

        Tag inlineTag = new Tag("span");
        assertTrue(inlineTag.isInline());
    }

    @DisplayName("Testing isData()")
    @Test
    void testIsData() {
        Tag tag = new Tag("div");
        assertFalse(tag.isData());

        Tag dataOnlyTag = new Tag("script");
        assertTrue(dataOnlyTag.isData());
    }

    @DisplayName("Testing isEmpty()")
    @Test
    void testIsEmpty() {
        Tag tag = new Tag("img");
        assertFalse(tag.isEmpty());

        Tag emptyTag = new Tag("img").setEmpty();
        assertTrue(emptyTag.isEmpty());
    }

    @DisplayName("Testing preserveWhitespace()")
    @Test
    void testPreserveWhitespace() {
        Tag tag = new Tag("div").setPreserveWhitespace();
        assertTrue(tag.preserveWhitespace());

        Tag nonPreserveTag = new Tag("div");
        assertFalse(nonPreserveTag.preserveWhitespace());
    }

    @DisplayName("Testing getImplicitParent()")
    @Test
    void testGetImplicitParent() {
        Tag tag = new Tag("div").setAncestors(new Tag("body"), new Tag("html"));
        assertEquals(tag.getImplicitParent().tagName, "body");
    }

    @DisplayName("Testing isValidParent()")
    @Test
    void testIsValidParent() {
        Tag tag = new Tag("div");
        Tag child = new Tag("p");
        assertTrue(tag.isValidParent(child));

        Tag invalidParent = new Tag("div");
        Tag invalidChild = new Tag("div");
        assertFalse(invalidParent.isValidParent(invalidChild));
    }

    @DisplayName("Testing equals()")
    @Test
    void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.equals(tag2));

        Tag tag3 = new Tag("p");
        assertFalse(tag1.equals(tag3));

        Tag tag4 = new Tag("div").setCanContainBlock(false);
        assertFalse(tag1.equals(tag4));
    }

    @DisplayName("Testing hashCode()")
    @Test
    void testHashCode() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());

        Tag tag3 = new Tag("p");
        assertNotEquals(tag1.hashCode(), tag3.hashCode());

        Tag tag4 = new Tag("div").setCanContainBlock(false);
        assertNotEquals(tag1.hashCode(), tag4.hashCode());
    }

    @DisplayName("Testing toString()")
    @Test
    void testToString() {
        Tag tag = new Tag("div");
        assertEquals(tag.toString(), "div");
    }

    private static Object[] getCanContainTestData() {
        return new Object[][]{
                new Object[]{new Tag("div"), new Tag("p")},
                new Object[]{new Tag("p"), new Tag("div")},
                new Object[]{new Tag("div"), new Tag("div")},
                new Object[]{new Tag("div"), new Tag("span")},
                new Object[]{new Tag("span"), new Tag("div")},
                new Object[]{new Tag("div"), new Tag("div")},
                new Object[]{new Tag("div"), new Tag("div")},
                new Object[]{new Tag("div"), new Tag("div")},
        };
    }

    private static List<Tag> getTagsForTest() {
        return Arrays.asList(
                new Tag("div"),
                new Tag("p"),
                new Tag("span"),
                new Tag("body"),
                new Tag("html")
        );
    }
}