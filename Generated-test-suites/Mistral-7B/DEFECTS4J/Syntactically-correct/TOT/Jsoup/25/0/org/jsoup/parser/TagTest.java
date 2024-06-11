import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagCreation() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);
 assertEquals("div", tag.getName());
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testUnknownTagCreation() {
 String tagName = "unknown";
 Tag tag = Tag.valueOf(tagName);
 assertNotNull(tag);
 assertEquals("unknown", tag.getName());
 assertFalse(tag.isBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testBlockTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testInlineTag() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertTrue(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testEmptyTag() {
 String tagName = "input";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertTrue(tag.isEmpty());
 assertTrue(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testSelfClosingTag() {
 String tagName = "br";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertTrue(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testPreserveWhitespaceTag() {
 String tagName = "pre";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertTrue(tag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 String tagName = null;
 Tag tag = Tag.valueOf(tagName);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 String tagName = "";
 Tag tag = Tag.valueOf(tagName);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithSpecialCharacters() {
 String tagName = "<tag>";
 Tag tag = Tag.valueOf(tagName);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testIsKnownTag() {
 String tagName1 = "p";
 String tagName2 = "div";
 assertTrue(Tag.isKnownTag(tagName1));
 assertTrue(Tag.isKnownTag(tagName2));
 }

 @Test
 public void testIsKnownTagFalse() {
 String tagName = "unknownTag";
 assertFalse(Tag.isKnownTag(tagName));
 }
}