package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Tag;
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
 public void testValueOfEmptyTag() {
 String tagName = "img";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertTrue(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertEquals("img", tag.getName());
 }

 @Test
 public void testValueOfUnknownTag() {
 String tagName = "xhtml";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isBlock());
 assertTrue(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertEquals("xhtml", tag.getName());
 }

 @Test
 public void testIsKnownTag() {
 String tagName1 = "div";
 String tagName2 = "xhtml";
 assertTrue(Tag.isKnownTag(tagName1));
 assertFalse(Tag.isKnownTag(tagName2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfNullTagName() {
 String tagName = null;
 Tag tag = null;
 try {
 tag = Tag.valueOf(tagName);
 } catch (IllegalArgumentException e) {
 tag = e.getCause().getCause();
 }
 assertNotNull(tag);
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertEquals("", tag.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfEmptyTagName() {
 String tagName = "";
 Tag tag = Tag.valueOf(tagName);
 assertNotNull(tag);
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertEquals("", tag.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueOfLongTagName() {
 String tagName = "this_tag_name_is_too_long_to_be_a_valid_html_tag_name_and_cannot_be_used_to_instantiate_a_Tag_object";
 Tag tag = null;
 try {
 tag = Tag.valueOf(tagName);
 } catch (IllegalArgumentException e) {
 tag = e.getCause().getCause();
 }
 assertNotNull(tag);
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 assertFalse(tag.canContainBlock());
 assertFalse(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 assertEquals("", tag.getName());
 }
}