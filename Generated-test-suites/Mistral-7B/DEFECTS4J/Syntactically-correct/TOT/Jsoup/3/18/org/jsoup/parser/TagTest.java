package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagPtwice = Tag.valueOf("p");
 Tag tagUnknown = Tag.valueOf("X");

 Assert.assertEquals("p", tagP.getName());
 Assert.assertEquals("div", tagDiv.getName());
 Assert.assertNotSame(tagP, tagPtwice);
 Assert.assertNotEquals("p", tagUnknown.getName());
 Assert.assertFalse(tagUnknown.isBlock());
 }

 @Test
 public void testCanContain() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagSpan = Tag.valueOf("span");
 Tag tagPagain = Tag.valueOf("p");

 Assert.assertTrue(tagP.canContain(tagSpan));
 Assert.assertTrue(tagDiv.canContain(tagP));
 Assert.assertTrue(tagDiv.canContain(tagDiv)); // Div can contain itself as a nested element
 Assert.assertTrue(tagP.canContain(tagPagain));
 Assert.assertFalse(tagP.canContain(tagDiv)); // P cannot contain div directly
 Assert.assertFalse(tagP.canContain(tagP)); // P cannot contain itself directly
 }

 @Test
 public void testIsData() {
 Tag tagP = Tag.valueOf("p");
 Tag tagScript = Tag.valueOf("script");
 Tag tagMeta = Tag.valueOf("meta");
 Tag tagEmptyP = Tag.valueOf("p");
 Tag tagEmpty = Tag.valueOf("");

 Assert.assertFalse(tagP.isData());
 Assert.assertTrue(tagScript.isData());
 Assert.assertTrue(tagMeta.isData());
 Assert.assertFalse(tagEmptyP.isData());
 Assert.assertTrue(tagEmpty.isData());
 }

 @Test
 public void testIsValidParent() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagSpan = Tag.valueOf("span");
 Tag tagP2 = Tag.valueOf("p");
 Tag tagHead = Tag.valueOf("head");

 Assert.assertTrue(tagP.isValidParent(tagSpan));
 Assert.assertTrue(tagDiv.isValidParent(tagP));
 Assert.assertFalse(tagP.isValidParent(tagDiv));
 Assert.assertFalse(tagHead.isValidParent(tagP));
 Assert.assertTrue(tagP2.isValidParent(tagSpan));
 }

 @Test
 public void testEqualsAndHashcode() {
 Tag tagP1 = Tag.valueOf("p");
 Tag tagP2 = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagP3 = new Tag("p");

 Assert.assertEquals(tagP1, tagP2);
 Assert.assertNotEquals(tagP1, tagDiv);
 Assert.assertNotEquals(tagP1, tagP3);

 int hashcodeP1 = tagP1.hashCode();
 int hashcodeP2 = tagP2.hashCode();
 int hashcodeP3 = tagP3.hashCode();
 int hashcodeDiv = tagDiv.hashCode();

 Assert.assertEquals(hashcodeP1, hashcodeP2);
 Assert.assertNotEquals(hashcodeP1, hashcodeDiv);
 Assert.assertNotEquals(hashcodeP1, hashcodeP3);
 }

 @Test
 public void testToString() {
 Tag tagP = Tag.valueOf("p");

 Assert.assertEquals("p", tagP.toString());
 }

 @Test
 public void testCreateBlockAndCreateInline() {
 Tag tagHTML = Tag.createBlock("HTML");
 Tag tagHead = Tag.createBlock("HEAD");
 Tag tagP = Tag.createBlock("P");
 Tag tagSpan = Tag.createInline("SPAN");

 Assert.assertEquals("HTML", tagHTML.getName());
 Assert.assertEquals("HEAD", tagHead.getName());
 Assert.assertEquals("P", tagP.getName());
 Assert.assertEquals("SPAN", tagSpan.getName());
 Assert.assertTrue(tagP.isBlock());
 Assert.assertFalse(tagSpan.isBlock());
 }

 @Test
 public void testRegister() {
 Tag tagP = new Tag("p");
 Tag tagDiv = new Tag("div");

 Tag.register(tagP);
 Tag.register(tagDiv);

 Tag tagPFromRegister = Tag.valueOf("p");
 Tag tagDivFromRegister = Tag.valueOf("div");

 Assert.assertEquals(tagP, tagPFromRegister);
 Assert.assertEquals(tagDiv, tagDivFromRegister);
 }

 @Test
 public void testSetContainInlineOnly() {
 Tag tagP = Tag.valueOf("p");

 Tag tagPContainInlineOnly = tagP.setContainInlineOnly();

 Assert.assertFalse(tagPContainInlineOnly.canContainBlock());
 Assert.assertTrue(tagPContainInlineOnly.canContainInline());
 }

 @Test
 public void testSetContainDataOnly() {
 Tag tagScript = Tag.valueOf("script");

 Tag tagScriptContainDataOnly = tagScript.setContainDataOnly();

 Assert.assertFalse(tagScriptContainDataOnly.canContainBlock());
 Assert.assertFalse(tagScriptContainDataOnly.canContainInline());
 Assert.assertTrue(tagScriptContainDataOnly.preserveWhitespace());
 }

 @Test
 public void testSetEmpty() {
 Tag tagP = Tag.valueOf("p");

 Tag tagPEmpty = tagP.setEmpty();

 Assert.assertFalse(tagPEmpty.canContainBlock());
 Assert.assertFalse(tagPEmpty.canContainInline());
 Assert.assertTrue(tagPEmpty.isEmpty());
 }

 @Test
 public void testSetOptionalClosing() {
 Tag tagP = Tag.valueOf("p");

 Tag tagPOptClosing = tagP.setOptionalClosing();

 Assert.assertTrue(tagPOptClosing.optionalClosing);
 }

 @Test
 public void testSetPreserveWhitespace() {
 Tag tagPre = Tag.valueOf("pre");

 Tag tagPrePreserveWhitespace = tagPre.setPreserveWhitespace();

 Assert.assertTrue(tagPrePreserveWhitespace.preserveWhitespace);
 }

 @Test
 public void testSetAncestor() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagHead = Tag.valueOf("head");

 List<String> tagNames = Arrays.asList("head", "body");
 List<Tag> tagList = Arrays.asList(tagHead, tagP);

 Tag tagPWithAncestor = tagP.setAncestor(tagNames.toArray(new String[0]));
 Tag tagPWithAncestorList = tagP.setAncestor(tagList);

 Assert.assertArrayEquals(tagNames.toArray(new String[0]), tagPWithAncestor.ancestors.toArray(new String[0]));
 Assert.assertSame(tagList, tagPWithAncestorList.ancestors);
 }
}