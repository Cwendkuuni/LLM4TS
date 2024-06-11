import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = new Tag("div");
 assertEquals("div", tag.getName());
 }

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testIsBlockTrue() {
 Tag tag = new Tag("div");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testIsBlockFalse() {
 Tag tag = new Tag("input");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testFormatAsBlockTrue() {
 Tag tag = new Tag("p");
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testFormatAsBlockFalse() {
 Tag tag = new Tag("img");
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlockTrue() {
 Tag tag = new Tag("div");
 assertTrue(tag.canContainBlock());
 }

 @Test
 public void testCanContainBlockFalse() {
 Tag tag = new Tag("input");
 assertFalse(tag.canContainBlock());
 }

 @Test
 public void testIsEmptyTrue() {
 Tag tag = new Tag("img");
 tag.empty = true;
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testIsEmptyFalse() {
 Tag tag = new Tag("div");
 assertFalse(tag.isEmpty());
 }

 @Test
 public void testIsSelfClosingTrue() {
 Tag tag = new Tag("img");
 tag.selfClosing = true;
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testIsSelfClosingFalse() {
 Tag tag = new Tag("div");
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testIsKnownTagTrue() {
 Tag tag = new Tag("div");
 assertTrue(Tag.isKnownTag("div"));
 }

 @Test
 public void testIsKnownTagFalse() {
 Tag tag = new Tag("unknown");
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testPreserveWhitespaceTrue() {
 Tag tag = new Tag("pre");
 tag.preserveWhitespace = true;
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testPreserveWhitespaceFalse() {
 Tag tag = new Tag("div");
 assertFalse(tag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 assertEquals("", tag.getName());
 }

 @Test
 public void testRegisteredTag() {
 Tag tag = Tag.valueOf("div");
 Tag tag2 = Tag.valueOf("div");
 assertEquals(tag, tag2);
 }

 @Test
 public void testUnknownTag() {
 Tag tag = Tag.valueOf("unknown");
 assertFalse(tag.isKnownTag());
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 assertEquals(tag1, tag2);
 }

 @Test
 public void testTagHashCode() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 @Test
 public void testTagToString() {
 Tag tag = new Tag("div");
 assertEquals("div", tag.toString());
 }
}