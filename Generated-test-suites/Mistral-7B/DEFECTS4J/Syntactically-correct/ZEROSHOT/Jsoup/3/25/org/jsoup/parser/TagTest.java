package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class TagTest {

    private Tag tag;
    private String tagName;

    @Parameterized.Parameters(name = "{index}: testTag({0})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Tag("HTML")}, "HTML",
                {new Tag("HEAD")}, "HEAD",
                {new Tag("BODY")}, "BODY",
                {new Tag("P")}, "P",
                {new Tag("DIV")}, "DIV",
                {new Tag("A")}, "A",
                {new Tag("IMG")}, "IMG",
                {new Tag("SUP")}, "SUP",
                {new Tag("UL")}, "UL",
                {new Tag("TD")}, "TD",
                {new Tag("BR")}, "BR"
        });
    }

    public TagTest(Tag tag, String tagName) {
        this.tag = tag;
        this.tagName = tagName;
    }

    @Test
    public void testTagName() {
        assertEquals(tagName, tag.getName());
    }

    @Test
    public void testEquals() {
        Tag tagSame = Tag.valueOf(tagName);
        assertTrue(tag.equals(tagSame));
        assertTrue(tagSame.equals(tag));
    }

    @Test
    public void testHashCode() {
        Tag tagSame = Tag.valueOf(tagName);
        int hashCodeThis = tag.hashCode();
        int hashCodeOther = tagSame.hashCode();
        assertEquals(hashCodeThis, hashCodeOther);
    }

    @Test
    public void testCanContainBlock() {
        List<String> ancestors = Arrays.asList("HTML", "BODY").toArray(new String[0]);
        Tag bodyTag = new Tag("BODY");
        bodyTag.setAncestors(ancestors);

        Tag pTag = new Tag("P");
        Tag imgTag = new Tag("IMG");
        Tag supTag = new Tag("SUP");
        Tag ulTag = new Tag("UL");
        Tag tdTag = new Tag("TD");
        Tag brTag = new Tag("BR");

        // Test with itself
        assertTrue(tag.canContain(tag));

        // Test with descendants
        assertTrue(tag.canContain(bodyTag));
        assertTrue(bodyTag.canContain(pTag));
        assertTrue(pTag.canContain(imgTag));
        assertTrue(pTag.canContain(supTag));
        assertTrue(ulTag.canContain(tdTag));
        assertTrue(tdTag.canContain(brTag));

        // Test with ancestors
        assertFalse(tag.canContain(tagName.equalsIgnoreCase("HEAD") ? tag : null));
        assertFalse(tag.canContain(tagName.equalsIgnoreCase("HEAD") ? new Tag("HEAD") : null));

        // Test with other tags
        assertFalse(tag.canContain(supTag));
        assertFalse(supTag.canContain(tag));
        assertFalse(tdTag.canContain(pTag));
        assertFalse(pTag.canContain(tdTag));
        assertFalse(ulTag.canContain(ulTag));
        assertFalse(tdTag.canContain(ulTag));
        assertFalse(imgTag.canContain(imgTag));
        assertFalse(imgTag.canContain(pTag));
        assertFalse(pTag.canContain(imgTag));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(new Tag("IMG").isEmpty());
        assertFalse(new Tag("P").isEmpty());
    }

    @Test
    public void testIsBlock() {
        assertTrue(new Tag("P").isBlock());
        assertFalse(new Tag("IMG").isBlock());
    }

    @Test
    public void testIsData() {
        assertFalse(new Tag("P").isData());
        assertTrue(new Tag("SCRIPT").isData());
    }

    @Test
    public void testIsInline() {
        assertFalse(new Tag("P").isInline());
        assertTrue(new Tag("IMG").isInline());
    }

    @Test
    public void testIsDataOrEmpty() {
        assertTrue(new Tag("SCRIPT").isDataOrEmpty());
        assertTrue(new Tag("IMG").isDataOrEmpty());
    }

    @Test
    public void testEqualsWithUnknownTag() {
        Tag unknownTag = new Tag("UNKNOWN");
        assertFalse(tag.equals(unknownTag));
    }

    @Test(expected = NullPointerException.class)
    public void testNullTag() {
        tag.canContain(null);
    }
}