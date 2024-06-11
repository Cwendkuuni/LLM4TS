import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());

 // Test known tags
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("div"));

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.isKnownTag());
 assertTrue(unknownTag.isInline()); // Default behavior for unknown tags
 }

 @Test
 public void testBlockTags() {
 Tag htmlTag = Tag.valueOf("html");
 Tag pTag = Tag.valueOf("p");

 assertTrue(htmlTag.isKnownTag());
 assertTrue(pTag.isKnownTag());

 // Test block tags
 for (String tagName : Tag.blockTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 }
 }

 @Test
 public void testInlineTags() {
 Tag imgTag = Tag.valueOf("img");
 Tag aTag = Tag.valueOf("a");

 assertTrue(imgTag.isKnownTag());
 assertTrue(aTag.isKnownTag());

 // Test inline tags
 for (String tagName : Tag.inlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 }
 }

 @Test
 public void testEmptyTags() {
 Tag metaTag = Tag.valueOf("meta");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(metaTag.isKnownTag());
 assertTrue(imgTag.isKnownTag());

 // Test empty tags
 for (String tagName : Tag.emptyTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isEmpty());
 }
 }

 @Test
 public void testFormatAsBlockTags() {
 Tag preTag = Tag.valueOf("pre");
 Tag pTag = Tag.valueOf("p");

 assertTrue(preTag.isKnownTag());
 assertTrue(pTag.isKnownTag());

 // Test format as block tags
 for (String tagName : Tag.formatAsBlockTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.formatAsBlock());
 }
 }

 @Test
 public void testPreserveWhitespaceTags() {
 Tag preTag = Tag.valueOf("pre");
 Tag titleTag = Tag.valueOf("title");

 assertTrue(preTag.isKnownTag());
 assertTrue(titleTag.isKnownTag());

 // Test preserve whitespace tags
 for (String tagName : Tag.preserveWhitespaceTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.preserveWhitespace());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag invalidTag = Tag.valueOf("123");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag emptyTag = Tag.valueOf("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag nullTag = Tag.valueOf(null);
 }
}