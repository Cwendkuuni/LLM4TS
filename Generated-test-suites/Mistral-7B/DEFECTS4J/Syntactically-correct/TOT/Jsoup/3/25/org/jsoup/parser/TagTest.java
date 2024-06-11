import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag unknownTag = Tag.valueOf("unknown");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());
 assertNotSame(pTag, divTag);
 assertTrue(pTag.equals(Tag.valueOf("p")));
 assertFalse(pTag.equals(divTag));
 assertFalse(pTag.equals(unknownTag));
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag pTag2 = Tag.valueOf("p");
 Tag headTag = Tag.valueOf("head");
 Tag bodyTag = Tag.valueOf("body");

 assertTrue(pTag.canContain(pTag2));
 assertTrue(divTag.canContain(pTag));
 assertFalse(headTag.canContain(pTag));
 assertFalse(bodyTag.canContain(headTag));
 }

 @Test
 public void testIsData() {
 Tag pTag = Tag.valueOf("p");
 Tag scriptTag = Tag.valueOf("script");
 Tag metaTag = Tag.valueOf("meta");

 assertFalse(pTag.isData());
 assertTrue(scriptTag.isData());
 assertTrue(metaTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag pTag = Tag.valueOf("p");

 assertFalse(imgTag.isEmpty());
 assertFalse(pTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 Tag textareaTag = Tag.valueOf("textarea");

 assertFalse(preTag.preserveWhitespace());
 assertTrue(textareaTag.preserveWhitespace());
 }

 @Test
 public void testEqualsAndHashCode() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertEquals(pTag1, pTag2);
 assertNotSame(pTag1, divTag);
 assertEquals(pTag1.hashCode(), pTag2.hashCode());
 }

 @Test
 public void testToString() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertEquals("p", pTag.toString());
 assertEquals("div", divTag.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNameInConstructor() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyNameInConstructor() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidNameInConstructor() {
 Tag tag = new Tag("<script>");
 }

}