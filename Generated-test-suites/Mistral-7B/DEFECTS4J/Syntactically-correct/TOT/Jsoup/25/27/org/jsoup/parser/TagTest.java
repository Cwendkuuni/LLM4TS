import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

 @Test
 public void testTagCreation() {
 Tag tag = Tag.valueOf("p");
 Assert.assertEquals("p", tag.getName());
 Assert.assertTrue(tag.isInline());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testUnknownTagCreation() {
 Tag tag = Tag.valueOf("unknownTag");
 Assert.assertEquals("unknownTag", tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = Tag.valueOf("unknownTag");
 Assert.assertTrue(Tag.isKnownTag("p"));
 Assert.assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testIsKnownTagWithEmptyTagName() {
 Assert.assertFalse(Tag.isKnownTag(""));
 }

 @Test
 public void testIsKnownTagWithNullTagName() {
 Assert.assertFalse(Tag.isKnownTag(null));
 }

 @Test
 public void testTagEquality() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = new Tag("p"); // different instance
 Tag tag4 = tag1;
 Assert.assertEquals(tag1, tag2);
 Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
 Assert.assertNotEquals(tag1, tag3);
 Assert.assertNotEquals(tag1.hashCode(), tag3.hashCode());
 Assert.assertEquals(tag1, tag4);
 Assert.assertEquals(tag1.hashCode(), tag4.hashCode());
 }

 @Test
 public void testTagInequality() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("div");
 Assert.assertNotEquals(tag1, tag2);
 Assert.assertNotEquals(tag1.hashCode(), tag2.hashCode());
 }

 @Test
 public void testIsBlockTag() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Assert.assertTrue(pTag.isBlock());
 Assert.assertTrue(divTag.isBlock());
 Tag imgTag = Tag.valueOf("img");
 Assert.assertFalse(imgTag.isBlock());
 }

 @Test
 public void testIsFormatAsBlockTag() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");
 Assert.assertTrue(pTag.formatAsBlock());
 Assert.assertTrue(divTag.formatAsBlock());
 Assert.assertFalse(imgTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlockTag() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");
 Assert.assertTrue(pTag.canContainBlock());
 Assert.assertTrue(divTag.canContainBlock());
 Assert.assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testIsInlineTag() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");
 Assert.assertFalse(pTag.isInline());
 Assert.assertFalse(divTag.isInline());
 Assert.assertTrue(imgTag.isInline());
 }

 @Test
 public void testIsDataTag() {
 Tag pTag = Tag.valueOf("p");
 Tag scriptTag = Tag.valueOf("script");
 Assert.assertFalse(pTag.isData());
 Assert.assertTrue(scriptTag.isData());
 }

 @Test
 public void testIsEmptyTag() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");
 Assert.assertFalse(imgTag.isEmpty());
 Assert.assertTrue(brTag.isEmpty());
 }

 @Test
 public void testIsSelfClosingTag() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");
 Tag selfClosingTag = new Tag("selfclosingtag");
 selfClosingTag.setSelfClosing();
 Assert.assertFalse(imgTag.isSelfClosing());
 Assert.assertFalse(brTag.isSelfClosing());
 Assert.assertTrue(selfClosingTag.isSelfClosing());
 }

 @Test
 public void testIsKnownPredefinedTag() {
 Assert.assertTrue(Tag.isKnownTag("html"));
 Assert.assertTrue(Tag.isKnownTag("head"));
 Assert.assertTrue(Tag.isKnownTag("body"));
 }

 @Test
 public void testIsKnownCustomTag() {
 Tag customTag = new Tag("customtag");
 Assert.assertFalse(Tag.isKnownTag("customtag"));
 Assert.assertEquals(customTag, Tag.valueOf("customtag"));
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag preTag = Tag.valueOf("pre");
 Tag textareaTag = Tag.valueOf("textarea");
 Tag scriptTag = Tag.valueOf("script");
 Assert.assertTrue(preTag.preserveWhitespace());
 Assert.assertFalse(textareaTag.preserveWhitespace());
 Assert.assertFalse(scriptTag.preserveWhitespace());
 }
}