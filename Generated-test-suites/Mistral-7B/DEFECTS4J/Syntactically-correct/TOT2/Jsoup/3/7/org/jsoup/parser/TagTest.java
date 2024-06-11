import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("p");
 Assert.assertEquals("p", tag.getName());
 }

 @Test
 public void testTagCanContain() {
 Tag parent = new Tag("div");
 Tag child1 = new Tag("p");
 Tag child2 = new Tag("div");

 parent.canContain(child1); // should be true
 parent.canContain(child2); // should be true

 child1.canContain(child1); // should be false (cannot contain self)
 child1.canContain(parent); // should be false (cannot contain parent)
 child2.canContain(child1); // should be false (cannot contain child that is not a descendant)
 child2.canContain(child2); // should be true
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = new Tag("p");
 Tag divTag = new Tag("div");

 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = new Tag("script");
 Tag metaTag = new Tag("meta");

 assertFalse(scriptTag.isData());
 assertFalse(metaTag.isData());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfNull() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfEmpty() {
 Tag tag = Tag.valueOf("");
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");

 assertTrue(tag1.equals(tag1));
 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");

 int hashCode1 = tag1.hashCode();
 int hashCode2 = tag2.hashCode();
 int hashCode3 = tag3.hashCode();

 assertEquals(hashCode1, hashCode2); // should be equal since they are the same object
 assertNotSame(hashCode1, hashCode3); // should be different since they are different objects
 }

 @Test
 public void testTagToString() {
 Tag tag = new Tag("p");
 assertEquals("p", tag.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagCanContainNull() {
 Tag tag = new Tag("p");
 tag.canContain(null);
 }
}