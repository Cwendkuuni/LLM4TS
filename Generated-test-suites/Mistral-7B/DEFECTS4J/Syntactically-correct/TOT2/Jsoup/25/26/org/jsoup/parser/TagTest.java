import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = new Tag("div");
 String name = tag.getName();
 assertEquals("div", name);
 }

 @Test
 public void testTagValueOf() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("div");
 assertEquals(tag1, Tag.valueOf("p")); // same instance
 assertNotSame(tag1, tag2); // different instances
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());
 }

 @Test
 public void testIsInline() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertFalse(pTag.isInline());
 assertFalse(divTag.isInline());
 }

 @Test
 public void testIsData() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertFalse(pTag.isData());
 assertFalse(divTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag pTag = Tag.valueOf("p");
 assertTrue(imgTag.isEmpty());
 assertFalse(pTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag pTag = Tag.valueOf("p");
 assertTrue(imgTag.isSelfClosing());
 assertFalse(pTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = new Tag("unknown");
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 Tag pTag = Tag.valueOf("p");
 assertTrue(preTag.preserveWhitespace());
 assertFalse(pTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag tag = new Tag("&");
 }

 @Test
 public void testCaseInsensitiveTagName() {
 Tag pTag = Tag.valueOf("p");
 Tag PTag = Tag.valueOf("P");
 assertEquals(pTag, PTag);
 }

 @Test
 public void testUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknown");
 Tag pTag = Tag.valueOf("p");
 assertNotSame(unknownTag, pTag);
 }
}