import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag unknownTag = Tag.valueOf("unknown");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());
 assertFalse(unknownTag.getName().equals("p"));
 }

 @Test
 public void testTagCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag h1Tag = Tag.valueOf("h1");

 assertTrue(pTag.canContain(divTag));
 assertTrue(divTag.canContain(pTag));
 assertFalse(pTag.canContain(h1Tag));
 assertFalse(h1Tag.canContain(pTag));
 }

 @Test
 public void testTagIsBlockAndCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.isBlock());
 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.isBlock());
 assertTrue(divTag.canContainBlock());
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("script");

 assertFalse(scriptTag.isData());

 Tag textAreaTag = new Tag("textarea");
 textAreaTag.setEmpty();
 textAreaTag.setContainDataOnly();

 assertTrue(textAreaTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isEmpty());
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testTagEquals() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag1.equals(pTag2));
 assertFalse(pTag1.equals(divTag));
 }

 @Test
 public void testTagHasAncestor() {
 Tag bodyTag = Tag.valueOf("body");
 Tag pTag = Tag.valueOf("p");
 Tag h1Tag = Tag.valueOf("h1");

 assertTrue(pTag.isValidParent(bodyTag));
 assertFalse(pTag.isValidParent(h1Tag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameIsNull() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameEmpty() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameEmptyTrimmed() {
 Tag tag = new Tag(" ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagNameWithWhitespace() {
 Tag tag = new Tag(" tagName ");
 }
}