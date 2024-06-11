import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testValueOfBlockTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 }

 @Test
 public void testValueOfUnknownTag() {
 String tagName = "mytag";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isKnownTag());
assertEquals(tagName.toLowerCase(), tag.getName());
 }

 @Test
 public void testValueOfEmptyTag() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testCanContainBlockTag() {
 String tagName1 = "div";
 String tagName2 = "p";
 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);
 assertTrue(tag1.canContainBlock());
 assertTrue(tag1.canContainBlock(tag2));
 }

 @Test
 public void testCannotContainBlockTag() {
 String tagName1 = "a";
 String tagName2 = "div";
 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);
 assertFalse(tag1.canContainBlock());
 assertFalse(tag1.canContainBlock(tag2));
 }

 @Test(expected = IllegalStateException.class)
 public void testUnknownTagAsArgumentOfCanContainBlockMethod() {
 String unknownTagName = "mytag";
 String blockTagName = "div";
 Tag blockTag = Tag.valueOf(blockTagName);
 Tag unknownTag = new Tag(unknownTagName);
 blockTag.canContainBlock(unknownTag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagAsArgumentOfCanContainBlockMethod() {
 String blockTagName = "div";
 Tag blockTag = Tag.valueOf(blockTagName);
 Tag emptyTag = Tag.valueOf("img");
 try {
 blockTag.canContainBlock(emptyTag);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFormatAsBlock() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testFormatAsInline() {
 String tagName = "span";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testIsBlock() {
 String tagName1 = "p";
 String tagName2 = "span";
 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);
 assertTrue(tag1.isBlock());
 assertFalse(tag2.isBlock());
 }

 @Test
 public void testIsInline() {
 String tagName1 = "p";
 String tagName2 = "span";
 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);
 assertFalse(tag1.isInline());
 assertTrue(tag2.isInline());
 }

 @Test
 public void testIsData() {
 String tagName = "script";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isData());
 }

 @Test
 public void testIsEmpty() {
 String tagName1 = "p";
 String tagName2 = "img";
 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);
 assertFalse(tag1.isEmpty());
 assertTrue(tag2.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 String tagName1 = "p";
 String tagName2 = "img";
 Tag tag1 = Tag.valueOf(tagName1);
 Tag tag2 = Tag.valueOf(tagName2);
 assertFalse(tag1.isSelfClosing());
 assertTrue(tag2.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(Tag.isKnownTag(tagName));
 }

 @Test
 public void testIsKnownTagUnknownTag() {
 String unknownTagName = "mytag";
 assertFalse(Tag.isKnownTag(unknownTagName));
 }

 @Test
 public void testPreserveWhitespace() {
 String tagName = "pre";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testNewTagWithCustomProperties() {
 String tagName = "mytag";
 Tag tag = new Tag(tagName).setSelfClosing();
 assertEquals(tagName.toLowerCase(), tag.getName());
 assertTrue(tag.isSelfClosing());
 }
}