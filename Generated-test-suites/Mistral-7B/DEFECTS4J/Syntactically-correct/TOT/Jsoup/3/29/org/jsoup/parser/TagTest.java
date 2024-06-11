import org.junit.Test;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("P");
 Tag tag3 = Tag.valueOf("DIV");

 assertEquals("p", tag1.getName());
 assertEquals("p", tag1.tagName);
 assertEquals("DIV", tag3.getName());
 assertEquals("div", tag3.tagName);

 // Case insensitive
 assertEquals(tag1, Tag.valueOf("p"));
 assertEquals(tag1, Tag.valueOf("P"));
 // Edge case: empty string
 assertEquals(Tag.getDefaultTag(), Tag.valueOf(""));
 // Error case: null
 try {
 Tag tag4 = Tag.valueOf(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag h1Tag = Tag.valueOf("h1");
 Tag imgTag = Tag.valueOf("img");

 // Test block-level and inline-level containment
 assertTrue(pTag.canContain(divTag));
 assertTrue(divTag.canContain(pTag));
 assertTrue(pTag.canContain(pTag));
 assertTrue(pTag.canContain(h1Tag));
 // Test empty and data-only tags
 assertFalse(imgTag.canContain(pTag));
 assertFalse(pTag.canContain(imgTag));
 // Test inheritance and ancestry
 assertTrue(h1Tag.canContain(pTag));
 // Test invalid cases
 assertFalse(pTag.canContain(pTag)); // cannot contain self
 assertFalse(pTag.canContain(h1Tag)); // cannot contain ancestor
 }

 @Test
 public void testIsData() {
 Tag pTag = Tag.valueOf("p");
 Tag scriptTag = Tag.valueOf("script");

 assertTrue(scriptTag.isData());
 assertFalse(pTag.isData());
 }

 @Test
 public void testGetImplicitParent() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag bodyTag = Tag.valueOf("body");

 // Test empty list
 assertEquals(null, pTag.getImplicitParent());
 // Test direct child
 assertEquals(bodyTag, divTag.getImplicitParent());
 // Test grandparent
 assertEquals(bodyTag, pTag.getImplicitParent());
 }

 @Test
 public void testIsValidParent() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag bodyTag = Tag.valueOf("body");
 Tag headTag = Tag.valueOf("head");

 // Test empty list
 assertTrue(pTag.isValidParent(Tag.getDefaultTag()));
 // Test direct child
 assertTrue(pTag.isValidParent(divTag));
 // Test ancestor
 assertTrue(bodyTag.isValidParent(pTag));
 // Test head
 assertTrue(headTag.isValidParent(scriptTag));
 // Test invalid cases
 assertFalse(divTag.isValidParent(pTag));
 assertFalse(headTag.isValidParent(bodyTag));
 }
}