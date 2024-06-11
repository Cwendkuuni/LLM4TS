import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.Tag;

public class TagTest {
    @Test
    public void testTagName() {
        Tag tag = new Tag("div");
        String name = tag.getName();
        Assert.assertEquals("div", name);
    }

    @Test
    public void testLowerCaseTagName() {
        Tag tag = new Tag("div");
        String name = Tag.valueOf("div").getName();
        Assert.assertEquals("div", name);

        Tag tag2 = new Tag("DIV");
        String name2 = Tag.valueOf("DIV").getName();
        Assert.assertEquals("div", name2);
    }

    @Test
    public void testIsBlockTag() {
        Tag tag = new Tag("div");
        boolean result = tag.isBlock();
        Assert.assertTrue(result);

        Tag tagInline = new Tag("span");
        result = tagInline.isBlock();
        Assert.assertFalse(result);
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("div");
        boolean result = tag.formatAsBlock();
        Assert.assertTrue(result);

        Tag tagInline = new Tag("span");
        result = tagInline.formatAsBlock();
        Assert.assertFalse(result);
    }

    @Test
    public void testCanContainBlockTag() {
        Tag div = new Tag("div");
        boolean result = div.canContainBlock();
        Assert.assertTrue(result);

        Tag span = new Tag("span");
        result = span.canContainBlock();
        Assert.assertFalse(result);
    }

    @Test
    public void testIsDataTag() {
        Tag tag = new Tag("script");
        boolean result = tag.isData();
        Assert.assertTrue(result);

        Tag div = new Tag("div");
        result = div.isData();
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEmptyTag() {
        Tag tag = new Tag("img");
        boolean result = tag.isEmpty();
        Assert.assertFalse(result);

        Tag imgEmpty = new Tag("img").setSelfClosing();
        result = imgEmpty.isEmpty();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsSelfClosingTag() {
        Tag img = new Tag("img");
        boolean result = img.isSelfClosing();
        Assert.assertFalse(result);

        Tag imgSelfClosing = new Tag("img").setSelfClosing();
        result = imgSelfClosing.isSelfClosing();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsKnownTag() {
        boolean result = Tag.isKnownTag("div");
        Assert.assertTrue(result);

        result = Tag.isKnownTag("unknown");
        Assert.assertFalse(result);
    }

    @Test
    public void testPreserveWhitespaceTag() {
        Tag tag = new Tag("pre");
        boolean result = tag.preserveWhitespace();
        Assert.assertTrue(result);

        Tag tagNotPreserveWhitespace = new Tag("span");
        result = tagNotPreserveWhitespace.preserveWhitespace();
        Assert.assertFalse(result);
    }

    @Test
    public void testIsFormListedTag() {
        Tag tag = new Tag("input");
        boolean result = tag.isFormListed();
        Assert.assertTrue(result);

        Tag tagNotFormListed = new Tag("div");
        result = tagNotFormListed.isFormListed();
        Assert.assertFalse(result);
    }

    @Test
    public void testIsFormSubmittableTag() {
        Tag tag = new Tag("input");
        boolean result = tag.isFormSubmittable();
        Assert.assertTrue(result);

        Tag tagNotFormSubmittable = new Tag("div");
        result = tagNotFormSubmittable.isFormSubmittable();
        Assert.assertFalse(result);
    }
}