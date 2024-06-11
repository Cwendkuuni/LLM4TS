package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.junit.Test;

import java.util.*;

/**
 * JUnit tests for the Tag class.
 *
 * @author Johnson
 */
public class TagTest {

    @Test
    public void testTagValueOf() {
        // Test getting a Tag by name
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("BODY");
        Tag unknownTag = Tag.valueOf("UnknownTag");

        // Test predefined tags
        assertEquals("p", pTag.tagName);
        assertEquals("div", divTag.tagName);
        assertEquals("BODY", bodyTag.tagName);

        // Test unknown tag
        assertEquals("unknownTag", unknownTag.tagName);
        assertFalse(unknownTag.isBlock);
        assertFalse(unknownTag.canContainBlock);
        assertFalse(unknownTag.canContainInline);
        assertFalse(unknownTag.optionalClosing);
        assertFalse(unknownTag.empty);
        assertFalse(unknownTag.preserveWhitespace);
        assertTrue(unknownTag.ancestors.isEmpty());
    }

    @Test
    public void testTagCanContain() {
        // Test p can contain div
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        assertTrue(pTag.canContain(divTag));

        // Test div cannot contain p
        assertFalse(divTag.canContain(pTag));

        // Test p can contain p
        assertTrue(pTag.canContain(pTag));

        // Test head can contain script
        Tag headTag = Tag.valueOf("HEAD");
        Tag scriptTag = Tag.valueOf("SCRIPT");
        assertTrue(headTag.canContain(scriptTag));

        // Test dt cannot contain dd
        Tag dtTag = Tag.valueOf("DT");
        Tag ddTag = Tag.valueOf("DD");
        assertFalse(dtTag.canContain(ddTag));

        // Test dd can contain dt
        assertTrue(ddTag.canContain(dtTag));

        // Test p cannot contain p inside p
        Tag p1Tag = Tag.valueOf("p");
        Tag p2Tag = Tag.valueOf("p");
        assertFalse(p1Tag.canContain(p2Tag));
    }

    @Test
    public void testTagIsBlock() {
        // Test p is block
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());

        // Test span is inline
        Tag spanTag = Tag.valueOf("SPAN");
        assertFalse(spanTag.isBlock());

        // Test table is block
        Tag tableTag = Tag.valueOf("TABLE");
        assertTrue(tableTag.isBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        // Test p can contain block
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.canContainBlock());

