import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;
import org.jsoup.parser.ParseSettings;

public class TagTest {

    @Test
    public void testTagName() {
        Tag htmlTag = Tag.valueOf("html");
        assertEquals("html", htmlTag.getName());

        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.getName());

        Tag unknownTag = Tag.valueOf("unknown");
        assertEquals("unknown", unknownTag.getName());
    }

    @Test
    public void testBlockTag() {
        Tag htmlTag = Tag.valueOf("html");
        assertTrue(htmlTag.isBlock());

        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.formatAsBlock());

        Tag brTag = Tag.valueOf("br");
        assertFalse(brTag.formatAsBlock());

        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.canContainBlock()); // synonym of isBlock()

        Tag divTag = Tag.valueOf("div");
        assertTrue(divTag.canContainBlock());

        Tag brTag = Tag.valueOf("br");
        assertFalse(brTag.canContainBlock());
    }

    @Test
    public void testIsDataTag() {
        Tag scriptTag = Tag.valueOf("script");
        assertTrue(scriptTag.isData());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isData());
    }

    @Test
    public void testIsEmptyTag() {
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isEmpty());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isSelfClosing());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("html"));
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isFormListed());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag inputTag = Tag.valueOf("input");
        assertTrue(inputTag.isFormSubmittable());

        Tag textareaTag = Tag.valueOf("textarea");
        assertTrue(textareaTag.isFormSubmittable());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isFormSubmittable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTagName() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTagName() {
        Tag.valueOf("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCaseSensitiveTagName() {
        Tag.valueOf("HTML");
    }
}