import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

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
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagP2 = Tag.valueOf("p");
 Tag tagDiv2 = Tag.valueOf("div");

 assertTrue(tagP.canContain(tagDiv)); // p can contain div
 assertTrue(tagDiv.canContain(tagP)); // div can contain p
 assertFalse(tagP.canContain(tagP)); // p cannot contain itself
 assertFalse(tagDiv.canContain(tagDiv)); // div cannot contain itself
 }

 @Test
 public void testIsBlockAndCanContainBlock() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagSpan = Tag.valueOf("span");

 assertTrue(tagP.isBlock());
 assertTrue(tagDiv.isBlock());
 assertTrue(tagDiv.canContainBlock());
 assertFalse(tagSpan.isBlock());
 assertFalse(tagSpan.canContainBlock());
 }

 @Test
 public void testIsData() {
 Tag tagScript = Tag.valueOf("script");
 Tag tagMeta = Tag.valueOf("meta");

 assertFalse(tagScript.isData());
 assertFalse(tagMeta.isData());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainNullTag() {
 Tag tagP = Tag.valueOf("p");
 Tag nullTag = null;

 assertFalse(tagP.canContain(nullTag));
 }

 @Test
 public void testEquals() {
 Tag tagP1 = Tag.valueOf("p");
 Tag tagP2 = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");

 assertEquals(tagP1, tagP2); // equal p tags
 assertNotEquals(tagP1, tagDiv); // different tag names
 }

 @Test
 public void testHashCode() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagDiv2 = Tag.valueOf("div");

 int hashcodeTagP = tagP.hashCode();
 int hashcodeTagDiv = tagDiv.hashCode();
 int hashcodeTagDiv2 = tagDiv2.hashCode();

 assertEquals(hashcodeTagP, hashcodeTagP); // same tag, same hashcode
 assertEquals(hashcodeTagDiv, hashcodeTagDiv); // same tag, same hashcode
 assertNotEquals(hashcodeTagP, hashcodeTagDiv); // different tags, different hashcodes
 assertNotEquals(hashcodeTagP, hashcodeTagDiv2); // different tags, different hashcodes
 }

 @Test
 public void testToString() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");

 assertEquals("p", tagP.toString());
 assertEquals("div", tagDiv.toString());
 }

 @Test
 public void testCanContainSelf() {
 Tag tagP = Tag.valueOf("p");

 assertFalse(tagP.canContain(tagP)); // p cannot contain itself
 }

 @Test
 public void testCanContainHeadTags() {
 Tag tagHead = Tag.valueOf("head");
 Tag tagScript = Tag.valueOf("script");
 Tag tagLink = Tag.valueOf("link");

 assertFalse(tagHead.canContain(tagScript)); // head cannot contain script
 assertFalse(tagHead.canContain(tagLink)); // head cannot contain link
 }

 @Test
 public void testCanContainDTDD() {
 Tag tagDL = Tag.valueOf("dl");
 Tag tagDT = Tag.valueOf("dt");
 Tag tagDD = Tag.valueOf("dd");

 assertTrue(tagDL.canContain(tagDT)); // dl can contain dt
 assertTrue(tagDL.canContain(tagDD)); // dl can contain dd
 assertFalse(tagDT.canContain(tagDD)); // dt cannot contain dd
 assertFalse(tagDD.canContain(tagDT)); // dd cannot contain dt
 }

 @Test
 public void testCanContainLI() {
 Tag tagOL = Tag.valueOf("ol");
 Tag tagLI = Tag.valueOf("li");

 assertTrue(tagOL.canContain(tagLI)); // ol can contain li
 }

 @Test
 public void testCanContainFrameAndNoFrames() {
 Tag tagFrameSet = Tag.valueOf("frameset");
 Tag tagFrame = Tag.valueOf("frame");
 Tag tagNoFrames = Tag.valueOf("noframes");

 assertTrue(tagFrameSet.canContain(tagFrame)); // frameset can contain frame
 assertTrue(tagFrameSet.canContain(tagNoFrames)); // frameset can contain noframes
 assertFalse(tagFrame.canContain(tagFrame)); // frame cannot contain frame
 assertFalse(tagNoFrames.canContain(tagFrame)); // noframes cannot contain frame
 }

}