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
 assertTrue(unknownTag.isData()); // since it's not registered, it's considered a data-only tag
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag spanTag = Tag.valueOf("span");

 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 assertFalse(spanTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag spanTag = Tag.valueOf("span");

 assertTrue(pTag.formatAsBlock());
 assertFalse(spanTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag divTag = Tag.valueOf("div");
 Tag pTag = Tag.valueOf("p");
 Tag spanTag = Tag.valueOf("span");

 assertTrue(divTag.canContainBlock());
 assertTrue(divTag.canContainInline());
 assertTrue(pTag.canContainBlock());
 assertTrue(pTag.canContainInline());
 assertFalse(spanTag.canContainBlock());
 assertFalse(spanTag.canContainInline());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isEmpty());
 assertFalse(pTag.isEmpty());
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");

 assertFalse(imgTag.isSelfClosing());
 assertFalse(pTag.isSelfClosing());
 assertTrue(brTag.isSelfClosing());
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("script");
 Tag pTag = Tag.valueOf("p");
 Tag metaTag = Tag.valueOf("meta");

 assertFalse(scriptTag.isEmpty());
 assertFalse(pTag.isEmpty());
 assertTrue(metaTag.isData());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = Tag.valueOf("unknown");

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknown"));
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
 public void testTagValueOfTooLong() {
 Tag tag = Tag.valueOf("thisTagNameIsTooLongToBeValid");
 }
}