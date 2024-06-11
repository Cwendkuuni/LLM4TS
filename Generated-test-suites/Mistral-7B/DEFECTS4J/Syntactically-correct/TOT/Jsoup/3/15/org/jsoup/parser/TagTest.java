package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TagTest {

    private static final String[] INLINE_TAGS = {"A", "IMG", "BR", "MAP", "Q", "SUB", "SUP", "SPAN", "BDO"};
    private static final String[] BLOCK_TAGS = {"P", "H1", "H2", "H3", "H4", "H5", "H6", "UL", "OL", "PRE", "DIV", "BLOCKQUOTE", "HR", "ADDRESS"};
    private static final String[] SPECIAL_TAGS = {"INPUT", "SELECT", "TEXTAREA", "LABEL", "BUTTON", "OPTGROUP", "OPTION", "FIELDSET", "LEGEND"};
    private static final String[] BLOCK_AND_INLINE_TAGS = {"LI"}; // can contain both block and inline elements
    private static final String[] FORM_CTRL_TAGS = {"FORM", "INPUT", "SELECT", "TEXTAREA", "LABEL", "BUTTON", "OPTGROUP", "OPTION", "FIELDSET", "LEGEND"};
    private static final String[] TABLE_TAGS = {"TABLE", "CAPTION", "THEAD", "TFOOT", "TBODY", "COLGROUP", "COL", "TR", "TH", "TD"};

    private Tag tagA;
    private Tag tagB;
    private Tag tagC;
    private Tag tagD;
    private Tag tagP;
    private Tag tagUL;
    private Tag tagLI;
    private Tag tagTable;

    @Before
    public void setup() {
        tagA = Tag.valueOf("A");
        tagB = Tag.valueOf("B");
        tagC = Tag.valueOf("C");
        tagD = Tag.valueOf("D");
        tagP = Tag.valueOf("P");
        tagUL = Tag.valueOf("UL");
        tagLI = Tag.valueOf("LI");
        tagTable = Tag.valueOf("TABLE");
    }

    @Test
    public void testGetName() {
        assertEquals("A", tagA.getName());
        assertEquals("P", tagP.getName());
        assertEquals("UL", tagUL.getName());
        assertEquals("LI", tagLI.getName());
        assertEquals("TABLE", tagTable.getName());
    }

    @Test
    public void testValueOf() {
        Tag tagE = Tag.valueOf("E");
        Tag tagEmpty = Tag.valueOf("");
        Tag tagSpace = Tag.valueOf(" ");

        assertTrue(tagE.isData());
        assertTrue(tagEmpty.isData());
        assertTrue(tagSpace.isData());
    }

    @Test
    public void testCanContain() {
        for (String tagName : INLINE_TAGS) {
            assertTrue(tagP.canContain(Tag.valueOf(tagName)));
            assertTrue(tagUL.canContain(Tag.valueOf(tagName)));
            assertTrue(tagUL.canContain(tagLI));
            assertTrue(tagTable.canContain(Tag.valueOf(tagName)));
            assertTrue(tagTable.canContain(tagTR));
            assertTrue(tagTable.canContain(tagTD));
        }

        for (String tagName : BLOCK_TAGS) {
            assertFalse(tagA.canContain(Tag.valueOf(tagName)));
            assertFalse(tagUL.canContain(Tag.valueOf(tagName)));
            assertFalse(tagTable.canContain(Tag.valueOf(tagName)));
            assertFalse(tagTable.canContain(tagTR));
            assertFalse(tagTable.canContain(tagTD));
            assertTrue(tagP.canContain(Tag.valueOf(tagName)));
            assertTrue(tagUL.canContain(tagLI));
            assertTrue(tagTable.canContain(tagTR));
            assertTrue(tagTable.canContain(tagTD));
        }

        for (String tagName : SPECIAL_TAGS) {
            assertTrue(tagForm.canContain(Tag.valueOf(tagName))); // assuming "FORM" is defined before "INPUT"
            assertFalse(tagUL.canContain(Tag.valueOf(tagName)));
            assertFalse(tagTable.canContain(Tag.valueOf(tagName)));
        }

        for (String tagName : BLOCK_AND_INLINE_TAGS) {
            assertTrue(tagUL.canContain(Tag.valueOf(tagName)));
            assertTrue(tagUL.canContain(tagLI));
            assertTrue(tagLI.canContain(Tag.valueOf(tagName)));
            assertTrue(tagLI.canContain(tagLI));
        }

        for (String tagName : FORM_CTRL_TAGS) {
            assertTrue(tagForm.canContain(Tag.valueOf(tagName)));
            assertFalse(tagUL.canContain(Tag.valueOf(tagName)));
            assertFalse(tagTable.canContain(Tag.valueOf(tagName)));
        }

        // Test invalid cases
        assertFalse(tagA.canContain(tagA));
        assertFalse(tagP.canContain(tagUL));
        assertFalse(tagUL.canContain(tagUL));
        assertFalse(tagUL.canContain(tagTable));
        assertFalse(tagTable.canContain(tagUL));
        assertFalse(tagTable.canContain(tagTable));
    }

    @Test
    public void testIsBlock() {
        assertTrue(tagP.isBlock());
        assertFalse(tagA.isBlock());
        assertFalse(tagUL.isBlock());
        assertFalse(tagLI.isBlock());
        assertFalse(tagTable.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(tagP.canContainBlock());
        assertFalse(tagA.canContainBlock());
        assertFalse(tagUL.canContainBlock());
        assertFalse(tagLI.canContainBlock());
        assertTrue(tagTable.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(tagP.isInline());
        assertTrue(tagA.isInline());
        assertTrue(tagUL.isInline());
        assertTrue(tagLI.isInline());
        assertFalse(tagTable.isInline());
    }

    @Test
    public void testIsData() {
        assertTrue(tagA.isData());
        assertTrue(tagEmpty.isData());
        assertTrue(tagSpace.isData());
        assertFalse(tagP.isData());
        assertFalse(tagUL.isData());
        assertFalse(tagTable.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tagP.isEmpty());
        assertFalse(tagUL.isEmpty());
        assertFalse(tagTable.isEmpty());
        assertTrue(tagBR.isEmpty());
        assertTrue(tagInput.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tagP.preserveWhitespace());
        assertFalse(tagUL.preserveWhitespace());
        assertTrue(tagPre.preserveWhitespace());
        assertFalse(tagTextarea.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        assertEquals(tagUL, tagLI.getImplicitParent());
        assertEquals(tagTable, tagTR.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        for (String tagName : INLINE_TAGS) {
            assertTrue(tagUL.isValidParent(Tag.valueOf(tagName)));
            assertTrue(tagTable.isValidParent(Tag.valueOf(tagName)));
            assertTrue(tagTable.isValidParent(tagTR));
            assertTrue(tagTable.isValidParent(tagTD));
        }

        for (String tagName : BLOCK_TAGS) {
            assertFalse(tagA.isValidParent(Tag.valueOf(tagName)));
            assertFalse(tagUL.isValidParent(Tag.valueOf(tagName)));
            assertFalse(tagTable.isValidParent(Tag.valueOf(tagName)));
            assertFalse(tagTable.isValidParent(tagTR));
            assertFalse(tagTable.isValidParent(tagTD));
            assertFalse(tagUL.isValidParent(tagUL));
            assertFalse(tagTable.isValidParent(tagTable));
        }

        for (String tagName : SPECIAL_TAGS) {
            assertFalse(tagUL.isValidParent(Tag.valueOf(tagName)));
            assertFalse(tagTable.isValidParent(Tag.valueOf(tagName)));
        }

        for (String tagName : BLOCK_AND_INLINE_TAGS) {
            assertTrue(tagUL.isValidParent(Tag.valueOf(tagName)));
            assertTrue(tagUL.isValidParent(tagLI));
            assertTrue(tagLI.isValidParent(Tag.valueOf(tagName)));
            assertTrue(tagLI.isValidParent(tagLI));
            assertTrue(tagUL.isValidParent(tagLI));
            assertTrue(tagLI.isValidParent(tagUL));
        }

        for (String tagName : FORM_CTRL_TAGS) {
            assertTrue(tagForm.isValidParent(Tag.valueOf(tagName)));
            assertFalse(tagUL.isValidParent(Tag.valueOf(tagName)));
            assertFalse(tagTable.isValidParent(Tag.valueOf(tagName)));
        }

        // Test invalid cases
        assertFalse(tagA.isValidParent(tagA));
        assertFalse(tagUL.isValidParent(tagUL));
        assertFalse(tagTable.isValidParent(tagUL));
        assertFalse(tagTable.isValidParent(tagTable));
    }

    @Test
    public void testEquals() {
        Tag tagSame = Tag.valueOf("P");
        Tag tagDifferent = Tag.valueOf("A");

        assertEquals(tagP, tagP);
        assertEquals(tagP, tagSame);
        assertNotEquals(tagA, tagP);
        assertNotEquals(tagP, tagDifferent);
    }

    @Test
    public void testHashCode() {
        Tag tagSame = Tag.valueOf("P");
        Tag tagDifferent = Tag.valueOf("A");

        int hashCodeP = tagP.hashCode();
        int hashCodeSame = tagSame.hashCode();
        int hashCodeA = tagA.hashCode();

        assertEquals(hashCodeP, hashCodeSame);
        assertNotEquals(hashCodeP, hashCodeA);
    }

    @Test
    public void testToString() {
        assertEquals("P", tagP.toString());
        assertEquals("UL", tagUL.toString());
        assertEquals("LI", tagLI.toString());
        assertEquals("TABLE", tagTable.toString());
    }
}