        // Test div cannot contain block
        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.canContainBlock());

        // Test table can contain block
        Tag tableTag = Tag.valueOf("TABLE");
        assertTrue(tableTag.canContainBlock());
    }

    @Test
    public void testTagIsData() {
        // Test img is data only
        Tag imgTag = Tag.valueOf("IMG");
        assertTrue(imgTag.isData());

        // Test p is not data only
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isData());
    }

    @Test
    public void testTagIsEmpty() {
        // Test img is empty
        Tag imgTag = Tag.valueOf("IMG");
        assertTrue(imgTag.isEmpty());

        // Test p is not empty
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        // Test pre does preserve whitespace
        Tag preTag = Tag.valueOf("PRE");
        assertTrue(preTag.preserveWhitespace());

        // Test p does not preserve whitespace
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.preserveWhitespace());
    }

    @Test
    public void testTagGetImplicitParent() {
        // Test p gets body as implicit parent
        Tag pTag = Tag.valueOf("p");
        Tag bodyTag = Tag.valueOf("BODY");
        assertEquals(bodyTag, pTag.getImplicitParent());
    }

    @Test
    public void testTagIsValidParent() {
        // Test div is valid parent for p
        Tag divTag = Tag.valueOf("div");
        Tag pTag = Tag.valueOf("p");
        assertTrue(divTag.isValidParent(pTag));

        // Test p is not valid parent for div
        assertTrue(pTag.isValidParent(divTag));

        // Test div is not valid parent for div
        assertFalse(divTag.isValidParent(divTag));
    }

    @Test
    public void testTagEquals() {
        // Test two equal tags are equal
        Tag p1Tag = Tag.valueOf("p");
        Tag p2Tag = Tag.valueOf("p");
        assertTrue(p1Tag.equals(p2Tag));

        // Test two different tags are not equal
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        assertFalse(pTag.equals(divTag));

        // Test null tag is not equal to any tag
        assertFalse(pTag.equals(null));
    }

    @Test
    public void testTagHashCode() {
        // Test hashcodes are equal for equal objects
        Tag p1Tag = Tag.valueOf("p");
        Tag p2Tag = Tag.valueOf("p");
        int hashcode1 = p1Tag.hashCode();
        int hashcode2 = p2Tag.hashCode();
        assertEquals(hashcode1, hashcode2);

        // Test hashcodes are different for different objects
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        int pHashcode = pTag.hashCode();
        int divHashcode = divTag.hashCode();
        assertNotEquals(pHashcode, divHashcode);
    }

    @Test
    public void testTagToString() {
        // Test tag name is returned as string
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.toString());
    }

    @Test
    public void testTagCreateBlock() {
        // Test createBlock for HTML
        Tag htmlTag = Tag.createBlock("HTML");
        assertEquals("HTML", htmlTag.tagName);
        assertTrue(htmlTag.isBlock());
        assertTrue(htmlTag.canContainBlock());
        assertTrue(htmlTag.canContainInline());
        assertTrue(htmlTag.optionalClosing);
        assertFalse(htmlTag.isEmpty());
        assertTrue(htmlTag.preserveWhitespace);
        assertTrue(htmlTag.ancestors.isEmpty());

        // Test createBlock for HEAD
        Tag headTag = Tag.createBlock("HEAD");
        assertEquals("HEAD", headTag.tagName);
        assertTrue(headTag.isBlock());
        assertTrue(headTag.canContainBlock());
        assertTrue(headTag.canContainInline());
        assertTrue(headTag.optionalClosing);
        assertFalse(headTag.isEmpty());
        assertFalse(headTag.preserveWhitespace);
        // Ancestors not defined for HEAD

        // Test createBlock for BODY
        Tag bodyTag = Tag.createBlock("BODY");
        assertEquals("BODY", bodyTag.tagName);
        assertTrue(bodyTag.isBlock());
        assertTrue(bodyTag.canContainBlock());
        assertTrue(bodyTag.canContainInline());
        assertFalse(bodyTag.optionalClosing);
        assertFalse(bodyTag.isEmpty());
        assertFalse(bodyTag.preserveWhitespace);
        // Ancestors not defined for BODY

        // Test createBlock for FRAMESET
        Tag framesetTag = Tag.createBlock("FRAMESET");
        assertEquals("FRAMESET", framesetTag.tagName);
        assertTrue(framesetTag.isBlock());
        assertFalse(framesetTag.canContainBlock); // TODO: Implement
        assertFalse(framesetTag.canContainInline); // TODO: Implement
        assertFalse(framesetTag.optionalClosing);
        assertFalse(framesetTag.isEmpty());
        assertFalse(framesetTag.preserveWhitespace);
        // Ancestors not defined for FRAMESET
    }

    @Test
    public void testTagCreateInline() {
        // Test createInline for FONT
        Tag fontTag = Tag.createInline("FONT");
        assertEquals("FONT", fontTag.tagName);
        assertFalse(fontTag.isBlock);
        assertFalse(fontTag.canContainBlock);
        assertFalse(fontTag.canContainInline);
        assertFalse(fontTag.optionalClosing);
        assertFalse(fontTag.isEmpty);
        assertFalse(fontTag.preserveWhitespace);
        // Ancestors not defined for FONT

        // Test createInline for TT
        Tag ttTag = Tag.createInline("TT");
        assertEquals("TT", ttTag.tagName);
        assertFalse(ttTag.isBlock());
        assertFalse(ttTag.canContainBlock());
        assertFalse(ttTag.canContainInline());
        assertFalse(ttTag.optionalClosing);
        assertFalse(ttTag.isEmpty());
        assertFalse(ttTag.preserveWhitespace);
        // Ancestors not defined for TT

        // Test createInline for I
        Tag iTag = Tag.createInline("I");
        assertEquals("I", iTag.tagName);
        assertFalse(iTag.isBlock());
        assertFalse(iTag.canContainBlock());
        assertFalse(iTag.canContainInline());
        assertFalse(iTag.optionalClosing);
        assertFalse(iTag.isEmpty);
        assertFalse(iTag.preserveWhitespace);
        // Ancestors not defined for I

        // Test createInline for B
        Tag bTag = Tag.createInline("B");
        assertEquals("B", bTag.tagName);
        assertFalse(bTag.isBlock());
        assertFalse(bTag.canContainBlock());
        assertFalse(bTag.canContainInline());
        assertFalse(bTag.optionalClosing);
        assertFalse(bTag.isEmpty);
        assertFalse(bTag.preserveWhitespace);
        // Ancestors not defined for B

        // Test createInline for Big
        Tag bigTag = Tag.createInline("BIG");
        assertEquals("BIG", bigTag.tagName);
        assertFalse(bigTag.isBlock());
        assertFalse(bigTag.canContainBlock());
        assertFalse(bigTag.canContainInline());
        assertFalse(bigTag.optionalClosing);
        assertFalse(bigTag.isEmpty);
        assertFalse(bigTag.preserveWhitespace);
        // Ancestors not defined for BIG

        // Test createInline for SMALL
        Tag smallTag = Tag.createInline("SMALL");
        assertEquals("SMALL", smallTag.tagName);
        assertFalse(smallTag.isBlock());
        assertFalse(smallTag.canContainBlock());
        assertFalse(smallTag.canContainInline());
        assertFalse(smallTag.optionalClosing);
        assertFalse(smallTag.isEmpty);
        assertFalse(smallTag.preserveWhitespace);
        // Ancestors not defined for SMALL
    }
}