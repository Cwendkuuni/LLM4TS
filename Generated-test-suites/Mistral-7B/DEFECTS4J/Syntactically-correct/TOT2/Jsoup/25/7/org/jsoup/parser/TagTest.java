import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.Tag;

public class TagTest {
    // Test for getName() method
    @Test
    public void testgetName() {
        Tag tag = new Tag("div");
        String expectedName = "div";
        String actualName = tag.getName();
        Assert.assertEquals(expectedName, actualName);
    }

    // Test for valueOf() method with pre-defined tag
    @Test
    public void testValueOfForKnownTag() {
        String tagName = "p";
        Tag tag = Tag.valueOf(tagName);
        boolean expectedIsKnownTag = true;
        boolean actualIsKnownTag = tag.isKnownTag();
        Assert.assertEquals(expectedIsKnownTag, actualIsKnownTag);
    }

    // Test for valueOf() method with unknown tag
    @Test
    public void testValueOfForUnknownTag() {
        String tagName = "myTag";
        Tag tag = Tag.valueOf(tagName);
        String expectedTagName = tagName;
        String actualTagName = tag.getName();
        Assert.assertEquals(expectedTagName, actualTagName);
        boolean expectedIsKnownTag = false;
        boolean actualIsKnownTag = tag.isKnownTag();
        Assert.assertEquals(expectedIsKnownTag, actualIsKnownTag);
    }

    // Test for isBlock() method
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        boolean expectedIsBlock = true;
        boolean actualIsBlock = tag.isBlock();
        Assert.assertEquals(expectedIsBlock, actualIsBlock);

        tag = new Tag("span");
        expectedIsBlock = false;
        actualIsBlock = tag.isBlock();
        Assert.assertEquals(expectedIsBlock, actualIsBlock);
    }

    // Test for canContainBlock() method
    @Test
    public void testCanContainBlock() {
        Tag pTag = new Tag("p");
        boolean expectedCanContainBlock = true;
        boolean actualCanContainBlock = pTag.canContainBlock();
        Assert.assertEquals(expectedCanContainBlock, actualCanContainBlock);

        Tag imgTag = new Tag("img");
        expectedCanContainBlock = false;
        actualCanContainBlock = imgTag.canContainBlock();
        Assert.assertEquals(expectedCanContainBlock, actualCanContainBlock);
    }

    // Test for isData() method
    @Test
    public void testIsData() {
        Tag scriptTag = new Tag("script");
        boolean expectedIsData = true;
        boolean actualIsData = scriptTag.isData();
        Assert.assertEquals(expectedIsData, actualIsData);

        Tag pTag = new Tag("p");
        expectedIsData = false;
        actualIsData = pTag.isData();
        Assert.assertEquals(expectedIsData, actualIsData);
    }

    // Test for isEmpty() method
    @Test
    public void testIsEmpty() {
        Tag imgTag = new Tag("img");
        boolean expectedIsEmpty = true;
        boolean actualIsEmpty = imgTag.isEmpty();
        Assert.assertEquals(expectedIsEmpty, actualIsEmpty);

        Tag pTag = new Tag("p");
        expectedIsEmpty = false;
        actualIsEmpty = pTag.isEmpty();
        Assert.assertEquals(expectedIsEmpty, actualIsEmpty);
    }

    // Test for isSelfClosing() method
    @Test
    public void testIsSelfClosing() {
        Tag imgTag = new Tag("img");
        boolean expectedIsSelfClosing = true;
        boolean actualIsSelfClosing = imgTag.isSelfClosing();
        Assert.assertEquals(expectedIsSelfClosing, actualIsSelfClosing);

        Tag pTag = new Tag("p");
        expectedIsSelfClosing = false;
        actualIsSelfClosing = pTag.isSelfClosing();
        Assert.assertEquals(expectedIsSelfClosing, actualIsSelfClosing);
    }

    // Test for isKnownTag() method
    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("div");
        boolean expectedIsKnownTag = true;
        boolean actualIsKnownTag = tag.isKnownTag();
        Assert.assertEquals(expectedIsKnownTag, actualIsKnownTag);

        String unknownTagName = "myTag";
        tag = new Tag(unknownTagName);
        expectedIsKnownTag = false;
        actualIsKnownTag = tag.isKnownTag();
        Assert.assertEquals(expectedIsKnownTag, actualIsKnownTag);
    }
}