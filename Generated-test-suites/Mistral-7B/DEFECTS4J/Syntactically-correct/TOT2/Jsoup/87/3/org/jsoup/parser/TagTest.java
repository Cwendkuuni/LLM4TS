import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;
import static org.jsoup.parser.Tag.*; // for static imports

public class TagTest {

 // Test for getName() method
 @Test
 public void testTagName() {
 Tag htmlTag = html();
 String tagName = htmlTag.getName();
 assertEquals("html", tagName);
 }

 // Test for valueOf() method with known tag
 @Test
 public void testTagValueOfKnown() {
 String tagName = "p";
 Tag pTag = Tag.valueOf(tagName);
 assertEquals(pTag.tagName, tagName);
 }

 // Test for valueOf() method with unknown tag
 @Test
 public void testTagValueOfUnknown() {
 String tagName = "x";
 Tag xTag = Tag.valueOf(tagName);
 assertNotNull(xTag);
 assertEquals(xTag.tagName, tagName);
 }

 // Test for valueOf() method with normalizeTag parameter
 @Test
 public void testTagValueOfNormalize() {
 String tagName = "P";
 Tag pTag = Tag.valueOf(tagName);
 assertEquals(pTag.tagName, "p");
 }

 // Test for isBlock() method
 @Test
 public void testIsBlock() {
 Tag htmlTag = html();
 assertTrue(htmlTag.isBlock());
 }

 // Test for formatAsBlock() method
 @Test
 public void testFormatAsBlock() {
 Tag pTag = p();
 assertTrue(pTag.formatAsBlock());
 }

 // Test for canContainBlock() method (deprecated)
 @Test
 public void testCanContainBlock() {
 Tag pTag = p();
 assertTrue(pTag.isBlock()); // equivalent to canContainBlock()
 }

 // Test for isInline() method
 @Test
 public void testIsInline() {
 Tag imgTag = img();
 assertFalse(imgTag.isBlock());
 }

 // Test for isData() method
 @Test
 public void testIsData() {
 Tag metaTag = meta();
 assertTrue(metaTag.isData());
 }

 // Test for isEmpty() method
 @Test
 public void testIsEmpty() {
 Tag imgTag = img();
 assertFalse(imgTag.isEmpty());
 }

 // Test for isSelfClosing() method
 @Test
 public void testIsSelfClosing() {
 Tag imgTag = img().setSelfClosing();
 assertTrue(imgTag.isSelfClosing());
 }

 // Test for isKnownTag() method
 @Test
 public void testIsKnownTag() {
 String tagName = "p";
 assertTrue(Tag.isKnownTag(tagName));
 }

 // Test for preserveWhitespace() method
 @Test
 public void testPreserveWhitespace() {
 Tag preTag = pre();
 assertTrue(preTag.preserveWhitespace());
 }

 // Test for isFormListed() method
 @Test
 public void testIsFormListed() {
 Tag inputTag = input();
 assertTrue(inputTag.isFormListed());
 }

 // Test for isFormSubmittable() method
 @Test
 public void testIsFormSubmittable() {
 Tag textareaTag = textarea();
 assertTrue(textareaTag.isFormSubmittable());
 }

 // Edge case tests

 @Test
 public void testInvalidTagName() {
 Tag tag = Tag.valueOf("$%^&*()");
 assertNotNull(tag); // should not throw NPE
 assertEquals("$%^&*()", tag.tagName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = Tag.valueOf("");
 fail(); // should throw IllegalArgumentException
 }

 // Test for registering duplicate tag names
 @Test(expected = IllegalArgumentException.class)
 public void testTagNameDuplicate() {
 Tag tag = new Tag("html");
 Tag existingTag = html();
 tags.put(tag.tagName, tag);
 tags.put(tag.tagName, existingTag); // should throw exception
 }
}