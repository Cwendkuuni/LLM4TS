import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = new Tag("p");
 String expected = "p";
 String actual = tag.getName();
 assertEquals(expected, actual);
 }

 @Test
 public void testValueOfTagName() {
 Tag tag = Tag.valueOf("p");
 String expected = "p";
 String actual = tag.getName();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsBlockTrue() {
 Tag tag = new Tag("p");
 boolean expected = true;
 boolean actual = tag.isBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatAsBlockTrue() {
 Tag tag = new Tag("p");
 boolean expected = true;
 boolean actual = tag.formatAsBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testCanContainBlockTrue() {
 Tag tag = new Tag("p");
 boolean expected = true;
 boolean actual = tag.canContainBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testCanContainInlineTrue() {
 Tag tag = new Tag("p");
 boolean expected = true;
 boolean actual = tag.canContainInline();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsEmptyFalse() {
 Tag tag = new Tag("p");
 boolean expected = false;
 boolean actual = tag.isEmpty();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsSelfClosingFalse() {
 Tag tag = new Tag("p");
 boolean expected = false;
 boolean actual = tag.isSelfClosing();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsKnownTagTrue() {
 Tag tag = new Tag("p");
 boolean expected = true;
 boolean actual = tag.isKnownTag();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsKnownTagFalse() {
 Tag tag = new Tag("unknown");
 boolean expected = false;
 boolean actual = tag.isKnownTag();
 assertEquals(expected, actual);
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
 public void testEmptyMap() {
 Tag tag = Tag.valueOf("unknown");
 Tag tag2 = Tag.valueOf("unknown2");
 Map<String, Tag> tags = new HashMap<>();
 Tag.register(tag);
 Tag.register(tag2);
 Tag unknownTag = Tag.valueOf("unknown");
 Tag unknownTag2 = Tag.valueOf("unknown2");
 Tag tagFromEmptyMap = Tag.valueOf("unknown3");
 assertEquals(tag, unknownTag);
 assertEquals(tag2, unknownTag2);
 assertEquals(tagFromEmptyMap, null);
 }

 @Test
 public void testBlockTags() {
 Tag tag = Tag.valueOf("html");
 boolean expected = true;
 boolean actual = tag.isBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testInlineTags() {
 Tag tag = Tag.valueOf("a");
 boolean expected = false;
 boolean actual = tag.isBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testEmptyTags() {
 Tag tag = Tag.valueOf("img");
 boolean expected = true;
 boolean actual = tag.isEmpty();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatAsInlineTags() {
 Tag tag = Tag.valueOf("title");
 boolean expected = false;
 boolean actual = tag.formatAsBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testPreserveWhitespaceTags() {
 Tag tag = Tag.valueOf("pre");
 boolean expected = true;
 boolean actual = tag.preserveWhitespace();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetTagByName() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 Tag tag3 = Tag.valueOf("p");
 Tag tag4 = Tag.valueOf("div");
 assertEquals(tag3, Tag.valueOf("p"));
 assertEquals(tag4, Tag.valueOf("div"));
 assertEquals(tag1, tag3);
 assertEquals(tag2, tag4);
 }
}