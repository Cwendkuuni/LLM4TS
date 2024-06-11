import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

 @Test
 public void testValueOfValidTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.canContain(Tag.valueOf("br")));
 }

 @Test
 public void testValueOfUnknownTag() {
 Tag tag = Tag.valueOf(" unknownTag ");
 assertFalse(tag.canContain(Tag.valueOf("p")));
 }

 @Test
 public void testCanContainBlockLevelChild() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertTrue(pTag.canContain(divTag));
 assertTrue(divTag.canContain(pTag));

 Tag headTag = Tag.valueOf("head");
 assertFalse(headTag.canContain(divTag));
 }

 @Test
 public void testCanContainInlineChild() {
 Tag pTag = Tag.valueOf("p");
 Tag brTag = Tag.valueOf("br");

 assertTrue(pTag.canContain(brTag));
 assertFalse(brTag.canContain(pTag));
 }

 @Test
 public void testNestedBlockLevelTags() {
 Tag pTag1 = Tag.valueOf("p");
 Tag divTag1 = Tag.valueOf("div");
 Tag pTag2 = Tag.valueOf("p");

 assertTrue(pTag1.canContain(divTag1));
 assertTrue(divTag1.canContain(pTag2));
 }

 @Test
 public void testEmptyTag() {
 Tag imgTag = Tag.valueOf("img");
 assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testDataOnlyTag() {
 Tag scriptTag = Tag.valueOf("script");
 scriptTag.setContainDataOnly();
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 preTag.setPreserveWhitespace();
 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testCannotContainSelf() {
 Tag aTag = Tag.valueOf("a");
 assertFalse(aTag.canContain(aTag));
 }

 @Test
 public void testInvalidParentChild() {
 Tag divTag = Tag.valueOf("div");
 Tag pTag = Tag.valueOf("p");

 assertFalse(divTag.canContain(pTag));
 assertFalse(pTag.canContain(divTag));
 }

 @Test
 public void testContainAncestor() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag bodyTag = Tag.valueOf("body");
 Tag headTag = Tag.valueOf("head");

 assertTrue(pTag.isValidParent(divTag));
 assertTrue(divTag.isValidParent(bodyTag));
 assertTrue(bodyTag.isValidParent(headTag));
 assertFalse(headTag.isValidParent(pTag));
 }

 @Test
 public void testTagNameComparison() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("P");

 assertTrue(pTag1.equals(pTag2));
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