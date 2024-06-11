package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testTagNormalName() {
 Tag tag = Tag.valueOf("P");
 assertEquals("p", tag.getName()); // case insensitive
 }

 @Test
 public void testUnknownTag() {
 Tag tag = Tag.valueOf("unknownTag");
 assertNotNull(tag);
 assertTrue(tag.isInline()); // default to inline for unknown tags
 }

 @Test
 public void testBlockTag() {
 Tag tag = Tag.valueOf("div");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testInlineTag() {
 Tag tag = Tag.valueOf("span");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testEmptyTag() {
 Tag tag = Tag.valueOf("img");
 assertFalse(tag.isEmpty());
 Tag emptyTag = Tag.valueOf("img").setSelfClosing();
 assertTrue(emptyTag.isEmpty());
 }

 @Test
 public void testSelfClosingTag() {
 Tag tag = Tag.valueOf("img");
 assertFalse(tag.isSelfClosing());
 Tag selfClosingTag = tag.setSelfClosing();
 assertTrue(selfClosingTag.isSelfClosing());
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag tag = Tag.valueOf("pre");
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testFormListedTag() {
 Tag tag = Tag.valueOf("input");
 assertTrue(tag.isFormListed());
 }

 @Test
 public void testFormSubmittableTag() {
 Tag tag = Tag.valueOf("input");
 assertTrue(tag.isFormSubmittable());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = Tag.valueOf(null); // should throw NullPointerException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = Tag.valueOf(""); // should throw IllegalArgumentException
 }
}