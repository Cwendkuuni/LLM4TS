package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValueOf() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 String expectedTagName = "p";
 assertEquals(expectedTagName, tag.getName());

 tagName = "DIV"; // case insensitive
 Tag tagFromCaseInsensitiveName = Tag.valueOf(tagName);
 assertEquals(expectedTagName, tagFromCaseInsensitiveName.getName());

 tagName = "UnknownTag";
 Tag tagFromUnknownName = Tag.valueOf(tagName);
 assertNotNull(tagFromUnknownName);
 assertEquals("UnknownTag", tagFromUnknownName.getName());
 }

 @Test
 public void testTagIsBlock() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagImg = Tag.valueOf("img");

 assertTrue(tagP.isBlock());
 assertTrue(tagDiv.isBlock());
 assertFalse(tagImg.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagBr = Tag.valueOf("br");

 assertTrue(tagP.formatAsBlock());
 assertTrue(tagDiv.formatAsBlock());
 assertFalse(tagBr.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagBr = Tag.valueOf("br");

 assertTrue(tagDiv.canContainBlock());
 assertTrue(tagDiv.canContainInline());
 assertFalse(tagBr.canContainBlock());
 assertFalse(tagBr.canContainInline());
 }

 @Test
 public void testTagIsEmpty() {
 Tag tagImg = Tag.valueOf("img");
 Tag tagInput = Tag.valueOf("input");

 assertFalse(tagImg.isEmpty());
 assertFalse(tagInput.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag tagImg = Tag.valueOf("img");
 Tag tagInput = Tag.valueOf("input");

 assertFalse(tagImg.isSelfClosing());
 assertFalse(tagInput.isSelfClosing());

 // self closing tag "img"
 Tag tagSelfClosingImg = tagImg.setSelfClosing();
 assertTrue(tagSelfClosingImg.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag tagP = Tag.valueOf("p");
 Tag tagDiv = Tag.valueOf("div");
 Tag tagUnknown = Tag.valueOf("UnknownTag");

 assertTrue(tagP.isKnownTag());
 assertTrue(tagDiv.isKnownTag());
 assertFalse(tagUnknown.isKnownTag());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag tagPre = Tag.valueOf("pre");
 Tag tagTextarea = Tag.valueOf("textarea");

 assertTrue(tagPre.preserveWhitespace());
 assertTrue(tagTextarea.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag tagButton = Tag.valueOf("button");
 Tag tagInput = Tag.valueOf("input");
 Tag tagTextarea = Tag.valueOf("textarea");

 assertTrue(tagButton.isFormListed());
 assertTrue(tagInput.isFormListed());
 assertTrue(tagTextarea.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag tagInput = Tag.valueOf("input");
 Tag tagButton = Tag.valueOf("button");

 assertTrue(tagButton.isFormSubmittable());
 assertTrue(tagInput.isFormSubmittable());
 }
}