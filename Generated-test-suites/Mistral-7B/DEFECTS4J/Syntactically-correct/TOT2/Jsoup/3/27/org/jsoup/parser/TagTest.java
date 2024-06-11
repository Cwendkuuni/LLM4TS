import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TagTest {

 // Test for getName() method
 @Test
 public void testTagName() {
 Tag tag = new Tag("div");
 Assert.assertEquals("div", tag.getName());
 }

 // Test for valueOf() method with known tag
 @Test
 public void testValueOfKnownTag() {
 Tag tag = Tag.valueOf("p");
 Assert.assertEquals("p", tag.getName());
 }

 // Test for valueOf() method with unknown tag
 @Test
 public void testValueOfUnknownTag() {
 Tag tag = Tag.valueOf("XYZ");
 Assert.assertNotSame(Tag.defaultAncestor, tag);
 Assert.assertEquals("XYZ", tag.getName());
 }

 // Test for canContain() method with valid child
 @Test
 public void testCanContainValidChild() {
 Tag parent = Tag.valueOf("div");
 Tag child = Tag.valueOf("p");
 Assert.assertTrue(parent.canContain(child));
 }

 // Test for canContain() method with invalid child
 @Test
 public void testCanContainInvalidChild() {
 Tag parent = Tag.valueOf("div");
 Tag child = Tag.valueOf("div");
 Assert.assertFalse(parent.canContain(child));
 }

 // Test for isBlock() method
 @Test
 public void testIsBlock() {
 Tag tag = new Tag("div");
 Assert.assertTrue(tag.isBlock());
 }

 // Test for canContainBlock() method
 @Test
 public void testCanContainBlock() {
 Tag tag = new Tag("div");
 Assert.assertTrue(tag.canContainBlock());
 }

 // Test for isInline() method
 @Test
 public void testIsInline() {
 Tag tag = new Tag("span");
 Assert.assertFalse(tag.isBlock());
 Assert.assertTrue(tag.isInline());
 }

 // Test for isData() method
 @Test
 public void testIsData() {
 Tag tag = new Tag("script");
 Assert.assertTrue(tag.isData());
 }

 // Test for isEmpty() method
 @Test
 public void testIsEmpty() {
 Tag tag = new Tag("img");
 Assert.assertTrue(tag.isEmpty());
 }

 // Test for preserveWhitespace() method
 @Test
 public void testPreserveWhitespace() {
 Tag tag = new Tag("pre");
 Assert.assertTrue(tag.preserveWhitespace());
 }

 // Test for getImplicitParent() method
 @Test
 public void testGetImplicitParent() {
 Tag tag = new Tag("div");
 Tag parent = tag.getImplicitParent();
 Assert.assertNull(parent);
 }

 // Test for isValidParent() method with valid child
 @Test
 public void testIsValidParentValidChild() {
 Tag parent = Tag.valueOf("div");
 Tag child = Tag.valueOf("p");
 Assert.assertTrue(parent.isValidParent(child));
 }

 // Test for isValidParent() method with invalid child
 @Test
 public void testIsValidParentInvalidChild() {
 Tag parent = Tag.valueOf("div");
 Tag child = parent;
 Assert.assertFalse(parent.isValidParent(child));
 }

 // Test for equals() method with same object
 @Test
 public void testEqualsSameObject() {
 Tag tag1 = new Tag("div");
 Tag tag2 = tag1;
 Assert.assertTrue(tag1.equals(tag2));
 }

 // Test for equals() method with same value
 @Test
 public void testEqualsSameValue() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 Assert.assertTrue(tag1.equals(tag2));
 }

 // Test for equals() method with different value
 @Test
 public void testEqualsDifferentValue() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("p");
 Assert.assertFalse(tag1.equals(tag2));
 }

 // Test for hashCode() method with same value
 @Test
 public void testHashCodeSameValue() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("div");
 Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 // Test for hashCode() method with different value
 @Test
 public void testHashCodeDifferentValue() {
 Tag tag1 = new Tag("div");
 Tag tag2 = new Tag("p");
 Assert.assertNotEquals(tag1.hashCode(), tag2.hashCode());
 }

 // Test for toString() method
 @Test
 public void testToString() {
 Tag tag = new Tag("div");
 Assert.assertEquals("div", tag.toString());
 }

 // Test for createBlock() method
 @Test
 public void testCreateBlock() {
 Tag htmlTag = Tag.createBlock("HTML");
 Tag headTag = htmlTag.createBlock("HEAD");
 Tag bodyTag = htmlTag.createBlock("BODY");
 Tag pTag = bodyTag.createInline("P");
 Assert.assertEquals("BODY", pTag.getImplicitParent().getName());
 }

 // Test for createInline() method
 @Test
 public void testCreateInline() {
 Tag tag = Tag.createInline("span");
 Assert.assertEquals("span", tag.getName());
 Assert.assertFalse(tag.isBlock());
 Assert.assertTrue(tag.isInline());
 }

 // Test for register() method
 @Test
 public void testRegister() {
 Tag tag = new Tag("div");
 Tag registeredTag = Tag.register(tag);
 Assert.assertEquals(tag.getName(), registeredTag.getName());
 }

 // Test for setContainInlineOnly() method
 @Test
 public void testSetContainInlineOnly() {
 Tag tag = new Tag("div");
 tag.setContainInlineOnly();
 Assert.assertFalse(tag.canContainBlock());
 Assert.assertTrue(tag.canContainInline());
 }

 // Test for setContainDataOnly() method
 @Test
 public void testSetContainDataOnly() {
 Tag tag = new Tag("script");
 tag.setContainDataOnly();
 Assert.assertFalse(tag.canContainBlock());
 Assert.assertFalse(tag.canContainInline());
 Assert.assertTrue(tag.preserveWhitespace);
 }

 // Test for setEmpty() method
 @Test
 public void testSetEmpty() {
 Tag tag = new Tag("img");
 tag.setEmpty();
 Assert.assertFalse(tag.canContainBlock());
 Assert.assertFalse(tag.canContainInline());
 Assert.assertTrue(tag.isEmpty());
 }

 // Test for setOptionalClosing() method
 @Test
 public void testSetOptionalClosing() {
 Tag tag = new Tag("span");
 tag.setOptionalClosing();
 Assert.assertTrue(tag.optionalClosing);
 }

 // Test for setPreserveWhitespace() method
 @Test
 public void testSetPreserveWhitespace() {
 Tag tag = new Tag("pre");
 tag.setPreserveWhitespace();
 Assert.assertTrue(tag.preserveWhitespace);
 }

 // Test for setAncestor() method with empty list
 @Test
 public void testSetAncestorEmptyList() {
 Tag tag = new Tag("div");
 tag.setAncestor();
 List<Tag> ancestors = tag.getAncestors();
 Assert.assertEquals(Collections.emptyList(), ancestors);
 }

 // Test for setAncestor() method with specific ancestors
 @Test
 public void testSetAncestorSpecificAncestors() {
 Tag tag = new Tag("div");
 List<String> ancestorNames = Arrays.asList("html", "body", "header");
 tag.setAncestor(ancestorNames.toArray(new String[ancestorNames.size()]));
 List<Tag> ancestors = tag.getAncestors();
 Assert.assertEquals(ancestors.size(), ancestorNames.size());
 for (int i = 0; i < ancestorNames.size(); i++) {
 Assert.assertEquals(ancestorNames.get(i), ancestors.get(i).getName());
 }
 }

 // Test for getAncestors() method with no ancestors
 @Test
 public void testGetAncestorsNoAncestors() {
 Tag tag = new Tag("div");
 List<Tag> ancestors = tag.getAncestors();
 Assert.assertEquals(Collections.emptyList(), ancestors);
 }

 // Test for getAncestors() method with ancestors
 @Test
 public void testGetAncestorsWithAncestors() {
 Tag htmlTag = Tag.createBlock("HTML");
 Tag headTag = htmlTag.createBlock("HEAD");
 Tag bodyTag = htmlTag.createBlock("BODY");
 Tag divTag = bodyTag.createBlock("DIV");
 Tag pTag = bodyTag.createInline("P");
 divTag.setAncestor(headTag.getName(), bodyTag.getName(), "footer");
 List<Tag> ancestors = divTag.getAncestors();
 Assert.assertEquals(ancestors.size(), 3);
 Assert.assertEquals(headTag.getName(), ancestors.get(0).getName());
 Assert.assertEquals(bodyTag.getName(), ancestors.get(1).getName());
 Assert.assertEquals(divTag.getName(), ancestors.get(2).getName());
 }

 // Test for canContain() method with empty tag
 @Test
 public void testCanContainEmptyTag() {
 Tag parent = Tag.valueOf("div");
 Tag child = Tag.createEmpty("img");
 Assert.assertFalse(parent.canContain(child));
 }

 // Test for canContain() method with empty parent tag
 @Test
 public void testCanContainEmptyParentTag() {
 Tag parent = Tag.createEmpty("div");
 Tag child = Tag.valueOf("p");
 Assert.assertFalse(parent.canContain(child));
 }

 // Test for canContain() method with parent and child having the same name
 @Test
 public void testCanContainSameNamedParentAndChild() {
 Tag parent = Tag.createInline("span");
 Tag child = parent;
 Assert.assertFalse(parent.canContain(child));
 }

 // Test for canContain() method with parent and child having different names but not allowed to be contained
 @Test
 public void testCanContainDifferentNamedParentAndChildNotAllowed() {
 Tag parent = Tag.valueOf("div");
 Tag child = Tag.valueOf("table");
 Assert.assertFalse(parent.canContain(child));
 }

 // Test for canContain() method with parent and child having different names and allowed to be contained
 @Test
 public void testCanContainDifferentNamedParentAndChildAllowed() {
 Tag parent = Tag.valueOf("div");
 Tag child = Tag.valueOf("p");
 Assert.assertTrue(parent.canContain(child));
 }

 // Test for canContain() method with parent and child having different names, but parent cannot contain child and child cannot contain parent
 @Test
 public void testCanContainDifferentNamedParentAndChildCannotContainEachOther() {
 Tag parent = Tag.valueOf("div");
 Tag child = Tag.valueOf("table");
 Assert.assertFalse(parent.canContain(child));
 Assert.assertFalse(child.canContain(parent));
 }
}