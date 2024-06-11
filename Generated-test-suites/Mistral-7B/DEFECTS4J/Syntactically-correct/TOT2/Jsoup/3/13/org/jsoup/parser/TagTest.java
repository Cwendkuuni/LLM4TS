import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testValueOfEmpty() {
 Tag tag = Tag.valueOf("");
 assertEquals("", tag.getName());
 }

 @Test
 public void testValueOfCapitalized() {
 Tag tag = Tag.valueOf("P");
 assertEquals("p", tag.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfNull() {
 Tag tag = Tag.valueOf(null);
 }

 @Test
 public void testCanContain() {
 Tag parent = new Tag("div");
 Tag child1 = new Tag("p");
 Tag child2 = new Tag("div");

 assertTrue(parent.canContain(child1));
 assertTrue(parent.canContain(child2));
 }

 @Test
 public void testCannotContainSelf() {
 Tag parent = new Tag("p");
 Tag child = new Tag("p");

 assertFalse(parent.canContain(child));
 }

 @Test
 public void testCannotContainEmptyTag() {
 Tag parent = new Tag("p");
 Tag child = new Tag("img");

 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCannotContainBlockWithinInline() {
 Tag parent = new Tag("span");
 Tag child = new Tag("div");

 assertFalse(parent.canContain(child));
 }

 @Test
 public void testCannotContainInlineWithinBlock() {
 Tag parent = new Tag("div");
 Tag child = new Tag("span");

 assertTrue(parent.canContain(child));
 }

 @Test
 public void testCannotContainSameTag() {
 Tag parent = new Tag("p");
 Tag child = new Tag("p");

 assertFalse(parent.canContain(child));
 }

 @Test
 public void testIsBlock() {
 Tag tag = new Tag("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsInline() {
 Tag tag = new Tag("span");
 assertFalse(tag.isBlock());
 assertTrue(tag.isInline());
 }

 @Test
 public void testIsData() {
 Tag tag = new Tag("script");
 assertTrue(tag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag tag = new Tag("img");
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tag = new Tag("pre");
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag tag = new Tag("p").setAncestor("div");
 assertEquals("div", tag.getImplicitParent().getName());
 }

 @Test
 public void testIsValidParent() {
 Tag child = new Tag("div");
 Tag parent1 = new Tag("body");
 Tag parent2 = new Tag("p");

 assertTrue(parent1.isValidParent(child));
 assertFalse(parent2.isValidParent(child));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAncestors() {
 Tag tag = new Tag("p").setAncestors(null);
 }

 @Test
 public void testEmptyAncestors() {
 Tag tag = new Tag("p").setAncestors(new Tag[0]);
 assertEquals(new Tag[0], tag.ancestors.toArray());
 }

 @Test
 public void testSingleAncestor() {
 Tag tag = new Tag("p").setAncestor("div");
 assertEquals("div", tag.ancestors.get(0).getName());
 }

 @Test
 public void testMultipleAncestors() {
 Tag tag = new Tag("p").setAncestor("div").setAncestor("body");
 assertEquals("body", tag.ancestors.get(1).getName());
 assertEquals("div", tag.ancestors.get(0).getName());
 }

 @Test
 public void testToString() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.toString());
 }
}