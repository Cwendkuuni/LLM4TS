import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 // Test case for getName() method
 @Test
 public void testGetName() {
 Tag tag = new Tag("div");
 Assert.assertEquals("div", tag.getName());
 }

 // Test case for valueOf() method with known tag
 @Test
 public void testValueOfKnownTag() {
 Tag tag = Tag.valueOf("p");
 Assert.assertEquals("p", tag.getName());
 Assert.assertTrue(tag.isKnownTag());
 }

 // Test case for valueOf() method with unknown tag
 @Test
 public void testValueOfUnknownTag() {
 Tag tag = Tag.valueOf("unknown");
 Assert.assertNotEquals("unknown", tag.getName());
 Assert.assertFalse(tag.isKnownTag());
 }

 // Test case for isBlock() method with block tag
 @Test
 public void testIsBlockBlockTag() {
 Tag tag = new Tag("div");
 Assert.assertTrue(tag.isBlock());
 }

 // Test case for isBlock() method with inline tag
 @Test
 public void testIsBlockInlineTag() {
 Tag tag = new Tag("span");
 Assert.assertFalse(tag.isBlock());
 }

 // Test case for canContainBlock() method with block tag
 @Test
 public void testCanContainBlockBlockTag() {
 Tag parentTag = new Tag("div");
 Tag childTag = new Tag("p");
 Assert.assertTrue(parentTag.canContainBlock());
 parentTag.add(childTag);
 }

 // Test case for canContainBlock() method with inline tag
 @Test
 public void testCanContainBlockInlineTag() {
 Tag parentTag = new Tag("span");
 Tag childTag = new Tag("i");
 Assert.assertFalse(parentTag.canContainBlock());
 parentTag.add(childTag);
 }

 // Test case for isData() method with data only tag
 @Test
 public void testIsDataDataOnlyTag() {
 Tag tag = new Tag("script");
 Assert.assertTrue(tag.isData());
 }

 // Test case for isEmpty() method with empty tag
 @Test
 public void testIsEmptyEmptyTag() {
 Tag tag = new Tag("img");
 tag.empty = true;
 Assert.assertTrue(tag.isEmpty());
 }

 // Test case for isSelfClosing() method with self closing tag
 @Test
 public void testIsSelfClosingSelfClosingTag() {
 Tag tag = new Tag("img");
 tag.selfClosing = true;
 Assert.assertTrue(tag.isSelfClosing());
 }

 // Test case for isSelfClosing() method with non-self closing tag
 @Test
 public void testIsSelfClosingNonSelfClosingTag() {
 Tag tag = new Tag("p");
 Assert.assertFalse(tag.isSelfClosing());
 }

 // Test case for isKnownTag() method with known tag
 @Test
 public void testIsKnownTagKnownTag() {
 Tag tag = new Tag("p");
 Assert.assertTrue(Tag.isKnownTag(tag.getName()));
 }

 // Test case for isKnownTag() method with unknown tag
 @Test
 public void testIsKnownTagUnknownTag() {
 Tag tag = new Tag("unknown");
 Assert.assertFalse(Tag.isKnownTag(tag.getName()));
 }

 // Test case for preserveWhitespace() method with preserve whitespace tag
 @Test
 public void testPreserveWhitespacePreserveWhitespaceTag() {
 Tag tag = new Tag("pre");
 Assert.assertTrue(tag.preserveWhitespace());
 }

 // Test case for preserveWhitespace() method with non-preserve whitespace tag
 @Test
 public void testPreserveWhitespaceNonPreserveWhitespaceTag() {
 Tag tag = new Tag("div");
 Assert.assertFalse(tag.preserveWhitespace());
 }

 // Test case for equals() method with same tag
 @Test
 public void testEqualsSameTag() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Assert.assertEquals(tag1, tag2);
 }

 // Test case for equals() method with different tags
 @Test
 public void testEqualsDifferentTags() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 Assert.assertNotEquals(tag1, tag2);
 }

 // Test case for equals() method with null object
 @Test(expected = NullPointerException.class)
 public void testEqualsNullObject() {
 Tag tag = new Tag("p");
 Assert.assertNotEquals(tag, null);
 }

 // Test case for hashCode() method with same tag
 @Test
 public void testHashCodeSameTag() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 // Test case for hashCode() method with different tags
 @Test
 public void testHashCodeDifferentTags() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 Assert.assertNotEquals(tag1.hashCode(), tag2.hashCode());
 }
}