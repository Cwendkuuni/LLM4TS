import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValues() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag unknownTag = Tag.valueOf("unknown");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());
 assertEquals(!Tag.isKnownTag("unknown"), unknownTag.isKnownTag());
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");
 assertTrue(pTag.isBlock());
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");
 assertTrue(pTag.formatAsBlock());
 assertFalse(brTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag imgTag = Tag.valueOf("img");
 Tag divTag = Tag.valueOf("div");
 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());
 assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");
 assertTrue(imgTag.isEmpty());
 assertTrue(brTag.isEmpty());
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
 Tag unknownTag = Tag.valueOf("unknown");
 assertTrue(Tag.isKnownTag("p"), pTag.isKnownTag());
 assertFalse(Tag.isKnownTag("unknown"), unknownTag.isKnownTag());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyMap() {
 Map<String, Tag> tags = new HashMap<>();
 Tag tag = Tag.valueOf("tag", tags);
 }

 @Test
 public void testGetTagByName() {
 Map<String, Tag> tags = new HashMap<>();
 Tag pTag = new Tag("p");
 Tag divTag = new Tag("div");
 tags.put("p", pTag);
 tags.put("div", divTag);

 Tag gotTag1 = Tag.valueOf("p", tags);
 Tag gotTag2 = Tag.valueOf("p", tags);
 Tag gotTag3 = Tag.valueOf("div", tags);

 assertEquals(pTag, gotTag1);
 assertEquals(pTag, gotTag2);
 assertEquals(divTag, gotTag3);
 }
}