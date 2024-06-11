package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.junit.Test;

import java.util.*;

/**
 * Test class for the Tag class.
 */
public class TagTest {

    @Test
    public void testTagCreation() {
        // Test creating a tag with a valid name
        Tag tag1 = new Tag("p");
        assertEquals("p", tag1.getName());

        // Test creating a tag with an invalid null name
        try {
            new Tag(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating a tag with an empty string name
        try {
            new Tag("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating a tag with a valid name and setting its ancestor
        Tag tag2 = new Tag("div");
        tag2.setAncestor("html");
        assertEquals("html", tag2.getImplicitParent().getName());
    }

    @Test
    public void testTagValueOf() {
        // Test getting a tag by name that is already registered
        Tag tag1 = Tag.valueOf("p");
        assertEquals("p", tag1.getName());

        // Test getting a tag by name that is not yet registered
        Tag tag2 = Tag.valueOf("unknown");
        Validate.notNull(tag2);
        assertFalse(tag2.isBlock());
        assertFalse(tag2.canContainBlock());
        assertFalse(tag2.canContainInline());
        assertEquals(Tag.class.getName(), tag2.tagName);
    }

    @Test
    public void testCanContain() {
        // Test a parent tag that can contain a child tag
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("p");
        assertTrue(parent.canContain(child));

        // Test a parent tag that cannot contain a child tag
        Tag parent2 = Tag.valueOf("p");
        Tag child2 = Tag.valueOf("div");
        assertFalse(parent2.canContain(child2));

        // Test a parent tag that can contain only block level tags and a child tag that is also a block level tag
        Tag parent3 = Tag.valueOf("table");
        Tag child3 = Tag.valueOf("tr");
        assertTrue(parent3.canContain(child3));

        // Test a parent tag that can contain only inline level tags and a child tag that is also an inline level tag
        Tag parent4 = Tag.valueOf("td");
        Tag child4 = Tag.valueOf("span");
        assertTrue(parent4.canContain(child4));

        // Test a parent tag that can contain both block and inline level tags and a child tag that is also a block level tag
        Tag parent5 = Tag.valueOf("body");
        Tag child5 = Tag.valueOf("p");
        assertTrue(parent5.canContain(child5));

        // Test a parent tag that can contain both block and inline level tags and a child tag that is also an inline level tag
        Tag parent6 = Tag.valueOf("body");
        Tag child6 = Tag.valueOf("span");
        assertTrue(parent6.canContain(child6));

        // Test a parent tag that can contain only block level tags and a child tag that is also an inline level tag
        Tag parent7 = Tag.valueOf("p");
        Tag child7 = Tag.valueOf("span");
        assertFalse(parent7.canContain(child7));

        // Test a parent tag that can contain only inline level tags and a child tag that is also a block level tag
        Tag parent8 = Tag.valueOf("span");
        Tag child8 = Tag.valueOf("div");
        assertFalse(parent8.canContain(child8));
    }

    @Test
    public void testIsBlock() {
        // Test a block level tag
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.isBlock());

        // Test an inline level tag
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        // Test a tag that can contain block level tags
        Tag tag1 = Tag.valueOf("body");
        assertTrue(tag1.canContainBlock());

        // Test a tag that cannot contain block level tags
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.canContainBlock());
    }

    @Test
    public void testCanContainInline() {
        // Test a tag that can contain inline level tags
        Tag tag1 = Tag.valueOf("body");
        assertTrue(tag1.canContainInline());

        // Test a tag that cannot contain inline level tags
        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag2.canContainInline());
    }

    @Test
    public void testIsData() {
        // Test a tag that can contain data only
        Tag tag1 = Tag.valueOf("script");
        assertTrue(tag1.isData());

        // Test a tag that cannot contain data only
        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.isData());
    }

    @Test
    public void testIsEmpty() {
        // Test a tag that can contain nothing
        Tag tag1 = Tag.valueOf("img");
        assertTrue(tag1.isEmpty());

        // Test a tag that cannot contain nothing
        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        // Test a tag that preserves whitespace
        Tag tag1 = Tag.valueOf("pre");
        assertTrue(tag1.preserveWhitespace());

        // Test a tag that does not preserve whitespace
        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        // Test getting the implicit parent of a tag with no ancestors
        Tag tag1 = Tag.valueOf("p");
        assertNull(tag1.getImplicitParent());

        // Test getting the implicit parent of a tag with ancestors
        Tag tag2 = Tag.valueOf("div");
        tag2.setAncestor("html", "body");
        assertEquals("body", tag2.getImplicitParent().getName());
    }

    @Test
    public void testIsValidParent() {
        // Test a child tag with no ancestors
        Tag child = Tag.valueOf("p");
        assertTrue(child.isValidParent(Tag.valueOf("div")));

        // Test a child tag with ancestors
        Tag parent = Tag.valueOf("div");
        parent.setAncestor("html", "body");
        assertTrue(child.isValidParent(parent));

        // Test a child tag that is not a descendant of the parent tag
        Tag parent2 = Tag.valueOf("div");
        Tag child2 = Tag.valueOf("ul");
        assertFalse(child2.isValidParent(parent));
    }

    @Test
    public void testEquals() {
        // Test equal tags
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1, tag2);

        // Test not equal tags with different names
        Tag tag3 = Tag.valueOf("div");
        assertNotEquals(tag1, tag3);

        // Test not equal tags with different attributes
        Tag tag4 = new Tag("p");
        tag4.setBlock(false);
        assertNotEquals(tag1, tag4);
    }

    @Test
    public void testHashCode() {
        // Test that two equal tags have the same hash code
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        assertEquals(hashCode1, hashCode2);

        // Test that two different tags have different hash codes
        Tag tag3 = Tag.valueOf("div");
        int hashCode3 = tag3.hashCode();
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        // Test that the tag name is returned as the string representation
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.toString());
    }
}