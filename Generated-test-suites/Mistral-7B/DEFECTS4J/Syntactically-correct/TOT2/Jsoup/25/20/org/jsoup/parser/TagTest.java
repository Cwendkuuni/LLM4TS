import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testgetValue() {
 Tag tag = Tag.valueOf("p");
 String expected = "p";
 String actual = tag.getName();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetUnknownValue() {
 Tag tag = Tag.valueOf("unknownTag");
 String expected = "unknownTag";
 String actual = tag.getName();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsBlockTrue() {
 Tag tag = new Tag("div");
 boolean expected = true;
 boolean actual = tag.isBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsBlockFalse() {
 Tag tag = new Tag("input");
 boolean expected = false;
 boolean actual = tag.isBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatAsBlockTrue() {
 Tag tag = new Tag("pre");
 boolean expected = true;
 boolean actual = tag.formatAsBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatAsBlockFalse() {
 Tag tag = new Tag("a");
 boolean expected = false;
 boolean actual = tag.formatAsBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testCanContainBlockTrue() {
 Tag tag = new Tag("div");
 boolean expected = true;
 boolean actual = tag.canContainBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testCanContainBlockFalse() {
 Tag tag = new Tag("input");
 boolean expected = false;
 boolean actual = tag.canContainBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsDataTrue() {
 Tag tag = new Tag("script");
 boolean expected = true;
 boolean actual = tag.isData();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsDataFalse() {
 Tag tag = new Tag("p");
 boolean expected = false;
 boolean actual = tag.isData();
 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testEmptyFalseAndCanContainBlockFalse() {
 Tag tag = new Tag("img");
 boolean expected = false;
 tag.empty = false;
 tag.canContainBlock = false;
 try {
 tag.isBlock();
 } catch (AssertionError e) {
 assertEquals("Expected: false, but: true", e.getMessage());
 }
 }

 @Test(expected = AssertionError.class)
 public void testEmptyFalseAndCanContainInlineFalse() {
 Tag tag = new Tag("img");
 boolean expected = false;
 tag.empty = false;
 tag.canContainInline = false;
 try {
 tag.isData();
 } catch (AssertionError e) {
 assertEquals("Expected: false, but: true", e.getMessage());
 }
 }

 @Test
 public void testIsKnownTagTrue() {
 Tag tag = Tag.valueOf("div");
 boolean expected = true;
 boolean actual = tag.isKnownTag();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsKnownTagFalse() {
 Tag tag = new Tag("unknownTag");
 boolean expected = false;
 boolean actual = tag.isKnownTag();
 assertEquals(expected, actual);
 }

 @Test
 public void testPreserveWhitespaceTrue() {
 Tag tag = new Tag("pre");
 boolean expected = true;
 boolean actual = tag.preserveWhitespace();
 assertEquals(expected, actual);
 }

 @Test
 public void testPreserveWhitespaceFalse() {
 Tag tag = new Tag("p");
 boolean expected = false;
 boolean actual = tag.preserveWhitespace();
 assertEquals(expected, actual);
 }

 @Test
 public void testEqualsTrue() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 boolean expected = true;
 boolean actual = tag1.equals(tag2);
 assertEquals(expected, actual);
 }

 @Test
 public void testEqualsFalse() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("div");
 boolean expected = false;
 boolean actual = tag1.equals(tag2);
 assertEquals(expected, actual);
 }
}