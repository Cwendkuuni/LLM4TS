import org.jsoup.parser.Tag;
import org.junit.Test;
import org.junit.Assert.*;

public class TagTest {

 // Test case for getName() method
 @Test
 public void testGetName() {
 Tag tag = new Tag("div");
 String expectedName = "div";
 String actualName = tag.getName();
 assertEquals(expectedName, actualName);
 }

 // Test case for valueOf() method with existing tag
 @Test
 public void testValueOfExistingTag() {
 String tagName = "p";
 Tag expectedTag = new Tag(tagName);
 Tag actualTag = Tag.valueOf(tagName);
 assertEquals(expectedTag, actualTag);
 }

 // Test case for valueOf() method with non-existing tag
 @Test
 public void testValueOfNonExistingTag() {
 String tagName = "ABCdef";
 Tag expectedTag = new Tag("ABCdef"); // as it's not registered, this is the default tag
 Tag actualTag = Tag.valueOf(tagName);
 assertTrue(expectedTag.equals(actualTag));
 }

 // Test case for canContain() method with valid child
 @Test
 public void testCanContainValidChild() {
 Tag parentTag = new Tag("div");
 Tag childTag = new Tag("p");
 assertTrue(parentTag.canContain(childTag));
 }

 // Test case for canContain() method with invalid child (block tag, but parent is not block)
 @Test(expected = AssertionError.class)
 public void testCanContainInvalidBlockChild() {
 Tag parentTag = new Tag("span");
 Tag childTag = new Tag("div");
 parentTag.canContain(childTag);
 }

 // Test case for canContain() method with invalid child (inline tag, but parent is not inline)
 @Test(expected = AssertionError.class)
 public void testCanContainInvalidInlineChild() {
 Tag parentTag = new Tag("div");
 Tag childTag = new Tag("input");
 parentTag.canContain(childTag);
 }

 // Test case for isBlock() method
 @Test
 public void testIsBlock() {
 Tag tag = new Tag("div");
 assertTrue(tag.isBlock());
 }

 // Test case for isInline() method
 @Test
 public void testIsInline() {
 Tag tag = new Tag("span");
 assertFalse(tag.isBlock());
 assertTrue(tag.isInline());
 }

 // Test case for isData() method
 @Test
 public void testIsData() {
 Tag tag = new Tag("script");
 assertTrue(tag.isData());
 }

 // Test case for isEmpty() method
 @Test
 public void testIsEmpty() {
 Tag tag = new Tag("img");
 assertTrue(tag.isEmpty());
 }

 // Test case for preserveWhitespace() method
 @Test
 public void testPreserveWhitespace() {
 Tag tag = new Tag("pre");
 assertTrue(tag.preserveWhitespace());
 }

 // Test case for equals() method
 @Test
 public void testEquals() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 assertTrue(tag1.equals(tag2));
 }

 // Test case for equals() method with different tag names
 @Test
 public void testEqualsDifferentTags() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("p");
 assertFalse(tag1.equals(tag2));
 }

 // Test case for hashCode() method
 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 int expectedHashCode1 = tag1.hashCode();
 int expectedHashCode2 = tag2.hashCode();
 int actualHashCode1 = tag1.hashCode();
 int actualHashCode2 = tag2.hashCode();
 assertEquals(expectedHashCode1, actualHashCode1);
 assertEquals(expectedHashCode2, actualHashCode2);
 }

 // Test case for toString() method
 @Test
 public void testToString() {
 Tag tag = new Tag("div");
 String expectedString = "div";
 String actualString = tag.toString();
 assertEquals(expectedString, actualString);
 }

}