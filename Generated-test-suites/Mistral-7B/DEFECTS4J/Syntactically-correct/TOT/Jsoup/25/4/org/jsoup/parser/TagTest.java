import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testNewTagCreation() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);
 assertEquals(tagName.toLowerCase(), tag.getName());
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testNewTagCreationUnknown() {
 String tagName = "foo";
 Tag tag = Tag.valueOf(tagName);
 assertEquals("generic", tag.getName());
 assertFalse(tag.isBlock());
 assertTrue(tag.canContainBlock());
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
 public void testEmptyTag() {
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
 public void testSelfClosingTag() {
 String tagName = "input";
 Tag tag = new Tag(tagName);
 tag.setSelfClosing();
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertTrue(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testKnownTag() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(Tag.isKnownTag(tagName));
 }

 @Test
 public void testUnknownTag() {
 String tagName = "foo";
 boolean result = Tag.isKnownTag(tagName);
 assertFalse(result);
 }

 @Test
 public void testPreserveWhitespaceTag() {
 String tagName = "pre";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.preserveWhitespace());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = Tag.valueOf(null);
 fail("Expected NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 String tagName = "";
 Tag tag = Tag.valueOf(tagName);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 String tagName = "1tag";
 Tag tag = Tag.valueOf(tagName);
 fail("Expected IllegalArgumentException");
 }
}