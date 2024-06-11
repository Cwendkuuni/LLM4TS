import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testTagNormalName() {
 Tag tag = Tag.valueOf("P");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testUnknownTag() {
 Tag tag = Tag.valueOf("unknownTag");
 assertFalse(tag.isKnownTag());
 }

 @Test
 public void testBlockTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testInlineTag() {
 Tag tag = Tag.valueOf("a");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testEmptyTag() {
 Tag tag = Tag.valueOf("img");
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testSelfClosingTag() {
 Tag tag = Tag.valueOf("img");
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag tag = Tag.valueOf("pre");
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testFormListTag() {
 Tag tag = Tag.valueOf("input");
 assertTrue(tag.isFormListed());
 }

 @Test
 public void testFormSubmitTag() {
 Tag tag = Tag.valueOf("input");
 assertTrue(tag.isFormSubmittable());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = Tag.valueOf(null);
 // should throw NullPointerException
 }

 @Test
 public void testCaseInsensitiveTagName() {
 Tag tag1 = Tag.valueOf("P");
 Tag tag2 = Tag.valueOf("p");
 assertEquals(tag1, tag2);
 }

 @Test
 public void testEmptyTagsWithData() {
 Tag imgTag = Tag.valueOf("img");
 imgTag.setSelfClosing(false);
 imgTag.canContainInline = true;
 imgTag.empty = false;
 String data = "test data";
 assertEquals(Tag.valueOf("img"), imgTag);
 assertEquals(data, imgTag.toString());
 }

 @Test
 public void testRegisteredTag() {
 Tag pTag = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 assertEquals(pTag, pTag2);
 }

 @Test
 public void testRegisteredUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknownTag");
 Tag unknownTag2 = Tag.valueOf("unknownTag");
 assertFalse(unknownTag.equals(unknownTag2));
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = Tag.valueOf("a");
 assertEquals(tag1, tag2);
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testParseSettingsNormalizeTag() {
 ParseSettings settings = ParseSettings.preserveCase;
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("P", settings);
 assertEquals(tag1, tag2);
 }
}