import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 tag = Tag.valueOf("DIV");
 assertEquals("div", tag.getName());
 }

 @Test
 public void testTagCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("DIV");
 Tag bodyTag = Tag.valueOf("BODY");
 Tag headTag = Tag.valueOf("HEAD");

 assertTrue(pTag.canContain(new Tag("br")));
 assertTrue(pTag.canContain(new Tag("span")));
 assertTrue(divTag.canContain(new Tag("p")));
 assertTrue(bodyTag.canContain(new Tag("p")));
 assertTrue(headTag.canContain(new Tag("title")));
 assertTrue(headTag.canContain(new Tag("script")));

 assertFalse(pTag.canContain(new Tag("div")));
 assertFalse(pTag.canContain(new Tag("p").getValue()));
 assertFalse(bodyTag.canContain(new Tag("h1")));
 assertFalse(headTag.canContain(new Tag("h1")));
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag spanTag = Tag.valueOf("span");

 assertTrue(pTag.isBlock());
 assertTrue(spanTag.isInline());
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("script");
 Tag linkTag = Tag.valueOf("link");

 assertTrue(scriptTag.isData());
 assertTrue(linkTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");

 assertTrue(imgTag.isEmpty());
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
 public void testTagValueOfEmptyString() {
 Tag tag = Tag.valueOf("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfInvalidTagName() {
 Tag tag = Tag.valueOf("INVALID_TAG");
 }

 @Test
 public void testTagEquals() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertEquals(pTag1, pTag2);
 assertNotEquals(pTag1, divTag);
 }

 @Test
 public void testTagHashCode() {
 Tag pTag = Tag.valueOf("p");
 Tag qTag = Tag.valueOf("q");

 int hashCodeP = pTag.hashCode();
 int hashCodeQ = qTag.hashCode();

 assertNotEquals(hashCodeP, hashCodeQ);
 }

 @Test
 public void testTagToString() {
 Tag pTag = Tag.valueOf("p");

 assertEquals("p", pTag.toString());
 }
}