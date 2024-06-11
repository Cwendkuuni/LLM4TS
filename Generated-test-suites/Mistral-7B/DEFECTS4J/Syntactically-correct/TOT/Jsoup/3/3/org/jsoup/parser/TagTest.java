package org.jsoup.parser;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testgetValueOf() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.getValueOf("DIV");
        Tag tagUnknown = Tag.getValueOf("UNKNOWN");

        assertEquals("p", tagP.getName());
        assertEquals("DIV", tagDiv.getName());
        assertNotEquals("p", tagUnknown.getName());
        assertTrue(tagUnknown.isData());

        Tag tagP2 = Tag.valueOf("P");
        Tag tagP3 = Tag.valueOf("p");
        assertTrue(tagP.equals(tagP2));
        assertTrue(tagP.equals(tagP3));
    }

    @Test
    public void testCanContain() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagHead = Tag.valueOf("HEAD");
        Tag tagImg = Tag.valueOf("IMG");
        Tag tagScript = Tag.valueOf("SCRIPT");
        Tag tagTable = Tag.valueOf("TABLE");
        Tag tagUL = Tag.valueOf("UL");

        assertTrue(tagP.canContain(tagP));
        assertTrue(tagP.canContain(tagDiv));
        assertFalse(tagDiv.canContain(tagP));
        assertFalse(tagHead.canContain(tagP));
        assertFalse(tagImg.canContain(tagScript));
        assertFalse(tagTable.canContain(tagP));
        assertFalse(tagUL.canContain(tagP));
        assertFalse(tagUL.canContain(tagTagUL)); // recursion not allowed

        assertTrue(tagHead.canContain(tagScript));
        assertTrue(tagHead.canContain(tagLink));
        assertFalse(tagHead.canContain(tagP));

        assertTrue(tagDiv.canContain(tagP));
        assertTrue(tagDiv.canContain(tagDiv));
        assertFalse(tagDiv.canContain(tagTable));

        assertTrue(tagTable.canContain(tagTR));
        assertTrue(tagTable.canContain(tagTH));
        assertTrue(tagTable.canContain(tagTD));
        assertFalse(tagTable.canContain(tagP));
    }

    @Test
    public void testIsBlock() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagHead = Tag.valueOf("HEAD");
        Tag tagScript = Tag.valueOf("SCRIPT");

        assertTrue(tagP.isBlock());
        assertTrue(tagDiv.isBlock());
        assertFalse(tagHead.isBlock());
        assertFalse(tagScript.isBlock());
    }

    @Test
    public void testIsData() {
        Tag tagP = Tag.valueOf("p");
        Tag tagImg = Tag.valueOf("IMG");
        Tag tagScript = Tag.valueOf("SCRIPT");

        assertFalse(tagP.isData());
        assertFalse(tagDiv.isData());
        assertTrue(tagImg.isData());
        assertTrue(tagScript.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tagP = Tag.valueOf("p");
        Tag tagBr = Tag.valueOf("BR");
        Tag tagImg = Tag.valueOf("IMG");

        assertFalse(tagP.isEmpty());
        assertFalse(tagDiv.isEmpty());
        assertFalse(tagImg.isEmpty());
        assertTrue(tagBr.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tagP = Tag.valueOf("p");
        Tag tagPre = Tag.valueOf("PRE");

        assertFalse(tagP.preserveWhitespace());
        assertTrue(tagPre.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagP = Tag.valueOf("p");
        Tag tagHead = Tag.valueOf("HEAD");

        assertNull(tagP.getImplicitParent());
        assertEquals(tagDiv, tagDiv.getImplicitParent());
        assertEquals(tagHead, tagHead.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagHead = Tag.valueOf("HEAD");
        Tag tagP = Tag.valueOf("p");
        Tag tagUl = Tag.valueOf("UL");

        assertTrue(tagDiv.isValidParent(tagP));
        assertTrue(tagDiv.isValidParent(tagDiv));
        assertFalse(tagHead.isValidParent(tagP));
        assertFalse(tagUl.isValidParent(tagDiv));
        assertFalse(tagP.isValidParent(tagDiv));
    }

    @Test
    public void testEquals() {
        Tag tagP1 = Tag.valueOf("p");
        Tag tagP2 = Tag.valueOf("p");
        Tag tagDiv1 = Tag.valueOf("DIV");
        Tag tagDiv2 = Tag.valueOf("DIV");
        Tag tagUnknown = Tag.valueOf("UNKNOWN");

        assertTrue(tagP1.equals(tagP2));
        assertNotEquals(tagP1, tagDiv1);
        assertNotEquals(tagDiv1, tagUnknown);
    }

    @Test
    public void testHashCode() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagUnknown = Tag.valueOf("UNKNOWN");

        assertEquals(tagP.hashCode(), tagP.hashCode());
        assertEquals(tagP.hashCode(), tagP.hashCode());
        assertNotEquals(tagP.hashCode(), tagDiv.hashCode());
        assertNotEquals(tagDiv.hashCode(), tagUnknown.hashCode());
    }

    @Test
    public void testToString() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagUnknown = Tag.valueOf("UNKNOWN");

        assertEquals("p", tagP.toString());
        assertEquals("DIV", tagDiv.toString());
        assertEquals("UNKNOWN", tagUnknown.toString());
    }

    @Test
    public void testCanContainEmpty() {
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagP = Tag.valueOf("p");
        Tag tagBr = Tag.valueOf("BR");

        assertTrue(tagDiv.canContain(Collections.emptyList()));
        assertTrue(tagDiv.canContain(Arrays.asList(tagP, tagBr)));
        assertFalse(tagP.canContain(Collections.emptyList()));
    }

    @Test
    public void testCanContainWithEmptyAncestor() {
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagP = Tag.valueOf("p");
        Tag tagHead = Tag.valueOf("HEAD");

        assertTrue(tagDiv.canContain(tagP));
        assertFalse(tagHead.canContain(tagDiv));
        assertFalse(tagHead.canContain(tagP));
    }

    @Test
    public void testCanContainWithDescendant() {
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagP = Tag.valueOf("p");
        Tag tagUL = Tag.valueOf("UL");
        Tag tagLI = Tag.valueOf("LI");

        assertTrue(tagDiv.canContain(tagP));
        assertTrue(tagDiv.canContain(tagUL));
        assertTrue(tagUL.canContain(tagLI));
        assertFalse(tagLI.canContain(tagDiv));
        assertFalse(tagLI.canContain(tagUL));
        assertFalse(tagLI.canContain(tagP));
    }

    @Test
    public void testCanContainWithIncompatibleParents() {
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagP = Tag.valueOf("p");
        Tag tagHead = Tag.valueOf("HEAD");
        Tag tagScript = Tag.valueOf("SCRIPT");

        assertFalse(tagDiv.canContain(tagScript));
        assertFalse(tagHead.canContain(tagDiv));
        assertFalse(tagHead.canContain(tagScript));
    }

    @Test
    public void testCanContainWithOptionalClosing() {
        Tag tagP = Tag.valueOf("p");
        Tag tagP2 = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("DIV");

        assertTrue(tagP.canContain(tagP2));
        assertFalse(tagDiv.canContain(tagP));
        assertFalse(tagDiv.canContain(tagP2));
    }

    @Test
    public void testCanContainWithEmptyTag() {
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagBr = Tag.valueOf("BR");

        assertTrue(tagDiv.canContain(tagBr));
        assertFalse(tagBr.canContain(tagDiv));
    }

    @Test
    public void testCanContainWithNonBlockAncestor() {
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagP = Tag.valueOf("p");
        Tag tagA = Tag.valueOf("a");

        assertFalse(tagDiv.canContain(tagP));
        assertTrue(tagA.canContain(tagP));
    }

    @Test
    public void testCanContainWithDataOnlyTag() {
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagScript = Tag.valueOf("SCRIPT");

        assertFalse(tagDiv.canContain(tagScript));
    }

    @Test
    public void testCanContainWithEmptyTagWithEmptyAncestor() {
        Tag tagScript = Tag.valueOf("SCRIPT");

        assertFalse(tagScript.canContain(Collections.emptyList()));
    }

    @Test
    public void testCanContainWithEmptyTagWithDescendants() {
        Tag tagScript = Tag.valueOf("SCRIPT");
        Tag tagComment = Tag.valueOf("!--");

        assertFalse(tagScript.canContain(tagComment));
    }
}