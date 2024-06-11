import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testTagCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(pTag.canContain(divTag));
 assertTrue(pTag.canContain(imgTag));
 assertFalse(pTag.canContain(pTag));
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(pTag.isBlock());
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testTagIsData() {
 Tag pTag = Tag.valueOf("p");
 Tag scriptTag = Tag.valueOf("script");

 assertFalse(pTag.isData());
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag pTag = Tag.valueOf("p");

 assertFalse(imgTag.isEmpty());
 assertFalse(pTag.isEmpty());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue(preTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfNull() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfEmpty() {
 Tag tag = Tag.valueOf("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfInvalidTag() {
 Tag tag = Tag.valueOf("invalidTag");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagCanContainNull() {
 Tag tag = new Tag("tag");
 try {
 tag.canContain(null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagCanContainSelf() {
 Tag tag = new Tag("tag");
 try {
 tag.canContain(tag);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testTagEquals() {
 Tag p1 = Tag.valueOf("p");
 Tag p2 = Tag.valueOf("p");
 Tag div = Tag.valueOf("div");

 assertEquals(p1, p2);
 assertNotEquals(p1, div);
 }

 @Test
 public void testTagHashCode() {
 Tag p1 = Tag.valueOf("p");
 Tag p2 = Tag.valueOf("p");
 Tag div = Tag.valueOf("div");

 int pHash = p1.hashCode();
 int p2Hash = p2.hashCode();
 int pSameHash = p1.hashCode();
 int divHash = div.hashCode();

 assertEquals(pHash, p2Hash);
 assertNotEquals(pHash, divHash);
 }

 @Test
 public void testTagToString() {
 Tag pTag = Tag.valueOf("p");
 assertEquals("p", pTag.toString());
 }
}