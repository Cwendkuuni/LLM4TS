import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 // Test for Tag valueOf() method
 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("p");
 String name = tag.getName();
 assertEquals("p", name);
 }

 // Test for Tag canContain() method with valid child
 @Test
 public void testTagCanContainValidChild() {
 Tag parentTag = new Tag("div");
 Tag childTag = new Tag("p");
 assertTrue(parentTag.canContain(childTag));
 }

 // Test for Tag canContain() method with invalid child (block tag in inline tag)
 @Test(expected = IllegalStateException.class)
 public void testTagCannotContainInvalidChild() {
 Tag parentTag = new Tag("p");
 Tag childTag = new Tag("div");
 parentTag.canContain(childTag);
 }

 // Test for Tag isEmpty() method
 @Test
 public void testTagIsEmpty() {
 Tag imgTag = new Tag("img");
 imgTag.setEmpty();
 assertTrue(imgTag.isEmpty());
 }

 // Test for Tag preserveWhitespace() method
 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = new Tag("pre");
 preTag.setPreserveWhitespace();
 assertTrue(preTag.preserveWhitespace());
 }

 // Test for Tag equals() method
 @Test
 public void testTagEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertTrue(tag1.equals(tag2));
 }

 // Test for Tag isValidParent() method
 @Test
 public void testTagIsValidParent() {
 Tag parentTag = new Tag("div");
 Tag childTag = new Tag("p");
 assertTrue(parentTag.isValidParent(childTag));
 }

 // Test for Tag isValidParent() method with invalid parent
 @Test(expected = IllegalStateException.class)
 public void testTagInvalidParent() {
 Tag parentTag = new Tag("p");
 Tag childTag = new Tag("div");
 parentTag.isValidParent(childTag);
 }

 // Test for Tag toString() method
 @Test
 public void testTagToString() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.toString());
 }

 // Test for Tag getImplicitParent() method
 @Test
 public void testTagGetImplicitParent() {
 Tag tag = new Tag("p").setAncestor("div");
 Tag implicitParent = tag.getImplicitParent();
 assertEquals("div", implicitParent.getName());
 }

 // Test for Tag getImplicitParent() method with empty ancestor list
 @Test
 public void testTagGetImplicitParentEmptyAncestorList() {
 Tag tag = new Tag("p");
 Tag implicitParent = tag.getImplicitParent();
 assertNull(implicitParent);
 }

 // Test for Tag canContain() method with duplicate tags
 @Test(expected = IllegalStateException.class)
 public void testTagCannotContainDuplicateTags() {
 Tag parentTag = new Tag("p");
 Tag childTag = new Tag("p");
 parentTag.canContain(childTag);
 }

 // Test for Tag canContain() method with head tag
 @Test
 public void testTagCanContainHeadTag() {
 Tag headTag = new Tag("head");
 Tag bodyTag = new Tag("body");
 assertTrue(headTag.canContain(bodyTag));
 }

 // Test for Tag canContain() method with dt and dd tags
 @Test
 public void testTagCanContainDtAndDdTags() {
 Tag dtTag = new Tag("dt");
 Tag ddTag = new Tag("dd");
 assertTrue(dtTag.canContain(ddTag));
 assertTrue(ddTag.canContain(dtTag));
 }

 // Test for Tag canContain() method with frame tag
 @Test(expected = IllegalStateException.class)
 public void testTagCannotContainFrameTag() {
 Tag parentTag = new Tag("div");
 Tag childTag = new Tag("frame");
 parentTag.canContain(childTag);
 }

 // Test for Tag canContain() method with map tag
 @Test
 public void testTagCanContainMapTag() {
 Tag parentTag = new Tag("body");
 Tag childTag = new Tag("map");
 assertTrue(parentTag.canContain(childTag));
 }

 // Test for Tag equals() method with same object
 @Test
 public void testTagEqualsSameObject() {
 Tag tag = new Tag("p");
 assertTrue(tag.equals(tag));
 }

 // Test for Tag equals() method with different objects but same value
 @Test
 public void testTagEqualsDifferentObjectsSameValue() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertTrue(tag1.equals(tag2));
 }

 // Test for Tag equals() method with different objects and different values
 @Test
 public void testTagEqualsDifferentObjectsDifferentValues() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 assertFalse(tag1.equals(tag2));
 }

 // Test for Tag hashCode() method
 @Test
 public void testTagHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 // Test for Tag hashCode() method with different objects but same value
 @Test
 public void testTagHashCodeDifferentObjectsSameValue() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 // Test for Tag hashCode() method with different objects and different values
 @Test
 public void testTagHashCodeDifferentObjectsDifferentValues() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 assertNotEquals(tag1.hashCode(), tag2.hashCode());
 }

 // Test for Tag toString() method with empty tag
 @Test
 public void testTagToStringEmptyTag() {
 Tag tag = new Tag("img").setEmpty();
 assertEquals("img", tag.toString());
 }

 // Test for Tag toString() method with non-empty tag
 @Test
 public void testTagToStringNonEmptyTag() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.toString());
 }
}