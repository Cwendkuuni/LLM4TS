import org.junit.Test;
import org.jsoup.parser.Tag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

 @Test
 public void testValueOfExistingTag() {
 Tag tag = Tag.valueOf("div");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testValueOfNewTag() {
 Tag tag = Tag.valueOf("newTag");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testCanContainBlockTag() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertTrue(pTag.canContain(divTag));
 }

 @Test
 public void testCannotContainBlockTagInsideBlockTag() {
 Tag divTag = Tag.valueOf("div");
 Tag divTag2 = Tag.valueOf("div");
 assertFalse(divTag.canContain(divTag2));
 }

 @Test
 public void testCanContainInlineTag() {
 Tag pTag = Tag.valueOf("p");
 Tag spanTag = Tag.valueOf("span");
 assertTrue(pTag.canContain(spanTag));
 }

 @Test
 public void testCannotContainInlineTagInsideBlockTag() {
 Tag divTag = Tag.valueOf("div");
 Tag spanTag = Tag.valueOf("span");
 assertFalse(divTag.canContain(spanTag));
 }

 @Test
 public void testCanContainSameTag() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.canContain(pTag));
 }

 @Test
 public void testContainHeadTags() {
 Tag headTag = Tag.valueOf("head");
 Tag scriptTag = Tag.valueOf("script");
 assertTrue(headTag.canContain(scriptTag));
 }

 @Test
 public void testCannotContainDTinsideDD() {
 Tag dlTag = Tag.valueOf("dl");
 Tag dtTag = Tag.valueOf("dt");
 Tag ddTag = Tag.valueOf("dd");
 assertFalse(dtTag.canContain(ddTag));
 }

 @Test
 public void testCannotContainDDinsideDT() {
 Tag dlTag = Tag.valueOf("dl");
 Tag dtTag = Tag.valueOf("dt");
 Tag ddTag = Tag.valueOf("dd");
 assertFalse(ddTag.canContain(dtTag));
 }

 @Test
 public void testCannotContainParentTagInsideChildTag() {
 Tag divTag = Tag.valueOf("div");
 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.canContain(divTag));
 }

 @Test
 public void testGetImplicitParent() {
 Tag pTag = Tag.valueOf("p");
 Tag bodyTag = pTag.getImplicitParent();
 assertTrue(bodyTag.isBlock());
 }

 @Test
 public void testGetImplicitParentWhenEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag parentTag = imgTag.getImplicitParent();
 assertNull(parentTag);
 }

 @Test
 public void testIsValidParent() {
 Tag divTag = Tag.valueOf("div");
 Tag pTag = Tag.valueOf("p");
 assertTrue(divTag.isValidParent(pTag));
 }

 @Test
 public void testIsValidParentWhenChildHasAncestors() {
 Tag divTag = Tag.valueOf("div");
 Tag pTag = Tag.valueOf("p");
 pTag.ancestors.add(Tag.valueOf("body"));
 assertFalse(divTag.isValidParent(pTag));
 }
}