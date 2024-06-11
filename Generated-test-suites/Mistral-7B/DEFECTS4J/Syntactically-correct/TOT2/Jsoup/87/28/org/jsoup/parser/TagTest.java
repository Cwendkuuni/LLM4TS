import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.Tag;

public class TagTest {
    @Test
    public void testTagName() {
        Tag tag = new Tag("div");
        Assert.assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOfWithPredefinedTag() {
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals(Tag.class, tag.getClass()); // predefined tag is an instance of Tag
        Assert.assertTrue(tag.isBlock()); // p is a block element
    }

    @Test
    public void testValueOfWithUnknownTag() {
        Tag tag = Tag.valueOf("newTag");
        Assert.assertTrue(tag.isInline()); // unknown tags default to inline
        Assert.assertFalse(tag.isBlock());
        Assert.assertEquals("newTag", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        Assert.assertTrue(tag.isBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("span");
        Assert.assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("script");
        Assert.assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        Assert.assertFalse(tag.isEmpty());

        tag = new Tag("img").setEmpty();
        Assert.assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("img");
        Assert.assertFalse(tag.isSelfClosing());

        tag = new Tag("img").setSelfClosing();
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Assert.assertTrue(Tag.isKnownTag("p"));
        Assert.assertFalse(Tag.isKnownTag("newTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        Assert.assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("button");
        Assert.assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("input");
        Assert.assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testNewTagWithEmptyTagName() {
        try {
            new Tag("");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNewTagWithNullTagName() {
        try {
            new Tag(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testValueOfWithNullTagName() {
        try {
            Tag tag = Tag.valueOf(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testValueOfWithEmptyTagName() {
        try {
            Tag tag = Tag.valueOf("");
            Assert.assertFalse(tag.isBlock()); // empty tag names default to inline
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("div");

        Assert.assertEquals(tag1, tag2);
        Assert.assertNotEquals(tag1, tag3);
        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
        Assert.assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("p");
        Assert.assertEquals("p", tag.toString());
    }

    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isBlock());
        }
    }

    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.isBlock());
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isEmpty());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertFalse(tag.formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.preserveWhitespace());
        }
    }

    @Test
    public void testFormListedTags() {
        for (String tagName : Tag.formListedTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isFormListed());
        }
    }

    @Test
    public void testFormSubmitTags() {
        for (String tagName : Tag.formSubmitTags) {
            Tag tag = Tag.valueOf(tagName);
            Assert.assertTrue(tag.isFormSubmittable());
        }
    }
}