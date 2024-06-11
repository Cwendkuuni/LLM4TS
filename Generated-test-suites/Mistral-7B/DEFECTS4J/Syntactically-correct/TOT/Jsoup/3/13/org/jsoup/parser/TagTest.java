import org.junit.Test;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag unknownTag = Tag.valueOf("unknown");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());
 assertFalse(unknownTag.isBlock()); // since it's not registered
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");
 Tag pInsideDiv = Tag.valueOf("p");
 Tag divInsideP = Tag.valueOf("div");

 assertTrue(pTag.canContain(pInsideP));
 assertTrue(divTag.canContain(pTag));
 assertTrue(divTag.canContain(pInsideDiv));
 assertTrue(divTag.canContain(imgTag));
 assertFalse(pTag.canContain(imgTag));
 }

 @Test
 public void testIsValidParent() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(pTag.isValidParent(imgTag)); // img can be a child of p
 assertFalse(pTag.isValidParent(divTag)); // p can't be a parent of div
 }

 @Test
 public void testIsData() {
 Tag scriptTag = Tag.valueOf("script");
 Tag metaTag = Tag.valueOf("meta");
 Tag textareaTag = Tag.valueOf("textarea");

 assertTrue(scriptTag.isData());
 assertTrue(metaTag.isData());
 assertTrue(textareaTag.isData());
 }

 @Test
 public void testEmptyTag() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");

 assertTrue(imgTag.isEmpty());
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue(preTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 }
}