import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testgetValueOf() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);
 String actualTagName = tag.getName();
 String expectedTagName = "div";
 assertEquals(expectedTagName, actualTagName);
 }

 @Test
 public void testIsBlockForDiv() {
 Tag tag = Tag.valueOf("div");
 boolean isBlock = tag.isBlock();
 boolean expectedIsBlock = true;
 assertEquals(expectedIsBlock, isBlock);
 }

 @Test
 public void testIsBlockForSpan() {
 Tag tag = Tag.valueOf("span");
 boolean isBlock = tag.isBlock();
 boolean expectedIsBlock = false;
 assertEquals(expectedIsBlock, isBlock);
 }

 @Test
 public void testCanContainBlockForDiv() {
 Tag divTag = Tag.valueOf("div");
 boolean canContainBlock = divTag.canContainBlock();
 boolean expectedCanContainBlock = true;
 assertEquals(expectedCanContainBlock, canContainBlock);
 }

 @Test
 public void testCanContainBlockForBr() {
 Tag brTag = Tag.valueOf("br");
 boolean canContainBlock = brTag.canContainBlock();
 boolean expectedCanContainBlock = false;
 assertEquals(expectedCanContainBlock, canContainBlock);
 }

 @Test
 public void testIsEmptyForImg() {
 Tag imgTag = Tag.valueOf("img");
 boolean empty = imgTag.isEmpty();
 boolean expectedEmpty = false;
 assertEquals(expectedEmpty, empty);
 }

 @Test
 public void testIsEmptyForInput() {
 Tag inputTag = Tag.valueOf("input");
 boolean empty = inputTag.isEmpty();
 boolean expectedEmpty = false;
 assertEquals(expectedEmpty, empty);
 }

 @Test
 public void testIsSelfClosingForImg() {
 Tag imgTag = Tag.valueOf("img");
 boolean selfClosing = imgTag.isSelfClosing();
 boolean expectedSelfClosing = false;
 assertEquals(expectedSelfClosing, selfClosing);
 }

 @Test
 public void testIsSelfClosingForInput() {
 Tag inputTag = Tag.valueOf("input");
 boolean selfClosing = inputTag.isSelfClosing();
 boolean expectedSelfClosing = false;
 assertEquals(expectedSelfClosing, selfClosing);
 }

 @Test
 public void testIsSelfClosingForImgWithSelfClosingAttribute() {
 String tagName = "img";
 String selfClosingAttribute = "selfclosing";
 Tag tag = new Tag(tagName);
 tag.selfClosing = true;
 String actualSelfClosing = tag.isSelfClosing();
 boolean expectedSelfClosing = true;
 assertEquals(expectedSelfClosing, actualSelfClosing);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testgetValueOfForNullTagName() {
 String tagName = null;
 Tag tag = Tag.valueOf(tagName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testgetValueOfForEmptyTagName() {
 String tagName = "";
 Tag tag = Tag.valueOf(tagName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testgetValueOfForInvalidTagName() {
 String tagName = "&tagName";
 Tag tag = Tag.valueOf(tagName);
 }

 @Test
 public void testIsKnownTagForDiv() {
 boolean isKnown = Tag.isKnownTag("div");
 boolean expectedIsKnown = true;
 assertEquals(expectedIsKnown, isKnown);
 }

 @Test
 public void testIsKnownTagForUnknownTag() {
 boolean isKnown = Tag.isKnownTag("&tagName");
 boolean expectedIsKnown = false;
 assertEquals(expectedIsKnown, isKnown);
 }

 @Test
 public void testPreserveWhitespaceForPre() {
 Tag preTag = Tag.valueOf("pre");
 boolean preserveWhitespace = preTag.preserveWhitespace();
 boolean expectedPreserveWhitespace = true;
 assertEquals(expectedPreserveWhitespace, preserveWhitespace);
 }

 @Test
 public void testPreserveWhitespaceForP() {
 Tag pTag = Tag.valueOf("p");
 boolean preserveWhitespace = pTag.preserveWhitespace();
 boolean expectedPreserveWhitespace = false;
 assertEquals(expectedPreserveWhitespace, preserveWhitespace);
 }
}