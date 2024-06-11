package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.Tag.blockTags;
import static org.jsoup.parser.Tag.emptyTags;
import static org.jsoup.parser.Tag.inlineTags;
import static org.jsoup.parser.Tag.isKnownTag;
import static org.jsoup.parser.Tag.valueOf;

public class TagTest {

 @Test
 public void testGetName() {
 String tagName = "div";
 Tag tag = new Tag(tagName);
 String name = tag.getName();
 assertEquals(tagName, name);
 }

 @Test
 public void testValueOf() {
 String tagName = "p";
 Tag expectedTag = new Tag(tagName);
 Tag tag = Tag.valueOf(tagName);
 assertEquals(expectedTag, tag);
 }

 @Test
 public void testValueOfUnknownTag() {
 String tagName = "asdf";
 Tag tag = Tag.valueOf(tagName);
 assertNotNull(tag);
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testIsBlock() {
 Tag tag = new Tag("div");
 assertTrue(tag.isBlock());

 tag = new Tag("span");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag tag = new Tag("div");
 assertTrue(tag.formatAsBlock());

 tag = new Tag("input");
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag tag = new Tag("div");
 assertTrue(tag.canContainBlock());

 tag = new Tag("input");
 assertFalse(tag.canContainBlock());
 }

 @Test
 public void testIsData() {
 Tag tag = new Tag("script");
 assertTrue(tag.isData());

 tag = new Tag("p");
 assertFalse(tag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag tag = new Tag("img");
 assertFalse(tag.isEmpty());

 tag = new Tag("br");
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag tag = new Tag("img");
 assertFalse(tag.isSelfClosing());

 tag = new Tag("img");
 tag.setSelfClosing();
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(isKnownTag("div"));
 assertFalse(isKnownTag("asdf"));
 }

 @Test
 public void testPreserveWhitespace() {
 Tag tag = new Tag("pre");
 assertTrue(tag.preserveWhitespace());

 tag = new Tag("p");
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testFormListed() {
 Tag tag = new Tag("input");
 assertTrue(tag.isFormListed());

 tag = new Tag("div");
 assertFalse(tag.isFormListed());
 }

 @Test
 public void testFormSubmittable() {
 Tag tag = new Tag("input");
 assertTrue(tag.isFormSubmittable());

 tag = new Tag("div");
 assertFalse(tag.isFormSubmittable());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfNullTagName() {
 Tag tag = Tag.valueOf(null);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfEmptyTagName() {
 Tag tag = Tag.valueOf("");
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testGetTagByName() {
 String tagName = "div";
 Tag expectedTag = new Tag(tagName);
 Tag tag = Tag.valueOf(tagName);
 Tag tagFromMap = Tag.valueOf(tagName);
 assertEquals(expectedTag, tag);
 assertEquals(expectedTag, tagFromMap);
 }

 @Test
 public void testGetBlockTags() {
 for (String tagName : blockTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 }
 }

 @Test
 public void testGetInlineTags() {
 for (String tagName : inlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 }
 }

 @Test
 public void testGetEmptyTags() {
 for (String tagName : emptyTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isEmpty());
 }
 }

 @Test
 public void testGetFormatAsInlineTags() {
 for (String tagName : formatAsInlineTags) {
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.formatAsBlock());
 }
 }

 @Test
 public void testGetPreserveWhitespaceTags() {
 for (String tagName : preserveWhitespaceTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.preserveWhitespace());
 }
 }

 @Test
 public void testGetFormListedTags() {
 for (String tagName : formListedTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isFormListed());
 }
 }

 @Test
 public void testGetFormSubmitTags() {
 for (String tagName : formSubmitTags) {
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isFormSubmittable());
 }
 }
}