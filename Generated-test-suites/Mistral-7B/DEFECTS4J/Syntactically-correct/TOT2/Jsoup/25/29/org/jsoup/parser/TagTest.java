import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testNewTag() {
 Tag tag = Tag.valueOf("div");
 assertEquals("div", tag.getName());
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testNewEmptyTag() {
 Tag tag = Tag.valueOf("img");
 assertEquals("img", tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertFalse(tag.canContainBlock());
 Assert.assertTrue(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testNewInlineTag() {
 Tag tag = Tag.valueOf("span");
 assertEquals("span", tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertFalse(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testNewBlockTag() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testNewSelfClosingTag() {
 Tag tag = Tag.valueOf("input");
 assertEquals("input", tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertFalse(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertTrue(tag.isSelfClosing());
 Assert.assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testNewDataOnlyTag() {
 Tag tag = Tag.valueOf("script");
 assertEquals("script", tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertFalse(tag.formatAsBlock());
 Assert.assertFalse(tag.canContainBlock());
 Assert.assertFalse(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertTrue(tag.isData());
 }

 @Test
 public void testNewPreformattedTag() {
 Tag tag = Tag.valueOf("pre");
 assertEquals("pre", tag.getName());
 Assert.assertTrue(tag.isBlock());
 Assert.assertTrue(tag.formatAsBlock());
 Assert.assertTrue(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 Assert.assertFalse(tag.isEmpty());
 Assert.assertFalse(tag.isSelfClosing());
 Assert.assertTrue(tag.preserveWhitespace());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = Tag.valueOf("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithDigit() {
 Tag tag = Tag.valueOf("tag1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithSpecialChar() {
 Tag tag = Tag.valueOf("tag<tag");
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("div"));
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknown"));
 }
}