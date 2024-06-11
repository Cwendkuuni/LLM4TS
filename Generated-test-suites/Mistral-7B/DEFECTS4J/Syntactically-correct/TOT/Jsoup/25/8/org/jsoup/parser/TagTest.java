package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testValueOfBlockTag() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 assertTrue(tag.canContainBlock());
 assertFalse(tag.isInline());
 assertFalse(tag.isData());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertEquals("div", tag.getName());
 }

 @Test
 public void testValueOfUnknownTag() {
 String tagName = "xhtml";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 assertTrue(tag.canContainBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.isInline());
 assertFalse(tag.isData());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertEquals("xhtml", tag.getName());
 }

 @Test
 public void testValueOfEmptyTag() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline);
 assertTrue(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertEquals("img", tag.getName());
 }

 @Test
 public void testValueOfSelfClosingTag() {
 String tagName = "input";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline);
 assertFalse(tag.isEmpty());
 assertTrue(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertEquals("input", tag.getName());
 }

 @Test
 public void testValueOfPreserveWhitespaceTag() {
 String tagName = "pre";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline);
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertTrue(tag.preserveWhitespace());
 assertEquals("pre", tag.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfNullTagName() {
 String tagName = null;
 Tag tag = Tag.valueOf(tagName);
 fail("Should throw IllegalArgumentException for null tagName.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfEmptyTagName() {
 String tagName = "";
 Tag tag = Tag.valueOf(tagName);
 fail("Should throw IllegalArgumentException for empty tagName.");
 }

 @Test
 public void testIsKnownTag() {
 String tagName = "div";
 assertTrue(Tag.isKnownTag(tagName));
 }

 @Test
 public void testIsKnownTagUnknown() {
 String tagName = "xhtml";
 assertFalse(Tag.isKnownTag(tagName));
 }
}