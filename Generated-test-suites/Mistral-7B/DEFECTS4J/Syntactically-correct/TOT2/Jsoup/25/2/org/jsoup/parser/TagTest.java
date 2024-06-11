
import org.junit.Test;
import org.junit.Assert;

public class TagTest {
    // Test cases for getName() method
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        String expected = "div";
        String actual = tag.getName();
        Assert.assertEquals(expected, actual);
    }

    // Test cases for valueOf(String tagName) method
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        String expectedTagName = "div";
        String actualTagName = tag.getName();
        Assert.assertEquals(expectedTagName, actualTagName);
    }

    @Test
    public void testValueOfForKnownTag() {
        Tag pTag = Tag.valueOf("p");
        Tag imgTag = Tag.valueOf("img");

        Assert.assertEquals(pTag.getName(), "p");
        Assert.assertEquals(imgTag.getName(), "img");
    }

    @Test
    public void testValueOfForUnknownTag() {
        Tag unknownTag = Tag.valueOf("unknownTag");
        String expectedTagName = "unknownTag";
        String actualTagName = unknownTag.getName();

        Assert.assertNotEquals(expectedTagName, actualTagName);
        Assert.assertEquals(unknownTag.isInline(), true);
        Assert.assertEquals(unknownTag.canContainBlock(), false);
        Assert.assertEquals(unknownTag.formatAsBlock(), false);
    }

    // Test cases for isBlock(), formatAsBlock(), canContainBlock(), isInline(), isData(), isEmpty(), and isSelfClosing() methods
    @Test
    public void testIsBlockForInlineTag() {
        Tag pTag = new Tag("p");
        Tag imgTag = new Tag("img");

        Assert.assertTrue(pTag.isBlock());
        Assert.assertFalse(imgTag.isBlock());
    }

    @Test
    public void testFormatAsBlockForInlineTag() {
        Tag pTag = new Tag("p");
        Tag imgTag = new Tag("img");

        Assert.assertTrue(pTag.formatAsBlock());
        Assert.assertFalse(imgTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlockForInlineTag() {
        Tag pTag = new Tag("p");
        Tag imgTag = new Tag("img");

        Assert.assertTrue(pTag.canContainBlock());
        Assert.assertFalse(imgTag.canContainBlock());
    }

    @Test
    public void testIsEmptyForEmptyTag() {
        Tag imgTag = new Tag("img");
        Tag pTag = new Tag("p");

        Assert.assertTrue(imgTag.isEmpty());
        Assert.assertFalse(pTag.isEmpty());
    }

    @Test
    public void testIsSelfClosingForSelfClosingTag() {
        Tag imgTag = new Tag("img");
        Tag pTag = new Tag("p");

        Assert.assertTrue(imgTag.isSelfClosing());
        Assert.assertFalse(pTag.isSelfClosing());
    }

    @Test
    public void testIsDataForDataTag() {
        Tag metaTag = new Tag("meta");
        Assert.assertTrue(metaTag.isData());
    }

    @Test
    public void testIsDataForNonDataTag() {
        Tag pTag = new Tag("p");
        Assert.assertFalse(pTag.isData());
    }

    @Test
    public void testIsKnownTagForKnownTag() {
        Tag pTag = new Tag("p");
        Tag unknownTag = new Tag("unknownTag");

        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("unknownTag"));
    }
}
