import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TagTest {

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.tagName);
        tag = Tag.valueOf("DIV");
        assertEquals("div", tag.tagName);
        tag = Tag.valueOf("UNKNOWN");
        assertNotSame(tag, Tag.valueOf("p"));
        assertTrue(tag.isData());
    }

    @Test
    public void testCanContain() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");
        Tag headTag = Tag.valueOf("head");
        Tag bodyTag = Tag.valueOf("body");
        Tag titleTag = Tag.valueOf("title");

        // Check p Tag can contain div Tag
        assertTrue(pTag.canContain(divTag));

        // Check div Tag can contain p Tag
        assertTrue(divTag.canContain(pTag));

        // Check p Tag cannot contain p Tag
        assertFalse(pTag.canContain(pTag));

        // Check head Tag cannot contain p Tag
        assertFalse(headTag.canContain(pTag));

        // Check body Tag can contain any tag
        assertTrue(bodyTag.canContain(pTag));
        assertTrue(bodyTag.canContain(divTag));

        // Check title Tag can only contain specific tags
        List<String> validChildren = Arrays.asList("SCRIPT", "STYLE", "LINK", "META", "title", "base", "BODY");
        for (String tagName : validChildren) {
            Tag childTag = Tag.valueOf(tagName);
            assertTrue(titleTag.canContain(childTag));
        }

        // Check title Tag cannot contain p Tag
        assertFalse(titleTag.canContain(pTag));
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());

        Tag spanTag = Tag.valueOf("span");
        assertFalse(spanTag.isBlock());
    }

    @Test
    public void testIsData() {
        Tag dataTag = Tag.valueOf("DATA");
        assertTrue(dataTag.isData());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isEmpty());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.preserveWhitespace());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTagName() {
        Tag tag = new Tag("Tag");
    }

    @Test
    public void testEqualsAndHashcode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Tag tag3 = Tag.valueOf("DIV");

        assertEquals(tag1, tag1);
        assertEquals(tag1, tag2);

        // Check hashcode equals for same tag
        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag1.hashCode();
        assertEquals(hashCode1, hashCode2);

        // Check hashcode different for different tags
        int hashCode3 = tag3.hashCode();
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.toString());
    }
}