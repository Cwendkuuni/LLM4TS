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
 public void testTagCaseInsensitive() {
 Tag tag = Tag.valueOf("P");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testTagNormalization() {
 Tag tag = Tag.valueOf("DIV", ParseSettings.stripLeadingAndTrailingWhitespace);
 assertEquals("div", tag.getName());
 }

 @Test
 public void testUnknownTag() {
 Tag tag = Tag.valueOf("X", ParseSettings.preserveCase);
 assertNotNull(tag);
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testBlockTag() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isBlock());
 }

 @Test
 public void testInlineTag() {
 Tag tag = Tag.valueOf("img");
 assertFalse(tag.isBlock());
 }

 @Test
 public void testEmptyTag() {
 Tag tag = Tag.valueOf("img");
 tag.empty = true;
 assertTrue(tag.isEmpty());
 }

 @Test
 public void testSelfClosingTag() {
 Tag tag = Tag.valueOf("input");
 tag.selfClosing = true;
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testPreserveWhitespaceTag() {
 Tag tag = Tag.valueOf("pre");
 tag.preserveWhitespace = true;
 assertTrue(tag.preserveWhitespace());
 }

 @Test
 public void testFormListTag() {
 Tag tag = Tag.valueOf("input");
 tag.formList = true;
 assertTrue(tag.isFormListed());
 }

 @Test
 public void testFormSubmittableTag() {
 Tag tag = Tag.valueOf("input");
 tag.formSubmit = true;
 assertTrue(tag.isFormSubmittable());
 }

 @Test
 public void testFormListAndFormSubmittableTag() {
 Tag tag = Tag.valueOf("input");
 tag.formList = true;
 tag.formSubmit = true;
 assertTrue(tag.isFormSubmittable());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag tag = Tag.valueOf(null, ParseSettings.preserveCase);
 fail("Should throw NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = Tag.valueOf("", ParseSettings.preserveCase);
 fail("Should throw IllegalArgumentException");
 }
}