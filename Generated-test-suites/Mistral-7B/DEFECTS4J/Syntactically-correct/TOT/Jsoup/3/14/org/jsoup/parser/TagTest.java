import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

    @Test
    public void testValueOf() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag unknownTag = Tag.valueOf("UnknownTag");

        // Test getting a pre-defined tag
        assertEquals("p", pTag.getName());
        assertEquals(pTag, pTag.canContain(new Tag("p")));
        assertEquals(true, pTag.isBlock());

        // Test getting a new generic tag
        assertEquals("UnknownTag", unknownTag.getName());
        assertEquals(false, unknownTag.isBlock());
        assertEquals(true, unknownTag.canContainBlock());

        // Test getting a non-existent tag
        assertNull(Tag.valueOf("NonexistentTag"));
    }

    @Test
    public void testCanContain() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("BODY");
        Tag headTag = Tag.valueOf("HEAD");
        Tag imageTag = Tag.valueOf("IMG");
        Tag scriptTag = Tag.valueOf("SCRIPT");

        // Test that a 'p' tag can contain a 'p' tag as a child
        assertTrue(pTag.canContain(pTag));

        // Test that a 'p' tag cannot contain a 'div' tag as a child
        assertFalse(pTag.canContain(divTag));

        // Test that a 'BODY' tag can contain any tag as a child
        assertTrue(bodyTag.canContain(pTag));
        assertTrue(bodyTag.canContain(divTag));

        // Test that a 'HEAD' tag can only contain certain tags as children
        assertFalse(headTag.canContain(pTag));
        assertTrue(headTag.canContain(scriptTag));
        assertTrue(headTag.canContain(imageTag));

        // Test that a 'p' tag cannot contain its parent tag
        assertFalse(pTag.canContain(pTag.getImplicitParent()));

        // Test that a tag can only contain tags that are its descendants
        Tag dlTag = Tag.valueOf("DL");
        Tag dtTag = Tag.valueOf("DT");
        Tag ddTag = Tag.valueOf("DD");
        assertFalse(dlTag.canContain(pTag));
        assertTrue(dlTag.canContain(dtTag));
        assertTrue(dlTag.canContain(ddTag));
        assertTrue(dtTag.canContain(ddTag));
    }

    @Test
    public void testEqualsAndHashcode() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        // Test that two equal tags have the same hashcode
        assertEquals(pTag1.hashCode(), pTag2.hashCode());

        // Test that two equal tags have the same equals method result
        assertEquals(pTag1, pTag2);

        // Test that two different tags have different hashcodes
        assertNotEquals(pTag1.hashCode(), divTag.hashCode());

        // Test that two different tags have different equals method results
        assertNotEquals(pTag1, divTag);
    }

    @Test
    public void testToString() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullParameter() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyParameter() {
        Tag.valueOf("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalParameter() {
        Tag.valueOf("ILLEGAL_TAG");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParentChildRelation() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag headTag = Tag.valueOf("HEAD");

        // Test that a 'p' tag cannot be a parent of a 'div' tag
        assertFalse(pTag.isValidParent(divTag));

        // Test that a 'HEAD' tag cannot be a parent of a 'p' tag
        assertFalse(headTag.isValidParent(pTag));
    }

    @Test
    public void testIsData() {
        Tag pTag = Tag.valueOf("p");
        Tag scriptTag = Tag.valueOf("SCRIPT");

        // Test that a 'p' tag is not a data-only tag
        assertFalse(pTag.isData());

        // Test that a 'SCRIPT' tag is a data-only tag
        assertTrue(scriptTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag pTag = Tag.valueOf("p");
        Tag imgTag = Tag.valueOf("IMG");
        Tag brTag = Tag.valueOf("BR");

        // Test that a 'p' tag is not an empty tag
        assertFalse(pTag.isEmpty());

        // Test that an 'IMG' tag is an empty tag
        assertTrue(imgTag.isEmpty());

        // Test that a 'BR' tag is an empty tag
        assertTrue(brTag.isEmpty());
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag spanTag = Tag.valueOf("SPAN");

        // Test that a 'p' tag is a block tag
        assertTrue(pTag.isBlock());

        // Test that a 'SPAN' tag is not a block tag
        assertFalse(spanTag.isBlock());
    }

    @Test
    public void testIsInline() {
        Tag pTag = Tag.valueOf("p");
        Tag spanTag = Tag.valueOf("SPAN");

        // Test that a 'p' tag is a block tag (opposite of isInline())
        assertFalse(pTag.isInline());

        // Test that a 'SPAN' tag is an inline tag
        assertTrue(spanTag.isInline());
    }

    @Test
    public void testIsOptionalClosing() {
        Tag pTag = Tag.valueOf("p");
        Tag aTag = Tag.valueOf("A");

        // Test that a 'p' tag is not an optional closing tag
        assertFalse(pTag.isOptionalClosing());

        // Test that an 'A' tag is an optional closing tag
        assertTrue(aTag.isOptionalClosing());
    }
}