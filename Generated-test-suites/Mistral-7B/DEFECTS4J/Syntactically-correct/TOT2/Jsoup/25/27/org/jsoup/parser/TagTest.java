import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = Tag.valueOf("div");
 String expectedTagName = "div";
 String actualTagName = tag.getName();
 assertEquals(expectedTagName, actualTagName);
 }

 @Test
 public void testUnknownTag() {
 Tag tag = Tag.valueOf("abc");
 assertTrue(tag.isUnknownTag());
 }

 @Test
 public void testIsBlock() {
 Tag tag = Tag.valueOf("div");
 boolean expectedIsBlock = true;
 boolean actualIsBlock = tag.isBlock();
 assertEquals(expectedIsBlock, actualIsBlock);

 Tag tagInline = Tag.valueOf("span");
 boolean expectedIsBlockInline = false;
 boolean actualIsBlockInline = tagInline.isBlock();
 assertEquals(expectedIsBlockInline, actualIsBlockInline);
 }

 @Test
 public void testFormatAsBlock() {
 Tag tag = Tag.valueOf("div");
 boolean expectedFormatAsBlock = true;
 boolean actualFormatAsBlock = tag.formatAsBlock();
 assertEquals(expectedFormatAsBlock, actualFormatAsBlock);

 Tag tagInline = Tag.valueOf("span");
 boolean expectedFormatAsBlockInline = false;
 boolean actualFormatAsBlockInline = tagInline.formatAsBlock();
 assertEquals(expectedFormatAsBlockInline, actualFormatAsBlockInline);
 }

 @Test
 public void testCanContainBlock() {
 Tag tag = Tag.valueOf("div");
 boolean expectedCanContainBlock = true;
 boolean actualCanContainBlock = tag.canContainBlock();
 assertEquals(expectedCanContainBlock, actualCanContainBlock);

 Tag tagInline = Tag.valueOf("span");
 boolean expectedCanContainBlockInline = false;
 boolean actualCanContainBlockInline = tagInline.canContainBlock();
 assertEquals(expectedCanContainBlockInline, actualCanContainBlockInline);
 }

 @Test
 public void testIsData() {
 Tag tag = Tag.valueOf("script");
 boolean expectedIsData = true;
 boolean actualIsData = tag.isData();
 assertEquals(expectedIsData, actualIsData);

 Tag tagNotData = Tag.valueOf("div");
 boolean expectedIsDataNotData = false;
 boolean actualIsDataNotData = tagNotData.isData();
 assertEquals(expectedIsDataNotData, actualIsDataNotData);
 }

 @Test
 public void testIsEmpty() {
 Tag tag = Tag.valueOf("img");
 boolean expectedIsEmpty = false;
 boolean actualIsEmpty = tag.isEmpty();
 assertEquals(expectedIsEmpty, actualIsEmpty);

 Tag tagEmpty = Tag.valueOf("br");
 boolean expectedIsEmptyEmpty = true;
 boolean actualIsEmptyEmpty = tagEmpty.isEmpty();
 assertEquals(expectedIsEmptyEmpty, actualIsEmptyEmpty);
 }

 @Test
 public void testIsSelfClosing() {
 Tag tagSelfClosing = Tag.valueOf("img");
 tagSelfClosing.setSelfClosing();
 boolean expectedIsSelfClosing = true;
 boolean actualIsSelfClosing = tagSelfClosing.isSelfClosing();
 assertEquals(expectedIsSelfClosing, actualIsSelfClosing);

 Tag tagNotSelfClosing = Tag.valueOf("div");
 boolean expectedIsSelfClosingNotSelfClosing = false;
 boolean actualIsSelfClosingNotSelfClosing = tagNotSelfClosing.isSelfClosing();
 assertEquals(expectedIsSelfClosingNotSelfClosing, actualIsSelfClosingNotSelfClosing);
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("div"));
 assertFalse(Tag.isKnownTag("abc"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag tag = null;
 try {
 tag = new Tag(null);
 } catch (IllegalArgumentException e) {
 // expected
 }

 assertNull(tag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = null;
 try {
 tag = new Tag("");
 } catch (IllegalArgumentException e) {
 // expected
 }

 assertNull(tag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWhitespaceTagName() {
 Tag tag = null;
 try {
 tag = new Tag(" Tag ");
 } catch (IllegalArgumentException e) {
 // expected
 }

 assertNull(tag);
 }
}