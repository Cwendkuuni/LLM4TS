import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagGetterMethods() {
 String expectedTagName = "div";
 Tag tag = new Tag(expectedTagName);

 String actualTagName = tag.getName();
 Assert.assertEquals(expectedTagName, actualTagName);

 boolean expectedIsBlock = true;
 boolean actualIsBlock = tag.isBlock();
 Assert.assertEquals(expectedIsBlock, actualIsBlock);

 boolean expectedFormatAsBlock = true;
 boolean actualFormatAsBlock = tag.formatAsBlock();
 Assert.assertEquals(expectedFormatAsBlock, actualFormatAsBlock);

 boolean expectedCanContainBlock = true;
 boolean actualCanContainBlock = tag.canContainBlock();
 Assert.assertEquals(expectedCanContainBlock, actualCanContainBlock);

 boolean expectedIsInline = false;
 boolean actualIsInline = tag.isInline();
 Assert.assertEquals(expectedIsInline, actualIsInline);

 boolean expectedIsData = false;
 boolean actualIsData = tag.isData();
 Assert.assertEquals(expectedIsData, actualIsData);

 boolean expectedIsEmpty = false;
 boolean actualIsEmpty = tag.isEmpty();
 Assert.assertEquals(expectedIsEmpty, actualIsEmpty);

 boolean expectedIsSelfClosing = false;
 boolean actualIsSelfClosing = tag.isSelfClosing();
 Assert.assertEquals(expectedIsSelfClosing, actualIsSelfClosing);

 boolean expectedIsKnownTag = true;
 boolean actualIsKnownTag = Tag.isKnownTag(expectedTagName);
 Assert.assertEquals(expectedIsKnownTag, actualIsKnownTag);
 }

 @Test
 public void testTagValueOfMethod() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);

 String actualTagName = tag.getName();
 Assert.assertEquals(tagName, actualTagName);

 boolean expectedIsBlock = true;
 boolean actualIsBlock = tag.isBlock();
 Assert.assertEquals(expectedIsBlock, actualIsBlock);

 boolean expectedFormatAsBlock = true;
 boolean actualFormatAsBlock = tag.formatAsBlock();
 Assert.assertEquals(expectedFormatAsBlock, actualFormatAsBlock);

 boolean expectedCanContainBlock = true;
 boolean actualCanContainBlock = tag.canContainBlock();
 Assert.assertEquals(expectedCanContainBlock, actualCanContainBlock);

 boolean expectedIsInline = false;
 boolean actualIsInline = tag.isInline();
 Assert.assertEquals(expectedIsInline, actualIsInline);

 boolean expectedIsData = false;
 boolean actualIsData = tag.isData();
 Assert.assertEquals(expectedIsData, actualIsData);

 boolean expectedIsEmpty = false;
 boolean actualIsEmpty = tag.isEmpty();
 Assert.assertEquals(expectedIsEmpty, actualIsEmpty);

 boolean expectedIsSelfClosing = false;
 boolean actualIsSelfClosing = tag.isSelfClosing();
 Assert.assertEquals(expectedIsSelfClosing, actualIsSelfClosing);
 }

 @Test
 public void testTagIsBlock() {
 String tagName1 = "div";
 Tag tag1 = new Tag(tagName1);
 boolean expectedIsBlock1 = true;
 boolean actualIsBlock1 = tag1.isBlock();
 Assert.assertEquals(expectedIsBlock1, actualIsBlock1);

 String tagName2 = "input";
 Tag tag2 = new Tag(tagName2);
 boolean expectedIsBlock2 = false;
 boolean actualIsBlock2 = tag2.isBlock();
 Assert.assertEquals(expectedIsBlock2, actualIsBlock2);
 }

 @Test
 public void testTagFormatAsBlock() {
 String tagName1 = "p";
 Tag tag1 = new Tag(tagName1);
 boolean expectedFormatAsBlock1 = true;
 boolean actualFormatAsBlock1 = tag1.formatAsBlock();
 Assert.assertEquals(expectedFormatAsBlock1, actualFormatAsBlock1);

 String tagName2 = "span";
 Tag tag2 = new Tag(tagName2);
 boolean expectedFormatAsBlock2 = false;
 boolean actualFormatAsBlock2 = tag2.formatAsBlock();
 Assert.assertEquals(expectedFormatAsBlock2, actualFormatAsBlock2);
 }

 @Test
 public void testTagCanContainBlock() {
 String tagName1 = "div";
 Tag tag1 = new Tag(tagName1);
 boolean expectedCanContainBlock1 = true;
 boolean actualCanContainBlock1 = tag1.canContainBlock();
 Assert.assertEquals(expectedCanContainBlock1, actualCanContainBlock1);

 String tagName2 = "input";
 Tag tag2 = new Tag(tagName2);
 boolean expectedCanContainBlock2 = false;
 boolean actualCanContainBlock2 = tag2.canContainBlock();
 Assert.assertEquals(expectedCanContainBlock2, actualCanContainBlock2);
 }

 @Test(expected = AssertionError.class)
 public void testTagUnknownTag() {
 String tagName = "unknownTag";
 Tag tag = Tag.valueOf(tagName);
 }

 @Test
 public void testTagIsKnownTag() {
 String tagName1 = "div";
 boolean expectedIsKnownTag1 = true;
 boolean actualIsKnownTag1 = Tag.isKnownTag(tagName1);
 Assert.assertEquals(expectedIsKnownTag1, actualIsKnownTag1);

 String tagName2 = "unknownTag";
 boolean expectedIsKnownTag2 = false;
 boolean actualIsKnownTag2 = Tag.isKnownTag(tagName2);
 Assert.assertEquals(expectedIsKnownTag2, actualIsKnownTag2);
 }
}