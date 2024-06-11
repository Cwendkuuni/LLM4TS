import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = new Tag("div");
 String tagName = tag.getName();
 assertEquals("div", tagName);
 }

 @Test
 public void testValueOf() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertEquals(tagName, tag.getName());
 }

 @Test
 public void testCanContainBlock() {
 Tag parentTag = new Tag("div");
 Tag childTag1 = new Tag("p");
 Tag childTag2 = new Tag("div");

 assertTrue(parentTag.canContain(childTag1));
 assertTrue(parentTag.canContain(childTag2));
 }

 @Test
 public void testCanContainInline() {
 Tag parentTag = new Tag("div");
 Tag childTag1 = new Tag("span");
 Tag childTag2 = new Tag("img");

 assertTrue(parentTag.canContain(childTag1));
 assertTrue(parentTag.canContain(childTag2));
 }

 @Test
 public void testCannotContainBlockChild() {
 Tag parentTag = new Tag("div");
 Tag childTag = new Tag("div");

 assertFalse(parentTag.canContain(childTag));
 }

 @Test
 public void testCannotContainInlineParent() {
 Tag parentTag = new Tag("div");
 Tag childTag = new Tag("div");

 assertFalse(parentTag.canContain(childTag));
 }

 @Test
 public void testEmptyTag() {
 Tag tag = new Tag("img");
 tag.isEmpty();
 assertTrue(tag.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyName() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAncestor() {
 Tag tag = new Tag("div");
 tag.setAncestor(null);
 }

 @Test
 public void testContainDataOnly() {
 Tag tag = new Tag("script");
 tag.setContainDataOnly();
 assertFalse(tag.canContainInline);
 assertFalse(tag.canContainBlock);
 assertTrue(tag.preserveWhitespace);
 }

 @Test
 public void testContainInlineOnly() {
 Tag tag = new Tag("p");
 tag.setContainInlineOnly();
 assertFalse(tag.canContainBlock);
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testOptionalClosing() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 tag1.setOptionalClosing();
 tag2.setOptionalClosing();
 tag1.canContain(tag2);
 assertFalse(tag1.canContain(tag2));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tag = new Tag("pre");
 tag.setPreserveWhitespace();
 tag.preserveWhitespace();
 assertTrue(tag.preserveWhitespace);
 }

 @Test
 public void testGetImplicitParent() {
 Tag tag = new Tag("p");
 tag.ancestors.add(new Tag("div"));
 Tag implicitParent = tag.getImplicitParent();
 assertEquals("div", implicitParent.getName());
 }

 @Test
 public void testIsValidParent() {
 Tag parentTag = new Tag("div");
 Tag childTag = new Tag("div");
 assertTrue(parentTag.isValidParent(childTag));
 }

 @Test
 public void testIsValidParentWithAncestor() {
 Tag parentTag = new Tag("p");
 Tag childTag = new Tag("div");
 assertFalse(parentTag.isValidParent(childTag));
 }

 @Test
 public void testIsValidParentWithSameChild() {
 Tag parentTag = new Tag("div");
 Tag childTag = new Tag("div");
 assertFalse(parentTag.isValidParent(childTag));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");
 assertTrue(tag1.equals(tag1));
 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualsWithEmptyList() {
 Tag tag = new Tag("div");
 tag.ancestors = Collections.emptyList();
 tag.equals(new Tag("div"));
 }

 @Test
 public void testToString() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.toString());
 }
}