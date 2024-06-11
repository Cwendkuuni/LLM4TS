import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

    @Test
    public void testTagName() {
        Tag tag = new Tag("div");
        String actual = tag.getName();
        String expected = "div";
        assert(expected.equals(actual));
    }

    @Test
    public void testValueOfTagName() {
        Tag tag = Tag.valueOf("div");
        String actual = tag.getName();
        String expected = "div";
        assert(expected.equals(actual));
    }

    @Test
    public void testValueOfUnknownTagName() {
        Tag tag = Tag.valueOf("Xyz");
        assert(tag.isSelfClosing());
    }

    @Test
    public void testIsBlock() {
        Tag divTag = new Tag("div");
        boolean actual = divTag.isBlock();
        assertTrue(actual);

        Tag spanTag = new Tag("span");
        actual = spanTag.isBlock();
        assertFalse(actual);
    }

    @Test
    public void testFormatAsBlock() {
        Tag pTag = new Tag("p");
        boolean actual = pTag.formatAsBlock();
        assertTrue(actual);

        Tag inputTag = new Tag("input");
        actual = inputTag.formatAsBlock();
        assertFalse(actual);
    }

    @Test
    public void testIsData() {
        Tag scriptTag = new Tag("script");
        boolean actual = scriptTag.isData();
        assertTrue(actual);

        Tag pTag = new Tag("p");
        actual = pTag.isData();
        assertFalse(actual);
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = new Tag("img");
        boolean actual = imgTag.isEmpty();
        assertFalse(actual);

        Tag imgTagEmpty = new Tag("img").setSelfClosing();
        actual = imgTagEmpty.isEmpty();
        assertTrue(actual);
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("Xyz"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = new Tag("pre");
        boolean actual = preTag.preserveWhitespace();
        assertTrue(actual);

        Tag pTag = new Tag("p");
        actual = pTag.preserveWhitespace();
        assertFalse(actual);
    }

    @Test
    public void testIsFormListed() {
        Tag inputTag = new Tag("input");
        boolean actual = inputTag.isFormListed();
        assertTrue(actual);

        Tag divTag = new Tag("div");
        actual = divTag.isFormListed();
        assertFalse(actual);
    }

    @Test
    public void testIsFormSubmittable() {
        Tag inputTypeSubmit = new Tag("input").setAttributes("type", "submit");
        boolean actual = inputTypeSubmit.isFormSubmittable();
        assertTrue(actual);

        Tag inputTypeText = new Tag("input").setAttributes("type", "text");
        actual = inputTypeText.isFormSubmittable();
        assertFalse(actual);
    }
}