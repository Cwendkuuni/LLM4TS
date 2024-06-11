import org.junit.Test;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagUnknown = Tag.valueOf("unknown");

 assertEquals("p", tagP.getName());
 assertEquals("div", tagDiv.getName());
 assertNotEquals("p", tagUnknown.getName());
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag pTag2 = Tag.valueOf("p");
 Tag divTag2 = Tag.valueOf("div");

 assertTrue(pTag.canContain(pTag2));
 assertTrue(divTag.canContain(pTag));
 assertFalse(pTag.canContain(divTag));
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag spanTag = Tag.valueOf("span");

 assertTrue(pTag.isBlock());
 assertFalse(spanTag.isBlock());
 }

 @Test
 public void testIsData() {
 Tag scriptTag = Tag.valueOf("script");
 Tag pTag = Tag.valueOf("p");

 assertFalse(pTag.isData());
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");

 assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue(preTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfNullTagName() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfEmptyTagName() {
 Tag tag = Tag.valueOf("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfInvalidTagName() {
 Tag tag = Tag.valueOf("InvalidTagName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainNullTag() {
 Tag pTag = Tag.valueOf("p");

 try {
 pTag.canContain(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainInvalidTag() {
 Tag pTag = Tag.valueOf("p");
 Tag invalidTag = Tag.valueOf("InvalidTag");

 try {
 pTag.canContain(invalidTag);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}