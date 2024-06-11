import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        String expected = "p";
        assertEquals(expected, tag.getName());

        tag = Tag.valueOf("DIV");
        expected = "div";
        assertEquals(expected, tag.getName());

        tag = Tag.valueOf("UNKNOWN_TAG");
        Tag expectedTag = new Tag("UNKNOWN_TAG");
        assertEquals(expectedTag, tag);
    }

    @Test
    public void testCanContain() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        assertTrue(pTag.canContain(divTag));

        Tag pTagWithDataOnly = new Tag("p").setContainDataOnly();
        assertFalse(pTagWithDataOnly.canContain(divTag));

        Tag divTagWithDataOnly = new Tag("div").setContainDataOnly();
        assertFalse(pTag.canContain(divTagWithDataOnly));

        Tag headTag = Tag.valueOf("head");
        assertTrue(headTag.canContain(pTag));
        assertTrue(headTag.canContain(divTag));
        assertFalse(headTag.canContain(headTag));
    }

    @Test(expected = NullPointerException.class)
    public void testCanContainWithNullChild() {
        Tag tag = Tag.valueOf("p");
        Tag nullChild = null;
        tag.canContain(nullChild);
    }

    // Add other test methods for other methods in the Tag class
}